package com.sg.flooringmastery.dao;
import com.sg.flooringmastery.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductDaoFileImplTest {

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        try {
            productDao = new ProductDaoFileImpl();
        } catch (FlooringMasteryDaoException e) {
            fail("Failed to initialize ProductDaoFileImpl: " + e.getMessage());
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

    @ParameterizedTest
    @ValueSource(strings = {"InvalidProduct", "WoodenTile", "NonExistent"})
    void testInvalidProductReturnsNull(String invalidProduct) {
        assertNull(productDao.getProduct(invalidProduct), "Invalid product should return null");
        assertNull(productDao.getCostPerSquareFoot(invalidProduct), "Invalid product should return null for cost per square foot");
        assertNull(productDao.getLaborCostPerSquareFoot(invalidProduct), "Invalid product should return null for labor cost per square foot");
        assertNull(productDao.getProductType(invalidProduct), "Invalid product should return null for product type");
    }

    @Test
    void testLoadProducts_fileNotFound() {
        try {
            new ProductDaoFileImpl();
        } catch (FlooringMasteryDaoException e) {
            assertTrue(e.getMessage().contains("Product file NOT FOUND"), "Expected a file not found error when Products.txt is missing.");
        }
    }

    @Test
    void testLoadProducts_invalidDataFormat() {
        try {
            new ProductDaoFileImpl();
        } catch (FlooringMasteryDaoException e) {
            assertTrue(e.getMessage().contains("Invalid numeric data in Products.txt") ||
                            e.getMessage().contains("No valid product data was loaded"),
                    "Expected an error due to invalid numeric data in Products.txt.");
        }
    }
}
