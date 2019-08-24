/**
 * @author Tres Finocchiaro
 *
 * Copyright (C) 2013 Tres Finocchiaro, QZ Industries
 *
 * IMPORTANT:  This software is dual-licensed
 *
 * LGPL 2.1
 * This is free software.  This software and source code are released under
 * the "LGPL 2.1 License".  A copy of this license should be distributed with
 * this software. http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * QZ INDUSTRIES SOURCE CODE LICENSE
 * This software and source code *may* instead be distributed under the
 * "QZ Industries Source Code License", available by request ONLY.  If source
 * code for this project is to be made proprietary for an individual and/or a
 * commercial entity, written permission via a copy of the "QZ Industries Source
 * Code License" must be obtained first.  If you've obtained a copy of the
 * proprietary license, the terms and conditions of the license apply only to
 * the licensee identified in the agreement.  Only THEN may the LGPL 2.1 license
 * be voided.
 *
 */
package qz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker.State;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Fidelity;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;
import javax.swing.JFrame;

public class PrintHTML extends JFXPanel implements Printable {

    private final AtomicReference<PrintService> ps = new AtomicReference<PrintService>(null);
    private final AtomicReference<String> jobName = new AtomicReference<String>("WebPrinter 2D Printing");
    private final AtomicInteger orientation = new AtomicInteger(PageFormat.PORTRAIT);
    private final AtomicReference<String> htmlData = new AtomicReference<String>(null);
    //private final AtomicReference<Paper> paper = new AtomicReference<Paper>(null);
    private final AtomicReference<Boolean> contentReady = new AtomicReference<Boolean>(false);
    private WebView webView = null;
    private JFrame j = null;

    public PrintHTML() {
        super();
        super.setOpaque(true);
        super.setBackground(Color.WHITE);
        super.setBorder(null);

        j = new JFrame();
        j.setUndecorated(true);
//        j.setLayout(new FlowLayout());
        j.add(this);

        createJFXPanelWebViewScene(this);
    }

    public void createJFXPanelWebViewScene(final JFXPanel jfxPanel) {
        Platform.setImplicitExit(false); // 解决不能二次打印问题
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webView = new WebView();
                webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
                    @Override
                    public void changed(ObservableValue ov, State oldState, State newState) {
                        if (newState == State.SUCCEEDED) {
                            System.out.println("WebEngine state change SUCCEEDED.");
                            contentReady.set(true);
                        }
                    }
                });
                jfxPanel.setScene(new Scene(webView));
            }
        });
    }

    public void jfxPanelWebViewLoadContent(final JFXPanel jfxPanel, final String contetnt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("jfxPanelWebViewLoadContent run...");
                WebView webView = (WebView) jfxPanel.getScene().getRoot();
                webView.getEngine().loadContent(contetnt);
            }
        });
    }

    public void set(String html) {
        htmlData.set(html);
    }

    //public void append(String html) {
    //    super.setText(super.getText() == null ? html : super.getText() + html);
    // }
    // public void clear() {
    //     super.setText(null);
    // }
    public void clear() {
        htmlData.set(null);
    }

    public String get() {
        return htmlData.get();
    }

    //public String get() {
    //    return super.getText();
    //}
    public void print() {
        System.out.println("print load jxpanel content...");
        contentReady.set(false);
        jfxPanelWebViewLoadContent(this, htmlData.get());

        try {
            while (!contentReady.get()) {
                System.out.println("waiting content...");
                Thread.sleep(200);
            }
            fxWebEngingPrint();
//            framePrint();
        } catch (Exception ex) {
            Logger.getLogger(PrintHTML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 使用JavaFx的WebEngine打印web完整内容
     * 使用oracle java 8能正常打印，mac不行
     */
    private void fxWebEngingPrint() {
        javafx.print.PrinterJob job = javafx.print.PrinterJob.createPrinterJob();
        if (job != null) {
            this.webView.getEngine().print(job);
            job.endJob();
        }
    }
    
    /**
     * 使用awt用打印控件图片的方式打印，只能打印jxpanel显示的部分，无法打印完整网页
     * @throws PrinterException 
     */
    private void framePrint() throws PrinterException {
        System.out.println("print begin ...");
        j.setTitle(jobName.get());

        j.pack();
        j.setExtendedState(j.ICONIFIED);
        j.setVisible(true);

        // Elimate any margins
        HashPrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(new PrinterResolution(300, 300, PrinterResolution.DPI));
        attr.add(new MediaPrintableArea(0f, 0f, getWidth() / 72f, getHeight() / 72f, MediaPrintableArea.INCH));
        attr.add(PrintQuality.HIGH);
//        attr.add(Fidelity.FIDELITY_TRUE);
        System.out.println("print ...W-" + getWidth() / 72f + ",H-" + getHeight() / 72f);

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintService(ps.get());
        job.setPrintable(this);
        job.setJobName(jobName.get());
        job.print(attr);
//        j.setVisible(false);

//        j.dispose();
        clear();
        System.out.println("print end.");
    }

    public void setPrintParameters(PrintManager a) {
        this.ps.set(a.getPrintService());
        this.jobName.set(a.getJobName().replace(" ___ ", " HTML "));
    }

    public void setPrintService(PrintService ps) {
        this.ps.set(ps);
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (g == null) {
            throw new PrinterException("No graphics specified");
        }
        if (pf == null) {
            throw new PrinterException("No page format specified");
        }
        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        }

        System.out.println("print: p-" + pageIndex + ", w-" + pf.getWidth() + ", h-" + pf.getHeight() + ", ori-" + pf.getOrientation());

        boolean doubleBuffered = super.isDoubleBuffered();
        super.setDoubleBuffered(false);

//        pf.setOrientation(orientation.get());
        //Paper paper = new Paper();
        //paper.setSize(8.5 * 72, 11 * 72);
        //paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        //pf.setPaper(paper);
        //Paper paper = pf.getPaper();
        //paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        //pf.getPaper().setImageableArea(0, 0, paper.getWidth() + 200, paper.getHeight() + 200);
        //pf.getPaper().setImageableArea(-100, -100, 200, 200);
        Graphics2D g2d = (Graphics2D) g;
        final AffineTransform trans = g2d.getDeviceConfiguration().getNormalizingTransform();
        System.out.println(trans.getScaleX() * 72 + " DPI horizontally");
        System.out.println(trans.getScaleY() * 72 + " DPI vertically");

        g2d.translate(pf.getImageableX(), pf.getImageableY());
        //g2d.translate(paper.getImageableX(), paper.getImageableY());
        double dpiScale = trans.getScaleY() * 72 / 300.0;
        g2d.scale(dpiScale, dpiScale);
        this.getParent().paint(g2d);
        super.setDoubleBuffered(doubleBuffered);
        return (PAGE_EXISTS);
    }
}
