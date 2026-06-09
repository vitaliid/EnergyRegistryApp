package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Operation(summary = "Health check")
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
