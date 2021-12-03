package com.example.hrmanagment.service;

import com.example.hrmanagment.component.Checker;
import com.example.hrmanagment.entity.Tourniquet;
import com.example.hrmanagment.entity.User;
import com.example.hrmanagment.payload.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeadershipService {

    final
    TurniketHistoryService turniketHistoryService;

    final
    TurniketService turniketService;

    final
    Checker checker;

    final
    UserService userService;

    final
    TaskService taskService;

    public LeadershipService(TurniketHistoryService turniketHistoryService, TurniketService turniketService, Checker checker, UserService userService, TaskService taskService) {
        this.turniketHistoryService = turniketHistoryService;
        this.turniketService = turniketService;
        this.checker = checker;
        this.userService = userService;
        this.taskService = taskService;
    }


    public ApiResponse getHistoryAndTasks(Timestamp startTime, Timestamp endTime, String email){
        ApiResponse apiResponse = userService.getByEmail(email);
        if (!apiResponse.isStatus())
            return apiResponse;

        User user = (User) apiResponse.getObject();

        ApiResponse responseTourniquet = turniketService.getByUser(user);
        if (!responseTourniquet.isStatus())
            return responseTourniquet;

        Tourniquet tourniquet= (Tourniquet) responseTourniquet.getObject();
        ApiResponse historyList = turniketHistoryService.getAllByDate(tourniquet.getNumber(), startTime, endTime);

        ApiResponse taskList = taskService.getAllByUserAndDate(startTime, endTime, user);

        List<ApiResponse> responseList = new ArrayList<>();
        responseList.add(historyList);
        responseList.add(taskList);

        return new ApiResponse("Found", true, responseList);
    }

}
