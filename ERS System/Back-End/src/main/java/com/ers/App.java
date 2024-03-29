package com.ers;

import com.ers.controllers.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static Javalin app;
    private static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        //app = Javalin.create(); // for running tests through postman

        app = Javalin.create((config)->{ // ec2 run
            config.addStaticFiles("FrontEnd", Location.EXTERNAL);
        });

//        app = Javalin.create((config)->{ // eric run
//            config.addStaticFiles("C:\\Users\\flodev\\Desktop\\ProjectOne\\project-1-mario-_-eric\\FrontEnd",
//                    Location.EXTERNAL);
//        });

        configure(new ReimbursementController(),
                new LoginController());

        app.start(7000);
        log.info("started the app!");
    }

    public static void configure(Controller... controllers){

        for (Controller c: controllers){
            c.addRoutes(app);
        }
    }
}

