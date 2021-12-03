package com.example.hrmanagment.controller;


import com.example.hrmanagment.payload.TurniketHistoryDto;
import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.service.TurniketHistoryService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api/tourniquetHistory")
public class TourniquetHistoryController {

    final
    TurniketHistoryService turniketHistoryService;

    public TourniquetHistoryController(TurniketHistoryService turniketHistoryService) {
        this.turniketHistoryService = turniketHistoryService;
    }

    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody TurniketHistoryDto turniketHistoryDto) {
        ApiResponse apiResponse = turniketHistoryService.add(turniketHistoryDto);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping("/byDate")
    public HttpEntity<?> getAllByDate(@RequestParam String number, @RequestParam Timestamp startTime, @RequestParam Timestamp endTime) {
        ApiResponse apiResponse = turniketHistoryService.getAllByDate(number, startTime, endTime);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll(@RequestParam String number) {
        ApiResponse apiResponse = turniketHistoryService.getAll(number);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
