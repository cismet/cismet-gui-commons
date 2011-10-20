/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands;

import javax.swing.JComponent;

import de.cismet.tools.gui.jbands.interfaces.Section;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class SimpleSection implements Section {

    //~ Instance fields --------------------------------------------------------

    private double from = 0;
    private double to = 0;

    private SimpleSectionPanel bmc;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SimpleSection object.
     */
    public SimpleSection() {
        bmc = new SimpleSectionPanel();
    }

    /**
     * Creates a new SimpleSection object.
     *
     * @param  from  DOCUMENT ME!
     * @param  to    DOCUMENT ME!
     */
    public SimpleSection(final double from, final double to) {
        this();
        this.from = from;
        this.to = to;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public double getFrom() {
        return from;
    }

    @Override
    public double getTo() {
        return to;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  from  DOCUMENT ME!
     */
    public void setFrom(final double from) {
        this.from = from;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  to  DOCUMENT ME!
     */
    public void setTo(final double to) {
        this.to = to;
    }

    @Override
    public double getMax() {
        return (from < to) ? to : from;
    }

    @Override
    public double getMin() {
        return (from < to) ? from : to;
    }

    @Override
    public JComponent getBandMemberComponent() {
        return bmc;
    }
}
