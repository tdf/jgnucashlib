/**
 * GnucashCustomerImpl.java
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

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashCustomer;
import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashInvoice;
import biz.wolschon.fileformats.gnucash.GnucashJob;
import biz.wolschon.fileformats.gnucash.GnucashTaxTable;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncCustomerType;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.ObjectFactory;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncCustomerType.CustTaxtableType;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * created: 14.05.2005 <br/>
 *
 * JWSDP-implementation of the
 * {@link biz.wolschon.fileformats.gnucash.GnucashCustomer}.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class GnucashCustomerImpl extends GnucashObjectImpl implements GnucashCustomer {

    /**
     * the JWSDP-object we are facading.
     */
    private final GncGncCustomerType jwsdpPeer;


    /**
     * @param peer the JWSDP-object we are facading.
     * @param gncFile the file to register under
     * @throws JAXBException on problems with the xml-backend
     * @see #jwsdpPeer
     * @see #file
     */
    protected GnucashCustomerImpl(
            final GncGncCustomerType peer,
            final GnucashFile gncFile) throws JAXBException {
        super((peer.getCustSlots() == null) ? new ObjectFactory().createSlotsType() : peer.getCustSlots(), gncFile);
        if (peer.getCustSlots() == null) {
            peer.setCustSlots(getSlots());
        }
        jwsdpPeer = peer;
    }

    /**
     *
     * @return the JWSDP-object we are wrapping.
     */
    public GncGncCustomerType getJwsdpPeer() {
        return jwsdpPeer;
    }


    /**
     * {@inheritDoc}
     */
    public String getId() {
         return jwsdpPeer.getCustGuid().getValue();
    }

    /**
     * @return the jobs that have this customer associated with them.
     * @see biz.wolschon.fileformats.gnucash.GnucashCustomer#getJobs()
     */
    public java.util.Collection<GnucashJob> getJobs() {

        List<GnucashJob> retval = new LinkedList<GnucashJob>();

        for (GnucashJob job : getGnucashFile().getJobs()) {
            if (job.getCustomerId().equals(getId())) {
                retval.add(job);
            }
        }

        return retval;
    }

    /**
     * {@inheritDoc}
     */
    public String getDiscount() {
        return jwsdpPeer.getCustDiscount();
    }

    /**
     * {@inheritDoc}
     */
    public String getNotes() {
        return jwsdpPeer.getCustNotes();
    }

    /**
     * date is not checked so invoiced that have entered payments in
     * the future are considered payed.
     * @return the current number of unpayed invoices
     *
     * @throws JAXBException if we have issues with the XML-backend
     */
    public int getOpenInvoices() throws JAXBException {
        int count = 0;
        for (GnucashInvoice invoice : getGnucashFile().getInvoices()) {
            if (invoice.getCustomer() != this) {
                continue;
            }

            if (invoice.isNotFullyPayed()) {
                count++;
            }


        }
        return count;
    }

    /**
     * @return the sum of payments for invoices to this client
     */
    public FixedPointNumber getIncomeGenerated() {
        FixedPointNumber retval = new FixedPointNumber();

        for (GnucashInvoice invoice : getGnucashFile().getInvoices()) {
            if (invoice.getCustomer() != this) {
                continue;
            }
            retval.add(invoice.getAmmountWithoutTaxes());
        }

        return retval;
    }

    /**
     * The currencyFormat to use for default-formating.<br/>
     * Please access only using {@link #getCurrencyFormat()}.
     * @see #getCurrencyFormat()
     */
    private NumberFormat currencyFormat = null;

    /**
     * @see #getIncomeGenerated()
     * @return formated acording to the current locale's currency-format
     */
    public String getIncomeGeneratedFormatet() {
        return getCurrencyFormat().format(getIncomeGenerated());

    }

    /**
     * @see #getIncomeGenerated()
     * @param l the locale to format for
     * @return formated acording to the given locale's currency-format
     */
    public String getIncomeGeneratedFormatet(final Locale l) {
        return NumberFormat.getCurrencyInstance(l).format(getIncomeGenerated());
    }

    /**
     * @return the sum of left to pay unpayed invoiced
     *
     * @throws JAXBException if we have issues with the XML-backend
     */
    public FixedPointNumber getOutstandingValue() throws JAXBException  {
        FixedPointNumber retval = new FixedPointNumber();

        for (GnucashInvoice invoice : getGnucashFile().getInvoices()) {
            if (invoice.getCustomer() != this) {
                continue;
            }
            retval.add(invoice.getAmmountUnPayed());
        }

        return retval;
    }

    /**
     * @throws JAXBException if we have issues with the XML-backend
     * @see #getOutstandingValue()
     * @return formatet acording to the current locale's currency-format
     */
    public String getOutstandingValueFormatet() throws JAXBException {
        return getCurrencyFormat().format(getOutstandingValue());
    }

    /**
     *
     * @throws JAXBException if we have issues with the XML-backend
     * @see #getOutstandingValue()
     * formatet acording to the given locale's currency-format
     */
    public String getOutstandingValueFormatet(final Locale l) throws JAXBException {
        return NumberFormat.getCurrencyInstance(l).format(getOutstandingValue());
    }



    /**
     * {@inheritDoc}
     */
    public String getCustomerNumber() {
        return jwsdpPeer.getCustId();
    }

    /**
     * {@inheritDoc}
     */
    public String getCustomerTaxTableID() {
        CustTaxtableType custTaxtable = jwsdpPeer.getCustTaxtable();
        if (custTaxtable == null) {
            return null;
        }
        return custTaxtable.getValue();
    }

    /**
     * {@inheritDoc}
     */
    public GnucashTaxTable getCustomerTaxTable() {
        String id = getCustomerTaxTableID();
        if (id == null) {
            return null;
        }
        return getGnucashFile().getTaxTableByID(id);
    }



    /**
     * {@inheritDoc}
     */
    public String getName() {
        return jwsdpPeer.getCustName();
    }

    /**
     * {@inheritDoc}
     */
    public GnucashCustomer.Address getAddress() {
        return new AddressImpl(jwsdpPeer.getCustAddr());
    }

    /**
     * {@inheritDoc}
     */
    public GnucashCustomer.Address getShippingAddress() {
        return new AddressImpl(jwsdpPeer.getCustShipaddr());
    }



    /**
    *
    * (c) 2005 by Wolschon Softwaredesign und Beratung.<br/>
    * Project: gnucashReader<br/>
    * GnucashCustomerImpl.java<br/>
    * @see Address
    * @author <a href="Marcus@Wolschon.biz">Marcus Wolschon</a>
    */
    public static class AddressImpl implements Address {

        /**
         * The JWSDP-object we are wrapping.
         */
        private final biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.Address jwsdpPeer;

        /**
         * @param newPeer the JWSDP-object we are wrapping.
         */
        public AddressImpl(
                final biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.Address newPeer) {
            super();
            jwsdpPeer = newPeer;
        }
        /**
         *
         * @return The JWSDP-object we are wrapping.
         */
        public biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.Address getJwsdpPeer() {
            return jwsdpPeer;
        }


        /**
         * @see biz.wolschon.fileformats.gnucash.GnucashCustomer.Address#getAddressName()
         */
       public String getAddressName() {
    	   if (jwsdpPeer.getAddrName() == null) {
            return "";
        }
          return jwsdpPeer.getAddrName();
         }

       /**
        * @see biz.wolschon.fileformats.gnucash.GnucashCustomer.Address#getAddressLine1()
        */
         public String getAddressLine1() {
        	 if (jwsdpPeer.getAddrAddr1() == null) {
                return "";
            }
             return jwsdpPeer.getAddrAddr1();
         }

         /**
          * @see biz.wolschon.fileformats.gnucash.GnucashCustomer.Address#getAddressLine2()
          */
         public String getAddressLine2() {
        	 if (jwsdpPeer.getAddrAddr2() == null) {
                return "";
            }
             return jwsdpPeer.getAddrAddr2();
         }

         /**
         *
         * @return third and last line below the name
         */
        public String getAddressLine3() {
        	if (jwsdpPeer.getAddrAddr3() == null) {
                return "";
            }
            return jwsdpPeer.getAddrAddr3();
        }
        /**
         *
         * @return fourth and last line below the name
         */
        public String getAddressLine4() {
        	if (jwsdpPeer.getAddrAddr4() == null) {
                return "";
            }
            return jwsdpPeer.getAddrAddr4();
        }

        /**
         *
         * @return telephone
         */
        public String getTel() {
        	if (jwsdpPeer.getAddrPhone() == null) {
                return "";
            }
            return jwsdpPeer.getAddrPhone();
        }

        /**
         *
         * @return Fax
         */
        public String getFax() {
        	if (jwsdpPeer.getAddrFax() == null) {
                return "";
            }
            return jwsdpPeer.getAddrFax();
        }

        /**
         *
         * @return Email
         */
        public String getEmail() {
        	if (jwsdpPeer.getAddrEmail() == null) {
                return "";
            }
            return jwsdpPeer.getAddrEmail();
        }

         /**
          * @see java.lang.Object#toString()
          */
         @Override
        public String toString() {
            return getAddressName() + "\n"
                 + getAddressLine1() + "\n"
                 + getAddressLine2();
           }
     }



    /**
     *
     * @return the currency-format to use if no locale is given.
     */
    protected NumberFormat getCurrencyFormat() {
        if (currencyFormat == null) {
            currencyFormat = NumberFormat.getCurrencyInstance();
        }

        return currencyFormat;
    }
}
