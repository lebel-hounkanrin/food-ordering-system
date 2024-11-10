package com.food.ordering.system.order.servide.domain;

import com.food.ordering.system.domain.valueObject.*;
import com.food.ordering.system.order.service.domain.OrderDomainService;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("51db372e-696d-4829-98b0-0341c41cccc2");
    private final UUID PRODUCT_ID = UUID.fromString("1bd7bfa6-72c4-4668-81d3-487cbcb007bd");
    private final UUID RESTAURANT_ID = UUID.fromString("b02b0769-2848-4064-883a-0c99f42fe291");
    private final UUID ORDER_ID = UUID.randomUUID();
    private final BigDecimal PRICE = new BigDecimal("100.00");

    @BeforeAll
    public void setUp() {
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .orderAdress(
                        OrderAddress.builder()
                                .street("rue des grandes pousses")
                                .city("limoges")
                                .postalCode("87000")
                                .build()
                )
                .price(PRICE)
                .items(List.of(
                        OrderItem.builder()
                                .quantity(1)
                                .productId(PRODUCT_ID)
                                .unitPrice(new BigDecimal("30.00"))
                                .totalPrice(PRICE)
                                .build(),
                        OrderItem.builder()
                                .quantity(1)
                                .productId(PRODUCT_ID)
                                .unitPrice(new BigDecimal("70.00"))
                                .totalPrice(PRICE)
                                .build()
                ))
                .build();
        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .orderAdress(
                        OrderAddress.builder()
                                .street("rue des grandes pousses")
                                .city("limoges")
                                .postalCode("87000")
                                .build()
                )
                .price(new BigDecimal("70.00"))
                .items(List.of(
                        OrderItem.builder()
                                .quantity(1)
                                .productId(PRODUCT_ID)
                                .unitPrice(new BigDecimal("30.00"))
                                .totalPrice(PRICE)
                                .build(),
                        OrderItem.builder()
                                .quantity(1)
                                .productId(PRODUCT_ID)
                                .unitPrice(new BigDecimal("70.00"))
                                .totalPrice(PRICE)
                                .build()
                ))
                .build();
        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "eggs", new Money(new BigDecimal("100"))),
                        new Product(new ProductId(PRODUCT_ID), "milks", new Money(new BigDecimal("100")))
                ))
                .isActive(true)
                .build();
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        Mockito.when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        Mockito.when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand))).thenReturn(Optional.of(restaurantResponse));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        Assertions.assertEquals(createOrderResponse.getOrderStatus(), OrderStatus.PENDING);
        Assertions.assertEquals(createOrderResponse.getMessage(), "order created successfully");
        Assertions.assertNotNull(createOrderResponse.getOrderTrackingId());
    }


}
