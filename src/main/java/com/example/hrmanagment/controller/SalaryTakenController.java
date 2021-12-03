package com.example.hrmanagment.controller;

import com.example.hrmanagment.payload.SalaryTakenDto;
import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.service.SalaryTakenService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/salariesTaken")
public class SalaryTakenController {
    final
    SalaryTakenService salaryTakenService;

    public SalaryTakenController(SalaryTakenService salaryTakenService) {
        this.salaryTakenService = salaryTakenService;
    }

    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody SalaryTakenDto salaryTakenDto) {
        ApiResponse apiResponse = salaryTakenService.add(salaryTakenDto);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @PutMapping
    public HttpEntity<?> edit(@RequestBody SalaryTakenDto salaryTakenDto) {
        ApiResponse apiResponse = salaryTakenService.add(salaryTakenDto);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @DeleteMapping
    public HttpEntity<?> delete(@RequestParam String email, @RequestParam String month) {
        ApiResponse apiResponse = salaryTakenService.delete(email, month);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @PutMapping("/stat")
    public HttpEntity<?> customize(@RequestParam String email, @RequestParam String month, @RequestParam boolean stat) {
        ApiResponse apiResponse = salaryTakenService.customize(email, month, stat);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
