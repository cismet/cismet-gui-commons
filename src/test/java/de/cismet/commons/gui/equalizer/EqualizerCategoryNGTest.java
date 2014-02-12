/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.equalizer;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class EqualizerCategoryNGTest {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testEnsureNameNotNull() {
        new EqualizerCategory(null);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testEnsureNameNotNull2() {
        new EqualizerCategory(null, 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testEnsureNameNotEmpty() {
        new EqualizerCategory("", 0);
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testClone() {
        final EqualizerCategory ec1 = new EqualizerCategory("test", 4);
        final EqualizerCategory clone = (EqualizerCategory)ec1.clone();
        assertEquals(clone, ec1);
    }
}
