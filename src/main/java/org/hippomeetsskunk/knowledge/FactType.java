package org.hippomeetsskunk.knowledge;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public enum FactType {
    FOOD,
        FRUIT(FOOD),
            APPLE(FRUIT),
        VEGETABLE(FOOD),
            TOMATO(VEGETABLE);


    private final FactType[] superClasses;

    FactType(FactType... superClasses) {
        this.superClasses = superClasses;
    }

    public boolean isSubclassOf(FactType factType){
        if(factType.equals(this)) return true;
        if(superClasses!=null){
            for(FactType s : superClasses){
                if(s.isSubclassOf(factType)) return true;
            }
        }
        return false;
    }
}
