package com.food.ordering.system.payment.service.domain.ports.output.message.listener;

import com.food.ordering.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;

public interface PaymentCompleteMessagePublisher extends DomainEventPublisher<PaymentCompletedEvent> {
}
