/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.equalizer;

import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class RubberBandEqualizerModelNGTest {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRubberBandEqualizerModel_sumNE100_1() {
        new RubberBandEqualizerModel(Arrays.asList(new EqualizerCategory("test1", 1)));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRubberBandEqualizerModel_sumNE100_2() {
        new RubberBandEqualizerModel(Arrays.asList(
                new EqualizerCategory("test1", 1),
                new EqualizerCategory("test1", 1),
                new EqualizerCategory("test1", 1),
                new EqualizerCategory("test1", 1),
                new EqualizerCategory("test1", 1)));
    }

    /**
     * DOCUMENT ME!
     */
    public void testRubberBandEqualizerModel_equalDistribution() {
        RubberBandEqualizerModel model = new RubberBandEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", 0),
                    new EqualizerCategory("test1", 0),
                    new EqualizerCategory("test1", 0),
                    new EqualizerCategory("test1", 0),
                    new EqualizerCategory("test1", 0)));

        assertEquals(model.getValueAt(0), 20);
        assertEquals(model.getValueAt(1), 20);
        assertEquals(model.getValueAt(2), 20);
        assertEquals(model.getValueAt(3), 20);
        assertEquals(model.getValueAt(4), 20);

        model = new RubberBandEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", 0),
                    new EqualizerCategory("test1", 0),
                    new EqualizerCategory("test1", 0)));

        assertEquals(model.getValueAt(0), 33);
        assertEquals(model.getValueAt(1), 33);
        assertEquals(model.getValueAt(2), 34);
    }

    /**
     * Test of setValueAt method, of class DefaultEqualizerModel.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetValueAt_notWithinRange() {
        final RubberBandEqualizerModel instance = new RubberBandEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", 33),
                    new EqualizerCategory("test2", 33),
                    new EqualizerCategory("test3", 34)));
        instance.setValueAt(0, 101);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testSetValueAt_indexTooLow() {
        final RubberBandEqualizerModel instance = new RubberBandEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", 33),
                    new EqualizerCategory("test2", 33),
                    new EqualizerCategory("test3", 34)));
        instance.setValueAt(-1, 8);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testSetValueAt_indexTooHigh() {
        final RubberBandEqualizerModel instance = new RubberBandEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", 33),
                    new EqualizerCategory("test2", 33),
                    new EqualizerCategory("test3", 34)));
        instance.setValueAt(3, 8);
    }

    /**
     * Test of setValueAt method, of class RubberBandEqualizerModel.
     */
    @Test
    public void testSetValueAt() {
        RubberBandEqualizerModel instance = new RubberBandEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", 33),
                    new EqualizerCategory("test2", 33),
                    new EqualizerCategory("test3", 34)));
        instance.setValueAt(2, 50);
        assertEquals(instance.getValueAt(0), 25);
        assertEquals(instance.getValueAt(1), 25);
        assertEquals(instance.getValueAt(2), 50);

        instance.setValueAt(2, 50);
        assertEquals(instance.getValueAt(0), 25);
        assertEquals(instance.getValueAt(1), 25);
        assertEquals(instance.getValueAt(2), 50);

        instance.setValueAt(1, 50);
        assertEquals(instance.getValueAt(0), 12);
        assertEquals(instance.getValueAt(1), 50);
        assertEquals(instance.getValueAt(2), 38);

        instance.setValueAt(0, 11);
        assertEquals(instance.getValueAt(0), 11);
        assertEquals(instance.getValueAt(1), 51);
        assertEquals(instance.getValueAt(2), 38);

        instance.setValueAt(1, 81);
        assertEquals(instance.getValueAt(0), 0);
        assertEquals(instance.getValueAt(1), 81);
        assertEquals(instance.getValueAt(2), 19);

        instance.setValueAt(2, 45);
        assertEquals(instance.getValueAt(0), 0);
        assertEquals(instance.getValueAt(1), 55);
        assertEquals(instance.getValueAt(2), 45);

        instance.setValueAt(0, 99);
        assertEquals(instance.getValueAt(0), 99);
        assertEquals(instance.getValueAt(1), 1);
        assertEquals(instance.getValueAt(2), 0);

        instance.setValueAt(2, 100);
        assertEquals(instance.getValueAt(0), 0);
        assertEquals(instance.getValueAt(1), 0);
        assertEquals(instance.getValueAt(2), 100);
        
        instance = new RubberBandEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", 10),
                    new EqualizerCategory("test2", 10),
                    new EqualizerCategory("test3", 10),
                    new EqualizerCategory("test4", 10),
                    new EqualizerCategory("test5", 10),
                    new EqualizerCategory("test6", 10),
                    new EqualizerCategory("test7", 10),
                    new EqualizerCategory("test8", 10),
                    new EqualizerCategory("test9", 10),
                    new EqualizerCategory("test10", 10)));
        instance.setValueAt(0, 50);
        assertEquals(instance.getValueAt(0), 50);
        assertEquals(instance.getValueAt(1), 5);
        assertEquals(instance.getValueAt(2), 5);
        assertEquals(instance.getValueAt(3), 5);
        assertEquals(instance.getValueAt(4), 5);
        assertEquals(instance.getValueAt(5), 6);
        assertEquals(instance.getValueAt(6), 6);
        assertEquals(instance.getValueAt(7), 6);
        assertEquals(instance.getValueAt(8), 6);
        assertEquals(instance.getValueAt(9), 6);
        instance.setValueAt(9, 49);
        assertEquals(instance.getValueAt(0), 45);
        assertEquals(instance.getValueAt(1), 0);
        assertEquals(instance.getValueAt(2), 0);
        assertEquals(instance.getValueAt(3), 0);
        assertEquals(instance.getValueAt(4), 0);
        assertEquals(instance.getValueAt(5), 1);
        assertEquals(instance.getValueAt(6), 1);
        assertEquals(instance.getValueAt(7), 2);
        assertEquals(instance.getValueAt(8), 2);
        assertEquals(instance.getValueAt(9), 49);
        instance.setValueAt(0, 50);
        assertEquals(instance.getValueAt(0), 50);
        assertEquals(instance.getValueAt(1), 0);
        assertEquals(instance.getValueAt(2), 0);
        assertEquals(instance.getValueAt(3), 0);
        assertEquals(instance.getValueAt(4), 0);
        assertEquals(instance.getValueAt(5), 0);
        assertEquals(instance.getValueAt(6), 0);
        assertEquals(instance.getValueAt(7), 1);
        assertEquals(instance.getValueAt(8), 1);
        assertEquals(instance.getValueAt(9), 48);
    }
}
