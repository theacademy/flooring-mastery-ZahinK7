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

@Service
public class FlooringMasteryServiceImpl implements FlooringMasteryService {

    private final ProductDao productDao;
    private final TaxDao taxDao;
    private final OrderDao orderDao;

    @Autowired
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
                .setScale(2, RoundingMode.HALF_UP);
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
        return !order.getOrderDate().isBefore(LocalDate.now());
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

    public void populateOrderCosts(Order order) throws FlooringMasteryServiceException {

        Tax tax = taxDao.getTaxByState(order.getState());
        if (tax == null) {
            throw new FlooringMasteryServiceException("Error: State '" + order.getState() + "' not found in tax data.");
        }
        order.setTaxRate(tax.getTaxRate());


        Product product = productDao.getProduct(order.getProductType());
        if (product == null) {
            throw new FlooringMasteryServiceException("Error: Product type '" + order.getProductType() + "' not found.");
        }
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());


        order.setMaterialCost(calculateMaterialCost(order));
        order.setLaborCost(calculateLaborCost(order));
        order.setTax(calculateTax(order));
        order.setTotal(calculateTotalCost(order));
    }

}
