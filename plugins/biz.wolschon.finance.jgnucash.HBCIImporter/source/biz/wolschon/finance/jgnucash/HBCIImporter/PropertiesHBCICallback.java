/**
 * LdapUserHBCICallback.java
 * created: 26.12.2005 15:15:43
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
     * @param myProperties The settings like account-number, pin-code, bank.url, ....
     */
    public PropertiesHBCICallback(final Properties myProperties) {
        super();
        this.setProperties(myProperties);
    }

    /**
     * @return The settings like account-number, pin-code, bank.url, ....
     */
    public Properties getProperties() {
        return myProperties;
    }

    /**
     * @param myProperties The settings like account-number, pin-code, bank.url, ....
     */
    public void setProperties(Properties myProperties) {
        this.myProperties = myProperties;
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



    public void status(HBCIPassport passport, int statusTag, Object o) {
        LOGGER.info("status: statusTag=" + statusTag + " o=" + o);

    }

    public void status(final HBCIPassport passport, final int statusTag, final Object[] o) {
        LOGGER.info("status: statusTag=" + statusTag + " o[]=" + Arrays.toString(o));

    }

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

        case 7:
            //if (msg.equals("Länderkennzeichen (DE für Deutschland)")) {
                String country = getProperties().getProperty(HBCIImporter.SETTINGS_COUNTRY, "DE");
                retData.setLength(0); // empty the buffer first
                retData.append(country);
                return;
            //}
        case 16:
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
        case 21:
            //  Bitte geben Sie das neue Passwort für die Sicherung der Passport-Datei ein
            //if (msg.equals("Bitte geben Sie das Passwort fÃ¼r den Zugriff auf die Passport-Datei ein")
            //      || msg.equals("Bitte geben Sie das Passwort für den Zugriff auf die Passport-Datei ein")) {
                retData.setLength(0); // empty the buffer first
                retData.append("0000");
                return;
            //}
        case 22:
            //if (msg.equals("Bitte geben Sie das neue Passwort fÃ¼r die Sicherung der Passport-Datei ein")
            //      || msg.equals("Bitte geben Sie das neue Passwort für die Sicherung der Passport-Datei ein")) {
                retData.setLength(0); // empty the buffer first
                retData.append("0000");
                return;
            //}
        case 8:
            //if (msg.equals("Bankleitzahl")) {
                retData.setLength(0); // empty the buffer first
                retData.append(getProperties().getProperty(HBCIImporter.SETTINGS_BANKCODE));
                return;
            //}
        case 9:
            //if (msg.equals("Hostname/IP-Adresse")) {
                retData.setLength(0); // empty the buffer first
                retData.append(getProperties().getProperty(HBCIImporter.SETTINGS_SERVER));
                return;
            //}
        case 11:
            if (msg.equals("Nutzerkennung")) {
                retData.setLength(0); // empty the buffer first
                retData.append(getProperties().getProperty(HBCIImporter.SETTINGS_ACCOUNT));
                return;
            }
        case 18:
            if (msg.equals("Kunden-ID")) {
                //retData.setLength(0); // empty the buffer first
                //Feld soll freigelassen werden retData.replace(0, retData.length(), "90246243");
                return;
            }
        case 24: // Bitte stellen Sie jetzt die Verbindung zum Internet her
            return;
        case 25: // Sie können die Internetverbindung jetzt beenden
            return;
        case 27: {
            if (retData.toString().contains("900:iTAN-Verfahren")) {
                retData.setLength(0); // empty the buffer first
                retData.append("900");
                return;
            }
            if (retData.toString().contains("999:Einschritt-Verfahren")) {
                retData.setLength(0); // empty the buffer first
                retData.append("999");
                return;
            }

           }
        }



        LOGGER.warn("callback: (unhandled) reason=" + reason + " msg=" + msg);
    }



    /**
     * Log to log4j.
     * @see org.kapott.hbci.callback.HBCICallback#log(java.lang.String, int, java.util.Date, java.lang.StackTraceElement)
     */
    public void log(final String msg, final int level, final Date date, final StackTraceElement trace) {
        LOGGER.info(msg);

    }


    public boolean useThreadedCallback(HBCIPassport arg0, int arg1, String arg2,
            int arg3, StringBuffer arg4) {
        // Auto-generated method stub
        return false;
    }


}
