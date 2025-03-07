package com.sg.flooringmastery.service;
import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.OrderDaoFileImpl;
import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.dao.ProductDaoFileImpl;
import com.sg.flooringmastery.dao.TaxDao;
import com.sg.flooringmastery.dao.TaxDaoFileImpl;
import com.sg.flooringmastery.dto.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FlooringMasteryIntegrationTest {

    private FlooringMasteryServiceImpl service;
    private ProductDao productDao;
    private TaxDao taxDao;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        try {

            productDao = new ProductDaoFileImpl();
            taxDao = new TaxDaoFileImpl();
            orderDao = new OrderDaoFileImpl();


            service = new FlooringMasteryServiceImpl(productDao, taxDao, orderDao);
        } catch (Exception e) {
            fail("Failed to initialize DAOs due to exception: " + e.getMessage());
        }
    }

    @Test
    void testPopulateOrderCosts_RealData() {
        Order order = new Order(1);
        order.setState("TX");
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("100"));

        assertDoesNotThrow(() -> service.populateOrderCosts(order));
        assertNotNull(order.getTaxRate(), "Tax rate should be set");
        assertNotNull(order.getCostPerSquareFoot(), "Cost per square foot should be set");
        assertNotNull(order.getLaborCostPerSquareFoot(), "Labor cost per square foot should be set");
        assertNotNull(order.getTotal(), "Total cost should be calculated");
    }

    @Test
    void testPopulateOrderCosts_InvalidState() {
        Order order = new Order(1);
        order.setState("INVALID_STATE");
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("100"));

        assertThrows(FlooringMasteryServiceException.class, () -> service.populateOrderCosts(order),
                "Should throw exception for invalid state");
    }

    @Test
    void testPopulateOrderCosts_InvalidProduct() {
        Order order = new Order(1);
        order.setState("TX");
        order.setProductType("INVALID_PRODUCT");
        order.setArea(new BigDecimal("100"));

        assertThrows(FlooringMasteryServiceException.class, () -> service.populateOrderCosts(order),
                "Should throw exception for invalid product type");
    }

    @Test
    void testGenerateOrderNumber() {
        LocalDate today = LocalDate.now();
        int orderNumber = orderDao.generateOrderNumber(today);

        assertTrue(orderNumber > 0, "Generated order number should be greater than 0");
    }

    @Test
    void testAddAndRetrieveOrder() {
        LocalDate today = LocalDate.now();
        int orderNumber = orderDao.generateOrderNumber(today);
        Order order = new Order(orderNumber);
        order.setCustomerName("John Doe");
        order.setState("TX");
        order.setProductType("Carpet");
        order.setArea(new BigDecimal("100"));
        order.setOrderDate(today);

        orderDao.addOrder(orderNumber, order);
        Order retrievedOrder = orderDao.getOrderByNumberAndDate(orderNumber, today);

        assertNotNull(retrievedOrder, "Order should be retrieved");
        assertEquals("John Doe", retrievedOrder.getCustomerName(), "Customer name should match");
    }

    @Test
    void testRemoveOrder() {
        LocalDate today = LocalDate.now();
        int orderNumber = orderDao.generateOrderNumber(today);
        Order order = new Order(orderNumber);
        order.setCustomerName("To Remove");
        order.setOrderDate(today);

        orderDao.addOrder(orderNumber, order);
        Order removedOrder = orderDao.removeOrder(orderNumber, today);

        assertNotNull(removedOrder, "Removed order should not be null");
        assertNull(orderDao.getOrderByNumberAndDate(orderNumber, today), "Order should no longer exist");
    }
}
