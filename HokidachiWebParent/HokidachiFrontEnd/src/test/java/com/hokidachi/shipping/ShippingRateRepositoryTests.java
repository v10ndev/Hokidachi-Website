package com.hokidachi.shipping;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.hokidachi.common.entity.City;
import com.hokidachi.common.entity.ShippingRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ShippingRateRepositoryTests {

    @Autowired private ShippingRateRepository repo;

    @Test
    public void testFindByCityAndDistrict() {
        City vietNam = new City(55);
        String district = "Bình Thạnh";
        ShippingRate shippingRate = repo.findByCityAndDistrict(vietNam, district);

        assertThat(shippingRate).isNotNull();
        System.out.println(shippingRate);
    }
}