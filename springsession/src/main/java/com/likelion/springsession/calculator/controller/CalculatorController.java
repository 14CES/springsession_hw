package com.likelion.springsession.calculator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    @GetMapping("/divide")
    public String divide(@RequestParam int a, @RequestParam int b) {
        try {
            return String.valueOf(a / b);
        } catch (ArithmeticException e) {
            return "0으로 나눌 수 없습니다";
        }
    }
}