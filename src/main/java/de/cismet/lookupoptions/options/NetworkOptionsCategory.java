package de.cismet.lookupoptions.options;

import de.cismet.lookupoptions.AbstractOptionsCategory;
import de.cismet.lookupoptions.OptionsCategory;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 * Example Category
 * @author jruiz
 */
@ServiceProvider(service = OptionsCategory.class)
public class NetworkOptionsCategory extends AbstractOptionsCategory {

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(NetworkOptionsCategory.class, "NetworkOptionsCategory.name");
    }

    @Override
    public Icon getIcon() {
        Image image = ImageUtilities.loadImage("de/cismet/lookupoptions/options/network.png");
        if (image != null) {
            return new ImageIcon(image);
        } else {
            return null;
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
