/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.lookupoptions.gui;

import org.jdom.Element;

import org.openide.util.Lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import de.cismet.connectioncontext.ClientConnectionContextStore;

import de.cismet.lookupoptions.OptionsCategory;
import de.cismet.lookupoptions.OptionsPanelController;

import de.cismet.tools.configuration.Configurable;
import de.cismet.tools.configuration.NoWriteError;

/**
 * This class provides some methods for interaction between the options dialog and the OptionsCategory- and
 * OptionsController- LookupServices.
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class OptionsClient implements Configurable {

    //~ Static fields/initializers ---------------------------------------------

    private static final OptionsClient INSTANCE = new OptionsClient();
    private static final String CONFIGURATION = "cismetLookupOptions"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final Hashtable<Class<? extends OptionsCategory>, OptionsCategory> categoriesTable = new Hashtable<>();
    private final ArrayList<OptionsPanelController> controllerList = new ArrayList<>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Makes a lookup for option categories and controllers.
     */
    private OptionsClient() {
        // Kategorien in Hashtable speichern, sodass zu jeder Kategorie-Klasse genau eine Instanz gehört.
        final Collection<? extends OptionsCategory> categories = Lookup.getDefault().lookupAll(OptionsCategory.class);
        for (final OptionsCategory category : categories) {
            categoriesTable.put(category.getClass(), category);
        }

        for (final OptionsPanelController controller : Lookup.getDefault().lookupAll(OptionsPanelController.class)) {
            if (controller instanceof ClientConnectionContextStore) {
                ((ClientConnectionContextStore)controller).setConnectionContext(new OptionsConnectionContext(
                        controller.getClass().getSimpleName()));
                ((ClientConnectionContextStore)controller).initAfterConnectionContext();
            }
            if (controller.isEnabled()) {
                controllerList.add(controller);
            }
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static OptionsClient getInstance() {
        return INSTANCE;
    }

    /**
     * Invokes the apply-method of each controller whose isChanged method returns true. This method will be invoked when
     * the OK-Button of the options dialog is pressed.
     */
    public void applyAll() {
        // für jeden Controller
        for (final OptionsPanelController controller : controllerList) {
            // prüfen ob isChanged true ist
            if (controller.isChanged()) {
                // gegebenenfalls applyChanges aufrufen
                controller.applyChanges();
            }
        }
    }

    /**
     * Invokes the cancel-method of each controller whose isChanged method returns true. This method will be invoked
     * when the Cancel-Button of the options dialog is pressed.
     */
    public void cancelAll() {
        // für jeden Controller
        for (final OptionsPanelController controller : controllerList) {
            // prüfen ob isChanged true ist
            if (controller.isChanged()) {
                // gegebenenfalls cancel aufrufen
                controller.cancel();
            }
        }
    }

    /**
     * Invokes the update-method of each controller of a category whose isChanged method returns true. This method will
     * be invoked when switching categories in the options dialog.
     *
     * @param  categoryClass  the category of the controller
     */
    public void update(final Class<? extends OptionsCategory> categoryClass) {
        // für jeden Controller
        for (final OptionsPanelController controller : controllerList) {
            // prüfen ob die Kategorie mit der übergebenen übereinstimmt, und ob isChanged true ist
            if (controller.getCategoryClass().equals(categoryClass) && !controller.isChanged()) {
                // gegebenenfalls update aufrufen
                controller.update();
            }
        }
    }

    /**
     * Returns the option categories, sorted by order.
     *
     * @return  category-array, sorted by order
     */
    public OptionsCategory[] getCategories() {
        // neue zu sortierte Liste mit den Werten der Original-Liste erzeugen
        final ArrayList<OptionsCategory> sortedCategories = new ArrayList<OptionsCategory>(categoriesTable.values());
        // die neue Liste sortieren
        Collections.sort(sortedCategories);
        // Liste als Array zurückliefern
        return sortedCategories.toArray(new OptionsCategory[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   categoryClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public OptionsCategory getCategory(final Class<? extends OptionsCategory> categoryClass) {
        return categoriesTable.get(categoryClass);
    }

    /**
     * Returns the optionspanel controllers of a category, sorted by order.
     *
     * @param   categoryClass  the options category
     *
     * @return  controller-array, sorted by order
     */
    public OptionsPanelController[] getControllers(final Class<? extends OptionsCategory> categoryClass) {
        // neue zu sortierte Liste erzeugen
        final ArrayList<OptionsPanelController> sortedPanels = new ArrayList<OptionsPanelController>();
        // für jeden Wert der OriginalListe
        for (final OptionsPanelController controller : controllerList) {
            // prüfen ob die Kategorie mit der übergebenen übereinstimmt
            if (controller.getCategoryClass().equals(categoryClass)) {
                // bei Übereinstimmung den Controller der Liste hinzufügen
                sortedPanels.add(controller);
            }
        }
        // die neue Liste sortieren
        Collections.sort(sortedPanels);
        // Liste als Array zurückliefern
        return sortedPanels.toArray(new OptionsPanelController[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  parent  DOCUMENT ME!
     */
    @Override
    public void configure(final Element parent) {
        final Element conf = parent.getChild(CONFIGURATION);
        // für jeden Controller
        for (final OptionsPanelController controller : controllerList) {
            controller.configure(conf);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  parent  DOCUMENT ME!
     */
    @Override
    public void masterConfigure(final Element parent) {
        final Element conf = parent.getChild(CONFIGURATION);
        // für jeden Controller
        for (final OptionsPanelController controller : controllerList) {
            controller.masterConfigure(conf);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NoWriteError  DOCUMENT ME!
     */
    @Override
    public Element getConfiguration() throws NoWriteError {
        if (log.isDebugEnabled()) {
            log.debug("OptionsClient.getConfiguration"); // NOI18N
        }
        final Element root = new Element(CONFIGURATION);

        for (final Configurable configurable : controllerList) {
            if (log.isDebugEnabled()) {
                log.debug(" - OptionsClient.getConfiguration");                      // NOI18N
            }
            try {
                final Element element = configurable.getConfiguration();
                if (element != null) {
                    root.addContent(element);
                }
            } catch (Exception t) {
                log.warn("Fehler beim Schreiben der eines Konfigurationsteils.", t); // NOI18N
            }
        }
        return root;
    }
}
