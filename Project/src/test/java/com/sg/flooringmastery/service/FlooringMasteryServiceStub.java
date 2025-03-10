package com.sg.flooringmastery.service;
import com.sg.flooringmastery.dto.Order;


import java.math.BigDecimal;
import java.time.LocalDate;

public class FlooringMasteryServiceStub extends FlooringMasteryServiceImpl {

    public FlooringMasteryServiceStub() {
        super(null, null, null);
    }

    @Override
    public BigDecimal calculateMaterialCost(Order order) {
        return new BigDecimal("225.00");
    }

    @Override
    public BigDecimal calculateLaborCost(Order order) {
        return new BigDecimal("210.00");
    }

    @Override
    public BigDecimal calculateTax(Order order) {
        return new BigDecimal("19.36");
    }

    @Override
    public BigDecimal calculateTotalCost(Order order) {
        return new BigDecimal("454.36");
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
