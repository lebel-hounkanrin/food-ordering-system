package com.food.ordering.system.payment.service.domain;

import com.food.ordering.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.payment.service.domain.event.PaymentEvent;
import com.food.ordering.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.service.domain.ports.output.message.listener.PaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.listener.PaymentCompleteMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.listener.PaymentFailedMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {
    private final PaymentRequestHelper paymentRequestHelper;
    private final PaymentCompleteMessagePublisher paymentCompleteMessagePublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledMessagePublisher;
    private final PaymentFailedMessagePublisher paymentFailedMessagePublisher;

    public PaymentRequestMessageListenerImpl(PaymentRequestHelper paymentRequestHelper,
                                             PaymentCompleteMessagePublisher paymentCompleteMessagePublisher,
                                             PaymentCancelledMessagePublisher paymentCancelledMessagePublisher,
                                             PaymentFailedMessagePublisher paymentFailedMessagePublisher) {
        this.paymentRequestHelper = paymentRequestHelper;
        this.paymentCompleteMessagePublisher = paymentCompleteMessagePublisher;
        this.paymentCancelledMessagePublisher = paymentCancelledMessagePublisher;
        this.paymentFailedMessagePublisher = paymentFailedMessagePublisher;
    }

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);
        fireEvent(paymentEvent);

    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistCancelPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    private void fireEvent(PaymentEvent paymentEvent) {
        log.info("Publishing payment event for payment with id: {} and order id: {}",
                paymentEvent.getPayment().getId(), paymentEvent.getPayment().getOrderId());
        if(paymentEvent instanceof PaymentCompletedEvent) {
            paymentCompleteMessagePublisher.publish((PaymentCompletedEvent) paymentEvent);
        } else if(paymentEvent instanceof PaymentCancelledEvent) {
            paymentCancelledMessagePublisher.publish((PaymentCancelledEvent) paymentEvent);
        } else if(paymentEvent instanceof PaymentFailedEvent) {
            paymentFailedMessagePublisher.publish((PaymentFailedEvent) paymentEvent);
        }
    }
}
