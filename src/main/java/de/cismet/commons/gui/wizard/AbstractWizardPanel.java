/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.wizard;

import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

import java.awt.Component;

import java.util.concurrent.locks.ReentrantLock;

import javax.swing.event.ChangeListener;

/**
 * Basic wizard panel implementation to eliminate the tedious task of creating Panel implementations.
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public abstract class AbstractWizardPanel implements WizardDescriptor.Panel {

    //~ Instance fields --------------------------------------------------------

    protected final transient ChangeSupport changeSupport;
    protected transient WizardDescriptor wizard;

    private transient volatile Component component;

    private final transient ReentrantLock componentLock;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractWizardPanel object.
     */
    public AbstractWizardPanel() {
        this.changeSupport = new ChangeSupport(this);
        this.componentLock = new ReentrantLock(false);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getComponent() {
        componentLock.lock();
        try {
            if (component == null) {
                component = createComponent();
            }

            return component;
        } finally {
            componentLock.unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected abstract Component createComponent();

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void readSettings(final Object settings) {
        this.wizard = (WizardDescriptor)settings;
        this.read(wizard);
    }

    @Override
    public void storeSettings(final Object settings) {
        this.wizard = (WizardDescriptor)settings;
        this.store(wizard);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  wizard  DOCUMENT ME!
     */
    protected abstract void read(final WizardDescriptor wizard);

    /**
     * DOCUMENT ME!
     *
     * @param  wizard  DOCUMENT ME!
     */
    protected abstract void store(final WizardDescriptor wizard);

    @Override
    public void addChangeListener(final ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(final ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
