package org.hippomeetsskunk.world.map.generation;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.util.Pair;
import org.hippomeetsskunk.knowledge.*;
import org.hippomeetsskunk.knowledge.facts.world.ClimateZoneFact;
import org.hippomeetsskunk.knowledge.facts.world.RegionFact;
import org.hippomeetsskunk.knowledge.facts.world.WorldFact;
import org.hippomeetsskunk.world.map.TerraformableTerrain;
import org.hippomeetsskunk.world.map.Terrain;
import org.hippomeetsskunk.world.map.TerrainType;
import org.hippomeetsskunk.world.map.WorldMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/19/2015.
 */
public class RegionGenerator {
    private final static Logger logger = LoggerFactory.getLogger(RegionGenerator.class);

    private static final int DX = 30;
    private static final int DY = 30;
    private static final int MIN_CLUSTER_SIZE = 2000;

    static private class RegionCluster{
        public RegionFact region;
        public List<Pair<Integer, Integer>> area = new ArrayList<>();
        public Vector<Pair<Integer, Integer>> stack = new Vector<>();
        public int size = 0;
    }

    public void generate(WorldMap map, KnowledgeBase knowledgeBase) throws IOException {
        NameGenerator regionNameGenerator = new NameGenerator("elvish.ngn");
        UniformIntegerDistribution nameSyllabs = new UniformIntegerDistribution(2, 7);

        // generate set of basis regions for clustering
        List<RegionCluster> clusters = createInitialClusters(map);
        logger.debug("Added " + clusters.size() + " initial region clusters.");

        // growByOne to max
        boolean growing = false;
        int min, max;
        do{
            min = Integer.MAX_VALUE;
            max = 0;
            growing = false;
            for(RegionCluster c : clusters){
                boolean grownCurrent = growByOne(c, map);
                growing = growing || grownCurrent;
                min = Math.min(min, c.size);
                max = Math.max(max, c.size);
            }
        } while(growing);

        for(int x=0; x<map.getMaxX(); ++x){
            for(int y=0; y<map.getMaxY(); ++y){
                Terrain t = map.get(x, y);
                if(!TerrainType.SEA.equals(t.getTerrainType()) && t.getRegion() == null){
                    // found an island
                    RegionCluster c = createCluster(x, y);
                    clusters.add(c);
                    while(true){ // grow to max
                        if(!growByOne(c, map)) break;
                    }
                }
            }
        }

        logger.debug("Cluster size after growing from " + min + " to " + max);

        clusters.sort(new Comparator<RegionCluster>() {
            @Override
            public int compare(RegionCluster o1, RegionCluster o2) {
                return Integer.compare(o1.size, o2.size);
            }
        });

        // merge clusters
        boolean merging = false;
        int numClusters;
        do{
            min = Integer.MAX_VALUE;
            max = 0;
            numClusters = 0;
            merging = false;
            Set<RegionCluster> mergedThisRound = new HashSet<>();

            for(RegionCluster c : clusters){
                if(mergedThisRound.contains(c)) continue; // avoid linear growth (maybe)

                // merged clusters will have size 0
                if(c.size > 0 && c.size < MIN_CLUSTER_SIZE){
                    RegionCluster neighbour = findNeighbour(c, clusters, map);
                    if(neighbour != null){
                        mergeClusters(c, neighbour, map);
                        mergedThisRound.add(neighbour);
                        merging = true;
                    }
                }
                if(c.size > 0){
                    min = Math.min(min, c.size);
                    max = Math.max(max, c.size);
                    ++numClusters;
                }
            }

        } while(merging);
        logger.debug("Cluster size after growing from " + min + " to " + max + " for " + numClusters + " clusters.");

        // correctly name and attach regions

        // mountain probability
        GammaDistribution mountainProbDist = new GammaDistribution(4.0, 0.4); // divide by 10
        GammaDistribution hillDist = new GammaDistribution(45.0, 0.1); // divide by 10, prob that a mountin is rather a hill

        int countMountain = 0;
        int countHill = 0;

        WorldFact worldFact = (WorldFact) knowledgeBase.getFactCaseInsensitive("World");
        for(RegionCluster c : clusters){
            if(c.size > 0){
                Pair<Integer, Integer> aPosition = c.area.iterator().next();
                Terrain terrain = map.get(aPosition.getFirst(), aPosition.getSecond());
                String name = regionNameGenerator.compose(nameSyllabs.sample());
                RegionFact realRegion = new RegionFact(name, terrain.getContinent());
                realRegion.setSize(c.size);

                knowledgeBase.insert(realRegion);

                int y=0;
                for(Pair<Integer, Integer> p : c.area){
                    TerraformableTerrain t = map.getTerraformable(p.getFirst(), p.getSecond());
                    t.setRegion(realRegion);
                    // merge continent as well
                    // TODO possibly we have unreferenced continents left-over
                    t.setContinent(terrain.getContinent());
                    y += p.getSecond();
                }
                int center = y / c.area.size();
                Optional<Fact> climateZone = worldFact.getConnectedFactsOfType(FactType.CLIMATE_ZONE).stream()
                        .filter(f -> ((ClimateZoneFact) f).isWithinZone(center))
                        .findFirst();
                if(climateZone.isPresent()){
                    realRegion.addConnection(new ConnectionImpl(ConnectionType.IS_DESCRIBED_BY, climateZone.get()));

                    // generate hills / mountains
                    double mBaseProb = mountainProbDist.sample() / 10.0;
                    double mBorderF = 2.5;
                    double mGroupingF = 2.5;

                    logger.debug("Region " + realRegion.getFactId() + " has mountain base prob of " + mBaseProb);

                    // first generate mountains / hills
                    for(Pair<Integer, Integer> p : c.area){

                        int numM = getMountainCount(p, map);
                        int numB = getBorderCount(p, map);
                        int numS = getSeaBorderCount(p, map);

                        TerraformableTerrain t = map.getTerraformable(p.getFirst(), p.getSecond());
                        double mProb = mBaseProb * Math.pow(numB / 8.0 + 7.0 / 8.0, mBorderF) * Math.pow(numM / 8.0 + 7.0 / 8.0, mGroupingF);
                        if(Math.random() < mProb){
                            double hProb = hillDist.sample() / 10.0 + 0.2; // increase chances of hill only
                            if(Math.random() < hProb){
                                // TODO possibly add hill name
                                // store as alias in knowledgeBase
                                t.setTerrain(TerrainType.HILL, numS > 0);
                                ++countHill;
                            }
                            else{
                                // TODO possibly add mountain name
                                // store as alias in knowledgeBase
                                t.setTerrain(TerrainType.MOUNTAIN, numS > 0);
                                ++countMountain;
                            }
                        }
                        else t.setTerrain(t.getTerrainType(), numS > 0);
                        // maybe add lake / rivers as well.
                        // would need to be strongly grouped / grow towards sea with strong probability to start from mountain / hill
                    }

                    generateClimaForRegion(realRegion, c, (ClimateZoneFact) climateZone.get(), map);
                }
                else throw new IllegalArgumentException("No climate zone found for latitude " + y + ".");

            }
        }
        double mountainRatio = countMountain / ((double) (map.getMaxX() * map.getMaxY()));
        double hillRatio = countHill / ((double) (map.getMaxX() * map.getMaxY()));
        logger.debug("Added mountains: " + countMountain + " (ratio " + mountainRatio + ").");
        logger.debug("Added hills    : " + countHill + " (ratio " + hillRatio + ").");
    }

    private int getSeaBorderCount(Pair<Integer, Integer> pos, WorldMap map) {
        AroundPositionIterator it = new AroundPositionIterator(pos);
        int r=0;
        while(it.hasNext()){
            Pair<Integer, Integer> y = it.next();
            if(!isValid(y, map)) continue;

            Terrain terrain = map.get(y.getFirst(), y.getSecond());
            if(TerrainType.SEA.equals(terrain.getTerrainType())) ++r;
        }
        return r;
    }

    private int getBorderCount(Pair<Integer, Integer> pos, WorldMap map) {
        RegionFact region = map.get(pos.getFirst(), pos.getSecond()).getRegion();
        AroundPositionIterator it = new AroundPositionIterator(pos);
        int r=0;
        while(it.hasNext()){
            Pair<Integer, Integer> y = it.next();
            if(!isValid(y, map)) continue;

            Terrain terrain = map.get(y.getFirst(), y.getSecond());
            if(terrain.getRegion() != region) ++r;
        }
        return r;
    }

    private int getMountainCount(Pair<Integer, Integer> pos, WorldMap map) {
        AroundPositionIterator it = new AroundPositionIterator(pos);
        int r=0;
        while(it.hasNext()){
            Pair<Integer, Integer> y = it.next();
            if(!isValid(y, map)) continue;

            Terrain terrain = map.get(y.getFirst(), y.getSecond());
            if(TerrainType.MOUNTAIN.equals(terrain.getTerrainType())) ++r;
            if(TerrainType.HILL.equals(terrain.getTerrainType())) ++r;

        }
        return r;
    }

    private void generateClimaForRegion(RegionFact region, RegionCluster cluster, ClimateZoneFact climateZone, WorldMap map) {

        int cHills=0, cMountains=0, cPlain=0, cSeaBorder=0;
        for(Pair<Integer, Integer> r : cluster.area){
            Terrain terrain = map.get(r.getFirst(), r.getSecond());
            if(terrain.hasSeaBorder()) ++cSeaBorder;

            switch(terrain.getTerrainType()){
                case MOUNTAIN: ++cMountains; break;
                case SEA: break;
                case HILL: ++cHills; break;
                case PLAIN: ++cPlain; break;
                case RIVER_LAKE: break;
            }
        }

        // maritime? (count sea border)
        boolean maritime = (Math.sqrt(cluster.area.size()) <= cSeaBorder);
        region.setMaritimeClima(maritime);
        logger.debug(region.getFactId() + " -- maritime: area=" + cluster.area.size() + " , coast=" + cSeaBorder);

        // height? (count hills / mountains / plain / sea)
        GammaDistribution mountainHeightDist = new GammaDistribution(40.0, 70.0);
        double minHillHeight = 750.0;
        double heightFactor = 1.3 * (1.0 + (1.5 * cHills + 2.5 * cMountains - 2.0 * cSeaBorder) / ((double) cluster.area.size()));

        GammaDistribution heightDist = new GammaDistribution(50.0, 5.0);
        double min=Double.POSITIVE_INFINITY, max=0;

        for(Pair<Integer, Integer> r : cluster.area){
            TerraformableTerrain terrain = map.getTerraformable(r.getFirst(), r.getSecond());
            double basisHeight = heightDist.sample() * heightFactor;
            double height = 0.0;
            switch(terrain.getTerrainType()){
                case MOUNTAIN: height = heightFactor * basisHeight + 2000.0 + mountainHeightDist.sample(); break;
                case SEA: height = 0.0; break;
                case HILL: height = heightFactor * basisHeight + minHillHeight; break;
                case PLAIN: height = heightFactor * basisHeight; break;
                case RIVER_LAKE: height = heightFactor * basisHeight; break;
            }
            terrain.setHeight(height);
            min = Math.min(min, height);
            max = Math.max(max, height);

            // TODO name heighest mountains and make available in region info?
            // TODO height average excluding mountains

        }

        logger.debug("Height min = " + min + " max = " + max);

        // TODO climate

        // https://en.wikipedia.org/wiki/K%C3%B6ppen_climate_classification
        // http://www.climate-zone.com/continent/europe/
    }

    private void mergeClusters(RegionCluster c1, RegionCluster c2, WorldMap map) {
        for(Pair<Integer, Integer> p : c1.area){
            TerraformableTerrain terrain = map.getTerraformable(p.getFirst(), p.getSecond());
            terrain.setRegion(c2.region);
            c2.area.add(p);
            ++c2.size;
        }
        c1.area = null;
        c1.size = 0;
        c1.region = null;
        c1.stack = null;
    }

    private RegionCluster findNeighbour(RegionCluster c, List<RegionCluster> clusters, WorldMap map) {
        RegionCluster minSizeNeighbour = null;

        for(Pair<Integer, Integer> pos : c.area){
            AroundPositionIterator it = new AroundPositionIterator(pos);
            while(it.hasNext()){
                Pair<Integer, Integer> n = it.next();
                if(!isValid(n, map)) continue;

                Terrain terrain = map.get(n.getFirst(), n.getSecond());
                if(c.region != terrain.getRegion()){
                    Optional<RegionCluster> clusterForRegion = getClusterForRegion(terrain.getRegion(), clusters);
                    if(clusterForRegion.isPresent() && clusterForRegion.get().size > 0){
                        if(minSizeNeighbour == null || minSizeNeighbour.size > clusterForRegion.get().size){
                            // return clusterForRegion;
                            minSizeNeighbour = clusterForRegion.get();
                        }
                    }
                }
            }
        }
        return minSizeNeighbour;
    }

    private Optional<RegionCluster> getClusterForRegion(RegionFact region, List<RegionCluster> clusters) {
        return clusters.stream().filter(c -> c.region == region).findFirst();
    }

    private boolean growByOne(RegionCluster c, WorldMap map) {
        while(!c.stack.isEmpty()){
            // lets try to use random element
            UniformIntegerDistribution indexDist = new UniformIntegerDistribution(0, c.stack.size()-1);
            int index = indexDist.sample();

            Pair<Integer, Integer> p = c.stack.get(index);
            c.stack.remove(index);

            if(!isValid(p, map)) continue;
            Terrain terrain = map.get(p.getFirst(), p.getSecond());
            if(TerrainType.SEA.equals(terrain.getTerrainType())) continue;
            if(terrain.getRegion() != null) continue;

            // ok, lets grow
            TerraformableTerrain terraformable = map.getTerraformable(p.getFirst(), p.getSecond());
            terraformable.setRegion(c.region);

            c.area.add(p);
            ++c.size;

            AroundPositionIterator it = new AroundPositionIterator(p);
            while(it.hasNext()){
                Pair<Integer, Integer> pos = it.next();
                if(!isValid(pos, map)) continue;

                Terrain t = map.get(pos.getFirst(), pos.getSecond());
                if(TerrainType.SEA.equals(t.getTerrainType())) continue;

                if(t.getRegion() != null) continue;

                c.stack.add(c.stack.size(), pos);
            }
            // return after 1 added
            return true;
        }
        return false;
    }

    private List<RegionCluster> createInitialClusters(WorldMap map) {
        List<RegionCluster> clusters = new ArrayList<>();
        for(int x=DX/2; x<map.getMaxX(); x+=DX){
            for(int y=DY/2; y<map.getMaxY(); y+=DY){
                if(!TerrainType.SEA.equals(map.get(x, y).getTerrainType())){
                    RegionCluster c = createCluster(x, y);
                    clusters.add(c);

                    boolean grown = growByOne(c, map);
                    if(grown == false) throw new IllegalArgumentException("Must be able to grow the initial cluster!");
                }
            }
        }
        return clusters;
    }

    private RegionCluster createCluster(int x, int y) {
        RegionCluster c = new RegionCluster();
        c.region = new RegionFact(x + "-" + y, null); // no association with continent yet, only after consolidation
        Pair<Integer, Integer> point = new Pair<>(x, y);
        c.stack.add(point);
        return c;
    }

    private boolean isValid(Pair<Integer, Integer> x, WorldMap map) {
        if(x.getFirst() < 0 || x.getFirst() >= map.getMaxX()) return false;
        if(x.getSecond() < 0 || x.getSecond() >= map.getMaxY()) return false;
        return true;
    }
}
