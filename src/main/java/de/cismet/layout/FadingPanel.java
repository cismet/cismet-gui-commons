/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.layout;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.event.EventListenerList;

/**
 * Implements a Component that fades from one image to an other in a given time.
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class FadingPanel extends Component {

    //~ Instance fields --------------------------------------------------------

    private BufferedImage fadeFromImage;
    private BufferedImage fadeToImage;
    private EventListenerList fadeListenerList;
    private long fadeDuration;
    private long startTime = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Standard construtor.
     */
    public FadingPanel() {
        fadeListenerList = new EventListenerList();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Register a fadingpanel listener.
     *
     * @param  listener  DOCUMENT ME!
     */
    public void addFadingPanelListener(final FadingPanelListener listener) {
        fadeListenerList.add(FadingPanelListener.class, listener);
    }

    /**
     * informs the listener that the fade animation is finished.
     */
    private void fireFadeFinished() {
        for (final FadingPanelListener listener : fadeListenerList.getListeners(FadingPanelListener.class)) {
            listener.fadeFinished();
        }
    }

    /**
     * Returns the fade status of the component.
     *
     * @return  true is the component is actualy fading, else false
     */
    public boolean isFading() {
        return startTime > 0;
    }

    /**
     * Fades from one component to an other, by first creating an image from both components. Then @see
     * startFading(BufferedImage, BufferedImage, long)
     *
     * @param  fadeFromComponent  DOCUMENT ME!
     * @param  fadeToComponent    DOCUMENT ME!
     * @param  fadeDuration       DOCUMENT ME!
     */
    public void startFading(final Component fadeFromComponent,
            final Component fadeToComponent,
            final long fadeDuration) {
        startFading(
            createImageFromComponent(fadeFromComponent),
            createImageFromComponent(fadeToComponent),
            fadeDuration);
    }

    /**
     * Fades an image to an other, in a specific duration of time. If the Component is still fading, then the timer is
     * reseted. Else a new fade thread will be started.
     *
     * @param  fadeFromImage  the image to fade from
     * @param  fadeToImage    the image to fade to
     * @param  fadeDuration   the duration in ms for the fade animation
     */
    public void startFading(final BufferedImage fadeFromImage,
            final BufferedImage fadeToImage,
            final long fadeDuration) {
        this.fadeFromImage = fadeFromImage;
        this.fadeToImage = fadeToImage;
        this.fadeDuration = fadeDuration;

        // schon am faden?
        if (isFading()) {
            // dann einfach den timer zurücksetzen
            resetStartTime();
        } else {
            // sonst einen neuen fade-thread starten
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        // als erstes Zeit zurücksetzen
                        resetStartTime();
                        // faden bis fadeDuration erreicht wurde
                        while (ellapsedTime() < fadeDuration) {
                            // neu zeichnen (repaint ist thread-safe)
                            repaint();
                        }
                        // Startzeit wieder auf 0 setzen
                        // dies ist wichtig damit das Objekt weiß, dass das Faden
                        // beendet wurde (isFading)
                        startTime = 0;
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                        } catch (InterruptedException ex) {
                            // Exceptions.printStackTrace(ex);
                        } catch (ExecutionException ex) {
                            // Exceptions.printStackTrace(ex);
                        }
                        // Arbeit fertig, listener informieren
                        fireFadeFinished();
                    }
                };
            worker.execute();
        }
    }

    @Override
    public void update(final Graphics graphics) {
        paint(graphics);
    }

    @Override
    public synchronized void paint(final Graphics graphics) {
        // wenn keine Bilder
        if ((fadeFromImage == null) || (fadeToImage == null)) {
            // dann nix zum Faden
            return;
        }

        float alpha;
        // alpha-Faktor berechnen
        if (fadeDuration == 0) {
            // division durch 0 verhindern
            alpha = 1;
        } else {
            // je mehr Zeit vergeht, desto größer wird alpha
            alpha = (float)ellapsedTime() / fadeDuration;
            // alpha ist minimal 0 wenn noch keine Zeit vergangen ist,
            alpha = (alpha < 0) ? 0 : alpha;
            // und maximal 1 wenn die fadeDauer erreicht wurde
            alpha = (alpha > 1) ? 1 : alpha;
        }

        final Graphics2D graphics2d = (Graphics2D)graphics;
        // alte Composite sichern
        final Composite oldComposite = graphics2d.getComposite();
        // "from" immer mehr durchsichtig malen
        graphics2d.setComposite(AlphaComposite.SrcOver.derive(1 - alpha));
        graphics2d.drawImage(fadeFromImage, null, 0, 0);
        // "to" immer weniger durchsichtig malen
        graphics2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
        graphics2d.drawImage(fadeToImage, null, 0, 0);
        // alte Composite wieder setzen
        graphics2d.setComposite(oldComposite);
    }

    /**
     * Returns the ellapsed time between now and the last start time.
     *
     * @return  ellapsed time in ms
     */
    public long ellapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Resets the start time to now.
     */
    public void resetStartTime() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Creates an "alpha-compatible" buffered image from a given component.
     *
     * @param   component  the component
     *
     * @return  the bufferd image
     */
    public static BufferedImage createImageFromComponent(final Component component) {
        // keine Komponente?
        if (component == null) {
            // dann kein Bild
            return null;
        }

        // Standart-Grafik-Konfiguration des Bildschirms holen
        final GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

        // Höhe und Breite der Komponente schonmal merken
        final int width = component.getSize().width;
        final int height = component.getSize().height;

        // Image anhand der Grafik-Einstellungen erzeugen lassen
        BufferedImage image;
        // unterstützt das verwendete Farbmodel Durchsichtigkeit?
        if (graphicsConfiguration.getColorModel().hasAlpha()) {
            // dann Image erzeugen lassen
            image = graphicsConfiguration.createCompatibleImage(width, height);
        } else {
            // sonst, Image selbst erzeugen
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        // Image wurde richtig erzeugt?
        if (image != null) {
            // dann Objekt zum Malen in das Image holen
            final Graphics graphics = image.getGraphics();
            // und die Komponente sich darin malen lassen
            component.paint(graphics);
            // ressource freigeben
            graphics.dispose();
            // fertiges Bild zurückliefern
            return image;
        } else {
            // sonst kein Bild
            return null;
        }
    }
}
