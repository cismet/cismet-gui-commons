/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.wizard;

import org.openide.WizardDescriptor;
import org.openide.awt.Mnemonics;

import java.lang.reflect.Field;

import javax.swing.AbstractButton;

/**
 * Some utilities that are helpful when working with the NetBeans Wizard API.
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public final class WizardUtils {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WizardUtils object.
     */
    private WizardUtils() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Setter for the button texts of a wizard. Currently supported buttons are:<br/>
     *
     * <ul>
     *   <li>{@link WizardDescriptor#FINISH_OPTION}</li>
     *   <li>{@link WizardDescriptor#CANCEL_OPTION}</li>
     *   <li>{@link WizardDescriptor#PREVIOUS_OPTION}</li>
     *   <li>{@link WizardDescriptor#NEXT_OPTION}</li>
     * </ul>
     * <br/>
     * <br/>
     * For more info on the possible options regarding the new text see
     * {@link Mnemonics#setLocalizedText(javax.swing.AbstractButton, java.lang.String) }.<br/>
     * <br/>
     * <i><b>IMPORTANT:</b> As the wizard api currently does not support changes of the button text individually (per
     * instance) this utility operation does it the "dirty" way via reflection. Thus this implementation is currently
     * valid for the 'org-openide-dialogs' artifact in version 'RELEASE701' and may break when any other version is
     * used. Thus this operation shall be removed when an official way is provided. Discussion at <a href="
     * http://forums.netbeans.org/viewtopic.php?t=55192&start=0&postdays=0&postorder=asc&highlight=">NetBeans User
     * Forum</a></i>
     *
     * @param   wizard  the wizard instance that shall be affected by the change
     * @param   option  the button type whose text shall be set
     * @param   text    the text for the button
     *
     * @throws  IllegalStateException  if any error occurs during text change
     *
     * @see     Mnemonics#setLocalizedText(javax.swing.AbstractButton, java.lang.String)
     */
    public static void setCustomButtonText(final WizardDescriptor wizard,
            final Object option,
            final String text) {
        try {
            final Field buttonField;
            if (WizardDescriptor.FINISH_OPTION.equals(option)) {
                buttonField = wizard.getClass().getDeclaredField("finishButton");    // NOI18N
            } else if (WizardDescriptor.CANCEL_OPTION.equals(option)) {
                buttonField = wizard.getClass().getDeclaredField("cancelButton");    // NOI18N
            } else if (WizardDescriptor.PREVIOUS_OPTION.equals(option)) {
                buttonField = wizard.getClass().getDeclaredField("previousButton");  // NOI18N
            } else if (WizardDescriptor.NEXT_OPTION.equals(option)) {
                buttonField = wizard.getClass().getDeclaredField("nextButton");      // NOI18N
            } else {
                throw new IllegalArgumentException("unsupported option: " + option); // NOI18N
            }

            final boolean accessible = buttonField.isAccessible();
            buttonField.setAccessible(true);
            final AbstractButton button = (AbstractButton)buttonField.get(wizard);
            buttonField.setAccessible(accessible);

            Mnemonics.setLocalizedText(button, text);
        } catch (final Exception ex) {
            throw new IllegalStateException("cannot set customized text: [wizard=" + wizard + "|optionButton=" // NOI18N
                        + option + "|text=" + text + "]", // NOI18N
                ex);
        }
    }
}
