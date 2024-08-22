package com.appliances.recyle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RecycleWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecycleWebApplication.class, args);
    }

}
