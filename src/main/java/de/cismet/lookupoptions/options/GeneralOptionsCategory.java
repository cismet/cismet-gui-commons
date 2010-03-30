package de.cismet.lookupoptions.options;

import de.cismet.lookupoptions.*;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 * Represents the general category in Options Dialog.
 * @author jruiz
 */
@ServiceProvider(service = OptionsCategory.class)
public class GeneralOptionsCategory extends AbstractOptionsCategory {

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(GeneralOptionsCategory.class, "GeneralOptionsCategory.name"); //NOI18N
    }

    @Override
    public Icon getIcon() {
        Image image = ImageUtilities.loadImage("de/cismet/lookupoptions/options/general.png"); //NOI18N
        if (image != null) {
            return new ImageIcon(image);
        } else {
            return null;
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
