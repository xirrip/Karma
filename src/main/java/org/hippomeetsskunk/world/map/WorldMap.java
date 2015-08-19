package org.hippomeetsskunk.world.map;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
public interface WorldMap {
    int getMaxX();
    int getMaxY();

    Terrain get(int x, int y);
    TerraformableTerrain getTerraformable(int x, int y);
}
