package com.hokidachi.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hokidachi.common.entity.Address;
import com.hokidachi.common.entity.Customer;
import com.hokidachi.common.entity.ShippingRate;

@Service
public class ShippingRateService {

    @Autowired private ShippingRateRepository repo;

    public ShippingRate getShippingRateForCustomer(Customer customer) {
        String district = customer.getDistrict();
        if (district == null || district.isEmpty()) {
            district = customer.getVillage();
        }

        return repo.findByCityAndDistrict(customer.getCity(), district);
    }

    public ShippingRate getShippingRateForAddress(Address address) {
        String district = address.getDistrict();
        if (district == null || district.isEmpty()) {
            district = address.getVillage();
        }

        return repo.findByCityAndDistrict(address.getCity(), district);
    }
}