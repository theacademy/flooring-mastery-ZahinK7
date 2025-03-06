package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TaxDaoFileImpl implements TaxDao {

    private static final String TAX_FILE = "src/main/resources/Data/Taxes.txt";
    private Map<String, Tax> taxes = new HashMap<>();

    public TaxDaoFileImpl() {
        loadTaxes();
    }

    private void loadTaxes() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Data/Taxes.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String stateAbbr = parts[0];
                    String stateName = parts[1];
                    BigDecimal taxRate = new BigDecimal(parts[2]);
                    taxes.put(stateAbbr, new Tax(stateAbbr, stateName, taxRate));

                    // Debugging output
                    System.out.println("Loaded Tax: " + stateAbbr +
                            ", State: " + stateName +
                            ", Tax Rate: " + taxRate);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading tax data: " + e.getMessage());
        }
    }


    @Override
    public Tax getTaxByState(String stateAbbr) {
        return taxes.get(stateAbbr);
    }

    @Override
    public BigDecimal getTaxRate(String stateAbbr) {
        Tax tax = taxes.get(stateAbbr);
        return (tax != null) ? tax.getTaxRate() : null;
    }

    @Override
    public String getStateName(String stateAbbr) {
        Tax tax = taxes.get(stateAbbr);
        return (tax != null) ? tax.getStateName() : null;
    }

    @Override
    public String getStateAbbr(String stateAbbr) {
        Tax tax = taxes.get(stateAbbr);
        return (tax != null) ? tax.getStateAbbreviation() : null;
    }
}