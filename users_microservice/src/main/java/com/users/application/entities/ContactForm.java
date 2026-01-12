package com.users.application.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact_form")
public class ContactForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @Size(max = 50, message = "Name must be less than 50 characters")
    private String sentDate;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Surname is required")
    @Size(max = 50, message = "Surname must be less than 50 characters")
    private String surname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "Message is required")
    @Size(max = 500, message = "Message must be less than 500 characters")
    private String message;


}