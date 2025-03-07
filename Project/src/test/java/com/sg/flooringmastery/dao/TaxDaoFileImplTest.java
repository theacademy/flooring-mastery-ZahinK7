package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoFileImplTest {

    private TaxDao taxDao;

    @BeforeEach
    void setUp() {
        try {
            taxDao = new TaxDaoFileImpl();
        } catch (FlooringMasteryDaoException e) {
            fail("Failed to initialize TaxDaoFileImpl: " + e.getMessage());
        }
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

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "XYZ", "123", "OHIO", "", "  ", "T@X", "1TX", "OH-IO"})
    void testInvalidStateReturnsNull(String invalidState) {
        assertNull(taxDao.getTaxByState(invalidState), "Invalid state should return null");
        assertNull(taxDao.getTaxRate(invalidState), "Invalid state should return null for tax rate");
        assertNull(taxDao.getStateName(invalidState), "Invalid state should return null for state name");
        assertNull(taxDao.getStateAbbr(invalidState), "Invalid state should return null for state abbreviation");
    }

    @ParameterizedTest
    @ValueSource(strings = {"tx", "Tx", " TX "})
    void testStateAbbreviationNormalization(String inputState) {
        Tax tax = taxDao.getTaxByState(inputState);
        assertNotNull(tax, "State abbreviation should be normalized correctly.");
        assertEquals("TX", tax.getStateAbbreviation(), "State abbreviation should match TX after normalization.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"t*x", "1TX", "OH-IO", "T@X", "123"})
    void testInvalidStateReturnsNullEvenWithFormatting(String invalidInput) {
        assertNull(taxDao.getTaxByState(invalidInput), "Improperly formatted state abbreviations should return null.");
    }


    @Test
    void testLoadTaxes_fileNotFound() {
        try {
            new TaxDaoFileImpl(); // Ensure no exception is thrown
        } catch (FlooringMasteryDaoException e) {
            assertTrue(e.getMessage().contains("Tax file NOT FOUND"), "Expected a file not found error when Taxes.txt is missing.");
        }
    }

    @Test
    void testLoadTaxes_invalidDataFormat() {
        try {
            new TaxDaoFileImpl(); // If invalid data is present, this should throw an exception
        } catch (FlooringMasteryDaoException e) {
            assertTrue(e.getMessage().contains("Invalid tax rate value in Taxes.txt") ||
                            e.getMessage().contains("No valid tax data was loaded"),
                    "Expected an error due to invalid numeric data in Taxes.txt.");
        }
    }
}
