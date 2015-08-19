package org.hippomeetsskunk.knowledge;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/14/2015.
 */
public class FactTypeTest {

    @Test
    public void testSubclassOf(){
        FactType apple = FactType.APPLE;

        Assert.assertTrue(apple.isSubclassOf(FactType.APPLE));
        Assert.assertTrue(apple.isSubclassOf(FactType.FRUIT));
        Assert.assertTrue(apple.isSubclassOf(FactType.FOOD));

        Assert.assertFalse(apple.isSubclassOf(FactType.VEGETABLE));
        Assert.assertFalse(apple.isSubclassOf(FactType.TOMATO));

    }
}
