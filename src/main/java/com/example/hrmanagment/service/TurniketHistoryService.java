package com.example.hrmanagment.service;

import com.example.hrmanagment.component.Checker;
import com.example.hrmanagment.entity.Role;
import com.example.hrmanagment.entity.Tourniquet;
import com.example.hrmanagment.entity.TurniketHistory;
import com.example.hrmanagment.enums.RoleName;
import com.example.hrmanagment.payload.TurniketHistoryDto;
import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.repository.TurniketHistoryRepository;
import com.example.hrmanagment.repository.TurniketRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TurniketHistoryService {

    final
    TurniketHistoryRepository turniketHistoryRepository;

    final
    TurniketRepository turniketRepository;

    final
    Checker checker;

    public TurniketHistoryService(TurniketHistoryRepository turniketHistoryRepository, TurniketRepository turniketRepository, Checker checker) {
        this.turniketHistoryRepository = turniketHistoryRepository;
        this.turniketRepository = turniketRepository;
        this.checker = checker;
    }

    public ApiResponse add(TurniketHistoryDto turniketHistoryDto){
        Optional<Tourniquet> optionalTurniket = turniketRepository.findByNumber(turniketHistoryDto.getNumber());
        if (!optionalTurniket.isPresent()) {
            return new ApiResponse("tourniquet not found!", false);
        }

        Set<Role> roles = optionalTurniket.get().getOwner().getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role role1 : roles) {
            role = role1.getName().name();
            break;
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("tourniquet not found!", false);

        TurniketHistory turniketHistory = new TurniketHistory();
        turniketHistory.setTurniket(optionalTurniket.get());
        turniketHistory.setType(turniketHistoryDto.getType());
        turniketHistoryRepository.save(turniketHistory);
        return new ApiResponse("Success!", true);
    }

    public ApiResponse getAllByDate(String number, Timestamp startTime, Timestamp endTime){
        Optional<Tourniquet> optionalTurniket = turniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("tourniquet not found", false);

        Set<Role> roles = optionalTurniket.get().getOwner().getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role role1 : roles) {
            role = role1.getName().name();
            break;
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("tourniquet not found", false);

        List<TurniketHistory> historyList = turniketHistoryRepository.findAllByTurniketAndTimeIsBetween(optionalTurniket.get(), startTime, endTime);
        return new ApiResponse("History list by date",true, historyList);
    }

    public ApiResponse getAll(String number){
        Optional<Tourniquet> optionalTurniket = turniketRepository.findByNumber(number);
        if (!optionalTurniket.isPresent())
            return new ApiResponse("not found", false);

        Set<Role> roles = optionalTurniket.get().getOwner().getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role role1 : roles) {
            role = role1.getName().name();
            break;
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("tourniquet not found", false);

        List<TurniketHistory> allByTurniket = turniketHistoryRepository.findAllByTurniket(optionalTurniket.get());
        return new ApiResponse("All history by tourniquet!", true, allByTurniket);
    }
}
