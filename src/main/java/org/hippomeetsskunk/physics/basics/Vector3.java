package org.hippomeetsskunk.physics.basics;

/**
 * Created by SRZCHX on 26.09.2016.
 */
public class Vector3 {
    // or array?
    // final?
    private double x;
    private double y;
    private double z;

    public Vector3(){
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void invert(){
        x = -x;
        y = -y;
        z = -z;
    }

    public void add(Vector3 a){
        x += a.x;
        y += a.y;
        z += a.z;
    }

    public void subtract(Vector3 a){
        x -= a.x;
        y -= a.y;
        z -= a.z;
    }

    public void scale(double s){
        x *= s;
        y *= s;
        z *= s;
    }

    public void scale(double sx, double sy, double sz){
        x *= sx;
        y *= sy;
        z *= sz;
    }

    public double dot(Vector3 v){
        return x*v.x + y*v.y + z*v.z;
    }

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public double length2(){
        return x*x + y*y + z*z;
    }

    public double magnitude(){ return length();}
    public double magnitude2(){ return length2();}

    public void normalize(){
        double l = length();
        scale(1.0 / l);
    }

    public void addScaled(Vector3 v, double s){
        x += s * v.x;
        y += s * v.y;
        z += s * v.z;
    }

    public void componentProduct(Vector3 v){
        x *= v.x;
        y *= v.y;
        z *= v.z;
    }

    public void cross(Vector3 v){
        double xx = y*v.z - z*v.y;
        double yy = z*v.x-x*v.z;
        double zz = x*v.y-z*v.x;
        x = xx;
        y = yy;
        z = zz;
    }

    static public Vector3 add(Vector3 a, Vector3 b){
        return new Vector3(a.x+b.x, a.y+b.y, a.z+b.z);
    }

    static public Vector3 subtract(Vector3 a, Vector3 b){
        return new Vector3(a.x-b.x, a.y-b.y, a.z-b.z);
    }

    static public Vector3 scale(Vector3 a, double s){
        return new Vector3(a.x * s, a.y*s, a.z*s);
    }

    static public double dot(Vector3 a, Vector3 b){
        return a.x*b.x +a.y*b.y + a.z*b.z;
    }

    static public Vector3 addScaled(Vector3 a, Vector3 b, double s){
        return new Vector3(a.x+s*b.x, a.y+s*b.y, a.z+s*b.z);
    }

    static public Vector3 compponentProduct(Vector3 a, Vector3 b){
        return new Vector3(a.x*b.x, a.y*b.y, a.z*b.z);
    }

    static public double angleOfNormalised(Vector3 a, Vector3 b){
        return Math.acos(dot(a, b));
    }

    static public double angle(Vector3 a, Vector3 b){
        return Math.acos(dot(a, b) / a.length() / b.length());
    }

    static public Vector3 cross(Vector3 a, Vector3 b){
        return new Vector3(a.y*b.z - a.z*b.y, a.z*b.x-a.x*b.z, a.x*b.y-a.y*b.x);
    }

    public void set(Vector3 v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }
}
