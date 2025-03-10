package com.sg.flooringmastery.ui;
import java.math.BigDecimal;

public interface UserIO {

    void print(String msg);

    double readDouble(String prompt);

    double readDouble(String prompt, double min, double max);

    float readFloat(String prompt);

    float readFloat(String prompt, float min, float max);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    BigDecimal readBigDecimal(String prompt);

    BigDecimal readBigDecimal(String prompt,BigDecimal min, BigDecimal max);

    String readString(String prompt);



}
