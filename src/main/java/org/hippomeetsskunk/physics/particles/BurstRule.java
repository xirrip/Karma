package org.hippomeetsskunk.physics.particles;

import java.util.Random;

/**
 * Created by SRZCHX on 29.09.2016.
 */
public class BurstRule implements ParticleRule {
    private final int minBursts;
    private final int maxBursts;
    private final double minVelocityBurst;
    private final double maxVelocityBurst;

    public BurstRule(int minBursts, int maxBursts, double minVelocityBurst, double maxVelocityBurst) {
        this.minBursts = minBursts;
        this.maxBursts = maxBursts;
        this.minVelocityBurst = minVelocityBurst;
        this.maxVelocityBurst = maxVelocityBurst;
    }

    @Override
    public void trigger(Particle particle, ParticleSimulator particleSimulator) {
        if(particle.getLifeTime() <= 0.0){
            System.out.println("--- BURST --- ");
            particleSimulator.remove(particle);

            int b = (int) (Math.random() * (maxBursts-minBursts)) + minBursts;

            for(int i=0; i<b; ++i){
                Particle p = new Particle(1.0/particle.getInverseMass()/b);
                p.setPosition(particle.getPosition());
                p.setVelocity(particle.getVelocity());

                p.getVelocity().add(
                        Math.random()*(maxVelocityBurst-minVelocityBurst)+minVelocityBurst,
                        Math.random()*(maxVelocityBurst-minVelocityBurst)+minVelocityBurst,
                        Math.random()*(maxVelocityBurst-minVelocityBurst)+minVelocityBurst);

                // change color and so...???

                particleSimulator.add(p);
            }
        }
    }
}
