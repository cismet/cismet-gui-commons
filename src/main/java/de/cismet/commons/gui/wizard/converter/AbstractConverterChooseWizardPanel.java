/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.wizard.converter;

import org.openide.WizardDescriptor;
import org.openide.util.NbBundle;

import java.awt.Component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.List;
import java.util.ResourceBundle;

import de.cismet.commons.converter.Converter;

import de.cismet.commons.gui.l10n.Localizable;
import de.cismet.commons.gui.wizard.AbstractWizardPanel;

/**
 * Basic ConverterChooser panel implementation.
 *
 * @author   mscholl
 * @version  1.0
 */
public abstract class AbstractConverterChooseWizardPanel extends AbstractWizardPanel implements Localizable {

    //~ Static fields/initializers ---------------------------------------------

    public static final String PROP_CONVERTER = "__prop_converter__"; // NOI18N

    /** Special property without value for the wizard panel. used to indicate that this step has become active. */
    public static final String PROPERTY_INIT = "__property_init__";                                            // NOI18N

    //~ Instance fields --------------------------------------------------------

    private final transient PropertyChangeSupport propCSupport;

    private transient Converter converter;
    private transient ResourceBundle resourceBundle;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractConverterChooseWizardPanel object.
     */
    public AbstractConverterChooseWizardPanel() {
        propCSupport = new PropertyChangeSupport(this);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Component createComponent() {
        return new DefaultConverterChooseVisualPanel(this);
    }

    @Override
    protected void read(final WizardDescriptor wizard) {
        converter = (Converter)wizard.getProperty(PROP_CONVERTER);

        propCSupport.firePropertyChange(PROPERTY_INIT, null, null);

        wizard.putProperty(
            WizardDescriptor.PROP_INFO_MESSAGE,
            getText(
                "AbstractConverterChooseWizardPanel.read(WizardDescriptor).wizard.putProperty(String,String)")); // NOI18N
    }

    @Override
    protected void store(final WizardDescriptor wizard) {
        wizard.putProperty(PROP_CONVERTER, converter);
        wizard.putProperty(WizardDescriptor.PROP_INFO_MESSAGE, null);
    }

    @Override
    public boolean isValid() {
        return converter != null;
    }

    /**
     * Shall return a list of converters that the user may choose from.
     *
     * @return  a list of converters that the user may choose from, never <code>null</code>
     */
    public abstract List<Converter> getAvailableConverters();

    /**
     * Getter for the currently chosen converter.
     *
     * @return  the currently chosen converter
     */
    public Converter getConverter() {
        return converter;
    }

    /**
     * Sets the chosen converter.
     *
     * @param  converter  the chosen converter
     */
    public void setConverter(final Converter converter) {
        final Converter old = this.converter;
        this.converter = converter;

        changeSupport.fireChange();
        propCSupport.firePropertyChange("converter", old, converter); // NOI18N
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    @Override
    public void setResourceBundle(final ResourceBundle resourceBundle) {
        final ResourceBundle old = this.resourceBundle;

        this.resourceBundle = resourceBundle;

        propCSupport.firePropertyChange("resourceBundle", old, resourceBundle); // NOI18N
    }

    /**
     * Get for localised text for certain properties. This operation tries to use the <code>ResourceBundle</code> from
     * {@link #getResourceBundle()} to locate the value for the given property. If no custom bundle is set or if the
     * custom bundle does not contain a value for the property the return value is provided by
     * {@link NbBundle#getMessage(java.lang.Class, java.lang.String)}.
     *
     * @param   property  the property to get the localised value for
     *
     * @return  the localised value for the property
     */
    public String getText(final String property) {
        if (resourceBundle == null) {
            return NbBundle.getMessage(DefaultConverterChooseVisualPanel.class, property);
        } else {
            try {
                return resourceBundle.getString(property);
            } catch (final Exception e) {
                return NbBundle.getMessage(DefaultConverterChooseVisualPanel.class, property);
            }
        }
    }

    /**
     * @see  PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(final PropertyChangeListener pcl) {
        propCSupport.addPropertyChangeListener(pcl);
    }

    /**
     * @see  PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(final String property, final PropertyChangeListener pcl) {
        propCSupport.addPropertyChangeListener(property, pcl);
    }

    /**
     * @see  PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(final PropertyChangeListener pcl) {
        propCSupport.removePropertyChangeListener(pcl);
    }

    /**
     * @see  PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(final String property, final PropertyChangeListener pcl) {
        propCSupport.removePropertyChangeListener(property, pcl);
    }
}
