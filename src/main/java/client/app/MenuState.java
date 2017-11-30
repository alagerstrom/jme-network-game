package client.app;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;

import java.nio.channels.CompletionHandler;

public class MenuState extends BaseAppState{


    private SimpleApplication application;
    private Container container;


    @Override
    protected void initialize(Application app) {
        System.out.println("Menu initialized");
        this.application = (SimpleApplication) app;

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        System.out.println("Enabling menu");
//        Light light = new Light();
//        light.addLight(application.getRootNode());

        // Create a simple container for our elements
        container = new Container();
        application.getGuiNode().attachChild(container);

        // Put it somewhere that we will see it
        // Note: Lemur GUI elements grow down from the upper left corner.
        container.setLocalTranslation(500, 500, 0);

        // Add some elements
        container.addChild(new Label("Welcome"));
        Label statusLabel = new Label("");
        container.addChild(statusLabel);
        container.addChild(new Label("Connect to ip address:"));
        TextField textField = new TextField("");
        container.addChild(textField);
        container.addChild(new Label("Connect to port:"));
        TextField portField = new TextField("");
        container.addChild(portField);
        Button connectButton = new Button("Connect");
        connectButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button button) {
                container.removeChild(connectButton);
                statusLabel.setText("Connecting...");

                NetDelegate netDelegate = getStateManager().getState(GameState.class).getNetDelegate();
                netDelegate.connectTo(textField.getText(), Integer.parseInt(portField.getText()), new CompletionHandler<Void, Void>() {
                    @Override
                    public void completed(Void result, Void attachment) {
                        getApplication().enqueue(()->{
                            statusLabel.setText("Connected.");
                        });
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        getApplication().enqueue(()->{
                            statusLabel.setText("Failed to connect.");
                        });
                    }
                });

            }
        });
        container.addChild(connectButton);
        Button startGameButton = container.addChild(new Button("Continue"));
        startGameButton.addClickCommands((Command<Button>) source -> {
            getStateManager().getState(GameState.class).setEnabled(true);
            getStateManager().getState(MenuState.class).setEnabled(false);
        });

        Button quitButton = new Button("Quit");
        quitButton.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button button) {
                getApplication().stop();
            }
        });
        container.addChild(quitButton);


    }

    @Override
    protected void onDisable() {
        application.getGuiNode().detachChild(container);
        container = null;

    }
}
