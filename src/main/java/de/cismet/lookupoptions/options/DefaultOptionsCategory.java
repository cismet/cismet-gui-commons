/***************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 *
 *              ... and it just works.
 *
 ****************************************************/
package de.cismet.lookupoptions.options;

import de.cismet.lookupoptions.*;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 * Represents the default category which is used in Options Dialog when no category is assigned to an option panel.
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = OptionsCategory.class)
public class DefaultOptionsCategory extends AbstractOptionsCategory {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(DefaultOptionsCategory.class, "DefaultOptionsCategory.name"); // NOI18N
    }

    @Override
    public Icon getIcon() {
        final Image image = ImageUtilities.loadImage("de/cismet/lookupoptions/options/nocat.png"); // NOI18N
        if (image != null) {
            return new ImageIcon(image);
        } else {
            return null;
        }
    }

    @Override
    public String getTooltip() {
        return org.openide.util.NbBundle.getMessage(DefaultOptionsCategory.class, "DefaultOptionsCategory.tooltip"); // NOI18N
    }
}
