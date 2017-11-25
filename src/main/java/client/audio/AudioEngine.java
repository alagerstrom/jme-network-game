package client.audio;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;
import client.exceptions.AudioEngineException;


/**
 * Created by andreas on 2016-10-28.
 */
public class AudioEngine {
    private AudioNode[] audioGun = new AudioNode[2];
    private int currentGun = 0;
    private AudioNode audio_nature;
    private AssetManager assetManager;
    private Node rootNode;
    private static AudioEngine instance = null;

    public AudioEngine(AssetManager assetManager, Node rootNode){
        if (instance != null)
            throw new AudioEngineException("Audio engine has already been initialized.");
        instance = this;
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        initAudio();
    }
    public static AudioEngine getInstance(){
        if (instance == null)
            throw new AudioEngineException("Audio engine has not been initialized.");
        return instance;
    }
    private void initAudio() {
    /* gun shot sound is to be triggered by a mouse click. */
        for (int i = 0; i < audioGun.length; i++){
            audioGun[i] = new AudioNode(assetManager, "sound/m4a1.wav", false);
            audioGun[i].setPositional(false);
            audioGun[i].setLooping(false);
            audioGun[i].setVolume(1);
            rootNode.attachChild(audioGun[i]);
        }

//
    /* nature sound - keeps playing in a loop. */
        audio_nature = new AudioNode(assetManager, "Sound/Environment/Nature.ogg", false);
        audio_nature.setLooping(true);  // activate continuous playing
        audio_nature.setPositional(false);
        audio_nature.setVolume(1);
        rootNode.attachChild(audio_nature);
        audio_nature.play(); // play continuously!
    }
    public void playGunSound(){
        currentGun++;
        if (currentGun >= audioGun.length)
            currentGun = 0;
        audioGun[currentGun].stop();
        audioGun[currentGun].play();
    }
}
