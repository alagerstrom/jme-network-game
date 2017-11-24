package model;

import com.jme3.math.Vector3f;

import java.util.ArrayList;

/**
 * Created by andreas on 2016-11-06.
 */
public class MapGraph {
    private ArrayList<WayPoint> points = new ArrayList<WayPoint>();

    public MapGraph(){
        points.add(new WayPoint(-48,-88));
        points.add(new WayPoint(-48,52));
        points.add(new WayPoint(-120,50));
        points.add(new WayPoint(-2,57));
        points.add(new WayPoint(70,57));
        points.add(new WayPoint(130,52));
        points.add(new WayPoint(131,-86));
        points.add(new WayPoint(260,47));
        points.add(new WayPoint(300,0));
        points.add(new WayPoint(263,-85));
        points.add(new WayPoint(198,129));
        points.add(new WayPoint(363,65));
        points.add(new WayPoint(-133,-86));
        points.add(new WayPoint(-130,-15));








        connect(points.get(0),points.get(1));
        connect(points.get(1),points.get(2));
        connect(points.get(2),points.get(3));
        connect(points.get(3),points.get(4));
        connect(points.get(4),points.get(5));
        connect(points.get(5),points.get(6));
        connect(points.get(0),points.get(6));
        connect(points.get(5),points.get(7));
        connect(points.get(7),points.get(8));
        connect(points.get(8),points.get(9));
        connect(points.get(6),points.get(9));
        connect(points.get(5),points.get(10));
        connect(points.get(10),points.get(11));
        connect(points.get(0),points.get(12));
        connect(points.get(13),points.get(12));
        connect(points.get(2),points.get(12));











    }
    private void connect(WayPoint firstWayPoint, WayPoint secondWayPoint){
        firstWayPoint.addConnectedPoint(secondWayPoint);
        secondWayPoint.addConnectedPoint(firstWayPoint);
    }
    public WayPoint get(int index){
        return points.get(index);
    }
    public WayPoint getRandomConnectedWayPoint(WayPoint wayPoint){
        ArrayList<WayPoint> connectedPoints = wayPoint.getConnectedPoints();
        if (connectedPoints.size() > 0){
            int randomNumber = (int)(Math.random() * connectedPoints.size());
            return connectedPoints.get(randomNumber);
        }else {
            return null;
        }

    }
    public WayPoint getClosestWayPoint(int x, int z){
        Vector3f vector3f = new Vector3f(x, 5, z);
        float closestDistance = points.get(0).getVector().distance(vector3f);
        int closestIndex = 0;
        for (int i = 1; i < points.size(); i++)
            if (points.get(i).getVector().distance(vector3f) < closestDistance){
                closestDistance = points.get(i).getVector().distance(vector3f);
                closestIndex = i;
            }
        return points.get(closestIndex);
    }
}
