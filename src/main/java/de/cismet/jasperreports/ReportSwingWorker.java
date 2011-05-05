/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.jasperreports;

import Sirius.navigator.plugin.PluginRegistry;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import java.awt.Frame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import de.cismet.cids.dynamics.CidsBean;

import de.cismet.cismap.navigatorplugin.CismapPlugin;

import de.cismet.tools.BrowserLauncher;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ReportSwingWorker extends SwingWorker<Boolean, Object> {

    //~ Static fields/initializers ---------------------------------------------

    private static final String CISMAP_FOLDER = ((CismapPlugin)PluginRegistry.getRegistry().getPlugin("cismap"))
                .getConfigurationManager().getFolder();
    private static String HOME_FOLDER = ((CismapPlugin)PluginRegistry.getRegistry().getPlugin("cismap"))
                .getConfigurationManager().getHome();
    private static String FS = ((CismapPlugin)PluginRegistry.getRegistry().getPlugin("cismap"))
                .getConfigurationManager().getFileSeperator();
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ReportSwingWorker.class);

    //~ Instance fields --------------------------------------------------------

// private Collection<CidsBean> cidsBeans;
// private String compiledReport;
    private final List<Collection<CidsBean>> cidsBeansList;
    private final List<String> compiledReportList;
    private final ReportSwingWorkerDialog dialog;
    private final boolean withDialog;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ReportSwingWorker object.
     *
     * @param  cidsBeansList       map DOCUMENT ME!
     * @param  compiledReportList  DOCUMENT ME!
     */
    public ReportSwingWorker(final List<Collection<CidsBean>> cidsBeansList, final List<String> compiledReportList) {
        this(cidsBeansList, compiledReportList, false, null);
    }

    /**
     * Creates a new ReportSwingWorker object.
     *
     * @param  cidsBeans       DOCUMENT ME!
     * @param  compiledReport  DOCUMENT ME!
     */
    public ReportSwingWorker(final Collection<CidsBean> cidsBeans, final String compiledReport) {
        this(cidsBeans, compiledReport, false, null);
    }

    /**
     * Creates a new ReportSwingWorker object.
     *
     * @param  cidsBeansList       DOCUMENT ME!
     * @param  compiledReportList  DOCUMENT ME!
     * @param  parent              DOCUMENT ME!
     */
    public ReportSwingWorker(final List<Collection<CidsBean>> cidsBeansList,
            final List<String> compiledReportList,
            final Frame parent) {
        this(cidsBeansList, compiledReportList, true, parent);
    }

    /**
     * Creates a new ReportSwingWorker object.
     *
     * @param  cidsBeans       DOCUMENT ME!
     * @param  compiledReport  DOCUMENT ME!
     * @param  parent          DOCUMENT ME!
     */
    public ReportSwingWorker(final Collection<CidsBean> cidsBeans, final String compiledReport, final Frame parent) {
        this(cidsBeans, compiledReport, true, parent);
    }

    /**
     * Creates a new ReportSwingWorker object.
     *
     * @param  cidsBeansList       DOCUMENT ME!
     * @param  compiledReportList  DOCUMENT ME!
     * @param  withDialog          DOCUMENT ME!
     * @param  parent              DOCUMENT ME!
     */
    public ReportSwingWorker(final List<Collection<CidsBean>> cidsBeansList,
            final List<String> compiledReportList,
            final boolean withDialog,
            final Frame parent) {
        this.cidsBeansList = cidsBeansList;
        this.compiledReportList = compiledReportList;
        this.withDialog = withDialog;
        if (withDialog) {
            dialog = new ReportSwingWorkerDialog(parent, true);
        } else {
            dialog = null;
        }
    }

    /**
     * Creates a new ReportSwingWorker object.
     *
     * @param  cidsBeans       DOCUMENT ME!
     * @param  compiledReport  DOCUMENT ME!
     * @param  withDialog      DOCUMENT ME!
     * @param  parent          DOCUMENT ME!
     */
    public ReportSwingWorker(final Collection<CidsBean> cidsBeans,
            final String compiledReport,
            final boolean withDialog,
            final Frame parent) {
        this.cidsBeansList = new ArrayList<Collection<CidsBean>>();
        this.cidsBeansList.add(cidsBeans);
        this.compiledReportList = new ArrayList<String>();
        this.compiledReportList.add(compiledReport);
        this.withDialog = withDialog;
        if (withDialog) {
            dialog = new ReportSwingWorkerDialog(parent, true);
        } else {
            dialog = null;
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Boolean doInBackground() throws Exception {
        if (withDialog) {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        dialog.setLocationRelativeTo(dialog.getParent());
                        dialog.setVisible(true);
                    }
                });
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        FileOutputStream fos = null;
        try {
            final List<InputStream> ins = new ArrayList<InputStream>();
            for (int index = 0; index < compiledReportList.size(); index++) {
                final String report = compiledReportList.get(index);
                final Collection<CidsBean> beans = cidsBeansList.get(index);

                // report holen
                final JasperReport jasperReport = (JasperReport)JRLoader.loadObject(ReportSwingWorker.class
                                .getResourceAsStream(report));
                // daten vorbereiten
                final JRDataSource dataSource = new CidsBeanDataSource(beans);
                // print aus report und daten erzeugen
                final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), dataSource);
                // quer- bzw hochformat übernehmen
                jasperPrint.setOrientation(jasperReport.getOrientation());

                // zum pdfStream exportieren und der streamliste hinzufügen
                final ByteArrayOutputStream outTmp = new ByteArrayOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, outTmp);
                ins.add(new ByteArrayInputStream(outTmp.toByteArray()));
                outTmp.close();
            }
            // pdfStreams zu einem einzelnen pdfStream zusammenfügen
            concatPDFs(ins, out, true);

            // zusammengefügten pdfStream in Datei schreiben
            final File file = new File(HOME_FOLDER + FS + CISMAP_FOLDER + FS + "report.pdf");
            file.getParentFile().mkdirs();
            fos = new FileOutputStream(file);
            fos.write(out.toByteArray());

            // Datei über Browser öffnen
            BrowserLauncher.openURL("file:///" + file);
            return true;
        } catch (IOException ex) {
            LOG.error("Export to PDF-Stream failed.", ex);
        } catch (JRException ex) {
            LOG.error("Export to PDF-Stream failed.", ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
                LOG.error("error while closing streams", ex);
            }
        }

        return false;
    }

    @Override
    protected void done() {
        boolean error = false;
        try {
            error = !get();
        } catch (InterruptedException ex) {
            // unterbrochen, nichts tun
        } catch (ExecutionException ex) {
            error = true;
            LOG.error("error while generating report", ex);
        }
        if (withDialog) {
            dialog.setVisible(false);
        }
        if (error) {
            JOptionPane.showMessageDialog(
                dialog.getParent(),
                "Beim Generieren des Reports ist ein Fehler aufgetreten.",
                "Fehler!",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  streamOfPDFFiles  DOCUMENT ME!
     * @param  outputStream      DOCUMENT ME!
     * @param  paginate          DOCUMENT ME!
     */
    private static void concatPDFs(final List<InputStream> streamOfPDFFiles,
            final OutputStream outputStream,
            final boolean paginate) {
        int totalNumOfPages = 0;
        final Document document = new Document();
        final List<InputStream> inputStreams = streamOfPDFFiles;
        final List<PdfReader> pdfReaders = new ArrayList<PdfReader>();

        try {
            final PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            for (final InputStream pdf : inputStreams) {
                final PdfReader pdfReader = new PdfReader(pdf);
                pdfReaders.add(pdfReader);
                totalNumOfPages += pdfReader.getNumberOfPages();
            }

            document.open();
            final BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            final PdfContentByte contentByte = writer.getDirectContent();

            PdfImportedPage page;
            int currentPageNumber = 0;

            for (final PdfReader pdfReader : pdfReaders) {
                int currentNumOfPages = 0;
                while (currentNumOfPages < pdfReader.getNumberOfPages()) {
                    currentNumOfPages++;
                    currentPageNumber++;

                    document.setPageSize(pdfReader.getPageSizeWithRotation(currentNumOfPages));
                    document.newPage();

                    page = writer.getImportedPage(pdfReader, currentNumOfPages);
                    contentByte.addTemplate(page, 0, 0);

                    if (paginate) {
                        contentByte.beginText();
                        contentByte.setFontAndSize(baseFont, 9);
                        contentByte.showTextAligned(
                            PdfContentByte.ALIGN_CENTER,
                            currentPageNumber
                                    + " of "
                                    + totalNumOfPages,
                            520,
                            5,
                            0);
                        contentByte.endText();
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("error while merging pdfs", ex);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                LOG.error("error whil closing pdfstream", ex);
            }
        }
    }
}
