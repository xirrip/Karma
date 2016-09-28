package org.hippomeetsskunk.physics.particles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SRZCHX on 28.09.2016.
 *
 * More alloc efficient would maybe be to keep the max particles list
 * and mark particles as NOT_USED -> skip in update
 * then adding / removing would be easier
 * resizing could still be dynamic
 * for added elements, state could be set to CREATED and then only activate in the next iteration...
 */
public class ParticleSimulator {

    private final LeapFrogIntegrator integrator = new LeapFrogIntegrator();
    private final List<Particle> particles;

    private final List<Particle> addees = new ArrayList<>();
    private final List<Particle> removees = new ArrayList<>();

    public ParticleSimulator(){
        this(new ArrayList<>());
    }

    public ParticleSimulator(List<Particle> particles) {
        // allow to share list with renderer...
        this.particles = particles;
    }

    public void update(double elapsedTime){
        // Loop through all circles, update them
        for (Particle particle : particles) {
            integrator.step(particle, elapsedTime);
            particle.triggerRules(this);
        }

        // update particles list for changes during update (due to rules)
        if(!removees.isEmpty()){
            particles.removeAll(removees);
            removees.clear();
        }
        if(!addees.isEmpty()){
            particles.addAll(addees);
            addees.clear();
        }
    }

    public void add(Particle particle){
        addees.add(particle);
    }

    public void add(List<Particle> particles){
        addees.addAll(particles);
    }

    public void remove(Particle particle){
        removees.add(particle);
    }

    public void replace(Particle oldParticle, Particle newParticle){
        remove(oldParticle);
        add(newParticle);
    }

    public void replace(Particle oldParticle, List<Particle> newParticles){
        remove(oldParticle);
        add(newParticles);
    }
}
