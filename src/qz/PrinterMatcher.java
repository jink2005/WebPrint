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

import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableSet;
import javafx.print.Printer;

public class PrinterMatcher {

    private static ObservableSet<Printer> printers = null;
    private static String printerListing = "";

    /**
     * Finds a printer in the PrintServices listing and returns it's respective
     * PrintService.<p>  If a PrintService is supplied, the same PrintService is
     * returned.  If an Object is supplied, it calls the "toString()" method and
     * then does a standard name search.</p>
     * @param o The object holding the name of the printer to search for.
     * @return PrintService ps for RawPrint(ps, cmds)
     */
    public static Printer findPrinter(Object o) {
        Printer exact = null;
        Printer begins = null;
        Printer partial = null;
      
        String printerName;
        if (o == null) {
            return null;
        } else if (o instanceof String) {
            printerName = "\\Q" + (String) o + "\\E";
        } else if (o instanceof Printer) {
            return (Printer) o;
        } else {
            printerName = "\\Q" + o.toString() + "\\E";
        }

        // Get print service list
        getPrinterList();

        LogIt.log(Level.INFO, "Found " + printers.size() + " attached printers.");
        LogIt.log(Level.INFO, "Printer specified: " + printerName);

        Pattern p1 = Pattern.compile("\\b" + printerName + "\\b", Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile("\\b" + printerName, Pattern.CASE_INSENSITIVE);
        Pattern p3 = Pattern.compile(printerName, Pattern.CASE_INSENSITIVE);
        
        // Search for best match by name
        for (Printer printer : printers) {
            String sysPrinter = printer.getName();

            Matcher m1 = p1.matcher(sysPrinter);
            Matcher m2 = p2.matcher(sysPrinter);
            Matcher m3 = p3.matcher(sysPrinter);

            if (m1.find()) {
                exact = printer;
                LogIt.log("Printer name match: " + sysPrinter);
            } else if (m2.find()) {
                begins = printer;
                LogIt.log("Printer name beginning match: " + sysPrinter);
            } else if (m3.find()) {
                partial = printer;
                LogIt.log("Printer name partial match: " + sysPrinter);
            }
        }
        
        // Return closest match
        if (exact != null) {
            LogIt.log("Using best match: " + exact.getName());
            return exact;
        } else if (begins != null) {
            LogIt.log("Using best match: " + begins.getName());
            return begins;
        } else if (partial != null) {
            LogIt.log("Using best match: " + partial.getName());
            return partial;
        }

        // Couldn't find printer
        LogIt.log(Level.WARNING, "Printer not found: " + printerName);
        return null;
    }

    public static ObservableSet<Printer> getPrinterList() {
        return getPrinterArray(false);
    }

    public static ObservableSet<Printer> getPrinterArray(boolean forceSearch) {
        ObservableSet<Printer> printers = Printer.getAllPrinters();
        printerListing = "";
        for (Printer printer : printers) {
            if (! Objects.equals(printerListing, "")) {
                printerListing = printerListing + ",";
            }
            printerListing = printerListing + printer.getName();
        }
        
        return printers;
    }

    /**
     * Returns a CSV format of printer names, convenient for JavaScript
     * @return
     */
    public static String getPrinterListing() {
        return printerListing;
    }
}
