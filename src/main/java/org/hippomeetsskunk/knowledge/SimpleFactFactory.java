package org.hippomeetsskunk.knowledge;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public class SimpleFactFactory implements FactFactory {

    // create typed facts (not instantiations by id)
    public void insertFact(FactType type, KnowledgeBase knowledgeBase) {
        switch(type){
            case APPLE:
                knowledgeBase.insert(new FactImpl(null, FactType.APPLE));
                break;
            case TOMATO:
                knowledgeBase.insert(new FactImpl(null, FactType.TOMATO));
                break;
            default:
                throw new IllegalArgumentException("Unknown fact type: " + type);
        }
    }

}
