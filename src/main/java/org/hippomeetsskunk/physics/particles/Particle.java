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
    private Vector3 force;

    // remove energy (possibly added through inacurracy in integration)
    private double damping;

    // can be used to count-down life time
    private double lifeTime;
    private ParticleRule rule = null;

    private final int type;

    public Particle(double mass) {
        this(0, mass);
    }

    public Particle(int type, double mass){
        this.type = type;
        setMass(mass);
        position = new Vector3(0.0, 0.0, 0.0);
        velocity = new Vector3(0.0, 0.0, 0.0);
        acceleration = new Vector3(0.0, 0.0, 0.0);
        force = new Vector3(0.0, 0.0, 0.0);

        damping = 1.0;
        lifeTime = 0.0;
    }

    public void setMass(double mass){
        if(Double.isInfinite(mass)){
            inverseMass = 0.0;
        }
        else{
            inverseMass = 1.0 / mass;
        }
    }

    public int getType() {
        return type;
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

    public void setForce(double x, double y, double z){
        force.set(x, y, z);
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

    public double kineticEnergy(){
        return 1.0 / (2.0 * inverseMass) * velocity.magnitude2();
    }

    public void setRule(ParticleRule rule) {
        this.rule = rule;
    }

    public void triggerRules(ParticleSimulator particleSimulator) {
        if(rule!=null){
            rule.trigger(this, particleSimulator);
        }
    }

    public void updateLifeTime(double elapsedTime){
        lifeTime -= elapsedTime;
    }

    public double getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(double lifeTime) {
        this.lifeTime = lifeTime;
    }

    public void setPosition(Vector3 position) {
        // need to make a copy
        setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setVelocity(Vector3 velocity) {
        // need to make a copy
        setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    public void clearForce() {
        force.set(0.0, 0.0, 0.0);
    }

    public Vector3 getForce() {
        return force;
    }

    public void addForce(Vector3 f){
        force.add(f);
    }
}
