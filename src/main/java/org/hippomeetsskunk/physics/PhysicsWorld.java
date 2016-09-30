package org.hippomeetsskunk.physics;

/**
 * Created by SRZCHX on 27.09.2016.
 */
public class PhysicsWorld {
    static final public double G = 6.67408e-11 ;
    static final public double mEarth = 5.972e24;
    static final public double rEarth = 6.371e6;
    static final public double dEarthSun = 149.6e9;

    static final public double gravitationalAccelerationOnEarth = G * mEarth / (rEarth * rEarth);

    static final public double secondsPerYear = 365.2425 * 24 * 60 * 60;
}
