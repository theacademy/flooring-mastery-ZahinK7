package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao {

    Order addOrder(int orderNumber, Order order);

    Order getOrder(int orderNumber);

    List<Order> getOrdersByDate(LocalDate Date);

    Order getOrderByNumberAndDate(int orderNumber, LocalDate date);

    Order removeOrder(int orderNumber, LocalDate Date);

    Order editOrder(int orderNumber, LocalDate date, Order updatedOrder);

    void saveOrder();
}


