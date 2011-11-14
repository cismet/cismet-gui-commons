/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands;

import de.cismet.tools.gui.jbands.interfaces.Band;
import de.cismet.tools.gui.jbands.interfaces.BandMember;
import de.cismet.tools.gui.jbands.interfaces.BandWeightProvider;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class EmptyWeightedBand implements Band, BandWeightProvider {

    //~ Instance fields --------------------------------------------------------

    final float weight;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EmptyWeightedBand object.
     */
    public EmptyWeightedBand() {
        this(1f);
    }

    /**
     * Creates a new EmptyWeightedBand object.
     *
     * @param  weight  DOCUMENT ME!
     */
    public EmptyWeightedBand(final float weight) {
        this.weight = weight;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public double getMax() {
        return 0;
    }

    @Override
    public BandMember getMember(final int i) {
        throw new UnsupportedOperationException("not allowed in EmptyBand");
    }

    @Override
    public double getMin() {
        return 0;
    }

    @Override
    public int getNumberOfMembers() {
        return 0;
    }

    @Override
    public float getBandWeight() {
        return weight;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
