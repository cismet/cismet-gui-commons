/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.security;

import net.environmatics.acs.accessor.WSSAccessorDeegree;
import net.environmatics.acs.accessor.interfaces.AuthenticationMethod;
import net.environmatics.acs.accessor.methods.PasswordAuthenticationMethod;
import net.environmatics.acs.exceptions.AuthenticationFailedException;

import org.apache.commons.httpclient.UsernamePasswordCredentials;

import java.awt.Component;

import java.io.BufferedReader;
import java.io.StringReader;

import java.net.URL;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public class WSSPasswordDialog extends PasswordDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WSSPasswordDialog.class);

    //~ Instance fields --------------------------------------------------------

    private final WSSAccessorDeegree wssac;
    private String sInfo;
    private String subParent;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WSSPasswordDialog object.
     *
     * @param  url              DOCUMENT ME!
     * @param  parentComponent  DOCUMENT ME!
     * @param  subParent        DOCUMENT ME!
     */
    public WSSPasswordDialog(final URL url, final Component parentComponent, final String subParent) {
        super(url, parentComponent);
        wssac = new WSSAccessorDeegree();
        // ToDo check if url is valid;
        wssac.setWSS(url.toString());
        this.subParent = subParent;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean authenticate(final String name, final char[] password, final String server) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Authentication with username: " + name); // NOI18N
        }

        try {
            final AuthenticationMethod authMethod = new PasswordAuthenticationMethod(name + "," + new String(password)); // NOI18N
            sInfo = wssac.getSession(authMethod).getSessionID();
            // smPanel.setCredentials(sInfo);
            if (log.isDebugEnabled()) {
                log.debug("Authentication successful for WSS " + url.toString() + " New SesionID:" + sInfo); // NOI18N
            }
            usernames.removeUserName(name);
            usernames.saveUserNames();
            usernames.addUserName(name);
            usernames.saveUserNames();
            isAuthenticationDone = true;
            setUsernamePassword(new UsernamePasswordCredentials(name, new String(password)));
            return true;
        } catch (AuthenticationFailedException ex) {
            log.error("Authentication failed for WSS: " + url.toString(), ex);                               // NOI18N
            return false;
        }
    }

//    public AbstractCapabilitiesTreeModel getCapabilitiesTree() {
//        try {
//            URL postURL;
//            AbstractCapabilitiesTreeModel capTreeModel;
//            // WFSCapabilities aus dem \u00FCbergebenen Link (liefert XML-Dok) parsen
//            if (true) {
//                final WFSOperator op = new WFSOperator();
//                final WFSCapabilities cap = op.parseWFSCapabilites(new BufferedReader(new StringReader(wssac.doService(WSSAccessorDeegree.DCP_HTTP_GET,"?REQUEST=GetCapabilities&service=WFS", url.toString()).asText())));
//
//                // Hashmap mit den FeatureLayer-Attributen erzeugen
//                log.debug("Erzeuge WFSCapabilitiesTreeModel");
//                capTreeModel = new WFSCapabilitiesTreeModel(cap);
//                ((WFSCapabilitiesTreeModel)capTreeModel).setFeatureTypes(op.getElements(url, cap.getFeatureTypeList()));
//                return capTreeModel;
//            } else if (false) {
//                OGCWMSCapabilitiesFactory capFact = new OGCWMSCapabilitiesFactory();
//                CismapBroker broker = CismapBroker.getInstance();
//                log.debug("Capability Widget: Creating WMScapabilities for URL: " + url.toString());
//                final WMSCapabilities cap = capFact.createCapabilities(new BufferedReader(new StringReader(wssac.doService(WSSAccessorDeegree.DCP_HTTP_GET,"?REQUEST=GetCapabilities&service=WMS", url.toString()).asText())));
//                broker.addHttpCredentialProviderCapabilities(cap, broker.getHttpCredentialProviderURL(url));
//                capTreeModel = new WMSCapabilitiesTreeModel(cap, subParent);
//                return capTreeModel;
//            } else {
//                log.warn("Unkown Service Type no WFS/WMS");
//                return null;
//            }
//
//        } catch (Exception ex) {
//            log.error("Failure during building CapabilitiesTrees", ex);
//            return null;
//        }
//    }
}
