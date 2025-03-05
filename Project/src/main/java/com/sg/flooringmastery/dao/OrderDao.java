package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao {

    Order addOrder(int orderNumber, Order order);

    Order getOrder(int orderNumber);

    List<Order> getOrdersByDate(LocalDate Date);

    Order removeOrder(int orderNumber, LocalDate Date);

    void saveOrder();
}


