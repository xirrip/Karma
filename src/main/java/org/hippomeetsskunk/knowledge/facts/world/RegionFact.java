package org.hippomeetsskunk.knowledge.facts.world;

import org.hippomeetsskunk.knowledge.ConnectionImpl;
import org.hippomeetsskunk.knowledge.ConnectionType;
import org.hippomeetsskunk.knowledge.FactImpl;
import org.hippomeetsskunk.knowledge.FactType;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/18/2015.
 */
public class RegionFact extends FactImpl {

    private int size;

    public RegionFact(String name, ContinentFact continent){
        super(name, new FactType[] { FactType.REGION });
        if(continent != null) {
            addConnection(new ConnectionImpl(ConnectionType.IS_PART_OF, continent));
            continent.addConnection(new ConnectionImpl(ConnectionType.CONSISTS_OF, this));
        }
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

}

