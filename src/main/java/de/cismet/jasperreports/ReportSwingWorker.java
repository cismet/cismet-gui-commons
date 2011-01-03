/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.jasperreports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ReportSwingWorker extends SwingWorker<JasperPrint, Object> {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ReportSwingWorker.class);

    //~ Instance fields --------------------------------------------------------

    private CidsBean cidsBean;
    private String compiledReport;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReportSwingWorker object.
     *
     * @param  cidsBean        DOCUMENT ME!
     * @param  compiledReport  DOCUMENT ME!
     */
    public ReportSwingWorker(final CidsBean cidsBean, final String compiledReport) {
        this.cidsBean = cidsBean;
        this.compiledReport = compiledReport;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected JasperPrint doInBackground() throws Exception {
        JasperPrint jasperPrint = null;
        try {
            final JasperReport jasperReport = (JasperReport)JRLoader.loadObject(ReportSwingWorker.class
                            .getResourceAsStream(compiledReport));
            final JRDataSource dataSource = new CidsBeanDataSource(new CidsBean[] { cidsBean });

            jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);
        } catch (Throwable t) {
            LOG.error("Error while generating report.", t);
        }
        return jasperPrint;
    }

    @Override
    protected void done() {
        try {
            final JRViewer aViewer = new JRViewer(get());
            aViewer.setZoomRatio(1F);
            final JFrame aFrame = new JFrame("Druckvorschau");
            aFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            aFrame.getContentPane().add(aViewer);
            final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            aFrame.setSize(screenSize.width / 2, screenSize.height - 100);
            final java.awt.Insets insets = aFrame.getInsets();
            aFrame.setSize(aFrame.getWidth() + insets.left + insets.right,
                aFrame.getHeight()
                        + insets.top
                        + insets.bottom
                        + 20);
            aFrame.setLocation((screenSize.width - aFrame.getWidth()) / 2,
                (screenSize.height - aFrame.getHeight())
                        / 2);
            aFrame.setVisible(true);
        } catch (InterruptedException ex) {
            LOG.error("Report generation was interrupted.", ex);
        } catch (ExecutionException ex) {
            LOG.error("An execution error occurred while generating a report.", ex);
        }
    }
}
