package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository  // Marks this as a Spring DAO component
public class TaxDaoFileImpl implements TaxDao {

    private static final String TAX_FILE = "Data/Taxes.txt";  // Ensure correct path
    private final Map<String, Tax> taxes = new HashMap<>();

    public TaxDaoFileImpl() {

        loadTaxes();
    }

    @PostConstruct
    private void loadTaxes() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TAX_FILE);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.out.println("ERROR: Tax file NOT FOUND! Ensure it's inside `target/classes/Data/`.");
                return;
            }

            br.readLine(); // Skip header row

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue; // Skip invalid lines

                try {
                    String stateAbbr = parts[0].trim().toUpperCase().replaceAll("[^A-Z]", "");
                    String stateName = parts[1].trim();
                    BigDecimal taxRate = new BigDecimal(parts[2].trim());

                    taxes.put(stateAbbr, new Tax(stateAbbr, stateName, taxRate));
                } catch (NumberFormatException e) {
                    // Skipping invalid tax rate values
                }
            }

            if (taxes.isEmpty()) {
                System.out.println("ERROR: No valid tax data was loaded. Check Taxes.txt format.");
            }

        } catch (Exception e) {
            System.out.println("ERROR loading tax data: " + e.getMessage());
        }
    }



    @Override
    public Tax getTaxByState(String stateAbbr) {
        if (stateAbbr == null || stateAbbr.trim().isEmpty()) {
            System.out.println("ERROR: Empty state input!");
            return null;
        }

        stateAbbr = stateAbbr.trim().toUpperCase().replaceAll("[^A-Z]", ""); // Normalize input
        System.out.println("Looking up tax info for state: " + stateAbbr);

        Tax tax = taxes.get(stateAbbr);
        if (tax == null) {
            System.out.println("ERROR: State '" + stateAbbr + "' not found in tax data.");
        }
        return tax;
    }

    @Override
    public BigDecimal getTaxRate(String stateAbbr) {
        Tax tax = getTaxByState(stateAbbr);
        return (tax != null) ? tax.getTaxRate() : null;
    }

    @Override
    public String getStateName(String stateAbbr) {
        Tax tax = getTaxByState(stateAbbr);
        return (tax != null) ? tax.getStateName() : null;
    }

    @Override
    public String getStateAbbr(String stateAbbr) {
        Tax tax = getTaxByState(stateAbbr);
        return (tax != null) ? tax.getStateAbbreviation() : null;
    }
}
