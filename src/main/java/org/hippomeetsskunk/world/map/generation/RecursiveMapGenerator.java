package org.hippomeetsskunk.world.map.generation;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.util.Pair;
import org.hippomeetsskunk.knowledge.Fact;
import org.hippomeetsskunk.knowledge.KnowledgeBase;
import org.hippomeetsskunk.knowledge.facts.world.ContinentFact;
import org.hippomeetsskunk.knowledge.facts.world.WorldFact;
import org.hippomeetsskunk.world.map.TerraformableTerrain;
import org.hippomeetsskunk.world.map.Terrain;
import org.hippomeetsskunk.world.map.TerrainType;
import org.hippomeetsskunk.world.map.WorldMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/17/2015.
 */
public class RecursiveMapGenerator {
    private final static Logger logger = LoggerFactory.getLogger(RecursiveMapGenerator.class);

    private final static double PI_HALF = 0.5 * Math.PI;

    public void generate(WorldMap map, KnowledgeBase knowledgeBase) throws IOException {
        NameGenerator continentNameGenerator = new NameGenerator("elvish.ngn");
        WorldFact world = (WorldFact) knowledgeBase.getFact("World");

        // seed of continent
        UniformIntegerDistribution xDist = new UniformIntegerDistribution(1, map.getMaxX() - 2);
        UniformIntegerDistribution yDist = new UniformIntegerDistribution(1, map.getMaxY() - 2);
        UniformIntegerDistribution nameSyllabs = new UniformIntegerDistribution(2, 6);

        GammaDistribution landSeaRatioDist = new GammaDistribution(30.0, 0.01);
        GammaDistribution lambdaDDist = new GammaDistribution(10.0, 0.2);
        GammaDistribution elongationDist = new GammaDistribution(5.0, 0.6);

        double tot = map.getMaxX() * map.getMaxY();
        long land = 0;
        double landSeaRatio = landSeaRatioDist.sample(); // 0.3;

        double distanceScale = Math.sqrt(tot);

        logger.debug("Land / Sea ratio = " + landSeaRatio);
        logger.debug("Distance scale   = " + distanceScale);

        while(land / tot < landSeaRatio){
            Stack<Pair<Integer, Integer>> stack = new Stack();
            Pair<Integer, Integer> root = new Pair(xDist.sample(), yDist.sample());

            // check if terrain is empty (SEA)
            if(!TerrainType.SEA.equals(map.get(root.getFirst(), root.getSecond()).getTerrainType())) continue;

            // we have a continent!
            ContinentFact continent = new ContinentFact(continentNameGenerator.compose(nameSyllabs.sample()), world);
            knowledgeBase.insert(continent);

            int continentSize = 0;
            double lambdaD = lambdaDDist.sample() / (10.0 * distanceScale); // 0.0004;
            double elongation = elongationDist.sample();
            double theta = 6.0;

            double angle0 = Math.random() * Math.PI;

            logger.debug("Adding continent at " + root.getFirst() + " | " + root.getSecond());
            logger.debug("alpha            = " + ((angle0 / Math.PI * 180.0)));
            logger.debug("lD               = " + lambdaD);
            logger.debug("elongation       = " + elongation);
            logger.debug("theta            = " + theta);

            stack.push(root);
            while(!stack.isEmpty()) {
                Pair<Integer, Integer> p = stack.pop();

                if(!isValid(map, p)) continue;
                if(!TerrainType.SEA.equals(map.get(p.getFirst(), p.getSecond()).getTerrainType())) continue;

                double distance = getDistance(root, p);

                // angle from 0 - PI/2 relative to main axis
                double relativeAngle = getRelativeAngle(root, p, distance, angle0);

                // now scale distance depending on angle to get different growths per direction
                double b = distance / elongation + distance * (elongation-1.0) / elongation
                        * (PI_HALF - relativeAngle) / PI_HALF;
                double effectiveDistance = distance * distance / b;

                // number of NON-SEA neighbours
                int landConnections = getNumberOfLandConnections(map, p);

                double prob = 1.0;
                if (distance > 2.0) { // make sure that at least a tiny spot of land is generated
                    prob =  Math.exp(-lambdaD * effectiveDistance * effectiveDistance);
                    prob += Math.pow(landConnections / theta, 1.75);
                }

                if (Math.random() < prob) {
                    TerraformableTerrain terraformable = map.getTerraformable(p.getFirst(), p.getSecond());
                    terraformable.setTerrain(TerrainType.PLAIN);
                    terraformable.setContinent(continent);
                    ++land;
                    ++continentSize;

                    Iterator<Pair<Integer, Integer>> a = new AroundPositionIterator(p);
                    while(a.hasNext()){
                        // trying to fill out near locations before growing off into the far distance, maybe?
                        stack.add(stack.size(), a.next());
                        // stack.push(a.next());
                    }
                }
            }
            continent.setSize(continentSize);
        }
        world.setLandSeaRatio(landSeaRatio);
    }

    private double getRelativeAngle(Pair<Integer, Integer> x, Pair<Integer, Integer> y, double hypothenuse, double angle0) {
        double angle = getAngle(x, y, hypothenuse);
        if (angle > Math.PI) {
            // grow symmetrically in both opposite directions
            angle -= Math.PI;
        }
        double dAngle = Math.abs(angle0 - angle);
        if(dAngle > 0.5 * Math.PI){
            dAngle = Math.PI - dAngle;
        }
        return dAngle;
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
