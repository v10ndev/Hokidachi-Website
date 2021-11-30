package com.hokidachi.admin.setting.city;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.hokidachi.admin.setting.city.CityRepository;
import com.hokidachi.common.entity.City;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CityRepositoryTests {
    @Autowired private CityRepository repo;

    @Test
    public void testCreateCity() {
        City city = repo.save(new City("Hà Nội", "HN"));
        assertThat(city).isNotNull();
        assertThat(city.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCities() {
        List<City> listCities = repo.findAllByOrderByNameAsc();
        listCities.forEach(System.out::println);

        assertThat(listCities.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateCity() {
        Integer id = 1;
        String name = "Hà Nội";

        City city = repo.findById(id).get();
        city.setName(name);

        City updatedCity = repo.save(city);

        assertThat(updatedCity.getName()).isEqualTo(name);
    }

    @Test
    public void testDeleteCity() {
        Integer id = 5;
        repo.deleteById(id);

        Optional<City> findById = repo.findById(id);
        assertThat(findById.isEmpty());
    }
}