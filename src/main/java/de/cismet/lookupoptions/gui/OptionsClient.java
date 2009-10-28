package de.cismet.lookupoptions.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import de.cismet.lookupoptions.OptionsCategory;
import de.cismet.lookupoptions.OptionsPanelController;
import de.cismet.tools.configuration.Configurable;
import de.cismet.tools.configuration.NoWriteError;
import java.util.Hashtable;
import org.openide.util.Lookup;
import org.jdom.Element;

/**
 * This class provides some methods for interaction between the options dialog
 * and the OptionsCategory- and OptionsController- LookupServices.
 * @author jruiz
 */
public class OptionsClient implements Configurable {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private static OptionsClient instance = new OptionsClient();
    private Hashtable<Class<? extends OptionsCategory>, OptionsCategory> categoriesTable = new Hashtable<Class<? extends OptionsCategory>, OptionsCategory>();
    private ArrayList<OptionsPanelController> controllerList = new ArrayList<OptionsPanelController>();
    private static final String CONFIGURATION = "cismetLookupOptions";

    /**
     * Makes a lookup for option categories and controllers.
     */
    private OptionsClient() {
        // Kategorien in Hashtable speichern, sodass zu jeder Kategorie-Klasse genau eine Instanz gehört.
        Collection<? extends OptionsCategory> categories = Lookup.getDefault().lookupAll(OptionsCategory.class);
        for (OptionsCategory category : categories) {
            categoriesTable.put(category.getClass(), category);
        }

        controllerList.addAll(Lookup.getDefault().lookupAll(OptionsPanelController.class));
    }

    public static OptionsClient getInstance() {
        return instance;
    }

    /**
     * Invokes the apply-method of each controller whose isChanged method returns true.
     * This method will be invoked when the OK-Button of the options dialog is pressed.
     */
    public void applyAll() {
        // für jeden Controller
        for (OptionsPanelController controller : controllerList) {
            // prüfen ob isChanged true ist
            if (controller.isChanged()) {
                // gegebenenfalls applyChanges aufrufen
                controller.applyChanges();
            }
        }
    }

    /**
     * Invokes the cancel-method of each controller whose isChanged method returns true.
     * This method will be invoked when the Cancel-Button of the options dialog is pressed.
     */
    public void cancelAll() {
        // für jeden Controller
        for (OptionsPanelController controller : controllerList) {
            // prüfen ob isChanged true ist
            if (controller.isChanged()) {
                // gegebenenfalls cancel aufrufen
                controller.cancel();
            }
        }
    }

    /**
     * Invokes the update-method of each controller of a category whose isChanged method returns true.
     * This method will be invoked when switching categories in the options dialog.
     * @param categoryClass the category of the controller
     */
    public void update(final Class<? extends OptionsCategory> categoryClass) {
        // für jeden Controller
        for (OptionsPanelController controller : controllerList) {
            // prüfen ob die Kategorie mit der übergebenen übereinstimmt, und ob isChanged true ist
            if (controller.getCategoryClass().equals(categoryClass) && !controller.isChanged()) {
                // gegebenenfalls update aufrufen
                controller.update();
            }
        }
    }

    /**
     * Returns the option categories, sorted by order.
     * @return category-array, sorted by order
     */
    public OptionsCategory[] getCategories() {
        // neue zu sortierte Liste mit den Werten der Original-Liste erzeugen
        ArrayList<OptionsCategory> sortedCategories = new ArrayList<OptionsCategory>(categoriesTable.values());
        // die neue Liste sortieren
        Collections.sort(sortedCategories);
        // Liste als Array zurückliefern
        return sortedCategories.toArray(new OptionsCategory[0]);
    }

    public OptionsCategory getCategory(final Class<? extends OptionsCategory> categoryClass) {
        return categoriesTable.get(categoryClass);
    }

    /**
     * Returns the optionspanel controllers of a category, sorted by order.
     * @param categoryClass the options category
     * @return controller-array, sorted by order
     */
    public OptionsPanelController[] getControllers(final Class<? extends OptionsCategory> categoryClass) {
        // neue zu sortierte Liste erzeugen
        ArrayList<OptionsPanelController> sortedPanels = new ArrayList<OptionsPanelController>();
        // für jeden Wert der OriginalListe
        for (OptionsPanelController controller : controllerList) {
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

    @Override
    public void configure(Element parent) {
        final Element conf = parent.getChild(CONFIGURATION);
        // für jeden Controller
        for (OptionsPanelController controller : controllerList) {
            controller.configure(conf);
        }
    }

    @Override
    public void masterConfigure(Element parent) {
        final Element conf = parent.getChild(CONFIGURATION);
        // für jeden Controller
        for (OptionsPanelController controller : controllerList) {
            controller.masterConfigure(conf);
        }
    }

    @Override
    public Element getConfiguration() throws NoWriteError {
        log.debug("OptionsClient.getConfiguration");
        Element root = new Element(CONFIGURATION);

        for (Configurable configurable : controllerList) {
        log.debug(" - OptionsClient.getConfiguration");
            try {
                Element element = configurable.getConfiguration();
                if (element != null) {
                    root.addContent(element);
                }
            } catch (Exception t) {
                log.warn("Fehler beim Schreiben der eines Konfigurationsteils.", t);
            }
        }
        return root;
    }

}
