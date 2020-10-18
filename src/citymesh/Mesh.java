package citymesh;

import java.util.*;

public class Mesh {

    // Name of mesh / object
    private String name;

    // Maps coord to ID
    // Mapping seems backwards, but this results in O(1) ID lookup and vertex checks, making adding faces fast.
    // Translation and other operations will be O(n) though and memory-intensive as the map must be cloned.
    Map<Vector3, Integer> verts;
    Map<Vector2, Integer> uvs;

    // Maps IDs to vertices
    //List<Vector3> vertsByID;
    //List<Vector2> uvsByID;

    class Face {
        int[] vertIDs;
        int[] uvIDs;
        int matID;
    }
    SortedSet<Face> faces;

    public Mesh(String name) {
        verts = new HashMap<>();
        uvs = new HashMap<>();

        // Faces are in a set sorted by material first
        faces = new TreeSet<>((a, b) ->
            {
                if (a.matID != b.matID) {
                    return Integer.compare(a.matID, b.matID);
                }
                return Arrays.compare(a.vertIDs, b.vertIDs);
            }
        );
        this.name = name;
    }

    /**
     * Copy constructor
     * @param m mesh to copy from
     */
    public Mesh(String name, Mesh m) {
        this(name);
        append(m, 0);
    }

    public String getName() { return name; }
    public int vertexCount() { return verts.size(); }
    public int faceCount() { return faces.size(); }

    public String toString() {
        return String.format("Mesh(\"%s\", V: %d, F: %d)", name, vertexCount(), faceCount());
    }

    /**
     * Add vertex to vertex set
     * @param vert coordinates of vertex to add
     * @return ID of new vertex
     */
     int addVert(Vector3 vert) {
        verts.put(vert, verts.size());
        return verts.size() - 1;
     }

    /**
     * Add uv to uv set
     * @param uv coordinates of uv to add
     * @return ID of new uv coordinate
     */
    int addUV(Vector2 uv) {
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
     * Get vertex ID, adding it if it doesn't exist
     * Uses a tolerance value to merge nearby vertices
     * @param vert vertex
     * @param tolerance distance to merge
     * @return vertex ID
     */
    public int getVertID(Vector3 vert, float tolerance) {
        Optional<Vector3> result =
            verts.keySet().stream().filter(
                (a) -> a.distance(vert) < tolerance
            ).findAny();
        if (result.isEmpty()) {
            return addVert(vert);
        }
        return verts.get(result.get());
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

    /**
     * Get UV ID, adding it if it doesn't exist
     * @param uv uv coord
     * @param tolerance distance to merge
     * @return uv coord id
     */
    public int getUVID(Vector2 uv, float tolerance) {
        Optional<Vector2> result =
                uvs.keySet().stream().filter(
                        (a) -> a.distance(uv) < tolerance
                ).findAny();
        if (result.isEmpty()) {
            return addUV(uv);
        }
        return uvs.get(result.get());
    }

    /**
     * Add a face to this mesh
     *
     * @param verts array of vertex positions
     * @param uvs array of corresponding texture coordinates
     * @param material integer material ID to use
     * @param tolerance distance to merge verts
     * @return face ID on success, or -1 on failure
     */
    public int addFace(Vector3[] verts, Vector2[] uvs, int material, float tolerance) {
        if (verts.length != uvs.length) {
            return -1;
        }
        Face face = new Face();
        face.vertIDs = new int[verts.length];
        face.uvIDs = new int[uvs.length];
        face.matID = material;
        if (tolerance <= 0.0f) {
            for (int i = 0; i < verts.length; i++) {
                face.vertIDs[i] = getVertID(verts[i]);
                face.uvIDs[i] = getUVID(uvs[i]);
            }
        } else {
            for (int i = 0; i < verts.length; i++) {
                face.vertIDs[i] = getVertID(verts[i], tolerance);
                face.uvIDs[i] = getUVID(uvs[i]);
            }
        }
        faces.add(face);
        return faces.size();
    }

    /**
     * Add a face to this mesh
     *
     * @param verts array of vertex positions
     * @param uvs array of corresponding texture coordinates
     * @param material integer material ID to use
     * @return face ID on success, or -1 on failure
     */
    public int addFace(Vector3[] verts, Vector2[] uvs, int material) {
        return addFace(verts, uvs, material, 0.0f);
    }

    /**
     * Translates all vertices in this mesh.
     * Expensive, so call sparingly.
     *
     * @param amount
     */
    public void translate(Vector3 amount) {
        Map<Vector3, Integer> newverts = new HashMap<>();
        for (Map.Entry<Vector3, Integer> vtx : verts.entrySet()) {
            newverts.put(vtx.getKey().add(amount), vtx.getValue());
        }
        verts = newverts;
    }

    /**
     * Transforms all vertices in this mesh
     * @param t transformation to use
     */
    public void transform(Transformation t) {
        Map<Vector3, Integer> newverts = new HashMap<>();
        for (Map.Entry<Vector3, Integer> vtx : verts.entrySet()) {
            newverts.put(t.transform(vtx.getKey()), vtx.getValue());
        }
        verts = newverts;
    }

    /**
     * Append another mesh to this one
     * @param other another mesh to add
     * @param tolerance distance to merge vertices
     */
    public void append(Mesh other, float tolerance) {
        for (Face f : other.faces) {
            Vector3[] verts = new Vector3[f.vertIDs.length];
            Vector2[] uvs = new Vector2[f.uvIDs.length];

            for (Map.Entry<Vector3, Integer> vtx : other.verts.entrySet()) {
                for (int i = 0; i < verts.length; i++) {
                    if (vtx.getValue() == f.vertIDs[i]) {
                        verts[i] = vtx.getKey();
                    }
                }
            }

            for (Map.Entry<Vector2, Integer> uv : other.uvs.entrySet()) {
                for (int i = 0; i < verts.length; i++) {
                    if (uv.getValue() == f.uvIDs[i]) {
                        uvs[i] = uv.getKey();
                    }
                }
            }

            addFace(verts, uvs, f.matID, tolerance);
        }
    }
}
