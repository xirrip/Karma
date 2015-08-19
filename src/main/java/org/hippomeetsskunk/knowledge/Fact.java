package org.hippomeetsskunk.knowledge;

import java.util.Collection;
import java.util.Set;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public interface Fact {

    /**
     * Each fact has a distinct id, facts in the  knowledge base should be singletons
     * @return fact id
     */
    String getFactId();

    /**
     * Each fact can have multiple, non structured types.
     * @return fact types
     */
    Collection<FactType> getFactTypes();

    /**
     * @param connectionType the connection type
     * @return all facts connected to this one by connection type
     */
    Collection<Fact> getConnectedFacts(ConnectionType connectionType);

    /**
     * Add a connection to the fact (connections are currently directed one-way)
     * @param connection
     */
    void addConnection(Connection connection);

    /**
     * @return distinct connection types from all connections of this fact.
     */
    Set<? extends ConnectionType> getConnectionTypes();
}
