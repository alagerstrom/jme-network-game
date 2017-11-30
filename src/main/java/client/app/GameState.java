package client.app;

import client.audio.AudioEngine;
import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.collision.CollisionResults;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import client.model.*;

import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

/**
 * Created by andreas on 2016-10-28.
 */
public class GameState extends BaseAppState {
    private Player player;
    private Town town;
    private UpdateLoop updateLoop = new UpdateLoop();
    private ArrayList<EvilGuy> evilGuys = new ArrayList<EvilGuy>();
    private Node shootables = new Node();
    private Node bricks = new Node();
    private long lastSpawnTime = 0;
    private long lastFireTime = 0;
    private boolean isFiring = false;
    private ArrayList<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
    private static final int NUMBER_OF_ENEMIES = 20;
    private static final int SPAWN_DELAY = 5000;
    private MapGraph mapGraph = new MapGraph();
    private NetDelegate netDelegate;

    private AssetManager assetManager;
    private SimpleApplication app;
    private Node rootNode;
    private AppStateManager stateManager;
    private InputManager inputManager;
    private ViewPort viewPort;
    private BulletAppState physics;

//    @Override
//    public void simpleInitApp() {
//
//
////
//
//
//    }

    private void startSendingPositionUpdates() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                sendMyPosition();
            }
        }, 100, 100);
    }

    private void sendMyPosition() {
        Camera cam = getApplication().getCamera();
        Message message = new Message()
                .setLocation(player.getPlayer().getPhysicsLocation())
                .setDirecion(cam.getDirection());
        netDelegate.send(message);
    }

    private void setUpNetDelegate() {
        CompletionHandler<Void, Void> completionHandler = new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void result, Void attachment) {
                startSendingPositionUpdates();
            }

            @Override
            public void failed(Throwable exc, Void attachment) {

            }
        };
        CompletableFuture.runAsync(() -> {
            try {
                netDelegate = new NetDelegate(this);
                completionHandler.completed(null, null);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to create net delegate");
            }
        });

    }

    public NetDelegate getNetDelegate(){
        return this.netDelegate;
    }

    public void incomingMessage(Message message, String sender) {

        getApplication().enqueue(()->{
            for (EvilGuy evilGuy : evilGuys) {
                if (evilGuy.getName().equals(sender)) {
                    evilGuy.move(message.getLocation(), message.getDirecion());
                }
            }
        });
    }

    private void initSpawnPoints() {
        spawnPoints.add(new SpawnPoint(-19, -155, mapGraph.get(0)));
        spawnPoints.add(new SpawnPoint(355, -38, mapGraph.get(11)));
        spawnPoints.add(new SpawnPoint(-104, -15, mapGraph.get(12)));
    }

    private void createEvilGuy(int x, int y, int z, String name, WayPoint firstWayPoint) {
        EvilGuy evilGuy = new EvilGuy(assetManager, physics, shootables);
        evilGuy.createEvilGuy(x, y, z, name, firstWayPoint);
        evilGuys.add(evilGuy);
    }

    protected void setUpCrossHairs() {
        app.setDisplayStatView(false);
    }

    private void setUpLight() {
        Light light = new Light();
        light.addLight(this.rootNode);
    }

    private void setUpBricks() {
        BrickMaker brickMaker = new BrickMaker(assetManager, physics, bricks);
        rootNode.attachChild(bricks);

    }

    private void setUpTown() {
        town = new Town(assetManager, viewPort);
        town.addTown(assetManager, rootNode);
//        physics.add(town.getLandscape());
    }

    private void setUpPlayer() {
        player = new Player();
    }

    private void setUpAudio() {
        AudioEngine audioEngine = new AudioEngine(assetManager, rootNode);
    }

    private void setUpPhysics() {
//        stateManager.attach(state);
//        physics.add(player.getPlayer());
//        physics.getPhysicsSpace().add(town.getLandscape());
        physics.getPhysicsSpace().add(player.getPlayer());

    }

    private void setUpKeys() {
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);

        Keyboard keyboard = new Keyboard();
        keyboard.addKey(new Key("Menu", KeyInput.KEY_ESCAPE, new KeyPressHandler() {
            @Override
            public void onPress(boolean isPressed) {
                stateManager.getState(MenuState.class).setEnabled(true);
                stateManager.getState(GameState.class).setEnabled(false);
            }
        }));
        keyboard.addKey(new Key("Left", KeyInput.KEY_A, new KeyPressHandler() {
            public void onPress(boolean isPressed) {
                updateLoop.left(isPressed);
            }
        }));
        keyboard.addKey(new Key("Right", KeyInput.KEY_D, new KeyPressHandler() {
            public void onPress(boolean isPressed) {
                updateLoop.right(isPressed);
            }
        }));
        keyboard.addKey(new Key("Up", KeyInput.KEY_W, new KeyPressHandler() {
            public void onPress(boolean isPressed) {
                updateLoop.up(isPressed);
            }
        }));
        keyboard.addKey(new Key("Down", KeyInput.KEY_S, new KeyPressHandler() {
            public void onPress(boolean isPressed) {
                updateLoop.down(isPressed);
            }
        }));
        keyboard.addKey(new Key("Jump", KeyInput.KEY_SPACE, new KeyPressHandler() {
            public void onPress(boolean isPressed) {
                if (isPressed)
                    updateLoop.jump();
            }
        }));
        keyboard.addKey(new Key("Shoot", KeyInput.KEY_LCONTROL, new KeyPressHandler() {
            public void onPress(boolean isPressed) {
                GameState.this.isFiring = isPressed;
            }
        }));
        keyboard.addMouseKey(new MouseKey("Shoot2", MouseInput.BUTTON_LEFT, new KeyPressHandler() {
            public void onPress(boolean isPressed) {
                GameState.this.isFiring = isPressed;
            }
        }));
        keyboard.addKeysToInputManager(inputManager);
    }

    private void fire() {
//        CollisionResults results = new CollisionResults();
//        Camera cam = getApplication().getCamera();
//        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
//        shootables.collideWith(ray, results);
//        if (results.size() > 0) {
//            float dist = results.getClosestCollision().getDistance();
//            String hit = results.getClosestCollision().getGeometry().getParent().getName();
//            System.out.println("You shot " + hit + " at a distance of " + dist);
//            int index = -1;
//            for (EvilGuy evilGuy : evilGuys)
//                if (evilGuy.getName().equals(hit))
//                    index = evilGuys.indexOf(evilGuy);
//            if (index > -1) {
//                int damage = (int) (30 - (dist / 10));
//                if (damage < 0)
//                    damage = 0;
//                EvilGuy evilGuy = evilGuys.get(index);
//                if (evilGuy.damage(damage) == 0) {
//                    evilGuy.die();
//                    evilGuys.remove(evilGuy);
//                }
//            }
//        }
    }

    private void spawn() {
        if (evilGuys.size() < NUMBER_OF_ENEMIES && System.currentTimeMillis() - lastSpawnTime > SPAWN_DELAY) {
            lastSpawnTime = System.currentTimeMillis();
            boolean hasSpawn = false;
            int randomNumber = (int) (Math.random() * spawnPoints.size());
            SpawnPoint randomSpawnPoint = spawnPoints.get(randomNumber);
            if (!hasSpawn && !randomSpawnPoint.noSpawnContains((int) player.getPlayer().getPhysicsLocation().getX(),
                    (int) player.getPlayer().getPhysicsLocation().getZ())) {
                createEvilGuy(randomSpawnPoint.getX(), 5, randomSpawnPoint.getZ(), "Evil guy " + (evilGuys.size() + 1), randomSpawnPoint.firstWayPoint);
                hasSpawn = true;
            }

            for (SpawnPoint spawnPoint : spawnPoints) {
                if (!hasSpawn && !spawnPoint.noSpawnContains((int) player.getPlayer().getPhysicsLocation().getX(),
                        (int) player.getPlayer().getPhysicsLocation().getZ())) {
                    createEvilGuy(spawnPoint.getX(), 5, spawnPoint.getZ(), "Evil guy " + (evilGuys.size() + 1), spawnPoint.firstWayPoint);
                    hasSpawn = true;
                }
            }

        }
    }

    public void kill(String name) {
//        this.evilGuys.get(evilGuys.indexOf())
        int index = -1;
        for (EvilGuy evilGuy : evilGuys)
            if (evilGuy.getName().equals(name))
                index = evilGuys.indexOf(evilGuy);
        if (index > -1) {
            evilGuys.get(index).die();
            evilGuys.remove(evilGuys.get(index));

        }

    }

    public void newConnectedPlayer(String uniqueName) {
        createEvilGuy(spawnPoints.get(0).getX(), 5, spawnPoints.get(0).getZ(), uniqueName, spawnPoints.get(0).firstWayPoint);
    }

    @Override
    protected void initialize(Application app) {

//        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort = this.app.getViewPort();
        this.physics = this.stateManager.getState(BulletAppState.class);

        setUpLight();
        setUpPlayer();
        setUpTown();
        setUpPhysics();
        setUpCrossHairs();
        setUpBricks();
        setUpAudio();
        initSpawnPoints();
        Node rootNode = ((SimpleApplication) getApplication()).getRootNode();
        rootNode.attachChild(shootables);

        ((SimpleApplication) app).getCamera().setLocation(new Vector3f(-100,50,-100));
        app.getCamera().lookAt(new Vector3f(0,0,0),new Vector3f(0,1,0));
        ((SimpleApplication) app).getFlyByCamera().setRotationSpeed(0);
        ((SimpleApplication) app).getFlyByCamera().setMoveSpeed(0);

        setUpKeys();
//
        setUpNetDelegate();

//

    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        Camera cam = getApplication().getCamera();
        if (isEnabled())
            updateLoop.loop(cam, player);
        if (isFiring && System.currentTimeMillis() - lastFireTime > 100) {
            lastFireTime = System.currentTimeMillis();
            AudioEngine.getInstance().playGunSound();
            GameState.this.fire();

        }
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
//        We re-use the flyby camera for rotation, while positioning is handled by physics

        stateManager.getState(FlyCamAppState.class).setEnabled(true);
        FlyByCamera flyCam = this.app.getFlyByCamera();
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        flyCam.setRotationSpeed(2f);
        flyCam.setEnabled(true);
        flyCam.setDragToRotate(false);


    }

    @Override
    protected void onDisable() {
        stateManager.getState(FlyCamAppState.class).setEnabled(false);
        FlyByCamera flyCam = this.app.getFlyByCamera();
        flyCam.setMoveSpeed(0);
        flyCam.setRotationSpeed(0);

    }
}
