package org.hippomeetsskunk.world.map;

import org.hippomeetsskunk.knowledge.facts.world.ContinentFact;
import org.hippomeetsskunk.knowledge.facts.world.RegionFact;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
public class TerrainImpl implements TerraformableTerrain {
    private TerrainType terrainType;
    private ContinentFact continent;
    private RegionFact region;

    public TerrainImpl(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrain(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    @Override
    public ContinentFact getContinent() {
        return continent;
    }

    @Override
    public void setContinent(ContinentFact continent) {
        this.continent = continent;
    }

    @Override
    public RegionFact getRegion() {
        return region;
    }

    @Override
    public void setRegion(RegionFact region) {
        this.region = region;
    }
}
