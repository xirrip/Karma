package org.hippomeetsskunk.world;

import org.hippomeetsskunk.knowledge.FactFactory;
import org.hippomeetsskunk.knowledge.FactType;
import org.hippomeetsskunk.knowledge.KnowledgeBase;
import org.hippomeetsskunk.knowledge.facts.world.WorldFact;
import org.hippomeetsskunk.world.map.WorldMap;
import org.hippomeetsskunk.world.map.generation.RecursiveMapGenerator;

import javax.inject.Inject;
import java.io.IOException;

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

    public void run() throws IOException {

        // insert basic knowledge
        System.out.println("Building base knowledge...");
        knowledgeBase.insert(new WorldFact());

        factFactory.insertFact(FactType.APPLE, knowledgeBase);

        // maybe inject, but then again, normal workflow rather loads map from file...
        System.out.println("Generating world...");
        RecursiveMapGenerator mapGenerator = new RecursiveMapGenerator();
        mapGenerator.generate(map, knowledgeBase);

    }

    public WorldMap getMap() {
        return map;
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }
}
