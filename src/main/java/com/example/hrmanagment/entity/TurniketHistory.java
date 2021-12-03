package com.example.hrmanagment.entity;

import com.example.hrmanagment.enums.TurniketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TurniketHistory {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    private Tourniquet turniket;

    @Enumerated(EnumType.STRING)
    private TurniketType type;

    @CreationTimestamp
    private Timestamp time;
}
