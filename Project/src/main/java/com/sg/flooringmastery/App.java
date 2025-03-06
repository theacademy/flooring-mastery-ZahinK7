package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        // Load Spring application context
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get controller bean from Spring (all dependencies are automatically injected)
        FlooringMasteryController controller = context.getBean(FlooringMasteryController.class);
        controller.run();
    }
}
