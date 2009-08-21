/**
 * GnucashDBSplit.javaTransactionMenuAction.java
 * created: 10.08.2009
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
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Currency;
import java.util.Locale;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * GnucashDBSplit<br/>
 * created: 10.08.2009 <br/>
 *<br/><br/>
 * <b>This class represents a transaction-split in a Gnucash 2.3.3 -database</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class GnucashDBSplit implements GnucashWritableTransactionSplit {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(GnucashDBSplit.class
            .getName());

    private final GnucashDatabase myGnucashFile;
    private final String myGUID;
    /**
     * @param aGuid
     * @param aTransactionID
     * @param aLotID
     * @param aMemo
     * @param aAction
     * @param aValue
     * @param aQuantity
     */
    protected GnucashDBSplit(final GnucashDatabase aGnucashFile,
                             final String aGuid,
                             final String aTransactionID,
                             final String aLotID,
                             final String anAccountID,
                             final String aMemo,
                             final String aAction,
                             final FixedPointNumber aValue,
                             final FixedPointNumber aQuantity) {
        super();
        LOG.info("GnucashDBSplit()");
        myGnucashFile = aGnucashFile;
        myGUID = aGuid;
        myTransactionID = aTransactionID;
        myLotID = aLotID;
        myAccountID = anAccountID;
        myMemo = aMemo;
        myAction = aAction;
        myValue = aValue;
        myQuantity = aQuantity;
    }

    private final String myTransactionID;
    private final String myLotID;
    private final String myAccountID;
    private final String myMemo;
    private final String myAction;
    private final FixedPointNumber myValue;
    private final FixedPointNumber myQuantity;
    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener aListener) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(String aPropertyName,
                                          PropertyChangeListener aListener) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#getTransaction()
     */
    @Override
    public GnucashWritableTransaction getTransaction() {
        LOG.info("getTransaction()");
        return myGnucashFile.getTransactionByID(myTransactionID);
    }

    /**
     * {@inheritDoc}
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#remove()
     */
    @Override
    public void remove() throws JAXBException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(String aPropertyName,
                                             PropertyChangeListener aListener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener aListener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setAccount(biz.wolschon.fileformats.gnucash.GnucashAccount)
     */
    @Override
    public void setAccount(GnucashAccount aAccount) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setAccountID(java.lang.String)
     */
    @Override
    public void setAccountID(String aAccountId) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String aDesc) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setQuantity(java.lang.String)
     */
    @Override
    public void setQuantity(String aN) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setQuantity(biz.wolschon.numbers.FixedPointNumber)
     */
    @Override
    public void setQuantity(FixedPointNumber aN) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setQuantityFormatetForHTML(java.lang.String)
     */
    @Override
    public void setQuantityFormatetForHTML(String aN) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setSplitAction(java.lang.String)
     */
    @Override
    public void setSplitAction(String aAction) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setValue(java.lang.String)
     */
    @Override
    public void setValue(String aN) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setValue(biz.wolschon.numbers.FixedPointNumber)
     */
    @Override
    public void setValue(FixedPointNumber aN) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit#setValueFormatetForHTML(java.lang.String)
     */
    @Override
    public void setValueFormatetForHTML(String aN) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccount()
     */
    @Override
    public GnucashAccount getAccount() {
        return myGnucashFile.getAccountByID(myAccountID);
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccountBalance()
     */
    @Override
    public FixedPointNumber getAccountBalance() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccountBalanceFormatet()
     */
    @Override
    public String getAccountBalanceFormatet() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccountBalanceFormatet(java.util.Locale)
     */
    @Override
    public String getAccountBalanceFormatet(Locale aLocale) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getAccountID()
     */
    @Override
    public String getAccountID() {
        return myAccountID;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getDescription()
     */
    @Override
    public String getDescription() {
        return myMemo;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getId()
     */
    @Override
    public String getId() {
        return myGUID;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getQuantity()
     */
    @Override
    public FixedPointNumber getQuantity() {
        return myQuantity;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getQuantityFormatet()
     */
    @Override
    public String getQuantityFormatet() {
        return getQuantityCurrencyFormat().format(getQuantity());
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getQuantityFormatet(java.util.Locale)
     */
    @Override
    public String getQuantityFormatet(final Locale aLocale) {
        if (getTransaction().getCurrencyNameSpace().equals(GnucashAccount.CURRENCYNAMESPACE_CURRENCY)) {
            return NumberFormat.getNumberInstance(aLocale).format(getQuantity());
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(aLocale);
        nf.setCurrency(Currency.getInstance(getAccount().getCurrencyID()));
        return nf.format(getQuantity());
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getQuantityFormatetForHTML()
     */
    @Override
    public String getQuantityFormatetForHTML() {
        return getQuantityFormatet().replaceFirst("¤", "&euro;");
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getQuantityFormatetForHTML(java.util.Locale)
     */
    @Override
    public String getQuantityFormatetForHTML(final Locale aLocale) {
        return getQuantityFormatet(aLocale).replaceFirst("¤", "&euro;");
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getSplitAction()
     */
    @Override
    public String getSplitAction() {
        return myAction;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getUserDefinedAttribute(java.lang.String)
     */
    @Override
    public String getUserDefinedAttribute(String aName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getUserDefinedAttributeKeys()
     */
    @Override
    public Collection<String> getUserDefinedAttributeKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValue()
     */
    @Override
    public FixedPointNumber getValue() {
        return myValue;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValueFormatet()
     */
    @Override
    public String getValueFormatet() {
        return getValueCurrencyFormat().format(getValue());
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValueFormatet(java.util.Locale)
     */
    @Override
    public String getValueFormatet(final Locale aLocale) {

        NumberFormat cf = NumberFormat.getInstance(aLocale);
        if (getTransaction().getCurrencyNameSpace().equals(GnucashAccount.CURRENCYNAMESPACE_CURRENCY)) {
            cf.setCurrency(Currency.getInstance(getTransaction().getCurrencyID()));
        } else {
            cf = NumberFormat.getNumberInstance(aLocale);
        }

        return cf.format(getValue());
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValueFormatetForHTML()
     */
    @Override
    public String getValueFormatetForHTML() {
        return getValueFormatet().replaceFirst("¤", "&euro;");
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashTransactionSplit#getValueFormatetForHTML(java.util.Locale)
     */
    @Override
    public String getValueFormatetForHTML(final Locale aLocale) {
        return getValueFormatet(aLocale).replaceFirst("¤", "&euro;");

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


    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableObject#getWritableGnucashFile()
     */
    @Override
    public GnucashWritableFile getWritableGnucashFile() {
        return myGnucashFile;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableObject#setUserDefinedAttribute(java.lang.String, java.lang.String)
     */
    @Override
    public void setUserDefinedAttribute(String aName, String aValue)
                                                                    throws JAXBException {
        // TODO Auto-generated method stub

    }

    /**
     * @return The currencyFormat for the quantity to use when no locale is given.
     */
    protected NumberFormat getQuantityCurrencyFormat() {

        return NumberFormat.getCurrencyInstance();//TODO: return ((GnucashAccountImpl) getAccount()).getCurrencyFormat();
    }
    /**
     * @return the currency-format of the transaction
     */
    public NumberFormat getValueCurrencyFormat() {

        return NumberFormat.getCurrencyInstance();//TODO: return ((GnucashTransactionImpl) getTransaction()).getCurrencyFormat();
    }

}
