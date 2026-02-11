package com.product.application.dto;

import com.users.application.entities.Users;
import com.utils.application.RequestContract;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterProductLitersRequest implements RequestContract {

    private String liters;

    @ManyToOne
    @JoinColumn(name = "administrator_id")
    private Users administrator;

    private Byte bottleReturnableStatus;
}
