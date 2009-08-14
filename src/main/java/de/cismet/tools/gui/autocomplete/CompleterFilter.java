/*
 * CompleterFilter.java
 *
 * Created on October 27, 2005, 3:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.tools.gui.autocomplete;

import javax.swing.JTextField;

/**
 * A filter that will attempt to autocomplete enties into a textfield with the string representations
 * of objects in a given array.
 * 
 * Add this filter class to the Document of the text field.
 * 
 * The first match in the array is the one used to autocomplete. So sort your array by most important
 * objects first.
 * @author neilcochrane
 */
public class CompleterFilter extends AbstractCompleterFilter {

    /** Creates a new instance of CompleterFilter
     * @param completerObjs an array of objects used to attempt completion
     * @param textField the text component to receive the completion
     */
    public CompleterFilter(Object[] completerObjs, JTextField textField) {
        this.objectList = completerObjs;
        this.textField = textField;
    }

    @Override
    public int getCompleterListSize() {
        return this.objectList.length;
    }

    @Override
    public Object getCompleterObjectAt(int i) {
        return this.objectList[i];
    }

    @Override
    public JTextField getTextField() {
        return this.textField;
    }

    /**
     * Set the list of objects to match against.
     * @param objectsToMatch
     */
    public void setCompleterMatches(Object[] objectsToMatch) {
        this.objectList = objectsToMatch;
        this.firstSelectedIndex = -1;
    }
    protected JTextField textField;
    protected Object[] objectList;
}
