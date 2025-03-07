package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


        FlooringMasteryController controller = context.getBean(FlooringMasteryController.class);
        controller.run();
    }
}
