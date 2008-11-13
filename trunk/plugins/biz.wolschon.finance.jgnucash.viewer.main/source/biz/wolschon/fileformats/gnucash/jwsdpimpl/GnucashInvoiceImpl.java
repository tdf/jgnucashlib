/**
 * GnucashInvoiceImpl.java
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;


import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashCustomer;
import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashInvoice;
import biz.wolschon.fileformats.gnucash.GnucashInvoiceEntry;
import biz.wolschon.fileformats.gnucash.GnucashJob;
import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type;
import biz.wolschon.numbers.FixedPointNumber;

/**
 *
 * created: 13.05.2005 <br/>
 * Implementation of GnucashInvoice that uses JWSDP.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class GnucashInvoiceImpl implements GnucashInvoice {


    /**
     *
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getCustomer()
     */
    public GnucashCustomer getCustomer() {
        return getJob().getCustomer();
    }
    /**
     * @return getAmmountWithoutTaxes().isMoreThen(getAmmountPayedWithoutTaxes())
     * 
     * @throws JAXBException if we have issues with the XML-backend
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#isNotFullyPayed()
     */
    public boolean isNotFullyPayed() throws JAXBException {
        return getAmmountWithTaxes().isMoreThen(getAmmountPayedWithTaxes());
    }

    /**
     * Format of the JWSDP-field openedDate.
     */
    protected static final DateFormat OPENEDDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");



    /**
     * The transactions that are paying for this invoice.
     */
    private Collection<GnucashTransaction> payingTransactions = new LinkedList<GnucashTransaction>();

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#addPayingTransaction(biz.wolschon.fileformats.gnucash.GnucashTransaction)
     */
    public void addPayingTransaction(final GnucashTransactionSplit trans) {

//        System.err.println("DEBUG: "
//                         + getClass().getName()
//                         + ".addPayingTransaction(split-action="
//                         + trans.getSplitAction()
//                         + ")");

        payingTransactions.add(trans.getTransaction());

    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#addTransaction(GnucashTransaction)
     */
    public void addTransaction(final GnucashTransaction trans) {
        //

    }


    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getPayingTransaction()
     */
    public Collection<GnucashTransaction> getPayingTransactions() {
        return payingTransactions;
    }
    
    /**
	 * @return
	 */
	public String getAccountIDToTransferMoneyTo() {
		return jwsdpPeer.getInvoicePostacc().getValue();
	}

    /**
     * @return the transaction that transferes the money from the customer to
     *         the account for money you are to get and the one you owe the
     *         taxes.
     */
    public GnucashTransaction getPostTransaction() {
     if (jwsdpPeer.getInvoicePosttxn() == null) {
     	return null;//unposted invoices have no postlot
     }
     return file.getTransactionByID(jwsdpPeer.getInvoicePosttxn().getValue());
    }

    /**
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getAmmountPayedWithTaxes()
     */
    public FixedPointNumber getAmmountPayedWithTaxes() throws JAXBException {

        FixedPointNumber takenFromReceivableAccount = new FixedPointNumber();
        for (GnucashTransaction transaction : getPayingTransactions()) {
            
            for (GnucashTransactionSplit split : transaction.getSplits()) {
                
                if (split.getAccount().getType().equals(GnucashAccount.ACCOUNTTYPE_RECEIVABLE)
                        &&
                  !split.getValue().isPositive()
                   )
                    takenFromReceivableAccount.subtract(split.getValue());
            }

        }


//        System.err.println("getAmmountPayedWithoutTaxes="+takenFromReceivableAccount.doubleValue());

        return takenFromReceivableAccount;
    }

    /**
     *
     *
     * 
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getAmmountUnPayed()
     */
    public FixedPointNumber getAmmountUnPayed() throws JAXBException {

// System.err.println("debug: GnucashInvoiceImpl.getAmmountUnPayed(): "
// + "getAmmountWithoutTaxes()="+getAmmountWithoutTaxes()+" getAmmountPayedWithTaxes()="+getAmmountPayedWithTaxes() );

        return ((FixedPointNumber) getAmmountWithTaxes().clone()).subtract(getAmmountPayedWithTaxes());


    }


    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getAmmountWithoutTaxes()
     */
    public FixedPointNumber getAmmountWithoutTaxes() {

        FixedPointNumber retval = new FixedPointNumber();

        for (Iterator iter = getEntries().iterator(); iter.hasNext();) {
            GnucashInvoiceEntry entry = (GnucashInvoiceEntry) iter.next();
            retval.add(entry.getSumExclTaxes());
        }

        return retval;
    }


    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getTaxes()
     */
    public TaxedSum[] getTaxes() {

        List taxedSums = new LinkedList();

        invoiceentries:
            for (Iterator iter = getEntries().iterator(); iter.hasNext();) {
            GnucashInvoiceEntry entry = (GnucashInvoiceEntry) iter.next();

            FixedPointNumber taxpercent = entry.getApplicableTaxPercend();

            for (Iterator iterator = taxedSums.iterator(); iterator.hasNext();) {
                TaxedSum taxedSum = (TaxedSum) iterator.next();
                if (taxedSum.getTaxpercent().equals(taxpercent)) {
                    taxedSum.setTaxsum(
                            taxedSum.getTaxsum().add(
                                    entry.getSumInclTaxes().subtract(entry.getSumExclTaxes())
                                                    )
                                    );
                    continue invoiceentries;
                }
            }

            TaxedSum taxedSum = new TaxedSum(taxpercent, entry.getSumInclTaxes().subtract(entry.getSumExclTaxes()));
            taxedSums.add(taxedSum);

        }

        return (TaxedSum[]) taxedSums.toArray(new TaxedSum[taxedSums.size()]);

    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getAmmountWithTaxes()
     */
    public FixedPointNumber getAmmountWithTaxes() {


        FixedPointNumber retval = new FixedPointNumber();

            //TODO: we should sum them without taxes grouped by tax% and
            //      multiply the sums with the tax% to be calculatng
            //      correctly

            for (Iterator iter = getEntries().iterator(); iter.hasNext();) {
                GnucashInvoiceEntry entry = (GnucashInvoiceEntry) iter.next();
                retval.add(entry.getSumInclTaxes());
            }

            return retval;
        }



    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getJob()
     */
    public GnucashJob getJob() {
        return file.getJobByID(getJobID());
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getJobID()
     */
    public String getJobID() {
        return jwsdpPeer.getInvoiceOwner().getOwnerId().getValue();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getJobType()
     */
    public String getJobType() {
        return jwsdpPeer.getInvoiceOwner().getOwnerType();
    }

    /**
     * the JWSDP-object we are facading.
     */
    protected GncV2Type.GncBookType.GncGncInvoiceType jwsdpPeer;

    /**
     * The file we belong to.
     */
    private GnucashFile file;


    /**
     * @param peer the JWSDP-object we are facading.
     * @see #jwsdpPeer
     * @param gncFile the file to register under
     */
    public GnucashInvoiceImpl(
            final GncV2Type.GncBookType.GncGncInvoiceType peer,
            final GnucashFile gncFile) {
        super();
        this.jwsdpPeer = peer;
        this.file = gncFile;

    }
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getId()
     */
    public String getId() {
        return jwsdpPeer.getInvoiceGuid().getValue();
    }

    /**
     *
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getLotID()
     */
    public String getLotID() {
    	if (jwsdpPeer.getInvoicePostlot() == null) {
    		return null;//unposted invoices have no postlot
    	}
        return jwsdpPeer.getInvoicePostlot().getValue();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getDescription()
     */
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getFile()
     */
    public GnucashFile getFile() {
        return file;
    }


    /**
     * The entries of this invoice.
     */
    protected Collection<GnucashInvoiceEntry> entries = new HashSet<GnucashInvoiceEntry>();

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getEntries()
     */
    public Collection<GnucashInvoiceEntry> getEntries() {
        return entries;
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getEntryById(java.lang.String)
     */
    public GnucashInvoiceEntry getEntryById(final String id) {
        for (GnucashInvoiceEntry element : getEntries()) {
            if (element.getId().equals(id)) {
                return element;
            }

        }
        return null;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getDateOpened()
     */
    protected static final DateFormat DATEOPENEDFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getDateOpened()
     */
    protected Date dateOpened;
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getDateOpened()
     */
    public Date getDateOpened() {
        if (dateOpened == null) {
            String s = jwsdpPeer.getInvoiceOpened().getTsDate();
            try {
                //"2001-09-18 00:00:00 +0200"
                dateOpened = DATEOPENEDFORMAT.parse(s);
            } catch (Exception e) {
                IllegalStateException ex = new IllegalStateException(
                        "unparsable date '"
                        + s
                        + "' in invoice!");
                ex.initCause(e);
                throw ex;
            }

        }
        return dateOpened;
    }

    /**
     * @see #getDateOpenedFormatet()
     * @see #getDatePostedFormatet()
     */
    private DateFormat dateFormat = null;

    /**
     * @see #getDateOpenedFormatet()
     * @see #getDatePostedFormatet()
     * @return the Dateformat to use.
     */
    protected DateFormat getDateFormat() {
     if (dateFormat == null) {
         dateFormat = DateFormat.getDateInstance();
     }

     return dateFormat;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getDateOpenedFormatet()
     */
    public String getDateOpenedFormatet() {
     return getDateFormat().format(getDateOpened());
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getDatePostedFormatet()
     */
    public String getDatePostedFormatet() {
        return getDateFormat().format(getDatePosted());
    }


    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getDatePosted()
     */
    private static final DateFormat DATEPOSTEDFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getDatePosted()
     */
    protected Date datePosted;
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getDatePosted()
     */
    public Date getDatePosted() {
        if (datePosted == null) {
            String s = jwsdpPeer.getInvoiceOpened().getTsDate();
            try {
                //"2001-09-18 00:00:00 +0200"
                datePosted = DATEPOSTEDFORMAT.parse(s);
            } catch (Exception e) {
                IllegalStateException ex = new IllegalStateException(
                        "unparsable date '"
                       + s
                       + "' in invoice!");
                ex.initCause(e);
                throw ex;
            }

        }
        return datePosted;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getInvoiceNumber()
     */
    public String getInvoiceNumber() {
        return jwsdpPeer.getInvoiceBillingId();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#addEntry(biz.wolschon.fileformats.gnucash.GnucashInvoiceEntry)
     */
    public void addEntry(final GnucashInvoiceEntry entry) {
        if (!entries.contains(entry))
            entries.add(entry);
    }

    /**
     * sorts primarily on the date the transaction happened
     * and secondarily on the date it was entered.
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Object o) {
        if (!(o instanceof GnucashInvoice)) {
            return 0;
        }


        GnucashInvoice other = (GnucashInvoice) o;

        try {
            int compare = other.getDatePosted().compareTo(getDatePosted());
            if (compare != 0) {
                return compare;
            }

            return other.getDateOpened().compareTo(getDateOpened());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @return The JWSDP-Object we are wrapping
     */
    public GncV2Type.GncBookType.GncGncInvoiceType getJwsdpPeer() {
        return jwsdpPeer;
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[GnucashInvoiceImpl:");
        buffer.append(" id: ");
        buffer.append(getId());
        buffer.append(" invoice-number: ");
        buffer.append(getInvoiceNumber());
        buffer.append(" description: ");
        buffer.append(getDescription());
        buffer.append(" #splits: ");
        buffer.append(entries.size());
        buffer.append(" dateOpened: ");
        try {
            buffer.append(DateFormat.getDateTimeInstance().format(getDateOpened()));
        } catch (Exception e) {
            e.printStackTrace();
            buffer.append("ERROR '" + e.getMessage() + "'");

        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * The currencyFormat to use for default-formating.<br/>
     * Please access only using {@link #getCurrencyFormat()}.
     * @see #getCurrencyFormat()
     */
    private NumberFormat currencyFormat = null;

    /**
    *
    * @return the currency-format to use if no locale is given.
    */
   protected NumberFormat getCurrencyFormat() {
       if (currencyFormat == null) {
           currencyFormat = NumberFormat.getCurrencyInstance();
       }

       return currencyFormat;
   }


    /**
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getAmmountUnPayedFormatet()
     */
    public String getAmmountUnPayedFormatet() throws JAXBException {
        return this.getCurrencyFormat().format(this.getAmmountUnPayed());
    }
    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getAmmountWithTaxesFormatet()
     */
    public String getAmmountWithTaxesFormatet() {
        return this.getCurrencyFormat().format(this.getAmmountWithTaxes());
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoice#getAmmountWithoutTaxesFormatet()
     */
    public String getAmmountWithoutTaxesFormatet() {
        return this.getCurrencyFormat().format(this.getAmmountWithoutTaxes());
    }
}
