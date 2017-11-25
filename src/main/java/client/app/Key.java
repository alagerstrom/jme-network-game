package client.app;

import com.jme3.input.controls.KeyTrigger;

/**
 * Created by andreas on 2016-10-28.
 */
public class Key {
    private KeyTrigger keyTrigger;
    private String identifier;
    private KeyPressHandler keyPressHandler;
    public Key(String identifier, int keyInput, KeyPressHandler keyPressHandler){
        this.identifier = identifier;
        this.keyTrigger = new KeyTrigger(keyInput);
        this.keyPressHandler = keyPressHandler;
    }
    public String getIdentifier(){
        return this.identifier;
    }
    public KeyTrigger getKeyTrigger(){
        return this.keyTrigger;
    }
    public KeyPressHandler getKeyPressHandler(){
        return this.keyPressHandler;
    }
}
