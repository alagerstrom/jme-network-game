package client.model;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Created by andreas on 2016-10-28.
 */
public class Light {
    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;

    public Light(){
        ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White.mult(1.3f));


        directionalLight = new DirectionalLight();
        directionalLight.setColor(ColorRGBA.White);
        directionalLight.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());

    }
    public void addLight(Node node){
        node.addLight(ambientLight);
        node.addLight(directionalLight);
    }

}
