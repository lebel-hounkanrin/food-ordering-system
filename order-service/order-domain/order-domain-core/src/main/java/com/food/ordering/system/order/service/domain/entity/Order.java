package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAdress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAdress = builder.deliveryAdress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }
    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAdress() {
        return deliveryAdress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void pay(){
        if (!orderStatus.equals(OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for payment");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve(){
        if (!orderStatus.equals(OrderStatus.PAID)) {
            throw new OrderDomainException("Order is not in correct state for approval");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages){
        if (!orderStatus.equals(OrderStatus.PAID)) {
            throw new OrderDomainException("Order is not in correct state for cancellation");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessage(failureMessages);
    }


    public void cancel(List<String> failureMessages){
        if (!(orderStatus.equals(OrderStatus.CANCELLING) || orderStatus.equals(OrderStatus.PENDING))) {
            throw new OrderDomainException("Order is not in correct state for cancel operation");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessage(failureMessages);
    }

    public void updateFailureMessage(List<String> failureMessages){
        if(this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isBlank()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }


    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if(!price.equals(orderItemsTotal)){
            throw new OrderDomainException("Total price: " + price.getAmount() +
                    " is not equal to Order items total: " + orderItemsTotal.getAmount());
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isValidPrice()){
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() +
                    " is not correct the product: " + orderItem.getProduct().getName());
        }
    }

    private void validateTotalPrice() {
        if(price == null || !price.isGreaterThanZero()){
            throw new OrderDomainException("Total price is greater than zero");
        }
    }

    private void validateInitialOrder() {
        if(orderStatus != null || getId() != null){
            throw new OrderDomainException("Order is not in correct state for initialization");
        }
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem item : items) {
            item.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAdress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAdress(StreetAddress val) {
            deliveryAdress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}