package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public List<Order> getOrdersByDate(LocalDate date) {
        List<Order> ordersOnDate = new ArrayList<>();

        // Loop through the orders and check if the order's date matches the provided date
        for (Order order : orders.values()) {
            if (order.getOrderDate().equals(date)) {
                ordersOnDate.add(order);
            }
        }

        return ordersOnDate;  // Return the list of orders matching the date
    }


    @Override
    public Order removeOrder(String orderNumber) {
        return null;
    }

    @Override
    public void saveOrder() {

    }
}
