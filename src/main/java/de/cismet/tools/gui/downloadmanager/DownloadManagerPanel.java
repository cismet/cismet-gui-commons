/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2011 jweintraut
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * DownloadManagerPanel.java
 *
 * Created on 27.04.2011, 13:22:46
 */
package de.cismet.tools.gui.downloadmanager;

import org.apache.log4j.Logger;

import java.awt.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JPanel;

/**
 * Visualizes the download list of DownloadManager. New downloads are dynamically added, completed ones are removed.
 * Erraneous downloads are marked red. This panel is informed of new, completed or erraneous downloads via the
 * DownloadListChangedListener interface by the download manager.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DownloadManagerPanel extends javax.swing.JPanel implements DownloadListChangedListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DownloadManagerPanel.class);

    //~ Instance fields --------------------------------------------------------

    private Map<Download, JPanel> panels = new HashMap<Download, JPanel>();
    private Component verticalGlue = Box.createVerticalGlue();

    //~ Constructors -----------------------------------------------------------

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Creates new form DownloadManagerPanel.
     */
    public DownloadManagerPanel() {
        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setBackground(java.awt.SystemColor.window);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));
    } // </editor-fold>//GEN-END:initComponents

    /**
     * Adds new downloads to display.
     *
     * @param  downloads  A collection of new downloads.
     */
    public synchronized void add(final Collection<Download> downloads) {
        if ((downloads == null) || (downloads.size() <= 0)) {
            return;
        }

        final LinkedList<JPanel> oldPanels = new LinkedList<JPanel>();
        for (final Component component : getComponents()) {
            if ((component instanceof DownloadPanel) || (component instanceof MultipleDownloadPanel)) {
                oldPanels.add((JPanel)component);
            }
        }

        removeAll();

        for (final Download download : downloads) {
            if (download instanceof MultipleDownload) {
                final MultipleDownloadPanel pnlDownload = new MultipleDownloadPanel((MultipleDownload)download);

                download.addObserver(pnlDownload);
                add(pnlDownload);
                panels.put(download, pnlDownload);
            } else {
                final DownloadPanel pnlDownload = new DownloadPanel(download);

                download.addObserver(pnlDownload);
                add(pnlDownload);
                panels.put(download, pnlDownload);
            }
        }

        for (final JPanel pnlDownload : oldPanels) {
            add(pnlDownload);
        }

        add(verticalGlue);

        revalidate();
        repaint();
    }

    /**
     * Removes the given downloads from the panel.
     *
     * @param  downloads  The downloads to remove.
     */
    protected synchronized void remove(final Collection<Download> downloads) {
        if ((downloads == null) || (downloads.size() <= 0)) {
            return;
        }

        remove(verticalGlue);

        for (final Download download : downloads) {
            final JPanel pnlDownload = panels.get(download);

            if (pnlDownload instanceof Observer) {
                download.deleteObserver((Observer)pnlDownload);
            }

            remove(pnlDownload);
            panels.remove(download);
        }

        add(verticalGlue);

        revalidate();
        repaint();
    }

    /**
     * Gets a collection of downloads and tells the panels of a MultipleDownload to redraw its encapsulated downloads.
     *
     * @param  downloads  DOCUMENT ME!
     */
    protected synchronized void addSubsequentDownloads(final Collection<Download> downloads) {
        if (downloads == null) {
            return;
        }
        for (final Download download : downloads) {
            final JPanel pnlDownload = panels.get(download);
            if (pnlDownload instanceof MultipleDownloadPanel) {
                final MultipleDownloadPanel mpnlDownload = (MultipleDownloadPanel)pnlDownload;
                mpnlDownload.redrawEncapsulatedDownloads();
            }
        }
    }

    @Override
    public synchronized void downloadListChanged(final DownloadListChangedEvent event) {
        final Collection<Download> downloads = event.getDownloads();

        switch (event.getAction()) {
            case ADDED: {
                add(downloads);
                break;
            }
            case REMOVED: {
                remove(downloads);
                break;
            }
            case ADDED_DOWNLOADS_SUBSEQUENTLY: {
                addSubsequentDownloads(downloads);
                break;
            }
        }
    }
}
