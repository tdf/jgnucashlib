/**
 * GnucashWritableObjectHelper.java
 * created: 01.10.2005 13:31:21
 * (c) 2005 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 */
package biz.wolschon.fileformats.gnucash.jwsdpimpl;

//other imports
import java.util.*;

//automatically created logger for debug and error -output
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableObject;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.ObjectFactory;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.Slot;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType;

//automatically created propertyChangeListener-Support
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;


/**
 * (c) 2005 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: gnucashReader<br/>
 * GnucashWritableObjectHelper.java<br/>
 * created: 01.10.2005 13:31:21 <br/>
 *<br/><br/>
 * Interface all writable gnucash-entities implement.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class GnucashWritableObjectHelper implements GnucashWritableObject {

    /**
     *
     */
    private GnucashObjectImpl gnucashObject;

    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableObject#getFile()
     */
    public GnucashWritableFile getWritableFile() {
        return ((GnucashWritableObject) getGnucashObject()).getWritableFile();
    }
    
    /**
     * @see biz.wolschon.fileformats.gnucash.GnucashWritableObject#getFile()
     */
    public GnucashWritableFile getFile() {
        return ((GnucashWritableObject) getGnucashObject()).getWritableFile();
    }

    /**
     * @param gnucashObject
     */
    public GnucashWritableObjectHelper(final GnucashObjectImpl gnucashObject) {
        super();
        setGnucashObject(gnucashObject);
    }

    /**
     * @param name the name of the user-defined attribute
     * @param value the value or null if not set
     * @throws JAXBException on problems with the xml-backend
     * @see {@link GnucashObject#getUserDefinedAttribute(String)}
     */
    @SuppressWarnings("unchecked")
    public void setUserDefinedAttribute(final String name, final String value) throws JAXBException {

        List<Slot> slots = (List<Slot>) getGnucashObject().getSlots().getSlot();
        for (Slot slot : slots) {
            if (slot.getSlotKey().equals(name)) {
                slot.getSlotValue().getContent().clear();
                slot.getSlotValue().getContent().add(value);
                return;
            }
        }
        ObjectFactory objectFactory = new ObjectFactory();
        Slot newSlot = objectFactory.createSlot();
        newSlot.setSlotKey(name);
        SlotValueType newValue = objectFactory.createSlotTypeSlotValueType();
        newValue.setType("string");
        newValue.getContent().add(value);
        newSlot.setSlotValue(newValue);
        slots.add(newSlot);

        getFile().setModified(true);

    }


/**
 * Automatically created logger for debug and error-output.
 */
private static final Log LOGGER = LogFactory
        .getLog(GnucashWritableObjectHelper.class);

//------------------------ support for propertyChangeListeners ------------------

/**
 * support for firing PropertyChangeEvents.
 * (gets initialized only if we really have listeners)
 */
private volatile PropertyChangeSupport myPropertyChange = null;

/**
 * Returned value may be null if we never had listeners.
 * @return Our support for firing PropertyChangeEvents
 */
protected PropertyChangeSupport getPropertyChangeSupport() {
    return myPropertyChange;
}

/**
 * Add a PropertyChangeListener to the listener list.
 * The listener is registered for all properties.
 *
 * @param listener  The PropertyChangeListener to be added
 */
public final void addPropertyChangeListener(
                                            final PropertyChangeListener listener) {
    if (myPropertyChange == null) {
        myPropertyChange = new PropertyChangeSupport(this);
    }
    myPropertyChange.addPropertyChangeListener(listener);
}

/**
 * Add a PropertyChangeListener for a specific property.  The listener
 * will be invoked only when a call on firePropertyChange names that
 * specific property.
 *
 * @param propertyName  The name of the property to listen on.
 * @param listener  The PropertyChangeListener to be added
 */
public final void addPropertyChangeListener(
                                            final String propertyName,
                                            final PropertyChangeListener listener) {
    if (myPropertyChange == null) {
        myPropertyChange = new PropertyChangeSupport(this);
    }
    myPropertyChange.addPropertyChangeListener(propertyName, listener);
}

/**
 * Remove a PropertyChangeListener for a specific property.
 *
 * @param propertyName  The name of the property that was listened on.
 * @param listener  The PropertyChangeListener to be removed
 */
public final void removePropertyChangeListener(
                                               final String propertyName,
                                               final PropertyChangeListener listener) {
    if (myPropertyChange != null) {
        myPropertyChange.removePropertyChangeListener(propertyName, listener);
    }
}

/**
 * Remove a PropertyChangeListener from the listener list.
 * This removes a PropertyChangeListener that was registered
 * for all properties.
 *
 * @param listener  The PropertyChangeListener to be removed
 */
public synchronized void removePropertyChangeListener(
                                                      final PropertyChangeListener listener) {
    if (myPropertyChange != null) {
        myPropertyChange.removePropertyChangeListener(listener);
    }
}

//-------------------------------------------------------

/**
 * Just an overridden ToString to return this classe's name
 * and hashCode.
 * @return className and hashCode
 */
public String toString() {
    return "GnucashWritableObjectHelper@" + hashCode();
}

/**
 *
 */
public GnucashWritableObjectHelper() {
    super();
    // TODO implement constructor for GnucashWritableObjectHelper
}

/**
 * @return Returns the gnucashObject.
 * @see ${@link #gnucashObject}
 */
public GnucashObjectImpl getGnucashObject() {
    return gnucashObject;
}

/**
 * @param gnucashObject The gnucashObject to set.
 * @see ${@link #gnucashObject}
 */
public void setGnucashObject(final GnucashObjectImpl gnucashObject) {
    if (gnucashObject == null) {
        throw new IllegalArgumentException("null 'gnucashObject' given!");
    }

    Object old = this.gnucashObject;
    if (old == gnucashObject) {
        return; // nothing has changed
    }
    this.gnucashObject = gnucashObject;
    // <<insert code to react further to this change here
    PropertyChangeSupport propertyChangeFirer = getPropertyChangeSupport();
    if (propertyChangeFirer != null) {
        propertyChangeFirer.firePropertyChange("gnucashObject", old,
                gnucashObject);
    }
}

}