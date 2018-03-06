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
package de.cismet.lookupoptions.gui;

import de.cismet.connectioncontext.ClientConnectionContext;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class OptionsConnectionContext extends ClientConnectionContext {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RendererConnectionContext object.
     *
     * @param  name  DOCUMENT ME!
     */
    public OptionsConnectionContext(final String name) {
        super(Category.OPTIONS, name);
    }
}
