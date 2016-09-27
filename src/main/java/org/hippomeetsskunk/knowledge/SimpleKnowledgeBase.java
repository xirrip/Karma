package org.hippomeetsskunk.knowledge;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public class SimpleKnowledgeBase implements KnowledgeBase {

    private final Collection<Fact> facts = new ArrayList<>();
    private final Map<String, Fact> idLookup = new HashMap<>();

    public Fact getFact(String id) {
        String lcId = id.toLowerCase();
        if(idLookup.containsKey(lcId)) return idLookup.get(lcId);
        throw new IllegalArgumentException("Unknown fact: " + id);
    }

    public Fact getFactCaseInsensitive(String id) {
        // we have a case insensitive implementation here
        return getFact(id);
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
        if(fact.getFactId() != null) {
            String id = fact.getFactId().toLowerCase();
            if (idLookup.containsKey(id))
                throw new IllegalArgumentException("Fact with id '" + id + "' already exists.");
            idLookup.put(id, fact);
        }
        facts.add(fact);
    }

    @Override
    public void alias(String alias, Fact fact) {
        String id = alias.toLowerCase();
        if(idLookup.containsKey(id)){
            throw new IllegalArgumentException("Fact with id '" + id + "' already exists.");
        }
        idLookup.put(id, fact);
    }
}
