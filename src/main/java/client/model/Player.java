package client.model;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;

/**
 * Created by andreas on 2016-10-28.
 */
public class Player {
    private CharacterControl player;

    public Player(){
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(new Vector3f(0, -30, 0));
        player.setPhysicsLocation(new Vector3f(0, 20, 0));
    }
    public CharacterControl getPlayer(){
        return this.player;
    }
}
