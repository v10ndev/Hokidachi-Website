package com.hokidachi.security;

import com.hokidachi.common.entity.AuthenticationType;
import com.hokidachi.common.entity.Customer;
import com.hokidachi.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        Customer customer = userDetails.getCustomer();

        customerService.updateAuthenticationType(customer, AuthenticationType.DATABASE);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
