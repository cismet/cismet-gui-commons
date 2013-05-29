/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.l10n;

import java.util.ResourceBundle;

/**
 * Marks implementing classes as localizable meaning that they may be provided with resources programmatically.
 * Implementing classes may or may not directly reflect changes. They are not obliged to do so.
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public interface Localizable {

    //~ Methods ----------------------------------------------------------------

    /**
     * The current (custom) resource bundle in use.
     *
     * @return  the current (custom) resource bundle in use.
     */
    ResourceBundle getResourceBundle();

    /**
     * Sets the new (custom) resource bundle that shall be used for L10N.
     *
     * @param  resourceBundle  the new (custom) resource bundle that shall be used for L10N
     */
    void setResourceBundle(final ResourceBundle resourceBundle);
}
