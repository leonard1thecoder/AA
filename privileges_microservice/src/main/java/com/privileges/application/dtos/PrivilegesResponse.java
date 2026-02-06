package com.privileges.application.dtos;


import com.utils.application.ResponseContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PrivilegesResponse implements ResponseContract {

    private int id;

    private String privilegeName;

}
