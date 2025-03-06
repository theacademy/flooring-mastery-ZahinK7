package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoFileImplTest {

    private TaxDao taxDao;

    @BeforeEach
    void setUp() {
        taxDao = new TaxDaoFileImpl(); // Initialize DAO before each test
    }

    @Test
    void testGetTaxByState_validState() {
        // Act
        Tax tax = taxDao.getTaxByState("TX");

        // Assert
        assertNotNull(tax, "Tax should not be null for a valid state (TX)");
        assertEquals("TX", tax.getStateAbbreviation(), "State abbreviation should match");
        assertEquals("Texas", tax.getStateName(), "State name should match");
        assertEquals(new BigDecimal("4.45"), tax.getTaxRate(), "Tax rate should match expected value");
    }

    @Test
    void testGetTaxRate_validState() {
        // Act
        BigDecimal taxRate = taxDao.getTaxRate("TX");

        // Assert
        assertNotNull(taxRate, "Tax rate should not be null for a valid state");
        assertEquals(new BigDecimal("4.45"), taxRate, "Tax rate should match expected value");
    }

    @Test
    void testGetStateName_validState() {
        // Act
        String stateName = taxDao.getStateName("TX");

        // Assert
        assertNotNull(stateName, "State name should not be null for a valid state");
        assertEquals("Texas", stateName, "State name should match expected value");
    }

    @Test
    void testGetStateAbbr_validState() {
        // Act
        String stateAbbr = taxDao.getStateAbbr("TX");

        // Assert
        assertNotNull(stateAbbr, "State abbreviation should not be null for a valid state");
        assertEquals("TX", stateAbbr, "State abbreviation should match expected value");
    }

    @Test
    void testInvalidStateReturnsNull() {
        // Act & Assert
        assertNull(taxDao.getTaxByState("INVALID"), "Invalid state should return null");
        assertNull(taxDao.getTaxRate("INVALID"), "Invalid state should return null for tax rate");
        assertNull(taxDao.getStateName("INVALID"), "Invalid state should return null for state name");
        assertNull(taxDao.getStateAbbr("INVALID"), "Invalid state should return null for state abbreviation");
    }
}
