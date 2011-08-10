/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.downloadmanager;

import Sirius.navigator.resource.PropertyManager;

import org.apache.log4j.Logger;

import org.jdom.Element;

import java.io.File;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.EventListenerList;

import de.cismet.tools.configuration.Configurable;
import de.cismet.tools.configuration.NoWriteError;

/**
 * The download manager manages all current downloads. New downloads are added to a collection, completed downloads are
 * removed. Erraneous downloads remain in the collection. The download manager observes all download objects for state
 * changed and informs the download manager panel via the DownloadListChangedListener interface.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DownloadManager implements Observer, Configurable {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DownloadManager.class);
    private static final String XML_CONF_ROOT = "downloads";
    private static final String XML_CONF_DIRECTORY = "directory";
    private static final String XML_CONF_DIALOG = "dialog";
    private static final String XML_CONF_DIALOG_AKSFORTITLE = "askForTitle";
    private static final String XML_CONF_DIALOG_OPENAUTOMATICALLY = "openAutomatically";
    private static final String XML_CONF_DIALOG_USERTITLE = "userTitle";
    private static DownloadManager instance = null;

    //~ Instance fields --------------------------------------------------------

    private boolean enabled = true;
    private File destinationDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator")
                    + "cidsDownload");
    private LinkedList<Download> downloads = new LinkedList<Download>();
    private EventListenerList listeners = new EventListenerList();
    private int countDownloadsTotal = 0;
    private int countDownloadsRunning = 0;
    private int countDownloadsErraneous = 0;
    private int countDownloadsCompleted = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DownloadManager object.
     */
    private DownloadManager() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * It's a Singleton. There can only be one download manager.
     *
     * @return  The download manager.
     */
    public static DownloadManager instance() {
        if (instance == null) {
            instance = new DownloadManager();
        }

        return instance;
    }

    /**
     * This method is used to add new downloads to the download list.
     *
     * @param  download  wfss A collection of downloads to add.
     */
    public synchronized void add(final Download download) {
        if (download == null) {
            return;
        }

        this.downloads.add(download);
        countDownloadsTotal++;

        download.addObserver(this);

        notifyDownloadListChanged(new DownloadListChangedEvent(
                this,
                download,
                DownloadListChangedEvent.Action.ADDED));
        notifyDownloadListChanged(new DownloadListChangedEvent(
                this,
                download,
                DownloadListChangedEvent.Action.CHANGED_COUNTERS));

        download.startDownload();
    }

    /**
     * This method is used to add new downloads to the download list.
     *
     * @param  downloads  wfss A collection of downloads to add.
     */
    public synchronized void add(final Collection<Download> downloads) {
        if ((downloads == null) || (downloads.size() <= 0)) {
            return;
        }

        this.downloads.addAll(0, downloads);
        countDownloadsTotal += downloads.size();

        for (final Download download : downloads) {
            download.addObserver(this);
        }

        notifyDownloadListChanged(new DownloadListChangedEvent(
                this,
                downloads,
                DownloadListChangedEvent.Action.ADDED));
        notifyDownloadListChanged(new DownloadListChangedEvent(
                this,
                downloads,
                DownloadListChangedEvent.Action.CHANGED_COUNTERS));

        for (final Download download : downloads) {
            download.startDownload();
        }
    }

    /**
     * Removes oboslete downloads. Only running downloads aren't obsolote.
     */
    public synchronized void removeObsoleteDownloads() {
        final Collection<Download> downloadsRemoved = new LinkedList<Download>();

        for (final Download download : downloads) {
            if ((download.getStatus() == Download.State.COMPLETED)
                        || (download.getStatus() == Download.State.COMPLETED_WITH_ERROR)) {
                downloadsRemoved.add(download);
            }
        }

        if (downloadsRemoved.size() > 0) {
            for (final Download download : downloadsRemoved) {
                downloads.remove(download);
                countDownloadsTotal--;

                switch (download.getStatus()) {
                    case COMPLETED_WITH_ERROR: {
                        countDownloadsErraneous--;
                        break;
                    }
                    case COMPLETED: {
                        countDownloadsCompleted--;
                        break;
                    }
                }
                download.deleteObserver(this);
            }

            notifyDownloadListChanged(new DownloadListChangedEvent(
                    this,
                    downloadsRemoved,
                    DownloadListChangedEvent.Action.REMOVED));
            notifyDownloadListChanged(new DownloadListChangedEvent(
                    this,
                    downloadsRemoved,
                    DownloadListChangedEvent.Action.CHANGED_COUNTERS));
        }
    }

    /**
     * Remove a specified download from the download list.
     *
     * @param  download  The download to remove.
     */
    public synchronized void removeDownload(final Download download) {
        downloads.remove(download);
        countDownloadsTotal--;

        switch (download.getStatus()) {
            case COMPLETED_WITH_ERROR: {
                countDownloadsErraneous--;
                break;
            }
            case COMPLETED: {
                countDownloadsCompleted--;
                break;
            }
        }
        download.deleteObserver(this);

        notifyDownloadListChanged(new DownloadListChangedEvent(
                this,
                download,
                DownloadListChangedEvent.Action.REMOVED));
        notifyDownloadListChanged(new DownloadListChangedEvent(
                this,
                download,
                DownloadListChangedEvent.Action.CHANGED_COUNTERS));
    }

    /**
     * Returns the current download list.
     *
     * @return  The current download list.
     */
    public Collection<Download> getDownloads() {
        return downloads;
    }

    /**
     * Returns the count of erraneous downloads.
     *
     * @return  The count of erraneous downloads.
     */
    public int getCountDownloadsErraneous() {
        return countDownloadsErraneous;
    }

    /**
     * Returns the count of running downloads.
     *
     * @return  The count of running downloads.
     */
    public int getCountDownloadsRunning() {
        return countDownloadsRunning;
    }

    /**
     * Returns the count of completed downloads.
     *
     * @return  The count of completed downloads.
     */
    public int getCountDownloadsCompleted() {
        return countDownloadsCompleted;
    }

    /**
     * Returns the total count of downloads.
     *
     * @return  The total count of downloads.
     */
    public int getCountDownloadsTotal() {
        return countDownloadsTotal;
    }

    /**
     * Returns the destination directory.
     *
     * @return  The destination directory for downloads.
     */
    public File getDestinationDirectory() {
        return destinationDirectory;
    }

    /**
     * Sets the destination directory for downloads. Setting this does only affect new downloads. If the given download
     * location is invalid, the DownloadManager will be disabled.
     *
     * @param  destinationDirectory  The new destination directory for downloads.
     */
    public void setDestinationDirectory(final File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;

        if (!destinationDirectory.isDirectory() || !destinationDirectory.canWrite()) {
            LOG.error("The download manager can't use the directory '" + destinationDirectory.getAbsolutePath()
                        + "'. The download manager will be disabled.");
            enabled = false;
        } else {
            enabled = true;
        }
    }

    /**
     * Returns a flag which tells whether download manager is enabled or not.
     *
     * @return  The flag whether the download manager is enabled or not.
     */
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public synchronized void update(final Observable o, final Object arg) {
        if (!(o instanceof Download)) {
            return;
        }

        final Download download = (Download)o;

        switch (download.getStatus()) {
            case COMPLETED: {
                countDownloadsRunning--;
                countDownloadsCompleted++;
                break;
            }
            case COMPLETED_WITH_ERROR: {
                countDownloadsRunning--;
                countDownloadsErraneous++;
                break;
            }
            case RUNNING: {
                countDownloadsRunning++;
                break;
            }
        }

        notifyDownloadListChanged(new DownloadListChangedEvent(
                this,
                download,
                DownloadListChangedEvent.Action.CHANGED_COUNTERS));
    }

    /**
     * Adds a new DownloadListChangedListener.
     *
     * @param  listener  The listener to add.
     */
    public void addDownloadListChangedListener(final DownloadListChangedListener listener) {
        listeners.add(DownloadListChangedListener.class, listener);
    }

    /**
     * Removes a DownloadListChangedListener.
     *
     * @param  listener  The listener to remove.
     */
    public void removeDownloadListChangedListener(final DownloadListChangedListener listener) {
        listeners.remove(DownloadListChangedListener.class, listener);
    }

    /**
     * Notifies all current DownloadListChangedListeners.
     *
     * @param  event  The event to notify about.
     */
    protected synchronized void notifyDownloadListChanged(final DownloadListChangedEvent event) {
        for (final DownloadListChangedListener listener : listeners.getListeners(DownloadListChangedListener.class)) {
            listener.downloadListChanged(event);
        }
    }

    @Override
    public void configure(final Element parent) {
        DownloadManagerDialog.setAskForJobname(true);
        DownloadManagerDialog.setJobname("");
        DownloadManagerDialog.setOpenAutomatically(true);

        destinationDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator")
                        + "cidsDownload");

        final Element downloads = parent.getChild(XML_CONF_ROOT);
        if (downloads == null) {
            LOG.warn("The download manager isn't configured. Using default values.");

            if (!destinationDirectory.isDirectory() || !destinationDirectory.canWrite()) {
                LOG.error("The download manager can't use the directory '" + destinationDirectory.getAbsolutePath()
                            + "'. The download manager will be disabled.");
                enabled = false;
            } else {
                enabled = true;
            }

            return;
        }

        final Element directory = downloads.getChild(XML_CONF_DIRECTORY);
        if ((directory == null) || (directory.getTextTrim() == null)) {
            LOG.warn("There is no destination directory configured for downloads. Using default destination directory '"
                        + System.getProperty("user.home") + System.getProperty("file.separator") + "cidsDownload'.");
        } else {
            destinationDirectory = new File(directory.getTextTrim());
        }
        if (!destinationDirectory.isDirectory() || !destinationDirectory.canWrite()) {
            LOG.error("The download manager can't use the directory '" + destinationDirectory.getAbsolutePath()
                        + "'. The download manager will be disabled.");
            enabled = false;
        } else {
            enabled = true;
        }

        final Element dialog = downloads.getChild(XML_CONF_DIALOG);
        if (dialog == null) {
            LOG.warn("The download dialog isn't configured. Using default values.");
            return;
        }

        final Element askForTitle = dialog.getChild(XML_CONF_DIALOG_AKSFORTITLE);
        if ((askForTitle == null) || (askForTitle.getTextTrim() == null)) {
            LOG.warn(
                "There is no configuration whether to ask for download titles or not. Using default value 'true'.");
        } else {
            final String value = askForTitle.getTextTrim();
            DownloadManagerDialog.setAskForJobname("1".equals(value) || "true".equalsIgnoreCase(value));
        }

        final Element openAutomatically = dialog.getChild(XML_CONF_DIALOG_OPENAUTOMATICALLY);
        if ((openAutomatically == null) || (openAutomatically.getTextTrim() == null)) {
            LOG.warn(
                "There is no configuration whether to open downloads automatically or not. Using default value 'true'.");
        } else {
            final String value = openAutomatically.getTextTrim();
            DownloadManagerDialog.setOpenAutomatically("1".equals(value) || "true".equalsIgnoreCase(value));
        }

        final Element userTitle = dialog.getChild(XML_CONF_DIALOG_USERTITLE);
        if ((userTitle == null) || (userTitle.getTextTrim() == null)) {
            LOG.warn("There is no user title for downloads configured. Using default value 'cidsDownload'.");
        } else {
            DownloadManagerDialog.setJobname(userTitle.getTextTrim());
        }
    }

    @Override
    public void masterConfigure(final Element parent) {
        // NOP
    }

    @Override
    public Element getConfiguration() throws NoWriteError {
        final Element root = new Element(XML_CONF_ROOT);

        final Element directory = new Element(XML_CONF_DIRECTORY);
        directory.addContent(destinationDirectory.getAbsolutePath());

        final Element dialog = new Element(XML_CONF_DIALOG);

        final Element askForTitle = new Element(XML_CONF_DIALOG_AKSFORTITLE);
        askForTitle.addContent(DownloadManagerDialog.isAskForJobname() ? "true" : "false");

        final Element openAutomatically = new Element(XML_CONF_DIALOG_OPENAUTOMATICALLY);
        openAutomatically.addContent(DownloadManagerDialog.isOpenAutomatically() ? "true" : "false");

        final Element userTitle = new Element(XML_CONF_DIALOG_USERTITLE);
        userTitle.addContent(DownloadManagerDialog.getJobname());

        dialog.addContent(askForTitle);
        dialog.addContent(openAutomatically);
        dialog.addContent(userTitle);

        root.addContent(directory);
        root.addContent(dialog);

        return root;
    }
}
