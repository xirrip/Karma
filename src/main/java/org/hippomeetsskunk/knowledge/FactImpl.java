package org.hippomeetsskunk.knowledge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public class FactImpl implements Fact{

    private final String id;
    private final Collection<FactType> factTypes;
    private final Collection<Connection> connections;

    public FactImpl(String id, Collection<FactType> factTypes, Collection<Connection> connections) {
        this.id = id;
        this.factTypes = factTypes;
        this.connections = connections;
    }

    public FactImpl(String id, FactType... factTypes) {
        this.id = id;
        this.factTypes = new ArrayList<>();
        for(FactType t: factTypes) this.factTypes.add(t);
        this.connections = new ArrayList<>();
    }

    public String getFactId() {
        return id;
    }

    public Collection<FactType> getFactTypes() {
        return factTypes;
    }

    public Set<Fact> getConnectedFacts(ConnectionType connectionType) {
        Set<Fact> set = connections.stream()
                .filter(it -> connectionType.equals(it.getConnectionType()))
                .map(it -> it.getConnectionFact())
                .collect(Collectors.toSet());
        return set;
    }

    public void addConnection(Connection connection) {
        this.connections.add(connection);
    }

    public Set<? extends ConnectionType> getConnectionTypes() {
        return connections.stream()
                .map(c -> c.getConnectionType())
                .collect(Collectors.toSet());
    }


}
