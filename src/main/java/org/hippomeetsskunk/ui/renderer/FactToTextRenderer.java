package org.hippomeetsskunk.ui.renderer;

import org.hippomeetsskunk.knowledge.ConnectionType;
import org.hippomeetsskunk.knowledge.Fact;
import org.hippomeetsskunk.knowledge.facts.world.ContinentFact;
import org.hippomeetsskunk.knowledge.facts.world.RegionFact;
import org.hippomeetsskunk.knowledge.facts.world.WorldFact;

import java.util.Collection;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/19/2015.
 */
public class FactToTextRenderer {
    public String render(Fact fact){
        StringBuffer text = new StringBuffer();

        if(fact instanceof WorldFact){
            WorldFact wf = (WorldFact) fact;
            text.append(String.format("The world:\nLand sea ratio is %3.1f%%.", 100.0 * wf.getLandSeaRatio()));
        }
        else if(fact instanceof ContinentFact){
            ContinentFact cf = (ContinentFact) fact;
            text.append(String.format("Continent " + cf.getFactId() + ":\n"));
            text.append(String.format("Size: %,5d", cf.getSize()));
        }
        else if(fact instanceof RegionFact){
            RegionFact rf = (RegionFact) fact;
            text.append(String.format("Region " + rf.getFactId() + ":\n"));
            text.append(String.format("Size: %,5d", rf.getSize()));
        }
        else{
            if(fact.getFactId() != null) text.append(fact.getFactId());
        }

        // lets see about connections...
        for(ConnectionType type : fact.getConnectionTypes()){
            text.append("\n" + type + ":\n");
            Collection<Fact> connectedFacts = fact.getConnectedFacts(type);
            if(connectedFacts != null) {
                for (Fact f : connectedFacts) {
                    if (f.getFactId() != null) text.append("\t" + f.getFactId() + "\n");
                }
            }
        }

        return text.toString();
    }
}
