/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.exceptionnotification;

/**
 * DOCUMENT ME!
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
public interface DefaultExceptionHandlerListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  thread  DOCUMENT ME!
     * @param  error   DOCUMENT ME!
     */
    void uncaughtException(final Thread thread, final Throwable error);
}
