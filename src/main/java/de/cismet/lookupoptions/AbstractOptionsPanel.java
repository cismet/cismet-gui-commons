package de.cismet.lookupoptions;

import de.cismet.lookupoptions.options.DefaultOptionsCategory;
import de.cismet.tools.configuration.NoWriteError;
import javax.swing.JPanel;
import org.jdom.Element;

/**
 * The base-class for Options Panels.
 * @author jruiz
 */
public abstract class AbstractOptionsPanel extends JPanel implements OptionsPanelController {

    private Class<? extends AbstractOptionsCategory> categoryClass;
    private String name;

    /**
     * constructor
     * @param name
     */
    public AbstractOptionsPanel(final String name) {
        this(name, DefaultOptionsCategory.class);
    }

    /**
     * constructor
     * if the category is null, then it is set to CATEGORY_GENERAL
     * @param name
     * @param category
     */
    public AbstractOptionsPanel(final String name, final Class<? extends AbstractOptionsCategory> categoryClass) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name;
        }
        this.categoryClass = categoryClass;
    }

    /**
     * @return the category of the lookup panel
     */
    @Override
    public Class<? extends OptionsCategory> getCategoryClass() {
        return categoryClass;
    }

    /**
     * @return the name of the lookup panel
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return Integer.MAX_Value by default
     */
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    /**
     * @return no tooltip by default
     */
    @Override
    public String getTooltip() {
        return null;
    }

    /**
     * @return no help by default
     */
    @Override
    public String getHelp() {
        return null;
    }

    /**
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public int compareTo(final OptionsPanelController o) {
        int orderCompare = getOrder() - o.getOrder();
        if (orderCompare == 0) {
            return getName().compareTo(o.getName());
        } else {
            return orderCompare;
        }
    }

    @Override
    public boolean isChanged() {
        return false;
    }

    @Override
    public void cancel() {
        update();
    }

    @Override
    public void applyChanges() { }

    @Override
    public void update() { }

    @Override
    public void configure(Element parent) { }

    @Override
    public Element getConfiguration() throws NoWriteError { return null; }

    @Override
    public void masterConfigure(Element parent) { }

}
