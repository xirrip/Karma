package org.hippomeetsskunk.knowledge;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public interface Connection {
    ConnectionType getConnectionType();
    Fact getConnectionFact();
}
