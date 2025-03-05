package com.sg.flooringmastery.dao;

public interface TaxDao {

    String getTaxRate(String state);
    String getStateName();
    String getStateAbbr();
}
