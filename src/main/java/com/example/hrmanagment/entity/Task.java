package com.example.hrmanagment.entity;


import com.example.hrmanagment.entity.template.AbsEntity;
import com.example.hrmanagment.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task extends AbsEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    private Timestamp deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.STATUS_NEW;

    @ManyToOne(optional = false)
    private User taskTaker;

    @ManyToOne(optional = false)
    private User taskGiver;
}
