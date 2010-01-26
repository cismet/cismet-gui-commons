package de.cismet.lookupoptions.options;

import de.cismet.lookupoptions.*;
import java.awt.Image;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 * Represents the default category which is used in Options Dialog when no
 * category is assigned to an option panel.
 * @author jruiz
 */
@ServiceProvider(service = OptionsCategory.class)
public class DefaultOptionsCategory extends AbstractOptionsCategory {

    private static final ResourceBundle I18N =
            ResourceBundle.getBundle("de/cismet/CismetCommonsBundle");

    @Override
    public String getName() {
        return I18N.getString("de.cismet.lookupoptions.options.DefaultOptionsCategory.getName().return");
    }

    @Override
    public Icon getIcon() {
        Image image = ImageUtilities.loadImage("de/cismet/lookupoptions/options/nocat.png");
        if (image != null) {
            return new ImageIcon(image);
        } else {
            return null;
        }
    }

}
