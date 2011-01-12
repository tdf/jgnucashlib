/**
 *
 */
package biz.wolschon.finance.jgnucash.wirecard.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit;
import biz.wolschon.finance.jgnucash.plugin.PluginConfigHelper;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * This class parses Wirecard-reports (clearing and account-statement)
 * and inserts transactions for them.<br/>Assumptions: payout currency is EUR
 */
public class WirecardImporter {


    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(WirecardImporter.class
            .getName());

    /**
     * The date-format used in the pago-files.
     */
    private static final SimpleDateFormat UOS_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * The date-format used in the wirecard-files.
     */
    private static final SimpleDateFormat UOS_DATEFORMAT2 = new SimpleDateFormat("dd.MM.yyyy");


    /**
     * Buffersize when reading streams.
     */
    private static final int BUFFERSIZE = 255;

    /**
     * The book we operate on.
     */
    private GnucashWritableFile myBook;

    /**
     * @param aBook The book we operate on
     */
    public WirecardImporter(final GnucashWritableFile aBook) {
        myBook = aBook;
    }

    /**
     * @param aBook The book we operate on
     * @param pdfFile can be a PDF-file or a ZIP-file containing pdfs.
     * @throws IOException errors reading or writing
     * @throws ParseException errors parsing the pdf-file
     * @throws JAXBException errors reading or writing the xml-datastructures of Gnucash
     */
    public static void importFile(final GnucashWritableFile aBook, final File pdfFile) throws IOException, ParseException, JAXBException {
        if (pdfFile.getName().toLowerCase().endsWith(".zip")) {
            InputStream in = new FileInputStream(pdfFile);
            importZIPStream(aBook, in, false);
            return;
        }
        // for debugging. Work without pdftotext
        if (pdfFile.getName().toLowerCase().endsWith(".txt")) {
            Reader reader = new FileReader(pdfFile);
            BufferedReader buffer = new BufferedReader(reader);
            if (pdfFile.getName().contains("SettlementNote")) {
                importClearingStatement(aBook, buffer);
            } else if (pdfFile.getName().contains("ReserveNote")) {
                importReserveStatement(aBook, buffer);
            } else {
                importAccountStatement(aBook, buffer);
            }
            return;
        }

        Process proc = Runtime.getRuntime()
                .exec(new String[] {"/usr/bin/pdftotext", pdfFile.getAbsolutePath(), "-"});
        Reader reader = new InputStreamReader(proc.getInputStream());
        BufferedReader buffer = new BufferedReader(reader);

        if (pdfFile.getName().toLowerCase().endsWith(".p7s")) {
            return; //skip cryptographic signatures
        }
        if (pdfFile.getName().contains("SettlementNote")) {
            importClearingStatement(aBook, buffer);
        } else if (pdfFile.getName().contains("ReserveNote")) {
            importReserveStatement(aBook, buffer);
        } else {
            importAccountStatement(aBook, buffer);
        }
    }
    /**
     * @param aBook the book to import into
     * @param aBuffer where to read from
     * @throws IOException if we annot read parts
     * @throws JAXBException issues with the XML-backend
     * @throws ParseException if we cannot parse parts
     */
    private static void importReserveStatement(final GnucashWritableFile aBook,
                                               final BufferedReader aBuffer) throws IOException, JAXBException, ParseException {
        if (aBuffer == null) {
            throw new IllegalArgumentException("null buffer given");
        }
        String line = aBuffer.readLine(); // line 1
        if (line == null) {
            throw new IllegalArgumentException(
                    "cannot read a single line from buffer");
        }
        aBuffer.readLine(); // line 2
        aBuffer.readLine();
        aBuffer.readLine();
        aBuffer.readLine();
        aBuffer.readLine();
        line = aBuffer.readLine(); // line 7 = date
        String[] splits = line.split(" ");
        Date date = UOS_DATEFORMAT.parse(splits[0]);
        String currency = splits[splits.length - 1];
        /*aBuffer.readLine(); // line 8
        aBuffer.readLine();
        aBuffer.readLine();
        aBuffer.readLine(); // line 11
        aBuffer.readLine();*/
        line = aBuffer.readLine(); // 2010-05-30 made adaptive due to new PDF-layout
        while (!line.startsWith("EUR EUR")) {
            line = aBuffer.readLine();
        }
        //aBuffer.readLine();
        //aBuffer.readLine();
        aBuffer.readLine();
        line = aBuffer.readLine(); // line 17 (old) line 29 (new)
        splits = line.split(" ");
        FixedPointNumber total;
        try {
            total = new FixedPointNumber(splits[splits.length - 1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse total from \"" + splits[splits.length - 1] + "\" in line \"" + line  + "\"", e);
        }

        GnucashWritableAccount sicherheitseinbehalt = PluginConfigHelper.getOrConfigureAccountWithKey(aBook, "wirecard.sicherheitseinbehalt." + currency, "thisAccount",
                "Please select the account for the accumulated 'Sicherheits-Einbehalt' for currency '" + currency + "'");
//        String sicherheitseinbehalt = "f982b88d02990008053d54d06ebd7049";
//        if (currency.equals("USD")) {
//            sicherheitseinbehalt = "259c916fcf62fd414d2b0ec27f252544";
//        }


        GnucashWritableTransaction trans = aBook.createWritableTransaction();
        trans.setDatePosted(date);
        trans.setDescription("TEST Saldo = " + total + " eur"); // total is given in euro in the document even if for other currencies :/
        GnucashWritableAccount account = sicherheitseinbehalt; //aBook.getAccountByID(sicherheitseinbehalt);
        if (!account.getBalance(date).equals(total)) {
            trans.setDescription(trans.getDescription() + " NAK");
        } else {
            trans.setDescription(trans.getDescription() + " OK");
        }
       trans.createWritingSplit(account);

    }

    /**
     * Import clearing-statements and account-statements compressed
     * in a zip-file.
     * @param aBook the model to import to.
     * @param in the zip-file or zip-stream
     * @param saveExractedFiles save the extracted files to ~/.jGnucash/imported
     * @throws IOException errors reading or writing
     * @throws ParseException errors parsing the pdf-file
     * @throws JAXBException errors reading or writing the xml-datastructures of Gnucash
     */
    public static void importZIPStream(final GnucashWritableFile aBook,
                                        final InputStream in,
                                        final boolean saveExractedFiles) throws IOException,
                                                       ParseException,
                                                       JAXBException {
        ZipInputStream zipIn = new ZipInputStream(in);
        ZipEntry entry = null;
        while ((entry = zipIn.getNextEntry()) != null) {
            String fileName = entry.getName();
            File tempFile = null;
            if (saveExractedFiles) {
                File dir = new File(new File(System.getProperty("user.home"), ".jgnucash"), "imported");
                dir.mkdirs();
                tempFile = new File(dir, entry.getName());
            } else {
                tempFile = File.createTempFile("wirecardImporter_", fileName);
                tempFile.deleteOnExit();
            }
            try {
                FileOutputStream tempOut = new FileOutputStream(tempFile);
               byte[] buffer = new byte[BUFFERSIZE];
               int len = 0;
               while ((len = zipIn.read(buffer)) > 0) {
                   tempOut.write(buffer, 0, len);
               }
               tempOut.close();
               importFile(aBook, tempFile);
            } finally {
                if (!saveExractedFiles) {
                    tempFile.delete();
                }
            }
        }
    }

    /**
     * Import a *_CLE*.pdf -file converted to text using pdftotext containing a clearing.
     * @param aBook The Book we operate on
     * @param aBuffer the import-statement converted to plaintext.
     * @throws IOException errors reading or writing
     * @throws ParseException errors parsing the pdf-file
     * @throws JAXBException errors reading or writing the xml-datastructures of Gnucash
     */
    public static void importClearingStatement(final GnucashWritableFile aBook, final BufferedReader aBuffer) throws IOException, ParseException, JAXBException {
        if (aBuffer == null) {
            throw new IllegalArgumentException("null buffer given");
        }
        String line = aBuffer.readLine();
        if (line == null) {
            throw new IllegalArgumentException(
                    "cannot read a single line from buffer");
        }
        lineMustBeNull(aBuffer);
        lineMustEqual(aBuffer, "Settlement Note");
        lineMustBeNull(aBuffer);
        lineMustEqual(aBuffer, "Settlement ID: Settlement Date: Settlement Period: Merchant Name:");
        lineMustBeNull(aBuffer);
        line = aBuffer.readLine();
        String[] splits = line.split(" ");
        String settlementID = splits[0];
        Date date = UOS_DATEFORMAT.parse(splits[1]);
        String settlementPeriod = splits[2];
        lineMustBeNull(aBuffer);
        line = aBuffer.readLine();
        if (!line.startsWith("Business Case")) {
            throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
        }
        lineMustBeNull(aBuffer);
        lineMustEqual(aBuffer, "Invoice No.");
        lineMustBeNull(aBuffer);
        line = aBuffer.readLine(); // Billing Periods
        if (!line.startsWith("Billing Period")) {
            throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
        }
        splits = line.split(" ");
        List<String> invoiceFrom = new ArrayList<String>((splits.length - 2) / 2);
        List<String> invoiceTo = new ArrayList<String>((splits.length - 2) / 2);
        for (int j = 2; j < splits.length; j++) {
            invoiceFrom.add(splits[j]);
            j++;
            invoiceTo.add(splits[j]);
        }
        lineMustBeNull(aBuffer);
        lineMustEqual(aBuffer, "Comment");
        lineMustBeNull(aBuffer);
        lineMustStartWith(aBuffer, "Invoice Amount Exchange Rate Settlement Amount"); // verify table header
        lineMustBeNull(aBuffer);
        line = aBuffer.readLine(); // customer-name
        lineMustBeNull(aBuffer);

        // the following table is given as columns and has settlements-statements that may be in EUR or currencies with exchange rates as rows
        line = aBuffer.readLine();// transaction-numbers
        List<String> invoiceNumbers = Arrays.asList(line.split(" "));

        lineMustBeNull(aBuffer);

        line = aBuffer.readLine(); // currencies
        List<String> currencies = Arrays.asList(line
                .replaceAll("Negative payout EUR", "Negative_payout_EUR")
                .replaceAll("Reserve payout EUR", "Reserve_payout_EUR")
                .replaceAll("Reserve payout USD", "Reserve_payout_USD")
                .split(" "));
        // special handling of "Negative Paypout" with no invoice-number and no billing period
        if (currencies.size() == (invoiceNumbers.size() - 1)
              && currencies.size() > 0
              && currencies.get(currencies.size() - 1).equals("Negative_payout_EUR")) {
            invoiceNumbers.add("Negative Payout");
            //does not work currencies.set(currencies.size() - 1, "EUR");
            currencies = Arrays.asList(line.replaceAll("Negative payout", "").split(" "));
        }

        lineMustBeNull(aBuffer);

        line = aBuffer.readLine(); // invoiceAmount (values without currencies)
        splits = line.split(" ");
        List<FixedPointNumber> invoiceAmount = new ArrayList<FixedPointNumber>(splits.length);
        for (String split : splits) {
            invoiceAmount.add(new FixedPointNumber(split));
        }

        lineMustBeNull(aBuffer);

        line = aBuffer.readLine(); // exchangeRates "0.69 EUR EUR" = 0.69 for 1st split, ""=1 for second split
        String line2 = line
                  // handle the empty exchange rate (2 currencies without a number in between)
                 .replaceAll("EUR EUR", "EUR 1 EUR")
                 .replaceAll("USD EUR", "UDS 1 EUR")
                 .replaceAll("^EUR", "1 EUR")
                 .replaceAll("EUR EUR", "EUR 1 EUR")
                 .replaceAll("USD EUR", "USD 1 EUR");
        splits = line2.split(" ");
        if (splits.length != 2 * invoiceAmount.size() && splits.length != 2 * invoiceAmount.size() + 2) {
            // +2 because we may or may not have a currency for the "total" in this column
            throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line
                    + "\" transformed to \"" + line2
                    + "\" has no wrong number of elements (value - currency -pairs) expected 2 * " + invoiceAmount.size()
                    + " found " + splits.length);
        }
        List<FixedPointNumber> exchangeRates = new ArrayList<FixedPointNumber>(splits.length / 2);
        for (int j = 0; j < Math.min(currencies.size() * 2, splits.length); j+=2) {
            // Math.min because with total being an additional row, we may have too few currencies for the exchange rates
            if (currencies.get(j / 2).equals("EUR")) {
                // ignore exchange rate for EUR-rows
                exchangeRates.add(new FixedPointNumber(1));
            } else {
                exchangeRates.add(new FixedPointNumber(splits[j]));
            }
        }

        lineMustBeNull(aBuffer);

        line = aBuffer.readLine(); // splits
        splits = line.split(" ");
        List<FixedPointNumber> settlementAmount = new ArrayList<FixedPointNumber>(splits.length);
        for (String split : splits) {
            String value = split;
            if (value.endsWith("*")) { // 12.34* = value not final
                value = value.substring(0, value.length() - 1);
            }
            settlementAmount.add(new FixedPointNumber(value));
        }

        // total payout may be an additional row in the "amount" colum
        // or a text below the table
        FixedPointNumber total = null;
        if (settlementAmount.size() == invoiceAmount.size() + 1) {
            total = settlementAmount.get(settlementAmount.size() - 1);
        } else if (settlementAmount.size() ==  invoiceAmount.size()) {
            lineMustBeNull(aBuffer);
            lineMustEqual(aBuffer, "Total Payout");

            lineMustBeNull(aBuffer);
            lineMustEqual(aBuffer, "EUR");
            lineMustBeNull(aBuffer);
            line = aBuffer.readLine(); // total
            total = new FixedPointNumber(line);
        } else {
            throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\" expected " + invoiceAmount.size() + " or +1 found " + splits.length);
        }


//        String auszahlungAccount = "0682d0d40fefc9af74cae75372b0159d";
//        String auszahlungAccountUSD = "eeda2c814e012144d7e0c240a6a0a3e8";
////        String zahlungenUSDAccount = "3ae589ac215cfdba454569ed652e5113";
////        String Sicherheitseinbehalt = "f982b88d02990008053d54d06ebd7049";
////        String SicherheitseinbehaltUSD = "259c916fcf62fd414d2b0ec27f252544";
////        String disagio = "035c0d6f1d0f19e5b94a1fe532084e95";
////        String disagioUSD = "28f9ed9929fc83e59082ceed5b77e596";
////        String disagioVorsteuerUSD = "e6e5f9c028769215c42a55a3fe616842";
//        String targetAccount = "e1a8001a2a189134b6fda79647471f1c";

      GnucashWritableTransaction auszahlung = aBook.createWritableTransaction();
      auszahlung.setCurrencyID("EUR");
      auszahlung.setCurrencyNameSpace("ISO4217");
      auszahlung.setDatePosted(date);
      auszahlung.setTransactionNumber("Settlement " + settlementID);
      auszahlung.setDescription("Wirecard - Auszahlung Kreditkartenakzeptanz " + settlementPeriod);
      //targetAccount 192,20eur "Auszahlung"
      //auszahlungAccount -57,62 "UEBERTRAG/UEBERWEISUNG..."
      //auszahlungAccountUSD -179,20 -134,58 "179,20USD*0,7510=134,58EUR"

    //TODO: test this
      GnucashWritableAccount targetAccount = PluginConfigHelper.getOrConfigureAccountWithKey(aBook, "wirecard.targetAccount", "thisAccount",
              "Please select the bank-account 'Auszahlung' is transfered to");
      GnucashWritableTransactionSplit targetSplit = auszahlung.createWritingSplit(targetAccount);
      targetSplit.setDescription("Auszahlung");
      targetSplit.setValue(total);
      targetSplit.setQuantity(total);


      LOG.fine("DEBUG: invoiceAmount = " + Arrays.toString(invoiceAmount.toArray()));
      LOG.fine("DEBUG: settlementAmount = " + Arrays.toString(settlementAmount.toArray()));
      LOG.fine("DEBUG: currencies = " + Arrays.toString(currencies.toArray()));
      LOG.fine("DEBUG: settlementAmount = " + Arrays.toString(settlementAmount.toArray()));

      for (int j = 0; j < Math.min(settlementAmount.size(), currencies.size()); j++) {
//          String accountID = auszahlungAccount;
//          if (!currencies.get(j).equals("EUR")) {
//              accountID = auszahlungAccountUSD;
//          }
//          GnucashWritableAccount account = aBook.getAccountByID(accountID);
          //TODO: test this, TODO: Rï¿½ckzahlungen von Sicherheits-Einbehalt
          GnucashWritableAccount account = PluginConfigHelper.getOrConfigureAccountWithKey(aBook, "wirecard.auszahlung." + currencies.get(j), "thisAccount",
                  "Please select the account to accumulate 'Auszahlung' in before transfer to the bank-account for currency '" + currencies.get(j) + "'");

          LOG.fine("DEBUG: accumulation-account for currency '" + currencies.get(j) + " is " + account.getQualifiedName());

          GnucashWritableTransactionSplit split = auszahlung.createWritingSplit(account);
          split.setDescription(invoiceAmount.get(j) + currencies.get(j)
                  + "*" + exchangeRates.get(j) + "=" + settlementAmount.get(j) + "EUR");
          split.setSplitAction("");
          if (invoiceNumbers.size() > j) {
              split.setSplitAction(invoiceNumbers.get(j));
              split.setDescription(split.getDescription() + " invoice " + invoiceNumbers.get(j) + " ");
          }
          if (invoiceFrom.size() > j) {
              split.setDescription(split.getDescription() + " : " + invoiceFrom.get(j));
          }
          if (invoiceTo.size() > j) {
              split.setDescription(split.getDescription() + " - " + invoiceTo.get(j));
          }
          //auszahlung.setCurrencyID(currencies.get(j));
          //auszahlung.setCurrencyNameSpace("ISO4217");
          split.setValue(settlementAmount.get(j).negate());
          split.setQuantity(invoiceAmount.get(j).negate());
      }

      // check the next 12 days for a transaction with exactly 2 splits
      // between the same accounts for the same
      // value. If found, replace it by this transaction.
      // (such a transaction may e.g. exist due to a HBCI-import of the bank-account we transfer to)
      final long dateDeltaMillis = 2 * 24 * 60 * 60 * 1000;
      long from = date.getTime() - dateDeltaMillis;
      long to = date.getTime() + 6 * dateDeltaMillis;

      // TODO: ein getTransactionSplits(fromDate, toDate) wï¿½re praktisch
      List<? extends GnucashTransactionSplit> splits2 = targetAccount.getTransactionSplits();
        for (GnucashTransactionSplit split : splits2) {
            if (split.getTransaction().getId() == auszahlung.getId()) {
                continue;
            }
            if (split.getTransaction().getDatePosted().getTime() < from) {
                continue;
            }
            if (split.getTransaction().getDatePosted().getTime() > to) {
                continue;
            }
            if (split.getTransaction().getSplits().size() != 2) {
                continue;
            }
            if (!split.getTransaction().getDescription().toLowerCase().contains("wirecard")) {
                continue;
            }
            if (split.getQuantity().equals(total)) {
                targetAccount.getWritableGnucashFile().removeTransaction((GnucashWritableTransaction) split.getTransaction());
//                auszahlung.setDatePosted(split.getTransaction().getDatePosted());
//                        ((GnucashWritableTransaction) split.getTransaction())
//                                .setDescription(split.getTransaction().getDescription() + " TODO: maybe remove this");
                return;
            }
        }

    }

    /**
     * Throws an IllegalArgumentException if the next line read is
     * not equal to the given one.
     * @param aBuffer the reader to read the line from
     * @param aString the string the line must be equal to
     * @return the given line
     * @throws IOException if we cannot read
     */
    private static String lineMustEqual(final BufferedReader aBuffer, final String aString) throws IOException {
        String line = aBuffer.readLine();
        if (!line.equals(aString)) {
            throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\" must equal \"" + aString + "\"");
        }
        return line;
    }

    /**
     * Throws an IllegalArgumentException if the next line read does
     * not start with the given one.
     * @param aBuffer the reader to read the line from
     * @param aString the string the line must be equal to
     * @return the given line
     * @throws IOException if we cannot read
     */
    private static String lineMustStartWith(final BufferedReader aBuffer, final String aString) throws IOException {
        String line = aBuffer.readLine();
        if (!line.startsWith(aString)) {
            throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons.");
        }
        return line;
    }

    /**
     * @param aBuffer the reader to read from
     * @throws IOException may happen
     */
    private static void lineMustBeNull(final BufferedReader aBuffer)
                                                                    throws IOException {
        String line;
        line = aBuffer.readLine();
        if (line.length() != 0) {
            throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line should be empty but is \"" + line + "\"");
        }
    }

    /**
     * Import a *_PAVI*.pdf -file converted to text using pdftotext containing an account-statement.
     * @param aBook our data-model
     * @param aBuffer the file to import conveted to plaintext
     * @throws IOException if we cannot read parts
     * @throws ParseException if we cannot parse parts of the account-statement
     * @throws JAXBException issues with the XML-backend
     */
    public static void importAccountStatement(final GnucashWritableFile aBook, final BufferedReader aBuffer) throws IOException, ParseException, JAXBException {

        if (aBuffer == null) {
            throw new IllegalArgumentException("null buffer given");
        }
        String line = aBuffer.readLine();
        if (line == null) {
            throw new IllegalArgumentException(
                    "cannot read a single line from buffer");
        }
        if (line.trim().length() == 0) {
            LOG.info("first line is empty, skipping.");
            line = aBuffer.readLine();
        }
        int formatVersion = 0;
        if (line.equals("Wirecard Technologies AG | Bretonischer Ring 4 | 85630 Grasbrunn")) {
            formatVersion = 0;
        } else if (line.equals("Wirecard Technologies AG | Bretonischer Ring 4 | D-85630 Grasbrunn, Deutschland")) {
            formatVersion = 1;
        } else {
            LOG.severe("first line=\"" + line + "\" != \"Wirecard Technologies...\"");
            throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons.");
        }
        lineMustBeNull(aBuffer);
        line = aBuffer.readLine();
        String[] splits = line.split(" ");
        if (splits[splits.length - 1].equalsIgnoreCase("USt-ID:")) {
            // new format
            lineMustBeNull(aBuffer);
            line = aBuffer.readLine();
            splits = line.split(" ");
        }
        Date date = null; // we may start with the date, USt-ID+empty line+Date or address+USt-ID+empty line+Date
        for (int i = 0; i < 10 ; i++) {
            try {
                date = UOS_DATEFORMAT.parse(splits[splits.length - 1]);
            } catch (Exception e) {
                if (i > 8) {
                    date = UOS_DATEFORMAT2.parse(splits[splits.length - 1]);
                } else {
                    try {
                        date = UOS_DATEFORMAT2.parse(splits[splits.length - 1]);
                    } catch (Exception e2) {
                        line = aBuffer.readLine();
                        splits = line.split(" ");
                        formatVersion = 2;
                        continue;
                    }
                }
            }
        }
        lineMustBeNull(aBuffer);

        lineMustEqual(aBuffer, "Rechnung");



        // body
        String currency = "EUR";
        String invoiceNr = null;
        String from = null;
        String to = null;
        String additionalText = "";
        FixedPointNumber umsatz = null;
        FixedPointNumber fees = null;
        FixedPointNumber feesTax = null;
        FixedPointNumber feesWithTax = null;
        FixedPointNumber security = null;
        FixedPointNumber sum = null;


        if (formatVersion == 0) {
            //old: Rechnungsnummer: Haendler: Haendlerkennung: Rechnungsperiode: Waehrung: Produkt: Acquirer: Brand:
            // 0913XA794429    Wolschon Import WDB 0000003161ED9CA8 2009-03-16 - 2009-03-22 USD Credit Card Wirecard Bank Master Card, Visa
            line = aBuffer.readLine();
            if (!line.startsWith("Rechnungsnummer: ")) {
                throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
            }
            splits = line.split(" ");
            final int invoiceNrplace = 8;
            invoiceNr = splits[invoiceNrplace];
            int i = 0;
            for (int j = 0; j < splits.length; j++) {
                if (splits[j].equals("USD")) {
                    currency = "USD";
                    i = j;
                    break;
                }
                if (splits[j].equals("EUR")) {
                    currency = "EUR";
                    i = j;
                    break;
                }
            }
            from = splits[(i - 2) - 1];
            to = splits[i - 1];

            lineMustBeNull(aBuffer);
            lineMustEqual(aBuffer, "Anzahl Netto Transaktionsumsatz Kreditkarteneinzuege "
                 + "Gutschriften Chargebacks Erfolgreiche Rueckweisung von Chargebacks Summe Netto "
                 + "Transaktionsumsatz Gebuehren Disagio Chargeback Gebuehr Summe Gebuehren ohne MwSt. "
                 + "MwSt. auf Gebuehren Summe Gebuehren Sicherheitseinbehalt Auszahlungsbetrag");
            lineMustBeNull(aBuffer);
            line = aBuffer.readLine();
            if (!line.equals("Volumen")) {
                   throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons.");
            }

            lineMustBeNull(aBuffer);
            line = aBuffer.readLine();
            if (!line.equals("Tarif")) {
                   throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons.");
            }
            lineMustBeNull(aBuffer);
            line = aBuffer.readLine();
            if (!line.startsWith("Betrag")) {
                   throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons.");
            }
            splits = line.split(" ");
            umsatz = new FixedPointNumber(splits[1]);

            line = aBuffer.readLine();
            line = aBuffer.readLine();
            // 0 0
            line = aBuffer.readLine();
            line = aBuffer.readLine();
            // 31,18 0 1,15 31,18
            line = aBuffer.readLine();
            line = aBuffer.readLine();
            // 3,70% 52,00 19,00% 5,00%
            line = aBuffer.readLine();
            line = aBuffer.readLine();
            // 1,15 0,00 1,15 0,22 1,37 1,56 28,25
            splits = line.split(" ");
            // 0 = disagio
            // 1 = chargeback
            int p = 2;
            fees = new FixedPointNumber(splits[p++]);
            feesTax = new FixedPointNumber(splits[p++]);
            feesWithTax = new FixedPointNumber(splits[p++]);
            security = new FixedPointNumber(splits[p++]);
            sum = new FixedPointNumber(splits[p++]);
        } else if (formatVersion == 1) {
            //new: Rechnungsnummer: Hï¿½ndler: Hï¿½ndlerkennung: Brand:
            // 201022TA00789371 Wolschon Import WDB 0000003161ED9CA8 Master Card, Vis
            line = aBuffer.readLine();
            while (!line.startsWith("Rechnungsnummer: ")) {
                line = aBuffer.readLine();
            }
            lineMustBeNull(aBuffer);

            line = aBuffer.readLine();
            splits = line.split(" ");
            try {
                invoiceNr = splits[0];
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
            }

            lineMustBeNull(aBuffer);

            line = aBuffer.readLine();
            if (!line.startsWith("Rechnungszeitraum:")) {
                throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
            }

            lineMustBeNull(aBuffer);

            line = aBuffer.readLine();
            splits = line.split(" ");
            int i = -1;
            for (int j = 0; j < splits.length; j++) {
                if (splits[j].equals("USD")) {
                    currency = "USD";
                    i = j;
                    break;
                }
                if (splits[j].equals("EUR")) {
                    currency = "EUR";
                    i = j;
                    break;
                }
            }
            if (i < 0) {
                throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
            }
            from = splits[(i - 2) - 1];
            to = splits[i - 1];

            line = aBuffer.readLine();
            while (!line.startsWith("Betrag")) {
                line = aBuffer.readLine();
            }
            splits = line.split(" ");
            try {
                umsatz = new FixedPointNumber(splits[1]);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
            }

            lineMustBeNull(aBuffer);

            line = aBuffer.readLine();
            if (!line.startsWith("19.00 %")) {
                throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
            }
            lineMustBeNull(aBuffer);
            line = aBuffer.readLine();
            splits = line.split(" ");

            try {
                fees = new FixedPointNumber(splits[0]).negate();
                if (!"EUR".equals(splits[1]) && !"USD".equals(splits[1])) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }
                feesTax = new FixedPointNumber(splits[2]);
                if (!"EUR".equals(splits[3]) && !"USD".equals(splits[3])) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }
                feesWithTax = new FixedPointNumber(splits[4]);
                if (!"EUR".equals(splits[5]) && !"USD".equals(splits[5])) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }
                security = new FixedPointNumber(splits[6]).negate();
                if (!"EUR".equals(splits[7]) && !"USD".equals(splits[7])) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }
                if (splits.length == 8) {
                    sum = security.negate();
                    security = new FixedPointNumber();
                } else {
                    sum = new FixedPointNumber(splits[8]);
                    if (!"EUR".equals(splits[9]) && !"USD".equals(splits[9])) {
                        throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
            }
        } else  {
            lineMustBeNull(aBuffer);
            lineMustEqual(aBuffer, "Rechnungsnummer:");
            lineMustEqual(aBuffer, "Händler:");
            lineMustEqual(aBuffer, "Händlerkennung:");
            lineMustEqual(aBuffer, "Brand:");
            lineMustBeNull(aBuffer);
            invoiceNr = aBuffer.readLine();
            line = aBuffer.readLine(); // name
            line = aBuffer.readLine(); // some number
            line = aBuffer.readLine(); // "Master Card, Visa"
            lineMustBeNull(aBuffer);

            lineMustEqual(aBuffer, "Rechnungszeitraum:");
            lineMustEqual(aBuffer, "Währung:");
            lineMustEqual(aBuffer, "Produkt:");
            lineMustEqual(aBuffer, "Acquirer:");
            lineMustBeNull(aBuffer);
            line = aBuffer.readLine();// 08.11.2010 - 14.11.2010
            splits = line.split(" ");
            from = splits[0];
            to = splits[2];

            line = aBuffer.readLine();
            if (line.equals("USD")) {
                currency = "USD";
            } else if (line.equals("EUR")) {
                currency = "EUR";
            } else {
                throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
            }
            lineMustEqual(aBuffer, "Credit Card");
            lineMustEqual(aBuffer, "Wirecard Bank");
            lineMustBeNull(aBuffer);
            lineMustEqual(aBuffer, "Sehr geehrte Damen und Herren,");
            lineMustEqual(aBuffer, "für den Rechnungszeitraum " + from + " - " + to + " werden die folgenden Transaktionen berechnet.");
            lineMustBeNull(aBuffer);
            lineMustEqual(aBuffer, "Anzahl");
            line = aBuffer.readLine();
            boolean hasGutschriften = false;
            boolean hasTransactions = false;
            if (line.length() > 0) {
                hasTransactions = true;
                // this block may be missing if no transactions happened
                try {
                    additionalText +=" #Zahlungen=" + Integer.parseInt(line); // count
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\" is not an integer");
                }
                line = aBuffer.readLine();
                if (line.length() > 0) {
                    additionalText +=" #Gutschriften=" + line;
                    hasGutschriften = true;
                    lineMustBeNull(aBuffer);
                }
                lineMustEqual(aBuffer, "Kreditkarteneinzuege");
                if (hasGutschriften) {
                    lineMustEqual(aBuffer, "Gutschriften");
                }
                lineMustBeNull(aBuffer);
            } else {
                additionalText +=" keine Zahlungen";
            }
            lineMustEqual(aBuffer, "Volumen");
            lineMustBeNull(aBuffer);
            lineMustEqual(aBuffer, "Tarif");
            lineMustBeNull(aBuffer);
            if (hasTransactions) {
                aBuffer.readLine();//48,90 EUR
                if (hasGutschriften) {
                    aBuffer.readLine();//Gutschrift EUR
                }
                lineMustBeNull(aBuffer);
                aBuffer.readLine();//48,90 EUR
                if (hasGutschriften) {
                    aBuffer.readLine();//-Gutschrift EUR
                }
                lineMustBeNull(aBuffer);
            }
            lineMustEqual(aBuffer, "Summe Netto Transaktionsumsatz");
            if (hasTransactions) {
                lineMustBeNull(aBuffer);

                line = aBuffer.readLine();//48,90 EUR
                splits = line.split(" ");
                try {
                    umsatz = new FixedPointNumber(splits[0]);
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }

                lineMustBeNull(aBuffer);
                lineMustEqual(aBuffer, "Disagio");
                lineMustBeNull(aBuffer);

                line = aBuffer.readLine();//    1,81 EUR
                splits = line.split(" ");

                fees = new FixedPointNumber(splits[0]).negate();
                if (!"EUR".equals(splits[1]) && !"USD".equals(splits[1])) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }
                lineMustBeNull(aBuffer);
            }
            lineMustEqual(aBuffer, "Summe Gebühren ohne MwSt.");
            lineMustEqual(aBuffer, "MwSt. auf Gebühren");
            if (!hasTransactions) {
                lineMustBeNull(aBuffer);
                security = new FixedPointNumber();
                feesWithTax = new FixedPointNumber();
                sum = new FixedPointNumber();
                umsatz = new FixedPointNumber();
            } else {
                lineMustEqual(aBuffer, "Summe Gebühren");
                lineMustEqual(aBuffer, "Sicherheitseinbehalt");
                lineMustBeNull(aBuffer);
                lineMustEqual(aBuffer, "19.00 %");
                line = aBuffer.readLine();//    48,90 EUR
                lineMustBeNull(aBuffer);
                line = aBuffer.readLine();//    1,81 EUR
                line = aBuffer.readLine();//    0,34 EUR
                splits = line.split(" ");
                feesTax = new FixedPointNumber(splits[0]);
                if (!currency.equals(splits[1])) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }

                lineMustBeNull(aBuffer);
                line = aBuffer.readLine();//    5.00 %
                lineMustBeNull(aBuffer);
                line = aBuffer.readLine();//    2,15 EUR
                splits = line.split(" ");
                feesWithTax = new FixedPointNumber(splits[0]);
                if (!currency.equals(splits[1])) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }

                line = aBuffer.readLine();//    2,44 EUR
                splits = line.split(" ");
                security = new FixedPointNumber(splits[0]);
                if (!currency.equals(splits[1])) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }
                lineMustBeNull(aBuffer);
                lineMustEqual(aBuffer, "Auszahlungsbetrag");
                lineMustBeNull(aBuffer);
                lineMustEqual(aBuffer, "Kontoinhaber:");
                lineMustEqual(aBuffer, "Kontonummer:");
                lineMustEqual(aBuffer, "BLZ:");
                lineMustEqual(aBuffer, "Bankname:");
                lineMustEqual(aBuffer, "Bankaddresse:");
                lineMustEqual(aBuffer, "SWIFT Code:");
                lineMustEqual(aBuffer, "IBAN:");
                lineMustBeNull(aBuffer);
                lineMustEqual(aBuffer, "Betrag");
                lineMustBeNull(aBuffer);

                line = aBuffer.readLine();//   44,31 EUR
                splits = line.split(" ");
                sum = new FixedPointNumber(splits[0]);
                if (!"EUR".equals(splits[1]) && !"USD".equals(splits[1])) {
                    throw new IllegalArgumentException("wrong input-format. Aborting for safety reasons. line=\"" + line + "\"");
                }
            }

        }

       String auszahlungAccount = "0682d0d40fefc9af74cae75372b0159d";//TODO: these should not be hardcoded
       if (currency.equals("USD")) {
           auszahlungAccount = "eeda2c814e012144d7e0c240a6a0a3e8";
       }
//       String zahlungenUSDAccount = "3ae589ac215cfdba454569ed652e5113";
       String sicherheitseinbehalt = "f982b88d02990008053d54d06ebd7049";
       if (currency.equals("USD")) {
           sicherheitseinbehalt = "259c916fcf62fd414d2b0ec27f252544";
       }
       String disagio = "035c0d6f1d0f19e5b94a1fe532084e95";
       if (currency.equals("USD")) {
           disagio = "28f9ed9929fc83e59082ceed5b77e596";
       }
       String zahlungen = "3ae589ac215cfdba454569ed652e5113";
       if (currency.equals("USD")) {
           zahlungen = "3dbb5458285627254e8ec3c7f2ede36e";
       }

//       String disagioVorsteuer =
//       if (currency.equals("USD")) {
//           disagioVorsteuer = "e6e5f9c028769215c42a55a3fe616842";
//       }
//       String targetAccount = "e1a8001a2a189134b6fda79647471f1c";

     GnucashWritableTransaction auszahlung = aBook.createWritableTransaction();
     auszahlung.setCurrencyID(currency);
     auszahlung.setCurrencyNameSpace("ISO4217");
     auszahlung.setDatePosted(date);
     auszahlung.setTransactionNumber("invoice " + invoiceNr);
     auszahlung.setDescription("Settlement " + from + " - " + to + " " + currency + " invoice="
             + invoiceNr);

     //auszahlungAccountUSD 179,2
     GnucashWritableTransactionSplit auszahlungSplit = auszahlung.createWritingSplit(aBook.getAccountByID(auszahlungAccount));
     auszahlungSplit.setValue(sum);
     auszahlungSplit.setQuantity(sum);
     auszahlungSplit.setDescription("Auszahlungen" + (additionalText.length() > 0?"(" + additionalText + ")":""));

     // "Sicherheitseinebhalt" 9,89 SicherheitseinbehaltUSD
     GnucashWritableTransactionSplit securitySplit = auszahlung.createWritingSplit(aBook.getAccountByID(sicherheitseinbehalt));
     securitySplit.setValue(security);
     securitySplit.setQuantity(security);
     securitySplit.setDescription("Sicherheitseinbehalt");

     // "197,80eur*3,7%=7,32=8,71eur Brutto" 8,71  disagioUSD
     GnucashWritableTransactionSplit disagioSplit = auszahlung.createWritingSplit(aBook.getAccountByID(disagio));
     disagioSplit.setValue(feesWithTax);
     disagioSplit.setQuantity(feesWithTax);
     disagioSplit.setDescription("Disagio + Chargeback " + umsatz + "*3.7%="
             + fees + " Netto + " + feesTax + " =" + feesWithTax + " Brutto");

     // sum Zahlungen
     GnucashWritableTransactionSplit zahlungenSplit = auszahlung.createWritingSplit(aBook.getAccountByID(zahlungen));
     zahlungenSplit.setValue(umsatz.negate());
     zahlungenSplit.setDescription("Zahlungen");

    }

    /**
     * @return the book
     */
    public GnucashWritableFile getBook() {
        return myBook;
    }

    /**
     * @param aBook the book to set
     */
    public void setBook(final GnucashWritableFile aBook) {
        myBook = aBook;
    }

}
