package org.hippomeetsskunk.knowledge.facts.world;

import org.hippomeetsskunk.knowledge.*;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/18/2015.
 */
public class ContinentFact extends FactImpl {

    private int size;

    public ContinentFact(String name, WorldFact world){
        super(name, new FactType[] { FactType.CONTINENT });
        addConnection(new ConnectionImpl(ConnectionType.IS_PART_OF, world));
        world.addConnection(new ConnectionImpl(ConnectionType.CONSISTS_OF, this));
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}

