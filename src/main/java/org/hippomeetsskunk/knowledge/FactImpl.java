package org.hippomeetsskunk.knowledge;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public class FactImpl implements Fact{

    private final FactId id;
    private final Collection<FactType> factTypes;
    private final Collection<Connection> connections;

    public FactImpl(FactId id, Collection<FactType> factTypes, Collection<Connection> connections) {
        this.id = id;
        this.factTypes = factTypes;
        this.connections = connections;
    }

    public FactImpl(FactId id, FactType... factTypes) {
        this.id = id;
        this.factTypes = new ArrayList<FactType>();
        for(FactType t: factTypes) this.factTypes.add(t);
        this.connections = new ArrayList<Connection>();
    }

    public FactId getFactId() {
        return id;
    }

    public Collection<FactType> getFactTypes() {
        return factTypes;
    }

    public Collection<Fact> getConnectedFacts(ConnectionType connectionType) {
        return null;
    }
}
