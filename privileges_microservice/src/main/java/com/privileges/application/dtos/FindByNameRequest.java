package com.privileges.application.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class FindByNameRequest {

    private String privilegeName;
}
