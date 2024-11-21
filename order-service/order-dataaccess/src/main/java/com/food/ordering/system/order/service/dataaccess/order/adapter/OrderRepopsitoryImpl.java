package com.food.ordering.system.order.service.dataaccess.order.adapter;

import com.food.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMaper;
import com.food.ordering.system.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepopsitoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMaper orderDataAccessMaper;
    public OrderRepopsitoryImpl(OrderJpaRepository orderJpaRepository, OrderDataAccessMaper orderDataAccessMaper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMaper = orderDataAccessMaper;
    }
    @Override
    public Order save(Order order) {
        return orderDataAccessMaper.orderEntityToOrder(orderJpaRepository.save(orderDataAccessMaper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackindId(TrackingId trackindId) {
        return orderJpaRepository.findByTrackingId(trackindId.getValue()).map(orderDataAccessMaper::orderEntityToOrder);
    }
}
