package com.hokidachi.setting;

import com.hokidachi.common.entity.City;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Integer> {
    public List<City> findAllByOrderByNameAsc();

    @Query("SELECT c FROM City c WHERE c.code = ?1")
    public City findByCode(String code);
}