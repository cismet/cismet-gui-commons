/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;

import java.io.PrintStream;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Monitors the AWT event dispatch thread for events that take longer than a certain time to be dispatched.
 *
 * <p>The principle is to record the time at which we start processing an event, and have another thread check
 * frequently to see if we're still processing. If the other thread notices that we've been processing a single event
 * for too long, it prints a stack trace showing what the event dispatch thread is doing, and continues to time it until
 * it finally finishes.</p>
 *
 * <p>This is useful in determining what code is causing your Java application's GUI to be unresponsive.</p>
 *
 * @author   Elliott Hughes <enh@jessies.org>
 * @version  $Revision$, $Date$
 */
public final class EventDispatchThreadHangMonitor extends EventQueue {

    //~ Static fields/initializers ---------------------------------------------

    private static final EventQueue INSTANCE = new EventDispatchThreadHangMonitor();
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            EventDispatchThreadHangMonitor.class);
    // Time to wait between checks that the event dispatch thread isn't hung.
    private static final long CHECK_INTERVAL_MS = 100;

    // Maximum time we won't warn about.
    private static final long UNREASONABLE_DISPATCH_DURATION_MS = 500;

    // Used as the value of startedLastEventDispatchAt when we're not in
    // the middle of event dispatch.
    private static final long NO_CURRENT_EVENT = 0;

    //~ Instance fields --------------------------------------------------------

    // When we started dispatching the current event, in milliseconds.
    private long startedLastEventDispatchAt = NO_CURRENT_EVENT;

    // Have we already dumped a stack trace for the current event dispatch?
    private boolean reportedHang = false;

    // The event dispatch thread, for the purpose of getting stack traces.
    private Thread eventDispatchThread = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EventDispatchThreadHangMonitor object.
     */
    private EventDispatchThreadHangMonitor() {
        initTimer();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Sets up a timer to check for hangs frequently.
     */
    private void initTimer() {
        final long initialDelayMs = 0;
        final boolean isDaemon = true;
        final Timer timer = new Timer("EventDispatchThreadHangMonitor", isDaemon); // NOI18N
        timer.schedule(new HangChecker(), initialDelayMs, CHECK_INTERVAL_MS);
    }

    /**
     * Returns how long we've been processing the current event (in milliseconds).
     *
     * @return  DOCUMENT ME!
     */
    private long timeSoFar() {
        final long currentTime = System.currentTimeMillis();
        return (currentTime - startedLastEventDispatchAt);
    }

    /**
     * Sets up hang detection for the event dispatch thread.
     */
    public static void initMonitoring() {
        if (log.isInfoEnabled()) {
            log.info("EventDispatchThreadHangMonitor inited"); // NOI18N
        }
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(INSTANCE);
    }

    /**
     * Overrides EventQueue.dispatchEvent to call our pre and post hooks either side of the system's event dispatch
     * code.
     *
     * @param  event  DOCUMENT ME!
     */
    @Override
    protected void dispatchEvent(final AWTEvent event) {
        preDispatchEvent();
        super.dispatchEvent(event);
        postDispatchEvent();
    }

    /**
     * Stores the time at which we started processing the current event.
     */
    private synchronized void preDispatchEvent() {
        if (eventDispatchThread == null) {
            // I don't know of any API for getting the event dispatch thread,
            // but we can assume that it's the current thread if we're in the
            // middle of dispatching an AWT event...
            eventDispatchThread = Thread.currentThread();
        }

        reportedHang = false;
        startedLastEventDispatchAt = System.currentTimeMillis();
    }

    /**
     * Reports the end of any ongoing hang, and notes that we're no longer processing an event.
     */
    private synchronized void postDispatchEvent() {
        if (reportedHang) {
            log.fatal("--- event dispatch thread unstuck after " + timeSoFar() + " ms."); // NOI18N
        }
        startedLastEventDispatchAt = NO_CURRENT_EVENT;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class HangChecker extends TimerTask {

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            // Synchronize on the outer class, because that's where all
            // the state lives.
            synchronized (INSTANCE) {
                checkForHang();
            }
        }

        /**
         * DOCUMENT ME!
         */
        private void checkForHang() {
            if (startedLastEventDispatchAt == NO_CURRENT_EVENT) {
                // We don't destroy the timer when there's nothing happening
                // because it would mean a lot more work on every single AWT
                // event that gets dispatched.
                return;
            }
            if (timeSoFar() > UNREASONABLE_DISPATCH_DURATION_MS) {
                reportHang();
            }
        }

        /**
         * DOCUMENT ME!
         */
        private void reportHang() {
            if (reportedHang) {
                // Don't keep reporting the same hang every 100 ms.
                return;
            }

            reportedHang = true;

            final StackTraceElement[] stackTrace = eventDispatchThread.getStackTrace();
            final Throwable customThrowable = new Throwable() {

                    @Override
                    public StackTraceElement[] getStackTrace() {
                        return stackTrace;
                    }
                };
            log.fatal("--- event dispatch thread stuck processing event for " + timeSoFar() + " ms:", customThrowable); // NOI18N
        }

        /**
         * DOCUMENT ME!
         *
         * @param  stackTrace  DOCUMENT ME!
         */
        private void logStackTrace(final StackTraceElement[] stackTrace) {
            // We know that it's not interesting to show any code above where
            // we get involved in event dispatch, so we stop printing the stack
            // trace when we get as far back as our code.
            String s = "";                                 // NOI18N
            final String ourEventQueueClassName = EventDispatchThreadHangMonitor.class.getName();
            for (final StackTraceElement stackTraceElement : stackTrace) {
                if (stackTraceElement.getClassName().equals(ourEventQueueClassName)) {
                    break;
                }
                s += "    " + stackTraceElement + "\n\n "; // NOI18N
            }
            log.fatal("Stacktrace\n\n" + s);               // NOI18N
        }
    }
}
