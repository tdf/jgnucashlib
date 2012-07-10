/**
 * GnucashInvoiceEntryWritingImpl.java
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

import java.beans.PropertyChangeSupport;
import java.util.Date;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashCustomer;
import biz.wolschon.fileformats.gnucash.GnucashInvoice;
import biz.wolschon.fileformats.gnucash.GnucashInvoiceEntry;
import biz.wolschon.fileformats.gnucash.GnucashTaxTable;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableInvoice;
import biz.wolschon.fileformats.gnucash.GnucashWritableInvoiceEntry;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.ObjectFactory;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryGuidType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryITaxtableType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryInvoiceType;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * created: 16.05.2005 <br/>
 * Additional supported properties for PropertyChangeListeners:
 * <ul>
 *  <li>description</li>
 *  <li>price</li>
 *  <li>quantity</li>
 *  <li>action</li>
 * </ul>
 *
 * Entry-Line in an invoice that can be created or removed.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class GnucashInvoiceEntryWritingImpl extends GnucashInvoiceEntryImpl implements GnucashWritableInvoiceEntry {

    /**
     * Our helper to implement the GnucashWritableObject-interface.
     */
    private final GnucashWritableObjectHelper helper = new GnucashWritableObjectHelper(this);

    /**
     * {@inheritDoc}
     */
    public void setUserDefinedAttribute(final String name, final String value) throws JAXBException {
        helper.setUserDefinedAttribute(name, value);
    }

    /**
     * @param file the file we belong to
     * @param jwsdpPeer the JWSDP-object we are facading.
     * @see GnucashInvoiceEntryImpl#GnucashInvoiceEntryImpl(GncV2Type.GncBookType.GncGncEntryType, GnucashFileImpl)
     * @throws JAXBException on problems with the xml-backend
     */
    public GnucashInvoiceEntryWritingImpl(
                      final GncGncEntryType jwsdpPeer,
                      final GnucashFileWritingImpl file) throws JAXBException {
        super(jwsdpPeer, file);
    }
    /**
     * @param invoice tne invoice this entry shall belong to
     * @param jwsdpPeer the JWSDP-object we are facading.
     * @see GnucashInvoiceEntryImpl#GnucashInvoiceEntryImpl(GnucashInvoice, GncV2Type.GncBookType.GncGncEntryType)
     * @throws JAXBException on problems with the xml-backend
     */
    public GnucashInvoiceEntryWritingImpl(final GnucashInvoiceWritingImpl invoice,
                                          final GncGncEntryType jwsdpPeer) throws JAXBException {
        super(invoice, jwsdpPeer);
        this.invoice = invoice;
    }

    /**
     * @param invoice
     * @param quantity
     * @param price
     * @return
     * @throws JAXBException
     */
    private static GncGncEntryType createSKR03_16PercentInvoiceEntry(
                                                                                           final GnucashInvoiceWritingImpl invoice,
                                                                                           final FixedPointNumber quantity,
                                                                                           final FixedPointNumber price) throws JAXBException {
        GnucashAccount account = invoice.getFile().getAccountByName("Umsatzerlöse 16% USt");
        if (account == null) {
            account = invoice.getFile().getAccountByName("Umsatzerlöse 19% USt"); // national tax-rate has changed
        }
        if (account == null) {
            throw new IllegalStateException("Cannot file account 'Umsatzerlöse 16% USt' from SKR04!");
        }

        return  createInvoiceEntry(invoice, account, quantity, price);
    }

    public GnucashInvoiceEntryWritingImpl(final GnucashInvoiceWritingImpl invoice,
                                          final FixedPointNumber quantity,
                                          final FixedPointNumber price) throws JAXBException {
        super(invoice, createSKR03_16PercentInvoiceEntry(invoice, quantity, price));
        invoice.addEntry(this);
        this.invoice = invoice;
    }

    /**
     * Create a taxable invoiceEntry.
     * (It has the taxtable of the customer with a fallback
     *  to the first taxtable found assigned)
     *
     * @param invoice the invoice to add this split to
     * @param account the income-account the money comes from
     * @param quantity see ${@link GnucashInvoiceEntry#getQuantity()}
     * @param price see ${@link GnucashInvoiceEntry#getPrice()}
     * @return
     * @throws JAXBException
     */
    public GnucashInvoiceEntryWritingImpl(final GnucashInvoiceWritingImpl invoice,
                                          final GnucashAccount account,
                                          final FixedPointNumber quantity,
                                          final FixedPointNumber price) throws JAXBException {
        super(invoice, createInvoiceEntry(invoice, account, quantity, price));
        invoice.addEntry(this);
        this.invoice = invoice;
    }

    /**
     * @see ${@link #GnucashInvoiceEntryWritingImpl(GnucashInvoiceWritingImpl, GnucashAccount, FixedPointNumber, FixedPointNumber)}
     */
    protected static GncGncEntryType createInvoiceEntry(
                                         final GnucashInvoiceWritingImpl invoice,
                                         final GnucashAccount account,
                                         final FixedPointNumber quantity,
                                         final FixedPointNumber price) throws JAXBException {

//      TODO: keep count-data in file intact <gnc:count-data cd:type="gnc:GncEntry">18</gnc:count-data>

        if (!invoice.isModifiable()) {
            throw new IllegalArgumentException("The given invoice has payments and is"
                    + " thus not modifiable");
        }


        GnucashFileWritingImpl gnucashFileWritingImpl = (GnucashFileWritingImpl) invoice.getFile();
        ObjectFactory factory = gnucashFileWritingImpl.getObjectFactory();

        GncGncEntryType entry = gnucashFileWritingImpl.createGncGncEntryType();

        {
            EntryGuidType guid = factory.createGncGncEntryTypeEntryGuidType();
            guid.setType("guid");
            guid.setValue((gnucashFileWritingImpl).createGUID());
            entry.setEntryGuid(guid);
        }

        entry.setEntryAction("Stunden");
        {
            GncGncEntryType.EntryDateType entryDate = factory.createGncGncEntryTypeEntryDateType();
            entryDate.setTsDate(ENTRYDATEFORMAT.format(new Date()));
            entry.setEntryDate(entryDate);
        }
        entry.setEntryDescription("no description");
        {
            GncGncEntryType.EntryEnteredType entered = factory.createGncGncEntryTypeEntryEnteredType();
            entered.setTsDate(ENTRYDATEFORMAT.format(new Date()));
            entry.setEntryEntered(entered);
        }
        {
            GncGncEntryType.EntryIAcctType iacct = factory.createGncGncEntryTypeEntryIAcctType();
            iacct.setType("guid");
            iacct.setValue(account.getId());
            entry.setEntryIAcct(iacct);
        }
        entry.setEntryIDiscHow("PRETAX");
        entry.setEntryIDiscType("PERCENT");
        {

            EntryInvoiceType inv = factory.createGncGncEntryTypeEntryInvoiceType();
            inv.setType("guid");
            inv.setValue(invoice.getId());
            entry.setEntryInvoice(inv);
        }
        entry.setEntryIPrice(price.toGnucashString());
        entry.setEntryITaxable(1);
        entry.setEntryITaxincluded(0);
        {
            //TODO: use not the first but the default taxtable
            EntryITaxtableType taxtableref = factory.createGncGncEntryTypeEntryITaxtableType();
            taxtableref.setType("guid");

            GnucashTaxTable taxTable = null;
            GnucashCustomer customer = invoice.getCustomer();
            if (customer != null) {
                taxTable = customer.getCustomerTaxTable();
            }

            // use first tax-table found
            if (taxTable == null) {
                taxTable = invoice.getFile().getTaxTables().iterator().next();
            }

            /*GncV2Type.GncBookType.GncGncTaxTableType taxtable = (GncV2Type.GncBookType.GncGncTaxTableType)
                ((GnucashFileImpl) invoice.getFile()).getRootElement().getGncBook().getGncGncTaxTable().get(0);

            taxtableref.setValue(taxtable.getTaxtableGuid().getValue());*/
            taxtableref.setValue(taxTable.getId());
            entry.setEntryITaxtable(taxtableref);
        }

        entry.setEntryQty(quantity.toGnucashString());
        entry.setVersion("2.0.0");



        invoice.getFile().getRootElement().getGncBook().getBookElements().add(entry);
        invoice.getFile().setModified(true);

        return entry;
    }

    /*public GncV2Type.GncBookType.GncGncEntryType getJwsdpPeer() {
        return super.getJwsdpPeer();
    }*/

    /**
     * @see ${@link #getInvoice()}
     */
    private GnucashWritableInvoice invoice;

    /**
     * {@inheritDoc}
     */
    @Override
    public GnucashWritableInvoice getInvoice() {
        if (invoice == null) {
            invoice = (GnucashWritableInvoice) super.getInvoice();
        }
        return invoice;

    }

    /**
     * Set the description-text.
     * @param desc the new description
     */
    public void setDescription(final String desc) {
        if (desc == null) {
            throw new IllegalArgumentException("null description given! Please use the empty string instead of null for an empty description");
        }
        if (!this.getInvoice().isModifiable()) {
            throw new IllegalStateException("This Invoice has payments and is not modifiable!");
        }
        Object old = getDescription();
        getJwsdpPeer().setEntryDescription(desc);

        PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange("description", old, desc);
        }
    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashInvoiceEntry#isTaxable()
     */
    public void setTaxable(final boolean is) {
        try {
            ((GnucashInvoiceWritingImpl) getInvoice()).subtractEntry(this);
            if (is) {
                getJwsdpPeer().setEntryITaxable(1);
            } else {
                getJwsdpPeer().setEntryITaxable(0);
            }
            ((GnucashInvoiceWritingImpl) getInvoice()).addEntry(this);
        } catch (JAXBException x) {
            IllegalStateException x2 = new IllegalStateException("cannot set isTaxable of Invoice-Entry '" + toString() + "'");
            x2.initCause(x);
            throw x2;
        }

    }

    /**
     * {@inheritDoc}
     */
    public void setTaxTable(final GnucashTaxTable tax) throws JAXBException {

        try {
            ((GnucashInvoiceWritingImpl) getInvoice()).subtractEntry(this);


            super.setTaxtable(tax);
            if (tax == null) {
                getJwsdpPeer().setEntryITaxable(0);
            } else {
                getJwsdpPeer().setEntryITaxable(1);
                if (getJwsdpPeer().getEntryITaxtable() == null) {
                    getJwsdpPeer().setEntryITaxtable(
                            ((GnucashFileWritingImpl) getInvoice().getFile())
                            .getObjectFactory()
                            .createGncGncEntryTypeEntryITaxtableType());
                    getJwsdpPeer().getEntryITaxtable().setValue("guid");
                }
                getJwsdpPeer().getEntryITaxtable().setValue(tax.getId());
            }

            ((GnucashInvoiceWritingImpl) getInvoice()).addEntry(this);
        } catch (JAXBException x) {
            IllegalStateException x2 = new IllegalStateException("cannot set taxTable of Invoice-Entry '" + toString() + "'");
            x2.initCause(x);
            throw x2;
        }



    }

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableInvoiceEntry#setPrice(FixedPointNumber)
     */
    public void setPrice(final String n) {
        this.setPrice(new FixedPointNumber(n));
    }
    public void setPriceFormatet(final String n) {
        this.setPrice(new FixedPointNumber(n));
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableInvoiceEntry#setPrice(FixedPointNumber)
     */
    public void setPrice(final FixedPointNumber price) {
        if (!this.getInvoice().isModifiable()) {
            throw new IllegalStateException("This Invoice has payments and is not modifiable!");
        }
        try {
            Object old = getPrice();

            ((GnucashInvoiceWritingImpl) getInvoice()).subtractEntry(this);
            getJwsdpPeer().setEntryIPrice(price.toGnucashString());
            ((GnucashInvoiceWritingImpl) getInvoice()).addEntry(this);


            PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
            if (propertyChangeSupport != null) {
                propertyChangeSupport.firePropertyChange("price", old, price);
            }

        } catch (JAXBException x) {
            IllegalStateException x2 = new IllegalStateException("cannot set price of Invoice-Entry '" + toString() + "'");
            x2.initCause(x);
            throw x2;
        }
    }
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableInvoiceEntry#setAction(java.lang.String)
     */
    public void setAction(final String action) {
        if (!this.getInvoice().isModifiable()) {
            throw new IllegalStateException("This Invoice has payments and is not modifiable!");
        }

        Object old = getAction();
        getJwsdpPeer().setEntryAction(action);

        PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange("action", old, action);
        }

    }


    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableInvoiceEntry#setQuantity(FixedPointNumber)
     */
    public void setQuantity(final String n) {
        FixedPointNumber fp = new FixedPointNumber(n);
        System.out.println("DEBUG: GnucashInvoiceEntryWritingImpl.setQuantity('" + n + "') - setting to " + fp.toGnucashString());
        this.setQuantity(fp);
    }
    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableInvoiceEntry#setQuantityFormated(String)
     */
    public void setQuantityFormated(final String n) {
        FixedPointNumber fp = new FixedPointNumber(n);
        System.out.println("DEBUG: GnucashInvoiceEntryWritingImpl.setQuantityFormated('" + n + "') - setting to " + fp.toGnucashString());
        this.setQuantity(fp);
    }
    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableInvoiceEntry#setQuantity(FixedPointNumber)
     */
    public void setQuantity(final FixedPointNumber quantity) {
        if (!this.getInvoice().isModifiable()) {
            throw new IllegalStateException("This Invoice has payments and is not modifiable!");
        }
        try {
            Object old = getQuantity();

            ((GnucashInvoiceWritingImpl) getInvoice()).subtractEntry(this);
            getJwsdpPeer().setEntryQty(quantity.toGnucashString());
            ((GnucashInvoiceWritingImpl) getInvoice()).addEntry(this);

            PropertyChangeSupport propertyChangeSupport = getPropertyChangeSupport();
            if (propertyChangeSupport != null) {
                propertyChangeSupport.firePropertyChange("quantity", old, quantity);
            }

        } catch (JAXBException x) {
            IllegalStateException x2 = new IllegalStateException("cannot set quantity of Invoice-Entry '"
                    + toString() + "'");
            x2.initCause(x);
            throw x2;
        }
    }
    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableInvoiceEntry#remove()
     */
    public void remove() {
        if (!this.getInvoice().isModifiable()) {
            throw new IllegalStateException("This Invoice has payments and is not modifiable!");
        }
        try {
            GnucashInvoiceWritingImpl gnucashInvoiceWritingImpl = ((GnucashInvoiceWritingImpl) getInvoice());
            gnucashInvoiceWritingImpl.removeEntry(this);
            gnucashInvoiceWritingImpl.getFile().getRootElement().getGncBook().getBookElements().remove(this.getJwsdpPeer());
            ((GnucashFileWritingImpl) gnucashInvoiceWritingImpl.getFile()).decrementCountDataFor("gnc:GncEntry");
        } catch (JAXBException x) {
            IllegalStateException x2 = new IllegalStateException("cannot remove Invoice-Entry '" + toString() + "'");
            x2.initCause(x);
            throw x2;
        }

    }

    /**
     * {@inheritDoc}
     */
    public GnucashWritableFile getWritableGnucashFile() {
        return (GnucashWritableFile) super.getGnucashFile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GnucashWritableFile getGnucashFile() {
        return (GnucashWritableFile) super.getGnucashFile();
    }
}
