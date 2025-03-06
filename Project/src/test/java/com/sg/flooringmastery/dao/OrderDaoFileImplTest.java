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
        Order newOrder = new Order(1);
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
        newOrder.setOrderDate(LocalDate.now());

        // Act
        orderDao.addOrder(newOrder.getOrderNumber(), newOrder);
        Order retrievedOrder = orderDao.getOrderByNumberAndDate(newOrder.getOrderNumber(), newOrder.getOrderDate());

        // Assert
        assertNotNull(retrievedOrder, "Order should not be null");
        assertEquals(newOrder.getOrderNumber(), retrievedOrder.getOrderNumber(), "Order numbers should match");
        assertEquals(newOrder.getCustomerName(), retrievedOrder.getCustomerName(), "Customer names should match");
    }

    @Test
    void testGetOrdersByDate() {
        // Arrange
        LocalDate today = LocalDate.now();
        Order order1 = new Order(2);
        order1.setCustomerName("Alice");
        order1.setState("WA");
        order1.setTaxRate(new BigDecimal("9.25"));
        order1.setProductType("Tile");
        order1.setArea(new BigDecimal("120"));
        order1.setCostPerSquareFoot(new BigDecimal("3.50"));
        order1.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order1.setMaterialCost(new BigDecimal("420.00"));
        order1.setLaborCost(new BigDecimal("498.00"));
        order1.setTax(new BigDecimal("84.26"));
        order1.setTotal(new BigDecimal("1002.26"));
        order1.setOrderDate(today);

        Order order2 = new Order(3);
        order2.setCustomerName("Bob");
        order2.setState("TX");
        order2.setTaxRate(new BigDecimal("4.45"));
        order2.setProductType("Laminate");
        order2.setArea(new BigDecimal("200"));
        order2.setCostPerSquareFoot(new BigDecimal("1.75"));
        order2.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order2.setMaterialCost(new BigDecimal("350.00"));
        order2.setLaborCost(new BigDecimal("420.00"));
        order2.setTax(new BigDecimal("34.45"));
        order2.setTotal(new BigDecimal("804.45"));
        order2.setOrderDate(today);

        orderDao.addOrder(order1.getOrderNumber(), order1);
        orderDao.addOrder(order2.getOrderNumber(), order2);

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
        Order newOrder = new Order(4);
        newOrder.setCustomerName("To Be Removed");
        newOrder.setState("KY");
        newOrder.setTaxRate(new BigDecimal("6.00"));
        newOrder.setProductType("Wood");
        newOrder.setArea(new BigDecimal("150"));
        newOrder.setCostPerSquareFoot(new BigDecimal("5.15"));
        newOrder.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        newOrder.setMaterialCost(new BigDecimal("772.50"));
        newOrder.setLaborCost(new BigDecimal("712.50"));
        newOrder.setTax(new BigDecimal("89.10"));
        newOrder.setTotal(new BigDecimal("1574.10"));
        newOrder.setOrderDate(today);

        orderDao.addOrder(newOrder.getOrderNumber(), newOrder);

        // Act
        Order removedOrder = orderDao.removeOrder(newOrder.getOrderNumber(), today);
        Order shouldBeNull = orderDao.getOrderByNumberAndDate(newOrder.getOrderNumber(), today);

        // Assert
        assertNotNull(removedOrder, "Removed order should not be null");
        assertEquals(newOrder.getOrderNumber(), removedOrder.getOrderNumber(), "Order numbers should match");
        assertNull(shouldBeNull, "Order should no longer exist after removal");
    }

    @Test
    void testEditOrder() {
        // Arrange
        LocalDate today = LocalDate.now();
        Order originalOrder = new Order(5);
        originalOrder.setCustomerName("Original Name");
        originalOrder.setState("CA");
        originalOrder.setTaxRate(new BigDecimal("25.00"));
        originalOrder.setProductType("Wood");
        originalOrder.setArea(new BigDecimal("200"));
        originalOrder.setCostPerSquareFoot(new BigDecimal("5.15"));
        originalOrder.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
        originalOrder.setMaterialCost(new BigDecimal("1030.00"));
        originalOrder.setLaborCost(new BigDecimal("950.00"));
        originalOrder.setTax(new BigDecimal("497.50"));
        originalOrder.setTotal(new BigDecimal("2477.50"));
        originalOrder.setOrderDate(today);

        orderDao.addOrder(originalOrder.getOrderNumber(), originalOrder);

        // Updated order
        Order updatedOrder = new Order(5);
        updatedOrder.setCustomerName("Updated Name");
        updatedOrder.setState("CA");
        updatedOrder.setTaxRate(new BigDecimal("25.00"));
        updatedOrder.setProductType("Tile");
        updatedOrder.setArea(new BigDecimal("250"));
        updatedOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
        updatedOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        updatedOrder.setMaterialCost(new BigDecimal("875.00"));
        updatedOrder.setLaborCost(new BigDecimal("1037.50"));
        updatedOrder.setTax(new BigDecimal("478.12"));
        updatedOrder.setTotal(new BigDecimal("2390.62"));
        updatedOrder.setOrderDate(today);

        // Act
        orderDao.editOrder(5, today, updatedOrder);
        Order retrievedOrder = orderDao.getOrderByNumberAndDate(5, today);

        // Assert
        assertNotNull(retrievedOrder, "Updated order should not be null");
        assertEquals("Updated Name", retrievedOrder.getCustomerName(), "Customer name should be updated");
        assertEquals("Tile", retrievedOrder.getProductType(), "Product type should be updated");
        assertEquals(new BigDecimal("250"), retrievedOrder.getArea(), "Area should be updated");
    }
}
