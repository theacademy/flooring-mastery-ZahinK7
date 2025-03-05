package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;

import java.math.BigDecimal;

public class FlooringMasteryServiceImpl implements FlooringMasteryService{

    @Override
    public BigDecimal calculateMaterialCost(Order order) {
        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot());

        return materialCost;
    }

    @Override
    public BigDecimal calculateLaborCost(Order order) {
        BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot());

        return laborCost;
    }

    @Override
    public BigDecimal calculateTax(Order order) {
      BigDecimal tax =   (order.getMaterialCost().add(order.getLaborCost())).multiply((order.getTaxRate().divide(BigDecimal.valueOf(100))));
        return tax;
    }

    @Override
    public BigDecimal calculateTotalCost(Order order) {
        BigDecimal total = order.getMaterialCost().add(order.getLaborCost()).add(order.getTax());

        return total;
    }
}
