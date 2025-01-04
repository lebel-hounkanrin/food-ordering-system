package com.food.ordering.payment.service.domain.valueobject;

import com.food.ordering.system.domain.valueObject.BaseId;

import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {
    public CreditEntryId(UUID value) {
        super(value);
    }
}
