package de.cismet.tools.gui.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractListModel;

/**
 * Class to hold the remaining objects that still match the users input.
 * @author ncochran
 *
 */
public class FilterListModel extends AbstractListModel {

    public FilterListModel(Object[] unfilteredList) {
        fullList = unfilteredList;
        filteredList = new ArrayList<Object>(Arrays.asList(unfilteredList));
    }

    @Override
    public int getSize() {
        return filteredList.size();
    }

    @Override
    public Object getElementAt(int index) {
        return filteredList.get(index);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        filteredList.clear();
        for (Object obj : fullList) {
            if (obj.toString().length() < filter.length()) {
                continue;
            }

            if (this.caseSensitive) {
                if (obj.toString().startsWith(filter)) {
                    filteredList.add(obj);
                }
            } else {
                if (obj.toString().substring(0, filter.length()).compareToIgnoreCase(filter) == 0) {
                    filteredList.add(obj);
                }
            }
        }
        fireContentsChanged(this, 0, filteredList.size());
    }

    public void clearFilter() {
        filter = null;
        filteredList = new ArrayList<Object>(Arrays.asList(fullList));
    }

    public boolean getCaseSensitive() {
        return this.caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        clearFilter();
    }

    public void setCompleterMatches(Object[] objectsToMatch) {
        fullList = objectsToMatch;
        clearFilter();
    }
    private Object[] fullList;
    private ArrayList<Object> filteredList;
    private String filter;
    private boolean caseSensitive = false;
}
