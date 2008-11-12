/**
 * WritableTransactionsPanel.java
 * created: 21.09.2008 07:49:55
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jgnucashLib-V1 by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 *  jgnucashLib-V1 is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jgnucashLib-V1 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jgnucashLib-V1.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************
 * Editing this file:
 *  -For consistent code-quality this file should be checked with the
 *   checkstyle-ruleset enclosed in this project.
 *  -After the design of this file has settled it should get it's own
 *   JUnit-Test that shall be executed regularly. It is best to write
 *   the test-case BEFORE writing this class and to run it on every build
 *   as a regression-test.
 */
package biz.wolschon.finance.jgnucash.panels;

//other imports

//automatically created logger for debug and error -output
import java.util.logging.Logger;

import biz.wolschon.fileformats.gnucash.GnucashAccount;

//automatically created propertyChangeListener-Support
//import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeSupport;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-V1<br/>
 * WritableTransactionsPanel.java<br/>
 * created: 21.09.2008 07:49:55 <br/>
 *<br/><br/>
 * <b>Variant of TransactionsPanel that allows editing the transactions.</a>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class WritableTransactionsPanel extends TransactionsPanel {

    /**
     *
     */
    public WritableTransactionsPanel() {
        super();
    }

    /**
     * @param account if null, an empty table will be shown.
     */
     public WritableTransactionsPanel(final GnucashAccount account) {
         super();
         setAccount(account);
         //TODO: add context-menu
     }


    //TODO: override tableModel to allow editing by the user of transaction-descriptions+dates and split-values for transactions with exactly 2 splits.
    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger
            .getLogger(WritableTransactionsPanel.class.getName());


    /**
     * The panel to show a single transaction.
     */
    private ShowWritableTransactionPanel mySingleWritableTransactionPanel;

    /**
     * Just an overridden ToString to return this classe's name
     * and hashCode.
     * @return className and hashCode
     */
    @Override
    public String toString() {
        return "WritableTransactionsPanel@" + hashCode();
    }

    /**
     * This method initializes ShowTransactionPanel.
     *
     * @return javax.swing.JPanel
     */

    @Override
    protected ShowTransactionPanel getSingleTransactionPanel() {
        return getSingleWritableTransactionPanel();
    }

    /**
     * This method initializes ShowTransactionPanel.
     *
     * @return javax.swing.JPanel
     */
    protected ShowTransactionPanel getSingleWritableTransactionPanel() {
        if (mySingleWritableTransactionPanel == null) {
            mySingleWritableTransactionPanel = new ShowWritableTransactionPanel();
        }
        return mySingleWritableTransactionPanel;
    }
//
//    //------------------------ support for propertyChangeListeners ------------------
//
//    /**
//     * support for firing PropertyChangeEvents.
//     * (gets initialized only if we really have listeners)
//     */
//    private volatile PropertyChangeSupport myPropertyChange = null;
//
//    /**
//     * Returned value may be null if we never had listeners.
//     * @return Our support for firing PropertyChangeEvents
//     */
//    protected PropertyChangeSupport getPropertyChangeSupport() {
//        return myPropertyChange;
//    }
//
//    /**
//     * Add a PropertyChangeListener to the listener list.
//     * The listener is registered for all properties.
//     *
//     * @param listener  The PropertyChangeListener to be added
//     */
//    public final void addPropertyChangeListener(
//                                                final PropertyChangeListener listener) {
//        if (myPropertyChange == null) {
//            myPropertyChange = new PropertyChangeSupport(this);
//        }
//        myPropertyChange.addPropertyChangeListener(listener);
//    }
//
//    /**
//     * Add a PropertyChangeListener for a specific property.  The listener
//     * will be invoked only when a call on firePropertyChange names that
//     * specific property.
//     *
//     * @param propertyName  The name of the property to listen on.
//     * @param listener  The PropertyChangeListener to be added
//     */
//    public final void addPropertyChangeListener(
//                                                final String propertyName,
//                                                final PropertyChangeListener listener) {
//        if (myPropertyChange == null) {
//            myPropertyChange = new PropertyChangeSupport(this);
//        }
//        myPropertyChange.addPropertyChangeListener(propertyName, listener);
//    }
//
//    /**
//     * Remove a PropertyChangeListener for a specific property.
//     *
//     * @param propertyName  The name of the property that was listened on.
//     * @param listener  The PropertyChangeListener to be removed
//     */
//    public final void removePropertyChangeListener(
//                                                   final String propertyName,
//                                                   final PropertyChangeListener listener) {
//        if (myPropertyChange != null) {
//            myPropertyChange.removePropertyChangeListener(propertyName,
//                    listener);
//        }
//    }
//
//    /**
//     * Remove a PropertyChangeListener from the listener list.
//     * This removes a PropertyChangeListener that was registered
//     * for all properties.
//     *
//     * @param listener  The PropertyChangeListener to be removed
//     */
//    public synchronized void removePropertyChangeListener(
//                                                          final PropertyChangeListener listener) {
//        if (myPropertyChange != null) {
//            myPropertyChange.removePropertyChangeListener(listener);
//        }
//    }
//
//    //-------------------------------------------------------

}


