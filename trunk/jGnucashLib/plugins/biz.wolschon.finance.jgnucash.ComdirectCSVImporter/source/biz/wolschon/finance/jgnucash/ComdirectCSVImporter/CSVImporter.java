/**
 * CSVImporter.java
 * Created on 16.05.2005
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 *  Permission is granted to use, modify, publish and sub-license this code
 *  as specified in the contract. If nothing else is specified these rights
 *  are given non-exclusively with no restrictions solely to the contractor(s).
 *  If no specified otherwise I reserve the right to use, modify, publish and
 *  sub-license this code to other parties myself.
 *
 * Otherwise, this code is made available under GPLv3 or later.
 *
 * -----------------------------------------------------------
 * major Changes:
 *  16.05.2005 - initial version
 * ...
 *
 */
package biz.wolschon.finance.jgnucash.ComdirectCSVImporter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashCustomer;
import biz.wolschon.fileformats.gnucash.GnucashInvoice;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashInvoiceWritingImpl;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashTransactionSplitWritingImpl;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashTransactionWritingImpl;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * created: 16.05.2005 <br/>
 * THIS IS VERY OLD CODE AND HAS BEEN REPLACED BY THE SCRIPTABLE HBCI-Importer.<br/>
 *
 * This class knows a few heuristics for importing
 * csv-exported transactions you get from your bank
 * into a gnucash-file<br/>
 *
 * TODO: The current Date instead of the given Date is uses
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class CSVImporter {

    private GnucashWritableAccount account;


    /**
     * @param account
     */
    public CSVImporter(final GnucashWritableAccount account) {
        super();
        setAccount(account);
    }

    public GnucashWritableAccount getAccount() {
        return account;
    }
    protected void setAccount(final GnucashWritableAccount account) {
        if (account == null) {
            throw new IllegalArgumentException(
                    "null not allowed for field this.account");
        }
        this.account = account;
    }

    /**
     * Write the file including a backup of the original one.
     * @throws IOException  the usual
     * @throws JAXBException the usual
     */
    public void flush() throws IOException, JAXBException {

     File oldfile = getAccount().getWritableGnucashFile().getFile();

     File newfile = new File(oldfile.getAbsolutePath() + ".backup");

     oldfile.renameTo(newfile);

     getAccount().getWritableGnucashFile().writeFile(oldfile);
    }

    /**
     * Import a single transaction.
     *
     *
     * @param buchungstag
     * @param valuta
     * @param vorgang
     * @param buchungstext
     * @param umsatzInEur
     */
    protected void importTransaction(final Date buchungstag,
                                     final Date valuta,
                                     final String vorgang,
                                     final String buchungstext,
                                     final FixedPointNumber umsatzInEur) {
        //System.err.println("["+buchungstagFormat.format(buchungstag)+"-"+valutaFormat.format(valuta)+"]<"+vorgang+"> "+umsatzInEur+"  \""+buchungstext+"\"");


        if (doesTransactionExist(buchungstag, valuta, vorgang, buchungstext, umsatzInEur)) {
            return;
        }


        try {
            //TODO: TG-Miete
            //  FREIBG.KOMMUNALBT.GMBH&COKG
            // LEV 07.05 NR.2201.5.0644.08
            // MIETSOLL JULI 2005
            // FÄLLIGER BETRAG ***34,80 EU

            //TODO: try to find out what kind of transaction it is
            // e.g. a payment for an invoice, food or furniture, car-expenses,...

            // if I get some cash at a bancomat
            //TODO: recognize the text "GEB.EUR 3,98 " and transfer these fees to another account
            //
            if (vorgang.indexOf("Auszahlung")!= -1) {
                GnucashAccount otherAccount = getAccount().getWritableGnucashFile().getAccountByName("Cash in Wallet");
                createSimpleTransaction(buchungstag, buchungstext, umsatzInEur, otherAccount);
                return;
            }


            //food
            if (vorgang.indexOf("PLUS")!= -1 && vorgang.indexOf("DIE KLEINEN PREISE")!= -1 ) {
                GnucashAccount otherAccount = getAccount().getWritableGnucashFile().getAccountByName("Essen");
                createSimpleTransaction(buchungstag, buchungstext, umsatzInEur, otherAccount);
                return;
            }

            //KFZ
            // "Laufende Kfz-Betriebskosten"
            // 16%, business transaction
            // search for: "POLO EXPRESSVERSAND" or "ZWEIRAD"

            //Quad-Versicherung
            // 16%, business transaction
            // search for: "DT. HEROLD ALLG. VERS. AG" and "FR-IE69"
            // "Kfz-Versicherungen"

            //Internet
            ///


            // test if it's a payment for an invoice
            //TODO: better test by looking for invoice-numbers, customer names,...
            if (buchungstext.toUpperCase().indexOf("COMPANYNAME") != -1) {


                GnucashCustomer customer = getAccount().getWritableGnucashFile().getCustomerByName("Calcucare");
                Collection<GnucashInvoice> c = getAccount().getWritableGnucashFile().getUnpayedInvoicesForCustomer(customer);
                if (c.size() != 1) {
                    System.err.println("there is " + c.size() + " unpayed invoices for customer: " + customer.getName() + ".\n"
                            + "I will create the transaction but not connect it with an invoice!\n"
                            + "(" + getAccount().getWritableGnucashFile().getUnpayedInvoices().size() + " unpayed invoices total)");
                    GnucashAccount otherAccount = getAccount().getWritableGnucashFile().getAccountByName("Forderungen aus Lieferungen und Leistungen ");
                    createSimpleTransaction(buchungstag, "Rechnung von Calcucare bezahlt", umsatzInEur, otherAccount);
                } else {
                    GnucashInvoiceWritingImpl invoice = (GnucashInvoiceWritingImpl) c.iterator().next();
                    String lotID = invoice.getJwsdpPeer().getInvoicePostlot().getValue();
                    createInvoicePaymentTransaction(buchungstag,buchungstext, umsatzInEur, lotID, "TODO"); //TODO: get paymentNumber from String
                }

                //TODO: make it a trasaction that pays an invoice.
                //TODO: close the invoice if everything is payed for
                return;
            }

            // Telephonrechnung
            if (buchungstext.toUpperCase().indexOf("O2 GERMANY") != -1) {
                GnucashAccount otherAccountPrivate = getAccount().getWritableGnucashFile().getAccountByName("Telephon");
                GnucashAccount otherAccountBusiness = getAccount().getWritableGnucashFile().getAccountByName("Telefon");
                createHalfBusinessTransaction(buchungstag, "Telephon", umsatzInEur, otherAccountPrivate, otherAccountBusiness);
                return;
            }

            //QSC
            if (buchungstext.toUpperCase().indexOf("QSC AG") != -1) {
                GnucashAccount otherAccountPrivate = getAccount().getWritableGnucashFile().getAccountByName("Online Services");
                GnucashAccount otherAccountBusiness = getAccount().getWritableGnucashFile().getAccountByName("Telefon");
                createHalfBusinessTransaction(buchungstag, "Telephon", umsatzInEur, otherAccountPrivate, otherAccountBusiness);
                return;
            }

            //Benzin
            if (buchungstext.toUpperCase().indexOf("TANKSTELLE") != -1) {
                GnucashAccount otherAccountPrivate = getAccount().getWritableGnucashFile().getAccountByID("6cf666101f3962413ed3ab57d28cb75a");
                GnucashAccount otherAccountBusiness = getAccount().getWritableGnucashFile().getAccountByName("Fahrzeugkosten");
                createHalfBusinessTransaction(buchungstag, "Benzin:" + buchungstext, umsatzInEur, otherAccountPrivate, otherAccountBusiness);
                //split it into 50% business-expenses
                //of the 50%, we have 16% taxes
                return;
            }


            // Umsatzsteuer-Vorauszahlungen erkennen
            // "FINANZAMT FREIBURG-STADT          06297/43607   UMS.ST APR.05 "
            /*
             * TODO: decode complex transactions:
             *
             * FINANZAMT FREIBURG-STADT
STEUERNR. 06297/43607
EINK.ST 2VJ.05 1.750,00
UMS.ST MAI 05 545,12
SOL.EST 2VJ.05 96,00
                        -2.391,12
             */
            if (buchungstext.toUpperCase().indexOf("FINANZAMT FREIBURG-STADT") != -1 &&
               buchungstext.toUpperCase().indexOf("UMS.ST") != -1) {
                GnucashAccount otherAccount = getAccount().getWritableGnucashFile().getAccountByName("Umsatzsteuervorauszahlungen");
                createSimpleTransaction(buchungstag, buchungstext, umsatzInEur, otherAccount);
                return;
            }

            // default-case for unknown transactions
            GnucashAccount otherAccount = getAccount().getWritableGnucashFile().getAccountByName("private Ausgaben");
            createSimpleTransaction(buchungstag, buchungstext, umsatzInEur, otherAccount);

        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    /**
     * @param buchungstag
     * @param buchungstext
     * @param umsatzInEur
     * @param otherAccount
     * @throws JAXBException
     */
    private GnucashWritableTransaction createSimpleTransaction(final Date buchungstag,
                                                               final String buchungstext,
                                                               final FixedPointNumber umsatzInEur,
                                                               final GnucashAccount otherAccount) throws JAXBException {
        GnucashWritableTransaction newTransaction = getAccount().getWritableGnucashFile().createWritableTransaction();

        newTransaction.setDatePosted(buchungstag);
        newTransaction.setDescription(buchungstext);

        GnucashTransactionSplitWritingImpl splitThisAccount =  new GnucashTransactionSplitWritingImpl((GnucashTransactionWritingImpl)newTransaction, getAccount());
        GnucashTransactionSplitWritingImpl splitOtherAccount = new GnucashTransactionSplitWritingImpl((GnucashTransactionWritingImpl)newTransaction, otherAccount);

        FixedPointNumber a = (FixedPointNumber) umsatzInEur.clone();
        FixedPointNumber b = ((FixedPointNumber) umsatzInEur.clone()).negate();

        splitThisAccount.setValue(a);
        splitThisAccount.setQuantity(a);
        splitOtherAccount.setValue(b);
        splitOtherAccount.setQuantity(b);

        assert newTransaction.isBalanced();

        return newTransaction;
    }

    /**
     * Buchung die 50% aus Betriebsausgaben besteht und
     * 16% MwSt hat.
     *
     * @param buchungstag
     * @param buchungstext
     * @param umsatzInEur
     * @throws JAXBException
     */
    private void createHalfBusinessTransaction(final Date buchungstag,
                                               final String buchungstext,
                                               final FixedPointNumber umsatzInEur,
                                               final GnucashAccount otherAccountPrivate,
                                               final GnucashAccount otherAccountBusiness) throws JAXBException {
        GnucashTransactionWritingImpl newTransaction = (GnucashTransactionWritingImpl) getAccount().getWritableGnucashFile().createWritableTransaction();

        newTransaction.setDatePosted(buchungstag);
        newTransaction.setDescription(buchungstext);

        GnucashTransactionSplitWritingImpl splitThisAccount = new GnucashTransactionSplitWritingImpl(newTransaction, getAccount());
        GnucashTransactionSplitWritingImpl splitOtherAccountPrivate = new GnucashTransactionSplitWritingImpl(newTransaction, otherAccountPrivate);
        GnucashTransactionSplitWritingImpl splitOtherAccountBusiness = new GnucashTransactionSplitWritingImpl(newTransaction, otherAccountBusiness);
        GnucashTransactionSplitWritingImpl splitOtherAccountBusinessMwSt = new GnucashTransactionSplitWritingImpl(newTransaction, getAccount().getWritableGnucashFile().getAccountByName("Umsatzsteuer 16%"));

        FixedPointNumber a = (FixedPointNumber) umsatzInEur.clone();
        FixedPointNumber b_brutto = ((FixedPointNumber) umsatzInEur.clone()).negate().multiply(new FixedPointNumber("50/100"));
        FixedPointNumber b_netto = ((FixedPointNumber)umsatzInEur.clone()).negate().multiply(new FixedPointNumber("50/100")).divideBy(new FixedPointNumber("116/100"));
        FixedPointNumber b_mwst = ((FixedPointNumber)umsatzInEur.clone()).negate().multiply(new FixedPointNumber("7/100"));

        //TODO: if b_brutto+b_netto+b_mwst is 0,01eur different from umsatzInEur, handle it

        splitThisAccount.setValue(a);
        splitThisAccount.setQuantity(a);
        splitOtherAccountPrivate.setValue(b_brutto);
        splitOtherAccountPrivate.setQuantity(b_brutto);
        splitOtherAccountBusiness.setValue(b_netto);
        splitOtherAccountBusiness.setQuantity(b_netto);
        splitOtherAccountBusinessMwSt.setValue(b_mwst);
        splitOtherAccountBusinessMwSt.setQuantity(b_mwst);

        assert newTransaction.isBalanced();
    }

    /**
     * Buchung die 50% aus Betriebsausgaben besteht und
     * 16% MwSt hat
     *
     * @param buchungstag
     * @param buchungstext
     * @param umsatzInEur
     * @throws JAXBException
     */
    private void createInvoicePaymentTransaction(Date buchungstag, String buchungstext, FixedPointNumber umsatzInEur, String lotID, String paymentNumber) throws JAXBException {
        createInvoicePaymentTransaction(getAccount(), buchungstag, buchungstext, umsatzInEur, lotID, paymentNumber);
    }
    /**
     * Buchung die 50% aus Betriebsausgaben besteht und
     * 16% MwSt hat.
     *
     * @param buchungstag
     * @param buchungstext
     * @param umsatzInEur
     * @throws JAXBException
     */
    public static GnucashWritableTransaction createInvoicePaymentTransaction(final GnucashWritableAccount receivingAccount, Date buchungstag, String buchungstext, FixedPointNumber umsatzInEur, String lotID, String paymentNumber) throws JAXBException {


        GnucashAccount otherAccount = receivingAccount.getWritableGnucashFile().getAccountByName("Forderungen aus Lieferungen und Leistungen ");


        GnucashTransactionWritingImpl newTransaction = (GnucashTransactionWritingImpl) receivingAccount.getWritableGnucashFile().createWritableTransaction();


        newTransaction.getJwsdpPeer().setTrnNum(paymentNumber);

        newTransaction.setDatePosted(buchungstag);
        newTransaction.setDescription(buchungstext);

        // transaction

        GnucashTransactionSplitWritingImpl splitThisAccount
            = new GnucashTransactionSplitWritingImpl(
                      newTransaction, receivingAccount);
        GnucashTransactionSplitWritingImpl splitOtherAccount
            = new GnucashTransactionSplitWritingImpl(
                      newTransaction, otherAccount);

        // taxes
//TODO: 26% taxes should rather be in the transaction for the invoice.

        GnucashTransactionSplitWritingImpl splitThisAccountTaxes
            = new GnucashTransactionSplitWritingImpl(
                       newTransaction,
                       receivingAccount.getWritableGnucashFile()
                          .getAccountByID("da125f57f79499fb2fcf36788b34878e"));
        splitThisAccountTaxes.setDescription(
                "Rücklagen für Einkommenssteuer (26% vom Betrag ohne MwSt)");

        GnucashTransactionSplitWritingImpl splitOtherAccountTaxes
            = new GnucashTransactionSplitWritingImpl(
                       newTransaction,
                       receivingAccount.getWritableGnucashFile()
                          .getAccountByID("4420a9d17091dfdb3365a653de4061a0"));
        splitOtherAccountTaxes.setDescription(
                "Rücklagen für Einkommenssteuer (26% vom Betrag ohne MwSt)");


        FixedPointNumber a = (FixedPointNumber)  umsatzInEur.clone();
        FixedPointNumber b = ((FixedPointNumber) umsatzInEur.clone()).negate();

        FixedPointNumber taxa = ((FixedPointNumber) umsatzInEur.clone())
                .divideBy(new FixedPointNumber("116/100"))
                .multiply(new FixedPointNumber("26/100"));
        FixedPointNumber taxb = ((FixedPointNumber) umsatzInEur.clone())
                .divideBy(new FixedPointNumber("116/100"))
                .multiply(new FixedPointNumber("26/100")).negate();


        splitThisAccount.setValue(a);
        splitThisAccount.setQuantity(a);
        splitThisAccount.getJwsdpPeer().setSplitAction("Zahlung");
        splitOtherAccount.setValue(b);
        splitOtherAccount.setQuantity(b);
        splitOtherAccount.getJwsdpPeer().setSplitAction("Zahlung");
        splitOtherAccount.setLotID(lotID);

        splitThisAccountTaxes.setValue(taxa);
        splitThisAccountTaxes.setQuantity(taxa);
        splitOtherAccountTaxes.setValue(taxb);
        splitOtherAccountTaxes.setQuantity(taxb);

        assert newTransaction.isBalanced();
        return newTransaction;
    }



    public boolean doesTransactionExist(final Date buchungstag, final Date valuta, final String vorgang, final String buchungstext, final FixedPointNumber umsatzInEur) {
        List<? extends GnucashTransactionSplit> splits = getAccount().getTransactionSplits();
        for (GnucashTransactionSplit split : splits) {

            if (isSameTransaction(split, buchungstag, valuta, vorgang, buchungstext, umsatzInEur)) {
                return true;
            }
        }



        return false;
    }


    public boolean isSameTransaction(GnucashTransactionSplit split, Date buchungstag, Date valuta, String vorgang, String buchungstext, FixedPointNumber umsatzInEur) {

        //TODO: look around the given 2 dates for transactions of the same sum

        return false;
    }
}
