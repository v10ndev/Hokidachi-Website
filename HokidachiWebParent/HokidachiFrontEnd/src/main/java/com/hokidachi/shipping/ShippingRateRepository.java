package com.hokidachi.shipping;

import org.springframework.data.repository.CrudRepository;

import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

    public ShippingRate findByCityAndDistrict(City city, String district);
}