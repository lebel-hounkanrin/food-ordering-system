package com.food.ordering.system.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class OrderAddress {
    @NotNull
    @Max(value = 10)
    private String postalCode;
    @Max(value = 50)
    @NotNull
    private String street;
    @NotNull
    @Max(value = 50)
    private String city;
}
