package com.foodapp.foodapp;

import com.foodapp.foodapp.common.PolishCharConverter;
import com.foodapp.foodapp.forDevelopment.DatabaseDataFaker;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "applicationAuditAware")
@EnableScheduling
@AllArgsConstructor
public class FoodappApplication implements ApplicationRunner {
    private static final String CHINESE = "GBK";

    @Autowired
    private final DatabaseDataFaker databaseDataFaker;

    public static void main(String[] args) {
        SpringApplication.run(FoodappApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        databaseDataFaker.initFakeData();

        var xd = PolishCharConverter.removePolishDiacritics("ąćęóżźłśŚ");
        System.out.println(xd);
    }
}
