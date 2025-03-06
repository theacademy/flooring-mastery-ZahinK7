package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.dao.TaxDao;
import com.sg.flooringmastery.dto.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FlooringMasteryView {

    private UserIO io;

    // Updated constructor to take UserIO as a parameter
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }

    public int displayMenu() {
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

        return io.readInt("Please select from the above choices (1-6): ", 1, 6);
    }


    public Order getOrderDetails(ProductDao productDao, TaxDao taxDao) {
        int orderNumber = io.readInt("Please enter order number: ");
        String customerName = getValidCustomerName();

        // Use the new validation methods
        String state = getValidState(taxDao);
        String productType = getValidProductType(productDao);

        BigDecimal area = getValidArea();
        LocalDate orderDate = getValidOrderDate();

        Order currentOrder = new Order(orderNumber);
        currentOrder.setCustomerName(customerName);
        currentOrder.setState(state);
        currentOrder.setProductType(productType);
        currentOrder.setArea(area);
        currentOrder.setOrderDate(orderDate);

        return currentOrder;
    }


    private String getValidCustomerName() {
        boolean isValid = false;
        String customerName = "";

        while (!isValid) {
            customerName = io.readString("Add your name: ");

            // Check if name is empty
            if (customerName == null || customerName.trim().isEmpty()) {
                io.print("Error: Customer name cannot be empty. Please try again.");
                continue;
            }

            // Remove extra spaces and check for invalid characters
            customerName = customerName.trim().replaceAll("\\s+", " ");

            if (!customerName.matches("^[a-zA-Z0-9.,\\s]+$")) {
                io.print("Error: Customer name can only contain letters, numbers, spaces, commas, and periods. Please try again.");
                continue;
            }

            isValid = true;
        }

        return customerName;
    }

    private BigDecimal getValidArea() {
        boolean isValid = false;
        BigDecimal area = BigDecimal.ZERO;

        while (!isValid) {
            try {
                area = io.readBigDecimal("How many square feet? (Minimum of 100 sq ft)");

                // Check minimum area requirement
                if (area.compareTo(BigDecimal.valueOf(100)) < 0) {
                    io.print("Error: Area must be at least 100 square feet. Please try again.");
                    continue;
                }

                // Check maximum area limit
                if (area.compareTo(BigDecimal.valueOf(10000000)) > 0) {
                    io.print("Error: Area cannot exceed 10,000 square feet. Please try again.");
                    continue;
                }

                isValid = true;
            } catch (Exception e) {
                io.print("Error: Invalid number format. Please enter a valid number for the area.");
            }
        }

        return area;
    }

    public LocalDate getValidOrderDate() {
        boolean isValid = false;
        LocalDate orderDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();

        while (!isValid) {
            try {
                String dateInput = io.readString("Enter order date (YYYY-MM-DD): ");
                orderDate = LocalDate.parse(dateInput, formatter);

                if (orderDate.isBefore(currentDate)) {
                    io.print("Error: Order date must be today or in the future. Please try again.");
                } else {
                    isValid = true;
                }
            } catch (DateTimeParseException e) {
                io.print("Error: Invalid date format. Please use YYYY-MM-DD format (e.g., 2025-03-15).");
            }
        }
        return orderDate;
    }
    public LocalDate getOrderDateForViewing() {
        boolean isValid = false;
        LocalDate orderDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!isValid) {
            try {
                String dateInput = io.readString("Enter order date (YYYY-MM-DD): ");
                orderDate = LocalDate.parse(dateInput, formatter);
                isValid = true; // Allow any date for viewing
            } catch (DateTimeParseException e) {
                io.print("Error: Invalid date format. Please use YYYY-MM-DD format (e.g., 2025-03-15).");
            }
        }
        return orderDate;
    }


//    public LocalDate getOrderDate() {
//        String dateString = io.readString("Enter order date (YYYY-MM-DD): ");
//
//        // Convert string input to LocalDate
//        return LocalDate.parse(dateString);
//    }

    public int getOrderNumberChoice(){
        return io.readInt("Please enter your order number: ");
    }

    public Order editOrderDetails(Order order) {
        io.print("Editing Order #" + order.getOrderNumber() + " from " + order.getOrderDate());
        io.print("Press Enter to keep existing value.");

        // Edit customer name
        String customerNamePrompt = String.format("Enter customer name (%s): ", order.getCustomerName());
        String newCustomerName = io.readString(customerNamePrompt);
        if (!newCustomerName.trim().isEmpty()) {
            // Validate new customer name
            if (newCustomerName.matches("^[a-zA-Z0-9.,\\s]+$")) {
                order.setCustomerName(newCustomerName);
            } else {
                io.print("Invalid customer name format. Original name retained.");
            }
        }

        // Edit state
        String statePrompt = String.format("Enter state (%s): ", order.getState());
        String newState = io.readString(statePrompt);
        if (!newState.trim().isEmpty()) {
            order.setState(newState.toUpperCase());
        }

        // Edit product type
        String productPrompt = String.format("Enter product type (%s): ", order.getProductType());
        String newProductType = io.readString(productPrompt);
        if (!newProductType.trim().isEmpty()) {
            order.setProductType(newProductType);
        }

        // Edit area
        String areaPrompt = String.format("Enter area in square feet (%s): ", order.getArea());
        String newAreaInput = io.readString(areaPrompt);
        if (!newAreaInput.trim().isEmpty()) {
            try {
                BigDecimal newArea = new BigDecimal(newAreaInput);
                if (newArea.compareTo(BigDecimal.valueOf(100)) >= 0) {
                    order.setArea(newArea);
                } else {
                    io.print("Area must be at least 100 sq ft. Original area retained.");
                }
            } catch (NumberFormatException e) {
                io.print("Invalid number format. Original area retained.");
            }
        }

        // Confirm the changes
        io.print("\n=== Updated Order Details ===");
        io.print("Order Number: " + order.getOrderNumber());
        io.print("Customer Name: " + order.getCustomerName());
        io.print("State: " + order.getState());
        io.print("Product Type: " + order.getProductType());
        io.print("Area: " + order.getArea() + " sq ft");

        String confirm = io.readString("\nSave these changes? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            return order;
        } else {
            return null; // User canceled the edit
        }
    }

    public String getValidState(TaxDao taxDao) {
        String state = "";
        boolean valid = false;

        while (!valid) {
            state = io.readString("Enter state abbreviation (e.g., TX, CA): ").toUpperCase();

            if (taxDao.getTaxByState(state) != null) {
                valid = true;
            } else {
                io.print("Invalid state. Please enter a valid state abbreviation from our service area.");
            }
        }
        return state;
    }

    public String getValidProductType(ProductDao productDao) {
        String productType = "";
        boolean valid = false;

        while (!valid) {
            productType = io.readString("Enter product type (e.g., Carpet, Tile): ");

            if (productDao.getProduct(productType) != null) {
                valid = true;
            } else {
                io.print("Invalid product. Please enter a valid product from our inventory.");
            }
        }
        return productType;
    }


    public void displayOrders(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            io.print("No orders found for the given date.");
            return;
        }

        for (Order orderByDate : orders) {
            io.print(orderByDate.getOrderNumber() + " " +
                    orderByDate.getCustomerName() + " " +
                    orderByDate.getOrderDate() + " " +
                    orderByDate.getProductType() + " " +
                    orderByDate.getTotal());
        }
    }

    public void displayAddOrderBanner() {
        io.print("=== Add Order ===");
    }
    public void displayAddSuccessBanner() {
        io.readString(
                "Order successfully add");
    }

    public void displayDisplayAllBanner() {
        io.print("=== Display All Orders by date ===");
    }

    public void displayRemoveOrderBanner () {
        io.print("=== Remove Student ===");
    }

    public void displayRemoveResult(Order orderRecord) {
        if(orderRecord != null){
            io.print("Order successfully removed.");
        }else{
            io.print("No such order.");
        }
    }

    public void displayEditOrderBanner() {
        io.print("=== Edit Order ===");
    }
    public void displayEditSuccessBanner() {
        io.readString(
                "Order successfully edited. Press enter to back to menu");
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayOrderSummary(Order order) {
        io.print("=== Order Summary ===");
        io.print("Order Number: " + order.getOrderNumber());
        io.print("Customer Name: " + order.getCustomerName());
        io.print("State: " + order.getState());
        io.print("Tax Rate: " + order.getTaxRate() + "%");
        io.print("Product Type: " + order.getProductType());
        io.print("Area: " + order.getArea() + " sq ft");
        io.print("Cost per Square Foot: $" + String.format("%.2f", order.getCostPerSquareFoot()));
        io.print("Labor Cost per Square Foot: $" + String.format("%.2f", order.getLaborCostPerSquareFoot()));
        io.print("Material Cost: $" + String.format("%.2f", order.getMaterialCost()));
        io.print("Labor Cost: $" + String.format("%.2f", order.getLaborCost()));
        io.print("Tax: $" + String.format("%.2f", order.getTax()));
        io.print("Total: $" + String.format("%.2f", order.getTotal()));
        io.print("=====================");
    }



}
