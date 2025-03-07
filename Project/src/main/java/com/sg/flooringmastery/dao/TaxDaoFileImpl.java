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

@Repository
public class TaxDaoFileImpl implements TaxDao {

    private static final String TAX_FILE = "Data/Taxes.txt";
    private final Map<String, Tax> taxes = new HashMap<>();

    public TaxDaoFileImpl() throws FlooringMasteryDaoException {
        loadTaxes();
    }

    @PostConstruct
    private void loadTaxes() throws FlooringMasteryDaoException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TAX_FILE);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                throw new FlooringMasteryDaoException("Tax file NOT FOUND! Ensure it's inside `target/classes/Data/`.");
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
                    throw new FlooringMasteryDaoException("Invalid tax rate value in Taxes.txt: " + line, e);
                }
            }

            if (taxes.isEmpty()) {
                throw new FlooringMasteryDaoException("No valid tax data was loaded. Check Taxes.txt format.");
            }

        } catch (Exception e) {
            throw new FlooringMasteryDaoException("Error loading tax data: " + e.getMessage(), e);
        }
    }

    @Override
    public Tax getTaxByState(String stateAbbr) {
        if (stateAbbr == null || stateAbbr.trim().isEmpty()) {
            return null;
        }

        stateAbbr = stateAbbr.trim(); // Trim spaces first

        // Reject inputs that contain anything other than letters (prevents "1TX", "T@X", "OH-IO")
        if (!stateAbbr.matches("^[A-Za-z]+$")) {
            return null;
        }

        stateAbbr = stateAbbr.toUpperCase();
        return taxes.get(stateAbbr);
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
