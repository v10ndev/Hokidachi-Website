package com.hokidachi.setting;

import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.District;
import com.hokidachi.common.entity.DistrictDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DistrictRestController {

    @Autowired private DistrictRepository repo;

    @GetMapping("/settings/list_districts_by_city/{id}")
    public List<DistrictDTO> listByCity(@PathVariable("id") Integer cityId){
        List<District> listDistricts = repo.findByCityOrderByNameAsc(new City(cityId));
        List<DistrictDTO> result = new ArrayList<>();

        for (District district : listDistricts) {
            result.add(new DistrictDTO(district.getId(), district.getName()));
        }

        return result;
    }

}