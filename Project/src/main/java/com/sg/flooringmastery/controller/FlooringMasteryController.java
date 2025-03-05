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
                    io.print("Edit an Order");
                    break;
                case 4:
                    io.print("Remove an Order");
                    break;
                case 5:
                    io.print("Export All Data");
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    io.print("UNKNOWN COMMAND");
            }

        }
        io.print("GOOD BYE");
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
        LocalDate date = view.getOrderDate();

        // Pass the retrieved date to getOrdersByDate
        List<Order> orderList = orderDao.getOrdersByDate(date);

        // Display the list of orders
        view.displayOrders(orderList);
    }


}

