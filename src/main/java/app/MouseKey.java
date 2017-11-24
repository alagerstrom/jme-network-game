package app;

import com.jme3.input.controls.MouseButtonTrigger;

/**
 * Created by andreas on 2016-11-06.
 */
public class MouseKey {
    private MouseButtonTrigger mouseButtonTrigger;
    private String identifier;
    private KeyPressHandler keyPressHandler;
    public MouseKey(String identifier, int keyInput, KeyPressHandler keyPressHandler){
        this.identifier = identifier;
        this.mouseButtonTrigger = new MouseButtonTrigger(keyInput);
        this.keyPressHandler = keyPressHandler;
    }
    public String getIdentifier(){
        return this.identifier;
    }
    public MouseButtonTrigger getMouseButtonTrigger(){
        return this.mouseButtonTrigger;
    }
    public KeyPressHandler getKeyPressHandler(){
        return this.keyPressHandler;
    }
}
