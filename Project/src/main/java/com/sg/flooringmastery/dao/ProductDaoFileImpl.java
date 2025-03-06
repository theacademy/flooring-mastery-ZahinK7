package com.sg.flooringmastery.dao;
import org.springframework.stereotype.Repository; // Fix for @Repository
import jakarta.annotation.PostConstruct;
import com.sg.flooringmastery.dto.Product;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository  // Marks this class as a Spring-managed DAO component
public class ProductDaoFileImpl implements ProductDao {

    private static final String PRODUCT_FILE = "src/main/resources/Data/Products.txt";
    private Map<String, Product> products = new HashMap<>();

    public ProductDaoFileImpl() {
        // Removed manual call to loadProducts(), Spring will handle it via @PostConstruct
        loadProducts();
    }

    @PostConstruct
    private void loadProducts() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Data/Products.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.out.println("ERROR: Product file NOT FOUND! Ensure it's inside `target/classes/Data/`.");
                return;
            }

            br.readLine(); // Skip header row

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue; // Skip invalid lines

                try {
                    String productType = parts[0].trim();
                    BigDecimal costPerSqFt = new BigDecimal(parts[1].trim());
                    BigDecimal laborCostPerSqFt = new BigDecimal(parts[2].trim());

                    products.put(productType, new Product(productType, costPerSqFt, laborCostPerSqFt));
                } catch (NumberFormatException e) {
                    // Skipping invalid cost values
                }
            }

            if (products.isEmpty()) {
                System.out.println("ERROR: No valid product data was loaded. Check Products.txt format.");
            }

        } catch (Exception e) {
            System.out.println("ERROR loading product data: " + e.getMessage());
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