package com.food.ordering.system.payment.service.domain.ports.output.repository;

import com.food.ordering.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.domain.valueObject.CustomerId;

import java.util.Optional;
import java.util.UUID;

public interface CreditEntryRepo {
    CreditEntry save(CreditEntry creditEntry);
    Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}
