/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands;

import de.cismet.tools.gui.jbands.interfaces.BandWeightProvider;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultBand extends SimpleBand implements BandWeightProvider {

    //~ Instance fields --------------------------------------------------------

    private final float heightWeight;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultBand object.
     */
    public DefaultBand() {
        this(1f, "");
    }

    /**
     * Creates a new DefaultBand object.
     *
     * @param  heightWeight  DOCUMENT ME!
     */
    public DefaultBand(final float heightWeight) {
        this(heightWeight, "");
    }

    /**
     * Creates a new DefaultBand object.
     *
     * @param  title  DOCUMENT ME!
     */
    public DefaultBand(final String title) {
        super(title);
        this.heightWeight = 1f;
    }

    /**
     * Creates a new DefaultBand object.
     *
     * @param  heightWeight  DOCUMENT ME!
     * @param  title         DOCUMENT ME!
     */
    public DefaultBand(final float heightWeight, final String title) {
        super(title);
        this.heightWeight = heightWeight;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public float getBandWeight() {
        return heightWeight;
    }
}
