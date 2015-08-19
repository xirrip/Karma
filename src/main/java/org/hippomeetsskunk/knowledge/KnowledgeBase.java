package org.hippomeetsskunk.knowledge;

import java.util.Collection;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public interface KnowledgeBase {

    Fact getFact(String id);
    Fact getFactCaseInsensitive(String id);
    Collection<Fact> getFacts(FactType... factTypes);

    void insert(Fact fact);
}
