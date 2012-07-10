/**
 * GnucashObject.java
 * License: GPLv3 or later
 * created: 01.10.2005 13:43:20
 * (c) 2005 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 */
package biz.wolschon.fileformats.gnucash.jwsdpimpl;

//other imports
import java.util.*;

//automatically created logger for debug and error -output
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import biz.wolschon.fileformats.gnucash.GnucashFile;

//automatically created propertyChangeListener-Support
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * (c) 2005 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: gnucashReader<br/>
 * GnucashObject.java<br/>
 * created: 01.10.2005 13:43:20 <br/>
 *<br/><br/>
 * Interface all gnucash-entities implement.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public interface GnucashObject {

    /**
     * @param name the name of the user-defined attribute
     * @return the value or null if not set
     */
    String getUserDefinedAttribute(String name);
    
    /**
     * 
     * @return all keys that can be used with ${@link #getUserDefinedAttribute(String)}}.
     */
    Collection<String> getUserDefinedAttributeKeys();

    /**
     * @return the File we belong to.
     */
    GnucashFile getGnucashFile();

}
