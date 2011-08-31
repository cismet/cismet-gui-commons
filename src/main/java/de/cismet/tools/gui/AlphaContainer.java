/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * A wrapper Container for holding components that use a background Color containing an alpha value with some
 * transparency.
 *
 * <p>A Component that uses a transparent background should really have its opaque property set to false so that the
 * area it occupies is first painted by its opaque ancestor (to make sure no painting artifacts exist). However, if the
 * property is set to false, then most Swing components will not paint the background at all, so you lose the
 * transparent background Color.</p>
 *
 * <p>This components attempts to get around this problem by doing the background painting on behalf of its contained
 * Component, using the background Color of the Component.</p>
 *
 * <p>For more details see http://tips4java.wordpress.com/2009/05/31/backgrounds-with-transparency/</p>
 *
 * @version  $Revision$, $Date$
 */
public class AlphaContainer extends JComponent {

    //~ Instance fields --------------------------------------------------------

    private JComponent component;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AlphaContainer object.
     *
     * @param  component  DOCUMENT ME!
     */
    public AlphaContainer(final JComponent component) {
        this.component = component;
        setLayout(new BorderLayout());
        setOpaque(false);
        component.setOpaque(false);
        add(component);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Paint the background using the background Color of the contained component.
     *
     * @param  g  DOCUMENT ME!
     */
    @Override
    public void paintComponent(final Graphics g) {
        g.setColor(component.getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
