package com.example.hrmanagment.repository;

import com.example.hrmanagment.entity.Task;
import com.example.hrmanagment.entity.User;
import com.example.hrmanagment.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByTaskTaker(User taskTaker);
    List<Task> findAllByTaskGiver(User taskTaker);
    List<Task> findByTaskTakerAndIdNot(User taskTaker, UUID id);
    List<Task> findAllByTaskGiverAndCreatedAtBetweenAndStatus(User taskGiver, Timestamp startTime, Timestamp endTime, TaskStatus status);
}
