package com.example.demo;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate")
public class NumberGenerator {

    private final Random random = new Random();

    @GetMapping
    public ResponseEntity<String> getNumber() {
        int number = random.nextInt(100) + 1;
        return ResponseEntity.ok(String.valueOf(number));
    }

}
