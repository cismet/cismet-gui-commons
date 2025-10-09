/***************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 *
 *              ... and it just works.
 *
 ****************************************************/
package de.cismet.tools.gui.jbands;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class BandModelEvent {

    //~ Instance fields --------------------------------------------------------

    private boolean selectionLost;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the selectionLost
     */
    public boolean isSelectionLost() {
        return selectionLost;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectionLost  the selectionLost to set
     */
    public void setSelectionLost(final boolean selectionLost) {
        this.selectionLost = selectionLost;
    }
}
