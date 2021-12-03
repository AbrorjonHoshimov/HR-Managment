package com.example.hrmanagment.repository;

import com.example.hrmanagment.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
