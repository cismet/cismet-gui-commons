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
import de.cismet.tools.gui.jbands.interfaces.BandListener;
import de.cismet.tools.gui.jbands.interfaces.BandModel;
import de.cismet.tools.gui.jbands.interfaces.BandModelListener;
import de.cismet.tools.gui.jbands.interfaces.BandModificationProvider;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class SimpleBandModel implements BandModel, BandListener {

    //~ Instance fields --------------------------------------------------------

    private ArrayList<Band> bands = new ArrayList<Band>();
    private ArrayList<BandModelListener> listeners = new ArrayList<BandModelListener>();
    private double min = -1;
    private double max = -1;

    //~ Methods ----------------------------------------------------------------

    @Override
    public Band getBand(final int bandNumber) {
        return bands.get(bandNumber);
    }

    @Override
    public int getNumberOfBands() {
        return bands.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   band  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public void addBand(final Band band) {
        if (band == null) {
            throw new IllegalArgumentException("band must not be null");
        }

        if (band instanceof BandModificationProvider) {
            ((BandModificationProvider)band).addBandListener(this);
        }
        bands.add(band);
        fireBandModelChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   band  DOCUMENT ME!
     * @param   pos   DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public void insertBand(final Band band, final int pos) {
        if (band == null) {
            throw new IllegalArgumentException("band must not be null");
        }

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
        final int pos = bands.indexOf(band);
        if (band instanceof BandModificationProvider) {
            ((BandModificationProvider)band).removeBandListener(this);
        }
        bands.remove(band);
        fireBandModelChanged();
        return pos;
    }

    @Override
    public double getMax() {
        if (max > -1) {
            return max;
        } else {
            double value = 0;
            for (final Band b : bands) {
                value = (b.getMax() > value) ? b.getMax() : value;
            }
            return value;
        }
    }

    @Override
    public double getMin() {
        if (min > -1) {
            return min;
        } else {
            double value = Double.MAX_VALUE;
            for (final Band b : bands) {
                value = (b.getMin() < value) ? b.getMin() : value;
            }
            return value;
        }
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
    public void fireBandModelChanged() {
        for (final BandModelListener bml : listeners) {
            bml.bandModelChanged(null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public void fireBandModelChanged(final BandModelEvent e) {
        for (final BandModelListener bml : listeners) {
            bml.bandModelChanged(e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void fireBandModelSelectionChanged() {
        for (final BandModelListener bml : listeners) {
            bml.bandModelSelectionChanged(null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public void fireBandModelValuesChanged(final BandModelEvent e) {
        for (final BandModelListener bml : listeners) {
            bml.bandModelValuesChanged(e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void fireBandModelValuesChanged() {
        for (final BandModelListener bml : listeners) {
            bml.bandModelValuesChanged(null);
        }
    }

    @Override
    public void bandChanged(final BandEvent e) {
        if (e.isSelectionLost()) {
            final BandModelEvent bme = new BandModelEvent();
            bme.setSelectionLost(true);
            if ((e != null) && !e.isModelChanged()) {
                fireBandModelValuesChanged(bme);
            } else {
                fireBandModelChanged(bme);
            }
            fireBandModelSelectionChanged();
        } else {
            if ((e != null) && !e.isModelChanged()) {
                fireBandModelValuesChanged(null);
            } else {
                fireBandModelChanged(null);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  min  the min to set
     */
    public void setMin(final double min) {
        this.min = min;

        for (final Band tmp : bands) {
            if (tmp instanceof BandModificationProvider) {
                ((BandModificationProvider)tmp).setMin(min);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  max  the max to set
     */
    public void setMax(final double max) {
        this.max = max;

        for (final Band tmp : bands) {
            if (tmp instanceof BandModificationProvider) {
                ((BandModificationProvider)tmp).setMax(max);
            }
        }
    }
}
