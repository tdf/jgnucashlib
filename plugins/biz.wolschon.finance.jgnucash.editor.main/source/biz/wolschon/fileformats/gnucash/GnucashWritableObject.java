/**
 * GnucashWritableObject.java
 * created: 01.10.2005 13:30:08
 * (c) 2005 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 */
package biz.wolschon.fileformats.gnucash;

//other imports
import java.util.*;

//automatically created logger for debug and error -output
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashObject;

//automatically created propertyChangeListener-Support
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.xml.bind.JAXBException;


/**
 * (c) 2005 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: gnucashReader<br/>
 * GnucashWritableObject.java<br/>
 * created: 01.10.2005 13:30:08 <br/>
 *<br/><br/>
 * Interface that all interfaces for writable gnucash-entities shall implement
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public interface GnucashWritableObject {

    /**
     * @return the File we belong to.
     */
    GnucashWritableFile getWritableFile();

    /**
     * @param name the name of the user-defined attribute
     * @param value the value or null if not set
     * @throws JAXBException on problems with the xml-backend
     * @see {@link GnucashObject#getUserDefinedAttribute(String)}
     */
    void setUserDefinedAttribute(String name, String value) throws JAXBException;
}
