package com.example.hrmanagment.entity;

import com.example.hrmanagment.entity.template.AbsEntity;
import com.example.hrmanagment.enums.Month;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SalaryTaken extends AbsEntity {

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    private Month period;

    private boolean paid = false;
}