package com.sg.flooringmastery.dao;

import java.math.BigDecimal;

public interface ProductDao {

    BigDecimal getCostPerSquareFoot();
    BigDecimal getLaborCostPerSquareFoot();
    String getProductType();

}
