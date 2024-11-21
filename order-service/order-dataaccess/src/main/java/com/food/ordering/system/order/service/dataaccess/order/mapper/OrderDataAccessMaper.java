package com.food.ordering.system.order.service.dataaccess.order.mapper;

import com.food.ordering.system.domain.valueObject.*;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDataAccessMaper {

    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(deliveryAddressToAddresEntity(order.getDeliveryAdress()))
                .price(order.getPrice().getAmount())
                .items(orderItemsToOrderItemsEntities(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null  ?
                        order.getFailureMessages().stream().collect(Collectors.joining(",")) : "")
                .build();
        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .price(new Money(orderEntity.getPrice()))
                .orderStatus(orderEntity.getOrderStatus())
                .deliveryAdress(addressEntityToDeliveryAddress(orderEntity.getAddress()))
                .items(orderItemEntitiesToOrderItem(orderEntity.getItems()))
                .failureMessages( orderEntity.getFailureMessages().isEmpty()  ? new ArrayList<>()
                         :
                        Arrays.stream(orderEntity.getFailureMessages().split(",")).toList())
                .build();
    }

    private List<OrderItem> orderItemEntitiesToOrderItem(List<OrderItemEntity> items) {
        return items.stream().map(item ->
                OrderItem.builder()
                        .id(new OrderItemId(item.getId()))
                        .product(new Product(
                                new ProductId(item.getProductId())
                        ))
                        .price(new Money(item.getPrice()))
                        .quantity(item.getQuantity())
                        .subTotal(new Money(item.getSubTotal()))
                        .build())
                .collect(Collectors.toList());
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(address.getId(), address.getStreet(), address.getCity(), address.getPostalCode());
    }

    private List<OrderItemEntity> orderItemsToOrderItemsEntities(List<OrderItem> items) {
        return items.stream().map(item ->
                OrderItemEntity.builder()
                        .id(item.getId().getValue())
                        .price(item.getPrice().getAmount())
                        .quantity(item.getQuantity())
                        .productId(item.getProduct().getId().getValue())
                        .subTotal(item.getSubTotal().getAmount())
                        .build()
        ).collect(Collectors.toList());
    }

    private OrderAddressEntity deliveryAddressToAddresEntity(StreetAddress deliveryAdress) {
        return OrderAddressEntity.builder()
                .id(deliveryAdress.getId())
                .city(deliveryAdress.getCity())
                .postalCode(deliveryAdress.getPostalCode())
                .street(deliveryAdress.getStreet())
                .build();
    }
}
