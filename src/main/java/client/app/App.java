package client.app;

import client.model.CrossHairs;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.FlyByCamera;
import com.jme3.input.KeyInput;
import com.simsilica.lemur.*;
import com.simsilica.lemur.style.BaseStyles;

public class App extends SimpleApplication {

    public App() {
        super(new FlyCamAppState(), new MenuState(), new GameState());
    }

    @Override
    public void simpleInitApp() {

//        flyCam = new FlyByCamera(cam);
        stateManager.attach(new BulletAppState());


        // Initialize the globals access so that the defualt
        // components can find what they need.
        GuiGlobals.initialize(this);



        // Load the 'glass' style
        BaseStyles.loadGlassStyle();

        new CrossHairs().setUpCrossHairs(assetManager, guiFont, settings, guiNode);
        // Set 'glass' as the default style when not specified
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        stateManager.getState(MenuState.class).setEnabled(true);
        stateManager.getState(GameState.class).setEnabled(false);

    }

}
