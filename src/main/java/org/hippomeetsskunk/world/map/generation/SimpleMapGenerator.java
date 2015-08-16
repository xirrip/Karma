package org.hippomeetsskunk.world.map.generation;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.util.Pair;
import org.hippomeetsskunk.world.map.TerrainType;
import org.hippomeetsskunk.world.map.WorldMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/16/2015.
 */
public class SimpleMapGenerator {
    private final static Logger logger = LoggerFactory.getLogger(SimpleMapGenerator.class);

    public void generate(WorldMap map){
        // seed of continent
        UniformIntegerDistribution xDist = new UniformIntegerDistribution(1, map.getMaxX()-2);
        UniformIntegerDistribution yDist = new UniformIntegerDistribution(1, map.getMaxY()-2);

        double tot = map.getMaxX() * map.getMaxY();
        long land = 0;

        double landSeaRatio = 0.4;
        int approximateNumberOfContinents = 5;
        int continentSize = (int) Math.round(tot * landSeaRatio / approximateNumberOfContinents);

        while(true){
            int x = xDist.sample();
            int y = yDist.sample();

            // check if terrain is empty (SEA)
            if(TerrainType.SEA.equals(map.get(x, y).getTerrainType())){
                logger.debug("-- adding continent at " + x + " / " + y);
                map.getTerraformable(x, y).setTerrain(TerrainType.PLAIN);
                ++land;

                int currentSize = 1;
                Pair<Integer, Integer> currentPos = new Pair(x, y-1);
                Pair<Integer, Integer> lastPos = new Pair(x+1, y-1); // just make up something to define the direction

                logger.debug("\ttrying to grow to " + continentSize);
                while(currentSize < continentSize){
                    // follow contour
                    Pair<Integer, Integer> newCurrentPos = getNextPosition(map, currentPos, lastPos);
                    if(newCurrentPos == null){
                        logger.debug("\tgot stuck, breaking.");
                        break; // no continuation found
                    }
                    lastPos = currentPos;
                    currentPos = newCurrentPos;

                    // evaluate neighbours

                    // evaluate direction

                    // calculate growth probability

                    // check if growing
                    if(true){
                        // grow
                        logger.debug("\tadding land at " + currentPos.getFirst() + " / " + currentPos.getSecond());
                        map.getTerraformable(currentPos.getFirst(), currentPos.getSecond()).setTerrain(TerrainType.PLAIN);
                        ++currentSize;
                        ++land;
                    }

                    // check if fusing with another continent -> break ?
                }
            }

            logger.debug("Added enough land. Stopping.");
            if(land / tot > landSeaRatio) break;
        }
    }

    private Pair<Integer, Integer> getNextPosition(WorldMap map, Pair<Integer, Integer> currentPosition, Pair<Integer, Integer> lastPosition) {
        // opposite direction from last pos
        Pair<Integer, Integer> dir = new Pair(currentPosition.getFirst() - lastPosition.getFirst(), currentPosition.getSecond() - lastPosition.getSecond());

        int count = 0;
        while(!checkFollowingContour(map, currentPosition, dir, lastPosition)){
            ++count;
            if(count>8) return null;
            dir = getNext(dir);
        }
        return new Pair(currentPosition.getFirst() + dir.getFirst(), currentPosition.getSecond() + dir.getSecond());
    }

    private boolean checkFollowingContour(WorldMap map, Pair<Integer, Integer> currentPosition, Pair<Integer, Integer> dir, Pair<Integer, Integer> lastPosition) {
        Pair<Integer, Integer> p = new Pair(currentPosition.getFirst() + dir.getFirst(), currentPosition.getSecond() + dir.getSecond());
        if(p.equals(lastPosition)) return false; // no going back

        if(p.getFirst() < 0 || p.getFirst() >= map.getMaxX()) return false;
        if(p.getSecond() < 0 || p.getSecond() >= map.getMaxY()) return false;

        Pair<Integer, Integer> d = new Pair(-dir.getFirst(), -dir.getSecond());
        for(int c=0; c<8; ++c){
            int nx = p.getFirst() + d.getFirst();
            int ny = p.getSecond() + d.getSecond();

            if(nx < 0 || nx >= map.getMaxX()){
                d = getNext(d);
                continue;
            }
            if(ny < 0 || ny >= map.getMaxY()){
                d = getNext(d);
                continue;
            }

            if(nx == currentPosition.getFirst() && ny == currentPosition.getSecond()){
                d = getNext(d);
                continue; // current added does not count
            }
            if(nx == lastPosition.getFirst() && ny == lastPosition.getSecond()){
                d = getNext(d);
                continue; // last added does not count
            }

            if(!TerrainType.SEA.equals(map.get(nx, ny).getTerrainType())){
                logger.debug("\tcheck following contour true because contour found at " +  nx + " / " + ny);
                return true;
            }
            d = getNext(d);
        }
        return false;
    }

    private Pair<Integer, Integer> getNext(Pair<Integer, Integer> dir) {
        int x = dir.getFirst();
        int y = dir.getSecond();

        if(y==-1){
            ++x;
            if(x==2){
                x=1;
                y=0;
            }
        }
        else if(y==0){
            if(x==-1){
                --y;
            }
            else{
                ++y;
            }
        }
        else{
            --x;
            if(x==-2){
                x=-1;
                y=0;
            }
        }
        return new Pair(x, y);
    }


    private TerrainType getRandomTerrainType() {
        int r = (int) Math.floor(5 * Math.random());
        if(r==0) return TerrainType.SEA;
        if(r==1) return TerrainType.PLAIN;
        if(r==2) return TerrainType.HILL;
        if(r==3) return TerrainType.MOUNTAIN;
        if(r==4) return TerrainType.RIVER_LAKE;
        throw new IllegalArgumentException("Wrong random number - unknown terrain type.");
    }


}
