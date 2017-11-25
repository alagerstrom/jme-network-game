package client.app;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

import java.util.ArrayList;

/**
 * Created by andreas on 2016-10-28.
 */
public class Keyboard implements ActionListener, AnalogListener {
    private ArrayList<Key> keys = new ArrayList<Key>();
    private ArrayList<MouseKey> mouseKeys = new ArrayList<MouseKey>();

    public void onAction(String binding, boolean isPressed, float v) {
        for (Key key : keys)
            if (binding.equals(key.getIdentifier()))
                key.getKeyPressHandler().onPress(isPressed);
        for (MouseKey mouseKey : mouseKeys)
            if (binding.equals(mouseKey.getIdentifier()))
                mouseKey.getKeyPressHandler().onPress(isPressed);
    }

    public void onAnalog(String s, float v, float v1) {

    }
    public void addMouseKey(MouseKey mouseKey){
        this.mouseKeys.add(mouseKey);

    }
    public void addKey(Key key){
        this.keys.add(key);
    }
    public void addKeysToInputManager(InputManager inputManager){
        for (Key key : keys) {
            System.out.println("Adding key " + key.getIdentifier());
            inputManager.addMapping(key.getIdentifier(), key.getKeyTrigger());
            inputManager.addListener(this, key.getIdentifier());
        }
        for (MouseKey mouseKey : mouseKeys){
            System.out.println("Adding mouse key " + mouseKey.getIdentifier());
            inputManager.addMapping(mouseKey.getIdentifier(), mouseKey.getMouseButtonTrigger());
            inputManager.addListener(this, mouseKey.getIdentifier());
        }
    }
}
