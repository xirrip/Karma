package org.hippomeetsskunk.knowledge;

import dagger.Module;
import dagger.Provides;
import org.hippomeetsskunk.world.map.SimpleWorldMap;
import org.hippomeetsskunk.world.map.WorldMap;

import javax.inject.Singleton;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
@Module
public class SimpleKnowledgeBaseModule {
    @Provides @Singleton KnowledgeBase provideKnowledgeBase(){
        return new SimpleKnowledgeBase();
    }

    @Provides @Singleton FactFactory provideFactFactory(){
        return new SimpleFactFactory();
    }

    @Provides @Singleton
    WorldMap provideMap(){
        return new SimpleWorldMap();
    }
}
