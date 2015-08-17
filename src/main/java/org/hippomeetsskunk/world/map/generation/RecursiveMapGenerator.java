package org.hippomeetsskunk.world.map.generation;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.util.Pair;
import org.hippomeetsskunk.world.map.Terrain;
import org.hippomeetsskunk.world.map.TerrainType;
import org.hippomeetsskunk.world.map.WorldMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/17/2015.
 */
public class RecursiveMapGenerator {
    private final static Logger logger = LoggerFactory.getLogger(RecursiveMapGenerator.class);

    public void generate(WorldMap map) {
        // seed of continent
        UniformIntegerDistribution xDist = new UniformIntegerDistribution(1, map.getMaxX() - 2);
        UniformIntegerDistribution yDist = new UniformIntegerDistribution(1, map.getMaxY() - 2);

        double tot = map.getMaxX() * map.getMaxY();
        long land = 0;
        double landSeaRatio = 0.3;

        double lambdaR = 0.9;
        double lambdaD = 0.0004;

        while(land / tot < landSeaRatio){
            Stack<Pair<Integer, Integer>> stack = new Stack();
            Pair<Integer, Integer> root = new Pair(xDist.sample(), yDist.sample());

            // check if terrain is empty (SEA)
            if(TerrainType.SEA.equals(map.get(root.getFirst(), root.getSecond()).getTerrainType())){

                double angle0 = Math.random() * Math.PI;

                logger.debug("Adding continent at " + root.getFirst() + " | " + root.getSecond());
                logger.debug("In direction " + ((angle0 / Math.PI * 180.0)));

                stack.push(root);
                while(!stack.isEmpty()) {
                    Pair<Integer, Integer> p = stack.pop();

                    if(!isValid(map, p)) continue;
                    if(!TerrainType.SEA.equals(map.get(p.getFirst(), p.getSecond()).getTerrainType())) continue;

                    double distance = getDistance(root, p);
                    double angle = getAngle(root, p, distance);
                    if (angle > Math.PI) {
                        // grow symmetrically in both opposite directions
                        angle -= Math.PI;
                    }

                    int landConnections = getNumberOfLandConnections(map, p);

                    double prob = 1.0;
                    if (distance > 2.0) {
                        prob = Math.exp(-lambdaD * distance * distance) *
                                Math.exp(-lambdaR * Math.pow(angle - angle0, 2)) +
                                landConnections / 10.0;
                    }

                    if (Math.random() < prob) {
                        map.getTerraformable(p.getFirst(), p.getSecond()).setTerrain(TerrainType.PLAIN);
                        ++land;

                        if(land / tot > landSeaRatio) break; // emergency exit

                        Iterator<Pair<Integer, Integer>> a = new AroundPositionIterator(p);
                        while(a.hasNext()){
                            stack.push(a.next());
                        }
                    }
                }
            }

        }
    }

    private int getNumberOfLandConnections(WorldMap map, Pair<Integer, Integer> p) {
        int c=0;
        Iterator<Pair<Integer, Integer>> it = new AroundPositionIterator(p);
        while(it.hasNext()){
            Pair<Integer, Integer> n = it.next();
            if(!isValid(map, n)) continue;
            Terrain t = map.get(n.getFirst(), n.getSecond());
            if(!(TerrainType.SEA.equals(t.getTerrainType()))){
                ++c;
            }
        }
        return c;
    }

    private boolean isValid(WorldMap map, Pair<Integer, Integer> x) {
        if(x.getFirst() < 0 || x.getFirst() >= map.getMaxX())  return false;
        if(x.getSecond() < 0 || x.getSecond() >= map.getMaxY()) return false;
        return true;
    }

    private double getAngle(Pair<Integer, Integer> x, Pair<Integer, Integer> y, double hypothenuse) {
        // first quadrant
        double dx = y.getFirst() - x.getFirst();
        double dy = y.getSecond() - x.getSecond();
        // from -pi/2 to pi/2
        double alpha = Math.asin(dy / hypothenuse);
        if(dx < 0){
            alpha = Math.PI - alpha;
        }
        if(alpha < 0){
            alpha += 2.0 * Math.PI;
        }
        return alpha;
    }

    private double getDistance(Pair<Integer, Integer> x, Pair<Integer, Integer> y) {
        return Math.sqrt(Math.pow(x.getFirst()-y.getFirst(), 2) + Math.pow(x.getSecond()-y.getSecond(), 2));
    }
}
