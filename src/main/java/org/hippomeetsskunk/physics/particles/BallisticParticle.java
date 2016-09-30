package org.hippomeetsskunk.physics.particles;

import org.hippomeetsskunk.physics.SlowMotion;
import org.hippomeetsskunk.ui.renderer.passive.ParticleRenderingGameController;

/**
 * Created by SRZCHX on 29.09.2016.
 */
public class BallisticParticle {
    public enum ShotType{
        PISTOL, ARTILLERY, FIREBALL, LASER;
    }

    static public Particle createParticle(ShotType shotType){
        switch(shotType){
            case PISTOL: {
                Particle p = new Particle(0, 2.0);
                p.setVelocity(0.0, 0.0, 35.0);
                p.setAcceleration(0.0, -SlowMotion.gravityAccelerationChange(0.1), 0.0);
                p.setDamping(0.99);
                return p;
            }
            case ARTILLERY: {
                Particle p = new Particle(1, 200.0);
                p.setVelocity(0.0, 30.0, 40.0);
                p.setAcceleration(0.0, -20.0, 0.0);
                p.setDamping(0.99);
                return p;
            }
            case FIREBALL: {
                Particle p = new Particle(2, 1.0);
                p.setVelocity(0.0, 0.0, 10.0);
                p.setAcceleration(0.0, 0.6, 0.0);  // floats up
                p.setDamping(0.9);
                return p;
            }
            case LASER: { // laser bolt, not realistic :-)
                Particle p = new Particle(3, 0.1);
                p.setVelocity(0.0, 0.0, 100.0);
                p.setAcceleration(0.0, 0.0, 0.0);  // no gravity
                p.setDamping(0.99);
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown shot type: " + shotType);
    }

}
