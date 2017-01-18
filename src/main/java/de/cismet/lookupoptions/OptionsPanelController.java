/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.lookupoptions;

import javax.swing.JPanel;

import de.cismet.tools.configuration.Configurable;

/**
 * This class represents one panel in Options Dialog.
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface OptionsPanelController extends Comparable<OptionsPanelController>, Configurable {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the category of this options panel
     */
    Class<? extends OptionsCategory> getCategoryClass();

    /**
     * DOCUMENT ME!
     *
     * @return  the name of this options panel
     */
    String getName();

    /**
     * DOCUMENT ME!
     *
     * @return  the order-value of this options panel
     */
    int getOrder();

    /**
     * DOCUMENT ME!
     *
     * @return  the tooltip for this options panel
     */
    String getTooltip();

    /**
     * DOCUMENT ME!
     *
     * @return  the help page (string containing html) for this options panel
     */
    String getHelp();

    /**
     * DOCUMENT ME!
     *
     * @return  the panel representing this Options
     */
    JPanel getPanel();

    /**
     * This method is called when Options Dialog "OK" button is pressed.
     */
    void applyChanges();

    /**
     * Component should load its data here.
     */
    void update();

    /**
     * This method is called when Options Dialog "Cancel" button is pressed.
     */
    void cancel();

    /**
     * Should return true if some option value has been changed.
     *
     * @return  true if some option value has been changed
     */
    boolean isChanged();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isEnabled();
}
