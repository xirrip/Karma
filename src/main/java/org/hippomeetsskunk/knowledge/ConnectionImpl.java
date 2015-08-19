package org.hippomeetsskunk.knowledge;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/18/2015.
 */
public class ConnectionImpl implements Connection {
    private final ConnectionType type;
    private final Fact fact;

    public ConnectionImpl(ConnectionType type, Fact fact) {
        this.type = type;
        this.fact = fact;
    }

    public ConnectionType getConnectionType() {
        return type;
    }

    public Fact getConnectionFact() {
        return fact;
    }
}
