/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.downloadmanager;

import org.apache.log4j.Logger;

import org.jdom.Element;

import java.io.File;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.EventListenerList;

import de.cismet.tools.configuration.Configurable;
import de.cismet.tools.configuration.NoWriteError;

/**
 * The download manager manages all current downloads. New downloads are added to a collection, completed downloads are
 * removed. Erroneous downloads remain in the collection. The download manager observes all download objects for state
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
    private static final String XML_CONF_PARALLEL_DOWNLOADS = "parallelDownloads";
    private static final String XML_CONF_NOTIFICATION_DISPLAY_TIME = "notificationDisplayTime";
    private static final String XML_CONF_DIALOG = "dialog";
    private static final String XML_CONF_DIALOG_AKSFORTITLE = "askForTitle";
    private static final String XML_CONF_DIALOG_OPENAUTOMATICALLY = "openAutomatically";
    private static final String XML_CONF_DIALOG_CLOSEAUTOMATICALLY = "closeAutomatically";
    private static final String XML_CONF_DIALOG_USERTITLE = "userTitle";
    private static DownloadManager instance = null;

    //~ Instance fields --------------------------------------------------------

    private File destinationDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator")
                    + "cidsDownload");
    private int parallelDownloads = 2;
    private int notificationDisplayTime = 3;
    private LinkedList<Download> downloads = new LinkedList<Download>();
    private List<Download> downloadsToStart = new LinkedList<Download>();
    private EventListenerList listeners = new EventListenerList();
    private int countDownloadsTotal = 0;
    private volatile int countDownloadsRunning = 0;
    private int countDownloadsErroneous = 0;
    private int countDownloadsCompleted = 0;
    private int countDownloadsCancelled = 0;

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
     * This method is used to add a download to the download list.
     *
     * @param  download  A new download to add.
     */
    public synchronized void add(final Download download) {
        if ((download == null) || downloads.contains(download)) {
            return;
        }

        downloads.add(download);
        countDownloadsTotal++;
        download.addObserver(this);

        if (download instanceof MultipleDownload) {
            final MultipleDownload multipleDownload = (MultipleDownload)download;
            downloadsToStart.add(multipleDownload);

            for (final Download singleDownload : multipleDownload.getDownloads()) {
                singleDownload.addObserver(this);
                singleDownload.addObserver(multipleDownload);

                downloadsToStart.add(singleDownload);
            }
        } else {
            downloadsToStart.add(download);
        }

        notifyDownloadListChanged(new DownloadListChangedEvent(
                this,
                download,
                DownloadListChangedEvent.Action.ADDED));
        notifyDownloadListChanged(new DownloadListChangedEvent(
                this,
                download,
                DownloadListChangedEvent.Action.CHANGED_COUNTERS));

        startDownloads();
    }

    /**
     * Checks if the encapsulated downloads of a BackgroundTaskMultipleDownload have already been added to the
     * DownloadManager. If a single download has not yet been added it is added and the observers will be notified.
     *
     * @param  backgroundTaskMultipleDownload  DOCUMENT ME!
     */
    public synchronized void addDownloadsSubsequently(
            final BackgroundTaskMultipleDownload backgroundTaskMultipleDownload) {
        if ((backgroundTaskMultipleDownload == null) || !downloads.contains(backgroundTaskMultipleDownload)) {
            return;
        }

        boolean downloadsWereAdded = false;
        for (final Download encapsulatedDownload : backgroundTaskMultipleDownload.getDownloads()) {
            if (!downloads.contains(encapsulatedDownload)) {
                encapsulatedDownload.addObserver(this);
                encapsulatedDownload.addObserver(backgroundTaskMultipleDownload);

                downloadsToStart.add(encapsulatedDownload);
                downloadsWereAdded = true;
            }
        }

        if (downloadsWereAdded) {
            notifyDownloadListChanged(new DownloadListChangedEvent(
                    this,
                    backgroundTaskMultipleDownload,
                    DownloadListChangedEvent.Action.ADDED_DOWNLOADS_SUBSEQUENTLY));
            notifyDownloadListChanged(new DownloadListChangedEvent(
                    this,
                    backgroundTaskMultipleDownload,
                    DownloadListChangedEvent.Action.CHANGED_COUNTERS));

            startDownloads();
        }
    }

    /**
     * Removes obsolete downloads. Only completed downloads are obsolete.
     */
    public synchronized void removeObsoleteDownloads() {
        final Collection<Download> downloadsRemoved = new LinkedList<Download>();

        for (final Download download : downloads) {
            if ((download.getStatus() == Download.State.COMPLETED)
                        || (download.getStatus() == Download.State.COMPLETED_WITH_ERROR)
                        || (download.getStatus() == Download.State.ABORTED)) {
                downloadsRemoved.add(download);
            }
        }

        if (downloadsRemoved.size() <= 0) {
            return;
        }

        for (final Download download : downloadsRemoved) {
            downloads.remove(download);
            countDownloadsTotal--;

            switch (download.getStatus()) {
                case COMPLETED_WITH_ERROR: {
                    countDownloadsErroneous--;
                    break;
                }
                case COMPLETED: {
                    countDownloadsCompleted--;
                    break;
                }
                case ABORTED: {
                    countDownloadsCancelled--;
                    break;
                }
            }

            download.deleteObserver(this);
            if (download instanceof MultipleDownload) {
                final MultipleDownload multipleDownload = (MultipleDownload)download;

                for (final Download singleDownload : multipleDownload.getDownloads()) {
                    singleDownload.deleteObserver(this);
                    singleDownload.deleteObserver(multipleDownload);
                }
            }
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

    /**
     * Remove a specified download from the download list.
     *
     * @param  download  The download to remove.
     */
    public synchronized void removeDownload(final Download download) {
        downloads.remove(download);
        download.deleteObserver(this);

        if (download instanceof MultipleDownload) {
            final MultipleDownload multipleDownload = (MultipleDownload)download;

            for (final Download singleDownload : multipleDownload.getDownloads()) {
                singleDownload.deleteObserver(this);
                singleDownload.deleteObserver(multipleDownload);
                downloadsToStart.remove(singleDownload);
            }
        } else {
            downloadsToStart.remove(download);
        }

        countDownloadsTotal--;
        switch (download.getStatus()) {
            case COMPLETED_WITH_ERROR: {
                countDownloadsErroneous--;
                break;
            }
            case COMPLETED: {
                countDownloadsCompleted--;
                break;
            }
            case ABORTED: {
                countDownloadsCancelled--;
                break;
            }
        }

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
     * Starts pending downloads.
     */
    private synchronized void startDownloads() {
        int downloadsRunning = countDownloadsRunning;
        final Iterator<Download> downloadToStartIter = downloadsToStart.iterator();
        final List<Download> startableDownloads = new LinkedList<Download>();

        while (downloadToStartIter.hasNext() && (downloadsRunning < parallelDownloads)) {
            final Download downloadToStart = downloadToStartIter.next();

            if (downloadToStart.getStatus().equals(Download.State.WAITING)) {
                startableDownloads.add(downloadToStart);
                downloadsRunning++;
                downloadToStartIter.remove();
            }
        }

        for (final Download downloadToStart : startableDownloads) {
            downloadToStart.startDownload();
        }
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
     * Returns the count of erroneous downloads.
     *
     * @return  The count of erroneous downloads.
     */
    public int getCountDownloadsErroneous() {
        return countDownloadsErroneous;
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
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getCountDownloadsCancelled() {
        return countDownloadsCancelled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getParallelDownloads() {
        return parallelDownloads;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  parallelDownloads  DOCUMENT ME!
     */
    public void setParallelDownloads(final int parallelDownloads) {
        this.parallelDownloads = parallelDownloads;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getNotificationDisplayTime() {
        return notificationDisplayTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  notificationDisplayTime  DOCUMENT ME!
     */
    public void setNotificationDisplayTime(final int notificationDisplayTime) {
        this.notificationDisplayTime = notificationDisplayTime;
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

        DownloadManagerDialog.getInstance().destinationDirectoryChanged();

        if (!destinationDirectory.isDirectory() || !destinationDirectory.canWrite()) {
            LOG.error("The download manager can't use the directory '" + destinationDirectory.getAbsolutePath() + "'.");
        }
    }

    /**
     * Returns a flag which tells whether download manager is enabled or not.
     *
     * @return      The flag whether the download manager is enabled or not.
     *
     * @deprecated  DOCUMENT ME!
     */
    public boolean isEnabled() {
        return true;
    }

    @Override
    public synchronized void update(final Observable o, final Object arg) {
        if (arg != null) {
            /*
             * in this case we assume that there was an update that doesnt concern us here. E.G this could happen if the
             * title of a download has changed and it wants to notify the its observers about it. This feature was
             * introduced in issue cismet/cismet-gui-commons#29 and is used for example in the NasDownload
             */
            return;
        }

        if (!(o instanceof Download)) {
            return;
        }

        final Download download = (Download)o;

        switch (download.getStatus()) {
            case COMPLETED: {
                if (!(download instanceof MultipleDownload)) {
                    countDownloadsRunning--;
                }

                if (downloads.contains(download)) {
                    countDownloadsCompleted++;
                }

                startDownloads();

                break;
            }
            case COMPLETED_WITH_ERROR: {
                if (!(download instanceof MultipleDownload)) {
                    countDownloadsRunning--;
                }

                if (downloads.contains(download)) {
                    countDownloadsErroneous++;
                }

                startDownloads();

                break;
            }
            case RUNNING: {
                if (!(download instanceof MultipleDownload)) {
                    countDownloadsRunning++;
                }

                break;
            }
            case ABORTED: {
                if (!(download instanceof MultipleDownload)) {
                    countDownloadsRunning--;
                }

                if (downloads.contains(download)) {
//                    countDownloadsCompleted++;
                    countDownloadsCancelled++;
                }

                startDownloads();

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
        DownloadManagerDialog.getInstance().setAskForJobNameEnabled(true);
        DownloadManagerDialog.getInstance().setJobName("");
        DownloadManagerDialog.getInstance().setOpenAutomaticallyEnabled(true);

        destinationDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator")
                        + "cidsDownload");

        Element downloads = null;
        if (parent == null) {
            LOG.warn("The download manager isn't configured. Using default values.");
        } else {
            downloads = parent.getChild(XML_CONF_ROOT);
        }

        if (downloads == null) {
            LOG.warn("The download manager isn't configured. Using default values.");

            if (!destinationDirectory.isDirectory() || !destinationDirectory.canWrite()) {
                LOG.error("The download manager can't use the directory '" + destinationDirectory.getAbsolutePath()
                            + "'.");
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
            LOG.error("The download manager can't use the directory '" + destinationDirectory.getAbsolutePath() + "'.");
        }

        final Element parallelDownloads = downloads.getChild(XML_CONF_PARALLEL_DOWNLOADS);
        if ((parallelDownloads == null) || (parallelDownloads.getTextTrim() == null)) {
            LOG.warn("There is no limit for parallel downloads configured. Using default limit '2'.");
        } else {
            try {
                this.parallelDownloads = Integer.parseInt(parallelDownloads.getText());
            } catch (NumberFormatException e) {
                LOG.warn("Configuration for limit of parallel downloads is invalid. Using default value of '2'", e);
                this.parallelDownloads = 2;
            }
        }

        final Element notificationDisplayTime = downloads.getChild(XML_CONF_NOTIFICATION_DISPLAY_TIME);
        if ((notificationDisplayTime == null) || (notificationDisplayTime.getTextTrim() == null)) {
            LOG.warn("There is no display time for download notifications configured. Using default time '3' sec.");
        } else {
            try {
                this.notificationDisplayTime = Integer.parseInt(notificationDisplayTime.getText());
            } catch (NumberFormatException e) {
                LOG.warn(
                    "Configuration for display time of download notification is invalid. Using default value of '3' sec",
                    e);
                this.notificationDisplayTime = 3;
            }
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
            DownloadManagerDialog.getInstance()
                    .setAskForJobNameEnabled("1".equals(value) || "true".equalsIgnoreCase(value));
        }

        final Element openAutomatically = dialog.getChild(XML_CONF_DIALOG_OPENAUTOMATICALLY);
        if ((openAutomatically == null) || (openAutomatically.getTextTrim() == null)) {
            LOG.warn(
                "There is no configuration whether to open downloads automatically or not. Using default value 'true'.");
        } else {
            final String value = openAutomatically.getTextTrim();
            DownloadManagerDialog.getInstance()
                    .setOpenAutomaticallyEnabled("1".equals(value) || "true".equalsIgnoreCase(value));
        }

        final Element closeAutomatically = dialog.getChild(XML_CONF_DIALOG_CLOSEAUTOMATICALLY);
        if ((closeAutomatically == null) || (closeAutomatically.getTextTrim() == null)) {
            LOG.warn(
                "There is no configuration whether to close the download manager dialog automatically or not. Using default value 'true'.");
        } else {
            final String value = closeAutomatically.getTextTrim();
            DownloadManagerDialog.getInstance()
                    .setCloseAutomaticallyEnabled("1".equals(value) || "true".equalsIgnoreCase(value));
        }

        final Element userTitle = dialog.getChild(XML_CONF_DIALOG_USERTITLE);
        if ((userTitle == null) || (userTitle.getTextTrim() == null)) {
            LOG.warn("There is no user title for downloads configured. Using default value 'cidsDownload'.");
        } else {
            DownloadManagerDialog.getInstance().setJobName(userTitle.getTextTrim());
        }

        // refresh the destination path in the download manager dialog
        DownloadManagerDialog.getInstance().destinationDirectoryChanged();
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

        final Element parallelDownloads = new Element(XML_CONF_PARALLEL_DOWNLOADS);
        parallelDownloads.addContent(String.valueOf(this.parallelDownloads));

        final Element askForTitle = new Element(XML_CONF_DIALOG_AKSFORTITLE);
        askForTitle.addContent(DownloadManagerDialog.getInstance().isAskForJobNameEnabled() ? "true" : "false");

        final Element openAutomatically = new Element(XML_CONF_DIALOG_OPENAUTOMATICALLY);
        openAutomatically.addContent(DownloadManagerDialog.getInstance().isOpenAutomaticallyEnabled() ? "true"
                                                                                                      : "false");

        final Element closeAutomatically = new Element(XML_CONF_DIALOG_CLOSEAUTOMATICALLY);
        closeAutomatically.addContent(DownloadManagerDialog.getInstance().isCloseAutomaticallyEnabled() ? "true"
                                                                                                        : "false");

        final Element userTitle = new Element(XML_CONF_DIALOG_USERTITLE);
        userTitle.addContent(DownloadManagerDialog.getInstance().getJobName());

        dialog.addContent(askForTitle);
        dialog.addContent(openAutomatically);
        dialog.addContent(closeAutomatically);
        dialog.addContent(userTitle);

        root.addContent(directory);
        root.addContent(parallelDownloads);
        root.addContent(dialog);

        return root;
    }
}
