package com.hokidachi.admin.product;

import java.io.IOException;
import java.util.List;

import com.hokidachi.admin.FileUploadUtil;
import com.hokidachi.admin.brand.BrandService;
import com.hokidachi.admin.category.CategoryService;
import com.hokidachi.admin.paging.PagingAndSortingHelper;
import com.hokidachi.admin.paging.PagingAndSortingParam;
import com.hokidachi.admin.security.HokidachiUserDetails;
import com.hokidachi.common.entity.Brand;
import com.hokidachi.common.entity.Category;
import com.hokidachi.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hokidachi.common.entity.product.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listFirstPage() {
        return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(
            @PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
            @PathVariable(name="pageNum") int pageNum, Model model,
            @Param("categoryId") Integer categoryId) {
        productService.listByPage(pageNum, helper, categoryId);

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        if(categoryId != null) model.addAttribute("categoryId", categoryId);
        model.addAttribute("listCategories", listCategories);

        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> listBrands = brandService.listAll();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("pageTitle", "Tạo sản phẩm mới");
        model.addAttribute("numberOfExistingExtraImages", 0);

        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes ra,
                              @RequestParam(value="fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value="extraImage", required = false) MultipartFile[] extraImageMultiparts,
                              @RequestParam(name="detailIDs", required = false) String[] detailIDs,
                              @RequestParam(name="detailNames", required = false) String[] detailNames,
                              @RequestParam(name="detailValues", required = false) String[] detailValues,
                              @RequestParam(name="imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name="imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal HokidachiUserDetails loggedUser
                            ) throws IOException  {
        if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")){
            if(loggedUser.hasRole("Salesperson")) {
                productService.saveProductPrice(product);
                ra.addFlashAttribute("message", "Sản phẩm đã được lưu thành công.");
                return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
            }
        }

        ProductSaveHelper.setMainImageName(mainImageMultipart, product);
        ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
        ProductSaveHelper.setNewExtraImageName(extraImageMultiparts, product);
        ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);

        Product savedProduct = productService.save(product);

        ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

        ProductSaveHelper.deleteExtraImagesWeredRemoveOnForm(product);

        ra.addFlashAttribute("message", "Sản phẩm đã được lưu thành công.");

        return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id,
                                             @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {

        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The product ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) throws ProductNotFoundException {
        try {
            productService.delete(id);

            String productExtraImageDir = "product-images/" + id + "/extras";
            String productImageDir = "product-images/" + id;

            FileUploadUtil.removeDir(productExtraImageDir);
            FileUploadUtil.removeDir(productImageDir);

            redirectAttributes.addFlashAttribute("message", "Sản phẩm ID " + id + " đã bị xóa thành công");
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Integer id, Model model,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal HokidachiUserDetails loggedUser) {
        try {
            Product product = productService.get(id);
            List<Brand> listBrands = brandService.listAll();
            Integer numberOfExistingExtraImages = product.getImages().size();

            boolean isReadOnlyForSalesperson = false;
            if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")){
                if(loggedUser.hasRole("Salesperson")) {
                    isReadOnlyForSalesperson = true;
                }
            }

            model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
            model.addAttribute("product", product);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

            return "products/product_form";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
        }
    }

    @GetMapping("/products/details/{id}")
    public String viewProductDetails(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);

            model.addAttribute("product", product);

            return "products/product_detail_modal";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
        }

    }
}