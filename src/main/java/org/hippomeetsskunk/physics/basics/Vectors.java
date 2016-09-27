package org.hippomeetsskunk.physics.basics;

import org.hippomeetsskunk.physics.basics.Vector3;

/**
 * Created by SRZCHX on 27.09.2016.
 */
public class Vectors {

    /*
        Right-handed basis.
     */
    static public Vector3 orthonormalize(Vector3 a, Vector3 b){
        a.normalize();

        Vector3 c = Vector3.cross(a, b);
        if(c.length2() <= 0.0){
            throw new IllegalArgumentException("Basis vectors are linearly dependent.");
        }
        c.normalize();
        b.set(Vector3.cross(c, b));

        return c;
    }
}
