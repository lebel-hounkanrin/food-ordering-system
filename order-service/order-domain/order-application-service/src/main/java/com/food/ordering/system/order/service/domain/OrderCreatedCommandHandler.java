package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.exception.DomainException;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreatedCommandHandler {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreatedCommandHandler(OrderDomainService orderDomainService, OrderRepository orderRepository,
                                      RestaurantRepository restaurantRepository, CustomerRepository customerRepository,
                                      OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.customerRepository = customerRepository;
        this.orderDataMapper = orderDataMapper;
    }

//    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
            checkCustomer(createOrderCommand.getCustomerId());
            Restaurant restaurant = checkRestaurant(createOrderCommand);
            Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
            OrderCreatedEvent orderCreatedEvent= orderDomainService.validateAndInitOrder(order, restaurant);
            Order savedOrder = saveOrder(order);
            return orderDataMapper.OrderToCreateOrderResponse(savedOrder);
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
       Restaurant restaurant=  orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
       Optional<Restaurant> restaurantInformation = restaurantRepository.findRestaurantInformation(restaurant);
       if (restaurantInformation.isEmpty()) {
           log.warn("Could not find restaurant with id {}", createOrderCommand.getRestaurantId());
           throw new OrderDomainException("Could not find customer with id " + createOrderCommand.getRestaurantId());
       }
       return restaurantInformation.get();
    }

    private void checkCustomer(@NotNull UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with id {}", customerId);
            throw new OrderDomainException("Could not find customer with id " + customerId);
        }
    }

    private Order saveOrder(Order order){
        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            throw new OrderDomainException("Could not save order");
        }
        log.info("Order with id {} is saved", savedOrder.getId().getValue());
        return savedOrder;
    }
}
