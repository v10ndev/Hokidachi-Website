package com.hokidachi.shoppingcart;

import com.hokidachi.Utility;
import com.hokidachi.common.entity.Customer;
import com.hokidachi.common.exception.CustomerNotFoundException;
import com.hokidachi.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
    @Autowired private ShoppingCartService cartService;
    @Autowired private CustomerService customerService;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductCart(@PathVariable(name = "productId") Integer productId,
                                 @PathVariable("quantity") Integer quantity,
                                 HttpServletRequest request){
        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
            return updatedQuantity + " sản phẩm này đã được thêm vào giỏ hàng của bạn.";
        } catch (CustomerNotFoundException e) {
            return "Bạn phải đăng nhập để thêm sản phẩm này vào giỏ hàng.";
        } catch (ShoppingCartException ex){
            return ex.getMessage();
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request)
            throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticationCustomer(request);
        if(email == null){
            throw new CustomerNotFoundException("Không có khách hàng xác thực");
        }
        return customerService.getCustomerByEmail(email);
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateProductCart(@PathVariable(name = "productId") Integer productId,
                                 @PathVariable("quantity") Integer quantity,
                                 HttpServletRequest request){
        try {
            Customer customer = getAuthenticatedCustomer(request);
            float subtotal = cartService.updateQuantity(productId, quantity, customer);
            return String.valueOf(subtotal);
        } catch (CustomerNotFoundException e) {
            return "You must login to change quantity of product.";
        }
    }

    @DeleteMapping("/cart/remove/{productId}")
    public String removeProduct(@PathVariable(name = "productId") Integer productId,
                                HttpServletRequest request){
        try {
            Customer customer = getAuthenticatedCustomer(request);
            cartService.removeProduct(productId, customer);
            return "The product has been removed from your shopping cart.";
        } catch (CustomerNotFoundException e) {
            return "You must login to remove product.";
        }
    }
}
