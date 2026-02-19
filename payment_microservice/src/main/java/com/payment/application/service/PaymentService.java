package com.payment.application.service;

import com.cart.application.entities.Orders;
import com.cart.application.exceptions.OrderNotFoundException;
import com.cart.application.repositories.OrdersRepository;
import com.payment.application.dtos.PaymentRequest;
import com.payment.application.dtos.PaymentResponseDTO;
import com.payment.application.entity.Payment;
import com.payment.application.exceptions.PaymentStrategyNotFoundException;
import com.payment.application.repository.PaymentRepository;
import com.payment.application.utilities.*;
import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;
import com.utils.application.ServiceContract;
import com.utils.application.globalExceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.utils.application.ExceptionHandler.throwExceptionAndReport;

@Service
@RequiredArgsConstructor
public class PaymentService implements ServiceContract {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentStrategyResolver resolver;

    public List<PaymentResponseDTO> pay(RequestContract requestContract) {

        if (requestContract instanceof PaymentRequest request) {
            Orders order = ordersRepository.findById(request.getOrderId())
                    .orElseThrow(() -> {var errorMessage = "Order not found";
                        var resolveIssue = "Provide correct order id";
                        return throwExceptionAndReport(new OrderNotFoundException(errorMessage), errorMessage, resolveIssue);
                    });

            Payment payment = Payment.builder()
                    .order(order)
                    .amount(order.getTotalAmount())
                    .method(request.getPaymentMethod())
                    .status(PaymentStatus.PENDING)
                    .createdAt(LocalDateTime.now().toString())
                    .build();

            PaymentStrategy strategy = resolver.resolve(request.getPaymentMethod());

            if (strategy instanceof CardPaymentStrategy cardPaymentStrategy) {
                cardPaymentStrategy.setCardDetails(request.getCardDetails());
                PaymentStatus status = cardPaymentStrategy.processPayment(payment);

                payment.setStatus(status);
                payment.setTransactionReference(UUID.randomUUID().toString());
                return mapToResponse(paymentRepository.save(payment));
            } else {
                var errorMessage = "Payment strategy not found";
                var resolveIssue = "Use correct  payment strategy ";
                throw throwExceptionAndReport(new PaymentStrategyNotFoundException(errorMessage), errorMessage, resolveIssue);

            }
        }else{
            var errorMessage = "Request sent is not correct to submit PaymentRequest";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new ServiceRunnerNotFoundException(errorMessage), errorMessage, resolveIssue);
        }
    }
    private List<PaymentResponseDTO> mapToResponse(Payment payment) {
        return List.of(PaymentResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrder().getOrderId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionReference(payment.getTransactionReference())
                .createdAt(payment.getCreatedAt())
                .build());
    }

    @Override
    public List<? extends ResponseContract> call(String serviceRunner, RequestContract request) {

        if (serviceRunner.equals("pay")) {
         return  pay(request);
        }else{
            var errorMessage = "no service executed for payment service, service runner not found";
            var resolveIssue = "Use correct service runner";
            throw throwExceptionAndReport(new ServiceRunnerNotFoundException(errorMessage), errorMessage, resolveIssue);
        }

    }
}

