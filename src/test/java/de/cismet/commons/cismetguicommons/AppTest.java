/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.cismetguicommons;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 *
 * @version  $Revision$, $Date$
 */
public class AppTest extends TestCase {

    //~ Constructors -----------------------------------------------------------

    /**
     * Create the test case.
     *
     * @param  testName  name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-).
     */
    public void testApp() {
        assertTrue(true);
    }
}
