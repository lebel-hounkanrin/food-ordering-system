package com.food.ordering.payment.service.domain.valueobject;

import com.food.ordering.system.domain.valueObject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {
    public PaymentId(UUID value) {
        super(value);
    }
}
