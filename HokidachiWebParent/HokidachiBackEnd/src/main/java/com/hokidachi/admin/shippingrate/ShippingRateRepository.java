package com.hokidachi.admin.shippingrate;

import com.hokidachi.admin.paging.SearchRepository;
import com.hokidachi.common.entity.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShippingRateRepository extends SearchRepository<ShippingRate, Integer> {

    @Query("SELECT sr FROM ShippingRate sr WHERE sr.city.id = ?1 AND sr.district = ?2")
    public ShippingRate findByCityAndDistrict(Integer cityId, String district);

    @Query("UPDATE ShippingRate sr SET sr.codSupported = ?2 WHERE sr.id = ?1")
    @Modifying
    public void updateCODSupport(Integer id, boolean enabled);

    @Query("SELECT sr FROM ShippingRate sr WHERE sr.city.name LIKE %?1% OR sr.district LIKE %?1%")
    public Page<ShippingRate> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);
}
