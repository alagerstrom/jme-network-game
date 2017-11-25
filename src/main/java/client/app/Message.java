package client.app;

import com.jme3.math.Vector3f;

import java.io.Serializable;

public class Message implements Serializable{
    private Vector3f location;
    private Vector3f direcion;

    public Vector3f getLocation() {
        return location;
    }

    public Message setLocation(Vector3f location) {
        this.location = location;
        return this;
    }

    public Vector3f getDirecion() {
        return direcion;
    }

    public Message setDirecion(Vector3f direcion) {
        this.direcion = direcion;
        return this;
    }
}
