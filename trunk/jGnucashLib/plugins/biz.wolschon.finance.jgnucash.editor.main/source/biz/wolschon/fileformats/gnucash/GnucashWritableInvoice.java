/**
 * GnucashWritableInvoice.java
 * Created on 12.06.2005
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
 *  12.06.2005 - initial version
 * ...
 *
 */
package biz.wolschon.fileformats.gnucash;

import java.util.Date;

import javax.xml.bind.JAXBException;

import biz.wolschon.numbers.FixedPointNumber;

/**
 * created: 12.06.2005 <br/>
 * Invoice that can be modified if isModifiable() returns true
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 * @see #isModifiable()
 */
public interface GnucashWritableInvoice extends GnucashInvoice {

    /**
     * The gnucash-file is the top-level class to contain everything.
     * @return the file we are associated with
     */
    GnucashWritableFile getFile();

    /**
     *
     * @return false if already payments have been made or this invoice sent to a customer!
     */
    boolean isModifiable();

    public void setJob(GnucashJob job);

    void setDatePosted(Date d);

    void setDatePosted(String d) throws java.text.ParseException;

    void setDateOpened(Date d);

    void setDateOpened(String d) throws java.text.ParseException;
    

    /**
     * @return the transaction that adds this invoice's sum to
     *         the expected money.
     */
    GnucashTransaction getPostTransaction();

    /**
     * @see GnucashInvoice#getEntryById(String)
     * @param id the id to look for
     * @return the modifiable version of the entry
     */
    GnucashWritableInvoiceEntry getWritableEntryById(String id);

    /**
     * remove this invoice from the system.
     * @throws JAXBException if we have issues accessing the XML-Backend.
     */
    void remove() throws JAXBException;

    /**
     * create and add a new entry.<br/>
     * The entry will have 16% salex-tax and use the accounts of the
     * SKR03.
     * @throws JAXBException if we have issues accessing the XML-Backend.
     */
    GnucashWritableInvoiceEntry createEntry(final FixedPointNumber singleUnitPrice, final FixedPointNumber quantity) throws JAXBException;

    /**
     * create and add a new entry.<br/>
     * The entry will use the accounts of the
     * SKR03.
     * @throws JAXBException if we have issues accessing the XML-Backend.
     */
    GnucashWritableInvoiceEntry createEntry(final FixedPointNumber singleUnitPrice, final FixedPointNumber quantity, final FixedPointNumber tax) throws JAXBException;

    /**
     * create and add a new entry.<br/>
     * @throws JAXBException if we have issues accessing the XML-Backend.
     * @return an entry using the given Tax-Table
     */
    GnucashWritableInvoiceEntry createEntry(final FixedPointNumber singleUnitPrice,
                                                   final FixedPointNumber quantity,
                                                   final GnucashTaxTable tax) throws JAXBException;
}
