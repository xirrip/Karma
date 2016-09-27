package org.hippomeetsskunk.physics.particles;

import org.hippomeetsskunk.physics.basics.Vector3;

/**
 * Created by SRZCHX on 27.09.2016.
 */
public class Particle {
    private double inverseMass;

    private Vector3 position;
    private Vector3 velocity;

    private Vector3 acceleration; // constant acceleration like gravity

    // remove energy (possibly added through inacurracy in integration)
    private double damping;


    public Particle(double mass){
        setMass(mass);
    }

    public void setMass(double mass){
        if(Double.isInfinite(mass)){
            inverseMass = 0.0;
        }
        else{
            inverseMass = 1.0 / mass;
        }
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public Vector3 getAcceleration() {
        return acceleration;
    }

    public double getDamping() {
        return damping;
    }

    public double getInverseMass() {
        return inverseMass;
    }

    public void setPosition(double x, double y, double z){
        position.set(x, y, z);
    }

    public void setVelocity(double x, double y, double z){
        velocity.set(x, y, z);
    }

    public void setAcceleration(double x, double y, double z){
        acceleration.set(x, y, z);
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

    public double kineticEnergy(){
        return 1.0 / (2.0 * inverseMass) * velocity.magnitude2();
    }
}
