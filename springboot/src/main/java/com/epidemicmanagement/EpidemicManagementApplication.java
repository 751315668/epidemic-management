package com.epidemicmanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.epidemicmanagement.dao")
@ServletComponentScan("com.epidemicmanagement.filters")
public class EpidemicManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpidemicManagementApplication.class, args);
    }

}
