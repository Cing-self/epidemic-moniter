package com.cjh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.cjh.mapper")
@SpringBootApplication
public class EpidemicMoniterApplication {

    public static void main(String[] args) {

        SpringApplication.run(EpidemicMoniterApplication.class, args);
    }

}
