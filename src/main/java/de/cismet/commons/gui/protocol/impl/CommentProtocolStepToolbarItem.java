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
package de.cismet.commons.gui.protocol.impl;

import org.openide.util.lookup.ServiceProvider;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import de.cismet.commons.gui.protocol.AbstractProtocolStepToolbarItemAction;
import de.cismet.commons.gui.protocol.ProtocolStepToolbarItem;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = ProtocolStepToolbarItem.class)
public class CommentProtocolStepToolbarItem extends AbstractProtocolStepToolbarItemAction {

    //~ Static fields/initializers ---------------------------------------------

    private static final String NAME = "";
    private static final String TOOLTIP = "";
    private static final ImageIcon ICON = new ImageIcon(CommentProtocolStepToolbarItem.class.getResource(
                "/de/cismet/commons/gui/protocol/impl/comment.png"));

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CommentProtocolStepToolbarItem object.
     */
    public CommentProtocolStepToolbarItem() {
        super(NAME, TOOLTIP, null, ICON);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getSorterString() {
        return "ZZZ";
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        StaticSwingTools.showDialog(new AddCommentProtocolStepDialog(null, true));
    }
}
