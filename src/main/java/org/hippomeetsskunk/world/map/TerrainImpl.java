package org.hippomeetsskunk.world.map;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
public class TerrainImpl implements TerraformableTerrain {
    private TerrainType terrainType;

    public TerrainImpl(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(TerrainType type){
        this.terrainType = type;
    }

    public void setTerrain(TerrainType terrainType) {
        this.terrainType = terrainType;
    }
}
