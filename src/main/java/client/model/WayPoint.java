package client.model;

import com.jme3.math.Vector3f;

import java.util.ArrayList;

/**
 * Created by andreas on 2016-11-06.
 */
public class WayPoint {
    public final int x;
    public final int z;
    private ArrayList<WayPoint> connectedPoints = new ArrayList<WayPoint>();

    public WayPoint(int x, int z){
        this.x = x;
        this.z = z;
    }
    public void addConnectedPoint(WayPoint wayPoint){
        this.connectedPoints.add(wayPoint);
    }
    public ArrayList<WayPoint> getConnectedPoints(){
        return this.connectedPoints;
    }
    public Vector3f getVector(){
        return new Vector3f(this.x, 5, this.z);
    }
}
