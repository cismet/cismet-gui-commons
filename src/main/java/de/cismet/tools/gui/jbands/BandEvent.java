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
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class BandEvent {

    //~ Instance fields --------------------------------------------------------

    private boolean selectionLost;
    private boolean modelChanged = true;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BandEvent object.
     */
    public BandEvent() {
    }

    /**
     * Creates a new BandEvent object.
     *
     * @param  modelChanged  DOCUMENT ME!
     */
    public BandEvent(final boolean modelChanged) {
        this.modelChanged = modelChanged;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the bandDeleted
     */
    public boolean isSelectionLost() {
        return selectionLost;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectionLost  bandDeleted the bandDeleted to set
     */
    public void setSelectionLost(final boolean selectionLost) {
        this.selectionLost = selectionLost;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the modelChanged
     */
    public boolean isModelChanged() {
        return modelChanged;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  modelChanged  the modelChanged to set
     */
    public void setModelChanged(final boolean modelChanged) {
        this.modelChanged = modelChanged;
    }
}
