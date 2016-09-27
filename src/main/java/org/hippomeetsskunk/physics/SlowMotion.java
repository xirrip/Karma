package org.hippomeetsskunk.physics;

import org.hippomeetsskunk.physics.particles.Gravity;

/**
 * Created by SRZCHX on 27.09.2016.
 */
public class SlowMotion {

    static public double massChange(double dv){
        return 1.0 / (dv * dv);
    }

    static public double accelerationChange(double dv, double a){
        return dv * a;
    }

    static public double gravityAccelerationChange(double dv){
        return accelerationChange(dv, PhysicsWorld.gravitationalAccelerationOnEarth);
    }

}
