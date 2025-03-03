package com.jyp.service.my_today_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MyTodayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyTodayServiceApplication.class, args);
    }

}
