package com.hokidachi.admin.setting.city;

import java.util.List;

import com.hokidachi.admin.setting.city.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hokidachi.common.entity.City;

@RestController
public class CityRestController {
    @Autowired private CityRepository repo;

    @GetMapping("/cities/list")
    public List<City> listAll(){
        return repo.findAllByOrderByNameAsc();
    }

    @PostMapping("/cities/save")
    public String save(@RequestBody City city) {
        City savedCity = repo.save(city);
        return String.valueOf(savedCity.getId());
    }

    @DeleteMapping("/cities/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        repo.deleteById(id);
    }

}