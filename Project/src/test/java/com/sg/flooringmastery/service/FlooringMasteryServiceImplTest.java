package com.sg.flooringmastery.service;
import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.dto.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryServiceImplTest {

    private FlooringMasteryServiceStub serviceStub;
    private ProductDao productDao;
    private TaxDao taxDao;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        try {

            productDao = new ProductDaoFileImpl();
            taxDao = new TaxDaoFileImpl();
            orderDao = new OrderDaoFileImpl();


            serviceStub = new FlooringMasteryServiceStub();
        } catch (FlooringMasteryDaoException e) {
            fail("Failed to initialize DAOs due to exception: " + e.getMessage());
        }
    }

    @Test
    void testCalculateMaterialCost() {
        Order order = new Order(1);
        assertEquals(new BigDecimal("225.00"), serviceStub.calculateMaterialCost(order));
    }

    @Test
    void testCalculateLaborCost() {
        Order order = new Order(1);
        assertEquals(new BigDecimal("210.00"), serviceStub.calculateLaborCost(order));
    }

    @Test
    void testCalculateTax() {
        Order order = new Order(1);
        assertEquals(new BigDecimal("19.36"), serviceStub.calculateTax(order));
    }

    @Test
    void testCalculateTotalCost() {
        Order order = new Order(1);
        assertEquals(new BigDecimal("454.36"), serviceStub.calculateTotalCost(order));
    }

    @Test
    void testPopulateOrderCosts() {
        Order order = new Order(1);
        order.setState("TX");
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("100"));

        assertDoesNotThrow(() -> serviceStub.populateOrderCosts(order));
        assertEquals(new BigDecimal("4.45"), order.getTaxRate());
        assertEquals(new BigDecimal("2.25"), order.getCostPerSquareFoot());
        assertEquals(new BigDecimal("2.10"), order.getLaborCostPerSquareFoot());
        assertEquals(new BigDecimal("225.00"), order.getMaterialCost());
        assertEquals(new BigDecimal("210.00"), order.getLaborCost());
        assertEquals(new BigDecimal("19.36"), order.getTax());
        assertEquals(new BigDecimal("454.36"), order.getTotal());
    }

    @Test
    void testPopulateOrderCostsThrowsExceptionForInvalidState() {
        Order order = new Order(1);
        order.setState("INVALID_STATE");
        order.setProductType("Carpet");

        assertThrows(FlooringMasteryServiceException.class, () -> serviceStub.populateOrderCosts(order));
    }

    @Test
    void testPopulateOrderCostsThrowsExceptionForInvalidProduct() {
        Order order = new Order(1);
        order.setState("TX");
        order.setProductType("INVALID_PRODUCT");

        assertThrows(FlooringMasteryServiceException.class, () -> serviceStub.populateOrderCosts(order));
    }
}
