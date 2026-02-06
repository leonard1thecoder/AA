package com.retails.application.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DisplayRetailCompanyByIdRequest {
    private Long retailCompanyId;
}
