package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Tax;
import java.math.BigDecimal;

public interface TaxDao {

    Tax getTaxByState(String stateAbbr);

    BigDecimal getTaxRate(String stateAbbr);

    String getStateName(String stateAbbr);

    String getStateAbbr(String stateAbbr);
}
