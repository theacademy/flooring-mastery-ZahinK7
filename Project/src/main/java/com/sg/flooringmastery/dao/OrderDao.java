package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao {

    Order addOrder(int orderNumber, Order order);

    List<Order> getOrdersByDate(LocalDate date);

    Order getOrderByNumberAndDate(int orderNumber, LocalDate date);

    int generateOrderNumber(LocalDate date);

    Order removeOrder(int orderNumber, LocalDate date);

    Order editOrder(int orderNumber, LocalDate date, Order updatedOrder);

    void exportAllOrders(LocalDate date) throws FlooringMasteryDaoException;

    void saveOrder() throws FlooringMasteryDaoException;
}
