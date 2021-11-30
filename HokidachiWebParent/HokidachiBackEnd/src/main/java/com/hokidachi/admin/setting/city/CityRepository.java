package com.hokidachi.admin.setting.city;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hokidachi.common.entity.City;

public interface CityRepository extends CrudRepository<City, Integer> {
    public List<City> findAllByOrderByNameAsc();
}