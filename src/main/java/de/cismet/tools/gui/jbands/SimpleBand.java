/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

import de.cismet.tools.gui.jbands.interfaces.BandPrefixProvider;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class SimpleBand extends PlainBand implements BandPrefixProvider {

    //~ Instance fields --------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected JLabel prefix = new JLabel();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SimpleBand object.
     */
    public SimpleBand() {
        this("");
    }

    /**
     * Creates a new SimpleBand object.
     *
     * @param  title  DOCUMENT ME!
     */
    public SimpleBand(final String title) {
        prefix.setOpaque(false);
        prefix.setText(title);
        prefix.setHorizontalAlignment(JLabel.RIGHT);
        prefix.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public JComponent getPrefixComponent() {
        return prefix;
    }
}
