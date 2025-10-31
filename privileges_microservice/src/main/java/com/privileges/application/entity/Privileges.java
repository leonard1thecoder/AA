package com.privileges.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Component
public class Privileges {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String privilegeName;

    private byte privilegeStatus;
}
