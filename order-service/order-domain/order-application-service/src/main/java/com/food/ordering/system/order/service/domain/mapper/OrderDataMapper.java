package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueObject.CustomerId;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.ProductId;
import com.food.ordering.system.domain.valueObject.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream().map(orderItem ->
                    new Product(new ProductId(orderItem.getProductId()))).collect(Collectors.toList())
                ).build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .items(orderItemsToorderItemsEntities(createOrderCommand.getItems()))
                .price(new Money(createOrderCommand.getPrice()))
                .deliveryAdress(orderAddressToStreetAddress(createOrderCommand.getOrderAdress()))
                .build();

    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order) {
            return CreateOrderResponse.builder()
                    .orderStatus(order.getOrderStatus())
                    .orderTrackingId(order.getTrackingId().getValue())
                    .build();
    }

    private List<OrderItem> orderItemsToorderItemsEntities(List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> items) {
        return items.stream().map(orderItem ->
                OrderItem.builder().product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getUnitPrice()))
                        .subTotal(new Money(orderItem.getTotalPrice()))
                        .quantity(orderItem.getQuantity())
                        .build()
        ).collect(Collectors.toList());
    }


    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAdress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAdress.getStreet(),
                orderAdress.getCity(),
                orderAdress.getPostalCode()
        );
    }
}
