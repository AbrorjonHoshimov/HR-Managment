package com.example.hrmanagment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(optional = false)
    private User director;

    @Column(nullable = false)
    private String name;

    public Company(User director, String name) {
        this.director = director;
        this.name = name;
    }
}