package com.example.hrmanagment.controller;

import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.service.LeadershipService;
import com.example.hrmanagment.service.SalaryTakenService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/leadership")
public class LeadershipController {

    final
    LeadershipService leadershipService;


    final
    SalaryTakenService salaryTakenService;

    public LeadershipController(LeadershipService leadershipService, SalaryTakenService salaryTakenService) {
        this.leadershipService = leadershipService;
        this.salaryTakenService = salaryTakenService;
    }

    @GetMapping
    public HttpEntity<?> getHistoryAndTasks(@RequestParam Timestamp startTime, @RequestParam Timestamp endTime, @RequestParam String number) {
        ApiResponse apiResponse = leadershipService.getHistoryAndTasks(startTime, endTime, number);
        return ResponseEntity.status(!apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping("/salaryByUser")
    public HttpEntity<?> getByUser(@RequestParam String email) {
        ApiResponse apiResponse = salaryTakenService.getByUser(email);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping("/salaryByMonth")
    public HttpEntity<?> getByMonth(@RequestParam String month) {
        ApiResponse apiResponse = salaryTakenService.getByMonth(month);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
