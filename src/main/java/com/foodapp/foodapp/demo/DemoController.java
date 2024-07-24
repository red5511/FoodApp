package com.foodapp.foodapp.demo;

import com.foodapp.foodapp.company.Company;
import com.foodapp.foodapp.company.CompanyRepository;
import com.foodapp.foodapp.company.Content;
import com.foodapp.foodapp.company.OpenHours;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;


@RestController
@RequestMapping
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DemoController {

    @Autowired
    CompanyRepository companyRepository;

    @Operation(summary = "say Hello")
    @ApiResponse(responseCode = "200", description = "User has been created")
    @GetMapping("/demo")
    public ResponseEntity<String> sayHello() {
        var xd = companyRepository.findById(1L);
        return ResponseEntity.ok("Hi man");
    }

    @Operation(summary = "say Hello2")
    @ApiResponse(responseCode = "200", description = "hello returned correctly")
    @GetMapping("/api/v1/auth/test")
    public ResponseEntity<String> sayHello2() {
        companyRepository.save(Company.builder()
                .address("xd")
                .content(Content.builder()
                        .openHours(OpenHours.builder()
                                .mondayStart(LocalTime.now())
                                .mondayEnd(LocalTime.now().plusHours(8))
                                .build())
                        .build())
                .build());
        return ResponseEntity.ok("Hi man");
    }
}
