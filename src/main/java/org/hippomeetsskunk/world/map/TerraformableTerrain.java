package org.hippomeetsskunk.world.map;

import org.hippomeetsskunk.knowledge.facts.world.ContinentFact;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/16/2015.
 */
public interface TerraformableTerrain extends Terrain {
    void setTerrain(TerrainType terrainType);
    void setContinent(ContinentFact continent);
}
