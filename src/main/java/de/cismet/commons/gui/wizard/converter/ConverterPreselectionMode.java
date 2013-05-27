/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.wizard.converter;

/**
 * Several possible modes that a component may allow for converter preselection, e.g. a wizard may be able to skip the
 * converter chooser if a specific mode is enabled, etc.
 *
 * @author   mscholl
 * @version  1.0
 */
public enum ConverterPreselectionMode {

    //~ Enum constants ---------------------------------------------------------

    AUTO_DETECT, CONFIGURE, CONFIGURE_AND_MEMORY, DEFAULT, PERMANENT_MEMORY, SESSION_MEMORY
}
