package com.product.application.services;

import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;

import java.util.List;

public interface ProductServicesContract {
    List<? extends ResponseContract> call(String serviceRunner, RequestContract request);
}
