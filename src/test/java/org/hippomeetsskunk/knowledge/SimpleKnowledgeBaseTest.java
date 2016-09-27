package org.hippomeetsskunk.knowledge;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public class SimpleKnowledgeBaseTest {

    @Test
    public void testFacts(){
        KnowledgeBase kb = new SimpleKnowledgeBase();
        FactFactory factory = new SimpleFactFactory();

        factory.insertFact(FactType.APPLE, kb);
        factory.insertFact(FactType.TOMATO, kb);

        /* TODO test by id facts
        Assert.assertNotNull(kb.getFact(FactType.APPLE));
        Assert.assertNotNull(kb.getFact(FactType.TOMATO));
        */

        Assert.assertEquals(1, kb.getFacts(FactType.APPLE).size());
        Assert.assertEquals(FactType.APPLE, kb.getFacts(FactType.APPLE).iterator().next().getFactTypes().iterator().next());

        Assert.assertEquals(1, kb.getFacts(FactType.FRUIT).size());
        Assert.assertEquals(FactType.APPLE, kb.getFacts(FactType.FRUIT).iterator().next().getFactTypes().iterator().next());

        Assert.assertEquals(1, kb.getFacts(FactType.VEGETABLE).size());
        Assert.assertEquals(FactType.TOMATO, kb.getFacts(FactType.VEGETABLE).iterator().next().getFactTypes().iterator().next());

        Assert.assertEquals(1, kb.getFacts(FactType.FOOD, FactType.FRUIT).size());
        Assert.assertEquals(FactType.APPLE, kb.getFacts(FactType.FOOD, FactType.FRUIT).iterator().next().getFactTypes().iterator().next());

        Assert.assertEquals(2, kb.getFacts(FactType.FOOD).size());
        Assert.assertEquals(0, kb.getFacts(FactType.FRUIT, FactType.VEGETABLE).size()); // AND
    }


}
