package org.hippomeetsskunk.physics.particles;

/**
 * Created by SRZCHX on 28.09.2016.
 */
public interface ParticleRule {
    void trigger(Particle particle, ParticleSimulator particleSimulator);
}
