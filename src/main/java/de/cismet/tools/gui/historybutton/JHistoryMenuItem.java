/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.historybutton;

import javax.swing.JMenuItem;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten.hell@cismet.de
 * @version  $Revision$, $Date$
 */
public class JHistoryMenuItem extends JMenuItem {

    //~ Instance fields --------------------------------------------------------

    private Object object = null;
    private int position = -1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JHistoryMenuItem object.
     *
     * @param  o         DOCUMENT ME!
     * @param  position  DOCUMENT ME!
     */
    public JHistoryMenuItem(final Object o, final int position) {
        object = o;
        this.position = position;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getObject() {
        return object;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  object  DOCUMENT ME!
     */
    public void setObject(final Object object) {
        this.object = object;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getPosition() {
        return position;
    }

    @Override
    public String getText() {
        if (object != null) {
            return object.toString();
        } else {
            return ""; // NOI18N
        }
    }
}
