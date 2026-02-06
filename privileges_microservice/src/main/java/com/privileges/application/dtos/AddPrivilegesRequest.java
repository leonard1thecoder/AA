package com.privileges.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddPrivilegesRequest {

    private String privilegeName;


}
