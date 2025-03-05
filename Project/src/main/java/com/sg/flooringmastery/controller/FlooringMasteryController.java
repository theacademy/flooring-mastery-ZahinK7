package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.OrderDaoFileImpl;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.service.FlooringMasteryServiceImpl;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryController {

    private UserIO io = new UserIOConsoleImpl();
    private FlooringMasteryView view = new FlooringMasteryView();
    private FlooringMasteryServiceImpl service = new FlooringMasteryServiceImpl();
    private OrderDao orderDao = new OrderDaoFileImpl();


    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        while (keepGoing) {


            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
                    listOrders();
                    break;
                case 2:
                    createOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    io.print("Export All Data");
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }

        }
       exitMessage();
    }

    private int getMenuSelection(){
        return view.displayMenu();
    }

    //Make sure to get tax based on state
    //Make you are getting product information from txt (either should the implemenation be done here or in Viewer or somewhere else)
    private void createOrder(){
        view.displayAddOrderBanner();
        boolean hasErrors =false;
        Order newOrder = view.getOrderDetails();
        newOrder.setCostPerSquareFoot(BigDecimal.valueOf(1));
        newOrder.setLaborCostPerSquareFoot(BigDecimal.valueOf(1));
        newOrder.setTaxRate(BigDecimal.valueOf(1.00));
        newOrder.setMaterialCost(service.calculateMaterialCost(newOrder));
        newOrder.setLaborCost(service.calculateLaborCost(newOrder));
        newOrder.setTax(service.calculateTax(newOrder));
        newOrder.setTotal(service.calculateTotalCost(newOrder));
        System.out.println(newOrder.getMaterialCost()+" "+newOrder.getLaborCost()+" "+newOrder.getTax()+" "+newOrder.getTotal()+" "+newOrder.getOrderDate());
        orderDao.addOrder(newOrder.getOrderNumber(), newOrder);
    }

    private void listOrders() {
        view.displayDisplayAllBanner();

        // Prompt user for the order date
        LocalDate date = view.getValidOrderDate();

        // Pass the retrieved date to getOrdersByDate
        List<Order> orderList = orderDao.getOrdersByDate(date);

        // Display the list of orders
        view.displayOrders(orderList);
    }

    private void removeOrder(){
        view.displayRemoveOrderBanner();
        int orderNumber = view.getOrderNumberChoice();
        LocalDate date = view.getValidOrderDate();
        Order removedOrder = orderDao.removeOrder(orderNumber,date);

        view.displayRemoveResult(removedOrder);
    }

    private void editOrder() {
        view.displayEditOrderBanner();

        // Get order date and number from user
        LocalDate orderDate = view.getValidOrderDate();
        int orderNumber = view.getOrderNumberChoice();

        // Retrieve the order
        Order existingOrder = orderDao.getOrderByNumberAndDate(orderNumber, orderDate);

        // If order exists, let user edit it
        if (existingOrder != null) {
            Order updatedOrder = view.editOrderDetails(existingOrder);

            // Recalculate costs based on updated details
            if (updatedOrder != null) {
                // Update costs based on potentially changed product, state, or area
                updatedOrder.setMaterialCost(service.calculateMaterialCost(updatedOrder));
                updatedOrder.setLaborCost(service.calculateLaborCost(updatedOrder));
                updatedOrder.setTax(service.calculateTax(updatedOrder));
                updatedOrder.setTotal(service.calculateTotalCost(updatedOrder));

                // Save the updated order
                orderDao.editOrder(orderNumber, orderDate, updatedOrder);
                view.displayEditSuccessBanner();
            } else {
                System.out.println("Edit Cancelled");;
            }
        } else {
            System.out.println("Order not found");;
        }
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }


}

