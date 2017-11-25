package client.model;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Created by andreas on 2016-10-28.
 */
public class EvilGuy implements AnimEventListener{
    private AssetManager assetManager;
    private Node rootNode;
    private BulletAppState bulletAppState;
    private Spatial golem;
    private CharacterControl control;
    private Vector3f direction = new Vector3f(0,0,1);
    private boolean isWalking = true;
    private String name;
    private int life = 100;
    private MapGraph mapGraph = new MapGraph();
    private WayPoint currentWayPoint = null;
    private boolean chasingPlayer = false;
    private float speed = 0.1f;

    private AnimChannel animChannel;
    private AnimControl animControl;

    public EvilGuy(AssetManager assetManager, BulletAppState bulletAppState, Node rootNode){
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
    }

    public void createEvilGuy(float x, float y, float z, String name, WayPoint firstWayPoint) {
        this.name = name;
        golem = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        control = new CharacterControl(capsuleShape, 0.05f);

        /** Add physical brick to physics space. */
        golem.addControl(control);
        System.out.println(golem.getName());

        bulletAppState.getPhysicsSpace().add(control);
        control.setPhysicsLocation(new Vector3f(x,y,z));
        golem.setName(name);

        currentWayPoint = firstWayPoint;

        animControl = golem.getControl(AnimControl.class);
        animControl.addListener(this);
        animChannel = animControl.createChannel();
        animChannel.setAnim("stand");

        rootNode.attachChild(golem);
    }
    public void move(Vector3f location, Vector3f viewDirection){
        control.setPhysicsLocation(location);
        control.setViewDirection(viewDirection);
    }
    public int damage(int amount){
        chasingPlayer = true;
        this.life -= amount;
        if (life < 0)
            life = 0;
        return life;
    }

    public void die(){
        this.life = 0;
        golem.removeFromParent();
    }
    public String getName() {
        return this.name;
    }

    public void onAnimCycleDone(AnimControl animControl, AnimChannel animChannel, String animName) {
        if (!isWalking) {
            animChannel.setAnim("stand");
            animChannel.setLoopMode(LoopMode.DontLoop);
        }
        if (isWalking){
            animChannel.setAnim("Walk", 0.5f);
            animChannel.setLoopMode(LoopMode.DontLoop);

        }

    }

    public void onAnimChange(AnimControl animControl, AnimChannel animChannel, String s) {

    }
}
