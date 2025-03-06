package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Product;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductDaoFileImpl implements ProductDao {

    private static final String PRODUCT_FILE = "src/main/resources/Data/Products.txt";
    private Map<String, Product> products = new HashMap<>();

    public ProductDaoFileImpl() {
        loadProducts();
    }

    private void loadProducts() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Data/Products.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String productType = parts[0];
                    BigDecimal costPerSqFt = new BigDecimal(parts[1]);
                    BigDecimal laborCostPerSqFt = new BigDecimal(parts[2]);
                    products.put(productType, new Product(productType, costPerSqFt, laborCostPerSqFt));

                    // Debugging output
                    System.out.println("Loaded Product: " + productType +
                            ", Cost: " + costPerSqFt +
                            ", Labor Cost: " + laborCostPerSqFt);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading product data: " + e.getMessage());
        }
    }


    @Override
    public Product getProduct(String productType) {
        return products.get(productType);
    }

    @Override
    public BigDecimal getCostPerSquareFoot(String productType) {
        Product product = products.get(productType);
        return (product != null) ? product.getCostPerSquareFoot() : null;
    }

    @Override
    public BigDecimal getLaborCostPerSquareFoot(String productType) {
        Product product = products.get(productType);
        return (product != null) ? product.getLaborCostPerSquareFoot() : null;
    }

    @Override
    public String getProductType(String productType) {
        Product product = products.get(productType);
        return (product != null) ? product.getProductType() : null;
    }
}
