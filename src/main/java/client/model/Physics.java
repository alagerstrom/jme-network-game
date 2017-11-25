package client.model;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.PhysicsControl;

/**
 * Created by andreas on 2016-10-28.
 */
public class Physics {
    private BulletAppState bulletAppState;

    public Physics(){
        bulletAppState = new BulletAppState();
    }
    public void add(PhysicsControl physicsControl){
        bulletAppState.getPhysicsSpace().add(physicsControl);
    }
    public BulletAppState getBulletAppState(){
        return this.bulletAppState;
    }
}
