package com.sg.flooringmastery.ui;

import java.math.BigDecimal;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO{

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        Scanner sc = new Scanner(System.in);

        System.out.println(prompt);
        return sc.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        Scanner sc = new Scanner(System.in);

        System.out.println(prompt);
        return sc.nextInt();
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        Scanner sc = new Scanner(System.in);

        System.out.println(prompt);
        int num = sc.nextInt();

        while(num<min && num>max){
            System.out.println("try again: ");
            num = sc.nextInt();
        }
        return num;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        Scanner sc = new Scanner(System.in);

        System.out.println(prompt);
        return new BigDecimal(sc.nextLine());
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        Scanner sc = new Scanner(System.in);
        BigDecimal num;

        System.out.println(prompt);
        num = new BigDecimal(sc.nextLine());

        while (num.compareTo(min) < 0 || num.compareTo(max) > 0) {
            System.out.println("Try again: ");
            num = new BigDecimal(sc.nextLine());
        }
        return num;
    }

    @Override
    public double readDouble(String prompt) {
        Scanner sc = new Scanner(System.in);

        System.out.println(prompt);
        return sc.nextDouble();
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        Scanner sc = new Scanner(System.in);

        System.out.println(prompt);
        double num = sc.nextDouble();

        while(num<min && num>max){
            System.out.println("try again: ");
            num = sc.nextDouble();
        }
        return num;
    }

    @Override
    public float readFloat(String prompt) {
        Scanner sc = new Scanner(System.in);

        System.out.println(prompt);
        return sc.nextFloat();
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        Scanner sc = new Scanner(System.in);

        System.out.println(prompt);
        float num = sc.nextFloat();

        while(num<min && num>max){
            System.out.println("try again: ");
            num = sc.nextFloat();
        }
        return num;
    }


}