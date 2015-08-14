package org.hippomeetsskunk.knowledge;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public class SimpleFactFactory implements FactFactory {

    public void insertFact(FactId id, KnowledgeBase knowledgeBase) {
        switch(id){
            case APPLE:
                knowledgeBase.insert(new FactImpl(FactId.APPLE, FactType.APPLE));
                break;
            case TOMATO:
                knowledgeBase.insert(new FactImpl(FactId.TOMATO, FactType.TOMATO));
                break;
            default:
                throw new IllegalArgumentException("Unknown fact id: " + id);
        }
    }

}
