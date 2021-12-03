package com.example.hrmanagment.controller;

import com.example.hrmanagment.payload.TaskDto;
import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.service.TaskService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    final
    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody TaskDto taskDto) throws MessagingException {
        ApiResponse apiResponse = taskService.add(taskDto);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@RequestBody TaskDto taskDto, @PathVariable UUID id) throws MessagingException {
        ApiResponse apiResponse = taskService.edit(id, taskDto);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @PutMapping("/s/{id}")
    public HttpEntity<?> editStatus(@RequestBody TaskDto taskDto, @PathVariable UUID id) throws MessagingException {
        ApiResponse apiResponse = taskService.editStatus(id, taskDto);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        ApiResponse apiResponse = taskService.getById(id);
        return ResponseEntity.status(apiResponse.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @GetMapping()
    public HttpEntity<?> getAllToFrom(@RequestParam String stat) {
        ApiResponse response = null;
        if (stat.equals("to")) {
            response = taskService.getAllTo();
        } else if (stat.equals("from"))
            response = taskService.getAllFrom();

        assert response != null;
        return ResponseEntity.status(response.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }


    @DeleteMapping("{id}")
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse response = taskService.deleteById(id);
        return ResponseEntity.status(response.isStatus() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

}
