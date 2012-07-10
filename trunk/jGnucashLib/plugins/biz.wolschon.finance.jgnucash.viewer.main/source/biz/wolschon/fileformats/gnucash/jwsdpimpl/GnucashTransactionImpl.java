/**
 * GnucashTransactionImpl.java
 * License: GPLv3 or later
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashInvoice;
import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncTransactionType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.ObjectFactory;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType;
import biz.wolschon.numbers.FixedPointNumber;

/**
 *
 * created: 13.05.2005 <br/>
 * Implementation of GnucashTransaction that uses JWSDP.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class GnucashTransactionImpl extends GnucashObjectImpl implements GnucashTransaction {


    /**
     * the JWSDP-object we are facading.
     */
    private final GncTransactionType jwsdpPeer;

    /**
     * The file we belong to.
     */
    private final GnucashFile file;

    /**
     * Create a new Transaction, facading a JWSDP-transaction.
     * @param peer the JWSDP-object we are facading.
     * @see #jwsdpPeer
     * @param gncFile the file to register under
     */
    public GnucashTransactionImpl(final GncTransactionType peer,
                                  final GnucashFile gncFile) throws JAXBException {
        super((peer.getTrnSlots() == null) ? new ObjectFactory().createSlotsType() : peer.getTrnSlots(), gncFile);
        if (peer.getTrnSlots() == null) {
            peer.setTrnSlots(getSlots());
        }

        if (peer == null) {
            throw new IllegalArgumentException("null jwsdpPeer given");
        }

        if (gncFile == null) {
            throw new IllegalArgumentException("null file given");
        }


        jwsdpPeer = peer;
        file = gncFile;

        for (GnucashInvoice invoice : getInvoices()) {
            invoice.addTransaction(this);
        }

    }

    /**
     *
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#isBalanced()
     */
    public boolean isBalanced() throws JAXBException {

        return getBalance().equals(new FixedPointNumber());

    }


    /**
     * @return "ISO4217" for a currency "FUND" or a fond,...
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getCurrencyNameSpace()
     */
    public String getCurrencyNameSpace() {
        return jwsdpPeer.getTrnCurrency().getCmdtySpace();
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getCurrencyID()
     */
    public String getCurrencyID() {
        return jwsdpPeer.getTrnCurrency().getCmdtyId();
    }



    /**
     * The result is in the currency of the transaction.
     * @throws JAXBException on issues with the XML-backend
     * @return the balance of the sum of all splits
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getBalance()
     */
    public FixedPointNumber getBalance() throws JAXBException {

        FixedPointNumber fp = new FixedPointNumber();

        for (GnucashTransactionSplit split : getSplits()) {
            fp.add(split.getValue());
        }

        return fp;
    }

    /**
     *
     * The result is in the currency of the transaction.
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getBalanceFormatet()
     */
    public String getBalanceFormatet() throws JAXBException {
        return getCurrencyFormat().format(getBalance());
    }
    /**
     *
     * The result is in the currency of the transaction.
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getBalanceFormatet(java.util.Locale)
     */
    public String getBalanceFormatet(final Locale loc) throws JAXBException {

        NumberFormat cf = NumberFormat.getInstance(loc);
        if (getCurrencyNameSpace().equals(GnucashAccount.CURRENCYNAMESPACE_CURRENCY)) {
            cf.setCurrency(Currency.getInstance(getCurrencyID()));
        } else {
            cf.setCurrency(null);
        }

        return cf.format(getBalance());
    }

    /**
     *
     * The result is in the currency of the transaction.
     * @throws JAXBException if we have issues with the XML-backend
     * @throws NumberFormatException if the input is not valid
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getNegatedBalance()
     */
    public FixedPointNumber getNegatedBalance() throws NumberFormatException, JAXBException {
        return getBalance().multiply(new FixedPointNumber("-100/100"));
    }
    /**
     *
     * The result is in the currency of the transaction.
     * @throws JAXBException if we have issues with the XML-backend
     * @throws NumberFormatException
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getNegatedBalanceFormatet()
     */
    public String getNegatedBalanceFormatet() throws NumberFormatException, JAXBException {
        return getCurrencyFormat().format(getNegatedBalance());
    }
    /**
     * The result is in the currency of the transaction.
     * @throws JAXBException if we have issues with the XML-backend
     * @throws NumberFormatException
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getNegatedBalanceFormatet(java.util.Locale)
     */
    public String getNegatedBalanceFormatet(final Locale loc) throws NumberFormatException, JAXBException {
        NumberFormat cf = NumberFormat.getInstance(loc);
        if (getCurrencyNameSpace().equals(GnucashAccount.CURRENCYNAMESPACE_CURRENCY)) {
            cf.setCurrency(Currency.getInstance(getCurrencyID()));
        } else {
            cf.setCurrency(null);
        }


        return cf.format(getNegatedBalance());
    }



    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getId()
     */
    public String getId() {
        return jwsdpPeer.getTrnId().getValue();
    }

    /**
     *
     * @return the invoices this transaction belongs to (not payments but the transaction belonging to handing out the invoice)
     */
    public Collection<GnucashInvoice> getInvoices() {
        Collection<String> invoiceIDs = getInvoiceIDs();
        List<GnucashInvoice> retval = new ArrayList<GnucashInvoice>(invoiceIDs.size());

        for (String invoiceID : invoiceIDs) {

            GnucashInvoice invoice = file.getInvoiceByID(invoiceID);
            if (invoice == null) {
                System.err.println(
                        "No invoice with id='"
                      + invoiceID
                      + "' for transaction '"
                      + getId()
                      + "' described '"
                      + getDescription()
                      + "'");
            } else {
                retval.add(invoice);
            }

        }

        return retval;
    }

    /**
     *
     * @return the invoices this transaction belongs to (not payments but the transaction belonging to handing out the invoice)
     */

    @SuppressWarnings("unchecked")
    public Collection<String> getInvoiceIDs() {

        List<String> retval = new LinkedList<String>();

        SlotsType slots = jwsdpPeer.getTrnSlots();
        if (slots == null) {
            return retval;
        }


        for (SlotType slot : (List<SlotType>) slots.getSlot()) {
            if (!slot.getSlotKey().equals("gncInvoice")) {
                continue;
            }


            biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotValueType slotvalue = slot.getSlotValue();

            SlotType subslot = (SlotType) slotvalue.getContent().get(0);
            if (!subslot.getSlotKey().equals("invoice-guid")) {
                continue;
            }

            if (!subslot.getSlotValue().getType().equals("guid")) {
                continue;
            }

            retval.add((String) subslot.getSlotValue().getContent().get(0));

        }

        return retval;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDescription()
     */
    public String getDescription() {
        return jwsdpPeer.getTrnDescription();
    }


    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getGnucashFile()
     */
    @Override
    public GnucashFile getGnucashFile() {
        return file;
    }


    /**
     * @see #getSplits()
     */
    protected List<GnucashTransactionSplit> mySplits = null;


    /**
     * @param impl the split to add to mySplits
     * @throws JAXBException if we have issues with the XML-backend
     */
    @SuppressWarnings("unchecked")
    protected void addSplit(final GnucashTransactionSplitImpl impl) throws JAXBException {
        if (!jwsdpPeer.getTrnSplits().getTrnSplit().contains(impl.getJwsdpPeer())) {
        	jwsdpPeer.getTrnSplits().getTrnSplit().add(impl.getJwsdpPeer());
        }

        Collection<GnucashTransactionSplit> splits = getSplits();
        if (!splits.contains(impl)) {
            splits.add(impl);
        }

    }

    /**
     *
     *
     * @throws JAXBException on issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getSplitsCount()
     */
    public int getSplitsCount() throws JAXBException {
        return getSplits().size();
    }

    /**
     * @throws JAXBException on issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getSplitByID(java.lang.String)
     */
    public GnucashTransactionSplit getSplitByID(final String id) throws JAXBException {
        for (GnucashTransactionSplit split : getSplits()) {
            if (split.getId().equals(id)) {
                return split;
            }

        }
        return null;
    }

    /**
     *
     *
     * @throws JAXBException on issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getFirstSplit()
     */
    public GnucashTransactionSplit getFirstSplit() throws JAXBException {
        return getSplits().iterator().next();
    }

    /**
     *
     *
     * @throws JAXBException on issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getSecondSplit()
     */
    public GnucashTransactionSplit getSecondSplit() throws JAXBException {
        Iterator<GnucashTransactionSplit> iter = getSplits().iterator();
        iter.next();
        return iter.next();
    }


    /**
     * @throws JAXBException on issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getSplits()
     */
    @SuppressWarnings("unchecked")
    public List<GnucashTransactionSplit> getSplits() throws JAXBException {
    	if (mySplits == null) {
    		List<GncTransactionType.TrnSplitsType.TrnSplitType > jwsdpSplits = jwsdpPeer.getTrnSplits().getTrnSplit();

    		mySplits = new ArrayList<GnucashTransactionSplit>(jwsdpSplits.size());
    		for (GncTransactionType.TrnSplitsType.TrnSplitType element :jwsdpSplits) {

    			mySplits.add(createSplit(element));
    		}
    	}
    	return mySplits;
    }


    /**
     * Create a new split for a split found in the jaxb-data.
     * @param element the jaxb-data
     * @return the new split-instance
     * @throws JAXBException
     */
    protected GnucashTransactionSplitImpl createSplit(final GncTransactionType.TrnSplitsType.TrnSplitType element) throws JAXBException {
        return new GnucashTransactionSplitImpl(element, this);
    }


    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDateEntered()
     */
    protected static final DateFormat DATEENTEREDFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDateEntered()
     */
    protected Date dateEntered;


    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDateEntered()
     */
    public Date getDateEntered() {
        if (dateEntered == null) {
            String s = jwsdpPeer.getTrnDateEntered().getTsDate();
            try {
                //"2001-09-18 00:00:00 +0200"
                dateEntered = DATEENTEREDFORMAT.parse(s);
            } catch (Exception e) {
                IllegalStateException ex =
                     new IllegalStateException(
                             "unparsable date '" + s + "' in transaction!");
                ex.initCause(e);
                throw ex;
            }
        }

        return dateEntered;
    }

    /**
     * format of the dataPosted-field in the xml(jwsdp)-file.
     */
    private static final DateFormat DATEPOSTEDFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ");

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDatePosted()
     */
    protected Date datePosted;

    /**
     * The Currency-Format to use if no locale is given.
     */
    protected NumberFormat currencyFormat;

    /**
     * The Currency-Format to use if no locale is given.
     * @return default currency-format with the transaction's
     *         currency set
     */
    protected NumberFormat getCurrencyFormat() {
        if (currencyFormat == null) {
            currencyFormat = NumberFormat.getCurrencyInstance();
            if (getCurrencyNameSpace().equals(GnucashAccount.CURRENCYNAMESPACE_CURRENCY)) {
                currencyFormat.setCurrency(Currency.getInstance(getCurrencyID()));
            } else {
                currencyFormat = NumberFormat.getInstance();
            }

        }
        return currencyFormat;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDatePostedFormatet()
     */
    public String getDatePostedFormatet() {
     return DateFormat.getDateInstance().format(getDatePosted());
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDatePosted()
     */
    public Date getDatePosted() {
        if (datePosted == null) {
            String s = jwsdpPeer.getTrnDatePosted().getTsDate();
            try {
                //"2001-09-18 00:00:00 +0200"
                datePosted = DATEPOSTEDFORMAT.parse(s);
            } catch (Exception e) {
                IllegalStateException ex =
                    new IllegalStateException(
                            "unparsable date '" + s + "' in transaction with id='"
                            + getId()
                            + "'!");
                ex.initCause(e);
                throw ex;
            }
        }

        return datePosted;
    }



    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[GnucashTransactionImpl:");
        buffer.append(" id: ");
        buffer.append(getId());
        buffer.append(" description: ");
        buffer.append(getDescription());
        buffer.append(" #splits: ");
        buffer.append(mySplits.size());
        buffer.append(" dateEntered: ");
        try {
            buffer.append(DateFormat.getDateTimeInstance().format(getDateEntered()));
        } catch (Exception e) {
            e.printStackTrace();
            buffer.append("ERROR '" + e.getMessage() + "'");

        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * sorts primarily on the date the transaction happened
     * and secondarily on the date it was entered.
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final GnucashTransaction o) {


        GnucashTransaction other = o;

        try {
            int compare = other.getDatePosted().compareTo(getDatePosted());
            if (compare != 0) {
                return compare;
            }

            return other.getDateEntered().compareTo(getDateEntered());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * @return the JWSDP-object we are facading.
     */
    public GncTransactionType getJwsdpPeer() {
        return jwsdpPeer;
    }

	public String getTransactionNumber() {
		return getJwsdpPeer().getTrnNum();
	}
}
