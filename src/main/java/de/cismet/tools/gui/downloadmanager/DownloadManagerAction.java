/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.downloadmanager;

import org.apache.log4j.Logger;

import org.openide.util.NbBundle;

import java.awt.Component;
import java.awt.event.ActionEvent;

import java.net.URL;

import java.util.MissingResourceException;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * This action is responsible for the the steps to be done when the user wants to see the current downloads.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DownloadManagerAction extends AbstractAction {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DownloadManagerAction.class);

    //~ Instance fields --------------------------------------------------------

    private Component parent;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DownloadManagerAction object.
     *
     * @param  parent  DOCUMENT ME!
     */
    public DownloadManagerAction(final Component parent) {
        super();

        this.parent = parent;

        final URL icon = getClass().getResource("/de/cismet/tools/gui/downloadmanager/res/downloadmanager.png");
        String name = "Download-Manager";
        String tooltiptext = "Zeigt die Downloads an";
        String command = "cmdDownloads";

        try {
            name = NbBundle.getMessage(DownloadManagerAction.class, "DownloadManagerAction.name");
            tooltiptext = NbBundle.getMessage(
                    DownloadManagerAction.class,
                    "DownloadManagerAction.tooltiptext");
            command = NbBundle.getMessage(
                    DownloadManagerAction.class,
                    "DownloadManagerAction.actionCommandKey");
        } catch (MissingResourceException e) {
            LOG.error("Couldn't find resources. Using fallback settings.", e);
        }

        if (icon != null) {
            putValue(SMALL_ICON, new javax.swing.ImageIcon(icon));
        }

        putValue(SHORT_DESCRIPTION, tooltiptext);
        putValue(ACTION_COMMAND_KEY, command);
        putValue(NAME, name);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        final JDialog downloadManager = DownloadManagerDialog.instance(parent);
        downloadManager.pack();
        StaticSwingTools.showDialog(downloadManager);
    }
}
