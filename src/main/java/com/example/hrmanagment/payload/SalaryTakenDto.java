package com.example.hrmanagment.payload;

import com.example.hrmanagment.enums.Month;
import lombok.Data;

import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class SalaryTakenDto {
    @Email
    @NotNull
    private String email;

    @NotNull
    private double amount;

    @Enumerated
    private Month period;
}
