package org.hippomeetsskunk.world.map;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
public class SimpleWorldMap implements WorldMap {

    // to do injection
    private final int MAX_X = 1000;
    private final int MAX_Y = 1000;

    private final Terrain[][] terrain;

    public SimpleWorldMap() {
        this.terrain = new Terrain[MAX_X][MAX_Y];
        initialize();
    }

    // do this differently (from outside, DI?)
    private void initialize(){
        for(int x=0; x<MAX_X; ++x){
            for(int y=0; y<MAX_Y; ++y){
                terrain[x][y] = new TerrainImpl(TerrainType.SEA);
            }
        }
    }

    public int getMaxX() {
        return MAX_X;
    }

    public int getMaxY() {
        return MAX_Y;
    }

    public Terrain get(int x, int y) {
        return terrain[x][y];
    }

    public TerraformableTerrain getTerraformable(int x, int y) {
        Terrain t = get(x, y);
        if(t instanceof TerraformableTerrain) return (TerraformableTerrain) t;
        throw new IllegalArgumentException("Terrain is not terraformable.");
    }
}
