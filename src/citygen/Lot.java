package citygen;

import citymesh.*;

import java.util.ArrayList;
import java.util.List;

public class Lot {

    /**
     * Vertices which define the lot shape, in order!
     */
    List<Vector3> vertices;

    public Lot() {
        vertices = new ArrayList<>();
    }

    public boolean addPoint(Vector3 pt) {
        return vertices.add(pt);
    }

    public Building generateBuildings() {

    }

}
