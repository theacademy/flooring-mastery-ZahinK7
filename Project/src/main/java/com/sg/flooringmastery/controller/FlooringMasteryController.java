package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.dao.TaxDao;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.FlooringMasteryServiceImpl;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryController {

    private UserIO io = new UserIOConsoleImpl();
    private FlooringMasteryView view;
    private FlooringMasteryServiceImpl service;
    private OrderDao orderDao;
    private ProductDao productDao;
    private TaxDao taxDao;


    public FlooringMasteryController(FlooringMasteryView view, FlooringMasteryServiceImpl service, OrderDao orderDao, ProductDao productDao, TaxDao taxDao) {
        this.view = view;
        this.service = service;
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }


    public void run() {
        boolean keepGoing = true;
        while (keepGoing) {
            int menuSelection = getMenuSelection();

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
                    exportOrders();
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

    private int getMenuSelection() {
        return view.displayMenu();
    }

    private void createOrder() {
        view.displayAddOrderBanner();

        // Pass ProductDao & TaxDao to enforce validation
        Order newOrder = view.getOrderDetails(productDao, taxDao);

        // Populate costs before calculations
        service.populateOrderCosts(newOrder);

        // Save order
        orderDao.addOrder(newOrder.getOrderNumber(), newOrder);
        view.displayOrderSummary(newOrder);
    }


    private void listOrders() {
        view.displayDisplayAllBanner();
        LocalDate date = view.getOrderDateForViewing(); // Use new method for past dates
        List<Order> orderList = orderDao.getOrdersByDate(date);
        view.displayOrders(orderList);
    }


    private void removeOrder() {
        view.displayRemoveOrderBanner();
        int orderNumber = view.getOrderNumberChoice();
        LocalDate date = view.getValidOrderDate();
        Order removedOrder = orderDao.removeOrder(orderNumber, date);
        view.displayRemoveResult(removedOrder);
    }

    private void editOrder() {
        view.displayEditOrderBanner();
        LocalDate orderDate = view.getOrderDateForViewing();
        int orderNumber = view.getOrderNumberChoice();
        Order existingOrder = orderDao.getOrderByNumberAndDate(orderNumber, orderDate);

        if (existingOrder != null) {
            Order updatedOrder = view.editOrderDetails(existingOrder);

            if (updatedOrder != null) {
                service.populateOrderCosts(updatedOrder);
                orderDao.editOrder(orderNumber, orderDate, updatedOrder);
                view.displayEditSuccessBanner();
            } else {
                io.print("Edit Cancelled");
            }
        } else {
            io.print("Order not found");
        }
    }

    private void exportOrders() {
        LocalDate date = view.getValidOrderDate(); // Ask user for the date to export
        orderDao.exportAllOrders(date);
        io.print("Orders exported successfully for " + date);
    }



    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}
