/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands.interfaces;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface BandModel {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getNumberOfBands();

    /**
     * DOCUMENT ME!
     *
     * @param   bandNumber  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Band getBand(int bandNumber);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    double getMin();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    double getMax();

    /**
     * DOCUMENT ME!
     *
     * @param  bml  DOCUMENT ME!
     */
    void addBandModelListener(BandModelListener bml);

    /**
     * DOCUMENT ME!
     *
     * @param  bml  DOCUMENT ME!
     */
    void removeBandModelListener(BandModelListener bml);
}
