package org.hippomeetsskunk.world.map.generation;

import org.apache.commons.math3.util.Pair;

import java.util.Iterator;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/16/2015.
 */
public class AroundPositionIterator implements Iterator<Pair<Integer, Integer>> {
    private final Pair<Integer, Integer> center;
    private final int startDx;
    private final int startDy;
    private final boolean clockwise;

    private int x;
    private int y;

    private int count = 0;

    public AroundPositionIterator(Pair<Integer, Integer> center) {
        this(center, -1, -1, true);
    }

    public AroundPositionIterator(Pair<Integer, Integer> center, int startDx, int startDy, boolean clockwise) {
        this.center = center;
        this.startDx = startDx;
        this.startDy = startDy;
        this.clockwise = clockwise;

        this.x = startDx;
        this.y = startDy;
    }


    public boolean hasNext() {
        return count < 8;
    }

    public Pair<Integer, Integer> next() {

        Pair pair = new Pair(center.getFirst() + x, center.getSecond() + y);
        internalNext();
        return pair;
    }

    private void internalNext() {
        if(y<0){
            if(clockwise) ++x;
            else --x;
        }
        else if(y==0){
            if(x<0){
                if(clockwise) --y;
                else ++y;
            }
            else{
                if(clockwise) ++y;
                else --y;
            }
        }
        else if(y>0){
            if(clockwise) --x;
            else ++x;
        }
        if(x<-1){
            x = -1;
            y = 0;
        }
        if(x>1){
            x = 1;
            y = 0;
        }
        ++count;
    }
}
