package com.hokidachi.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EntityScan({"com.hokidachi.common.entity","com.hokidachi.admin.user"})
public class HokidachiBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(HokidachiBackEndApplication.class, args);
    }

}
