package org.hippomeetsskunk.physics.particles;

import org.hippomeetsskunk.physics.basics.Vector3;

/**
 * Created by SRZCHX on 27.09.2016.
 */
public class LeapFrogIntegrator {

    // dt needs to be constant over the frames
    // could possibly be improved by also knowing last dt...?
    public void step(Particle particle, double dt){
        if(particle.getInverseMass() <= 0) return;

        // update position
        particle.getPosition().addScaled(particle.getVelocity(), dt);

        // drag on velocity
        particle.getVelocity().scale(Math.pow(particle.getDamping(), dt));

        // F = m*a
        Vector3 acceleration = new Vector3(particle.getAcceleration());
        if(particle.getForce()!=null) {
            acceleration.addScaled(particle.getForce(), particle.getInverseMass());
        }

        // update velocity
        particle.getVelocity().addScaled(acceleration, dt);
    }
}
