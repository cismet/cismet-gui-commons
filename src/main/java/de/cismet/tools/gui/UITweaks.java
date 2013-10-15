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
package de.cismet.tools.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.awt.Color;

import java.util.HashMap;

import javax.swing.UIManager;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@XmlRootElement
public class UITweaks {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UITweaks.class);

    //~ Instance fields --------------------------------------------------------

    private HashMap<String, String> colors;
    private HashMap<String, Boolean> booleans;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void apply() {
        if (colors != null) {
            for (final String key : colors.keySet()) {
                try {
                    UIManager.put(key, Color.decode(colors.get(key)));
                } catch (Exception exception) {
                    LOG.warn("cannot put" + key + " to UIManager with " + colors.get(key), exception);
                }
            }
        }
        if (booleans != null) {
            for (final String key : booleans.keySet()) {
                try {
                    UIManager.put(key, booleans.get(key));
                } catch (Exception exception) {
                    LOG.warn("cannot put" + key + " to UIManager with " + booleans.get(key), exception);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public HashMap<String, String> getColors() {
        return colors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  colors  DOCUMENT ME!
     */
    public void setColors(final HashMap<String, String> colors) {
        this.colors = colors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public HashMap<String, Boolean> getBooleans() {
        return booleans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  booleans  DOCUMENT ME!
     */
    public void setBooleans(final HashMap<String, Boolean> booleans) {
        this.booleans = booleans;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        final HashMap<String, String> colors = new HashMap<String, String>();
        colors.put("Table.selectionBackground", "#c3d4e8");
        colors.put("Tree.selectionBackground", "#c3d4e8");
        colors.put("Table.selectionForeground", "#000000");
        colors.put("Tree.selectionForeground", "#000000");
        final UITweaks uit = new UITweaks();
        uit.setColors(colors);
        System.out.println(mapper.writeValueAsString(uit));
    }
}
