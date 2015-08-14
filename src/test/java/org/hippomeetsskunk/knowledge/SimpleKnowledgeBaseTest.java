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

        factory.insertFact(FactId.APPLE, kb);
        factory.insertFact(FactId.TOMATO, kb);

        Assert.assertNotNull(kb.getFact(FactId.APPLE));
        Assert.assertNotNull(kb.getFact(FactId.TOMATO));

        Assert.assertEquals(1, kb.getFacts(FactType.APPLE).size());
        Assert.assertEquals(FactId.APPLE, kb.getFacts(FactType.APPLE).iterator().next().getFactId());

        Assert.assertEquals(1, kb.getFacts(FactType.FRUIT).size());
        Assert.assertEquals(FactId.APPLE, kb.getFacts(FactType.FRUIT).iterator().next().getFactId());

        Assert.assertEquals(1, kb.getFacts(FactType.VEGETABLE).size());
        Assert.assertEquals(FactId.TOMATO, kb.getFacts(FactType.VEGETABLE).iterator().next().getFactId());

        Assert.assertEquals(1, kb.getFacts(FactType.FOOD, FactType.FRUIT).size());
        Assert.assertEquals(FactId.APPLE, kb.getFacts(FactType.FOOD, FactType.FRUIT).iterator().next().getFactId());

        Assert.assertEquals(2, kb.getFacts(FactType.FOOD).size());
        Assert.assertEquals(0, kb.getFacts(FactType.FRUIT, FactType.VEGETABLE).size()); // AND
    }


}
