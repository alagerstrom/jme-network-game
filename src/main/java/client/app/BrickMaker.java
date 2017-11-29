package client.app;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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
    private RigidBodyControl brick_phy;
    private BulletAppState bulletAppState;
    private Node rootNode;
    private AssetManager assetManager;

    private static final float brickLength = 2f;
    private static final float brickWidth = 2f;
    private static final float brickHeight = 2f;

    public BrickMaker(AssetManager assetManager, BulletAppState bulletAppState, Node rootNode) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.bulletAppState = bulletAppState;
        box = new Box(brickWidth / 2, brickHeight / 2, brickLength / 2);
        wall_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        setColor(ColorRGBA.Yellow);
        makeWall(-10, 0, -10, 20, 1, 20);
        setColor(ColorRGBA.Red);
        addBrick(1, 3, 1);
        addBrick(1, 4, 1);
        addBrick(1, 3, 2);
        setColor(ColorRGBA.Pink);
        makeWall(3, 3, 3, 3, 3, 3);
        setColor(ColorRGBA.Magenta);
        makeWall(6, 6, 6, 3, 3, 3);

        setColor(ColorRGBA.Blue);
        createTunnel(10, 0, 5, 10, 10, 40, Direction.Z);
        setColor(ColorRGBA.Cyan);
        createTunnel(10, -10, -10, 10, 10, 10, Direction.Y);

    }

    public void setColor(ColorRGBA color){
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey("Textures/Terrain/BrickWall/BrickWall.jpg");
        key.setGenerateMips(true);
        Texture tex = assetManager.loadTexture(key);
        material.setTexture("ColorMap", tex);
        material.setColor("Color", color);
        wall_mat = material;
    }

    private void createBrick(Vector3f loc) {
        /** Create a brick geometry and attach to scene graph. */
        Geometry brick_geo = new Geometry("brick", box);
        brick_geo.setMaterial(wall_mat);
        rootNode.attachChild(brick_geo);
        /** BrickPosition the brick geometry  */
        brick_geo.move(loc);
        /** Make brick physical with a mass > 0.0f. */
        brick_phy = new RigidBodyControl(0);
        /** Add physical brick to physics space. */
        brick_geo.addControl(brick_phy);
        bulletAppState.getPhysicsSpace().add(brick_phy);
    }

    public void addBrick(int x, int y, int z) {
        createBrick(new Vector3f(x * brickWidth, y * brickHeight, z * brickLength));
    }

    public void makeWall(int x, int y, int z, int width, int height, int depth) {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                for (int k = 0; k < depth; k++)
                    addBrick(x + i, y + j, z + k);
    }

    //    public void makeWall(float x, float y, float z, int width, int height, int depth){
//        for (int i = 0; i < width; i++)
//            for (int j = 0; j < height; j++)
//                for (int k = 0; k < depth; k++)
////                    addBrick(i*brickWidth + x, j*brickHeight + y, z + k*brickLength);
//                    createBrick(new Vector3f(i*brickWidth+x,j*brickHeight+y, k*brickLength+z));
//    }

    public enum Direction {
        X, Y, Z
    }

    public void createTunnel(int startX, int startY, int startZ, int width, int height, int depth, Direction direction) {
        switch (direction) {
            case Z:
                createTunnelZ(startX, startY, startZ, width, height, depth);
                break;
            case X:
                createTunnelX(startX, startY, startZ, width, height, depth);
                break;
            case Y:
                createTunnelY(startX, startY, startZ, width, height, depth);
                break;
        }
    }

    private void createTunnelY(int startX, int startY, int startZ, int width, int height, int depth) {
        //floor:
        makeWall(startX, startY, startZ, width, height, 1);
        //walls:
        makeWall(startX, startY, startZ + 1, 1, height, depth-2);
        makeWall(startX+(width-1), startY, startZ + 1, 1,height,depth-2);
        //ceiling:
        makeWall(startX, startY, startZ + (depth - 1), width, height, 1);
    }

    private void createTunnelX(int startX, int startY, int startZ, int width, int height, int depth) {
        //floor:
        makeWall(startX, startY, startZ, width, 1, depth);
        //walls:
        makeWall(startX, startY + 1, startZ, 1, height - 2, depth);
        makeWall(startX + (width - 1), startY + 1, startZ, 1, height - 2, depth);
        //ceiling:
        makeWall(startX, startY + (height - 1), startZ, width, 1, depth);
    }

    private void createTunnelZ(int startX, int startY, int startZ, int width, int height, int depth) {
        //floor:
        makeWall(startX, startY, startZ, width, 1, depth);
        //walls:
        makeWall(startX, startY + 1, startZ, 1, height - 2, depth);
        makeWall(startX + (width - 1), startY + 1, startZ, 1, height - 2, depth);
        //ceiling:
        makeWall(startX, startY + (height - 1), startZ, width, 1, depth);
    }

}
