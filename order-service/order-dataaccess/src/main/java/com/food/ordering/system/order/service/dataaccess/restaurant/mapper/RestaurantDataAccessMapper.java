package com.food.ordering.system.order.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.ProductId;
import com.food.ordering.system.domain.valueObject.RestaurantId;
import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessMapperException;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {
    public List<UUID> restaurantToRestaurantProduct(Restaurant restaurant) {
        return restaurant.getProducts().stream().map(
                product -> product.getId().getValue()
        ).collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity = restaurantEntities.stream().findFirst().orElseThrow(
                () -> new RestaurantDataAccessMapperException("Restaurant not found")
        );
        List<Product> products = restaurantEntities.stream().map(entity ->
                new Product(new ProductId(entity.getProductId()),
                        entity.getProductName(),
                        new Money(entity.getProductPrice()))
                ).collect(Collectors.toList());
        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(products)
                .isActive(restaurantEntity.isRestaurantActive())
                .build();
    }
}
