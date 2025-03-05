package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDaoFileImpl implements OrderDao {

    private Map<Integer, Order> orders = new HashMap<>();


    @Override
    public Order addOrder(int orderNumber, Order order) {
        return orders.put(orderNumber, order);  // No need for String conversion
    }


    @Override
    public Order getOrder(int orderNumber) {
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
    public Order removeOrder(int orderNumber, LocalDate date) {
        for (Order order : orders.values()) {
            if (order.getOrderNumber() == orderNumber && order.getOrderDate().equals(date)) {
                return orders.remove(orderNumber);
            }
        }

        return null;
    }


    @Override
    public void saveOrder() {

    }
}
