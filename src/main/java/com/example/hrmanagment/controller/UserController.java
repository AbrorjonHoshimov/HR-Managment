package com.example.hrmanagment.controller;


import com.example.hrmanagment.payload.UserDto;
import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.security.JwtProvider;
import com.example.hrmanagment.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    final
    UserService userService;

    final
    JwtProvider jwtProvider;

    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    //ADD MANAGER AND EMPLOYEE
//    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody UserDto userDto) throws MessagingException {
        ApiResponse apiResponse = userService.add(userDto);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    //Edit information. User's can only change their credentials
//    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_STAFF')")
    @PutMapping
    public HttpEntity<?> edit(@Valid @RequestBody UserDto userDto) throws MessagingException {
        ApiResponse apiResponse = userService.edit(userDto);
        return ResponseEntity.status(apiResponse.isStatus()?HttpStatus.OK:HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    //By token
//    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_STAFF')")
    @GetMapping("/me")
    public HttpEntity<?> getByToken(){
        ApiResponse apiResponse = userService.getOne();
        return ResponseEntity.status(apiResponse.isStatus()?200:409).body(apiResponse);
    }

    //By EMAIL
//    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping()
    public HttpEntity<?> getByEmail(@RequestParam String email){
        ApiResponse apiResponse = userService.getByEmail(email);
        return ResponseEntity.status(apiResponse.isStatus()?200:409).body(apiResponse);
    }

    //Verify email
    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String email, @RequestParam String code) {
        ApiResponse apiResponse = userService.verifyEmail(email, code);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}