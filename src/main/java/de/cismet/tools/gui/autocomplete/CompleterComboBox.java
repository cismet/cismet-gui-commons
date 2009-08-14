package de.cismet.tools.gui.autocomplete;

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * An editable combo class that will autocomplete the user entered text to the entries
 * in the combo drop down. 
 * 
 * You can directly add auto-complete to existing JComboBox derived classes
 * using:
 * ComboCompleterFilter.addCompletion(yourCombo);
 * 
 */
public class CompleterComboBox extends JComboBox {

    public CompleterComboBox(ComboBoxModel aModel) {
        super(aModel);
        // TODO Auto-generated constructor stub
    }

    public CompleterComboBox(Object[] items) {
        super(items);
        init();
    }

    public CompleterComboBox(Vector<?> items) {
        super(items);
        // TODO Auto-generated constructor stub
    }

    public CompleterComboBox() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void init() {
        setEditable(true);

        filter = ComboCompleterFilter.addCompletionMechanism(this);
    }

    public boolean isCaseSensitive() {
        return filter.isCaseSensitive();
    }

    public boolean isCorrectingCase() {
        return filter.isCorrectingCase();
    }

    public void setCaseSensitive(boolean caseSensitive) {
        filter.setCaseSensitive(caseSensitive);
    }

    public boolean isStrict() {
        return filter.isStrict();
    }

    public void setStrict(boolean strict) {
        filter.setStrict(strict);
    }

    public void setCorrectCase(boolean correctCase) {
        filter.setCorrectCase(correctCase);
    }
    private ComboCompleterFilter filter;
}
