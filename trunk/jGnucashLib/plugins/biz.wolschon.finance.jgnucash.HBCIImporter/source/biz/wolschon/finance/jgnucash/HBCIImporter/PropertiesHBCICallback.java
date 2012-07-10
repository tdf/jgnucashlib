/**
 * LdapUserHBCICallback.java
 * created: 26.12.2005 15:15:43
 * License: GPLv3 or later
 * (c) 2005 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 */
package biz.wolschon.finance.jgnucash.HBCIImporter;

//other imports
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kapott.hbci.passport.HBCIPassport;


/**
 * (c) 2005 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: Papierkram<br/>
 * LdapUserHBCICallback.java<br/>
 * created: 26.12.2005 15:15:43 <br/>
 *<br/><br/>
 * Callback needed to connect the HBCI-attributes on an LdapUser with
 * the hbci-library used.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class PropertiesHBCICallback implements org.kapott.hbci.callback.HBCICallback {


    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Log LOGGER = LogFactory.getLog(PropertiesHBCICallback.class);


    /**
     * The settings like account-number, pin-code, bank.url, ....
     *
     */
    private Properties myProperties;


    //------------------------ support for propertyChangeListeners ------------------


    /**
     * @param aProperties The settings like account-number, pin-code, bank.url, ....
     */
    public PropertiesHBCICallback(final Properties aProperties) {
        super();
        this.setProperties(aProperties);
    }

    /**
     * @return The settings like account-number, pin-code, bank.url, ....
     */
    public Properties getProperties() {
        return myProperties;
    }

    /**
     * @param aProperties The settings like account-number, pin-code, bank.url, ....
     */
    public void setProperties(final Properties aProperties) {
        myProperties = aProperties;
    }


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
    @Override
    public String toString() {
        return "LdapUserHBCICallback@" + hashCode();
    }



    /**
     * {@inheritDoc}
     */
    public void status(final HBCIPassport passport, final int statusTag, final Object o) {
        LOGGER.info("status: statusTag=" + statusTag + " o=" + o);

    }

    /**
     * {@inheritDoc}
     */
    public void status(final HBCIPassport passport, final int statusTag, final Object[] o) {
        LOGGER.info("status: statusTag=" + statusTag + " o[]=" + Arrays.toString(o));
    }

    /**
     * {@inheritDoc}
     */
    public void callback(final HBCIPassport passport,
            final int reason,
            final String msg,
            final int datatype,
            final StringBuffer retData) {
        LOGGER.info("callback: reason=" + reason
                + " msg=\"" + msg + "\" "
                + "datatype=" + datatype + " "
                + "retData=\"" + retData + "\"");

        switch (reason) {

        case NEED_COUNTRY:
            //if (msg.equals("Länderkennzeichen (DE für Deutschland)")) {
                String country = getProperties().getProperty(HBCIImporter.SETTINGS_COUNTRY, "DE");
                retData.setLength(0); // empty the buffer first
                retData.append(country);
                return;
            //}
        case NEED_PT_PIN:
            //if (msg.equals("Bitte geben Sie die PIN fÃ¼r das PIN/TAN-Verfahren ein")
            //      || msg.equals("Bitte geben Sie die PIN für das PIN/TAN-Verfahren ein")) {
                String pin = getProperties().getProperty(HBCIImporter.SETTINGS_PIN);
                if (pin == null || pin.trim().length() == 0 || pin.equalsIgnoreCase("(optional)")) {
                    pin = JOptionPane.showInputDialog("Your HBCI-PIN:");
                }
                retData.setLength(0); // empty the buffer first
                retData.append(pin);
                return;
            //}
        case NEED_PASSPHRASE_LOAD:
            //  Bitte geben Sie das neue Passwort für die Sicherung der Passport-Datei ein
            //if (msg.equals("Bitte geben Sie das Passwort fÃ¼r den Zugriff auf die Passport-Datei ein")
            //      || msg.equals("Bitte geben Sie das Passwort für den Zugriff auf die Passport-Datei ein")) {
                retData.setLength(0); // empty the buffer first
                retData.append("0000");
                return;
            //}
        case NEED_PASSPHRASE_SAVE:
            //if (msg.equals("Bitte geben Sie das neue Passwort fÃ¼r die Sicherung der Passport-Datei ein")
            //      || msg.equals("Bitte geben Sie das neue Passwort für die Sicherung der Passport-Datei ein")) {
                retData.setLength(0); // empty the buffer first
                retData.append("0000");
                return;
            //}
        case NEED_BLZ:
            //if (msg.equals("Bankleitzahl")) {
                retData.setLength(0); // empty the buffer first
                retData.append(getProperties().getProperty(HBCIImporter.SETTINGS_BANKCODE));
                return;
            //}
        case NEED_HOST:
            //if (msg.equals("Hostname/IP-Adresse")) {
                retData.setLength(0); // empty the buffer first
                String property = getProperties().getProperty(HBCIImporter.SETTINGS_SERVER);
                if (property.startsWith("https://")) {
                    property = property.substring("https://".length());
                }
                retData.append(property);
                return;
            //}
        case NEED_USERID:
            if (msg.equals("Nutzerkennung")) {
                retData.setLength(0); // empty the buffer first
                retData.append(getProperties().getProperty(HBCIImporter.SETTINGS_ACCOUNT));
                return;
            }
        case NEED_CUSTOMERID:
            if (msg.equals("Kunden-ID")) {
                //retData.setLength(0); // empty the buffer first
                //Feld soll freigelassen werden retData.replace(0, retData.length(), "90246243");
                return;
            }
        case NEED_CONNECTION: // Bitte stellen Sie jetzt die Verbindung zum Internet her
            return;
        case CLOSE_CONNECTION: // Sie können die Internetverbindung jetzt beenden
            return;
        case NEED_PT_SECMECH:
            // we don't really care about this but prefer iTan and Einschritt if possible
            // the fallback-case is enough to handle everything anyway.
            if (retData.toString().contains("900:iTAN")) {
                retData.setLength(0); // empty the buffer first
                retData.append("900");
                return;
            }
            if (retData.toString().contains("999:Einschritt")) {
                retData.setLength(0); // empty the buffer first
                retData.append("999");
                return;
            }
            retData.setLength("999".length()); // select whatever method comes first

        default:
            LOGGER.warn("callback: (unhandled) reason=" + reason + " msg=" + msg);

       }



    }



    /**
     * Log to log4j.
     * @see org.kapott.hbci.callback.HBCICallback#log(java.lang.String, int, java.util.Date, java.lang.StackTraceElement)
     * @param msg the message to log
     * @param level the log-level
     * @param date timestamp
     * @param trace optional stracktrace
     */
    public void log(final String msg, final int level, final Date date, final StackTraceElement trace) {
        if (level <= org.kapott.hbci.manager.HBCIUtils.LOG_ERR) {
            LOGGER.error(msg);
        } else if (level <= org.kapott.hbci.manager.HBCIUtils.LOG_WARN) {
            LOGGER.warn(msg);
        } else {
            LOGGER.info(msg);
        }
    }


    /**
     * {@inheritDoc}
     */
    public boolean useThreadedCallback(final HBCIPassport arg0,
                                       final int arg1,
                                       final String arg2,
                                       final int arg3,
                                       final StringBuffer arg4) {
        // Auto-generated method stub
        return false;
    }


}
