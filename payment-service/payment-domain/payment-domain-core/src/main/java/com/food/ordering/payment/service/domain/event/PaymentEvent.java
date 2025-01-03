package com.food.ordering.payment.service.domain.event;

import com.food.ordering.payment.service.domain.entity.Payment;
import com.food.ordering.system.domain.event.DomainEvent;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentEvent implements DomainEvent {

    private final Payment payment;
    private final ZonedDateTime createdAt;
    private final List<String> failureMessages;

    public PaymentEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        this.payment = payment;
        this.createdAt = createdAt;
        this.failureMessages = failureMessages;
    }

    public Payment getPayment() {
        return payment;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
}
