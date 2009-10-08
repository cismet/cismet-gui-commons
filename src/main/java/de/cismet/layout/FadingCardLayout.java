package de.cismet.layout;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.SwingUtilities;

/**
 * A CardLayout extensions, which animates the change from one card to
 * another card with a fade animation (by using the FadingPanel component).
 * @author jruiz
 */
public class FadingCardLayout extends CardLayout {

    /**
     * the cardname of the fade panel
     */
    private static final String FADEPANEL_NAME = "__fadePanel__";
    /**
     * The default duration of the fade animation.
     */
    private long fadeDuration = 1000;
    /**
     * Maps the added components to their cardname.
     */
    private Hashtable<String, Component> componentHashtable = new Hashtable<String, Component>();
    /**
     * the panel to show when the fade animation is finished
     */
    private Component fadeTo = null;
    /**
     * this panel is used to render the fade animation
     */
    private FadingPanel fadePanel = null;

    public FadingCardLayout() {
        super();
        // neuen fadePanel erzeugen
        fadePanel = new FadingPanel();
        // als Listener anmelden um informiert zu werden wenn das Faden beendet wurde
        fadePanel.addFadingPanelListener(new FadingPanelListener() {

            @Override
            public void fadeFinished() {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        Container parent = fadePanel.getParent();
                        // ursprüngliches Panel zu dem gefadet wurde zeigen
                        showWithoutFade(parent, getCardnameOf(fadeTo));
                        fadeTo = null;
                        // fadePanel wieder aus dem Container entfernen
                        parent.remove(fadePanel);
                    }
                });

            }
        });

    }

    @Override
    public void addLayoutComponent(String name, Component component) {
        componentHashtable.put(name, component);
        super.addLayoutComponent(name, component);
    }

    @Override
    public void removeLayoutComponent(Component component) {
        Enumeration<String> keys = componentHashtable.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (componentHashtable.get(key) == component) {
                componentHashtable.remove(key);
            }
        }
        super.removeLayoutComponent(component);
    }

    @Override
    public void next(Container parent) {
        synchronized (parent.getTreeLock()) {
            // current ist die momentan angezeigte komponente, oder aber fadeto falls gerade gefadet wird
            int currentIndex = (fadeTo != null) ? getIndexOfComponent(parent, fadeTo) : getIndexOfCurrentComponent(parent);
            // next ist die nächste komponente
            int nextIndex = ++currentIndex % parent.getComponentCount();
            // ... beziehungsweise die übernächste falls die nächste der fadepanel ist
            nextIndex = (parent.getComponent(nextIndex) == fadePanel) ? ++nextIndex % parent.getComponentCount() : nextIndex;
            // faden zur nächsten komponente
            fade(parent, parent.getComponent(nextIndex));
        }
    }

    @Override
    public void previous(Container parent) {
        synchronized (parent.getTreeLock()) {
            // current ist die momentan angezeigte komponente, oder aber fadeto falls gerade gefadet wird
            int currentIndex = (fadeTo != null) ? getIndexOfComponent(parent, fadeTo) : getIndexOfCurrentComponent(parent);
            // previous ist die vorherige komponente
            int previousIndex = (currentIndex == 0) ? parent.getComponentCount() - 1 : --currentIndex;
            // ... beziehungsweise die vor-vorherige falls die vorherige der fadepanel ist
            previousIndex = (parent.getComponent(previousIndex) == fadePanel) ? --previousIndex % parent.getComponentCount() : previousIndex;
            // faden zur vorherigen komponente
            fade(parent, parent.getComponent(previousIndex));
        }
    }

    @Override
    public void first(Container parent) {
        synchronized (parent.getTreeLock()) {
            // first ist die erste komponente
            int firstIndex = 0;
            // ... beziehungsweise die nächste wenn die erste der fadepanel ist
            firstIndex = (parent.getComponent(firstIndex) == fadePanel) ? ++firstIndex : firstIndex;
            // faden zur ersten komponente
            fade(parent, parent.getComponent(firstIndex));
        }
    }

    @Override
    public void last(Container parent) {
        synchronized (parent.getTreeLock()) {
            // last ist die letzte komponente
            int lastIndex = parent.getComponentCount() - 1;
            // ... beziehungsweise die vorherige wenn die letzte der fadepanel ist
            lastIndex = (parent.getComponent(lastIndex) == fadePanel) ? --lastIndex : lastIndex;
            // faden zur letzten komponente
            fade(parent, parent.getComponent(lastIndex));
        }
    }

    @Override
    public void show(Container parent, String name) {
        synchronized (parent.getTreeLock()) {
            fade(parent, componentHashtable.get(name));
        }
    }

    /**
     * Shows the given card within the parent container, without to start the fade animation.
     * @param parent the container
     * @param name the name of the card to show
     */
    private void showWithoutFade(Container parent, String name) {
        super.show(parent, name);
    }

    /**
     * Fades the actualy shown component to the given component.
     * The fade panel is shown and his animation is started.
     * @param parent
     * @param fadeTo
     */
    private void fade(Container parent, Component fadeTo) {
        // component holen von der aus gefadet werden soll
        Component fadeFrom = getCurrentComponent(parent);

        // wenn nicht gefadet werden brauch (Quelle = Ziel)
        if (fadeFrom == fadeTo) {
            // dann "normal" zum Ziel wechseln (ohne zu faden).
            showWithoutFade(parent, getCardnameOf(fadeTo));
        } else { // sonst => faden:
            // Ziel-Panel merken um es nach dem Faden setzen zu können (listener)
            this.fadeTo = fadeTo;
            // fadePanel dem parent temporär hinzufügen und anzeigen
            if (getCardnameOf(fadePanel) == null) {
                parent.add(fadePanel, FADEPANEL_NAME);
            }
            showWithoutFade(parent, FADEPANEL_NAME);
            // fade-Vorgang starten
            fadePanel.startFading(fadeFrom, fadeTo, fadeDuration);
        }
    }

    /**
     * Returns the card name of a component.
     * @param component The component
     * @return the card name of the component, or null if the component was
     *         not found within the container
     */
    private String getCardnameOf(Component component) {
        Enumeration<String> keys = componentHashtable.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (componentHashtable.get(key) == component) {
                return key;
            }
        }
        return null;
    }

    /**
     * Returns the index of the first visible component within the given container.
     * If the containers layout is a cardlayout, then it will be the index of the
     * currently shown component.
     * @param parent the container
     * @return the index of the first visible component, or -1 if no component is visible
     */
    private static int getIndexOfCurrentComponent(Container parent) {
        for (int index = 0; index < parent.getComponentCount(); index++) {
            Component component = parent.getComponent(index);
            if (component.isVisible()) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Returns the index of a given component, within a given container.
     * @param parent the container
     * @param component the component
     * @return the index of the component, or -1 when the component was not found
     */
    private static int getIndexOfComponent(Container parent, Component component) {
        for (int index = 0; index < parent.getComponentCount(); index++) {
            if (parent.getComponent(index) == component) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Returns the first visible component within a given container.
     * @param parent the container
     * @return the component, or null when no component is visible
     */
    private static Component getCurrentComponent(Container parent) {
        return parent.getComponent(getIndexOfCurrentComponent(parent));
    }

    /**
     * @return the duration of the fade animation in ms
     */
    public long getFadeDuration() {
        return fadeDuration;
    }

    /**
     * @param fadeDuration the duration of the fade animation in ms
     */
    public void setFadeDuration(long fadeDuration) {
        this.fadeDuration = fadeDuration;
    }
}
