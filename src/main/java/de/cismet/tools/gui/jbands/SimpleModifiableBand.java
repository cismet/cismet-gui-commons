/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.WaitingDialogThread;
import de.cismet.tools.gui.jbands.interfaces.BandListener;
import de.cismet.tools.gui.jbands.interfaces.BandMember;
import de.cismet.tools.gui.jbands.interfaces.BandMemberListener;
import de.cismet.tools.gui.jbands.interfaces.BandModificationProvider;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class SimpleModifiableBand extends DefaultBand implements BandModificationProvider, BandMemberListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(SimpleModifiableBand.class);

    //~ Instance fields --------------------------------------------------------

    protected boolean readOnly = false;
    protected Double fixMin = null;
    protected Double fixMax = null;
    private List<BandListener> listenerList = new ArrayList<BandListener>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MassnahmenBand object.
     *
     * @param  title  DOCUMENT ME!
     */
    public SimpleModifiableBand(final String title) {
        this(1f, title);
    }

    /**
     * Creates a new MassnahmenBand object.
     *
     * @param  heightWeight  DOCUMENT ME!
     */
    public SimpleModifiableBand(final float heightWeight) {
        super(heightWeight);
    }

    /**
     * Creates a new MassnahmenBand object.
     *
     * @param  heightWeight  DOCUMENT ME!
     * @param  title         DOCUMENT ME!
     */
    public SimpleModifiableBand(final float heightWeight, final String title) {
        super(heightWeight, title);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  readOnly  DOCUMENT ME!
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public void addMember(final Double startStation,
            final Double endStation,
            final Double minStart,
            final Double maxEnd,
            final List<BandMember> memberList) {
        final WaitingDialogThread<Void> wdt = new WaitingDialogThread<Void>(StaticSwingTools.getParentFrame(
                    getPrefixComponent()),
                true,
                "Erstelle Abschnitt",
                null,
                100) {

                @Override
                protected Void doInBackground() throws Exception {
                    if (endStation == null) {
                        addUnspecifiedMember(startStation, minStart, maxEnd, memberList);
                    } else {
                        addSpecifiedMember(startStation, endStation);
                    }

                    return null;
                }
            };
        wdt.start();
    }

    @Override
    public void addMember(final BandMember m) {
        super.addMember(m);
        if (m instanceof SimpleModifiableBandMember) {
            ((SimpleModifiableBandMember)m).addBandMemberListener(this);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  startStation  DOCUMENT ME!
     * @param  minStart      DOCUMENT ME!
     * @param  maxEnd        DOCUMENT ME!
     * @param  memberList    DOCUMENT ME!
     */
    private void addUnspecifiedMember(final Double startStation,
            final Double minStart,
            final Double maxEnd,
            final List<BandMember> memberList) {
        double distanceBefore = Double.MAX_VALUE;
        double distanceBehind = Double.MAX_VALUE;
        double newTo = maxEnd;
        double newFrom = minStart;

        if (memberList != null) {
            // member list will be considered.
            for (final BandMember tmp : memberList) {
                final Double from = (Double)tmp.getMin();
                final Double till = (Double)tmp.getMax();

                if ((from != null) && (till != null)) {
                    double distance = startStation - till;
                    if ((distance < distanceBefore) && (distance >= 0)) {
                        distanceBefore = distance;
                        newFrom = till;
                    }

                    distance = from - startStation;
                    if ((distance < distanceBehind) && (distance >= 0)) {
                        distanceBehind = distance;
                        newTo = from;
                    }
                }
            }

            try {
                addNewMember(newFrom, newTo);
            } catch (Exception e) {
                LOG.error("error while creating new station.", e);
            }
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @param  startStation  DOCUMENT ME!
     * @param  endStation    minStart DOCUMENT ME!
     */
    private void addSpecifiedMember(final Double startStation,
            final Double endStation) {
        try {
            addNewMember(startStation, endStation);
        } catch (Exception e) {
            LOG.error("error while creating new station.", e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   start  beanBefore DOCUMENT ME!
     * @param   end    beanBehind DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void addNewMember(final Double start, final Double end) throws Exception {
        final SimpleModifiableBandMember m = createNewBandMember(start, end);
        members.add(m);

        fireBandChanged(new BandEvent());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   start  DOCUMENT ME!
     * @param   end    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected SimpleModifiableBandMember createNewBandMember(final Double start, final Double end) {
        final SimpleModifiableBandMember member = new SimpleModifiableBandMember(this, readOnly, start, end);
        member.addBandMemberListener(this);

        return member;
    }

    @Override
    public void addBandListener(final BandListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeBandListener(final BandListener listener) {
        listenerList.remove(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public void fireBandChanged(final BandEvent e) {
        for (final BandListener l : listenerList) {
            l.bandChanged(e);
        }
    }

    @Override
    public void bandMemberChanged(final BandMemberEvent e) {
        final BandEvent ev = new BandEvent();
        if ((e != null)) {
            if (e.isSelectionLost()) {
                ev.setSelectionLost(true);
            }
            ev.setModelChanged(e.isModelChanged());
        }
        fireBandChanged(ev);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  member  DOCUMENT ME!
     */
    public void deleteMember(final SimpleModifiableBandMember member) {
        members.remove(member);

        final BandEvent e = new BandEvent();
        e.setSelectionLost(true);
        fireBandChanged(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  min  DOCUMENT ME!
     */
    @Override
    public void setMin(final Double min) {
        this.fixMin = min;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  max  DOCUMENT ME!
     */
    @Override
    public void setMax(final Double max) {
        this.fixMax = max;
    }

    @Override
    public double getMin() {
        if (fixMin != null) {
            return fixMin;
        } else {
            return super.getMin();
        }
    }

    @Override
    public double getMax() {
        if (fixMax != null) {
            return fixMax;
        } else {
            return super.getMax();
        }
    }
}
