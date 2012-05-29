/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands;

import org.apache.log4j.Logger;

import java.awt.Cursor;

import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class JBandCursorManager {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(JBandCursorManager.class);

    private static final JBandCursorManager INSTANCE = new JBandCursorManager();

    //~ Instance fields --------------------------------------------------------

    private boolean locked = false;
    private Cursor cursor;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JBandCursorManager object.
     */
    private JBandCursorManager() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static JBandCursorManager getInstance() {
        return INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the locked
     */
    public synchronized boolean isLocked() {
        return locked;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  locked  the locked to set
     */
    public synchronized void setLocked(final boolean locked) {
        this.locked = locked;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the c
     */
    public synchronized Cursor getCursor() {
        return cursor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cursor  c the c to set
     */
    public synchronized void setCursor(final Cursor cursor) {
        if (!locked) {
            this.cursor = cursor;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  comp  DOCUMENT ME!
     */
    public synchronized void setCursor(final JComponent comp) {
        comp.setCursor(cursor);
    }
}
