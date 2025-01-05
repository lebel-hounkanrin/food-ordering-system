package com.food.ordering.system.payment.service.domain.ports.input.message.listener;

import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;

public class PaymentRequestMessageListener {
    void completePayment(PaymentRequest paymentRequest) {}

    void cancelPayment(PaymentRequest paymentRequest) {}
}
