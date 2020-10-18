package citymesh;

import java.io.*;
import java.util.*;

/**
 * Exporter which uses the obj file format
 */
public class ObjExporter implements Exporter {

    // Stuff for file writing
    private FileOutputStream objFile, mtlFile;
    private PrintStream objWriter, mtlWriter;

    /**
     * Get ready to export. Prints file header in file to export. Call only once!
     *
     * @param filename file name to export to excluding extension
     */
    @Override
    public boolean init(String filename) {
        try {
            objFile = new FileOutputStream(filename + ".obj");
            mtlFile = new FileOutputStream(filename + ".mtl");
            objWriter = new PrintStream(objFile);
            mtlWriter = new PrintStream(mtlFile);

            // Print a header message
            objWriter.println("# Generated with Gigopolis City Generator");
            objWriter.println("# https://github.com/caljwalker/city-gen");

        } catch (Exception e) {
            System.out.println("Failed to initialize output stream!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Close output streams.
     *
     * @return true on success, false on failures
     */
    @Override
    public boolean done() {
        try {
            objFile.close();
            mtlFile.close();
            objWriter = null;
            mtlWriter = null;
        } catch (IOException e) {
            System.out.println("Failed to close output stream!");
            System.err.println("IO exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Export a mesh
     *
     * @param m mesh to read and export
     * @return true on success, false on failure
     */
    @Override
    public synchronized boolean appendMesh(Mesh m) {
        if (objFile == null || mtlFile == null || objWriter == null || mtlWriter == null) {
            System.err.println("Failed to export mesh" + m + "!");
            return false;
        }
        try {
            objWriter.println("o " + m.getName());
            writeMesh(m, objWriter, mtlWriter);
        } catch (IOException e) {
            System.out.println("Failed to export mesh" + m + "!");
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Writes mesh data using specified print streams
     * @param m mesh to export
     * @param obj object writer
     * @param mtl material writer
     * @throws IOException if error occurs while writing
     */
    private void writeMesh(Mesh m, PrintStream obj, PrintStream mtl) throws IOException {
        // Sort verts by ID, then print them
        Vector3[] sortedVerts = new Vector3[m.vertexCount()];
        for (Map.Entry<Vector3, Integer> vtx : m.verts.entrySet()) {
            sortedVerts[vtx.getValue()] = vtx.getKey();
        }
        for (Vector3 vtx : sortedVerts) {
            obj.printf("v %f %f %f%n", vtx.getX(), vtx.getY(), vtx.getZ());
        }

        // Sort UVs by ID, then print them
        Vector2[] sortedUVs = new Vector2[m.uvs.size()];
        for (Map.Entry<Vector2, Integer> uv : m.uvs.entrySet()) {
            sortedUVs[uv.getValue()] = uv.getKey();
        }
        for (Vector2 uv : sortedUVs) {
            obj.printf("vt %f %f%n", uv.getU(), uv.getV());
        }

        // Output faces
        int lastmat = -1;  // Material of last face we saw
        for (Mesh.Face f : m.faces) {
            // If material changed, put in change material directive
            if (f.matID != lastmat) {
                lastmat = f.matID;
                obj.println("usemtl mat" + f.matID);
            }
            // Calculate the vert ids
            obj.print("f");
            for (int i = 0; i < f.vertIDs.length; i++) {
                // Recalculate vert IDs as offsets
                int vtxid = f.vertIDs[i] - m.verts.size();
                int uvid = f.uvIDs[i] - m.uvs.size();
                obj.printf(" %d/%d", vtxid, uvid);
            }
            obj.println();
        }
    }
}
