package com.havving.tutorial.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HAVVING
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    @ApiOperation(value = "exam", notes = "예제입니다.")
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello!");
    }
}
