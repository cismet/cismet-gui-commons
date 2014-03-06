/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.downloadmanager;

import java.util.Collection;

import javax.swing.SwingWorker;

/**
 * BackgroundTaskMultipleDownload extends MultipleDownload, the difference is that the BackgroundTaskMultipleDownload
 * does not need to know how many downloads it contains when it is started. Its encapsulated downloads are created in a
 * background task provided to it by an instance of FetchDownloadsTask. This tasks runs in a SwingWorker and returns a
 * List of Downloads. After the task has finished the created downloads are added to the BackgroundTaskMultipleDownload.
 *
 * <p>Note: the FetchDownloadsTask will not run in the EDT.</p>
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
public class BackgroundTaskMultipleDownload extends MultipleDownload {

    //~ Instance fields --------------------------------------------------------

    private FetchDownloadsTask fetchDownloadTask;
    private SwingWorker<Collection<? extends Download>, Void> worker;
    private Exception caughtException;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BackgroundTaskMultipleDownload object.
     *
     * @param  downloads           DOCUMENT ME!
     * @param  title               DOCUMENT ME!
     * @param  fetchDownloadsTask  DOCUMENT ME!
     */
    public BackgroundTaskMultipleDownload(final Collection<? extends Download> downloads,
            final String title,
            final FetchDownloadsTask fetchDownloadsTask) {
        super(downloads, title);
        this.fetchDownloadTask = fetchDownloadsTask;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void startDownload() {
        worker = new SwingWorker<Collection<? extends Download>, Void>() {

                @Override
                protected Collection<? extends Download> doInBackground() throws Exception {
                    return fetchDownloadTask.fetchDownloads();
                }

                @Override
                protected void done() {
                    try {
                        addDownloadsSubsequently(get());
                        setStatus(State.RUNNING);
                    } catch (Exception ex) {
                        caughtException = ex;
                        setStatus(State.COMPLETED_WITH_ERROR);
                    }
                }
            };

        worker.execute();
    }

    @Override
    public Exception getCaughtException() {
        return caughtException;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  <D>           DOCUMENT ME!
     * @param  newDownloads  DOCUMENT ME!
     */
    private <D extends Download> void addDownloadsSubsequently(final Collection<D> newDownloads) {
        final Collection<D> existentDownloads = (Collection<D>)getDownloads();
        for (final D download : newDownloads) {
            existentDownloads.add(download);
        }
        DownloadManager.instance().addDownloadsSubsequently(this);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = (47 * hash) + ((this.fetchDownloadTask != null) ? this.fetchDownloadTask.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BackgroundTaskMultipleDownload other = (BackgroundTaskMultipleDownload)obj;
        if ((this.fetchDownloadTask != other.fetchDownloadTask)
                    && ((this.fetchDownloadTask == null) || !this.fetchDownloadTask.equals(other.fetchDownloadTask))) {
            return false;
        }
        return true;
    }

    //~ Inner Interfaces -------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public interface FetchDownloadsTask {

        //~ Methods ------------------------------------------------------------

        /**
         * A task, which is executed during the startDownload()-method of BackgroundTaskMultipleDownload. Every
         * exception thrown in this method, will later on be caught by the BackgroundTaskMultipleDownload and shown in
         * the DownloadManager.
         *
         * <p>Note: Do not forget to close, eventually opened, system resources e.g. FileOutputStream. This can be done
         * with a try-finally block.</p>
         *
         * @return  DOCUMENT ME!
         *
         * @throws  Exception  the Exceptions will be caught by BackgroundTaskMultipleDownload
         */
        Collection<? extends Download> fetchDownloads() throws Exception;
    }
}
