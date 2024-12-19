package com.food.ordering.system.order.service.dataaccess.customer.adapter;

import com.food.ordering.system.order.service.dataaccess.customer.mapper.CustomerDataAccessMaper;
import com.food.ordering.system.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.ports.input.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMaper customerDataAccessMaper;
    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository,
                                  CustomerDataAccessMaper customerDataAccessMaper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerDataAccessMaper = customerDataAccessMaper;
    }
    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return this.customerJpaRepository.findById(customerId).map(customerDataAccessMaper::customerEntityToCustomer);
    }
}
