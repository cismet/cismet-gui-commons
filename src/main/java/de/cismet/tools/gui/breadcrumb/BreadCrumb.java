/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.breadcrumb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 *
 * @author thorsten
 */
public abstract class BreadCrumb extends AbstractAction {
    private Vector<ActionListener> listeners=new Vector<ActionListener>();

    public BreadCrumb(String name, Icon icon) {
        super(name, icon);
    }

    public BreadCrumb(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        crumbActionPerformed(e);
        fireActionPerformed(e);
    }

    public void setIcon(Icon icon){
        this.putValue(BreadCrumb.SMALL_ICON, icon);
    }


    public abstract void crumbActionPerformed(ActionEvent e);

    public void addActionListener(ActionListener al){
        listeners.add(al);
    }
    public void removeActionListener(ActionListener al){
        listeners.remove(al);
    }
    public void fireActionPerformed(ActionEvent e){
        for (ActionListener al:listeners){
            al.actionPerformed(e);
        }
    }
}
