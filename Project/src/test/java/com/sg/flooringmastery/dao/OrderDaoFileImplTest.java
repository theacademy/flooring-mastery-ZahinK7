package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoFileImplTest {

    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDaoFileImpl(); // Initialize before each test
    }

    @Test
    void testAddAndGetOrder() {
        // Arrange
        LocalDate today = LocalDate.now();
        int orderNumber = orderDao.generateOrderNumber(today); // Generate order number
        Order newOrder = new Order(orderNumber);

        newOrder.setCustomerName("Test Customer");
        newOrder.setState("TX");
        newOrder.setTaxRate(new BigDecimal("4.45"));
        newOrder.setProductType("Carpet");
        newOrder.setArea(new BigDecimal("100"));
        newOrder.setCostPerSquareFoot(new BigDecimal("2.25"));
        newOrder.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        newOrder.setMaterialCost(new BigDecimal("225.00"));
        newOrder.setLaborCost(new BigDecimal("210.00"));
        newOrder.setTax(new BigDecimal("19.31"));
        newOrder.setTotal(new BigDecimal("454.31"));
        newOrder.setOrderDate(today);

        // Act
        orderDao.addOrder(orderNumber, newOrder);
        Order retrievedOrder = orderDao.getOrderByNumberAndDate(orderNumber, today);

        // Assert
        assertNotNull(retrievedOrder, "Order should not be null");
        assertEquals(orderNumber, retrievedOrder.getOrderNumber(), "Order numbers should match");
        assertEquals(newOrder.getCustomerName(), retrievedOrder.getCustomerName(), "Customer names should match");
    }

    @Test
    void testGetOrdersByDate() {
        // Arrange
        LocalDate today = LocalDate.now();

        int orderNumber1 = orderDao.generateOrderNumber(today);
        Order order1 = new Order(orderNumber1);
        order1.setCustomerName("Alice");
        order1.setState("WA");
        order1.setTaxRate(new BigDecimal("9.25"));
        order1.setProductType("Tile");
        order1.setArea(new BigDecimal("120"));
        order1.setOrderDate(today);

        orderDao.addOrder(orderNumber1, order1);

        // Ensure second order gets a unique number
        int orderNumber2 = orderDao.generateOrderNumber(today);
        if (orderNumber2 == orderNumber1) {
            orderNumber2++; // Ensure different order numbers
        }

        Order order2 = new Order(orderNumber2);
        order2.setCustomerName("Bob");
        order2.setState("TX");
        order2.setTaxRate(new BigDecimal("4.45"));
        order2.setProductType("Laminate");
        order2.setArea(new BigDecimal("200"));
        order2.setOrderDate(today);

        orderDao.addOrder(orderNumber2, order2);

        // Debugging: Print all orders in DAO
        System.out.println("All orders stored in DAO:");
        List<Order> allOrders = orderDao.getOrdersByDate(today);
        for (Order order : allOrders) {
            System.out.println(order.getOrderNumber() + " - " + order.getCustomerName());
        }

        // Act
        List<Order> orders = orderDao.getOrdersByDate(today);

        // Assert
        assertNotNull(orders, "Order list should not be null");
        assertEquals(2, orders.size(), "There should be exactly 2 orders for today");
    }


    @Test
    void testRemoveOrder() {
        // Arrange
        LocalDate today = LocalDate.now();
        int orderNumber = orderDao.generateOrderNumber(today);
        Order newOrder = new Order(orderNumber);
        newOrder.setCustomerName("To Be Removed");
        newOrder.setOrderDate(today);

        orderDao.addOrder(orderNumber, newOrder);

        // Act
        Order removedOrder = orderDao.removeOrder(orderNumber, today);
        Order shouldBeNull = orderDao.getOrderByNumberAndDate(orderNumber, today);

        // Assert
        assertNotNull(removedOrder, "Removed order should not be null");
        assertEquals(orderNumber, removedOrder.getOrderNumber(), "Order numbers should match");
        assertNull(shouldBeNull, "Order should no longer exist after removal");
    }

    @Test
    void testEditOrder() {
        // Arrange
        LocalDate today = LocalDate.now();
        int orderNumber = orderDao.generateOrderNumber(today);

        Order originalOrder = new Order(orderNumber);
        originalOrder.setCustomerName("Original Name");
        originalOrder.setState("CA");
        originalOrder.setProductType("Wood");
        originalOrder.setOrderDate(today);

        orderDao.addOrder(orderNumber, originalOrder);

        // Updated order
        Order updatedOrder = new Order(orderNumber);
        updatedOrder.setCustomerName("Updated Name");
        updatedOrder.setProductType("Tile");
        updatedOrder.setOrderDate(today);

        // Act
        orderDao.editOrder(orderNumber, today, updatedOrder);
        Order retrievedOrder = orderDao.getOrderByNumberAndDate(orderNumber, today);

        // Assert
        assertNotNull(retrievedOrder, "Updated order should not be null");
        assertEquals("Updated Name", retrievedOrder.getCustomerName(), "Customer name should be updated");
        assertEquals("Tile", retrievedOrder.getProductType(), "Product type should be updated");
    }
}
