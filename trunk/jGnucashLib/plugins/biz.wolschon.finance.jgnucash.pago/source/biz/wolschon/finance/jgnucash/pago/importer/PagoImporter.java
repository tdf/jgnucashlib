/**
 * License: GPLv3 or later
 */
package biz.wolschon.finance.jgnucash.pago.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashFileWritingImpl;
import biz.wolschon.finance.jgnucash.common.HttpFetcher;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * This class parses PAGO-reports (clearing and account-statement)
 * and inserts transactions for them.<br/>
 * TODO: set account-names/ids in a config-file
 */
public class PagoImporter {

    /**
     * Do not download years before this one.
     * Arbitrarily chosen to be 2007.
     */
    private static final int FIRSTYEARTOCONSIDER = 2007;

    /**
     * Size of the read-buffer to use.
     */
    private static final int BUFFERSIZE = 255;

    /**
     * The date-format used in the pago-files.
     */
    private static final SimpleDateFormat UOS_DATEFORMAT = new SimpleDateFormat("dd.MM.yy");

    /**
     * The book we operate on.
     */
    private GnucashWritableFile myBook;

    /**
     * @param aBook The book we operate on
     */
    public PagoImporter(final GnucashWritableFile aBook) {
        myBook = aBook;
    }

    /**
     * In all cases we write to /tmp/testdata_out instead of the opened file.
     * @param args either no arguments (use all pago-files in the current dir and /tmp/testdata)
     * or a gnucash-file followed by pago-files.
     */
    public static void main(final String[] args) {

        GnucashWritableFile book = null;
        PagoImporter subject = null;

        if (args.length == 0) {
            File listDir = new File(".");
            String[] files = listDir.list();

            List<String> newArgs = new LinkedList<String>();
            newArgs.add("/tmp/testdata");
            for (String file : files) {
                if (file.endsWith(".pdf") && file.contains("_CLE_")) {
                    newArgs.add(listDir.getAbsolutePath() + "/" + file);
                }
            }

            for (String file : files) {
                if (file.endsWith(".pdf") && file.contains("_PAVI_")) {
                    newArgs.add(listDir.getAbsolutePath() + "/" + file);
                }
            }
            if (files.length > 1) {
                main(newArgs.toArray(new String[newArgs.size()]));
                return;
            }
            System.out.println("usage: [gnucash-file] [pdf-file with Pago clearing or account-statement]*");
            return;
        }


        try {
            System.out.println("importing " + (args.length - 1) + " files...");
            for (String arg : args) {
                File file = new File(arg);
                if (!file.exists()) {
                    throw new IllegalArgumentException("file '" + file.getAbsolutePath() + "' does not exist!");
                }

                if (book == null) {
                    book = new GnucashFileWritingImpl(file);
                    subject = new PagoImporter(book);
                    continue;
                }

                try {
                    System.out.println("importing " + file.getAbsolutePath() + " ...");
                    subject.importFile(book, file);
                } catch (Exception e) {
                    System.err.println("cannot import file: " + arg);
                    e.printStackTrace();
                }
            }

            book.writeFile(new File("/tmp/testdata_out"));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        Process proc = Runtime.getRuntime()
                .exec(new String[] {"/usr/bin/pdftotext", pdfFile.getAbsolutePath(), "-"});
        Reader reader = new InputStreamReader(proc.getInputStream());
        BufferedReader buffer = new BufferedReader(reader);

        if (pdfFile.getName().toLowerCase().endsWith(".p7s")) {
            return; //skip cryptographic signatures
        }
        if (pdfFile.getName().contains("PAVI")) {
            importAccountStatement(aBook, buffer);
        }
        if (pdfFile.getName().contains("CLE")) {
            importClearingStatement(aBook, buffer);
        }
    }

    /**
     * Import all new statements via HTTP directly from the pago-server.
     * @param aBook The book we operate on
     * @param username the username for the pago-bis -website
     * @param password the password for the pago-bis -website
     * @throws IOException errors reading or writing
     * @throws ParseException errors parsing the pdf-file
     * @throws JAXBException errors reading or writing the xml-datastructures of Gnucash
     */
    public static void importFromHttp(final GnucashWritableFile aBook, final String username, final String password) throws IOException, ParseException, JAXBException {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat urlDateFormat = new SimpleDateFormat("MM.yyyy");

        //go back from the current month until there are no new ones anymore
        while (calendar.get(GregorianCalendar.YEAR) > FIRSTYEARTOCONSIDER) {
            String fetchIDsURL = "https://bis.pago.de/digidoc/digidoc.php?monat=01." + urlDateFormat.format(new Date(calendar.getTimeInMillis())) + "&typ=&only_new=y";
            String page = HttpFetcher.fetchURL(new URL(fetchIDsURL), username, password);
            Pattern pattern = Pattern.compile("<input type='checkbox' name='id\\[\\]' value='([0-9]*)'>");
            Matcher matcher = pattern.matcher(page);

            List<Integer> ids = new LinkedList<Integer>();
            String fetchDocURL = "https://bis.pago.de/digidoc/zip_download.php?";
            while (matcher.find()) {
                String id = matcher.group(1);
                ids.add(Integer.parseInt(id));
                fetchDocURL += "id[]=" + id + "&";
            }

            if (ids.size() > 0) {
                InputStream in = HttpFetcher.fetchURLStream(new URL(fetchDocURL), username, password);
                importZIPStream(aBook, in, true);
                calendar.add(GregorianCalendar.MONTH, -1);
            } else {
                break;
            }

        }
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
                tempFile = File.createTempFile("pagoImporter_", fileName);
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
        };
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

        String[] splits = line.split(" ");
        if (splits.length != 2) {
            final int newHeaderLine = 10;
            if (splits.length != newHeaderLine) { // they changed the first line
                throw new IllegalArgumentException("first line is not in format "
                        + "'Köln, [date]' - wrong number of splits '"
                        + line + "'");
            }
        }
        Date date = UOS_DATEFORMAT.parse(splits[1]);


        //------------------ get clearing-number

        while (!line.startsWith("Clearing-Aufstellung Nr")) {
            line = aBuffer.readLine();
        }
        splits = line.split(" ");
        final int splitCountClearingHeader = 5;
        if (splits.length != splitCountClearingHeader) {
            throw new IllegalArgumentException("first line is not in format "
                    + "'Clearing-Aufstellung Nr [nr] Kundennummer [nr2]'"
                    + " - wrong number of splits '" + line + "'");
        }
        int clearingNr = Integer.parseInt(splits[2]);

//      ------------------ get safety-deposit
        while (!line.startsWith("Der Sicherheitseinbehalt")) {
            line = aBuffer.readLine();
        }

        // safetyDeposit is <0 for deposit, >0 for refund
        FixedPointNumber safetyDeposit = new FixedPointNumber();
        splits = line.split(" ");
        for (int i = 0; i < splits.length - 2; i++) {
            if (splits[i].equals("Sicherheitsleistung")) {
                if (splits[i + 1].equals("Einbehalt")
                 || splits[i + 1].equals("Erstattung")) {
                    safetyDeposit = safetyDeposit.add(
                            new FixedPointNumber(splits[i + 2]));
                }
            }
        }

        //--------------------- charge safety-deposit

        GnucashWritableTransaction safetyTrans = aBook.createWritableTransaction();
        safetyTrans.setDatePosted(date);
        safetyTrans.setDateEntered(date);
        safetyTrans.setDescription("Sicherheits-Einbehalt - UOS-Clearing " + clearingNr);
        GnucashWritableAccount uosAccount = aBook.getAccountByName("UnitedOnline");
        safetyTrans.createWritingSplit(uosAccount).setValue(safetyDeposit);
        safetyTrans.createWritingSplit(aBook.getAccountByName("UnitedOnline - Kreditkarten-Sicherheits-Einbehalt")).setValue(
                ((FixedPointNumber) safetyDeposit.clone()).negate());

//      ------------------ get transactions

        while ((line = aBuffer.readLine()) != null) {

            // skip text
            if (!line.startsWith("Forderungen")) {
                continue;
            }

            // get count
            splits = line.split(" ");
            int count = (splits.length - 1) / 2;

//          get dates
            Date[] dates = new Date[count];
            for (int i = 0; i < count; i++) {
                dates[i] = UOS_DATEFORMAT.parse(splits[1 + count + i]);
            }

            // get count
            while (!line.startsWith("Anzahl")) {
                line = aBuffer.readLine();
            }
            splits = line.split(" ");
            int[] transactionsPerDay = new int[count];
            for (int i = 0; i < count; i++) {
                transactionsPerDay[i] = Integer.parseInt(splits[1 + i]);
            }

//          get Einreichungsbetrag
            while (!line.startsWith("Einreichungsbetrag")) {
                line = aBuffer.readLine();
            }
            splits = line.split(" ");
            FixedPointNumber[] values = new FixedPointNumber[count];
            for (int i = 0; i < count; i++) {
                values[i] = new FixedPointNumber(splits[1 + 2 * i]);
            }

//          get Disagio%
            while (!line.startsWith("Disagio")) {
                line = aBuffer.readLine();
            }
            splits = line.split(" ");
            String[] disagioPercent = new String[count];
            for (int i = 0; i < count; i++) {
                disagioPercent[i] = splits[1 + i];
            }

//          get Disagio-value
            while (!line.startsWith("Disagiobetrag")) {
                line = aBuffer.readLine();
            }
            splits = line.split(" ");
            FixedPointNumber[] disagios = new FixedPointNumber[count];
            for (int i = 0; i < count; i++) {
                disagios[i] = new FixedPointNumber(splits[1 + 2 * i]);
            }


            int foundCount = 0;
            int totalCount = 0;
            for (int i = 0; i < count; i++) {
                if (transactionsPerDay[i] == 1) {
                    if (clearTransaction(uosAccount, dates[i], values[i],
                                                     disagioPercent[i], disagios[i],
                                                      "" + clearingNr)) {
                        foundCount++;
                    }
                    totalCount++;
                } else {
                    //TODO: add support for multiple transactions per day
                    totalCount += transactionsPerDay[i];
                }
            }
            safetyTrans.setDescription(safetyTrans.getDescription()
                    + " [" + foundCount + " of " + totalCount + " transactions identified in automatic import]");

        }

        // TODO Auto-generated method stub

    }

    /**
     * Given a UOS-transaction, find the Gnucash-transaction for it and
     * if required correct the estimated disagio-value payed to the one actually
     * invoiced.
     * @param aUosAccount
     * @param aDate
     * @param aValue
     * @param aPercentage
     * @param aNettoDisagioValue
     * @param clearingNR number of the clearing this UOS-transaction was invoiced in
     * @throws JAXBException If we have issues with the XML-backend.
     * @return false if we could not find the transaction
     */
    private static boolean clearTransaction(final GnucashWritableAccount aUosAccount,
                                  final Date aDate,
                                  final FixedPointNumber aValue,
                                  final String aPercentage,
                                  final FixedPointNumber aNettoDisagioValue,
                                  String clearingNR) throws JAXBException {

        GregorianCalendar greg = new GregorianCalendar();
        greg.setTime(aDate);
        greg.add(GregorianCalendar.DATE, -2);
        Date startDate = greg.getTime();
        greg.add(GregorianCalendar.DATE, 4);
        Date endDate = greg.getTime();

        FixedPointNumber aBruttoDisagioValue = ((FixedPointNumber) aNettoDisagioValue.clone()).multiply(new FixedPointNumber("-1.19")); // <0
        FixedPointNumber aDisagioTaxValue = ((FixedPointNumber) aBruttoDisagioValue.clone()).add(aNettoDisagioValue).negate();  // >0

        for (GnucashTransactionSplit split : aUosAccount.getTransactionSplits()) {
            if (split.getValue().compareTo(aValue.getBigDecimal()) != 0) {
                continue;
            }
            GnucashWritableTransaction trans = (GnucashWritableTransaction) split.getTransaction();
            Date datePosted = trans.getDatePosted();
            if (datePosted.after(endDate)) {
                continue;
            }
            if (datePosted.before(startDate)) {
                continue;
            }
            System.out.println("found transaction! '" + trans.getDescription() + "'");

            trans.setDescription(trans.getDescription().replace("UOS-Clearing ?????", "UOS-Clearing " + clearingNR));

            FixedPointNumber winDifference = new FixedPointNumber();

            // find disagio
            boolean found = false;
            for (GnucashWritableTransactionSplit disagioSplit : trans.getWritingSplits()) {
                if (disagioSplit.getAccountID().equals(aUosAccount.getId()) && disagioSplit.getDescription().toLowerCase().contains("disagio")) {
                    found = true;
                    winDifference = disagioSplit.getValue().subtract(aBruttoDisagioValue);
                    disagioSplit.setValue(aBruttoDisagioValue);
                    disagioSplit.setDescription("Disagio (" + aPercentage + "=" + aNettoDisagioValue + "EUR) automatically imported from pdf-clearing " + clearingNR);
                    break;
                }
            }
            if (!found) {
                System.err.println("count not find disagio-split");
            }

            // find tax for disagio
            found = false;
            for (GnucashWritableTransactionSplit disagioTaxSplit : trans.getWritingSplits()) {
                if (disagioTaxSplit.getAccountID().equals("99005ea2fdbecf0b8bd90bf1715f2bf4")) { // first possibility
                    found = true;
                    winDifference = winDifference.add(disagioTaxSplit.getValue().subtract(aDisagioTaxValue));
                    disagioTaxSplit.setValue(aDisagioTaxValue);
                    break;
                }
                if (disagioTaxSplit.getAccountID().equals("409a25e011024abbab67e26d8039f9b0")) { // second possibiltiy where sales-tax may go to
                    found = true;
                    winDifference = winDifference.add(disagioTaxSplit.getValue().subtract(aDisagioTaxValue));
                    disagioTaxSplit.setValue(aDisagioTaxValue);
                    break;
                }
            }
            if (!found) {
                System.err.println("count not find disagio-tax-split! Splits:");
                for (GnucashWritableTransactionSplit disagioTaxSplit : trans.getWritingSplits()) {
                   System.err.println(disagioTaxSplit.getAccountID());
                }
            }

//          find win
            found = false;
            for (GnucashWritableTransactionSplit winSplit : trans.getWritingSplits()) {
                if (winSplit.getValue().isPositive() & !winSplit.getAccountID().equals(aUosAccount.getId())) {
                    found = true;
                    winSplit.setValue(winSplit.getValue().add(winDifference));
                    break;
                }
            }
            if (!found) {
                System.err.println("count not find win-split");
            }

            System.out.println("found transaction! '" + trans.getDescription() + "'  winDifference=" + winDifference);
            //TODO
            return true;
        }
        System.out.println("found no such transaction! Date='" + aDate + "' value='" + aValue + "'");

        return false;
    }

    /**
     * Import a *_PAVI*.pdf -file converted to text using pdftotext containing an account-statement.
     * @param aBook our data-model
     * @param aBuffer the file to import conveted to plaintext
     * @throws IOException
     * @throws ParseException
     * @throws JAXBException
     */
    public static void importAccountStatement(final GnucashWritableFile aBook, final BufferedReader aBuffer) throws IOException, ParseException, JAXBException {

        if (aBuffer == null) {
            throw new IllegalArgumentException("null buffer given");
        }
        if (aBook == null) {
            throw new IllegalArgumentException("null book given");
        }

        String firstLine = aBuffer.readLine();
        if (firstLine == null) {
            throw new IllegalArgumentException("cannot read a single line from buffer");
        }
        String[] splits = firstLine.split(" ");
        if (splits.length != 3) {
            throw new IllegalArgumentException("first line is not in format "
                    + "'[date] AVIS [nr]' - wrong number of splits '"
                    + firstLine + "'");
        }
        Date date = UOS_DATEFORMAT.parse(splits[0]);
        if (!splits[1].equals("AVIS")) {
            throw new IllegalArgumentException("first line is not in format "
                    + "'[date] AVIS [nr]' - no 'AVIS' '"
                    + firstLine + "' but '" + splits[1] + "'");
        }
        int paviNr = Integer.parseInt(splits[2]);


        while (!aBuffer.readLine().equals("EUR")) {
            // ignored lines
        }

        String lineClearing = aBuffer.readLine();
        String[] clearing = lineClearing.split(" ");
        if (clearing.length < 5) {
            System.err.println("clearing.length = " + clearing.length + " < 5!!! \"" + java.util.Arrays.toString(clearing) + "\"");
         }
        String clearingSaldo =  "";
        if (clearing.length < 6) {
            clearingSaldo = clearing[4]; // change of format 2008-03
          } else {
            clearingSaldo = clearing[5];
          }
        while (!aBuffer.readLine().equals("EUR")) {
            // ignored lines
        }

        String lineReserve = aBuffer.readLine();
        String[] reserve = lineReserve.split(" ");
        if (reserve.length < 5) {
            System.err.println("reserve.length = " + reserve.length + " < 5!!! \"" + java.util.Arrays.toString(reserve) + "\"");
         }
        String reserveSaldo = "";
        if (reserve.length < 6) {
            reserveSaldo = reserve[4]; // change of format 2008-03
          } else {
            reserveSaldo = reserve[5];
          }

        GnucashWritableTransaction transaction = aBook.createWritableTransaction();
      //"Aktiva:B. Umlaufverm�gen:II. Forderungen und sonstige Vermoegensgegenst�nde:
      //1. Forderungen aus Lieferungen und Leistungen:Forderungen aus Lieferungen und Leistungen :UnitedOnline");
        GnucashWritableAccount accountClearing = aBook.getAccountByName("UnitedOnline");
        GnucashWritableTransactionSplit splitClearing = transaction.createWritingSplit(accountClearing);
      //"Aktiva:B. Umlaufvermoegen:II. Forderungen und sonstige Vermoegensgegenstaende:
      //4. sonstige Vermoegensgegenstaende:Darlehen:UnitedOnline - Kreditkarten-Sicherheits-Einbehalt");
        GnucashWritableAccount accountReserve = aBook.getAccountByName("UnitedOnline - Kreditkarten-Sicherheits-Einbehalt");
        GnucashWritableTransactionSplit splitReserve = transaction.createWritingSplit(accountReserve);

        splitClearing.setValue(new FixedPointNumber(0));
        splitReserve.setValue(new FixedPointNumber(0));
        transaction.setDateEntered(date);
        transaction.setDatePosted(date);
        transaction.setDescription("PAVI " + paviNr
                + " Saldo=" + clearingSaldo + " " + (accountClearing.getBalance(date).compareTo((new FixedPointNumber(clearingSaldo)).getBigDecimal()) == 0?"OK":"NAK")
                + " Saldo Sicherheits-Einbehalt: " + reserveSaldo + " "
                + (accountReserve.getBalance(date).compareTo((new FixedPointNumber(reserveSaldo)).getBigDecimal()) == 0?"OK":"NAK"));

        System.out.println(transaction.getDescription() + " (real saldo is " + accountClearing.getBalance(date) + ")");

        aBuffer.close();
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
    public void setBook(GnucashWritableFile aBook) {
        myBook = aBook;
    }

}
