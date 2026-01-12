package com.users.application.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactUsResponse {
    private String status,message;
}
