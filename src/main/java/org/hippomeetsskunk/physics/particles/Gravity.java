package org.hippomeetsskunk.physics.particles;

import org.hippomeetsskunk.physics.PhysicsWorld;
import org.hippomeetsskunk.physics.basics.Vector3;

/**
 * Created by SRZCHX on 27.09.2016.
 */
public class Gravity {

    static public Vector3 gravity(Vector3 lightParticle, double lightMass, Vector3 heavyParticle, double heavyMass){
        Vector3 f = Vector3.addScaled(heavyParticle, lightParticle, -1.0);
        double r2 = f.length2();
        f.normalize();
        f.scale(PhysicsWorld.G * lightMass * heavyMass / r2);
        return f;
    }
}
