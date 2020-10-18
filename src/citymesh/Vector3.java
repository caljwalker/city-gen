package citymesh;

/**
 * Immutable 3D vector type
 */
public class Vector3 {

    // Coords
    private float x, y, z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(float x, float y) {
        this(x, y, 0.0f);
    }

    public Vector3(Vector2 vec2) {
        this(vec2.getX(), vec2.getY());
    }

    /**
     * Copy constructor
     * @param vec Vector whose values to copy
     */
    public Vector3(Vector3 vec) {
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public float getZ() { return z; }

    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }

    public boolean equals(Object other) {
        if (!(other instanceof Vector3)) {
            return false;
        } else {
            Vector3 vec = (Vector3) other;
            return vec.x == x && vec.y == y && vec.z == z;
        }
    }

    public int hashCode() {
        return Float.hashCode(x * y * z + x * y + y * z + x + y + z);
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public float sqDistance(Vector3 other) {
        return (x * x + other.x * other.x) +
                (y * y + other.y * other.y) +
                (z * z + other.z * other.z);
    }

    public float distance(Vector3 other) {
        return (float)Math.sqrt(sqDistance(other));
    }
}
