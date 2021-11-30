package com.hokidachi.admin.shippingrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingRateRestController {

    @Autowired private ShippingRateService service;

    @PostMapping("/get_shipping_cost")
    public String getShippingCost(Integer productId, Integer cityId, String district)
            throws ShippingRateNotFoundException {
        float shippingCost = service.calculateShippingCost(productId, cityId, district);
        return String.valueOf(shippingCost);
    }
}