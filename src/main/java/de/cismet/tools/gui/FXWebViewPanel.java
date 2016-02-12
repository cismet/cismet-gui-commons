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
package de.cismet.tools.gui;

import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.concurrent.Worker;

import javafx.embed.swing.JFXPanel;

import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.apache.log4j.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

import de.cismet.tools.BrowserLauncher;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class FXWebViewPanel extends JFXPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(FXWebViewPanel.class);

    //~ Instance fields --------------------------------------------------------

    protected WebEngine webEng = null;
    protected WebView webView;
    protected Scene scene;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FXWebViewPanel object.
     */
    public FXWebViewPanel() {
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    scene = createBrowserScene();
                    FXWebViewPanel.this.setScene(scene);
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Scene createBrowserScene() {
        webView = new WebView();
//         disabling the context menue
        webView.setContextMenuEnabled(false);

        webEng = webView.getEngine();
        webEng.setJavaScriptEnabled(true);
        // Log errors that happen in the web engine
        webEng.getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {

                @Override
                public void changed(final ObservableValue<? extends Throwable> ov,
                        final Throwable t,
                        final Throwable t1) {
                    LOG.error("Error in WebEngine Load Worker", t);
                }
            });
        // every time a new document was loaded, we need to add listeners to the a elements in that document, that check
        // if that link represents a non hml document we want to open in the system browser
        webEng.getLoadWorker().stateProperty().addListener(
            new ChangeListener<Worker.State>() {

                @Override
                public void changed(final ObservableValue ov,
                        final Worker.State oldState,
                        final Worker.State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        addClickListenerToLinks();
                    }
                }
            });

        final BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(5));
        pane.setCenter(webView);

        final Scene s = new Scene(pane);
        return s;
    }

    /**
     * this method adds a EventListener to each link element in the loaded document. The added EventListner checks if
     * the href of the link points to a non html document or anchor and if so opens he url in the external browser
     */
    private void addClickListenerToLinks() {
        final NodeList nodeList = webEng.getDocument().getElementsByTagName("a");
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node node = nodeList.item(i);
            final EventTarget eventTarget = (EventTarget)node;
            eventTarget.addEventListener("click", new EventListener() {

                    @Override
                    public void handleEvent(final Event evt) {
                        final EventTarget target = evt.getCurrentTarget();
                        final HTMLAnchorElement anchorElement = (HTMLAnchorElement)target;
                        final String href = anchorElement.getHref();
                        final String targetWindow = anchorElement.getTarget();
                        if (((targetWindow != null) && targetWindow.equalsIgnoreCase("_blank"))
                                    || ((href != null) && !href.endsWith("html") && !href.contains("#"))) {
                            openInSystemBrowser(href);
                            evt.preventDefault();
                        }
                    }
                }, false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    private void openInSystemBrowser(final String url) {
        try {
            if (url != null) {
                BrowserLauncher.openURL(url);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Returns the {@link WebEngine} used by the {@link WebView}. Hence the WebView is a JavaFX component, it should
     * only be accessed from JavaFX Application Thread.
     *
     * @return  DOCUMENT ME!
     */
    public WebEngine getWebEngine() {
        return webEng;
    }

    /**
     * Returns the {@link WebView} used to render html documents. Hence the{@link WebView} is a JavaFX component, it
     * should only be accessed from JavaFX Application Thread.
     *
     * @return  DOCUMENT ME!
     */
    public WebView getWebView() {
        return webView;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    public void loadUrl(final String url) {
        if (webEng == null) {
            LOG.warn("JavaFX WebEnginge is not initialized. can not load url: " + url);
            return;
        }
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    webEng.load(url);
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param  htmlContent  DOCUMENT ME!
     */
    public void loadContent(final String htmlContent) {
        if (webEng == null) {
            LOG.warn("JavaFX WebEnginge is not initialized. can not load html content: " + htmlContent);
            return;
        }
        Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    webEng.loadContent(htmlContent);
                    webEng.setJavaScriptEnabled(true);
                }
            });
    }
}
