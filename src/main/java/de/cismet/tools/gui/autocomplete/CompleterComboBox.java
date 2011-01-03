/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.autocomplete;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * An editable combo class that will autocomplete the user entered text to the entries in the combo drop down.
 *
 * <p>You can directly add auto-complete to existing JComboBox derived classes using:
 * ComboCompleterFilter.addCompletion(yourCombo);</p>
 *
 * @version  $Revision$, $Date$
 */
public class CompleterComboBox extends JComboBox {

    //~ Instance fields --------------------------------------------------------

    private ComboCompleterFilter filter;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CompleterComboBox object.
     */
    public CompleterComboBox() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new CompleterComboBox object.
     *
     * @param  aModel  DOCUMENT ME!
     */
    public CompleterComboBox(final ComboBoxModel aModel) {
        super(aModel);
        // TODO Auto-generated constructor stub
    }

    /**
     * Creates a new CompleterComboBox object.
     *
     * @param  items  DOCUMENT ME!
     */
    public CompleterComboBox(final Object[] items) {
        super(items);
        init();
    }

    /**
     * Creates a new CompleterComboBox object.
     *
     * @param  items  DOCUMENT ME!
     */
    public CompleterComboBox(final Vector<?> items) {
        super(items);
        // TODO Auto-generated constructor stub
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void init() {
        setEditable(true);

        filter = ComboCompleterFilter.addCompletionMechanism(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isCaseSensitive() {
        return filter.isCaseSensitive();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isCorrectingCase() {
        return filter.isCorrectingCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  caseSensitive  DOCUMENT ME!
     */
    public void setCaseSensitive(final boolean caseSensitive) {
        filter.setCaseSensitive(caseSensitive);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isStrict() {
        return filter.isStrict();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  strict  DOCUMENT ME!
     */
    public void setStrict(final boolean strict) {
        filter.setStrict(strict);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  correctCase  DOCUMENT ME!
     */
    public void setCorrectCase(final boolean correctCase) {
        filter.setCorrectCase(correctCase);
    }
}
