package com.sg.flooringmastery.ui;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class UserIOConsoleImpl implements UserIO {

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
        int num;
        while (true) {
            System.out.println(prompt);
            try {
                num = sc.nextInt();
                sc.nextLine();
                return num;
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                sc.nextLine();
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        Scanner sc = new Scanner(System.in);
        int num;
        while (true) {
            System.out.println(prompt);
            try {
                num = sc.nextInt();
                sc.nextLine();
                if (num >= min && num <= max) {
                    return num;
                } else {
                    System.out.println("Invalid choice! Please enter a number between " + min + " and " + max + ".");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                sc.nextLine();
            }
        }
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
        while (num < min && num > max) {
            System.out.println("Try again: ");
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
        while (num < min && num > max) {
            System.out.println("Try again: ");
            num = sc.nextFloat();
        }
        return num;
    }
}
