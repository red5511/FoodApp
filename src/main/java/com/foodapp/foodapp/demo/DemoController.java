package com.foodapp.foodapp.demo;

import com.foodapp.foodapp.company.CompanyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
        return ResponseEntity.ok("Hi man");
    }

//    @Operation(summary = "say Hello2")
//    @ApiResponse(responseCode = "200", description = "hello returned correctly")
//    @GetMapping("/api/v1/auth/test")
//    public ResponseEntity<MyResponse> sayHello2() {
//        MyResponse response = new MyResponse();
//        response.setApprovalDeadline(LocalDateTime.now());
//        return ResponseEntity.ok(response);
//    }

}
