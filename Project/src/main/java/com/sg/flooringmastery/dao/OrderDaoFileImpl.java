package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoFileImpl implements OrderDao {

    private Map<Integer, Order> orders = new HashMap<>();

    public OrderDaoFileImpl() {
    }

    @Override
    public Order addOrder(int orderNumber, Order order) {
        if (order.getOrderDate() == null) {
            throw new IllegalArgumentException("Order date must be set before adding the order.");
        }
        return orders.put(orderNumber, order);
    }


    @Override
    public List<Order> getOrdersByDate(LocalDate date) {
        List<Order> ordersOnDate = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getOrderDate().equals(date)) {
                ordersOnDate.add(order);
            }
        }
        return ordersOnDate;
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
    public int generateOrderNumber(LocalDate date) {
        List<Order> ordersOnDate = getOrdersByDate(date);
        if (ordersOnDate.isEmpty()) {
            return 1;
        }
        return ordersOnDate.stream()
                .mapToInt(Order::getOrderNumber)
                .max()
                .orElse(0) + 1;
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
        Order existingOrder = getOrderByNumberAndDate(orderNumber, date);
        if (existingOrder != null) {
            orders.put(orderNumber, updatedOrder);
            return updatedOrder;
        }
        return null;
    }

    @Override
    public void exportAllOrders(LocalDate date) throws FlooringMasteryDaoException {
        String directory = "Project/SampleFileData/SampleFileData/Orders/";
        String fileName = directory + "Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";

        File folder = new File(directory);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

            for (Order order : getOrdersByDate(date)) {
                writer.println(order.getOrderNumber() + "," +
                        order.getCustomerName() + "," +
                        order.getState() + "," +
                        order.getTaxRate() + "," +
                        order.getProductType() + "," +
                        order.getArea() + "," +
                        order.getCostPerSquareFoot() + "," +
                        order.getLaborCostPerSquareFoot() + "," +
                        order.getMaterialCost() + "," +
                        order.getLaborCost() + "," +
                        order.getTax() + "," +
                        order.getTotal());
            }

        } catch (IOException e) {
            throw new FlooringMasteryDaoException("Error exporting orders to file: " + fileName, e);
        }
    }

    @Override
    public void saveOrder() throws FlooringMasteryDaoException {
        for (Order order : orders.values()) {
            exportAllOrders(order.getOrderDate());
        }
    }
}
