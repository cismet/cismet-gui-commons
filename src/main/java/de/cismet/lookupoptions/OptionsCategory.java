package de.cismet.lookupoptions;

import javax.swing.Icon;

/**
 * This class represents one category in Options Dialog.
 * @author jruiz
 */
public interface OptionsCategory extends Comparable<OptionsCategory> {

    /**
     * Returns 32x32 icon used in list on the top of Options Dialog.
     * @return 32x32 icon
     */
    public Icon getIcon();

    /**
     * Returns name of category used in list on the top side of Options Dialog.
     * @return name of category
     */
    public String getName();

    /**
     * Return the relative order of category in the Options Dialog.
     * @return relative order
     */
    public int getOrder();

}
