package controller;

import app.Application;

/**
 * Created by andreas on 2016-10-28.
 */
public class Controller {
    private Application application;
    public void createApplication(){
        application = new Application();
    }
    public void startApplication(){
        application.start();
    }
}
