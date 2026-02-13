package com.product.application.dto;

import com.utils.application.RequestContract;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UpdateCompanyProductQuantityRequest implements RequestContract {
    private Long companyProductId;
    private Integer quantity;
}
