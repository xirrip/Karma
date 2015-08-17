package org.hippomeetsskunk.world;

import org.hippomeetsskunk.knowledge.FactFactory;
import org.hippomeetsskunk.knowledge.FactId;
import org.hippomeetsskunk.knowledge.KnowledgeBase;
import org.hippomeetsskunk.world.map.WorldMap;
import org.hippomeetsskunk.world.map.generation.RecursiveMapGenerator;

import javax.inject.Inject;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
public class World {
    private final KnowledgeBase knowledgeBase;
    private final FactFactory factFactory;
    private final WorldMap map;

    @Inject
    public World(KnowledgeBase knowledgeBase, FactFactory factFactory, WorldMap map) {
        this.knowledgeBase = knowledgeBase;
        this.factFactory = factFactory;
        this.map = map;
    }

    public void run(){

        // insert basic knowledge
        System.out.println("Building base knowledge...");
        factFactory.insertFact(FactId.APPLE, knowledgeBase);

        // maybe inject, but then again, normal workflow rather loads map from file...
        System.out.println("Generating world...");
        RecursiveMapGenerator mapGenerator = new RecursiveMapGenerator();
        mapGenerator.generate(map);

    }

    public WorldMap getMap() {
        return map;
    }
}
