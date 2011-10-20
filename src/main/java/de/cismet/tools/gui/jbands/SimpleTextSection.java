/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands;

import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class SimpleTextSection extends SimpleSection {

    //~ Instance fields --------------------------------------------------------

    protected String title;
    protected TextSectionComponent comp;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SimpleTextSection object.
     *
     * @param  title  DOCUMENT ME!
     * @param  from   DOCUMENT ME!
     * @param  to     DOCUMENT ME!
     */
    public SimpleTextSection(final String title, final double from, final double to) {
        this(title, from, to, false, false);
    }

    /**
     * Creates a new SimpleTextSection object.
     *
     * @param  title      DOCUMENT ME!
     * @param  from       DOCUMENT ME!
     * @param  to         DOCUMENT ME!
     * @param  openLeft   DOCUMENT ME!
     * @param  openRight  DOCUMENT ME!
     */
    public SimpleTextSection(final String title,
            final double from,
            final double to,
            final boolean openLeft,
            final boolean openRight) {
        super(from, to);
        this.title = title;
        comp = new TextSectionComponent(title, openLeft, openRight);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public JComponent getBandMemberComponent() {
        return comp;
    }
}
