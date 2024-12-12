package com.example.wilda.service;

import java.util.List;

import com.example.wilda.entity.Order;
import com.example.wilda.model.request.CreateOrderRequest;

public interface OrderService {
    
    void placeOrder(CreateOrderRequest request);

    List<Order> getList();
    
    List<Order> getOrderByUser(String username);
}
