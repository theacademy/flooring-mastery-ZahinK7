package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;


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

    public boolean validateOrderDate(Order order) {
        return order.getOrderDate().isAfter(LocalDate.now());
    }


    public boolean validateCustomerName(Order order) {
        String name = order.getCustomerName();
        // Regex to match alphanumeric characters, periods, commas, and ensure it's not blank
        String regex = "^[a-zA-Z0-9., ]+$";
        return name != null && !name.trim().isEmpty() && Pattern.matches(regex, name);
    }

    public boolean validateArea(Order order) {
        BigDecimal area = order.getArea();
        // Ensure the area is positive and at least 100 square feet
        return area.compareTo(BigDecimal.ZERO) > 0 && area.compareTo(BigDecimal.valueOf(100)) >= 0;
    }

//    public boolean validateState(Order order) {
//        String state = order.getState();
//        // Check if the state exists in the validStates set
//        return validStates.contains(state);
//    }
//
//    public boolean validateProductType(Order order) {
//        Product product = order.getProduct();
//        // Check if the product is in the list of available products
//        return availableProducts.contains(product);
//    }

    // Combined validation check
    public boolean validateOrder(Order order) {
        return validateOrderDate(order) &&
                validateCustomerName(order) &&
                validateArea(order);
    }



}
