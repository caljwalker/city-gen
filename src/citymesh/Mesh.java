package citymesh;

import java.util.*;

public class Mesh {

    // Name of mesh / object
    private String name;

    // Maps coord to ID
    HashMap<Vector3, Integer> verts;
    HashMap<Vector2, Integer> uvs;

    class Face {
        int[] vertIDs;
        int[] uvIDs;
    }
    Set<Face> faces;

    public Mesh() {
        verts = new HashMap<Vector3, Integer>();
        uvs = new HashMap<Vector2, Integer>();
        faces = new HashSet<Face>();
    }

    public int addVert(Vector3 vert) {
        verts.put(vert, verts.size());
        return verts.size() - 1;
    }

    public int addUV(Vector2 uv) {
        uvs.put(uv, uvs.size());
        return uvs.size() - 1;
    }

    /**
     * Get vertex ID, adding it if it doesn't exists
     * @param vert vertex
     * @return vertex ID
     */
    public int getVertID(Vector3 vert) {
        if (!verts.containsKey(vert)) {
            return addVert(vert);
        }
        return verts.get(vert);
    }

    /**
     * Get UV ID, adding it if it doesn't exist
     * @param uv uv coord
     * @return uv coord id
     */
    public int getUVID(Vector2 uv) {
        if (!uvs.containsKey(uv)) {
            return addUV(uv);
        }
        return uvs.get(uv);
    }

    public int addFace(Vector3[] verts, Vector2[] uvs) {
        if (verts.length != uvs.length) {
            return -1;
        }
        Face face = new Face();
        face.vertIDs = new int[verts.length];
        face.uvIDs = new int[uvs.length];
        for (int i = 0; i < verts.length; i++) {
            face.vertIDs[i] = getVertID(verts[i]);
            face.uvIDs[i] = getUVID(uvs[i]);
        }
        faces.add(face);
        return faces.size();
    }

}
