package com.foodapp.foodapp.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
@AllArgsConstructor
public class DemoController {

    @Operation(summary = "say Hello")
    @ApiResponse(responseCode = "200", description = "User has been created")
    @GetMapping("/demo")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hi man");
    }

    @Operation(summary = "say Hello2")
    @ApiResponse(responseCode = "200", description = "hello returned correctly")
    @GetMapping("/api/v1/auth/test")
    public ResponseEntity<String> sayHello2() {
        return ResponseEntity.ok("Hi man");
    }
}
