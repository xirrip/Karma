package org.hippomeetsskunk.knowledge.facts.world;

import org.hippomeetsskunk.knowledge.FactImpl;
import org.hippomeetsskunk.knowledge.FactType;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/18/2015.
 */
public class WorldFact extends FactImpl {

    private double landSeaRatio;

    public WorldFact(){
        super("World", new FactType[] { FactType.WORLD });
    }

    public void setLandSeaRatio(double landSeaRatio) {
        this.landSeaRatio = landSeaRatio;
    }

    public double getLandSeaRatio() {
        return landSeaRatio;
    }
}
