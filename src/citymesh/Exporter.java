package citymesh;

/**
 * Interface for 3D file format exporters
 */
public interface Exporter {

    /**
     * Get ready to export
     *
     * @param filename file name to export to excluding extension
     * @return true on success, false on failure
     */
    boolean init(String filename);

    /**
     * Call when exporting is done to close open files and clean up.
     * @return true on success, false on failure
     */
    boolean done();

    /**
     * Export a mesh
     *
     * @param m mesh to read and export
     * @return true on success, false on failure
     */
    boolean appendMesh(Mesh m);

}
