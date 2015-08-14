package org.hippomeetsskunk.knowledge;

import java.util.Collection;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public interface KnowledgeBase {

    Collection<Fact> getFacts(FactType... factTypes);
}
