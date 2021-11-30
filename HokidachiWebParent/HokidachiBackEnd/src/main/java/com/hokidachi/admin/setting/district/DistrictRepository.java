package com.hokidachi.admin.setting.district;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.District;

public interface DistrictRepository extends CrudRepository<District, Integer> {
    public List<District> findByCityOrderByNameAsc(City city);
}