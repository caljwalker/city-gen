package citygen;

import citymesh.*;

public class Generator {
    public static void main(String[] args) {
        Mesh m = new Mesh("Cube");
        Vector2[] uvs = new Vector2[]{
            new Vector2(0, 0),
            new Vector2(1, 0),
            new Vector2(1, 1),
            new Vector2(0, 1)
        };
        m.addFace(
            new Vector3[] {
                new Vector3(-1, -1, -1),
                new Vector3(1, -1, -1),
                new Vector3(1, 1, -1),
                new Vector3(-1, 1, -1)
            },
            uvs, 0
        );
        m.addFace(
                new Vector3[] {
                        new Vector3(-1, -1, 1),
                        new Vector3(1, -1, 1),
                        new Vector3(1, 1, 1),
                        new Vector3(-1, 1, 1)
                },
                uvs, 0
        );
        m.addFace(
                new Vector3[] {
                        new Vector3(-1, -1, -1),
                        new Vector3(1, -1, -1),
                        new Vector3(1, -1, 1),
                        new Vector3(-1, -1, 1)
                },
                uvs, 0
        );

        Exporter exp = new ObjExporter();
        if (!exp.init("export\\out"))  System.exit(1);

        for (int i = 0; i < 10; i++) {
            Mesh dup = new Mesh("Cube " + i, m);
            //dup.transform();
            dup.transform(Transformation.translation(i* 3, 0, i).transform(Transformation.rotateZ(i / 3.0f)));
            exp.appendMesh(dup);
        }

        if (!exp.done()) System.exit(1);
        System.exit(0);
    }
}
