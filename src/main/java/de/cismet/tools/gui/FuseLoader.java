/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import org.jdesktop.fuse.ResourceInjector;

import java.util.Properties;

/**
 * DOCUMENT ME!
 *
 * @author   nhaffke
 * @version  $Revision$, $Date$
 */
public final class FuseLoader {

    //~ Static fields/initializers ---------------------------------------------

    private static FuseLoader instance;

    //~ Instance fields --------------------------------------------------------

    private Properties properties;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FuseLoader object.
     */
    private FuseLoader() {
        instance = this;
        ResourceInjector.addModule("org.jdesktop.fuse.swing.SwingModule");                               // NOI18N
        try {
            ResourceInjector.get("coolpanel.style")
                    .load(getClass().getResource("/coolobjectrenderer/style.properties"));               // NOI18N
        } catch (Exception e) {
        }
        try {
            ResourceInjector.get("purecoolpanel.style")
                    .load(getClass().getResource("/de/cismet/tools/gui/purecoolpanelstyle.properties")); // NOI18N
        } catch (Exception e) {
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public static void load() {
        if (instance == null) {
            instance = new FuseLoader();
        }
    }
}
