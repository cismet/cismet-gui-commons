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

import javax.swing.JDialog;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DialogOpenedEvent {

    //~ Instance fields --------------------------------------------------------

    private JDialog dialog;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DialogOpenedEvent object.
     *
     * @param  dialog  DOCUMENT ME!
     */
    public DialogOpenedEvent(final JDialog dialog) {
        this.dialog = dialog;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the dialog
     */
    public JDialog getDialog() {
        return dialog;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  dialog  the dialog to set
     */
    public void setDialog(final JDialog dialog) {
        this.dialog = dialog;
    }
}
