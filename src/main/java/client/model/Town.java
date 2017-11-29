package client.model;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/**
 * Created by andreas on 2016-10-28.
 */
public class Town {
//    private Spatial sceneModel;
    private RigidBodyControl landscape;


    public Town(AssetManager assetManager, ViewPort viewPort){
        assetManager.registerLocator("town.zip", ZipLocator.class);
//        sceneModel = assetManager.loadModel("main.scene");
//        sceneModel.setLocalScale(2f);

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
//        CollisionShape sceneShape =
//                CollisionShapeFactory.createMeshShape(sceneModel);
//        landscape = new RigidBodyControl(sceneShape, 0);
//        sceneModel.addControl(landscape);

        viewPort.setBackgroundColor(ColorRGBA.Black);

    }
    public void addTown(AssetManager assetManager, Node node){
        node.attachChild(SkyFactory.createSky(
                assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        assetManager.registerLocator("assets", FileLocator.class);
//        node.attachChild(sceneModel);
    }
    public RigidBodyControl getLandscape(){
        return this.landscape;
    }
}
