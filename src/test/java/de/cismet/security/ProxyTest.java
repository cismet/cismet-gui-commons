/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.security;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public class ProxyTest {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProxyTest object.
     */
    public ProxyTest() {
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
     */
    @Before
    public void setUp() {
    }

    /**
     * DOCUMENT ME!
     */
    @After
    public void tearDown() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getCurrentMethodName() {
        return new Throwable().getStackTrace()[1].getMethodName();
    }

    /**
     * Be careful with that test. It will delete all your probably present preferences.
     */
    @Ignore
    @Test
    public void testFromAndToPreferences() {
        System.out.println("TEST " + getCurrentMethodName());
        Proxy.toPreferences(null);
        Proxy proxy = Proxy.fromPreferences();
        assertNull(proxy);
        proxy = new Proxy(null, 0);
        proxy.toPreferences();
        proxy = Proxy.fromPreferences();
        assertNull(proxy);
        proxy = new Proxy("abc", 1);
        proxy.toPreferences();
        proxy = Proxy.fromPreferences();
        assertNotNull(proxy);
        assertEquals("abc", proxy.getHost());
        assertEquals(1, proxy.getPort());
        proxy = new Proxy("cba", 2, "I", "and", "you", true);
        proxy.toPreferences();
        proxy = Proxy.fromPreferences();
        assertNotNull(proxy);
        assertEquals("cba", proxy.getHost());
        assertEquals(2, proxy.getPort());
        assertEquals("I", proxy.getUsername());
        assertEquals("and", proxy.getPassword());
        assertEquals("you", proxy.getDomain());
        Proxy.toPreferences(null);
        proxy = Proxy.fromPreferences();
        assertNull(proxy);
    }
}
