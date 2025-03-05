package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        // Read the order date as a string and convert to LocalDate
        String dateInput = io.readString("Enter order date (YYYY-MM-DD): ");
        LocalDate orderDate = LocalDate.parse(dateInput);
        Order currentOrder = new Order(orderNumber);
        currentOrder.setOrderNumber(orderNumber);
        currentOrder.setCustomerName(customerName);
        currentOrder.setState(state);
        currentOrder.setProductType(productType);
        currentOrder.setArea(area);
        currentOrder.setOrderDate(orderDate);
        return currentOrder;
    }

    public LocalDate getOrderDate() {
        String dateString = io.readString("Enter order date (YYYY-MM-DD): ");

        // Convert string input to LocalDate
        return LocalDate.parse(dateString);
    }




    public void  displayOrders(List<Order> orders){
        for(Order orderByDate:orders){
            io.print(String.valueOf(orderByDate.getOrderNumber())+" "+orderByDate.getCustomerName()+" "+orderByDate.getOrderDate()+" "+orderByDate.getProductType()+" "+orderByDate.getTotal());

        }
    }

    public void displayAddOrderBanner() {
        io.print("=== Add Order ===");
    }
    public void displayAddSuccessBanner() {
        io.readString(
                "Order successfully add.  Please hit enter to continue");
    }

    public void displayDisplayAllBanner() {
        io.print("=== Display All Orders by date ===");
    }

}
