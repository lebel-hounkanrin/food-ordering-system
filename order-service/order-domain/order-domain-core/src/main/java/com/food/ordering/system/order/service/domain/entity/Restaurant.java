package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.RestaurantId;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        isActive = builder.isActive;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isActive() {
        return isActive;
    }

    public List<Product> getProducts() {
        return products;
    }

    private final boolean isActive;


    public static final class Builder {
        private RestaurantId restaurantId;
        private List<Product> products;
        private boolean isActive;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder isActive(boolean val) {
            isActive = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
