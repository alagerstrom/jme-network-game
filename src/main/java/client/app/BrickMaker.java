package client.app;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * Created by andreas on 2016-10-28.
 */
public class BrickMaker {

    private Box box;
    private Material wall_mat;
    private RigidBodyControl    brick_phy;
    private BulletAppState bulletAppState;
    private Node rootNode;


    private static final float brickLength = 3f;
    private static final float brickWidth  = 3f;
    private static final float brickHeight = 3f;

    public BrickMaker(AssetManager assetManager, BulletAppState bulletAppState, Node rootNode){
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
        box = new Box(brickLength, brickHeight, brickWidth);
        box.scaleTextureCoordinates(new Vector2f(1f, .5f));
        wall_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey("Textures/Terrain/BrickWall/BrickWall.jpg");
        key.setGenerateMips(true);
        Texture tex = assetManager.loadTexture(key);
        wall_mat.setTexture("ColorMap", tex);
    }


    public void makeBrick(Vector3f loc) {
        /** Create a brick geometry and attach to scene graph. */
        Geometry brick_geo = new Geometry("brick", box);
        brick_geo.setMaterial(wall_mat);
        rootNode.attachChild(brick_geo);
        /** Position the brick geometry  */
        brick_geo.setLocalTranslation(loc);
        /** Make brick physical with a mass > 0.0f. */
        brick_phy = new RigidBodyControl(10f);
        /** Add physical brick to physics space. */
        brick_geo.addControl(brick_phy);
        bulletAppState.getPhysicsSpace().add(brick_phy);
    }
    public void addBrick(float x, float y, float z){
        makeBrick(new Vector3f(x * brickWidth, y * brickHeight, z * brickLength));
    }
    public void makeWall(float x, float y, float z, int width, int height, int depth){
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                for (int k = 0; k < depth; k++)
                    addBrick(i + x, j + y, z + k);

    }
}
