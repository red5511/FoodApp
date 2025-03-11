package com.foodapp.foodapp;

import com.foodapp.foodapp.forDevelopment.DatabaseDataFakerInterface;
import com.foodapp.foodapp.forDevelopment.TechnicalContextDev;
import com.foodapp.foodapp.forDevelopment.common.DatabaseFakerUtils;
import com.foodapp.foodapp.forDevelopment.uBuly.UBulyDatabaseDataFakerInterface;
import com.foodapp.foodapp.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "applicationAuditAware")
@EnableScheduling
public class FoodappApplication implements ApplicationRunner {
    private final DatabaseDataFakerInterface databaseDataFaker;
    private final UBulyDatabaseDataFakerInterface uBulyDatabaseDataFaker;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final String adminLogin;
    private final String adminPassword;

    public FoodappApplication(DatabaseDataFakerInterface databaseDataFaker,
                              UBulyDatabaseDataFakerInterface uBulyDatabaseDataFaker,
                              PasswordEncoder passwordEncoder,
                              UserRepository userRepository,
                              @Value("${app.admin-login}") String adminLogin,
                              @Value("${app.admin-password}") String adminPassword) {
        this.databaseDataFaker = databaseDataFaker;
        this.uBulyDatabaseDataFaker = uBulyDatabaseDataFaker;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.adminLogin = adminLogin;
        this.adminPassword = adminPassword;
    }

    public static void main(String[] args) {
        SpringApplication.run(FoodappApplication.class, args);
    }

    @Override
    @TechnicalContextDev
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        var adminOptional = userRepository.findByEmail(adminLogin);

        if (adminOptional.isEmpty()) {
            var admin = DatabaseFakerUtils.createFakeAdmin(adminLogin, passwordEncoder.encode(adminPassword));
            userRepository.save(admin);
        }

        //To moze byc zmockowane
        databaseDataFaker.initFakeData();
        uBulyDatabaseDataFaker.initData();

    }
}
