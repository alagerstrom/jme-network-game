package client.app;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import client.model.Player;

/**
 * Created by andreas on 2016-10-28.
 */
public class UpdateLoop {
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private boolean strafeLeft = false, strafeRight = false, forward = false,
            back = false, up = false, down = false;
    private Player player;

    public void loop(Camera cam, Player player){
        this.player = player;
        camDir.set(cam.getDirection()).multLocal(0.5f);
        camLeft.set(cam.getLeft()).multLocal(0.2f);
        walkDirection.set(0, 0, 0);
        if (strafeLeft) {
            walkDirection.addLocal(camLeft);
        }
        if (strafeRight) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (forward) {
            walkDirection.addLocal(camDir);
        }
        if (back) {
            walkDirection.addLocal(camDir.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        walkDirection.set(1,0);
        walkDirection.divide(walkDirection.length());
        player.getPlayer().setWalkDirection(walkDirection);
        cam.setLocation(player.getPlayer().getPhysicsLocation());
//        System.out.println("Player location: " + player.getPlayer().getPhysicsLocation().getX() + ", "
//                                + player.getPlayer().getPhysicsLocation().getZ());

    }
    public void left(boolean isPressed){
        strafeLeft = isPressed;
    }
    public void right(boolean isPressed){
        strafeRight = isPressed;
    }
    public void up(boolean isPressed){
        up = isPressed;
    }
    public void down(boolean isPressed){
        down = isPressed;
    }
    public void jump(){
        this.player.getPlayer().jump();
    }
}
