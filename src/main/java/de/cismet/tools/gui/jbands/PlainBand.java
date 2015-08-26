/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands;

import java.util.ArrayList;

import de.cismet.tools.gui.jbands.interfaces.Band;
import de.cismet.tools.gui.jbands.interfaces.BandMember;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class PlainBand implements Band {

    //~ Instance fields --------------------------------------------------------

    protected ArrayList<BandMember> members = new ArrayList<BandMember>();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Double min = null;
    private Double max = null;

    private boolean enabled = true;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SimpleBand object.
     */
    public PlainBand() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public BandMember getMember(final int i) {
        assert (i > 0);
        assert (i < members.size());
        return members.get(i);
    }

    @Override
    public int getNumberOfMembers() {
        return members.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  m  DOCUMENT ME!
     */
    public void addMember(final BandMember m) {
        assert (m != null);
        members.add(m);
        min = null;
        max = null;
    }

    /**
     * DOCUMENT ME!
     */
    public void removeAllMember() {
        members.clear();
        min = null;
        max = null;
    }

    @Override
    public double getMax() {
        if (max == null) {
            max = 0.0;
            for (final BandMember bm : members) {
                max = (max < bm.getMax()) ? bm.getMax() : max;
            }
        }
        return max;
    }

    @Override
    public double getMin() {
        if (min == null) {
            min = Double.MAX_VALUE;
            for (final BandMember bm : members) {
                min = (min > bm.getMin()) ? bm.getMin() : min;
            }
        }
        return min;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    /**
     * DOCUMENT ME!
     *
     * @param  enabled  DOCUMENT ME!
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
