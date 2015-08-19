package org.hippomeetsskunk.world.map.generation;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.util.Pair;
import org.hippomeetsskunk.knowledge.KnowledgeBase;
import org.hippomeetsskunk.knowledge.facts.world.RegionFact;
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
        for(RegionCluster c : clusters){
            if(c.size > 0){
                RegionFact region = c.region;
                Pair<Integer, Integer> aPosition = c.area.iterator().next();
                Terrain terrain = map.get(aPosition.getFirst(), aPosition.getSecond());
                String name = regionNameGenerator.compose(nameSyllabs.sample());
                RegionFact realRegion = new RegionFact(name, terrain.getContinent());
                realRegion.setSize(c.size);

                knowledgeBase.insert(realRegion);

                for(Pair<Integer, Integer> p : c.area){
                    TerraformableTerrain t = map.getTerraformable(p.getFirst(), p.getSecond());
                    t.setRegion(realRegion);
                }
            }
        }
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
