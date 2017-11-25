package client.model;

/**
 * Created by andreas on 2016-11-06.
 */
public class SpawnPoint {
    private int x;
    private int z;
    private int noSpawnAreaStartX;
    private int noSpawnAreaStopX;
    private int noSpawnAreaStartZ;
    private int noSpawnAreaStopZ;
    private static final int MARGIN = 150;
    public final WayPoint firstWayPoint;

    public SpawnPoint(int x, int z, WayPoint firstWayPoint){
        this.x = x;
        this.z = z;
        this.noSpawnAreaStartX = x - MARGIN;
        this.noSpawnAreaStopX = x + MARGIN;
        this.noSpawnAreaStartZ = z - MARGIN;
        this.noSpawnAreaStopZ = z + MARGIN;
        this.firstWayPoint = firstWayPoint;
    }
    public void setArea(int startX, int stopX, int startZ, int stopZ){
        this.noSpawnAreaStartX = startX;
        this.noSpawnAreaStopX = stopX;
        this.noSpawnAreaStartZ = startZ;
        this.noSpawnAreaStopZ = stopZ;
    }

    public boolean noSpawnContains(int x, int z){
        boolean result = false;
        if (x < this.noSpawnAreaStopX && x > this.noSpawnAreaStartX)
            if (z < this.noSpawnAreaStopZ && z > this.noSpawnAreaStartZ)
                result = true;
        return result;
    }
    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
