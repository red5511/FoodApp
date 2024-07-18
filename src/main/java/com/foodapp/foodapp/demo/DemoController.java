package com.foodapp.foodapp.demo;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
@AllArgsConstructor
public class DemoController {

    @GetMapping("/demo")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hi man");
    }
    @GetMapping("/api/v1/auth/test")
    public ResponseEntity<String> sayHello2() {
        return ResponseEntity.ok("Hi man");
    }
}
