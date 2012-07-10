/**
 * GnucashTransactionSplitImpl.java
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

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashInvoice;
import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncTransactionType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.ObjectFactory;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * created: 13.05.2005 <br/>
 * Implementation of GnucashTransactionSplit that uses JWSDSP.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 *
 */
public class GnucashTransactionSplitImpl extends GnucashObjectImpl implements GnucashTransactionSplit {



    /**
     * the JWSDP-object we are facading.
     */
    private GncTransactionType.TrnSplitsType.TrnSplitType jwsdpPeer;

    /**
     * the transaction this split belongs to.
     */
    private final GnucashTransaction myTransaction;


    /**
     * @param peer the JWSDP-object we are facading.
     * @see #jwsdpPeer
     * @param transaction the transaction this split belongs to
     * @throws JAXBException on issues with the XML-backend
     * @see #myTransaction
     */
    public GnucashTransactionSplitImpl(
            final GncTransactionType.TrnSplitsType.TrnSplitType peer,
            final GnucashTransaction transaction) throws JAXBException {
        super((peer.getSplitSlots() == null) ? new ObjectFactory().createSlotsType() : peer.getSplitSlots(), transaction.getGnucashFile());
        jwsdpPeer = peer;
        myTransaction = transaction;

        GnucashAccount account = getAccount();
        if (account == null) {
            System.err.println(
                    "No such Account id='"
                   + getAccountID()
                   + "' for Transactions-Split with id '"
                   + getId()
                   + "' descibed '"
                   + getDescription()
                   + "' in transaction with id '"
                   + getTransaction().getId()
                   + "' described '"
                   + getTransaction().getDescription()
                   + "'");
        } else {
            account.addTransactionSplit(this);
        }


        String lot = getLotID();
        if (lot != null) {
            for (GnucashInvoice invoice : getTransaction().getGnucashFile().getInvoices()) {
            	String lotID = invoice.getLotID();
                if (lotID != null && lotID.equals(lot)) {
                    // if action=="Rechnung" this is a split of the post-transaction,
                    // not a paying transaction
                    // if it's "Zahlung" it's a payment
                    if (getSplitAction().equals("Zahlung")) {
                        invoice.addPayingTransaction(this);
                    }
                }

            }
        }

    }

    /**
     *
     * @return the lot-id that identifies this transaction to belong to
     *         an invoice with that lot-id.
     */
    public String getLotID() {
        if (getJwsdpPeer().getSplitLot() == null) {
            return null;
        }

        return getJwsdpPeer().getSplitLot().getValue();

    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getSplitAction()
     */
    public String getSplitAction() {
        if (getJwsdpPeer().getSplitAction() == null) {
            return "";
        }

        return getJwsdpPeer().getSplitAction();
    }





    /**
     *
     * @return the JWSDP-object we are facading.
     */
    public GncTransactionType.TrnSplitsType.TrnSplitType getJwsdpPeer() {
        return jwsdpPeer;
    }

    /**
     *
     * @param newPeer the JWSDP-object we are facading.
     */
    protected void setJwsdpPeer(
            final GncTransactionType.TrnSplitsType.TrnSplitType newPeer) {
        if (newPeer == null) {
            throw new IllegalArgumentException(
            "null not allowed for field this.jwsdpPeer");
        }

        jwsdpPeer = newPeer;
    }
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getId()
     */
    public String getId() {
        return jwsdpPeer.getSplitId().getValue();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccountID()
     */
    public String getAccountID() {
        assert jwsdpPeer.getSplitAccount().getType().equals("guid");
        String id = jwsdpPeer.getSplitAccount().getValue();
        assert id != null;
        return id;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccount()
     */
    public GnucashAccount getAccount() {
        return myTransaction.getGnucashFile().getAccountByID(getAccountID());
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getTransaction()
     */
    public GnucashTransaction getTransaction() {
        return myTransaction;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValue()
     */
    public FixedPointNumber getValue() {
        return new FixedPointNumber(jwsdpPeer.getSplitValue());
    }


    /**
     * @return The currencyFormat for the quantity to use when no locale is given.
     */
    protected NumberFormat getQuantityCurrencyFormat() {

        return ((GnucashAccountImpl) getAccount()).getCurrencyFormat();
    }
    /**
     * @return the currency-format of the transaction
     */
    public NumberFormat getValueCurrencyFormat() {

        return ((GnucashTransactionImpl) getTransaction()).getCurrencyFormat();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValueFormatet()
     */
    public String getValueFormatet() {
        return getValueCurrencyFormat().format(getValue());
    }
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValueFormatet(java.util.Locale)
     */
    public String getValueFormatet(final Locale locale) {

        NumberFormat cf = NumberFormat.getInstance(locale);
        if (getTransaction().getCurrencyNameSpace().equals(GnucashAccount.CURRENCYNAMESPACE_CURRENCY)) {
            cf.setCurrency(Currency.getInstance(getTransaction().getCurrencyID()));
        } else {
            cf = NumberFormat.getNumberInstance(locale);
        }

        return cf.format(getValue());
    }
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValueFormatetForHTML()
     */
    public String getValueFormatetForHTML() {
        return getValueFormatet().replaceFirst("¤", "&euro;");
    }
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValueFormatetForHTML(java.util.Locale)
     */
    public String getValueFormatetForHTML(final Locale locale) {
        return getValueFormatet(locale).replaceFirst("¤", "&euro;");
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccountBalance()
     */
    public FixedPointNumber getAccountBalance() {
     return getAccount().getBalance(this);
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccountBalanceFormatet()
     */
    public String getAccountBalanceFormatet() {
        return ((GnucashAccountImpl) getAccount()).getCurrencyFormat()
             .format(getAccountBalance());
    }
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccountBalanceFormatet(java.util.Locale)
     */
    public String getAccountBalanceFormatet(final Locale locale) {
        return getAccount().getBalanceFormated(locale);
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getQuantity()
     */
    public FixedPointNumber getQuantity() {
        return new FixedPointNumber(jwsdpPeer.getSplitQuantity());
    }

    /**
     * The value is in the currency of the account!
     * @return
     */
    public String getQuantityFormatet() {
        return getQuantityCurrencyFormat().format(getQuantity());
    }
    /**
     * The value is in the currency of the account!
     * @param locale the locale to format to
     * @return the formated number
     */
    public String getQuantityFormatet(final Locale locale) {
        if (getTransaction().getCurrencyNameSpace().equals(GnucashAccount.CURRENCYNAMESPACE_CURRENCY)) {
            return NumberFormat.getNumberInstance(locale).format(getQuantity());
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setCurrency(Currency.getInstance(getAccount().getCurrencyID()));
        return nf.format(getQuantity());
    }
    /**
     * The value is in the currency of the account!
     * @return
     */
    public String getQuantityFormatetForHTML() {
        return getQuantityFormatet().replaceFirst("¤", "&euro;");
    }
    /**
     * The value is in the currency of the account!
     * @param locale
     * @return
     */
    public String getQuantityFormatetForHTML(final Locale locale) {
        return getQuantityFormatet(locale).replaceFirst("¤", "&euro;");
    }



    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getDescription()
     */
    public String getDescription() {
        if (jwsdpPeer.getSplitMemo() == null) {
            return "";
        }
        return jwsdpPeer.getSplitMemo();
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[GnucashTransactionSplitImpl:");
        buffer.append(" id: ");
        buffer.append(getId());
        buffer.append(" transaction-id: ");
        buffer.append(getTransaction().getId());
        buffer.append(" accountID: ");
        buffer.append(getAccountID());
        buffer.append(" account: ");
        GnucashAccount account = getAccount();
        buffer.append(account == null ?  "null" : account.getName());
        buffer.append(" description: ");
        buffer.append(getDescription());
        buffer.append(" transaction-description: ");
        buffer.append(getTransaction().getDescription());
        buffer.append(" value X quantity: ");
        buffer.append(getValue()).append(" X ").append(getQuantity());
        buffer.append("]");
        return buffer.toString();
    }

    /**
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Object o) {
        try {
            GnucashTransactionSplit otherSplit = (GnucashTransactionSplit) o;
            GnucashTransaction otherTrans = otherSplit.getTransaction();
            int c = otherTrans.compareTo(getTransaction());
            if (c != 0) {
                return c;
            }


            c = otherSplit.getId().compareTo(getId());
            if (c != 0) {
                return c;
            }


            if (o != this) {
                 System.err.println("doublicate transaction-split-id!! "
                      + otherSplit.getId()
                      + "[" + otherSplit.getClass().getName() + "] and "
                      + getId() + "[" + getClass().getName() + "]\n"
                      + "split0=" + otherSplit.toString() + "\n"
                      + "split1=" + toString() + "\n");
                 IllegalStateException x = new IllegalStateException("DEBUG");
                 x.printStackTrace();

            }


            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
