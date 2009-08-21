/**
 * GnucashDBTransaction.javaTransactionMenuAction.java
 * created: 11.08.2009
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jgnucashLib-GPL by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 *  jgnucashLib-GPL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jgnucashLib-GPL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jgnucashLib-V1.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************
 * Editing this file:
 *  -For consistent code-quality this file should be checked with the
 *   checkstyle-ruleset enclosed in this project.
 *  -After the design of this file has settled it should get it's own
 *   JUnit-Test that shall be executed regularly. It is best to write
 *   the test-case BEFORE writing this class and to run it on every build
 *   as a regression-test.
 */

package biz.wolschon.finance.jgnucash.mysql.impl;

import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * GnucashDBTransaction<br/>
 * created: 11.08.2009 <br/>
 *<br/><br/>
 * <b>This class represents a transaction in a Gnucash 2.3.3 -database</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class GnucashDBTransaction implements GnucashWritableTransaction {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(GnucashDBTransaction.class
            .getName());

    private final GnucashDatabase myGnucashFile;
    /**
     * @param aGnucashFile
     * @param aGuid
     * @param aCurrencyGUID
     * @param aTransactionNumber
     * @param aDescription
     * @param aDatePosted
     * @param aDateEntered
     */
    protected GnucashDBTransaction(GnucashDatabase aGnucashFile, String aGuid,
            String aCurrencyGUID, String aTransactionNumber,
            String aDescription, Date aDatePosted, Date aDateEntered) {
        super();
        myGnucashFile = aGnucashFile;
        myGUID = aGuid;
        myCurrencyGUID = aCurrencyGUID;
        myTransactionNumber = aTransactionNumber;
        myDescription = aDescription;
        myDatePosted = aDatePosted;
        myDateEntered = aDateEntered;
    }

    private final String myGUID;
    private final String myCurrencyGUID;
    private final String myTransactionNumber;
    private final String myDescription;
    private final Date myDatePosted;
    private final Date myDateEntered;

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener aListener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(String aPropertyName,
                                          PropertyChangeListener aListener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#createWritingSplit(biz.wolschon.fileformats.gnucash.GnucashAccount)
     */
    @Override
    public GnucashWritableTransactionSplit createWritingSplit(GnucashAccount aAccount)
                                                                                      throws JAXBException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#createWritingSplit(biz.wolschon.fileformats.gnucash.GnucashAccount, java.lang.String)
     */
    @Override
    public GnucashWritableTransactionSplit createWritingSplit(GnucashAccount aAccount,
                                                              String aSplitID)
                                                                              throws JAXBException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getFirstSplit()
     */
    @Override
    public GnucashWritableTransactionSplit getFirstSplit() throws JAXBException {
        return getWritingFirstSplit();
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getSecondSplit()
     */
    @Override
    public GnucashWritableTransactionSplit getSecondSplit()
                                                           throws JAXBException {
        return getWritingSecondSplit();
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingFile()
     */
    @Override
    public GnucashWritableFile getWritingFile() {
        return myGnucashFile;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingFirstSplit()
     */
    @Override
    public GnucashWritableTransactionSplit getWritingFirstSplit()
                                                                 throws JAXBException {
        return (GnucashWritableTransactionSplit)getSplits().iterator().next();
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingSecondSplit()
     */
    @Override
    public GnucashWritableTransactionSplit getWritingSecondSplit()
                                                                  throws JAXBException {
        Iterator<? extends GnucashWritableTransactionSplit> iter = getWritingSplits().iterator();
        iter.next();
        return iter.next();
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingSplitByID(java.lang.String)
     */
    @Override
    public GnucashWritableTransactionSplit getWritingSplitByID(String aId)
                                                                          throws JAXBException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingSplits()
     */
    @Override
    public Collection<? extends GnucashWritableTransactionSplit> getWritingSplits() {
    LOG.info("getWritingSplits()");
    String sql = "select * from " + SplitRowMapper.DBTABLE + " where " + SplitRowMapper.ROWTRANSACTIONGUID + " = ?";
    return myGnucashFile.getJDBCTemplate().query(sql, new SplitRowMapper(myGnucashFile), getId());

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#remove(biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit)
     */
    @Override
    public void remove(GnucashWritableTransactionSplit aImpl)
                                                             throws JAXBException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#remove()
     */
    @Override
    public void remove() throws JAXBException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(String aPropertyName,
                                             PropertyChangeListener aListener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener aListener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setCurrencyID(java.lang.String)
     */
    @Override
    public void setCurrencyID(String aId) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setCurrencyNameSpace(java.lang.String)
     */
    @Override
    public void setCurrencyNameSpace(String aId) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setDateEntered(java.util.Date)
     */
    @Override
    public void setDateEntered(Date aDateEntered) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setDatePosted(java.util.Date)
     */
    @Override
    public void setDatePosted(Date aDatePosted) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setDatePostedFormatet(java.lang.String)
     */
    @Override
    public void setDatePostedFormatet(String aDatePosted) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String aDesc) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setTransactionNumber(java.lang.String)
     */
    @Override
    public void setTransactionNumber(String aString) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setUserDefinedAttribute(java.lang.String, java.lang.String)
     */
    @Override
    public void setUserDefinedAttribute(String aName, String aValue)
                                                                    throws JAXBException {
        // TODO Auto-generated method stub

    }

    /**
     * The result is in the currency of the transaction.
     * @throws JAXBException on issues with the XML-backend
     * @return the balance of the sum of all splits
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getBalance()
     */
    @Override
    public FixedPointNumber getBalance() throws JAXBException {

        FixedPointNumber fp = new FixedPointNumber();

        for (GnucashTransactionSplit split : getSplits()) {
            fp.add(split.getValue());
        }

        return fp;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getBalanceFormatet()
     */
    @Override
    public String getBalanceFormatet() throws JAXBException {
        return getCurrencyFormat().format(getBalance());
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getBalanceFormatet(java.util.Locale)
     */
    @Override
    public String getBalanceFormatet(final Locale aLoc) throws JAXBException {

        NumberFormat cf = NumberFormat.getInstance(aLoc);
        if (getCurrencyNameSpace().equals(GnucashAccount.CURRENCYNAMESPACE_CURRENCY)) {
            cf.setCurrency(Currency.getInstance(getCurrencyID()));
        } else {
            cf.setCurrency(null);
        }

        return cf.format(getBalance());
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getCurrencyID()
     */
    @Override
    public String getCurrencyID() {
        return myGnucashFile.getCommodityByID(myCurrencyGUID).getMnemonic();
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getCurrencyNameSpace()
     */
    @Override
    public String getCurrencyNameSpace() {
        return myGnucashFile.getCommodityByID(myCurrencyGUID).getNamespace();
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDateEntered()
     */
    @Override
    public Date getDateEntered() {
        return myDateEntered;
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDatePosted()
     */
    @Override
    public Date getDatePosted() {
        return myDatePosted;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDatePostedFormatet()
     */
    @Override
    public String getDatePostedFormatet() {
        return DateFormat.getDateInstance().format(getDatePosted());
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getDescription()
     */
    @Override
    public String getDescription() {
        return myDescription;
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getGnucashFile()
     */
    @Override
    public GnucashFile getGnucashFile() {
        return getWritingFile();
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getId()
     */
    @Override
    public String getId() {
        return myGUID;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getNegatedBalance()
     */
    @Override
    public FixedPointNumber getNegatedBalance() throws JAXBException {
        return getBalance().multiply(new FixedPointNumber("-100/100"));
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getNegatedBalanceFormatet()
     */
    @Override
    public String getNegatedBalanceFormatet() throws JAXBException {
        return getCurrencyFormat().format(getNegatedBalance());
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getNegatedBalanceFormatet(java.util.Locale)
     */
    @Override
    public String getNegatedBalanceFormatet(final Locale aLoc) throws JAXBException {
        NumberFormat cf = NumberFormat.getInstance(aLoc);
        if (getCurrencyNameSpace().equals(GnucashAccount.CURRENCYNAMESPACE_CURRENCY)) {
            cf.setCurrency(Currency.getInstance(getCurrencyID()));
        } else {
            cf.setCurrency(null);
        }


        return cf.format(getNegatedBalance());
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getSplitByID(java.lang.String)
     */
    @Override
    public GnucashTransactionSplit getSplitByID(final String aId)
                                                           throws JAXBException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getSplits()
     */
    @Override
    public List<? extends GnucashTransactionSplit> getSplits()
                                                              throws JAXBException {
        return (List<? extends GnucashTransactionSplit>) getWritingSplits();
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getSplitsCount()
     */
    @Override
    public int getSplitsCount() throws JAXBException {
        return getWritingSplits().size();
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getTransactionNumber()
     */
    @Override
    public String getTransactionNumber() {
        return myTransactionNumber;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getUserDefinedAttribute(java.lang.String)
     */
    @Override
    public String getUserDefinedAttribute(String aName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#getUserDefinedAttributeKeys()
     */
    @Override
    public Collection<String> getUserDefinedAttributeKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransaction#isBalanced()
     */
    @Override
    public boolean isBalanced() throws JAXBException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final GnucashTransaction aO) {


        GnucashTransaction other = aO;

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

}
