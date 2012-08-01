/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.lookupoptions;

import javax.swing.Icon;

/**
 * This class represents one category in Options Dialog.
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface OptionsCategory extends Comparable<OptionsCategory> {

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns 32x32 icon used in list on the top of Options Dialog.
     *
     * @return  32x32 icon
     */
    Icon getIcon();

    /**
     * Returns name of category used in list on the top side of Options Dialog.
     *
     * @return  name of category
     */
    String getName();

    /**
     * Returns the relative order of category in the Options Dialog.
     *
     * @return  relative order
     */
    int getOrder();

    /**
     * Returns text for tooltip describing the category.
     *
     * @return  tooltip text
     */
    String getTooltip();
}
