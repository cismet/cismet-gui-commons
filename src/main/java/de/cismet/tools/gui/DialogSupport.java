/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DialogSupport {

    //~ Static fields/initializers ---------------------------------------------

    private static final List<DialogOpenedListener> DIALOG_OPENED_LISTENER = new ArrayList<>();

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    public static void addDialogListener(final DialogOpenedListener l) {
        DIALOG_OPENED_LISTENER.add(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   l  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static boolean removeDialogListener(final DialogOpenedListener l) {
        return DIALOG_OPENED_LISTENER.remove(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public static void fireNewDialogOpened(final DialogOpenedEvent e) {
        for (final DialogOpenedListener tmp : new ArrayList<>(DIALOG_OPENED_LISTENER)) {
            tmp.DialogOpened(e);
        }
    }
}
