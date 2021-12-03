package com.example.hrmanagment.service;

import com.example.hrmanagment.component.Checker;
import com.example.hrmanagment.entity.Role;
import com.example.hrmanagment.entity.SalaryTaken;
import com.example.hrmanagment.entity.User;
import com.example.hrmanagment.enums.Month;
import com.example.hrmanagment.enums.RoleName;
import com.example.hrmanagment.payload.SalaryTakenDto;
import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.repository.SalaryTakenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class SalaryTakenService {
    final
    SalaryTakenRepository salaryTakenRepository;

    final
    Checker checker;

    final
    UserService userService;

    public SalaryTakenService(SalaryTakenRepository salaryTakenRepository, Checker checker, UserService userService) {
        this.salaryTakenRepository = salaryTakenRepository;
        this.checker = checker;
        this.userService = userService;
    }

    public ApiResponse add(SalaryTakenDto salaryTakenDto) {
        ApiResponse response = userService.getByEmail(salaryTakenDto.getEmail());
        if (!response.isStatus())
            return response;
        User user = (User) response.getObject();

        Set<Role> roles = user.getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role rolex : roles) {
            role = rolex.getName().name();
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("Forbidden!", false);

        SalaryTaken salaryTaken = new SalaryTaken();
        salaryTaken.setAmount(salaryTakenDto.getAmount());
        salaryTaken.setOwner(user);
        salaryTaken.setPeriod(salaryTakenDto.getPeriod());
        salaryTakenRepository.save(salaryTaken);
        return new ApiResponse("Saved!", true);
    }

    public ApiResponse edit(SalaryTakenDto salaryTakenDto) {

        ApiResponse response = userService.getByEmail(salaryTakenDto.getEmail());
        if (!response.isStatus())
            return response;
        User user = (User) response.getObject();

        Set<Role> roles = user.getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role rolex : roles) {
            role = rolex.getName().name();
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("Forbidden!", false);

        Optional<SalaryTaken> optional = salaryTakenRepository.findByOwnerAndPeriod(user, salaryTakenDto.getPeriod());
        if (!optional.isPresent())
            return new ApiResponse("Not salary!", false);

        if (optional.get().isPaid())
            return new ApiResponse("This salary already payed, you don't edit it!", false);


        SalaryTaken salaryTaken = optional.get();
        salaryTaken.setAmount(salaryTakenDto.getAmount());
        salaryTaken.setOwner(user);
        salaryTaken.setPeriod(salaryTakenDto.getPeriod());
        salaryTakenRepository.save(salaryTaken);
        return new ApiResponse("Employee salary edited!", true);
    }

    public ApiResponse delete(String email, String month) {
        ApiResponse response = userService.getByEmail(email);
        if (!response.isStatus())
            return response;
        User user = (User) response.getObject();

        Set<Role> roles = user.getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role rolex : roles) {
            role = rolex.getName().name();
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("Forbidden!", false);

        Month period = null;

        for (Month value : Month.values()) {
            if (value.name().equals(month)) {
                period = value;
                break;
            }
        }
        if (period == null)
            return new ApiResponse("Error!", false);

        Optional<SalaryTaken> optional = salaryTakenRepository.findByOwnerAndPeriod(user, period);
        if (!optional.isPresent())
            return new ApiResponse("Salary not found!", false);

        if (optional.get().isPaid())
            return new ApiResponse("This salary already payed, you don't edit it!", false);

        salaryTakenRepository.delete(optional.get());
        return new ApiResponse("Salary deleted!", true);
    }

    // Change Salary payed
    public ApiResponse customize(String email, String month, boolean stat) {
        ApiResponse response = userService.getByEmail(email);
        if (!response.isStatus())
            return response;
        User user = (User) response.getObject();

        Set<Role> roles = user.getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role rolex : roles) {
            role = rolex.getName().name();
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("Forbidden!", false);

        Month period = null;

        for (Month value : Month.values()) {
            if (value.name().equals(month)) {
                period = value;
                break;
            }
        }
        if (period == null)
            return new ApiResponse("Error!", false);

        Optional<SalaryTaken> optional = salaryTakenRepository.findByOwnerAndPeriod(user, period);
        if (!optional.isPresent())
            return new ApiResponse("Salary not found!", false);

        SalaryTaken salaryTaken = optional.get();
        if (salaryTaken.isPaid())
            return new ApiResponse("This salary already payed, you don't edit it!", false);

        salaryTaken.setPaid(stat);
        return new ApiResponse("Edited salary payed!", true);
    }

    public ApiResponse getByUser(String email) {
        ApiResponse response = userService.getByEmail(email);
        if (!response.isStatus())
            return response;
        User user = (User) response.getObject();

        Set<Role> roles = user.getRoles();
        String role = RoleName.ROLE_STAFF.name();
        for (Role rolex : roles) {
            role = rolex.getName().name();
        }

        boolean check = checker.check(role);
        if (!check)
            return new ApiResponse("Forbidden!", false);

        return new ApiResponse("List by Owner", true, salaryTakenRepository.findAllByOwner(user));
    }

    public ApiResponse getByMonth(String month) {
        boolean check = checker.check();
        if (!check)
            return new ApiResponse("Forbidden", false);

        Month period = null;

        for (Month value : Month.values()) {
            if (value.name().equals(month)) {
                period = value;
                break;
            }
        }
        if (period == null)
            return new ApiResponse("Error!", false);

        return new ApiResponse("List by period", true, salaryTakenRepository.findAllByPeriod(period));
    }
}
