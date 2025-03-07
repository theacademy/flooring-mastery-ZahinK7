package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoFileImplTest {

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        try {
            productDao = new ProductDaoFileImpl();
        } catch (FlooringMasteryDaoException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    }


    @Test
    void testGetProduct_validProduct() {
        // Act
        Product product = productDao.getProduct("Carpet");

        // Assert
        assertNotNull(product, "Product should not be null for a valid product type (Carpet)");
        assertEquals("Carpet", product.getProductType(), "Product type should match");
        assertEquals(new BigDecimal("2.25"), product.getCostPerSquareFoot(), "Cost per square foot should match expected value");
        assertEquals(new BigDecimal("2.10"), product.getLaborCostPerSquareFoot(), "Labor cost per square foot should match expected value");
    }

    @Test
    void testGetCostPerSquareFoot_validProduct() {
        // Act
        BigDecimal cost = productDao.getCostPerSquareFoot("Carpet");

        // Assert
        assertNotNull(cost, "Cost should not be null for a valid product type");
        assertEquals(new BigDecimal("2.25"), cost, "Cost per square foot should match expected value");
    }

    @Test
    void testGetLaborCostPerSquareFoot_validProduct() {
        // Act
        BigDecimal laborCost = productDao.getLaborCostPerSquareFoot("Carpet");

        // Assert
        assertNotNull(laborCost, "Labor cost should not be null for a valid product type");
        assertEquals(new BigDecimal("2.10"), laborCost, "Labor cost per square foot should match expected value");
    }

    @Test
    void testGetProductType_validProduct() {
        // Act
        String productType = productDao.getProductType("Carpet");

        // Assert
        assertNotNull(productType, "Product type should not be null for a valid product");
        assertEquals("Carpet", productType, "Product type should match expected value");
    }

    @Test
    void testInvalidProductReturnsNull() {
        // Act & Assert
        assertNull(productDao.getProduct("InvalidProduct"), "Invalid product should return null");
        assertNull(productDao.getCostPerSquareFoot("InvalidProduct"), "Invalid product should return null for cost per square foot");
        assertNull(productDao.getLaborCostPerSquareFoot("InvalidProduct"), "Invalid product should return null for labor cost per square foot");
        assertNull(productDao.getProductType("InvalidProduct"), "Invalid product should return null for product type");
    }
}
