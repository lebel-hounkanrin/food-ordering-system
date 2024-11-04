package com.food.ordering.system.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderItem {
    @NotNull
    private UUID productId;
    @NotNull
    private int quantity;
    @NotNull
    private BigDecimal unitPrice;
    @NotNull
    private BigDecimal totalPrice;
}
