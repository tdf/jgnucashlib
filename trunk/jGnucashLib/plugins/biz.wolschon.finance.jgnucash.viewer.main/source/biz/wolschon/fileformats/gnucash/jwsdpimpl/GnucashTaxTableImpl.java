/**
 * GnucashJobImpl.java
 * License: GPLv3 or later
 * Created on 14.05.2005
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 *
 * -----------------------------------------------------------
 * major Changes:
 *  14.05.2005 - initial version
 * ...
 *
 */
package biz.wolschon.fileformats.gnucash.jwsdpimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashTaxTable;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncTaxTableType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncTaxTableType.TaxtableEntriesType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncTaxTableType.TaxtableParentType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncTaxTableType.TaxtableEntriesType.GncGncTaxTableEntryType;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * created: 14.05.2005 <br/>
 * Implementation of GnucashTaxTable that uses JWSDP.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 * @see biz.wolschon.fileformats.gnucash.GnucashTaxTable
 */
public class GnucashTaxTableImpl implements GnucashTaxTable {


    /**
     * (c) 2005 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
     * Project: gnucashReader<br/>
     * GnucashTaxTableImpl.java<br/>
     * created: 22.09.2005 16:37:34 <br/>
     * <br/><br/>
     * Entry in the Taxtable
     * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
     */
    protected static class GnucashTaxTableEntryImpl implements TaxTableEntry {


        /**
         * the jwsdp-object we are wrapping.
         */
        private GncGncTaxTableEntryType jwsdpPeer;

        /**
         * the file we belong to.
         */
        private final GnucashFile myFile;


        /**
         * @param element the jwsdp-object we are wrapping
         * @param file the file we belong to
         */
        public GnucashTaxTableEntryImpl(final GncGncTaxTableEntryType element, final GnucashFile file) {
            super();
            setJwsdpPeer(element);
            myFile = file;
        }


        /**
         * @return the jwsdp-object we are wrapping
         */
        protected GncGncTaxTableEntryType getJwsdpPeer() {
            return jwsdpPeer;
        }


        private void setJwsdpPeer(GncGncTaxTableEntryType jwsdpPeer) {
            if (jwsdpPeer == null) {
                throw new IllegalArgumentException("null 'jwsdpPeer' given!");
            }

            this.jwsdpPeer = jwsdpPeer;
        }


        /**
         * @return the amount the tax is
         * @see ${@link #getType()}
         */
        public FixedPointNumber getAmount() {
            return new FixedPointNumber(getJwsdpPeer().getTteAmount());
        }

/*
        public void setAmount(final FixedPointNumber amount) {
            if (amount == null) {
                throw new IllegalArgumentException("null 'amount' given!");
            }
            getJwsdpPeer().setTteAmount(amount.toGnucashString());
        }
*/

        /**
         * usually ${@link TaxTableEntry#TYPE_PERCENT}.
         * @see ${@link #getAmount())
         */
        public String getType() {
            return getJwsdpPeer().getTteType();
        }

/*
        public void setType(final String type) {
            if (type == null) {
                throw new IllegalArgumentException("null 'type' given!");
            }
            getJwsdpPeer().setTteType(type);
        }*/

        /**
         * initialised lazy.
         */
        private String myAccountID;
        /**
         * initialised lazy.
         */
        private GnucashAccount myAccount;


        /**
         * @return Returns the account.
         * @see ${@link #myAccount}
         */
        public GnucashAccount getAccount() {
            if (myAccount == null) {
                myAccount = myFile.getAccountByID(this.getAccountID());
            }

            return myAccount;
        }


        /**
         * @param account The account to set.
         * @see ${@link #myAccount}
         */
        public void setAccount(final GnucashAccount account) {
            if (account == null) {
                throw new IllegalArgumentException("null 'account' given!");
            }

            myAccount = account;
            myAccountID = account.getId();
            getJwsdpPeer().getTteAcct().setType("guid");
            getJwsdpPeer().getTteAcct().setValue(account.getId());
        }


        /**
         * @return Returns the accountID.
         * @see ${@link #myAccountID}
         */
        public String getAccountID() {
            if (myAccountID == null) {
                myAccountID = getJwsdpPeer().getTteAcct().getValue();

            }

            return myAccountID;
        }




        /**
         * @param accountID The accountID to set.
         * @see ${@link #myAccountID}
         */
  /*      public void setAccountID(final String accountID) {
            if (accountID == null) {
                throw new IllegalArgumentException("null 'accountID' given!");
            }
            myAccount = null;
            myAccountID = accountID;
            getJwsdpPeer().getTteAcct().setType("guid");
            getJwsdpPeer().getTteAcct().setValue(accountID);
        }

*/
    }

    /**
     * the JWSDP-object we are facading.
     */
    private final GncGncTaxTableType jwsdpPeer;

    /**
     * The file we belong to.
     */
    private final GnucashFile file;

    /**
     * @param peer the JWSDP-object we are facading.
     * @see #jwsdpPeer
     * @param gncFile the file to register under
     */
    public GnucashTaxTableImpl(
            final GncGncTaxTableType peer,
            final GnucashFile gncFile) {
        super();
        jwsdpPeer = peer;
        file = gncFile;
    }

    /**
     * The gnucash-file is the top-level class to contain everything.
     * @return the file we are associated with
     */
    public GnucashFile getFile() {
        return file;
    }

    /**
     *
     * @return The JWSDP-Object we are wrapping.
     */
    public GncGncTaxTableType getJwsdpPeer() {
        return jwsdpPeer;
    }


    /**
     * @return the unique-id to identify this object with across name- and hirarchy-changes
     */
    public String getId() {
        assert  jwsdpPeer.getTaxtableGuid().getType().equals("guid");

        String guid = jwsdpPeer.getTaxtableGuid().getValue();
        if (guid == null) {
            throw new IllegalStateException(
                    "taxtable has a null guid-value! guid-type="
                   + jwsdpPeer.getTaxtableGuid().getType());
        }

         return guid;
    }



    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashTaxTable#getName()
     */
    public String getName() {
        return jwsdpPeer.getTaxtableName();
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashTaxTable#isInvisible()
     */
    public boolean isInvisible() {
        return jwsdpPeer.getTaxtableInvisible()  != 0;
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashTaxTable#getParentID()
     */
    public String getParentID() {
        TaxtableParentType parent = jwsdpPeer.getTaxtableParent();
        if (parent == null) {
            return null;
        }
        return parent.getValue();
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashTaxTable#getParent()
     * @return the parent tax-table or null
     */
    public GnucashTaxTable getParent() {
        return getFile().getTaxTableByID(getParentID());
    }

    /**
     * @see #getEntries()
     */
    private Collection<TaxTableEntry> entries = null;

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashTaxTable#getEntries()
     * @return all entries to this tax-table
     */
    @SuppressWarnings("unchecked")
    public Collection<TaxTableEntry> getEntries() {
        if (entries == null) {
            TaxtableEntriesType jwsdpEntries = getJwsdpPeer().getTaxtableEntries();
            entries = new ArrayList<TaxTableEntry>(jwsdpEntries.getGncGncTaxTableEntry().size());
            for (Iterator<GncGncTaxTableEntryType> iter = jwsdpEntries.getGncGncTaxTableEntry().iterator(); iter.hasNext();) {
                GncGncTaxTableEntryType element = iter.next();

                entries.add(new GnucashTaxTableEntryImpl(element, getFile()));
            }

        }

        return entries;

    }
}
