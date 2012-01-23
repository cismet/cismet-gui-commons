/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * An extension of JCheckBoxMenuItem that doesn't close the menu when selected.
 *
 * @author   Darryl
 * @author   jweintraut Erweiterung für Popup-Menüs
 * @version  $Revision$, $Date$
 * @url      http://tips4java.wordpress.com/2010/09/12/keeping-menus-open/
 */
public class StayOpenCheckBoxMenuItem extends JCheckBoxMenuItem {

    //~ Static fields/initializers ---------------------------------------------

    private static MenuElement[] path;
    private static JCheckBoxMenuItem bsp = new JCheckBoxMenuItem("white");

    //~ Instance fields --------------------------------------------------------

    private Color selectedBackgroundColor;
    private Color selectedForegroundColor;
    private Color normalBackgroundColor;
    private Color normalForegroundColor;

    //~ Instance initializers --------------------------------------------------

    {
        getModel().addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    if (getModel().isArmed() && isShowing()) {
                        path = MenuSelectionManager.defaultManager().getSelectedPath();
                    }
                }
            });
    }

    //~ Constructors -----------------------------------------------------------

    /**
     * @see  JCheckBoxMenuItem#JCheckBoxMenuItem()
     */
    public StayOpenCheckBoxMenuItem() {
        super();
    }

    /**
     * @see  JCheckBoxMenuItem#JCheckBoxMenuItem(Action)
     */
    public StayOpenCheckBoxMenuItem(final Action a) {
        super(a);
    }

    /**
     * @see  JCheckBoxMenuItem#JCheckBoxMenuItem(Icon)
     */
    public StayOpenCheckBoxMenuItem(final Icon icon) {
        super(icon);
    }

    /**
     * @see  JCheckBoxMenuItem#JCheckBoxMenuItem(String)
     */
    public StayOpenCheckBoxMenuItem(final String text) {
        super(text);
    }

    /**
     * @see  JCheckBoxMenuItem#JCheckBoxMenuItem(String, boolean)
     */
    public StayOpenCheckBoxMenuItem(final String text, final boolean selected) {
        super(text, selected);
    }

    /**
     * @see  JCheckBoxMenuItem#JCheckBoxMenuItem(String, Icon)
     */
    public StayOpenCheckBoxMenuItem(final String text, final Icon icon) {
        super(text, icon);
    }

    /**
     * Creates a new StayOpenCheckBoxMenuItem object.
     *
     * @param  action                   DOCUMENT ME!
     * @param  selectedBackgroundColor  DOCUMENT ME!
     * @param  selectedForegroundColor  DOCUMENT ME!
     */
    public StayOpenCheckBoxMenuItem(final Action action,
            final Color selectedBackgroundColor,
            final Color selectedForegroundColor) {
        this(action);
        this.selectedBackgroundColor = selectedBackgroundColor;
        this.selectedForegroundColor = selectedForegroundColor;
        normalBackgroundColor = bsp.getBackground();
        normalForegroundColor = bsp.getForeground();
    }

    /**
     * @see  JCheckBoxMenuItem#JCheckBoxMenuItem(String, Icon, boolean)
     */
    public StayOpenCheckBoxMenuItem(final String text, final Icon icon, final boolean selected) {
        super(text, icon, selected);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Overridden to reopen the menu.
     *
     * @param  pressTime  the time to "hold down" the button, in milliseconds
     */
    @Override
    public void doClick(final int pressTime) {
        super.doClick(pressTime);

        if ((path != null) && (path.length > 1) && (path[0] instanceof JPopupMenu)) {
            final JPopupMenu menu = (JPopupMenu)path[0];
            menu.setVisible(true);
        }

        MenuSelectionManager.defaultManager().setSelectedPath(path);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Color getBackground() {
        if (isSelected()) {
            return selectedBackgroundColor;
        } else {
            return normalBackgroundColor;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Color getForeground() {
        if (isSelected()) {
            return selectedForegroundColor;
        } else {
            return normalForegroundColor;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Color getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectedBackgroundColor  DOCUMENT ME!
     */
    public void setSelectedBackgroundColor(final Color selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Color getSelectedForegroundColor() {
        return selectedForegroundColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectedForegroundColor  DOCUMENT ME!
     */
    public void setSelectedForegroundColor(final Color selectedForegroundColor) {
        this.selectedForegroundColor = selectedForegroundColor;
    }
}
