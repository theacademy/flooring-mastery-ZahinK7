package com.sg.flooringmastery.service;
import com.sg.flooringmastery.dto.Order;


import java.math.BigDecimal;

public interface FlooringMasteryService {

    BigDecimal calculateMaterialCost(Order order);
    BigDecimal calculateLaborCost(Order order);
    BigDecimal calculateTax(Order order);
    BigDecimal calculateTotalCost(Order order);

}
