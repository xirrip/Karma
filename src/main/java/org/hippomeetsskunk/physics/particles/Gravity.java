package org.hippomeetsskunk.physics.particles;

import org.hippomeetsskunk.physics.PhysicsWorld;
import org.hippomeetsskunk.physics.basics.Vector3;

/**
 * Created by SRZCHX on 27.09.2016.
 */
public class Gravity implements ParticleForceGenerator {

    private final Particle p1;
    private final Particle p2;

    public Gravity(Particle p1, Particle p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void updateForce(double elapsedTime) {
        Vector3 f = Vector3.addScaled(p2.getPosition(), p1.getPosition(), -1.0);
        double r2 = f.length2();
        f.normalize();
        f.scale(PhysicsWorld.G / p1.getInverseMass() / p2.getInverseMass() / r2);
        p1.addForce(f);
        f.invert();
        p2.addForce(f);
    }

    static public Vector3 gravity(Vector3 lightParticle, double lightMass, Vector3 heavyParticle, double heavyMass){
        Vector3 f = Vector3.addScaled(heavyParticle, lightParticle, -1.0);
        double r2 = f.length2();
        f.normalize();
        f.scale(PhysicsWorld.G * lightMass * heavyMass / r2);
        return f;
    }


}
