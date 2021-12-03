package com.example.hrmanagment.controller;

import com.example.hrmanagment.payload.TurniketDto;
import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.service.TurniketService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/tourniquet")
public class TourniquetController {

    final
    TurniketService turniketService;

    public TourniquetController(TurniketService turniketService) {
        this.turniketService = turniketService;
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll() {
        ApiResponse apiResponse = turniketService.getAll();
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getByNumber(@RequestParam String number) {
        ApiResponse apiResponse = turniketService.getByNumber(number);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody TurniketDto turniketDto) throws MessagingException {
        ApiResponse apiResponse = turniketService.add(turniketDto);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @PutMapping
    public HttpEntity<?> edit(@RequestBody TurniketDto turniketDto, @RequestParam String number) throws MessagingException {
        ApiResponse apiResponse = turniketService.edit(number, turniketDto);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @DeleteMapping
    public HttpEntity<?> delete(@RequestParam String number) {
        ApiResponse apiResponse = turniketService.delete(number);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
