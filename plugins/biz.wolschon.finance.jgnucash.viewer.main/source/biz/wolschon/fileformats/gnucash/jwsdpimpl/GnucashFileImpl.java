/**
 * GnucashFileImpl.java
 * Created on 13.05.2005
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 *
 * -----------------------------------------------------------
 * major Changes:
 *  13.05.2005 - initial version
 * ...
 *
 */
package biz.wolschon.fileformats.gnucash.jwsdpimpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashCustomer;
import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashInvoice;
import biz.wolschon.fileformats.gnucash.GnucashInvoiceEntry;
import biz.wolschon.fileformats.gnucash.GnucashJob;
import biz.wolschon.fileformats.gnucash.GnucashTaxTable;
import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncAccountType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncCountDataType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncTransactionType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.ObjectFactory;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type.GncBookType.GncPricedbType.PriceType.PriceCommodityType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.DefaultJAXBContextImpl;
import biz.wolschon.finance.ComplexCurrencyTable;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * created: 13.05.2005<br/>
 * <br/>
 *  Implementation of GnucashFile that can only
 * read but not modify Gnucash-Files. <br/>
 *
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 * @see biz.wolschon.fileformats.gnucash.GnucashFile
 */
public class GnucashFileImpl implements GnucashFile {

    /**
     * Our logger for debug- and error-ourput.
     */
    private static final Log LOGGER = LogFactory.getLog(GnucashFileImpl.class);


    /**
     * my CurrencyTable.
     */
    private final ComplexCurrencyTable currencyTable = new ComplexCurrencyTable();


    /**
     * @return Returns the currencyTable.
     * @see ${@link #currencyTable}
     */
    public ComplexCurrencyTable getCurrencyTable() {
        return currencyTable;
    }


    /**
     *
     * @return a read-only collection of all accounts
     */
    public Collection<GnucashAccount> getAccounts() {
        if (accountid2account == null) {
            throw new IllegalStateException("no root-element loaded");
        }

        return Collections.unmodifiableCollection(new TreeSet<GnucashAccount>(accountid2account.values()));
    }

    /**
     * Filles lazy in getTaxTables() .
     * @see #getTaxTables()
     */
    protected Map<String, GnucashTaxTable> taxTablesById = null;

    /**
     * @param id id of a taxtable
     * @return the identified taxtable or null
     */
    public GnucashTaxTable getTaxTableByID(final String id) {
        if (taxTablesById == null) {
            getTaxTables();
        }
        return taxTablesById.get(id);
    }

    /**
     * @return all TaxTables defined in the book
     * @see ${@link GnucashTaxTable}
     */
    public Collection<GnucashTaxTable> getTaxTables() {
        if (taxTablesById == null) {

            taxTablesById = new HashMap<String, GnucashTaxTable>();

            for (Iterator iter = this.getRootElement().getGncBook().getGncGncTaxTable().iterator(); iter.hasNext();) {
                GncV2Type.GncBookType.GncGncTaxTableType jwsdpPeer = (GncV2Type.GncBookType.GncGncTaxTableType) iter.next();
                GnucashTaxTableImpl gnucashTaxTable = new GnucashTaxTableImpl(jwsdpPeer, this);
                taxTablesById.put(gnucashTaxTable.getId(), gnucashTaxTable);
            }
        }

        return taxTablesById.values();
    }

    /**
     *
     * @return a read-only collection of all accounts that have no parent (the
     *         result is sorted)
     */
    public Collection<? extends GnucashAccount> getRootAccounts() {
        try {
            Collection<GnucashAccount> retval = new TreeSet<GnucashAccount>();

            for (GnucashAccount account : getAccounts()) {
                if (account.getParentAccountId() == null) {
                    retval.add(account);
                }

            }

            return retval;
        } catch (RuntimeException e) {
            LOGGER.error("Problem getting all root-account", e);
            throw e;
        } catch (Throwable e) {
            LOGGER.error("SERIOUS Problem getting all root-account", e);
            return new LinkedList<GnucashAccount>();
        }
    }

    /**
     *
     * @param id
     *            if null, gives all account that have no parent
     * @return the sorted collection of children of that account
     */
    public Collection getAccountsByParentID(final String id) {
        if (accountid2account == null) {
            throw new IllegalStateException("no root-element loaded");
        }

        SortedSet retval = new TreeSet();

        for (Object element : accountid2account.values()) {
         GnucashAccount account = (GnucashAccount) element;

         String parent = account.getParentAccountId();
         if (parent == null) {
        if (id == null) {
            retval.add(account);
        }
         } else {
        if (parent.equals(id)) {
            retval.add(account);
        }
         }
      }

        return retval;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getAccountByName(java.lang.String)
     */
    public GnucashAccount getAccountByName(final String name) {

        if (accountid2account == null) {
            throw new IllegalStateException("no root-element loaded");
        }


        for (GnucashAccount account : accountid2account.values()) {
            if (account.getName().equals(name)) {
                return account;
            }
            if (account.getQualifiedName().equals(name)) {
                return account;
            }
        }
        return null;
    }
    /**
     * warning: this function has to traverse all
     * accounts. If it much faster to try
     * getAccountByID first and only call this method
     * if the returned account does not have the right name.
     *
     * @param nameRegEx the regular expression of the name to look for
     * @return null if not found
     * @see #getAccountByID(String)
     * @see #getAccountByName(String)
     */
    public GnucashAccount getAccountByNameEx(final String nameRegEx) {

        if (accountid2account == null) {
            throw new IllegalStateException("no root-element loaded");
        }

        GnucashAccount foundAccount = getAccountByName(nameRegEx);
        if (foundAccount != null) {
            return foundAccount;
        }
        Pattern pattern = Pattern.compile(nameRegEx);


        for (GnucashAccount account : accountid2account.values()) {
            Matcher matcher = pattern.matcher(account.getName());
            if (matcher.matches()) {
                return account;
            }
        }
        return null;
    }

    /**
     * First try to fetch the account by id, then
     * fall back to traversing all accounts to get
     * if by it's name.
     *
     * @param id the id to look for
     * @param name the name to look for if nothing is found for the id
     * @return null if not found
     * @see #getAccountByID(String)
     * @see #getAccountByName(String)
     */
    public GnucashAccount getAccountByIDorName(final String id, final String name) {
        GnucashAccount retval = getAccountByID(id);
        if (retval == null) {
            retval = getAccountByName(name);
        }
        return retval;
    }
    /**
     * First try to fetch the account by id, then
     * fall back to traversing all accounts to get
     * if by it's name.
     *
     * @param id the id to look for
     * @param name the regular expression of the name to look for
     *        if nothing is found for the id
     * @return null if not found
     * @see #getAccountByID(String)
     * @see #getAccountByName(String)
     */
    public GnucashAccount getAccountByIDorNameEx(final String id, final String name) {
        GnucashAccount retval = getAccountByID(id);
        if (retval == null) {
            retval = getAccountByNameEx(name);
        }
        return retval;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getInvoiceByID(java.lang.String)
     */
    public GnucashInvoice getInvoiceByID(final String id) {
        return invoiceid2invoice.get(id);
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getInvoices()
     */
    @SuppressWarnings("unchecked")
	public Collection<GnucashInvoice> getInvoices() {

        Collection<GnucashInvoice> c = invoiceid2invoice.values();

        ArrayList<GnucashInvoice>  retval = new ArrayList<GnucashInvoice> (c);
        Collections.sort(retval);

        return retval;
    }

    /**
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getUnpayedInvoices()
     */
    public Collection<GnucashInvoice> getUnpayedInvoices() throws JAXBException {
        Collection<GnucashInvoice> retval = new LinkedList<GnucashInvoice>();
        for (GnucashInvoice invoice : getInvoices()) {
            if (invoice.getAmmountUnPayed().isPositive()) {
                retval.add(invoice);
            }
        }

        return retval;
    }

    /**
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getUnpayedInvoicesForCustomer(biz.wolschon.fileformats.gnucash.GnucashCustomer)
     */
    public Collection<GnucashInvoice> getUnpayedInvoicesForCustomer(
            final GnucashCustomer customer) throws JAXBException {
        Collection<GnucashInvoice> retval = new LinkedList<GnucashInvoice>();
        for (GnucashInvoice invoice : getUnpayedInvoices()) {
            if (invoice.getJob().getCustomerId().equals(customer.getId())) {
                retval.add(invoice);
            }
        }

        return retval;
    }

    /**
     *
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getPayedInvoices()
     */
    public Collection<GnucashInvoice> getPayedInvoices() throws JAXBException {
        Collection<GnucashInvoice> retval = new LinkedList<GnucashInvoice>();
        for (GnucashInvoice invoice : getInvoices()) {
            if (!invoice.getAmmountUnPayed().isPositive()) {
                retval.add(invoice);
            }
        }

        return retval;
    }

    /**
     * @see #getFile()
     */
    private File file;

    /**
     * @param pCmdtySpace the namespace for pCmdtyId
     * @param pCmdtyId the currency-name
     * @return the latest price-quote in the gnucash-file in EURO
     * @see {@link GnucashFile#getLatestPrice(String, String)}
     */
    public FixedPointNumber getLatestPrice(final String pCmdtySpace,
                                            final String pCmdtyId) {
        return getLatestPrice(pCmdtySpace, pCmdtyId, 0);
    }

    /**
     * the top-level Element of the gnucash-files parsed and checked for
     * validity by JAXB.
     */
    private GncV2 rootElement;

    /**
     * All accounts indexed by their unique id-String.
     *
     * @see GnucashAccount
     * @see GnucashAccountImpl
     */
    protected Map<String, GnucashAccount> accountid2account;

    /**
     * All transactions indexed by their unique id-String.
     *
     * @see GnucashTransaction
     * @see GnucashTransactionImpl
     */
    protected Map<String, GnucashTransaction> transactionid2transaction;

    /**
     * All invoices indexed by their unique id-String.
     *
     * @see GnucashInvoice
     * @see GnucashInvoiceImpl
     */
    protected Map<String, GnucashInvoice> invoiceid2invoice;

    /**
     * All jobs indexed by their unique id-String.
     *
     * @see GnucashJob
     * @see GnucashJobImpl
     */
    protected Map<String, GnucashJob> jobid2job;

    /**
     * All customers indexed by their unique id-String.
     *
     * @see GnucashCustomer
     * @see GnucashCustomerImpl
     */
    protected Map<String, GnucashCustomer> customerid2customer;

    /**
     * @return the underlying JAXB-element
     */
    protected GncV2 getRootElement() {
        return rootElement;
    }

    /**
     * Set the new root-element and load all accounts, transactions,... from it.
     *
     * @param pRootElement
     *            the new root-element
     */
    @SuppressWarnings("unchecked")
    protected void setRootElement(final GncV2 pRootElement) {
        if (pRootElement == null) {
            throw new IllegalArgumentException(
                    "null not allowed for field this.rootElement");
        }
        rootElement = pRootElement;

        // fill prices

        loadPriceDatabase(pRootElement);


        // fill maps
        accountid2account = new HashMap<String, GnucashAccount>();
        for (Iterator iter = pRootElement.getGncBook().getGncAccount()
                .iterator(); iter.hasNext();) {
            GncAccountType jwsdpAccount = (GncAccountType) iter.next();
            try {
            	GnucashAccount account = createAccount(jwsdpAccount);
                accountid2account.put(account.getId(), account);
            } catch (RuntimeException e) {
                LOGGER.error("[RuntimeException] Problem in "
                           + getClass().getName()
                           + "ignoring illegal Account-Entry with id="
                           + jwsdpAccount.getActId().getValue(),
                             e);
            } catch (JAXBException e) {
                LOGGER.error("[JAXBException] Problem in "
                        + getClass().getName()
                        + "ignoring illegal Account-Entry with id="
                           + jwsdpAccount.getActId().getValue(),
                          e);
           }

        }

        invoiceid2invoice = new HashMap<String, GnucashInvoice>();
        for (Iterator iter = pRootElement.getGncBook().getGncGncInvoice()
                .iterator(); iter.hasNext();) {
            GncV2Type.GncBookType.GncGncInvoiceType jwsdpInvoice = (GncV2Type.GncBookType.GncGncInvoiceType) iter
                    .next();
            GnucashInvoice invoice = createInvoice(jwsdpInvoice);
            invoiceid2invoice.put(invoice.getId(), invoice);
        }
        // invoiceEntries reer to invoices, therefore they must be loaded after
        // them
        for (Iterator iter = pRootElement.getGncBook().getGncGncEntry()
                .iterator(); iter.hasNext();) {
            GncV2Type.GncBookType.GncGncEntryType jwsdpInvoiceEntry = (GncV2Type.GncBookType.GncGncEntryType) iter
                    .next();
            try {
                createInvoiceEntry(jwsdpInvoiceEntry);
            } catch (RuntimeException e) {
                LOGGER.error("[RuntimeException] Problem in "
                           + getClass().getName()
                           + "ignoring illegal Invoice-Entry with id="
                           + jwsdpInvoiceEntry.getEntryGuid().getValue(),
                             e);
            } catch (JAXBException e) {
                LOGGER.error("[JAXBException] Problem in "
                        + getClass().getName()
                        + "ignoring illegal Invoice-Entry with id="
                        + jwsdpInvoiceEntry.getEntryGuid().getValue(),
                          e);
           }
        }

        // transactions refer to invoices, therefore they must be loaded after
        // them
        transactionid2transaction = new HashMap<String, GnucashTransaction>();
        for (GncTransactionType jwsdpTransaction
                : (List<GncTransactionType>) pRootElement.getGncBook().getGncTransaction()) {


            try {
            	GnucashTransactionImpl account = createTransaction(jwsdpTransaction);
                transactionid2transaction.put(account.getId(), account);
                for (GnucashTransactionSplit split : account.getSplits()) {
                    /*String accountID = */split.getAccountID();
                }
            } catch (RuntimeException e) {
                LOGGER.error("[RuntimeException] Problem in "
                           + getClass().getName()
                           + "ignoring illegal Transaction-Entry with id="
                           + jwsdpTransaction.getTrnId().getValue(),
                             e);
            } catch (JAXBException e) {
                LOGGER.error("[JAXBException] Problem in "
                        + getClass().getName()
                        + "ignoring illegal Transaction-Entry with id="
                           + jwsdpTransaction.getTrnId().getValue(),
                          e);
           }

        }

        customerid2customer = new HashMap<String, GnucashCustomer>();
        for (GncV2Type.GncBookType.GncGncCustomerType jwsdpCustomer
                : (List<GncV2Type.GncBookType.GncGncCustomerType>) pRootElement.getGncBook().getGncGncCustomer()) {

            try {
                GnucashCustomerImpl customer = createCustomer(jwsdpCustomer);
                customerid2customer.put(customer.getId(), customer);
            } catch (JAXBException e) {
                LOGGER.error("[JAXBException] Problem in "
                           + getClass().getName(),
                             e);
            }
        }

        jobid2job = new HashMap<String, GnucashJob>();
        for (GncV2Type.GncBookType.GncGncJobType jwsdpjob
                :(List<GncV2Type.GncBookType.GncGncJobType>) pRootElement.getGncBook().getGncGncJob()) {

            GnucashJobImpl job = createJob(jwsdpjob);
            String jobID = job.getId();
            if (jobID == null) {
                LOGGER.error("File contains a job w/o an ID. indexing it with the ID ''");
                jobID = "";
            }
            jobid2job.put(job.getId(), job);
        }

    }


	/**
	 * @param pRootElement
	 */
	private void loadPriceDatabase(final GncV2 pRootElement) {
		if (pRootElement.getGncBook().getGncPricedb() == null) {
        	//case: no priceDB in file
        	getCurrencyTable().clear();
        } else {
        	if (pRootElement.getGncBook().getGncPricedb().getVersion() != 1) {

        		LOGGER.warn("We know only the format of the price-db 1, "
        				+ "the file has version "
        				+ pRootElement.getGncBook().getGncPricedb().getVersion()
        				+ " prices will not be loaded!");
        	} else {
        	  //TODO: use base-currency from gnucash-file
        		getCurrencyTable().clear();
        		getCurrencyTable().setConversionFactor("ISO4217",
        				"EUR",
        				new FixedPointNumber(1));

        		for (Iterator iter = pRootElement.getGncBook()
        				.getGncPricedb().getPrice().iterator(); iter.hasNext();) {
        			GncV2Type.GncBookType.GncPricedbType.PriceType price = (GncV2Type.GncBookType.GncPricedbType.PriceType) iter.next();
        			PriceCommodityType comodity = price.getPriceCommodity();

                    // check if we already have a latest price for this comodity
                    // (=currency, fund, ...)
                    if (getCurrencyTable().getConversionFactor(comodity.getCmdtySpace(),
                            comodity.getCmdtyId()) != null) {
                        continue;
                    }
//TODO: use base-currency from gnucash-file
                    if (comodity.getCmdtySpace().equals("ISO4217")
                            &&
                            comodity.getCmdtyId().equals("EUR")) {
                        LOGGER.warn("Ignoring price-quote for EUR because EUR is"
                                + "our base-currency.");
                        continue;
                    }

                    // get the latest price in the file and insert it into
                    // our currency table
                    FixedPointNumber factor = getLatestPrice(
                            comodity.getCmdtySpace(),
                            comodity.getCmdtyId());

                    if (factor != null) {
                        getCurrencyTable().setConversionFactor(comodity.getCmdtySpace(),
                                comodity.getCmdtyId(),
                                factor);
                    } else {
                        LOGGER.warn("The gnucash-file defines a factor for a comodity '"
                                + comodity.getCmdtySpace()
                                + "' - '"
                                + comodity.getCmdtyId()
                                + "' but has no comodity for it");
                    }
                }
            }
        }
    }


    /**
     * @see {@link #getLatestPrice(String, String)}
     */
    protected static final DateFormat PRICEQUOTEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");

    /**
     * @param pCmdtySpace the namespace for pCmdtyId
     * @param pCmdtyId the currency-name
     * @param depth used for recursion. Allways call with '0'
     * @param used for aborting recursive quotes (quotes to other then the base-
     *        currency) we abort if the depth reached 6.
     * @return the latest price-quote in the gnucash-file in EURO
     * @see {@link GnucashFile#getLatestPrice(String, String)}
     */
    private FixedPointNumber getLatestPrice(final String pCmdtySpace,
                                            final String pCmdtyId,
                                            final int depth) {

        if (pCmdtySpace == null) {
            throw new IllegalArgumentException("null parameter 'pCmdtySpace' "
                                             + "given");
        }
        if (pCmdtyId == null) {
            throw new IllegalArgumentException("null parameter 'pCmdtyId' "
                                             + "given");
        }

        Date latestDate = null;
        FixedPointNumber latestQuote = null;

        // factor is used if the quote is not to our base-currency
        FixedPointNumber factor = new FixedPointNumber(1);



            for (Iterator iterator = getRootElement().getGncBook()
                                    .getGncPricedb().getPrice()
                                    .iterator(); iterator.hasNext();) {
                GncV2Type.GncBookType.GncPricedbType.PriceType priceQuote
                  = (GncV2Type.GncBookType.GncPricedbType.PriceType)
                  iterator.next();


                try {
                    if (priceQuote == null) {
                        LOGGER.warn("gnucash-file contains null price-quotes"
                                + " there may be a problem with JWSDP");
                        continue;
                    }

                    if (priceQuote.getPriceCurrency() == null) {
                        LOGGER.warn("gnucash-file contains price-quotes"
                                + " with no currency id='"
                                + priceQuote.getPriceId().getValue()
                                + "'");
                        continue;
                    }

                    if (priceQuote.getPriceCurrency().getCmdtyId() == null) {
                        LOGGER.warn("gnucash-file contains price-quotes"
                                + " with no currency-id id='"
                                + priceQuote.getPriceId().getValue()
                                + "'");
                        continue;
                    }
                    if (priceQuote.getPriceCurrency().getCmdtySpace() == null) {
                        LOGGER.warn("gnucash-file contains price-quotes"
                                + " with no currency-namespace id='"
                                + priceQuote.getPriceId().getValue()
                                + "'");
                        continue;
                    }
                    if (priceQuote.getPriceTime() == null) {
                        LOGGER.warn("gnucash-file contains price-quotes"
                                + " with no timestamp id='"
                                + priceQuote.getPriceId().getValue()
                                + "'");
                        continue;
                    }
                    if (priceQuote.getPriceValue() == null) {
                        LOGGER.warn("gnucash-file contains price-quotes"
                                + " with no value id='"
                                + priceQuote.getPriceId().getValue()
                                + "'");
                        continue;
                    }
                    /*if (priceQuote.getPriceCommodity().getCmdtySpace().equals("FUND")
                            &&
                        priceQuote.getPriceType() == null) {
                        LOGGER.warn("gnucash-file contains FUND-price-quotes"
                                + " with no type id='"
                                + priceQuote.getPriceId().getValue()
                                + "'");
                        continue;
                    }*/


                    if (!priceQuote.getPriceCommodity().getCmdtySpace().equals(pCmdtySpace)) {
                        continue;
                    }

                    if (!priceQuote.getPriceCommodity().getCmdtyId().equals(pCmdtyId)) {
                        continue;
                    }

                    /*if (priceQuote.getPriceCommodity().getCmdtySpace().equals("FUND")
                            &&
                        (priceQuote.getPriceType() == null
                            ||
                         !priceQuote.getPriceType().equals("last")
                       )) {
                        LOGGER.warn("ignoring FUND-price-quote of unknown type '"
                                  + priceQuote.getPriceType()
                                  + "' expecting 'last' ");
                        continue;
                    }*/


                    if (!priceQuote.getPriceCurrency()
                            .getCmdtySpace().equals("ISO4217")) {
                        if (depth > 5) {
                            LOGGER.warn("ignoring price-quote that is not in an"
                                  + " ISO4217 -currency but in '"
                                  + priceQuote.getPriceCurrency().getCmdtyId());
                            continue;
                        }
                        factor = getLatestPrice(priceQuote.getPriceCurrency()
                                .getCmdtySpace(), priceQuote.getPriceCurrency()
                                .getCmdtyId(), depth + 1);
                    } else { //TODO: use base-currency from gnucash-file
                        if (!priceQuote.getPriceCurrency()
                                .getCmdtyId().equals("EUR")) {
                            if (depth > 5) {
                                LOGGER.warn("ignoring price-quote that is not in EUR "
                                        + "but in  '"
                                        + priceQuote.getPriceCurrency().getCmdtyId());
                                continue;
                            }
                            factor = getLatestPrice(priceQuote.getPriceCurrency()
                                    .getCmdtySpace(), priceQuote.getPriceCurrency()
                                    .getCmdtyId(), depth + 1);
                        }
                    }

                    Date date = PRICEQUOTEDATEFORMAT.parse(
                            priceQuote.getPriceTime().getTsDate());

                    if (latestDate == null || latestDate.before(date)) {
                        latestDate = date;
                        latestQuote = new FixedPointNumber(
                                priceQuote.getPriceValue());
                        LOGGER.debug(getClass().getName()
                                + ".getLatestPrice(pCmdtySpace='"
                                + pCmdtySpace
                                + "', String pCmdtyId='"
                                + pCmdtyId
                                + "') converted " + latestQuote
                                + " <= "
                                + priceQuote.getPriceValue());
                    }

                } catch (NumberFormatException e) {
                    LOGGER.error("[NumberFormatException] Problem in "
                               + getClass().getName()
                               + ".getLatestPrice(pCmdtySpace='"
                               + pCmdtySpace
                               + "', String pCmdtyId='"
                               + pCmdtyId
                               + "')! Ignoring a bad price-quote '"
                               + priceQuote
                               + "'",
                                 e);
                } catch (ParseException e) {
                    LOGGER.error("[ParseException] Problem in "
                            + getClass().getName()
                            + ".getLatestPrice(pCmdtySpace='"
                            + pCmdtySpace
                            + "', String pCmdtyId='"
                            + pCmdtyId
                            + "')! Ignoring a bad price-quote '"
                            + priceQuote
                            + "'",
                              e);
                } catch (NullPointerException e) {
                    LOGGER.error("[NullPointerException] Problem in "
                            + getClass().getName()
                            + ".getLatestPrice(pCmdtySpace='"
                            + pCmdtySpace
                            + "', String pCmdtyId='"
                            + pCmdtyId
                            + "')! Ignoring a bad price-quote '"
                            + priceQuote
                            + "'",
                              e);
                }

            }

        LOGGER.debug(getClass().getName()
                            + ".getLatestPrice(pCmdtySpace='"
                            + pCmdtySpace
                            + "', String pCmdtyId='"
                            + pCmdtyId
                            + "')= " + latestQuote
                            + " from "
                            + latestDate);
        if (latestQuote == null) {
            return null;
        }
        return factor.multiply(latestQuote);
    }


    /**
     * @param jwsdpAccount
     *            the JWSDP-peer (parsed xml-element) to fill our object with
     * @return the new GnucashAccount to wrap the given jaxb-object.
     */
    protected GnucashAccount createAccount(final GncAccountType jwsdpAccount) throws JAXBException {
        GnucashAccount account = new GnucashAccountImpl(jwsdpAccount, this);
        return account;
    }

    /**
     * @param jwsdpInvoice
     *            the JWSDP-peer (parsed xml-element) to fill our object with
     * @return the new GnucashInvoice to wrap the given jaxb-object.
     */
    protected GnucashInvoice createInvoice(
            final GncV2Type.GncBookType.GncGncInvoiceType jwsdpInvoice) {
        GnucashInvoice invoice = new GnucashInvoiceImpl(jwsdpInvoice, this);
        return invoice;
    }

    /**
     * @param jwsdpInvoiceEntry
     *            the JWSDP-peer (parsed xml-element) to fill our object with
     * @return the new GnucashInvoiceEntry to wrap the given jaxb-object.
     * @throws JAXBException on problems with the xml-backend
     */
    protected GnucashInvoiceEntry createInvoiceEntry(
            final GncV2Type.GncBookType.GncGncEntryType jwsdpInvoiceEntry) throws JAXBException {
        GnucashInvoiceEntry entry = new GnucashInvoiceEntryImpl(
                jwsdpInvoiceEntry, this);
        return entry;
    }

    /**
     * @param jwsdpjob
     *            the JWSDP-peer (parsed xml-element) to fill our object with
     * @return the new GnucashJob to wrap the given jaxb-object.
     */
    protected GnucashJobImpl createJob(
            final GncV2Type.GncBookType.GncGncJobType jwsdpjob) {

        GnucashJobImpl job = new GnucashJobImpl(jwsdpjob, this);
        return job;
    }

    /**
     * @param jwsdpCustomer
     *            the JWSDP-peer (parsed xml-element) to fill our object with
     * @return the new GnucashCustomer to wrap the given jaxb-object.
     * @throws JAXBException on problems with the xml-backend
     */
    protected GnucashCustomerImpl createCustomer(
            final GncV2Type.GncBookType.GncGncCustomerType jwsdpCustomer) throws JAXBException {
        GnucashCustomerImpl customer = new GnucashCustomerImpl(jwsdpCustomer,
                this);
        return customer;
    }

    /**
     * @param jwsdpTransaction
     *            the JWSDP-peer (parsed xml-element) to fill our object with
     * @return the new GnucashTransaction to wrap the given jaxb-object.
     */
    protected GnucashTransactionImpl createTransaction(
            final GncTransactionType jwsdpTransaction) throws JAXBException {
        GnucashTransactionImpl account = new GnucashTransactionImpl(
                jwsdpTransaction, this);
        return account;
    }

    /**
     * @param pFile
     *            the file to load and initialize from
     * @throws IOException
     *             on low level reading-errors (FileNotFoundException if not
     *             found)
     * @throws JAXBException
     *             on jaxb-errors (invalid xml,...)
     * @see #loadFile(File)
     */
    public GnucashFileImpl(final File pFile) throws IOException, JAXBException {
        super();
        loadFile(pFile);
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getFile()
     */
    public File getFile() {
        return file;
    }

    /**
     * Internal method, just sets this.file .
     *
     * @param pFile
     *            the file loaded
     */
    protected void setFile(final File pFile) {
        if (pFile == null) {
            throw new IllegalArgumentException(
                    "null not allowed for field this.file");
        }
        file = pFile;
    }

    /**
     * loads the file and calls setRootElement.
     *
     * @param pFile
     *            the file to read
     * @throws IOException
     *             on low level reading-errors (FileNotFoundException if not
     *             found)
     * @throws JAXBException
     *             on jaxb-errors (invalid xml,...)
     * @see #setRootElement(GncV2)
     */
    protected void loadFile(final File pFile) throws IOException, JAXBException {

        long start = System.currentTimeMillis();

        if (pFile == null) {
            throw new IllegalArgumentException(
                    "null not allowed for field this.file");
        }

        if (!pFile.exists()) {
            throw new IllegalArgumentException("Given file '"
                    + pFile.getAbsolutePath() + "' does not exist!");
        }

        setFile(pFile);

        InputStream in = new FileInputStream(pFile);
        if (pFile.getName().endsWith(".gz")) {
            in = new BufferedInputStream(in);
            in = new GZIPInputStream(in);
        } else {
            // determine if it's gzipped by the magic bytes
            byte[] magic = new byte[2];
            in.read(magic);
            in.close();

            in = new FileInputStream(pFile);
            in = new BufferedInputStream(in);
            if (magic[0] == 31 && magic[1] == -117) {
                in = new GZIPInputStream(in);
            }
        }


        // reader = new NamespaceRemovererReader(new FileReader(file));
        /* V1
         NamespaceRemovererReader reader = new NamespaceRemovererReader(
                new EuroConverterReader(new InputStreamReader(
                        in, "ISO8859-15")));
         */

        // works with V1.9 and V2.0
        NamespaceRemovererReader reader
                        = new NamespaceRemovererReader(new InputStreamReader(
                in, "utf-8"));
        try {

            try {

                JAXBContext myContext = getJAXBContext();
                Unmarshaller unmarshaller = myContext.createUnmarshaller();

                GncV2 o = (GncV2) unmarshaller.unmarshal(new InputSource(
                        new BufferedReader(reader)));
                long start2 = System.currentTimeMillis();
                setRootElement(o);
                long end = System.currentTimeMillis();
                LOGGER.info("GnucashFileImpl.loadFile took "
                        + (end - start) + " ms (total) "
                        + (start2 - start) + " ms (jaxb-loading)"
                        + (end - start2) + " ms (building facades)"
                                                             );

            } finally {
                reader.close();
            }
        } catch (JAXBException ex) {

            // output what has been reat to far
        	if (reader instanceof NamespaceRemovererReader) {
                try {
                    NamespaceRemovererReader nsr = reader;
                    int pos = (int) nsr.getPosition();
                    char[] c = new char[pos];
                    NamespaceRemovererReader reader2 = new NamespaceRemovererReader(
                            new EuroConverterReader(new InputStreamReader(
                                    new FileInputStream(pFile), "ISO8859-15")));
                    try {
                        reader2.read(c);
                    } finally {
                        reader2.close();
                    }
                    System.err.println("reat so far:");
                    String s = new String(c);
                    System.err.println(s.substring(Math.max(0, s.length() - 500)));
                } catch (Throwable x) {
                    // ignore
                }
            }

            if (reader instanceof NamespaceRemovererReader && (reader).debugLastReatLength > 0) {
            	NamespaceRemovererReader nsr = reader;
                System.err.println("last reat chars: '"
                        + new String(nsr.debugLastTeat, 0,
                                     nsr.debugLastReatLength) + "'");
            } else {
                System.err.println("last reat chars: none!");
            }


            throw ex;
        }
    }

    /**
     * @see #getObjectFactory()
     */
    private volatile ObjectFactory myJAXBFactory;

    /**
     * @return the jaxb object-factory used to create new peer-objects to extend
     *         this
     * @throws JAXBException
     *             o jaxb-errors
     */
    protected ObjectFactory getObjectFactory() throws JAXBException {
        if (myJAXBFactory == null) {
            myJAXBFactory = new ObjectFactory();
        }
        return myJAXBFactory;
    }

    /**
     * @see #getJAXBContext()
     */
    private volatile JAXBContext myJAXBContext;


    /**
     * @return the JAXB-context
     * @throws JAXBException
     *             on jaxb-errors
     */
    protected JAXBContext getJAXBContext() throws JAXBException {
        if (myJAXBContext == null) {
            myJAXBContext = DefaultJAXBContextImpl
                    .newInstance("biz.wolschon.fileformats.gnucash.jwsdpimpl.generated", this.getClass().getClassLoader());
        }
        return myJAXBContext;
    }

    /**
     *
     * @param type
     *            the type-string to look for
     * @return the count-data saved in the xml-file
     */
    protected GncCountDataType findCountDataByType(final String type) {
        for (Iterator iter = getRootElement().getGncBook().getGncCountData()
                .iterator(); iter.hasNext();) {
            GncCountDataType count = (GncCountDataType) iter.next();
            if (count.getCdType().equals(type)) {
                return count;
            }
        }
        return null;
    }

    /**
     *
     * @return the number of transactions
     */
    protected int getTransactionCount() {
        GncCountDataType count = findCountDataByType("transaction");
        return count.getValue();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getAccountByID(java.lang.String)
     */
    public GnucashAccount getAccountByID(final String id) {
        if (accountid2account == null) {
            throw new IllegalStateException("no root-element loaded");
        }

        GnucashAccount retval = accountid2account.get(id);
        if (retval == null) {
            System.err.println("No Account with id '" + id + "'. We know "
                    + accountid2account.size() + " accounts.");
        }
        return retval;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getCustomerByID(java.lang.String)
     */
    public GnucashCustomer getCustomerByID(final String id) {
        if (accountid2account == null) {
            throw new IllegalStateException("no root-element loaded");
        }

        GnucashCustomer retval = customerid2customer.get(id);
        if (retval == null) {
            LOGGER.warn("No Customer with id '" + id + "'. We know "
                    + customerid2customer.size() + " accounts.");
        }
        return retval;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getCustomerByName(java.lang.String)
     */
    public GnucashCustomer getCustomerByName(final String name) {

        if (accountid2account == null) {
            throw new IllegalStateException("no root-element loaded");
        }

        for (GnucashCustomer customer : getCustomers()) {
            if (customer.getName().equals(name)) {
                return customer;
            }
        }
        return null;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getCustomers()
     */
    public Collection<GnucashCustomer> getCustomers() {
        return customerid2customer.values();
    }

    /**
     *
     * @param customer the customer to look for.
     * @return all jobs that have this customer, never null
     */
    public Collection getJobsByCustomer(final GnucashCustomer customer) {
        if (jobid2job == null) {
            throw new IllegalStateException("no root-element loaded");
        }


        Collection retval = new LinkedList();

        for (Object element : jobid2job.values()) {
            GnucashJob job = (GnucashJob) element;
            if (job.getCustomerId().equals(customer.getId())) {
                retval.add(job);
            }

        }
        return retval;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getJobByID(java.lang.String)
     */
    public GnucashJob getJobByID(final String id) {
        if (jobid2job == null) {
            throw new IllegalStateException("no root-element loaded");
        }


        GnucashJob retval = jobid2job.get(id);
        if (retval == null) {
            LOGGER.warn("No Job with id '"
                       + id
                       + "'. We know "
                       + customerid2customer.size()
                       + " accounts.");
        }

        return retval;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getJobs()
     */
    public Collection<GnucashJob> getJobs() {
        if (jobid2job == null) {
            throw new IllegalStateException("no root-element loaded");
        }
        return jobid2job.values();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getTransactionByID(java.lang.String)
     */
    public GnucashTransaction getTransactionByID(final String id) {
        if (transactionid2transaction == null) {
            throw new IllegalStateException("no root-element loaded");
        }

        GnucashTransaction retval = transactionid2transaction
                .get(id);
        if (retval == null) {
            LOGGER.warn("No Transaction with id '" + id + "'. We know "
                    + customerid2customer.size() + " accounts.");
        }
        return retval;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashFile#getTransactions()
     */
    public Collection<? extends GnucashTransaction> getTransactions() {
        if (transactionid2transaction == null) {
            throw new IllegalStateException("no root-element loaded");
        }

        return Collections.unmodifiableCollection(transactionid2transaction
                .values());
    }

    /**
     *
     * created: 13.05.2005 <br/>
     *
     *
     * replaces ':' in tag-names and attribute-names by '_' .
     * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
     */
    public static class NamespaceRemovererReader extends Reader {

        /**
         * How much we have reat.
         */
        private long position = 0;

        /**
         * @return How much we have reat.
         */
        public long getPosition() {
            return position;
        }

        /**
         * @param pInput what to read from.
         */
        public NamespaceRemovererReader(final Reader pInput) {
            super();
            input = pInput;
        }

        /**
         *
         * @return What to read from.
         */
        public Reader getInput() {
            return input;
        }

        /**
         *
         * @param newInput What to read from.
         */
        public void setInput(final Reader newInput) {
            if (newInput == null) {
                throw new IllegalArgumentException(
                "null not allowed for field this.input");
            }

            input = newInput;
        }

        /**
         * What to read from.
         */
        private Reader input;

        /**
         * true if we are in a quotation and thus
         * shall not remove any namespaces.
         */
        private boolean isInQuotation = false;

        /**
         * true if we are in a quotation and thus
         * shall remove any namespaces.
         */
        private boolean isInTag = false;

        /**
         *
         * @see java.io.Reader#close()
         */
        @Override
        public void close() throws IOException {
            input.close();
        }

        /**
         * For debugging.
         */
        public char[] debugLastTeat = new char[255];

        /**
         * For debugging.
         */
        public int debugLastReatLength = -1;

        /**
         * Log the last chunk of bytes reat for debugging-purposes.
         *
         * @param cbuf the data
         * @param off where to start in cbuf
         * @param reat how much
         */
        private void logReatBytes(final char[] cbuf,
                                  final int off,
                                  final int reat) {
            debugLastReatLength = Math.min(debugLastTeat.length, reat);
            try {
                System.arraycopy(cbuf, off, debugLastTeat, 0,
                        debugLastTeat.length);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.debug("debugLastReatLength=" + debugLastReatLength
                        + "\n" + "off=" + off + "\n" + "reat=" + reat + "\n"
                        + "cbuf.length=" + cbuf.length + "\n"
                        + "debugLastTeat.length=" + debugLastTeat.length
                        + "\n");
            }
        }

        /**
         *
         * @see java.io.Reader#read(char[], int, int)
         */
        @Override
        public int read(final char[] cbuf,
                        final int off,
                        final int len) throws IOException {

            int reat = input.read(cbuf, off, len);

            logReatBytes(cbuf, off, reat);

            for (int i = off; i < off + reat; i++) {
                position++;

                if (isInTag && (cbuf[i] == '"' || cbuf[i] == '\'')) {
                    toggleIsInQuotation();
                } else if (cbuf[i] == '<' && !isInQuotation) {
                    isInTag = true;
                } else if (cbuf[i] == '>' && !isInQuotation) {
                    isInTag = false;
                } else if (cbuf[i] == ':' && isInTag && !isInQuotation) {
                    cbuf[i] = '_';
                }

            }

            return reat;
        }

        /**
         *
         */
        private void toggleIsInQuotation() {
            if (isInQuotation) {
                isInQuotation = false;
            } else {
                isInQuotation = true;
            }
        }
    }

    /**
     *
     * created: 13.05.2005 <br/>
     *
     *
     * replaces &#164; by the euro-sign .
     * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
     */
    public static class EuroConverterReader extends Reader {

        /**
         * This is "&#164;".length .
         */
        private static final int REPLACESTRINGLENGTH = 5;

        /**
         * @param pInput Where to read from.
         */
        public EuroConverterReader(final Reader pInput) {
            super();
            input = pInput;
        }

        /**
         *
         * @return Where to read from.
         */
        public Reader getInput() {
            return input;
        }

        /**
         *
         * @param newInput Where to read from.
         */
        public void setInput(Reader newInput) {
            if (newInput == null) {
                throw new IllegalArgumentException(
                "null not allowed for field this.input");
            }

            input = newInput;
        }

        /**
         * Where to read from.
         */
        private Reader input;

        /**
         *
         * @see java.io.Reader#close()
         */
        @Override
        public void close() throws IOException {
            input.close();

        }

        /**
         *
         * @see java.io.Reader#read(char[], int, int)
         */
        @Override
        public int read(final char[] cbuf,
                        final int off,
                        final int len) throws IOException {

            int reat = input.read(cbuf, off, len);

            // this does not work if the euro-sign is wrapped around the
            // edge of 2 read-call buffers

            int state = 0;

            for (int i = off; i < off + reat; i++) {

                switch (state) {

                case 0: {
                    if (cbuf[i] == '&') {
                        state++;
                    }
                    break;
                }

                case 1: {
                    if (cbuf[i] == '#') {
                        state++;
                    } else {
                        state = 0;
                    }
                    break;
                }

                case 2: {
                    if (cbuf[i] == '1') {
                        state++;
                    } else {
                        state = 0;
                    }
                    break;
                }

                case REPLACESTRINGLENGTH - 2: {
                    if (cbuf[i] == '6') {
                        state++;
                    } else {
                        state = 0;
                    }
                    break;
                }

                case REPLACESTRINGLENGTH - 1: {
                    if (cbuf[i] == '4') {
                        state++;
                    } else {
                        state = 0;
                    }
                    break;
                }
                case REPLACESTRINGLENGTH: {
                    if (cbuf[i] == ';') {
                        // found it!!!
                        cbuf[i - REPLACESTRINGLENGTH] = '¤';
                        if (i != reat - 1) {
                            System.arraycopy(cbuf, (i + 1), cbuf,
                                    (i - (REPLACESTRINGLENGTH - 1)),
                                    (reat - i - 1));
                        }
                        int reat2 = input
                                .read(cbuf, reat - REPLACESTRINGLENGTH,
                                        REPLACESTRINGLENGTH);
                        if (reat2 != REPLACESTRINGLENGTH) {
                            reat -= (REPLACESTRINGLENGTH - reat2);
                        }
                        i -= (REPLACESTRINGLENGTH - 1);
                        state = 0;
                    } else {
                        state = 0;
                    }
                    break;
                }

                default:
                }

            }
            return reat;
        };
    }


}
