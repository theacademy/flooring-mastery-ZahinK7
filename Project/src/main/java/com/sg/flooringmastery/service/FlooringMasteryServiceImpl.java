package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.dao.TaxDao;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;
import java.math.RoundingMode;

@Service  // Marks this class as a Spring-managed Service
public class FlooringMasteryServiceImpl implements FlooringMasteryService {

    private final ProductDao productDao;
    private final TaxDao taxDao;
    private final OrderDao orderDao;

    @Autowired  // Spring automatically injects dependencies
    public FlooringMasteryServiceImpl(ProductDao productDao, TaxDao taxDao, OrderDao orderDao) {
        this.productDao = productDao;
        this.taxDao = taxDao;
        this.orderDao = orderDao;
    }

    @Override
    public BigDecimal calculateMaterialCost(Order order) {
        if (order.getArea() == null || order.getCostPerSquareFoot() == null) {
            throw new IllegalArgumentException("Error: Order is missing area or product cost data.");
        }
        return order.getArea()
                .multiply(order.getCostPerSquareFoot())
                .setScale(2, RoundingMode.HALF_UP);  // Round to 2 decimal places
    }

    @Override
    public BigDecimal calculateLaborCost(Order order) {
        return order.getArea()
                .multiply(order.getLaborCostPerSquareFoot())
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTax(Order order) {
        return (order.getMaterialCost().add(order.getLaborCost()))
                .multiply(order.getTaxRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotalCost(Order order) {
        return order.getMaterialCost()
                .add(order.getLaborCost())
                .add(order.getTax())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean validateOrderDate(Order order) {
        return order.getOrderDate().isAfter(LocalDate.now());
    }

    public boolean validateCustomerName(Order order) {
        String name = order.getCustomerName();
        String regex = "^[a-zA-Z0-9., ]+$";
        return name != null && !name.trim().isEmpty() && Pattern.matches(regex, name);
    }

    public boolean validateArea(Order order) {
        return order.getArea().compareTo(BigDecimal.valueOf(100)) >= 0;
    }

    public boolean validateOrder(Order order) {
        return validateOrderDate(order) && validateCustomerName(order) && validateArea(order);
    }

    public void populateOrderCosts(Order order) {
        // Fetch tax data
        Tax tax = taxDao.getTaxByState(order.getState());
        if (tax != null) {
            order.setTaxRate(tax.getTaxRate());
        }

        // Fetch product data
        Product product = productDao.getProduct(order.getProductType());
        if (product != null) {
            order.setCostPerSquareFoot(product.getCostPerSquareFoot());
            order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
        }

        // Recalculate order costs with rounding
        order.setMaterialCost(calculateMaterialCost(order));
        order.setLaborCost(calculateLaborCost(order));
        order.setTax(calculateTax(order));
        order.setTotal(calculateTotalCost(order));
    }
}
