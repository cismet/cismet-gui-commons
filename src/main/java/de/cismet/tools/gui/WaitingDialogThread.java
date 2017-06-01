/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui;

import org.apache.log4j.Logger;

import org.openide.util.Cancellable;

import java.awt.EventQueue;

import java.util.concurrent.locks.ReentrantLock;

import javax.swing.Icon;

/**
 * Executes a task in the background and shows a WaitingDialog until the end of the task.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public abstract class WaitingDialogThread<T> implements Runnable, Cancellable {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(WaitingDialogThread.class);

    //~ Instance fields --------------------------------------------------------

    protected WaitDialog wd;
    protected boolean canceled = false;

    private int delay = 0;
    private volatile boolean isAlive = true;
    private volatile boolean shouldBeSetVisible = false;
    private final java.awt.Frame parent;
    private final boolean modal;
    private final String text;
    private final Icon icon;
    private T backgroundResult;
    private Exception thrownException;
    private final boolean cancellable;
    private Thread worker = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WaitingDialogThread object.
     *
     * @param  parent  paretn frame
     * @param  modal   true, if the dialog should be modal
     * @param  text    the text that should be shown in the dialog
     * @param  icon    the icon of the dialog
     * @param  delay   after this delay, the dialog should be shown
     */
    public WaitingDialogThread(final java.awt.Frame parent,
            final boolean modal,
            final String text,
            final Icon icon,
            final int delay) {
        this(parent, modal, text, icon, delay, false);
    }

    /**
     * Creates a new WaitingDialogThread object.
     *
     * @param  parent       paretn frame
     * @param  modal        true, if the dialog should be modal
     * @param  text         the text that should be shown in the dialog
     * @param  icon         the icon of the dialog
     * @param  delay        after this delay, the dialog should be shown
     * @param  cancellable  true, if a cancel button should be shown and the {@link #doInBackground()} method should
     *                      handle the interrupt signal, if this is true
     */
    public WaitingDialogThread(final java.awt.Frame parent,
            final boolean modal,
            final String text,
            final Icon icon,
            final int delay,
            final boolean cancellable) {
        this.parent = parent;
        this.modal = modal;
        this.text = text;
        this.icon = icon;
        this.delay = delay;
        this.cancellable = cancellable;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public final void run() {
        try {
            backgroundResult = doInBackground();
        } catch (Exception e) {
            thrownException = e;
        }
    }

    /**
     * The task that should be executed is implemented in this method.
     *
     * @return  the result of the task. This result can be accessed in the done method
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected abstract T doInBackground() throws Exception;

    /**
     * This method is executed in the edt after the backgrund task.
     */
    protected void done() {
    }

    /**
     * provides the result of the background task.
     *
     * @return  the result of the background task
     *
     * @throws  Exception  the exception, that was thrown in the background task
     */
    protected T get() throws Exception {
        if (thrownException != null) {
            throw thrownException;
        } else {
            return backgroundResult;
        }
    }

    @Override
    public boolean cancel() {
        canceled = true;
        try {
            worker.interrupt();
        } catch (SecurityException e) {
            return false;
        }

        return true;
    }

    /**
     * starts the implemented task.
     */
    public void start() {
        if (!EventQueue.isDispatchThread()) {
            LOG.error("The start method of the WaitingDialogThread must be invoked in the Edt.");
            return;
        }
        isAlive = true;
        shouldBeSetVisible = false;
        thrownException = null;
        final ReentrantLock lock = new ReentrantLock();

        if (cancellable) {
            wd = new WaitDialog(parent, modal, text, icon, this);
        } else {
            wd = new WaitDialog(parent, modal, text, icon);
        }

        final Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            worker = new Thread(WaitingDialogThread.this);
                            worker.start();

                            try {
                                worker.join();
                            } catch (InterruptedException e) {
                                // nothing to do
                            }
                        } finally {
                            if (shouldBeSetVisible) {
                                try {
                                    lock.lock();
                                    isAlive = false;
                                    while (!wd.isVisible()) {
                                        try {
                                            Thread.sleep(20);
                                        } catch (InterruptedException e) {
                                            // nothing to do
                                        }
                                    }

                                    wd.setVisible(false);
                                    wd.dispose();
                                } finally {
                                    lock.unlock();
                                }
                            }
                        }
                    }
                });

        t.start();

        if (delay > 0) {
            try {
                t.join(delay);
            } catch (InterruptedException e) {
                // nothing to do
            }
        }

        try {
            lock.lock();
            if (t.isAlive() && isAlive) {
                shouldBeSetVisible = true;
            }
        } finally {
            lock.unlock();
        }

        if (shouldBeSetVisible) {
            // show the waiting dialog
            StaticSwingTools.showDialog(wd);
        }

        if (!canceled) {
            // starts the task in the edt without waiting dialog
            done();
        }
    }
}
