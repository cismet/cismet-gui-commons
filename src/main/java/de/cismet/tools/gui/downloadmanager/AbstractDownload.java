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
package de.cismet.tools.gui.downloadmanager;

import org.apache.log4j.Logger;

import org.openide.util.NbBundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.text.MessageFormat;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.JPanel;

import de.cismet.commons.concurrency.CismetConcurrency.CismetThreadFactory;
import de.cismet.commons.concurrency.CismetExecutors;

/**
 * The objects of this class represent downloads. This class encompasses several default methods which should be the
 * same for most download which care about single files.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public abstract class AbstractDownload extends Observable implements Download, Runnable, Comparable {

    //~ Static fields/initializers ---------------------------------------------

    private static final ExecutorService downloadThreadPool;

    static {
        final SecurityManager s = System.getSecurityManager();
        final ThreadGroup parent = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        final int initialParallelDownloads = (DownloadManager.instance().getParallelDownloads() == 0)
            ? 10 : DownloadManager.instance().getParallelDownloads();
        final ThreadGroup threadGroup = new ThreadGroup(parent, "DownloadThreadPool");
        downloadThreadPool = CismetExecutors.newCachedLimitedThreadPool(
                initialParallelDownloads,
                new CismetThreadFactory(threadGroup, "DownloadThreadPool", null),
                new DownloadRejectExecutionHandler());
    }

    protected static final Logger log = Logger.getLogger(AbstractDownload.class);

    //~ Instance fields --------------------------------------------------------

    protected String directory;
    protected File fileToSaveTo;
    protected State status;
    protected String title;
    protected Future downloadFuture;
    protected boolean started = false;
    protected Exception caughtException;

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns the title of the download.
     *
     * @return  The title.
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Returns a file object pointing to the download location of this download.
     *
     * @return  A file object pointing to the download location.
     */
    @Override
    public File getFileToSaveTo() {
        return fileToSaveTo;
    }

    /**
     * Returns the status of this download.
     *
     * @return  The status of this download.
     */
    @Override
    public State getStatus() {
        return status;
    }

    /**
     * Returns the exception which is caught during the download. If an exception occurs the download is aborted.
     *
     * @return  The caught exception.
     */
    @Override
    public Exception getCaughtException() {
        return caughtException;
    }

    @Override
    public int getDownloadsTotal() {
        return 1;
    }

    @Override
    public int getDownloadsCompleted() {
        if (status == State.RUNNING) {
            return 0;
        }

        return 1;
    }

    @Override
    public int getDownloadsErroneous() {
        if (status == State.COMPLETED_WITH_ERROR) {
            return 1;
        }

        return 0;
    }

    /**
     * Logs a caught exception and sets some members accordingly.
     *
     * @param  exception  The caught exception.
     */
    protected void error(final Exception exception) {
        if (fileToSaveTo != null) {
            log.error("Exception occurred while downloading '" + fileToSaveTo + "'.", exception);
            fileToSaveTo.deleteOnExit();
        } else {
            log.error("Exception occurred while download.", exception);
        }
        caughtException = exception;
        status = State.COMPLETED_WITH_ERROR;
        stateChanged();
    }

    /**
     * Starts a thread which starts the download by starting this Runnable.
     */
    @Override
    public void startDownload() {
        if (!started) {
            started = true;
            if (downloadThreadPool != null) {
                downloadFuture = downloadThreadPool.submit(this);
            } else {
                log.error("Download Thread Pool is null. Downlaod can not be started");
                error(new IllegalStateException("downloadThread is null. Can not start Download"));
            }
        }
    }

    @Override
    public JPanel getExceptionPanel(final Exception exception) {
        return null;
    }

    @Override
    public abstract void run();

    /**
     * Determines the destination file for this download. There exist given parameters like a download destination and a
     * pattern for the file name. It's possible that a previous download with equal parameters still exists physically,
     * therefore this method adds a counter (2..999) which is appended to the filename. This counter is surrounded by
     * parentheses.
     *
     * @param  filename   The file name for this download.
     * @param  extension  The extension for the downloaded file.
     */
    protected void determineDestinationFile(final String filename,
            final String extension) {
        final File directoryToSaveTo;

        if (isAbsolute(directory)) {
            // the directory is a absolute path, so it should be used as destination directory
            directoryToSaveTo = new File(directory);
        } else {
            if ((directory != null) && (directory.trim().length() > 0)) {
                directoryToSaveTo = new File(DownloadManager.instance().getDestinationDirectory(), directory);
            } else {
                directoryToSaveTo = DownloadManager.instance().getDestinationDirectory();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Determined path '" + directoryToSaveTo + "' for file '" + filename + extension + "'.");
        }

        if (!directoryToSaveTo.exists()) {
            if (!directoryToSaveTo.mkdirs()) {
                log.error("Couldn't create destination directory '"
                            + directoryToSaveTo.getAbsolutePath()
                            + "'. Cancelling download.");
                error(new Exception(
                        "Couldn't create destination directory '"
                                + directoryToSaveTo.getAbsolutePath()
                                + "'. Cancelling download."));
                return;
            }
        }

        if (!directoryToSaveTo.canWrite()) {
            log.error("Can not write to " + directoryToSaveTo.getAbsolutePath()
                        + ". Probably write permissions are missing.");
            final String errorMessage = NbBundle.getMessage(
                    AbstractDownload.class,
                    "AbstractDownload.determineDestinationFile().canNotWriteToDirectory");
            final Exception ex = new Exception();
            ex.getLocalizedMessage();
            error(new Exception(MessageFormat.format(errorMessage, directoryToSaveTo.getAbsolutePath())));
            return;
        }

        fileToSaveTo = new File(directoryToSaveTo, filename + extension);
        boolean fileFound = false;
        int counter = 2;

        while (!fileFound) {
            while (fileToSaveTo.exists() && (counter < 1000)) {
                fileToSaveTo = new File(directoryToSaveTo, filename + "(" + counter + ")" + extension);
                counter++;
            }

            try {
                if (!fileToSaveTo.getParentFile().exists()) {
                    fileToSaveTo.getParentFile().mkdirs();
                }
                fileToSaveTo.createNewFile();

                if (fileToSaveTo.exists() && fileToSaveTo.isFile() && fileToSaveTo.canWrite()) {
                    fileFound = true;
                }
            } catch (IOException ex) {
                log.warn("IOEXception while trying to create destination file '" + fileToSaveTo.getAbsolutePath()
                            + "'.",
                    ex);
                fileToSaveTo.deleteOnExit();
            }

            if ((counter >= 1000) && !fileFound) {
                log.error("Could not create a file for the download. The tested path is '"
                            + directoryToSaveTo.getAbsolutePath()
                            + File.separatorChar
                            + filename
                            + "<1.."
                            + 999
                            + ">."
                            + extension
                            + ".");
                error(new FileNotFoundException(
                        "Could not create a file for the download. The tested path is '"
                                + directoryToSaveTo.getAbsolutePath()
                                + File.separatorChar
                                + filename
                                + "<1.."
                                + 999
                                + ">."
                                + extension
                                + "."));
                return;
            }
        }
    }

    /**
     * Checks, if the given directory is absolute.
     *
     * @param   directory  The directory to check
     *
     * @return  true, iff the directory is absolute
     */
    private boolean isAbsolute(final String directory) {
        if ((directory != null) && (directory.trim().length() > 1)) {
            final File path = new File(directory);
            return path.isAbsolute();
        }

        return false;
    }

//    @Override
//    public boolean cancel() {
//        final boolean flag = f.cancel(true);
//        if (!flag) {
//            log.fatal("could not cancel download thread");
//        }
//        this.status = State.ABORTED;
//        stateChanged();
//        return f.isCancelled();
//    }
    /**
     * Marks this observable as changed and notifies observers.
     */
    protected void stateChanged() {
        setChanged();
        notifyObservers();
    }

    /**
     * DOCUMENT ME!
     */
    protected void titleChanged() {
        setChanged();
        notifyObservers(getTitle());
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof AbstractDownload)) {
            return false;
        }

        return this == obj;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = (43 * hash) + ((this.fileToSaveTo != null) ? this.fileToSaveTo.hashCode() : 0);

        return hash;
    }

    @Override
    public int compareTo(final Object o) {
        if (!(o instanceof AbstractDownload)) {
            return 1;
        }

        final AbstractDownload other = (AbstractDownload)o;
        return this.title.compareTo(other.title);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  parallelDownloads  DOCUMENT ME!
     */
    public static void setParallelDownloads(final int parallelDownloads) {
        if (downloadThreadPool == null) {
            log.error("Thread pool for downloads is null. Currently it is not possible to start downloads");
        }
        if (downloadThreadPool instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor)downloadThreadPool).setMaximumPoolSize(parallelDownloads);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class DownloadRejectExecutionHandler implements RejectedExecutionHandler {

        //~ Methods ------------------------------------------------------------

        @Override
        public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
            log.error("Execution of Downlaod Thread was rejected.");
            if (r instanceof AbstractDownload) {
                final AbstractDownload download = (AbstractDownload)r;
                download.error(new RejectedExecutionException(
                        " Downlaod konnte nicht gestartet werden. Es stehen nicht genügend DownlaodThreads bereit."));
            }
        }
    }
}
