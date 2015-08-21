package org.hippomeetsskunk.knowledge.facts.world;

import org.hippomeetsskunk.knowledge.FactImpl;
import org.hippomeetsskunk.knowledge.FactType;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/18/2015.
 */
public class WorldFact extends FactImpl {

    private double landSeaRatio;
    private final int area;
    private final int maxX;
    private final int maxY;

    public WorldFact(int maxX, int maxY){
        super("World", new FactType[] { FactType.WORLD });
        this.maxX = maxX;
        this.maxY = maxY;
        this.area = maxX * maxY;
    }

    public void setLandSeaRatio(double landSeaRatio) {
        this.landSeaRatio = landSeaRatio;
    }

    public double getLandSeaRatio() {
        return landSeaRatio;
    }

    public int getArea() {
        return area;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }
}
