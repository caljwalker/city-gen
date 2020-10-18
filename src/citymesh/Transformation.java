package citymesh;

/**
 * Represents an affine transformation
 */
public class Transformation {

    // There must be 16 of these
    float[] elements;

    private Transformation(float[] elements) {
        this.elements = elements;
    }

    private Transformation() {
        elements = new float[] {
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        };
    }

    public static Transformation identity() {
        return new Transformation(new float[] {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });
    }

    public static Transformation translation(Vector3 v) {
        return new Transformation(new float[] {
                1, 0, 0, v.getX(),
                0, 1, 0, v.getY(),
                0, 0, 1, v.getZ(),
                0, 0, 0, 1
        });
    }

    public static Transformation translation(float x, float y, float z) {
        return new Transformation(new float[] {
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        });
    }

    /**
     * Rotation around unit vector
     * @param axis unit axis
     * @param amount rotation amount in radians
     */
    public static Transformation rotation(Vector3 axis, float amount) {
        return new Transformation(new float[] {
                axis.getX() * axis.getX() * (1.0f - (float)Math.cos(amount)) + (float)Math.cos(amount),
                axis.getY() * axis.getX() * (1.0f - (float)Math.cos(amount)) - axis.getZ() * (float)Math.sin(amount),
                axis.getZ() * axis.getX() * (1.0f - (float)Math.cos(amount)) + axis.getY() * (float)Math.sin(amount),
                0,
                axis.getX() * axis.getY() * (1.0f - (float)Math.cos(amount)) + axis.getZ() * (float)Math.sin(amount),
                axis.getY() * axis.getY() * (1.0f - (float)Math.cos(amount)) + (float)Math.cos(amount),
                axis.getZ() * axis.getY() * (1.0f - (float)Math.cos(amount)) - axis.getX() * (float)Math.sin(amount),
                0,
                axis.getX() * axis.getZ() * (1.0f - (float)Math.cos(amount)) - axis.getY() * (float)Math.sin(amount),
                axis.getY() * axis.getZ() * (1.0f - (float)Math.cos(amount)) + axis.getX() * (float)Math.sin(amount),
                axis.getZ() * axis.getZ() * (1.0f - (float)Math.cos(amount)) + (float)Math.cos(amount),
                0,
                0, 0, 0, 1
        });
    }

    /**
     * Rotation about +z axis
     * @param amount amount to rotate in radians
     */
    public static Transformation rotateZ(float amount) {
        return new Transformation(new float[] {
                (float)Math.cos(amount), -(float)Math.sin(amount), 0, 0,
                (float)Math.sin(amount), (float)Math.cos(amount), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });
    }

    /**
     * Scale about origin
     * @param factor amount to scale by
     */
    public static Transformation scale(Vector3 factor) {
        return new Transformation(new float[] {
                factor.getX(), 0, 0, 0,
                0, factor.getY(), 0, 0,
                0, 0, factor.getZ(), 0,
                0, 0, 0, 1
        });
    }
    /**
     * Scale about origin
     */
    public static Transformation scale(float xs, float ys, float zs) {
        return new Transformation(new float[] {
                xs, 0, 0, 0,
                0, ys, 0, 0,
                0, 0, zs, 0,
                0, 0, 0, 1
        });
    }

    /**
     * Transforms this transformation by another
     * @param t other transform
     * @return new transformation of result of transforming this by t
     */
    public Transformation transform(Transformation t) {
        Transformation ret = new Transformation();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ret.elements[i * 4 + j] =
                        elements[i * 4]     * t.elements[j]     +
                        elements[i * 4 + 1] * t.elements[j + 4] +
                        elements[i * 4 + 2] * t.elements[j + 8] +
                        elements[i * 4 + 3] * t.elements[j + 12];
            }
        }
        return ret;
    }

    /**
     * Transform a point using this transformation
     * @param v coordinates to transform
     * @return a new transformed coordinate
     */
    public Vector3 transform(Vector3 v) {
        return new Vector3(
            v.getX() * elements[0] + v.getY() * elements[1] + v.getZ() * elements[2] + elements[3],
            v.getX() * elements[4] + v.getY() * elements[5] + v.getZ() * elements[6] + elements[7],
            v.getX() * elements[8] + v.getY() * elements[9] + v.getZ() * elements[10] + elements[11]
        );
    }

}
