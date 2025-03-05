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
    public Order getOrderByNumberAndDate(int orderNumber, LocalDate date) {
        for (Order order : orders.values()) {
            if (order.getOrderNumber() == orderNumber && order.getOrderDate().equals(date)) {
                return order;
            }
        }
        return null;
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
    public Order editOrder(int orderNumber, LocalDate date, Order updatedOrder) {
        // Find the order by number and date
        Order existingOrder = getOrderByNumberAndDate(orderNumber, date);


        if (existingOrder != null) {

            orders.put(orderNumber, updatedOrder);
            return updatedOrder;
        }

        return null; // Order not found
    }

    //To be implemented
//    public Order editOrder(){
//
//
//    }

    @Override
    public void saveOrder() {

    }
}
