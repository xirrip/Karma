package org.hippomeetsskunk.world.map;

import org.hippomeetsskunk.knowledge.facts.world.ContinentFact;
import org.hippomeetsskunk.knowledge.facts.world.RegionFact;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/16/2015.
 */
public interface TerraformableTerrain extends Terrain {
    void setTerrain(TerrainType terrainType, boolean hasSeaBorder);
    void setContinent(ContinentFact continent);
    void setRegion(RegionFact region);
}
