package com.example.hrmanagment.repository;

import com.example.hrmanagment.entity.SalaryTaken;
import com.example.hrmanagment.entity.User;
import com.example.hrmanagment.enums.Month;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaryTakenRepository extends JpaRepository<SalaryTaken, UUID> {
    Optional<SalaryTaken> findByOwnerAndPeriod(User owner, Month period);
    List<SalaryTaken> findAllByOwner(User user);
    List<SalaryTaken> findAllByPeriod(Month period);
}
