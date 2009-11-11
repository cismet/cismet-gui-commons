/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.tools.gui.breadcrumb;

/**
 *
 * @author thorsten
 */
public class BreadCrumbEvent {
    private BreadCrumbModel source;
    private BreadCrumb breadCrumb;

    public BreadCrumbEvent(BreadCrumbModel source) {
        this.source = source;
    }


    public BreadCrumbEvent(BreadCrumbModel source, BreadCrumb breadCrumb) {
        this.source = source;
        this.breadCrumb = breadCrumb;
    }


    public BreadCrumb getBreadCrumb() {
        return breadCrumb;
    }

    public void setBreadCrumb(BreadCrumb breadCrumb) {
        this.breadCrumb = breadCrumb;
    }

    public BreadCrumbModel getSource() {
        return source;
    }

    public void setSource(BreadCrumbModel source) {
        this.source = source;
    }


}
