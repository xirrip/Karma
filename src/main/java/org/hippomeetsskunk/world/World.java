package org.hippomeetsskunk.world;

import org.hippomeetsskunk.knowledge.FactFactory;
import org.hippomeetsskunk.knowledge.FactType;
import org.hippomeetsskunk.knowledge.KnowledgeBase;
import org.hippomeetsskunk.knowledge.facts.world.WorldFact;
import org.hippomeetsskunk.world.map.WorldMap;
import org.hippomeetsskunk.world.map.generation.RecursiveMapGenerator;
import org.hippomeetsskunk.world.map.generation.RegionGenerator;

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

    public WorldMap getMap() {
        return map;
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }
}
