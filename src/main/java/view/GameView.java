package view;

import controller.Controller;

/**
 * Created by andreas on 2016-10-28.
 */
public class GameView {
    private Controller controller = new Controller();;

    public void start(){
        controller.createApplication();
        controller.startApplication();
    }
}
