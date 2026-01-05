package com.utils.application.mailing.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VerifyUpdatePasswordEvent {
    private String emailFrom,emailTo,Subject,name,token;

}
