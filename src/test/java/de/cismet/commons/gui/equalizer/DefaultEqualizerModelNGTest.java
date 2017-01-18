/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.equalizer;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.*;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class DefaultEqualizerModelNGTest {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultEqualizerModelNGTest object.
     */
    public DefaultEqualizerModelNGTest() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testDefaultEqualizerModel_nullCategories() {
        new DefaultEqualizerModel(null);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testDefaultEqualizerModel_emptyCategories() {
        new DefaultEqualizerModel(new ArrayList<EqualizerCategory>(0));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testDefaultEqualizerModel_nullRange() {
        new DefaultEqualizerModel(Arrays.asList(new EqualizerCategory("test")), null);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testDefaultEqualizerModel_valueNotWithinRange1() {
        new DefaultEqualizerModel(Arrays.asList(new EqualizerCategory("test1", -1)), new Range(0, 1));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testDefaultEqualizerModel_valueNotWithinRange2() {
        new DefaultEqualizerModel(Arrays.asList(
                new EqualizerCategory("test1", -1),
                new EqualizerCategory("test2", 7),
                new EqualizerCategory("test3", -6)),
            new Range(-5, 7));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testDefaultEqualizerModel_duplicateCatName() {
        new DefaultEqualizerModel(Arrays.asList(
                new EqualizerCategory("test1", -1),
                new EqualizerCategory("test2", 7),
                new EqualizerCategory("test1", -5)),
            new Range(-5, 7));
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testGetEqualizerCategory_indexTooLow() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.getEqualizerCategory(-1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testGetEqualizerCategory_indexTooHigh() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.getEqualizerCategory(3);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetEqualizerCategory() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        assertEquals(instance.getEqualizerCategory(0), "test1");
        assertEquals(instance.getEqualizerCategory(2), "test3");
    }

    /**
     * Test of getEqualizerCategoryCount method, of class DefaultEqualizerModel.
     */
    @Test
    public void testGetEqualizerCategoryCount() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));

        assertEquals(instance.getEqualizerCategoryCount(), 3);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testGetValueAt_indexTooLow() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.getValueAt(-1);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testGetValueAt_indexTooHigh() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.getValueAt(3);
    }

    /**
     * Test of getValueAt method, of class DefaultEqualizerModel.
     */
    @Test
    public void testGetValueAt() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        assertEquals(instance.getValueAt(0), -1);
        assertEquals(instance.getValueAt(2), -5);
    }

    /**
     * Test of setValueAt method, of class DefaultEqualizerModel.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetValueAt_notWithinRange() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.setValueAt(2, 8);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testSetValueAt_indexTooLow() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.setValueAt(-1, 8);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testSetValueAt_indexTooHigh() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.setValueAt(3, 8);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(dependsOnMethods = "testGetValueAt")
    public void testSetValueAt() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.setValueAt(0, 1);
        instance.setValueAt(2, 4);

        assertEquals(instance.getValueAt(0), 1);
        assertEquals(instance.getValueAt(2), 4);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCheckValueWithinRange_notWithinRange1() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.checkValueWithinRange(8);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testCheckValueWithinRange_notWithinRange2() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.checkValueWithinRange(-6);
    }
    
    @Test
    public void testCheckValueWithinRange() {
        final DefaultEqualizerModel instance = new DefaultEqualizerModel(Arrays.asList(
                    new EqualizerCategory("test1", -1),
                    new EqualizerCategory("test2", 7),
                    new EqualizerCategory("test3", -5)),
                new Range(-5, 7));
        instance.checkValueWithinRange(0);
        instance.checkValueWithinRange(2);
        instance.checkValueWithinRange(-5);
        instance.checkValueWithinRange(7);
    }
}
