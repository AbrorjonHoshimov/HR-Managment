package com.example.hrmanagment.service;


import com.example.hrmanagment.component.Checker;
import com.example.hrmanagment.component.MailSender;
import com.example.hrmanagment.entity.Role;
import com.example.hrmanagment.entity.Task;
import com.example.hrmanagment.entity.User;
import com.example.hrmanagment.enums.RoleName;
import com.example.hrmanagment.enums.TaskStatus;
import com.example.hrmanagment.payload.TaskDto;
import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.repository.TaskRepository;
import com.example.hrmanagment.repository.UserRepository;
import com.example.hrmanagment.security.JwtProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {
    final
    TaskRepository taskRepository;

    final
    Checker checker;

    final
    UserRepository userRepository;

    final
    MailSender mailSender;

    final
    JwtProvider jwtProvider;

    public TaskService(TaskRepository taskRepository, Checker checker, UserRepository userRepository, MailSender mailSender, JwtProvider jwtProvider) {
        this.taskRepository = taskRepository;
        this.checker = checker;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.jwtProvider = jwtProvider;
    }

    // Add task
    public ApiResponse add(TaskDto taskDto) throws MessagingException {
        String email = taskDto.getUserEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("User Not found!", false);

        User user = optionalUser.get();
        // check role
        Set<Role> roles = user.getRoles();
        ApiResponse response = null;
        for (Role role : roles) {
            response = checker.checkForAny(role.getName().name());
            if (!response.isStatus())
                return new ApiResponse("You have no such right!", false);
        }

        // Check not upload task
        List<Task> takerTasks = taskRepository.findAllByTaskTaker(user);
        for (Task takerTask : takerTasks) {
            if (!takerTask.getStatus().name().equals(TaskStatus.STATUS_COMPLETED.name()))
                return new ApiResponse("An unfinished task in the employee!", true);
        }

        Task task = new Task();
        task.setTaskTaker(user);
        assert response != null;
        task.setTaskGiver((User) response.getObject());
        task.setDeadline(taskDto.getDeadline());
        task.setDescription(taskDto.getDescription());
        task.setName(taskDto.getName());
        assert taskDto.getStatus() != null;
        task.setStatus(taskDto.getStatus());
        Task saved = taskRepository.save(task);

        boolean addTask = mailSender.mailTextAddTask(user.getEmail(), saved.getName(), saved.getId());
        if (!addTask)
            return new ApiResponse("Task added, but no email sent!", true);
        return new ApiResponse("Task added and email sent!", true);
    }

    //Edit task
    public ApiResponse edit(UUID id, TaskDto taskDto) throws MessagingException {
        ApiResponse apiResponse = getById(id);
        if (!apiResponse.isStatus())
            return apiResponse;

        Task oldTask = (Task) apiResponse.getObject();

        String email = taskDto.getUserEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            return new ApiResponse("User Not found!", false);

        User user = optionalUser.get();

        //Check role
        Set<Role> roles = user.getRoles();
        ApiResponse response = null;
        for (Role role : roles) {
            response = checker.checkForAny(role.getName().name());
            if (!response.isStatus())
                return new ApiResponse("You have no such right!", false);
        }

        //Check not upload task
        List<Task> takerTasks = taskRepository.findByTaskTakerAndIdNot(user, id);
        for (Task takerTask : takerTasks) {
            if (!takerTask.getStatus().name().equals(TaskStatus.STATUS_COMPLETED.name()))
                return new ApiResponse("An unfinished task in the employee!", true);
        }

        oldTask.setTaskTaker(user);

        assert response != null;
        oldTask.setTaskGiver((User) response.getObject());

        assert taskDto.getDeadline() != null;
        oldTask.setDeadline(taskDto.getDeadline());

        assert taskDto.getName() != null;
        oldTask.setName(taskDto.getName());

        assert taskDto.getStatus() != null;
        oldTask.setStatus(taskDto.getStatus());

        assert taskDto.getDescription() != null;
        oldTask.setDescription(taskDto.getDescription());

        Task saved = taskRepository.save(oldTask);

        boolean editTask = mailSender.mailTextEditTask(user.getEmail(), saved.getName(), saved.getId());
        if (!editTask)
            return new ApiResponse("Task edited, but no email sent!", true);
        return new ApiResponse("Task edited and email sent!", true);
    }

    //TASK status change
    public ApiResponse editStatus(UUID id, TaskDto taskDto) throws MessagingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (!optionalUser.isPresent())
            return new ApiResponse("User not found!", false);

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Task not found!", false);

        Task task = optionalTask.get();
        if (!task.getTaskTaker().getEmail().equals(user.getEmail()))
            return new ApiResponse("The task does not belong to you.", false);

        task.setStatus(taskDto.getStatus());
        Task saved = taskRepository.save(task);

        if (saved.getStatus().name().equals(TaskStatus.STATUS_COMPLETED.name())) {
            boolean completed = mailSender.mailTextTaskCompleted(saved.getTaskGiver().getEmail(), saved.getTaskTaker().getEmail(), saved.getName());
            if (completed)
                return new ApiResponse("Task completed and email sent.", true);
            return new ApiResponse("Task completed and but email not sent.", true);
        }
        return new ApiResponse("Task edited", true);
    }

    // Taskni qaytarish id bo'yicha
    public ApiResponse getById(UUID id) {
        Optional<Task> byId = taskRepository.findById(id);
        if (!byId.isPresent())
            return new ApiResponse("Task Not found!", false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (!userOptional.isPresent())
            return new ApiResponse("Error!", false);

        for (Role role : userOptional.get().getRoles()) {
            if (role.getName().name().equals(RoleName.ROLE_DIRECTOR.name()))
                return new ApiResponse("Task by id!", true, byId.get());
        }

        UUID idTaker = byId.get().getTaskTaker().getId();
        UUID idGiver = byId.get().getTaskGiver().getId();
        UUID idToken = userOptional.get().getId();
        if (idToken != idTaker && idToken != idGiver)
            return new ApiResponse("Your task does not belong to you!", false);
        return new ApiResponse("Task by id!", true, byId.get());
    }

    public ApiResponse getAllTo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (!userOptional.isPresent())
            return new ApiResponse("User not found!", false);
        List<Task> taskList = taskRepository.findAllByTaskGiver(userOptional.get());
        return new ApiResponse("TaskTaker task list!", true, taskList);
    }

    public ApiResponse getAllFrom() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (!userOptional.isPresent())
            return new ApiResponse("User not found!", false);
        List<Task> taskList = taskRepository.findAllByTaskTaker(userOptional.get());
        return new ApiResponse("TaskGiver task list!", true, taskList);
    }

    public ApiResponse deleteById(UUID id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (!userOptional.isPresent())
            return new ApiResponse("User not found!", false);

        Optional<Task> byId = taskRepository.findById(id);
        if (byId.isPresent() && byId.get().getTaskGiver().getEmail().equals(userOptional.get().getEmail())) {
            taskRepository.deleteById(id);
            return new ApiResponse("Task deleted!", true);
        }
        return new ApiResponse("The task was not deleted!", false);
    }

    public ApiResponse getAllByUserAndDate(Timestamp startTime, Timestamp endTime, User user) {
        Set<Role> roles = user.getRoles();
        String role = null;
        for (Role roleName : roles) {
            role = roleName.getName().name();
            break;
        }
        boolean check = checker.check(role);

        if (!check)
            return new ApiResponse("Forbidden!", false);

        List<Task> taskList = taskRepository.findAllByTaskGiverAndCreatedAtBetweenAndStatus(user, startTime, endTime, TaskStatus.STATUS_COMPLETED);
        return new ApiResponse("List Task by Date and user!", true, taskList);
    }
}