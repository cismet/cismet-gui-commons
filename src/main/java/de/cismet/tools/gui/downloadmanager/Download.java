/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.downloadmanager;

import java.io.File;

import java.util.Observer;

/**
 * Download provides an enum and some methods which are to be implemented by any download implementation. A Download
 * encapsulates all information to download files.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public interface Download {

    //~ Enums ------------------------------------------------------------------

    /**
     * The state of a download.
     *
     * @version  $Revision$, $Date$
     */
    enum State {

        //~ Enum constants -----------------------------------------------------

        WAITING, RUNNING, RUNNING_WITH_ERROR, COMPLETED, COMPLETED_WITH_ERROR
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Notifies the download that it should start.
     */
    void startDownload();

    /**
     * Returns the current state of the download.
     *
     * @return  The state of the download.
     */
    State getStatus();

    /**
     * A download can be observed for state changes. Thus a download implementation should extend Observable. This
     * method allows a download to add a new observer.
     *
     * @param  o  An observer to add.
     */
    void addObserver(Observer o);

    /**
     * A download can be observed for state changes. Thus a download implementation should extend Observable. This
     * method allows a download to remove an observer from the observer list.
     *
     * @param  o  The observer to remove.
     */
    void deleteObserver(Observer o);

    /**
     * Returns the count of downloads this Download object encapsulates.
     *
     * @return  Total count of downloads.
     */
    int getDownloadsTotal();

    /**
     * Returns the count of completed downloads this Download object encapsulates.
     *
     * @return  Count of completed downloads.
     */
    int getDownloadsCompleted();

    /**
     * Returns the count of erraneous downloads this Download object encapsulates.
     *
     * @return  Count of erraneous downloads.
     */
    int getDownloadsErraneous();

    /**
     * Returns the File object pointing to the destination download location.
     *
     * @return  Destination download location.
     */
    File getFileToSaveTo();
}
