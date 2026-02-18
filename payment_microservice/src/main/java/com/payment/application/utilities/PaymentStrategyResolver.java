package com.payment.application.utilities;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentStrategyResolver {

    private final Map<PaymentMethod, PaymentStrategy> strategies = new HashMap<>();

    public PaymentStrategyResolver(List<PaymentStrategy> strategyList) {
        for (PaymentStrategy strategy : strategyList) {
            strategies.put(strategy.getMethod(), strategy);
        }
    }

    public PaymentStrategy resolve(PaymentMethod method) {
        return strategies.get(method);
    }
}
