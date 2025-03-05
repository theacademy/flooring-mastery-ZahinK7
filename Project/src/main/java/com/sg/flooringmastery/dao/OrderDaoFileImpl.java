package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDaoFileImpl implements OrderDao {

    private Map<String,Order> orders = new HashMap<>();

    @Override
    public Order addOrder(int orderNumber, Order order) {
        Order prevOrder = orders.put(String.valueOf(orderNumber),order);

        return prevOrder;
    }

    @Override
    public Order getOrder(String orderNumber) {
        return null;
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate Date) {
        return List.of();
    }

    @Override
    public Order removeOrder(String orderNumber) {
        return null;
    }

    @Override
    public void saveOrder() {

    }
}
