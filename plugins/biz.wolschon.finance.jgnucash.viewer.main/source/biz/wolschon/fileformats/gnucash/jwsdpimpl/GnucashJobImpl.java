/**
 * GnucashJobImpl.java
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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import biz.wolschon.fileformats.gnucash.GnucashCustomer;
import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashInvoice;
import biz.wolschon.fileformats.gnucash.GnucashJob;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type;

/**
 * created: 14.05.2005 <br/>
 * Implementation of GnucashJob that uses JWSDP.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 * @see biz.wolschon.fileformats.gnucash.GnucashJob
 */
public class GnucashJobImpl implements GnucashJob {


    /**
     * the JWSDP-object we are facading.
     */
    private GncV2Type.GncBookType.GncGncJobType jwsdpPeer;

    /**
     * The file we belong to.
     */
    private GnucashFile file;

    /**
     * @param peer the JWSDP-object we are facading.
     * @see #jwsdpPeer
     * @param gncFile the file to register under
     */
    public GnucashJobImpl(
            final GncV2Type.GncBookType.GncGncJobType peer,
            final GnucashFile gncFile) {
        super();
        this.jwsdpPeer = peer;
        this.file = gncFile;
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
    public GncV2Type.GncBookType.GncGncJobType getJwsdpPeer() {
        return jwsdpPeer;
    }


    /**
     * @return the unique-id to identify this object with across name- and hirarchy-changes
     */
    public String getId() {
        assert  jwsdpPeer.getJobGuid().getType().equals("guid");

        String guid = jwsdpPeer.getJobGuid().getValue();
        if (guid == null) {
            throw new IllegalStateException(
                    "job has a null guid-value! guid-type="
                   + jwsdpPeer.getJobGuid().getType());
        }

         return guid;
    }

    /**
     * @return all invoices that refer to this job.
     * @see GnucashJob#getInvoices()
     */
    public Collection getInvoices() {
        List retval = new LinkedList();
        for (GnucashInvoice invoice : getFile().getInvoices()) {
            if (invoice.getJobID().equals(getId())) {
                retval.add(invoice);
            }
        }

        return retval;
    }


    /**
     * @return true if the job is still active
     */
    public boolean isJobActive() {
        return getJwsdpPeer().getJobActive() == 1;
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashJob#getCustomerType()
     */
    public String getCustomerType() {
        return jwsdpPeer.getJobOwner().getOwnerType();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashJob#getCustomerId()
     */
    public String getCustomerId() {
        assert jwsdpPeer.getJobOwner().getOwnerId().getType().equals("guid");
        return jwsdpPeer.getJobOwner().getOwnerId().getValue();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashJob#getCustomer()
     */
    public GnucashCustomer getCustomer() {
        return file.getCustomerByID(getCustomerId());
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashJob#getJobNumber()
     */
    public String getJobNumber() {
     return jwsdpPeer.getJobId();
    }

    /**
     *
     * @see biz.wolschon.fileformats.gnucash.GnucashJob#getName()
     */
    public String getName() {
        return jwsdpPeer.getJobName();
    }
}
