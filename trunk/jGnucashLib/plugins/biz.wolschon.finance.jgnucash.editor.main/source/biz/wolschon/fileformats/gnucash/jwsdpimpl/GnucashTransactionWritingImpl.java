/**
 * GnucashTransactionWritingImpl.java
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
package biz.wolschon.fileformats.gnucash.jwsdpimpl;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncTransaction;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncTransactionType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.ObjectFactory;

/**
 * created: 16.05.2005 <br/>
 * JWSDP-Implmentation of a Transaction that can be changed.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class GnucashTransactionWritingImpl extends GnucashTransactionImpl implements GnucashWritableTransaction {

    /**
     * Our logger for debug- and error-ourput.
     */
    private static final Log LOGGER = LogFactory.getLog(GnucashTransactionWritingImpl.class);

    /**
     * Our helper to implement the GnucashWritableObject-interface.
     */
    private final GnucashWritableObjectHelper helper = new GnucashWritableObjectHelper(this);

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableObject#setUserDefinedAttribute(java.lang.String, java.lang.String)
     */
    public void setUserDefinedAttribute(final String name, final String value) throws JAXBException {
        helper.setUserDefinedAttribute(name, value);
    }



    /**
     * @param file the file we belong to
     * @param jwsdpPeer the JWSDP-object we are facading.
     */
    public GnucashTransactionWritingImpl(
            final GncTransactionType jwsdpPeer,
            final GnucashFileImpl file) throws JAXBException {
        super(jwsdpPeer, file);

        // repair a broken file
        if (jwsdpPeer.getTrnDatePosted() == null) {
            LOGGER.warn("repairing broken transaction " + jwsdpPeer.getTrnId() + " with no date-posted!");
            //we use our own ObjectFactory because:   Exception in thread "AWT-EventQueue-0" java.lang.IllegalAccessError: tried to access method biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashFileImpl.getObjectFactory()Lbiz/wolschon/fileformats/gnucash/jwsdpimpl/generated/ObjectFactory; from class biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashTransactionWritingImpl
            //ObjectFactory factory =  file.getObjectFactory();
            ObjectFactory factory = new ObjectFactory();
            GncTransactionType.TrnDatePostedType datePosted = factory.createGncTransactionTypeTrnDatePostedType();
            datePosted.setTsDate(jwsdpPeer.getTrnDateEntered().getTsDate());
            jwsdpPeer.setTrnDatePosted(datePosted);
        }

    }

    /**
     * Create a new Transaction and add it to the file.
     * @param file the file we belong to
     * @throws JAXBException
     */
    public GnucashTransactionWritingImpl(final GnucashFileWritingImpl file, final String id) throws JAXBException {
        super(createTransaction(file, id), file);
        file.addTransaction(this);
    }

    /**
     * The gnucash-file is the top-level class to contain everything.
     * @return the file we are associated with
     */
    public GnucashFileWritingImpl getWritingFile()  {
        return (GnucashFileWritingImpl) getGnucashFile();
    }

    /**
     * Create a new split for a split found in the jaxb-data.
     * @param element the jaxb-data
     * @return the new split-instance
     * @throws JAXBException if we have issues with the XML-backend
     */
    @Override
    protected GnucashTransactionSplitImpl createSplit(final GncTransactionType.TrnSplitsType.TrnSplitType element) throws JAXBException {
        GnucashTransactionSplitWritingImpl gnucashTransactionSplitWritingImpl = new GnucashTransactionSplitWritingImpl(element, this);
        if (getPropertyChangeSupport() != null) {
        	getPropertyChangeSupport().firePropertyChange("splits", null, getWritingSplits());
       }
        return gnucashTransactionSplitWritingImpl;
    }



    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#createWritingSplit(biz.wolschon.fileformats.gnucash.GnucashAccount)
     */
    public GnucashWritableTransactionSplit createWritingSplit(final GnucashAccount account) throws JAXBException {
        GnucashTransactionSplitWritingImpl gnucashTransactionSplitWritingImpl = new GnucashTransactionSplitWritingImpl(this, account);
        addSplit(gnucashTransactionSplitWritingImpl);
        if (getPropertyChangeSupport() != null) {
        	getPropertyChangeSupport().firePropertyChange("splits", null, getWritingSplits());
        }
        return gnucashTransactionSplitWritingImpl;
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#createWritingSplit(biz.wolschon.fileformats.gnucash.GnucashAccount)
     */
    public GnucashWritableTransactionSplit createWritingSplit(final GnucashAccount account, final String splitID) throws JAXBException {
        GnucashTransactionSplitWritingImpl gnucashTransactionSplitWritingImpl = new GnucashTransactionSplitWritingImpl(this, account, splitID);
        if (getPropertyChangeSupport() != null) {
        	getPropertyChangeSupport().firePropertyChange("splits", null, getWritingSplits());
        }
        return gnucashTransactionSplitWritingImpl;
    }
    /**
     * Creates a new Transaction and add's it to the given gnucash-file
     * Don't modify the ID of the new transaction!
     *
     * @param file
     * @return
     * @throws JAXBException
     */
    protected static GncTransaction createTransaction(final GnucashFileWritingImpl file, final String newId) throws JAXBException {


        ObjectFactory factory = file.getObjectFactory();


        GncTransaction transaction = file.createGncTransaction();


        {
            GncTransactionType.TrnIdType id = factory.createGncTransactionTypeTrnIdType();
            id.setType("guid");
            id.setValue(newId);
            transaction.setTrnId(id);
        }

        {
            GncTransactionType.TrnDateEnteredType dateEntered = factory.createGncTransactionTypeTrnDateEnteredType();
            dateEntered.setTsDate(DATEENTEREDFORMAT.format(new Date()));
            transaction.setTrnDateEntered(dateEntered);
        }

        {
            GncTransactionType.TrnDatePostedType datePosted = factory.createGncTransactionTypeTrnDatePostedType();
            datePosted.setTsDate(DATEENTEREDFORMAT.format(new Date()));
            transaction.setTrnDatePosted(datePosted);
        }

        {
            GncTransactionType.TrnCurrencyType currency = factory.createGncTransactionTypeTrnCurrencyType();
            currency.setCmdtyId(file.getDefaultCurrencyID());
            currency.setCmdtySpace("ISO4217");
            transaction.setTrnCurrency(currency);
        }

        {
            GncTransactionType.TrnSplitsType splits = factory.createGncTransactionTypeTrnSplitsType();
            transaction.setTrnSplits(splits);
        }


        transaction.setVersion("2.0.0");
        transaction.setTrnDescription("-");





        return transaction;
    }

    /**
     * @param impl the split to remove from this transaction
     * @throws JAXBException if we have issues accessing the XML-Backend.
     */
    public void remove(final GnucashWritableTransactionSplit impl) throws JAXBException {
        getJwsdpPeer().getTrnSplits().getTrnSplit().remove(((GnucashTransactionSplitWritingImpl) impl).getJwsdpPeer());
        getWritingFile().setModified(true);
        if (mySplits != null) {
            mySplits.remove(impl);
        }
        GnucashAccountWritingImpl account =  (GnucashAccountWritingImpl)
                           impl.getAccount();
        if (account != null) {
            account.removeTransactionSplit(impl);
        }

        //there is no count for splits up to now getWritingFile().decrementCountDataFor()

        if (getPropertyChangeSupport() != null) {
        	getPropertyChangeSupport().firePropertyChange("splits", null, getWritingSplits());
        }
    }



    /**
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingFirstSplit()
     */
    @Override
    public GnucashWritableTransactionSplit getFirstSplit() throws JAXBException {
        return (GnucashWritableTransactionSplit) super.getFirstSplit();
    }


    /**
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingFirstSplit()
     */
    public GnucashWritableTransactionSplit getWritingFirstSplit() throws JAXBException {
        return (GnucashWritableTransactionSplit) super.getFirstSplit();
    }
    /**
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingSecondSplit()
    */
   @Override
public GnucashWritableTransactionSplit getSecondSplit() throws JAXBException {
       return (GnucashWritableTransactionSplit) super.getSecondSplit();
   }


    /**
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingSecondSplit()
     */
    public GnucashWritableTransactionSplit getWritingSecondSplit() throws JAXBException {
        return (GnucashWritableTransactionSplit) super.getSecondSplit();
    }

    /**
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingSplitByID(java.lang.String)
     */
    public GnucashWritableTransactionSplit getWritingSplitByID(final String id) throws JAXBException {
        return (GnucashWritableTransactionSplit) super.getSplitByID(id);
    }

    /**
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#getWritingSplits()
     */
    public Collection getWritingSplits() throws JAXBException {
        return super.getSplits();
    }

    /**
     * @param impl the split to add to mySplits
     * @throws JAXBException if we have issues with the XML-backend
     */
    protected void addSplit(final GnucashTransactionSplitWritingImpl impl) throws JAXBException {
    	super.addSplit(impl);
    }

    /**
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#remove()
     */
    public void remove() throws JAXBException {
        getWritingFile().removeTransaction(this);
        Collection<GnucashWritableTransactionSplit> c = new LinkedList<GnucashWritableTransactionSplit>();
        c.addAll(getWritingSplits());
        for (GnucashWritableTransactionSplit element : c) {
            element.remove();
        }

    }


    /**
     * @param id the new currency
     * @see #setCurrencyNameSpace(String)
     * @see ${@link GnucashTransaction#getCurrencyID()}
     */
    public void setCurrencyID(final String id) {
        this.getJwsdpPeer().getTrnCurrency().setCmdtyId(id);
    }
    /**
     * @param id the new namespace
     * @see ${@link GnucashTransaction#getCurrencyNameSpace()}
     */
    public void setCurrencyNameSpace(final String id) {
        this.getJwsdpPeer().getTrnCurrency().setCmdtySpace(id);
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setDateEntered(java.util.Date)
     */
    public void setDateEntered(final Date dateEntered) {
        this.dateEntered = dateEntered;
        getJwsdpPeer().getTrnDateEntered().setTsDate(DATEENTEREDFORMAT.format(dateEntered));
        getWritingFile().setModified(true);
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setDatePosted(java.util.Date)
     */
    public void setDatePosted(final Date datePosted) {
        this.datePosted = datePosted;
        getJwsdpPeer().getTrnDatePosted().setTsDate(DATEENTEREDFORMAT.format(datePosted));
        getWritingFile().setModified(true);
    }

    /**
     * @see ${@link #setDatePosted(Date)};
     */
    public void setDatePostedFormatet(final String datePosted) {
        try {
            this.setDatePosted(java.text.DateFormat.getDateInstance().parse(datePosted));
        } catch (ParseException e) {
            IllegalArgumentException x = new IllegalArgumentException("cannot parse datePosted '"
                    + datePosted
                    + "'");
            x.initCause(e);
            throw x;
        }
    }


    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setDescription(java.lang.String)
     */
    public void setDescription(final String desc) {
        if (desc == null) {
            throw new IllegalArgumentException("null description given! Please use the empty string instead of null for an empty description");
        }

        String old = getJwsdpPeer().getTrnDescription();
        getJwsdpPeer().setTrnDescription(desc);
        getWritingFile().setModified(true);

        if (old == null || !old.equals(desc)) {
            if (getPropertyChangeSupport() != null) {
            	getPropertyChangeSupport().firePropertyChange("description", old, desc);
            }
        }
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableTransaction#setTransactionNumber(java.lang.String)
     */
    public void setTransactionNumber(final String tnum) {
        if (tnum == null) {
            throw new IllegalArgumentException("null transaction-number given! Please use the empty string instead of null for an empty description");
        }

        String old = getJwsdpPeer().getTrnNum();
        getJwsdpPeer().setTrnNum(tnum);
        getWritingFile().setModified(true);

        if (old == null || !old.equals(tnum)) {
            if (getPropertyChangeSupport() != null) {
            	getPropertyChangeSupport().firePropertyChange("transactionNumber", old, tnum);
            }
        }
	}






}
