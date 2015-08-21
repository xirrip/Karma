package org.hippomeetsskunk.knowledge.facts.world;

import org.hippomeetsskunk.knowledge.ConnectionImpl;
import org.hippomeetsskunk.knowledge.ConnectionType;
import org.hippomeetsskunk.knowledge.FactImpl;
import org.hippomeetsskunk.knowledge.FactType;

/**
 * Created by srzchx on 21.08.2015.
 */
public class ClimateZoneFact extends FactImpl {

    public enum ClimateZoneType{
        POLAR_N, TEMPERATE_N, TROPICAL_N, TROPICAL_S, TEMPERATE_S, POLAR_S
    }

    private final ClimateZoneType zoneType;
    private final int latitudeFrom;
    private final int latitudeTo;

    public ClimateZoneFact(String name, ClimateZoneType zoneType, int latitudeFrom, int latitudeTo, WorldFact world){
        super(name, new FactType[] { FactType.CLIMATE_ZONE });
        this.zoneType = zoneType;
        this.latitudeFrom = latitudeFrom;
        this.latitudeTo = latitudeTo;
        addConnection(new ConnectionImpl(ConnectionType.DESCRIBES, world));
        world.addConnection(new ConnectionImpl(ConnectionType.IS_DESCRIBED_BY, this));
    }

    public boolean isWithinZone(int latitude){
        return latitude >= latitudeFrom && latitude < latitudeTo;
    }

}
