package com.product.application.dto;

import com.utils.application.RequestContract;
import lombok.Builder;
import lombok.Data;
import org.apache.catalina.User;


@Builder
@Data
public class RegisterProductListRequest implements RequestContract {

    private User administrator;
    private String productName,productImagePath;
}
