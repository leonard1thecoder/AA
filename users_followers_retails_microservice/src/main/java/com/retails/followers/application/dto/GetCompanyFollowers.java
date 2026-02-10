package com.retails.followers.application.dto;

import com.utils.application.RequestContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GetCompanyFollowers implements RequestContract {
    public Long fk_retail_company_id;
}
