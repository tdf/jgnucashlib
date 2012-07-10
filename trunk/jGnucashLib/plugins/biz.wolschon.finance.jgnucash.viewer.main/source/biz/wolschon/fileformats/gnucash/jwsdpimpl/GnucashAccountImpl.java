/**
 * GnucashAccountImpl.java
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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.baseclasses.SimpleAccount;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncAccountType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.ObjectFactory;

/**
 * created: 13.05.2005 <br/>
 *
 * Implementation of GnucashAccount that used a
 * jwsdp-generated backend.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class GnucashAccountImpl extends SimpleAccount implements GnucashAccount {

    /**
     * Our logger for debug- and error-ourput.
     */
    private static final Log LOGGER = LogFactory.getLog(GnucashAccountImpl.class);


    /**
     * Helper to implement the {@link GnucashObject}-interface.
     */
    protected GnucashObjectImpl helper;


    /**
     * @param peer the JWSDP-object we are facading.
     * @param gncfile the file to register under
     * @see #jwsdpPeer
     * @see #file
     * @throws JAXBException if there are issues with the XML-backend
     */
    public GnucashAccountImpl(final GncAccountType peer,
                              final GnucashFile gncfile) throws JAXBException {
        super(gncfile);
        jwsdpPeer = peer;
        if (peer.getActSlots() == null) {
            peer.setActSlots(new ObjectFactory().createSlotsType());
        }
        helper = new GnucashObjectImpl(peer.getActSlots(), gncfile);
    }


    /**
     * Examples:
     * The user-defined-attribute "hidden"="true"/"false"
     * was introduced in gnucash2.0 to hide accounts.
     *
     * @param name the name of the user-defined attribute
     * @return the value or null if not set
     */
    public String getUserDefinedAttribute(final String name) {
        return helper.getUserDefinedAttribute(name);
    }

    /**
     *
     * @return all keys that can be used with ${@link #getUserDefinedAttribute(String)}}.
     */
    public Collection<String> getUserDefinedAttributeKeys() {
    	return helper.getUserDefinedAttributeKeys();
    }


    /**
     * the JWSDP-object we are facading.
     */
    private GncAccountType jwsdpPeer;


    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getId()
     */
    public String getId() {
        return jwsdpPeer.getActId().getValue();
    }

    /**
     *
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getParentAccountId()
     */
    public String getParentAccountId() {
        biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncAccountType.ActParentType parent = jwsdpPeer.getActParent();
        if (parent == null) {
             return null;
        }

        return parent.getValue();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getChildren()
     */
    public Collection getChildren() {
        return getGnucashFile().getAccountsByParentID(getId());
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getName()
     */
    public String getName() {
        return jwsdpPeer.getActName();
    }


    /**
     * @return "ISO4217" for a currency "FUND" or a fond,...
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getCurrencyNameSpace()
     */
    public String getCurrencyNameSpace() {
        if (jwsdpPeer.getActCommodity() == null) {
	    return "ISO4217"; // default-currency because gnucash 2.2 has no currency on the root-account
	}
        return jwsdpPeer.getActCommodity().getCmdtySpace();
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getCurrencyID()
     */
    public String getCurrencyID() {
        if (jwsdpPeer.getActCommodity() == null) {
        return "EUR"; // default-currency because gnucash 2.2 has no currency on the root-account
    }
        return jwsdpPeer.getActCommodity().getCmdtyId();
    }


    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getDescription()
     */
    public String getDescription() {
        return jwsdpPeer.getActDescription();
    }

   /**
    *
    * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getAccountCode()
    */
   public String getAccountCode() {
       return jwsdpPeer.getActCode();
   }


    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getType()
     */
    public String getType() {
        return jwsdpPeer.getActType();
    }


    /**
     * The splits of this transaction.
     * May not be fully initialized during
     * loading of the gnucash-file.
     * @see #mySplitsNeedSorting
     */
    private final List<GnucashTransactionSplit> mySplits = new LinkedList<GnucashTransactionSplit>();

    /**
     * If {@link #mySplits} needs to be sorted
     * because it was modified.
     * Sorting is done in a lazy way.
     */
    private boolean mySplitsNeedSorting = false;

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#getTransactionSplits()
     */
    @SuppressWarnings("unchecked")
	public List<GnucashTransactionSplit> getTransactionSplits() {

        if (mySplitsNeedSorting) {
         Collections.sort(mySplits);
         mySplitsNeedSorting = false;
        }

        return mySplits;
    }


    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashAccount#addTransactionSplit(biz.wolschon.fileformats.gnucash.GnucashTransactionSplit)
     */
    public void addTransactionSplit(final GnucashTransactionSplit split) {

        GnucashTransactionSplit old = getTransactionSplitByID(split.getId());
        if (old != null) {
            if (old != split) {
                IllegalStateException ex = new IllegalStateException("DEBUG");
                ex.printStackTrace();
                replaceTransactionSplit(old, split);
            }
        } else {
            mySplits.add(split);
            mySplitsNeedSorting = true;
        }
    }

    /**
     * For internal use only.
     * @param transactionSplitByID -
     * @param impl -
     */
    private void replaceTransactionSplit(
            final GnucashTransactionSplit transactionSplitByID,
            final GnucashTransactionSplit impl) {
        if (!mySplits.remove(transactionSplitByID)) {
            throw new IllegalArgumentException("old object not found!");
        }

        mySplits.add(impl);
    }


    /**
     *
     * @return the JWSDP-object we are wrapping.
     */
    public GncAccountType getJwsdpPeer() {
        return jwsdpPeer;
    }

    /**
     *
     * @param newPeer the JWSDP-object we are wrapping.
     */
    protected void setJwsdpPeer(final GncAccountType newPeer) {
        if (newPeer == null) {
            throw new IllegalArgumentException(
            "null not allowed for field this.jwsdpPeer");
        }

        jwsdpPeer = newPeer;
    }







}
