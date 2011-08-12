/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.downloadmanager;

import org.apache.log4j.Logger;

import java.io.File;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

/**
 * A multiple download comprises of several SingleDownloads.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class MultipleDownload extends Observable implements Download, Observer {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(MultipleDownload.class);

    //~ Instance fields --------------------------------------------------------

    private Collection<SingleDownload> downloads;
    private String title;
    private State status;
    private int downloadsCompleted;
    private int downloadsErraneous;
    private File fileToSaveTo;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MultipleDownload object.
     *
     * @param  downloads  A collection of SingleDownloads which are to be downloaded by this multiple download.
     * @param  title      The title of the multiple download.
     */
    public MultipleDownload(final Collection<SingleDownload> downloads, final String title) {
        this.downloads = downloads;
        this.title = title;

        if (DownloadManager.instance().isEnabled()) {
            for (final SingleDownload download : this.downloads) {
                if (fileToSaveTo == null) {
                    fileToSaveTo = download.getFileToSaveTo().getParentFile();
                } else {
                    if (!fileToSaveTo.equals(download.getFileToSaveTo().getParentFile())) {
                        fileToSaveTo = null;
                        break;
                    }
                }
            }

            status = State.WAITING;
        } else {
            status = State.COMPLETED_WITH_ERROR;
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void startDownload() {
        // NOP
    }

    @Override
    public State getStatus() {
        return status;
    }

    @Override
    public int getDownloadsTotal() {
        return downloads.size();
    }

    @Override
    public int getDownloadsCompleted() {
        return downloadsCompleted;
    }

    @Override
    public int getDownloadsErraneous() {
        return downloadsErraneous;
    }

    @Override
    public File getFileToSaveTo() {
        return fileToSaveTo;
    }

    /**
     * Getter for the title attribute.
     *
     * @return  The title of this multiple download.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the collection of SingleDownload objects.
     *
     * @return  The single downloads encapsulated by this multiple download.
     */
    public Collection<SingleDownload> getDownloads() {
        return downloads;
    }

    @Override
    public synchronized void update(final Observable o, final Object arg) {
        if (!(o instanceof SingleDownload)) {
            return;
        }

        final SingleDownload download = (SingleDownload)o;
        if ((download.getStatus() == State.RUNNING) && (status == State.WAITING)) {
            status = State.RUNNING;
        } else if (download.getStatus() == State.COMPLETED) {
            downloadsCompleted++;
        } else if (download.getStatus() == State.COMPLETED_WITH_ERROR) {
            downloadsCompleted++;
            downloadsErraneous++;
            status = State.RUNNING_WITH_ERROR;
        }

        if (downloadsCompleted == downloads.size()) {
            if (status == State.RUNNING) {
                status = State.COMPLETED;
            } else {
                status = State.COMPLETED_WITH_ERROR;
            }
        }

        setChanged();
        notifyObservers();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final MultipleDownload other = (MultipleDownload)obj;

        if ((this.downloads == null) ? (other.downloads != null) : (!this.downloads.containsAll(other.downloads))) {
            return false;
        }
        if ((this.title == null) ? (other.title != null) : (!this.title.equals(other.title))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;

        hash = (37 * hash) + ((this.downloads != null) ? this.downloads.hashCode() : 0);
        hash = (37 * hash) + ((this.title != null) ? this.title.hashCode() : 0);

        return hash;
    }
}
