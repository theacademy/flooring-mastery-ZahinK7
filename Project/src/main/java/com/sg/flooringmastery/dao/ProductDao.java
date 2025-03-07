package com.sg.flooringmastery.dao;
import com.sg.flooringmastery.dto.Product;
import java.math.BigDecimal;

public interface ProductDao {

    Product getProduct(String productType);

    BigDecimal getCostPerSquareFoot(String productType);

    BigDecimal getLaborCostPerSquareFoot(String productType);

    String getProductType(String productType);
}
