package com.hokidachi.admin.setting.district;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.hokidachi.admin.setting.district.DistrictRepository;
import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.District;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class DistrictRepositoryTests {
    @Autowired private DistrictRepository repo;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateDistrictsInIndia() {
        Integer cityId = 1;
        City city = entityManager.find(City.class, cityId);

        District district = repo.save(new District("Hà Nội", city));        

        assertThat(district).isNotNull();
        assertThat(district.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateDistrictsInUS() {
        Integer cityId = 2;
        City city = entityManager.find(City.class, cityId);

        //District district = repo.save(new District("California", city));
        //District district = repo.save(new District("Texas", city));
        //District district = repo.save(new District("New York", city));
        District district = repo.save(new District("Washington", city));

        assertThat(district).isNotNull();
        assertThat(district.getId()).isGreaterThan(0);
    }

    @Test
    public void testListDistrictByCity() {
        Integer cityId = 2;
        City city = entityManager.find(City.class, cityId);
        List<District> listDistricts = repo.findByCityOrderByNameAsc(city);

        listDistricts.forEach(System.out::println);

        assertThat(listDistricts.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateDistrict() {
        Integer districtId = 3;
        String districtName = "Tamil Nadu";
        District district = repo.findById(districtId).get();

        district.setName(districtName);
        District updatedDistrict = repo.save(district);

        assertThat(updatedDistrict.getName()).isEqualTo(districtName);
    }

    @Test
    public void testGetDistrict() {
        Integer districtId = 1;

        Optional<District> findById = repo.findById(districtId);
        assertThat(findById.isPresent());
    }

    @Test
    public void testDeleteDistrict() {
        Integer districtId = 8;
        repo.deleteById(districtId);

        Optional<District> findById = repo.findById(districtId);
        assertThat(findById.isEmpty());
    }
}