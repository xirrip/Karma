package org.hippomeetsskunk.world.map;

import org.hippomeetsskunk.knowledge.Fact;
import org.hippomeetsskunk.knowledge.facts.world.ContinentFact;
import org.hippomeetsskunk.knowledge.facts.world.RegionFact;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 * Each terrain should be an accessible fact as well
 */
public interface Terrain extends Fact {
    TerrainType getTerrainType();
    ContinentFact getContinent();
    RegionFact getRegion();
}
