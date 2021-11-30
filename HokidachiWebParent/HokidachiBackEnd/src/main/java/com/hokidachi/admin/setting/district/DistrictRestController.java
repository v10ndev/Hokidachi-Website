package com.hokidachi.admin.setting.district;

import java.util.ArrayList;
import java.util.List;

import com.hokidachi.common.entity.DistrictDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.District;

@RestController
public class DistrictRestController {

    @Autowired private DistrictRepository repo;

    @GetMapping("/districts/list_by_city/{id}")
    public List<DistrictDTO> listByCity(@PathVariable("id") Integer cityId){
        List<District> listDistricts = repo.findByCityOrderByNameAsc(new City(cityId));
        List<DistrictDTO> result = new ArrayList<>();

        for (District district : listDistricts) {
            result.add(new DistrictDTO(district.getId(), district.getName()));
        }

        return result;
    }

    @PostMapping("/districts/save")
    public String save(@RequestBody District district) {
        District savedDistrict = repo.save(district);
        return String.valueOf(savedDistrict.getId());
    }

    @DeleteMapping("/districts/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repo.deleteById(id);
    }
}