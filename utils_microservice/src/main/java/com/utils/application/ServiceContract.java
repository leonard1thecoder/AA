package com.utils.application;

import java.util.List;

public interface ServiceContract {
    List<? extends ResponseContract> call(String serviceRunner,RequestContract request);
}
