package app;

import audio.AudioEngine;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import model.*;

import java.util.ArrayList;

/**
 * Created by andreas on 2016-10-28.
 */
public class Application extends SimpleApplication {
    private Player player;
    private Town town;
    private UpdateLoop updateLoop = new UpdateLoop();
    private Physics physics;
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

    private void initSpawnPoints(){
        spawnPoints.add(new SpawnPoint(-19,-155, mapGraph.get(0)));
        spawnPoints.add(new SpawnPoint(355,-38, mapGraph.get(11)));
        spawnPoints.add(new SpawnPoint(-104,-15, mapGraph.get(12)));



    }
    public void simpleInitApp() {
        // We re-use the flyby camera for rotation, while positioning is handled by physics
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        flyCam.setRotationSpeed(2f);

        setUpTown();
        setUpLight();
        setUpPlayer();
        setUpPhysics();
        setUpKeys();
        setUpCrossHairs();
        setUpAudio();
        setUpBricks();
        initSpawnPoints();
        rootNode.attachChild(shootables);
    }
    private void createEvilGuy(int x, int y, int z, String name, WayPoint firstWayPoint){
        EvilGuy evilGuy = new EvilGuy(assetManager, physics.getBulletAppState(), shootables);
        evilGuy.createEvilGuy(x,y,z,name,firstWayPoint);
        evilGuys.add(evilGuy);
    }
    protected void setUpCrossHairs() {
        setDisplayStatView(false);
        new CrossHairs().setUpCrossHairs(assetManager, guiFont, settings, guiNode);
    }
    private void setUpLight() {
        Light light = new Light();
        light.addLight(this.rootNode);
    }
    private void setUpBricks(){
        BrickMaker brickMaker = new BrickMaker(assetManager, physics.getBulletAppState(), bricks);
        brickMaker.makeWall(10,10,10,5,5,5);
        rootNode.attachChild(bricks);

    }
    private void setUpTown(){
        town = new Town(assetManager, viewPort);
        town.addTown(assetManager, rootNode);
    }
    private void setUpPlayer(){
        player = new Player();
    }
    private void setUpAudio(){
        AudioEngine audioEngine = new AudioEngine(assetManager, rootNode);
    }
    private void setUpPhysics(){
        physics = new Physics();
        stateManager.attach(physics.getBulletAppState());
        physics.add(town.getLandscape());
        physics.add(player.getPlayer());
    }
    private void setUpKeys(){
        Keyboard keyboard = new Keyboard();
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
        keyboard.addKey(new Key("Shoot", KeyInput.KEY_RCONTROL, new KeyPressHandler() {
            public void onPress(boolean isPressed) {
                Application.this.isFiring = isPressed;
            }
        }));
        keyboard.addMouseKey(new MouseKey("Shoot2", MouseInput.BUTTON_LEFT, new KeyPressHandler() {
            public void onPress(boolean isPressed) {
                Application.this.isFiring = isPressed;
            }
        }));
        keyboard.addKeysToInputManager(inputManager);
    }

    private void fire() {
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        shootables.collideWith(ray, results);
        if (results.size() > 0){
            float dist = results.getClosestCollision().getDistance();
            String hit = results.getClosestCollision().getGeometry().getParent().getName();
            System.out.println("You shot " + hit + " at a distance of " + dist);
            int index = -1;
            for (EvilGuy evilGuy : evilGuys)
                if (evilGuy.getName().equals(hit))
                    index = evilGuys.indexOf(evilGuy);
            if (index > -1){
                int damage = (int)(30 - (dist / 10));
                if (damage < 0)
                    damage = 0;
                EvilGuy evilGuy = evilGuys.get(index);
                if (evilGuy.damage(damage) == 0){
                    evilGuy.die();
                    evilGuys.remove(evilGuy);
                }
            }
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        for (EvilGuy evilguy: evilGuys)
            evilguy.move(tpf, player);
        updateLoop.loop(cam, player);
        spawn();
        if (isFiring && System.currentTimeMillis() - lastFireTime > 100){
            lastFireTime = System.currentTimeMillis();
            AudioEngine.getInstance().playGunSound();
            Application.this.fire();
        }

    }
    private void spawn(){
        if (evilGuys.size() < NUMBER_OF_ENEMIES && System.currentTimeMillis() - lastSpawnTime > SPAWN_DELAY){
            lastSpawnTime = System.currentTimeMillis();
            boolean hasSpawn = false;
            int randomNumber = (int) (Math.random() * spawnPoints.size());
            SpawnPoint randomSpawnPoint = spawnPoints.get(randomNumber);
            if (!hasSpawn && !randomSpawnPoint.noSpawnContains((int) player.getPlayer().getPhysicsLocation().getX(),
                    (int) player.getPlayer().getPhysicsLocation().getZ())) {
                createEvilGuy(randomSpawnPoint.getX(), 5, randomSpawnPoint.getZ(), "Evil guy " + (evilGuys.size() + 1), randomSpawnPoint.firstWayPoint);
                hasSpawn = true;
            }

            for (SpawnPoint spawnPoint : spawnPoints){
                if (!hasSpawn && !spawnPoint.noSpawnContains((int) player.getPlayer().getPhysicsLocation().getX(),
                        (int) player.getPlayer().getPhysicsLocation().getZ())){
                    createEvilGuy(spawnPoint.getX(),5,spawnPoint.getZ(),"Evil guy " + (evilGuys.size() + 1), spawnPoint.firstWayPoint);
                    hasSpawn = true;
                }
            }

        }
    }
    public void kill(String name){
//        this.evilGuys.get(evilGuys.indexOf())
        int index = -1;
        for (EvilGuy evilGuy : evilGuys)
            if (evilGuy.getName().equals(name))
                index = evilGuys.indexOf(evilGuy);
        if (index > -1){
            evilGuys.get(index).die();
            evilGuys.remove(evilGuys.get(index));

        }

    }
}
