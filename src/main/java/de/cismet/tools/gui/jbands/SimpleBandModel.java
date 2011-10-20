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
import de.cismet.tools.gui.jbands.interfaces.BandModel;
import de.cismet.tools.gui.jbands.interfaces.BandModelListener;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class SimpleBandModel implements BandModel {

    //~ Instance fields --------------------------------------------------------

    ArrayList<Band> bands = new ArrayList<Band>();
    ArrayList<BandModelListener> listeners = new ArrayList<BandModelListener>();

    //~ Methods ----------------------------------------------------------------

    @Override
    public Band getBand(final int bandNumber) {
        assert (bandNumber > 0);
        assert (bandNumber < bands.size());
        return bands.get(bandNumber);
    }

    @Override
    public int getNumberOfBands() {
        return bands.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  band  DOCUMENT ME!
     */
    public void addBand(final Band band) {
        assert (band != null);
        bands.add(band);
        fireBandModelChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  band  DOCUMENT ME!
     * @param  pos   DOCUMENT ME!
     */
    public void insertBand(final Band band, final int pos) {
        assert (band != null);
        bands.add(pos, band);
        fireBandModelChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   band  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int removeBand(final Band band) {
        assert (band != null);
        assert (bands.contains(band));
        final int pos = bands.indexOf(band);
        bands.remove(band);
        fireBandModelChanged();
        return pos;
    }

    @Override
    public double getMax() {
        double value = 0;
        for (final Band b : bands) {
            value = (b.getMax() > value) ? b.getMax() : value;
        }
        return value;
    }

    @Override
    public double getMin() {
        double value = 0;
        for (final Band b : bands) {
            value = (b.getMin() < value) ? b.getMin() : value;
        }
        return value;
    }

    @Override
    public void addBandModelListener(final BandModelListener bml) {
        listeners.add(bml);
    }

    @Override
    public void removeBandModelListener(final BandModelListener bml) {
        listeners.remove(bml);
    }

    /**
     * DOCUMENT ME!
     */
    protected void fireBandModelChanged() {
        for (final BandModelListener bml : listeners) {
            bml.bandModelChanged(null);
        }
    }
}
