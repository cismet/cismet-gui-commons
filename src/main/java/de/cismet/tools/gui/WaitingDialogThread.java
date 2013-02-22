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

import java.awt.EventQueue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.Icon;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public abstract class WaitingDialogThread<T> implements Runnable {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(WaitingDialogThread.class);

    //~ Instance fields --------------------------------------------------------

    private int delay = 0;
    private volatile boolean isAlive = true;
    private volatile boolean shouldBeSetVisible = false;
    private java.awt.Frame parent;
    private boolean modal;
    private String text;
    private Icon icon;
    private T backgroundResult;
    private Exception thrownException;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WaitingDialogThread object.
     *
     * @param  parent  DOCUMENT ME!
     * @param  modal   DOCUMENT ME!
     * @param  text    DOCUMENT ME!
     * @param  icon    DOCUMENT ME!
     * @param  delay   DOCUMENT ME!
     */
    public WaitingDialogThread(final java.awt.Frame parent,
            final boolean modal,
            final String text,
            final Icon icon,
            final int delay) {
        this.parent = parent;
        this.modal = modal;
        this.text = text;
        this.icon = icon;
        this.delay = delay;
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
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected abstract T doInBackground() throws Exception;

    /**
     * DOCUMENT ME!
     */
    protected void done() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    protected T get() throws Exception {
        if (thrownException != null) {
            throw thrownException;
        } else {
            return backgroundResult;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        if (!EventQueue.isDispatchThread()) {
            LOG.error("The start method of the WaitingDialogThread must be invoked in the Edt.");
            return;
        }
        isAlive = true;
        shouldBeSetVisible = false;
        thrownException = null;
        final WaitDialog wd = new WaitDialog(parent, modal, text, icon);
        final ReentrantLock lock = new ReentrantLock();

        final Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            final Thread worker = new Thread(WaitingDialogThread.this);
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
            StaticSwingTools.showDialog(wd);
        }

        done();
    }
}
