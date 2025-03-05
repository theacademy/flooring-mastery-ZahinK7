package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;

import java.math.BigDecimal;
import java.util.List;

public class FlooringMasteryView {

    private UserIO io = new UserIOConsoleImpl();

    public int displayMenu(){
        io.print("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("  * <<Flooring Program>>");
        io.print("  * 1. Display Orders");
        io.print("  * 2. Add an Order");
        io.print("  * 3. Edit an Order");
        io.print("  * 4. Remove an Order");
        io.print("  * 5. Export All Data");
        io.print("  * 6. Quit");
        io.print("  *");
        io.print("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ");

        return io.readInt("Please select from the above choices.", 1, 6);

    };

    //Calculation for Area needs to be done
    public Order getOrderDetails(){
        int orderNumber = io.readInt("Please enter number: ");
        String customerName = io.readString("Add your name: ");
        String state = io.readString("Add state: ");
        String productType = io.readString("What is your product: ");
        BigDecimal area = io.readBigDecimal("How many square feet? (Minimum of 100 sq ft)",BigDecimal.valueOf(100),BigDecimal.valueOf(10000));
        Order currentOrder = new Order(orderNumber);
        currentOrder.setOrderNumber(orderNumber);
        currentOrder.setCustomerName(customerName);
        currentOrder.setState(state);
        currentOrder.setProductType(productType);
        currentOrder.setArea(area);
        return currentOrder;
    }




    public void  displayOrders(List<Order> orders){

    }

    public void displayAddOrderBanner() {
        io.print("=== Add Order ===");
    }
    public void displayAddSuccessBanner() {
        io.readString(
                "Order successfully add.  Please hit enter to continue");
    }


}
