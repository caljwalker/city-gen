package citymesh;

/**
 * Immutable 2D Vector Type
 */
public class Vector2 {

    public static final Vector2 ORIGIN = new Vector2(0, 0);

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

    public float sqDistance(Vector2 other) {
        return (x * x + other.x * other.x) +
                (y * y + other.y * other.y);
    }

    public float distance(Vector2 other) {
        return (float)Math.sqrt(sqDistance(other));
    }

    public Vector2 normalized() {
        float len = distance(ORIGIN);
        if (len == 0) return ORIGIN; // don't divide by 0
        return new Vector2(x / len, y / len);
    }
}
