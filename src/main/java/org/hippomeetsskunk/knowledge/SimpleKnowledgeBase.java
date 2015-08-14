package org.hippomeetsskunk.knowledge;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public class SimpleKnowledgeBase implements KnowledgeBase {

    private final Collection<Fact> facts = new ArrayList<Fact>();


    public Fact getFact(FactId id) {
        for(Fact f : facts){
            if(id.equals(f.getFactId())) return f;
        }
        throw new IllegalArgumentException("Unknown fact.");
    }

    public Collection<Fact> getFacts(FactType... factTypes) {
        Collection<Fact> matching = new ArrayList<Fact>();
        for(Fact f : facts){
            boolean matchesAll = true;
            for(FactType t : factTypes){
                boolean anyMatch = false;
                for(FactType a : f.getFactTypes()){
                    if(a.isSubclassOf(t)){
                        anyMatch = true;
                        break;
                    }
                }
                if(!anyMatch){
                    matchesAll = false;
                    break;
                }
            }
            if(matchesAll){
                matching.add(f);
            }
        }
        return matching;
    }

    public void insert(Fact fact) {
        facts.add(fact);
    }
}
