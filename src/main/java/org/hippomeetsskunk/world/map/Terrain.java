package org.hippomeetsskunk.world.map;

import org.hippomeetsskunk.knowledge.facts.world.ContinentFact;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
public interface Terrain {
    TerrainType getTerrainType();
    ContinentFact getContinent();
}
