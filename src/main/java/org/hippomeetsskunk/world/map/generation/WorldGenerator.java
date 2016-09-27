package org.hippomeetsskunk.world.map.generation;

import org.hippomeetsskunk.knowledge.KnowledgeBase;
import org.hippomeetsskunk.knowledge.facts.world.ClimateZoneFact;
import org.hippomeetsskunk.knowledge.facts.world.WorldFact;
import org.hippomeetsskunk.world.World;
import org.hippomeetsskunk.world.map.WorldMap;

import java.io.IOException;

/**
 * Created by srzchx on 21.08.2015.
 */
public class WorldGenerator {
    // TODO inject
    private final RecursiveMapGenerator mapGenerator = new RecursiveMapGenerator();
    private final RegionGenerator regionGenerator = new RegionGenerator();


    public void generate(World world) throws IOException {

        KnowledgeBase knowledgeBase = world.getKnowledgeBase();
        WorldMap map = world.getMap();

        System.out.println("Building base knowledge...");
        WorldFact worldFact = new WorldFact(map.getMaxX(), map.getMaxY());
        knowledgeBase.insert(worldFact);

        addGlobalClimateZones(knowledgeBase, worldFact, map);

        // TODO add type based fact in an intelligent way
        // factFactory.insertFact(FactType.APPLE, knowledgeBase);

        System.out.println("Generating world...");
        RecursiveMapGenerator mapGenerator = new RecursiveMapGenerator();
        mapGenerator.generate(map, knowledgeBase);

        RegionGenerator regionGenerator = new RegionGenerator();
        regionGenerator.generate(map, knowledgeBase);

    }

    private void addGlobalClimateZones(KnowledgeBase knowledgeBase, WorldFact worldFact, WorldMap map) {
        double slice = worldFact.getMaxY() / 12.0;
        ClimateZoneFact northPolar = new ClimateZoneFact("Polar (N)", ClimateZoneFact.ClimateZoneType.POLAR_N, 0, (int) Math.ceil(slice), worldFact);
        knowledgeBase.insert(northPolar);

        ClimateZoneFact northTemperate = new ClimateZoneFact("Temperate (N)", ClimateZoneFact.ClimateZoneType.TEMPERATE_N, (int) Math.ceil(slice), (int) Math.ceil(4.0 * slice), worldFact);
        knowledgeBase.insert(northTemperate);

        ClimateZoneFact northTropical = new ClimateZoneFact("Tropical (N)", ClimateZoneFact.ClimateZoneType.TROPICAL_N, (int) Math.ceil(4.0 * slice), (int) Math.ceil(6.0 * slice), worldFact);
        knowledgeBase.insert(northTropical);

        ClimateZoneFact southTropical = new ClimateZoneFact("Tropical (S)", ClimateZoneFact.ClimateZoneType.TROPICAL_S, (int) Math.ceil(6.0 * slice), (int) Math.ceil(8.0 * slice), worldFact);
        knowledgeBase.insert(southTropical);

        ClimateZoneFact southTemperate = new ClimateZoneFact("Temperate (S)", ClimateZoneFact.ClimateZoneType.TEMPERATE_S, (int) Math.ceil(8.0 * slice), (int) Math.ceil(11.0 * slice), worldFact);
        knowledgeBase.insert(southTemperate);

        ClimateZoneFact southPolar = new ClimateZoneFact("Polar (S)", ClimateZoneFact.ClimateZoneType.POLAR_S, (int) Math.ceil(11.0 * slice), (int) Math.ceil(12.0 * slice), worldFact);
        knowledgeBase.insert(southPolar);
    }
}
