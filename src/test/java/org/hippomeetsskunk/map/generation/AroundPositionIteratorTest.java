package org.hippomeetsskunk.map.generation;

import org.apache.commons.math3.util.Pair;
import org.hippomeetsskunk.world.map.generation.AroundPositionIterator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/16/2015.
 */
public class AroundPositionIteratorTest {

    @Test
    public void testAroundDefaultStart(){
        Pair<Integer, Integer> center = new Pair(10, 20);
        AroundPositionIterator it = new AroundPositionIterator(center);
        Assert.assertTrue(it.hasNext());

        Pair<Integer, Integer> n = it.next();
        Assert.assertEquals(new Pair(9, 19), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(10, 19), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(11, 19), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(11, 20), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(11, 21), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(10, 21), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(9, 21), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(9, 20), n);
        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void testAroundCustomStartClockwise(){
        Pair<Integer, Integer> center = new Pair(10, 20);
        AroundPositionIterator it = new AroundPositionIterator(center, 1, 0, true);
        Assert.assertTrue(it.hasNext());

        Pair<Integer, Integer> n;

        n = it.next();
        Assert.assertEquals(new Pair(11, 20), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(11, 21), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(10, 21), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(9, 21), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(9, 20), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(9, 19), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(10, 19), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(11, 19), n);
        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void testAroundCustomStartCounterClockwise(){
        Pair<Integer, Integer> center = new Pair(10, 20);
        AroundPositionIterator it = new AroundPositionIterator(center, 1, 0, false);
        Assert.assertTrue(it.hasNext());

        Pair<Integer, Integer> n;

        n = it.next();
        Assert.assertEquals(new Pair(11, 20), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(11, 19), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(10, 19), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(9, 19), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(9, 20), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(9, 21), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(10, 21), n);
        Assert.assertTrue(it.hasNext());

        n = it.next();
        Assert.assertEquals(new Pair(11, 21), n);
        Assert.assertFalse(it.hasNext());

    }

}
