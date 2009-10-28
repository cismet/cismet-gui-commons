package de.cismet.lookupoptions;

import de.cismet.tools.configuration.Configurable;
import javax.swing.JPanel;

/**
 * This class represents one panel in Options Dialog.
 * @author jruiz
 */
public interface OptionsPanelController extends Comparable<OptionsPanelController>, Configurable {

    /**
     * @return the category of this options panel
     */
    public Class<? extends OptionsCategory> getCategoryClass();

    /**
     * @return the name of this options panel
     */
    public String getName();

    /**
     * @return the order-value of this options panel
     */
    public int getOrder();

    /**
     * @return the tooltip for this options panel
     */
    public String getTooltip();

    /**
     * @return the help page (string containing html) for this options panel
     */
    public String getHelp();

    /**
     * @return the panel representing this Options
     */
    public JPanel getPanel();

    /**
     * This method is called when Options Dialog "OK" button is pressed.
     */
    public void applyChanges();

    /**
     * Component should load its data here.
     */
    public void update();

    /**
     * This method is called when Options Dialog "Cancel" button is pressed.
     */
    public void cancel();

    /**
     * Should return true if some option value has been changed.
     * @return true if some option value has been changed
     */
    public boolean isChanged();


}
