package de.cismet.lookupoptions;

import javax.swing.Icon;

/**
 * This class provides a skeletal implementation of the OptionsCategory
 * interface to minimize the effort required to implement this interface.
 * @author jruiz
 */
public abstract class AbstractOptionsCategory implements OptionsCategory {

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public abstract String getName();

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int compareTo(final OptionsCategory o) {
        int orderCompare = getOrder() - o.getOrder();
        if (orderCompare == 0) {
            return getName().compareTo(o.getName());
        } else {
            return orderCompare;
        }
    }

}
