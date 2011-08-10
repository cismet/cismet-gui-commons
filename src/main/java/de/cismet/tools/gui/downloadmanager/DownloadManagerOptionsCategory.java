/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.downloadmanager;

import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.cismet.lookupoptions.AbstractOptionsCategory;
import de.cismet.lookupoptions.OptionsCategory;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = OptionsCategory.class)
public class DownloadManagerOptionsCategory extends AbstractOptionsCategory {

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getName() {
        return NbBundle.getMessage(
                DownloadManagerOptionsCategory.class,
                "DownloadManagerOptionsCategory.name");
    }

    @Override
    public Icon getIcon() {
        final Image image = ImageUtilities.loadImage("de/cismet/tools/gui/downloadmanager/res/download.png");
        if (image != null) {
            return new ImageIcon(image);
        } else {
            return null;
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
