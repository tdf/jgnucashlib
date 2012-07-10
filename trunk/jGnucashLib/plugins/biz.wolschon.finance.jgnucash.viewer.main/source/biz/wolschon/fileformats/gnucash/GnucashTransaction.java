/**
 * GnucashTransaction.java
 * License: GPLv3 or later
 * Created on 05.05.2005
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 *
 * -----------------------------------------------------------
 * major Changes:
 *  05.05.2005 - initial version
 * ...
 *
 */
package biz.wolschon.fileformats.gnucash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.Slot;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * created: 05.05.2005
 * TODO write a comment what this type does here
 *
 *
 * It is comparable and sorts primarily on the date the transaction happened
 * and secondarily on the date it was entered.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public interface GnucashTransaction extends Comparable<GnucashTransaction> {

    /**
     *
     * @return the unique-id to identify this object with across name- and hirarchy-changes
     */
    String getId();

    /**
     * @return the user-defined description for this object (may contain multiple lines and non-ascii-characters)
     */
    String getDescription();
    
    /**
     * 
     * @return the transaction-number.
     */
    String getTransactionNumber();

    /**
     * The gnucash-file is the top-level class to contain everything.
     * @return the file we are associated with
     */
    GnucashFile getGnucashFile();

    /**
     * Do not modify the returned collection!
     * @return all splits of this transaction.
     * @throws JAXBException on issues with the XML-backend
     */
    List<? extends GnucashTransactionSplit> getSplits() throws JAXBException;

    /**
     * Get a split of this transaction it's id.
     * @param id the id to look for
     * @return null if not found
     * @throws JAXBException on issues with the XML-backend
     */
    GnucashTransactionSplit getSplitByID(String id) throws JAXBException;

    /**
     *
     * @return the first split of this transaction or null.
     * @throws JAXBException on issues with the XML-backend
     */
    GnucashTransactionSplit getFirstSplit() throws JAXBException;

    /**
     * @return the second split of this transaction or null.
     * @throws JAXBException on issues with the XML-backend
     */
    GnucashTransactionSplit getSecondSplit() throws JAXBException;

    /**
     *
     * @return the number of splits in this transaction.
     * @throws JAXBException on issues with the XML-backend
     */
    int getSplitsCount() throws JAXBException;;

    /**
     *
     * @return the date the transaction was entered
     *         into the system
     */
    Date getDateEntered();

    /**
     *
     * @return the date the transaction happened
     */
    Date getDatePosted();

    /**
     *
     * @return date the transaction happened
     */
    String getDatePostedFormatet();

    /**
     *
     * @return true if the sum of all splits adds up to zero.
     * @throws JAXBException on issues with the XML-backend
     */
    boolean isBalanced() throws JAXBException;

    /**
     * @return "ISO4217" for a currency "FUND" or a fond,...
     * @see {@link GnucashAccount#CURRENCYNAMESPACE_CURRENCY}
     * @see {@link GnucashAccount#CURRENCYNAMESPACE_FUND}
     */
    String getCurrencyNameSpace();

    /**
     * The name of the currency in the given namespace
     * e.g. "EUR" for euro in namespace "ISO4217"= {@link GnucashAccount#CURRENCYNAMESPACE_CURRENCY}
     * @see {@link #getCurrencyNameSpace()}
     */
    String getCurrencyID();


    /**
     * The result is in the currency of the transaction.<br/>
     * if the transaction is unbalanced, get sum of all split-values.
     * @return the sum of all splits
     * @throws JAXBException on issues with the XML-backend
     * @see #isBalanced()
     */
    FixedPointNumber getBalance() throws JAXBException;
    /**
     * The result is in the currency of the transaction.
     * @see GnucashTransaction#getBalance()
     * @throws JAXBException on issues with the XML-backend
     */
    String getBalanceFormatet() throws JAXBException;
    /**
     * The result is in the currency of the transaction.
     * @see GnucashTransaction#getBalance()
     * @throws JAXBException on issues with the XML-backend
     */
    String getBalanceFormatet(Locale loc) throws JAXBException;

    /**
     * The result is in the currency of the transaction.<br/>
     * if the transaction is unbalanced, get the missing split-value to balance it.
     * @return the sum of all splits
     * @see #isBalanced()
     * @throws JAXBException on issues with the XML-backend
     */
    FixedPointNumber getNegatedBalance() throws JAXBException;
    /**
     * The result is in the currency of the transaction.
     * @see GnucashTransaction#getNegatedBalance()
     * @throws JAXBException on issues with the XML-backend
     */
    String getNegatedBalanceFormatet() throws JAXBException;
    /**
     * The result is in the currency of the transaction.
     * @see GnucashTransaction#getNegatedBalance()
     * @throws JAXBException on issues with the XML-backend
     */
    String getNegatedBalanceFormatet(Locale loc) throws JAXBException;

    /**
     * @return all keys that can be used with ${@link #getUserDefinedAttribute(String)}}.
     */
    Collection<String> getUserDefinedAttributeKeys();

    /**
     * @param name the name of the user-defined attribute
     * @return the value or null if not set
     */
    String getUserDefinedAttribute(final String name);
}
