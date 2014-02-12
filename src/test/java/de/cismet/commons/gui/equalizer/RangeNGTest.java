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
public class RangeNGTest {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testEnsureMinLessThanMax1() {
        final Range range = new Range(0, 0);
        fail("no exception thrown");
    }

    /**
     * DOCUMENT ME!
     */
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testEnsureMinLessThanMax2() {
        final Range range = new Range(1, 0);
        fail("no exception thrown");
    }
}
