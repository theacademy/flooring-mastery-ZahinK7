package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringMasteryController;
import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.OrderDaoFileImpl;
import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.dao.ProductDaoFileImpl;
import com.sg.flooringmastery.dao.TaxDao;
import com.sg.flooringmastery.dao.TaxDaoFileImpl;
import com.sg.flooringmastery.service.FlooringMasteryServiceImpl;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

public class App {
    public static void main(String[] args) {
        // Initialize dependencies
        UserIO io = new UserIOConsoleImpl();
        FlooringMasteryView view = new FlooringMasteryView(io);
        OrderDao orderDao = new OrderDaoFileImpl();
        ProductDao productDao = new ProductDaoFileImpl();
        TaxDao taxDao = new TaxDaoFileImpl();
        FlooringMasteryServiceImpl service = new FlooringMasteryServiceImpl(productDao, taxDao);

        // Pass dependencies to the controller
        FlooringMasteryController controller = new FlooringMasteryController(view, service, orderDao, productDao, taxDao);
        controller.run();
    }
}

