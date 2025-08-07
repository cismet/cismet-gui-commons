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

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class ContinueOrExitDialogActionEvent {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum Action {

        //~ Enum constants -----------------------------------------------------

        EXIT_ACTION, CONTINUE_ACTION
    }

    //~ Instance fields --------------------------------------------------------

    private final ContinueOrExitDialog source;
    private final Action action;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ContinueOrExitDialogActionEvent object.
     *
     * @param  source  DOCUMENT ME!
     * @param  action  DOCUMENT ME!
     */
    public ContinueOrExitDialogActionEvent(final ContinueOrExitDialog source, final Action action) {
        this.source = source;
        this.action = action;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ContinueOrExitDialog getSource() {
        return source;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Action getAction() {
        return action;
    }
}
