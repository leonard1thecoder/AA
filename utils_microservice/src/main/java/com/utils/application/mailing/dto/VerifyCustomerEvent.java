package com.utils.application.mailing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class VerifyCustomerEvent {

private String emailFrom,emailTo,Subject,name,token;
private Integer privilegeId;
}
