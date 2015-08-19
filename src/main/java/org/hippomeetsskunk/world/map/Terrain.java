package org.hippomeetsskunk.world.map;

import org.hippomeetsskunk.knowledge.facts.world.ContinentFact;
import org.hippomeetsskunk.knowledge.facts.world.RegionFact;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
public interface Terrain {
    TerrainType getTerrainType();
    ContinentFact getContinent();
    RegionFact getRegion();
}
