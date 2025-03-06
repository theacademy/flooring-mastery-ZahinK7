package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

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

@Repository  // Marks this as a Spring-managed DAO component
public class OrderDaoFileImpl implements OrderDao {

    private Map<Integer, Order> orders = new HashMap<>();

    public OrderDaoFileImpl() {
        // No need to manually call loadOrders(), we will use @PostConstruct if needed
    }

    @Override
    public Order addOrder(int orderNumber, Order order) {
        return orders.put(orderNumber, order);
    }

    @Override
    public Order getOrder(int orderNumber) {
        return null;
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
    public void exportAllOrders(LocalDate date) {
        String directory = "Project/SampleFileData/SampleFileData/Orders/";  // Set the correct path
        String fileName = directory + "Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";

        // Ensure the directory exists
        File folder = new File(directory);
        if (!folder.exists()) {
            folder.mkdirs();  // Create directory if it doesn’t exist
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

            System.out.println("Export successful: " + fileName);
        } catch (IOException e) {
            System.out.println("Error exporting orders: " + e.getMessage());
        }
    }


    @Override
    public void saveOrder() {
        for (Order order : orders.values()) {
            exportAllOrders(order.getOrderDate()); // Save orders by date
        }
    }
}
