package client.model;

import java.util.ArrayList;
import java.util.List;

public class BrickMap {

    private List<BrickPosition> brickPositions = new ArrayList<>();

    public void add(BrickPosition brickPosition){
        brickPositions.add(brickPosition);
    }


}
