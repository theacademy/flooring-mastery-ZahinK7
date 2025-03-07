package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FlooringMasteryServiceStub extends FlooringMasteryServiceImpl {

    public FlooringMasteryServiceStub() {
        super(null, null, null); // Pass nulls since we are stubbing behavior
    }

    @Override
    public BigDecimal calculateMaterialCost(Order order) {
        return new BigDecimal("225.00"); // Always return a fixed material cost
    }

    @Override
    public BigDecimal calculateLaborCost(Order order) {
        return new BigDecimal("210.00"); // Always return a fixed labor cost
    }

    @Override
    public BigDecimal calculateTax(Order order) {
        return new BigDecimal("19.36"); // Always return a fixed tax amount
    }

    @Override
    public BigDecimal calculateTotalCost(Order order) {
        return new BigDecimal("454.36"); // Always return a fixed total amount
    }

    @Override
    public void populateOrderCosts(Order order) throws FlooringMasteryServiceException {
        if ("INVALID_STATE".equals(order.getState())) {
            throw new FlooringMasteryServiceException("Error: Invalid state");
        }
        if ("INVALID_PRODUCT".equals(order.getProductType())) {
            throw new FlooringMasteryServiceException("Error: Invalid product type");
        }

        order.setTaxRate(new BigDecimal("4.45"));
        order.setCostPerSquareFoot(new BigDecimal("2.25"));
        order.setLaborCostPerSquareFoot(new BigDecimal("2.10"));
        order.setMaterialCost(calculateMaterialCost(order));
        order.setLaborCost(calculateLaborCost(order));
        order.setTax(calculateTax(order));
        order.setTotal(calculateTotalCost(order));
    }
}
