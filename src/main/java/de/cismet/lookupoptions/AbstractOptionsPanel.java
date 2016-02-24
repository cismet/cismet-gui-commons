/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.lookupoptions;

import org.jdom.Element;

import javax.swing.JPanel;

import de.cismet.lookupoptions.options.DefaultOptionsCategory;

import de.cismet.tools.configuration.NoWriteError;

/**
 * The base-class for Options Panels.
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class AbstractOptionsPanel extends JPanel implements OptionsPanelController {

    //~ Instance fields --------------------------------------------------------

    private Class<? extends AbstractOptionsCategory> categoryClass;
    private String name;

    //~ Constructors -----------------------------------------------------------

    /**
     * constructor.
     *
     * @param  name  DOCUMENT ME!
     */
    public AbstractOptionsPanel(final String name) {
        this(name, DefaultOptionsCategory.class);
    }

    /**
     * constructor if the category is null, then it is set to CATEGORY_GENERAL.
     *
     * @param  name           DOCUMENT ME!
     * @param  categoryClass  DOCUMENT ME!
     */
    public AbstractOptionsPanel(final String name, final Class<? extends AbstractOptionsCategory> categoryClass) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name;
        }
        this.categoryClass = categoryClass;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the category of the lookup panel
     */
    @Override
    public Class<? extends OptionsCategory> getCategoryClass() {
        return categoryClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the name of the lookup panel
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  Integer.MAX_Value by default
     */
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  no tooltip by default
     */
    @Override
    public String getTooltip() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  no help by default
     */
    @Override
    public String getHelp() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the panel
     */
    @Override
    public JPanel getPanel() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final OptionsPanelController o) {
        final int orderCompare = new Integer(getOrder()).compareTo(o.getOrder());
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
    public void applyChanges() {
    }

    @Override
    public void update() {
    }

    @Override
    public void configure(final Element parent) {
    }

    @Override
    public Element getConfiguration() throws NoWriteError {
        return null;
    }

    @Override
    public void masterConfigure(final Element parent) {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
