package com.example.hrmanagment.component;

import com.example.hrmanagment.entity.Role;
import com.example.hrmanagment.entity.User;
import com.example.hrmanagment.enums.RoleName;
import com.example.hrmanagment.payload.response.ApiResponse;
import com.example.hrmanagment.repository.UserRepository;
import com.example.hrmanagment.security.JwtProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class Checker {

    final
    JwtProvider jwtProvider;

    final
    UserRepository userRepository;

    public Checker(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public boolean check(String role) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            Set<Role> roles = userOptional.get().getRoles();
            String position = userOptional.get().getPosition();
            if (role.equals(RoleName.ROLE_DIRECTOR.name()))
                return false;

            for (Role adminRole : roles) {
                if (role.equals(RoleName.ROLE_MANAGER.name()) &&
                        adminRole.getName().name().equals(RoleName.ROLE_DIRECTOR.name())) {
                    return true;
                }

                if (role.equals(RoleName.ROLE_STAFF.name()) &&
                        ((adminRole.getName().name().equals(RoleName.ROLE_MANAGER.name()) &&
                                position.equalsIgnoreCase("hrManagement")) ||
                                adminRole.getName().name().equals(RoleName.ROLE_DIRECTOR.name()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public ApiResponse checkForAny(String role) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            Set<Role> roles = userOptional.get().getRoles();
            if (role.equals(RoleName.ROLE_DIRECTOR.name()))
                return new ApiResponse("False!", false);

            for (Role adminRole : roles) {
                if (role.equals(RoleName.ROLE_MANAGER.name()) &&
                        adminRole.getName().name().equals(RoleName.ROLE_DIRECTOR.name())) {
                    return new ApiResponse("True", true, userOptional.get());
                }

                if (role.equals(RoleName.ROLE_STAFF.name()) &&
                        ((adminRole.getName().name().equals(RoleName.ROLE_MANAGER.name()) ||
                                adminRole.getName().name().equals(RoleName.ROLE_DIRECTOR.name())))) {
                    return new ApiResponse("True", true, userOptional.get());
                }
            }
        }
        return new ApiResponse("False!", false);
    }

    public boolean check() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            for (Role role : user.getRoles()) {
                if (role.getName().name().equals(RoleName.ROLE_DIRECTOR.name())
                        || ((role.getName().name().equals(RoleName.ROLE_MANAGER.name())
                        && user.getPosition().equalsIgnoreCase("hrManagement")))) {
                    return true;
                }
            }
        }
        return false;
    }
}
