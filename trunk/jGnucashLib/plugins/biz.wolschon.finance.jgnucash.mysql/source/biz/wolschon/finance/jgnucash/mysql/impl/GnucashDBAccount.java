/**
 * GnucashDBAccount.javaTransactionMenuAction.java
 * created: 06.08.2009
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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.baseclasses.SimpleAccount;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * GnucashDBAccount<br/>
 * created: 06.08.2009 <br/>
 *<br/><br/>
 * <b>This class represents an account in a Gnucash 2.3.3 -database</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class GnucashDBAccount extends SimpleAccount implements GnucashWritableAccount {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(GnucashDBAccount.class
            .getName());

//    /**
//     * Helper to support PropertyChangeListeners.
//     */
//    private final PropertyChangeSupport myPropertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * The {@link GnucashDatabase} we belong to.
     */
    private final GnucashDatabase myGnucashFile;

    private final String myGUID;
    /**
     * @param aGnucashFile
     * @param aGuid
     * @param aName
     * @param aAccountType
     * @param aAccountCode
     * @param aComodityID
     * @param aComoditySCU
     * @param aComodityNonStandardSCU
     */
    protected GnucashDBAccount(final GnucashDatabase aGnucashFile,
                               final String aGuid,
                               final String aParentGUID,
                               final String aName,
                               final String aAccountType,
                               final String aAccountCode,
                               final String aComodityID,
                               final int aComoditySCU,
                               final boolean aComodityNonStandardSCU) {
        super(aGnucashFile);
        myGnucashFile = aGnucashFile;
        myGUID = aGuid;
        myParentGUID = aParentGUID;
        myName = aName;
        myAccountType = aAccountType;
        myAccountCode = aAccountCode;
        myComodityID = aComodityID;
        myComoditySCU = aComoditySCU;
        myComodityNonStandardSCU = aComodityNonStandardSCU;
    }

    private final String myParentGUID;
    private final String myName;
    private final String myAccountType;
    private final String myAccountCode;
    private String myAccountDescription;
    private final String myComodityID;
    private final int myComoditySCU;
    private final boolean myComodityNonStandardSCU;
//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#addPropertyChangeListener(java.beans.PropertyChangeListener)
//     */
//    @Override
//    public void addPropertyChangeListener(final PropertyChangeListener aListener) {
//        myPropertyChangeSupport.addPropertyChangeListener(aListener);
//    }
//
//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
//     */
//    @Override
//    public void addPropertyChangeListener(final String aPropertyName,
//                                          final PropertyChangeListener aListener) {
//        myPropertyChangeSupport.addPropertyChangeListener(aPropertyName, aListener);
//    }
//
    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#getBalanceChange(java.util.Date, java.util.Date)
     */
    @Override
    public FixedPointNumber getBalanceChange(final Date aFrom, final Date aTo) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#getWritableGnucashFile()
     */
    @Override
    public GnucashWritableFile getWritableGnucashFile() {
        return myGnucashFile;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#remove()
     */
    @Override
    public void remove() {
        // TODO Auto-generated method stub

    }

//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
//     */
//    @Override
//    public void removePropertyChangeListener(String aPropertyName,
//                                             PropertyChangeListener aListener) {
//
//    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener aListener) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#setAccountCode(java.lang.String)
     */
    @Override
    public void setAccountCode(final String aCode) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#setCurrencyID(java.lang.String)
     */
    @Override
    public void setCurrencyID(final String aId) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#setCurrencyNameSpace(java.lang.String)
     */
    @Override
    public void setCurrencyNameSpace(final String aId) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(final String aDesc) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#setName(java.lang.String)
     */
    @Override
    public void setName(final String aName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#setParentAccount(biz.wolschon.fileformats.gnucash.GnucashAccount)
     */
    @Override
    public void setParentAccount(final GnucashAccount aNewparent)
                                                           throws JAXBException {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#setParentAccountId(java.lang.String)
     */
    @Override
    public void setParentAccountId(final String aNewparent) {
        //TODO
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#setType(java.lang.String)
     */
    @Override
    public void setType(final String aType) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableAccount#setUserDefinedAttribute(java.lang.String, java.lang.String)
     */
    @Override
    public void setUserDefinedAttribute(final String aName, final String aValue)
                                                                    throws JAXBException {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#addTransactionSplit(biz.wolschon.fileformats.gnucash.GnucashTransactionSplit)
     */
    @Override
    public void addTransactionSplit(GnucashTransactionSplit aSplit) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getAccountCode()
     */
    @Override
    public String getAccountCode() {
        return myAccountCode;
    }

//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalance()
//     */
//    @Override
//    public FixedPointNumber getBalance() {
//        return null;
//    }

//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalance(java.util.Date)
//     */
//    @Override
//    public FixedPointNumber getBalance(Date aDate) {
//        return null;
//    }

//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalance(java.util.Date, java.util.Collection)
//     */
//    @Override
//    public FixedPointNumber getBalance(final Date aDate,
//                                       final Collection<GnucashTransactionSplit> aAfter) {
//        return null;
//    }

//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalance(biz.wolschon.fileformats.gnucash.GnucashTransactionSplit)
//     */
//    @Override
//    public FixedPointNumber getBalance(GnucashTransactionSplit aLastIncludesSplit) {
//        return null;
//    }

//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalanceFormated()
//     */
//    @Override
//    public String getBalanceFormated() {
//        return null;
//    }

//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalanceFormated(java.util.Locale)
//     */
//    @Override
//    public String getBalanceFormated(Locale aLocale) {
//        return null;
//    }
//
//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalanceRecursive()
//     */
//    @Override
//    public FixedPointNumber getBalanceRecursive() {
//        return null;
//    }
//
//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalanceRecursive(java.util.Date, java.util.Currency)
//     */
//    @Override
//    public FixedPointNumber getBalanceRecursive(Date aDate, Currency aCurrency) {
//        return null;
//    }
//
//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalanceRecursive(java.util.Date)
//     */
//    @Override
//    public FixedPointNumber getBalanceRecursive(Date aDate) {
//        return null;
//    }
//
//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalanceRecursive(java.util.Date, java.lang.String, java.lang.String)
//     */
//    @Override
//    public FixedPointNumber getBalanceRecursive(Date aDate,
//                                                String aCurrencyNameSpace,
//                                                String aCurrencyName) {
//        return null;
//    }
//
//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalanceRecursiveFormated()
//     */
//    @Override
//    public String getBalanceRecursiveFormated() {
//        return null;
//    }
//
//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getBalanceRecursiveFormated(java.util.Date)
//     */
//    @Override
//    public String getBalanceRecursiveFormated(final Date aDate) {
//        return null;
//    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getChildren()
     */
    @Override
    public Collection<? extends GnucashAccount> getChildren() {
        String sql = "select * from accounts where parent_guid = ?";
        return myGnucashFile.getJDBCTemplate().query(sql, new AccountRowMapper(myGnucashFile), getId());
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getCurrencyID()
     */
    @Override
    public String getCurrencyID() {
        return myGnucashFile.getCommodityByID(myComodityID).getMnemonic();
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getCurrencyNameSpace()
     */
    @Override
    public String getCurrencyNameSpace() {
        return myGnucashFile.getCommodityByID(myComodityID).getNamespace();
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getDescription()
     */
    @Override
    public String getDescription() {
        return myAccountDescription;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getId()
     */
    @Override
    public String getId() {
        return myGUID;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getLastSplitBeforeRecursive(java.util.Date)
     */
    @Override
    public GnucashTransactionSplit getLastSplitBeforeRecursive(final Date aDate) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getName()
     */
    @Override
    public String getName() {
        return myName;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getParentAccount()
     */
    @Override
    public GnucashAccount getParentAccount() {
        String id = getParentAccountId();
        if (id == null || id.equals(getId())) {
            return null;
        }
        return getWritableGnucashFile().getAccountByID(id);
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getParentAccountId()
     */
    @Override
    public String getParentAccountId() {
        return myParentGUID;
    }

//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getQualifiedName()
//     */
//    @Override
//    public String getQualifiedName() {
//        // TODO Auto-generated method stub
//        return getName();
//    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getSubAccounts()
     */
    @Override
    public Collection<? extends GnucashAccount> getSubAccounts() {
        return getChildren();
    }

    /* (non-Javadoc)
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getTransactionSplitByID(java.lang.String)
     */
    @Override
    public GnucashTransactionSplit getTransactionSplitByID(final String aId) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getTransactionSplits()
     */
    @Override
    public List<? extends GnucashTransactionSplit> getTransactionSplits() {
        LOG.info("getTransactionSplits()");
        String sql = "select * from splits where account_guid = ?";
        return myGnucashFile.getJDBCTemplate().query(sql, new SplitRowMapper(myGnucashFile), getId());
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getTransactions()
     */
    @Override
    public List getTransactions() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getType()
     */
    @Override
    public String getType() {
        return myAccountType;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getUserDefinedAttribute(java.lang.String)
     */
    @Override
    public String getUserDefinedAttribute(final String aName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getUserDefinedAttributeKeys()
     */
    @Override
    public Collection<String> getUserDefinedAttributeKeys() {
        // TODO Auto-generated method stub
        return null;
    }

//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#hasTransactions()
//     */
//    @Override
//    public boolean hasTransactions() {
//        // TODO Auto-generated method stub
//        return false;
//    }
//
//    /**
//     * {@inheritDoc}.
//     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#hasTransactionsRecursive()
//     */
//    @Override
//    public boolean hasTransactionsRecursive() {
//        // TODO Auto-generated method stub
//        return false;
//    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#isChildAccountRecursive(biz.wolschon.fileformats.gnucash.GnucashAccount)
     */
    @Override
    public boolean isChildAccountRecursive(final GnucashAccount aAccount) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Object aO) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}.
     * @see biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashObject#getGnucashFile()
     */
    @Override
    public GnucashFile getGnucashFile() {
        return myGnucashFile;
    }

}
