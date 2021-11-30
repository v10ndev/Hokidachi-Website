package com.hokidachi.setting;

import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.District;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DistrictRepository extends CrudRepository<District, Integer> {
    public List<District> findByCityOrderByNameAsc(City city);
}