package citymesh;

/**
 * Immutable 2D Vector Type
 */
public class Vector2 {

    // Coords
    private float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor
     * @param vec Vector whose values to copy
     */
    public Vector2(Vector2 vec) {
        this(vec.getX(), vec.getY());
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public float getU() { return x; }

    public float getV() { return y; }

    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    public boolean equals(Object other) {
        if (!(other instanceof Vector2)) {
            return false;
        } else {
            Vector2 vec = (Vector2) other;
            return vec.x == x && vec.y == y;
        }
    }

    public int hashCode() {
        return Float.hashCode(x * y + x + y);
    }
}
