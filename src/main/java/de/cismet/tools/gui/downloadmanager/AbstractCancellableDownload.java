/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.downloadmanager;

import org.openide.util.Cancellable;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public abstract class AbstractCancellableDownload extends AbstractDownload implements Cancellable {

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean cancel() {
        boolean cancelled = true;
        boolean isDone = false;
        if (downloadFuture != null) {
            isDone = downloadFuture.isDone();
            cancelled = downloadFuture.cancel(true);
        }

        if (cancelled || isDone) {
            status = State.ABORTED;
            stateChanged();
        }

        return downloadFuture.isCancelled();
    }
}
