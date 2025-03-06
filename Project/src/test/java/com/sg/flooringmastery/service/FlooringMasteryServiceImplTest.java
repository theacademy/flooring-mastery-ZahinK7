package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.dao.TaxDao;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlooringMasteryServiceImplTest {

    private FlooringMasteryServiceImpl service;
    private ProductDao productDaoMock;
    private TaxDao taxDaoMock;
    private OrderDao orderDaoMock;

    @BeforeEach
    void setUp() {
        // Create mock objects
        productDaoMock = Mockito.mock(ProductDao.class);
        taxDaoMock = Mockito.mock(TaxDao.class);
        orderDaoMock = Mockito.mock(OrderDao.class);

        // Initialize service with mocked dependencies
        service = new FlooringMasteryServiceImpl(productDaoMock, taxDaoMock, orderDaoMock);
    }

    @Test
    void testCalculateMaterialCost() {
        // Arrange
        Order order = new Order(1);
        order.setArea(new BigDecimal("100"));
        order.setCostPerSquareFoot(new BigDecimal("2.25"));

        // Act
        BigDecimal materialCost = service.calculateMaterialCost(order);

        // Assert
        assertEquals(new BigDecimal("225.00"), materialCost, "Material cost calculation is incorrect");
    }

    @Test
    void testCalculateLaborCost() {
        // Arrange
        Order order = new Order(1);
        order.setArea(new BigDecimal("150"));
        order.setLaborCostPerSquareFoot(new BigDecimal("3.50"));

        // Act
        BigDecimal laborCost = service.calculateLaborCost(order);

        // Assert
        assertEquals(new BigDecimal("525.00"), laborCost, "Labor cost calculation is incorrect");
    }

    @Test
    void testCalculateTax() {
        // Arrange
        Order order = new Order(1);
        order.setMaterialCost(new BigDecimal("200.00"));
        order.setLaborCost(new BigDecimal("150.00"));
        order.setTaxRate(new BigDecimal("5.50")); // 5.5% tax

        // Act
        BigDecimal tax = service.calculateTax(order);

        // Assert
        assertEquals(new BigDecimal("19.25"), tax, "Tax calculation is incorrect");
    }

    @Test
    void testCalculateTotalCost() {
        // Arrange
        Order order = new Order(1);
        order.setMaterialCost(new BigDecimal("200.00"));
        order.setLaborCost(new BigDecimal("150.00"));
        order.setTax(new BigDecimal("19.25"));

        // Act
        BigDecimal totalCost = service.calculateTotalCost(order);

        // Assert
        assertEquals(new BigDecimal("369.25"), totalCost, "Total cost calculation is incorrect");
    }

    @Test
    void testValidateOrderDate() {
        // Arrange
        Order order1 = new Order(1);
        order1.setOrderDate(LocalDate.now().plusDays(1)); // Future date

        Order order2 = new Order(2);
        order2.setOrderDate(LocalDate.now()); // Today (invalid)

        // Assert
        assertTrue(service.validateOrderDate(order1), "Future date should be valid");
        assertFalse(service.validateOrderDate(order2), "Today's date should be invalid");
    }

    @Test
    void testValidateCustomerName() {
        // Arrange
        Order validOrder = new Order(1);
        validOrder.setCustomerName("John Doe");

        Order invalidOrder = new Order(2);
        invalidOrder.setCustomerName("John@Doe!!");

        // Assert
        assertTrue(service.validateCustomerName(validOrder), "Valid name should pass");
        assertFalse(service.validateCustomerName(invalidOrder), "Invalid name should fail");
    }

    @Test
    void testValidateArea() {
        // Arrange
        Order validOrder = new Order(1);
        validOrder.setArea(new BigDecimal("100"));

        Order invalidOrder = new Order(2);
        invalidOrder.setArea(new BigDecimal("50"));

        // Assert
        assertTrue(service.validateArea(validOrder), "Area of 100 should be valid");
        assertFalse(service.validateArea(invalidOrder), "Area below 100 should be invalid");
    }

    @Test
    void testPopulateOrderCosts() {
        // Arrange
        Order order = new Order(1);
        order.setState("TX");
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("100"));

        // Mock taxDao and productDao responses
        when(taxDaoMock.getTaxByState("TX")).thenReturn(new Tax("TX", "Texas", new BigDecimal("4.45")));
        when(productDaoMock.getProduct("Carpet")).thenReturn(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));

        // Act
        service.populateOrderCosts(order);

        // Assert
        assertEquals(new BigDecimal("4.45"), order.getTaxRate(), "Tax rate should be 4.45%");
        assertEquals(new BigDecimal("2.25"), order.getCostPerSquareFoot(), "Cost per square foot should be 2.25");
        assertEquals(new BigDecimal("2.10"), order.getLaborCostPerSquareFoot(), "Labor cost per square foot should be 2.10");
        assertEquals(new BigDecimal("225.00"), order.getMaterialCost(), "Material cost should be correctly calculated");
        assertEquals(new BigDecimal("210.00"), order.getLaborCost(), "Labor cost should be correctly calculated");
        assertEquals(new BigDecimal("19.36"), order.getTax(), "Tax should be correctly calculated");
        assertEquals(new BigDecimal("454.36"), order.getTotal(), "Total should be correctly calculated");
    }
}
