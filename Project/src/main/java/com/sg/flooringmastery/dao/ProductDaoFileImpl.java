package com.sg.flooringmastery.dao;

import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import com.sg.flooringmastery.dto.Product;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductDaoFileImpl implements ProductDao {

    private static final String PRODUCT_FILE = "src/main/resources/Data/Products.txt";
    private Map<String, Product> products = new HashMap<>();

    public ProductDaoFileImpl() throws FlooringMasteryDaoException {
        loadProducts();
    }

    @PostConstruct
    private void loadProducts() throws FlooringMasteryDaoException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Data/Products.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                throw new FlooringMasteryDaoException("Product file NOT FOUND! Ensure it's inside `target/classes/Data/`.");
            }

            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                try {
                    String productType = parts[0].trim();
                    BigDecimal costPerSqFt = new BigDecimal(parts[1].trim());
                    BigDecimal laborCostPerSqFt = new BigDecimal(parts[2].trim());

                    products.put(productType, new Product(productType, costPerSqFt, laborCostPerSqFt));
                } catch (NumberFormatException e) {
                    throw new FlooringMasteryDaoException("Invalid numeric data in Products.txt: " + line, e);
                }
            }

            if (products.isEmpty()) {
                throw new FlooringMasteryDaoException("No valid product data was loaded. Check Products.txt format.");
            }

        } catch (IOException e) {
            throw new FlooringMasteryDaoException("Error loading product data: " + e.getMessage(), e);
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
