package client.model;

import client.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

/**
 * Created by andreas on 2016-10-29.
 */
public class CannonBall {
    private AssetManager assetManager;
    private BulletAppState bulletAppState;
    private Node rootNode;
    private RigidBodyControl ball_phy;
    private Sphere sphere;
    private Material stone_mat;
    private Camera cam;
    private Geometry ball_geo;
    private Node node = new Node("Cannonball");
    private Application application;

    public CannonBall(AssetManager assetManager, BulletAppState bulletAppState,
                      Node node, Camera cam, Application application){
        this.application = application;
        this.assetManager = assetManager;
        this.bulletAppState = bulletAppState;
        this.rootNode = node;
        this.ball_phy = new RigidBodyControl(1f);
        this.cam = cam;
        sphere = new Sphere(32, 32, 1f, true, false);
        sphere.setTextureMode(Sphere.TextureMode.Projected);
        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);

        ball_geo = new Geometry("cannon ball", sphere);
        ball_geo.setMaterial(stone_mat);
        this.node.attachChild(ball_geo);
        node.attachChild(this.node);
        /** BrickPosition the cannon ball  */
        ball_geo.setLocalTranslation(cam.getLocation());
        ball_geo.move(cam.getDirection().mult(5));
//        ball_geo.move(new Vector3f(0,2,0));
        /** Make the ball physcial with a mass > 0.0f */
        ball_phy = new RigidBodyControl(3f);
        /** Add physical ball to physics space. */
        ball_geo.addControl(ball_phy);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        /** Accelerate the physcial ball to shoot it. */
        ball_phy.setLinearVelocity(cam.getDirection().mult(100));
    }
    public void checkForCollisions(Node node){
        CollisionResults results = new CollisionResults();
        BoundingSphere boundingSphere = new BoundingSphere(1f,ball_phy.getPhysicsLocation());
        node.collideWith(boundingSphere, results);
        if (results.size() > 0){
            Geometry closestCollision = results.getClosestCollision().getGeometry();
            String name = closestCollision.getParent().getName();
//            System.out.println("You hit " + name);
//            closestCollision.getParent().removeFromParent();
            application.kill(name);

        }


    }
}
