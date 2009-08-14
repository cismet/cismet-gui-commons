package de.cismet.tools.gui;

import java.util.Properties;
import org.jdesktop.fuse.ResourceInjector;

/**
 * @author nhaffke
 */
public final class FuseLoader {

    private static FuseLoader instance;
    private Properties properties;

    private FuseLoader() {
        instance = this;
        ResourceInjector.addModule("org.jdesktop.fuse.swing.SwingModule");
        try {
            ResourceInjector.get("coolpanel.style").load(getClass().getResource("/coolobjectrenderer/style.properties"));
        } catch (Exception e) {
        }
        try {
            ResourceInjector.get("purecoolpanel.style").load(getClass().getResource("/de/cismet/tools/gui/purecoolpanelstyle.properties"));
        } catch (Exception e) {
        }
    }

    public static void load() {
        if (instance == null) {
            instance = new FuseLoader();
        }
    }
}