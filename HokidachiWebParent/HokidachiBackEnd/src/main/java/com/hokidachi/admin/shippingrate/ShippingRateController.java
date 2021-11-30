package com.hokidachi.admin.shippingrate;

import com.hokidachi.admin.paging.PagingAndSortingHelper;
import com.hokidachi.admin.paging.PagingAndSortingParam;
import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.ShippingRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ShippingRateController {
    private String defaultRedirectURL = "redirect:/shipping_rates/page/1?sortField=city&sortDir=asc";

    @Autowired private ShippingRateService service;

    @GetMapping("/shipping_rates")
    public String listFirstPage(){
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "shippingRates",
                    moduleURL = "/shipping_rates")PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum){
        service.listByPage(pageNum, helper);
        return "shipping_rates/shipping_rates";
    }

    @GetMapping("/shipping_rates/new")
    public String newRate(Model model){
        List<City> listCities = service.listAllCities();

        model.addAttribute("rate", new ShippingRate());
        model.addAttribute("listCities", listCities);
        model.addAttribute("pageTitle", "Tỷ lệ mới");

        return "shipping_rates/shipping_rate_form";
    }

    @PostMapping("/shipping_rates/save")
    public String saveRate(ShippingRate rate, RedirectAttributes ra){
        try {
            service.save(rate);
            ra.addFlashAttribute("message", "Tỷ lệ vận chuyển đã được lưu thành công.");
        } catch (ShippingRateAlreadyExistsException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/edit/{id}")
    public String editRate(@PathVariable(name = "id") Integer id,
                           Model model, RedirectAttributes ra){
        try{
            ShippingRate rate = service.get(id);
            List<City> listCities = service.listAllCities();

            model.addAttribute("rate", rate);
            model.addAttribute("listCities", listCities);
            model.addAttribute("pageTitle", "Chỉnh sửa Tỷ lệ (ID: " + id + ")");

            return "shipping_rates/shipping_rate_form";
        } catch (ShippingRateNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
    public String updateCODSupport(@PathVariable(name = "id") Integer id,
                           @PathVariable(name = "supported") Boolean supported,
                           Model model, RedirectAttributes ra){
        try{
            service.updateCODSupport(id, supported);
            ra.addFlashAttribute("message", "Hỗ trợ COD cho tỷ lệ vận chuyển ID " + id + " đã được cập nhật");
        } catch (ShippingRateNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return defaultRedirectURL;
    }

    @GetMapping("/shipping_rates/delete/{id}")
    public String deleteRate(@PathVariable(name = "id") Integer id,
                             Model model, RedirectAttributes ra){
        try {
            service.delete(id);
            ra.addFlashAttribute("message", "Tỷ lệ vận chuyển ID " + id + " đã bị xóa.");
        } catch (ShippingRateNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return defaultRedirectURL;
    }
}
