/**
 * ShowWritableTransactionPanel.java
 * created: 21.09.2008 07:27:37
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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

//automatically created propertyChangeListener-Support
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.xml.bind.JAXBException;


import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit;
import biz.wolschon.numbers.FixedPointNumber;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-V1<br/>
 * ShowWritableTransactionPanel.java<br/>
 * created: 21.09.2008 07:27:37 <br/>
 *<br/><br/>
 * <b>This is a variant of {@link ShowTransactionPanel} that also allows
 * to edit the transaction.</b>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class ShowWritableTransactionPanel extends ShowTransactionPanel {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger
            .getLogger(ShowWritableTransactionPanel.class.getName());

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
            myPropertyChange.removePropertyChangeListener(propertyName,
                    listener);
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
        return "ShowWritableTransactionPanel@" + hashCode();
    }

    /**
     * @param aTransaction The transaction to set.
     * @see #myTransaction
     */
    public void setTransaction(final GnucashTransaction aTransaction) {

        Object old = getTransaction();
        if (old == aTransaction) {
            return; // nothing has changed
        }
        super.setTransaction(aTransaction);

        SingleTransactionTableModel model = null;

        if (aTransaction == null) {
            model = new SingleWritableTransactionTableModel();
            setPreferredSize(new Dimension(0, 0));
            invalidate();
        } else {
            model = new SingleWritableTransactionTableModel(aTransaction);
            setPreferredSize(new Dimension(200, 200));
            invalidate();
        }
        setModel(model);
    }

    /**
     * This method initializes transactionTable.
     *
     * @return javax.swing.JTable
     */
    protected JTable getTransactionTable() {
        JTable transactionTable = super.getTransactionTable();
        if (!(transactionTable.getModel() instanceof SingleWritableTransactionTableModel)) {
            transactionTable.setModel(new SingleWritableTransactionTableModel());
        }
        return transactionTable;
    }

    /**
     * @param aModel The model to set.
     * @see #model
     */
    protected void setModel(final SingleTransactionTableModel aModel) {
        super.setModel(aModel);

        // if editing is possible, install a jcomboBox as an editor for the accounts
        if (aModel != null && aModel instanceof SingleWritableTransactionTableModel) {
            GnucashTransaction transaction = aModel.getTransaction();
            JComboBox accountsCombo = new JComboBox() {

                /**
                 * ${@inheritDoc}.
                 */
                @Override
                public String getToolTipText() {
                    Object selectedItem = getSelectedItem();
                    if (selectedItem != null) {
                        return selectedItem.toString();
                    }
                    return super.getToolTipText();
                }
            };
            accountsCombo.setToolTipText("Account-name"); //make sure a tooltip-manager exists
            if (transaction != null) {
                Collection<GnucashAccount> accounts = transaction.getFile().getAccounts();
                for (GnucashAccount gnucashAccount : accounts) {
                    accountsCombo.addItem(gnucashAccount.getQualifiedName());
                }
            }

//            getTransactionTable().getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField()));
//            getTransactionTable().getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()));
//            getTransactionTable().getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));
//            getTransactionTable().getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(accountsCombo));
//            getTransactionTable().getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JTextField()));
//            getTransactionTable().getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JTextField()));
            getTransactionTable().getColumn("account").setCellEditor(new DefaultCellEditor(accountsCombo));
        }
    }

    /**
     * 
     * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
     * Project: jgnucashLib-V1<br/>
     * ShowWritableTransactionPanel.java<br/>
     * created: 21.09.2008 07:29:43 <br/>
     *<br/><br/>
     * <b>Extended version of the {@link SingleTransactionTableModel} that
     * allows changing the displayed transaction</a>
     * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
     */
    protected static class SingleWritableTransactionTableModel extends SingleTransactionTableModel {

        /** 
         * ${@inheritDoc}.
         */
        @Override
        public int getRowCount() {
            // add one row for adding a new split
            return super.getRowCount() + 1;
        }

        /** 
         * ${@inheritDoc}.
         */
        @Override
        public Object getValueAt(final int aRowIndex, final int aColumnIndex) {
            if (aRowIndex == getRowCount() - 1) {
             // add one row for adding a new split
                return "";
            }
            return super.getValueAt(aRowIndex, aColumnIndex);
        }

        /**
         *
         * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
         */
        private Set<TableModelListener> myTableModelListeners = new HashSet<TableModelListener>();

        /**
         * @param aTransaction the transaction we are displaying.
         */
        public SingleWritableTransactionTableModel(
                final GnucashTransaction aTransaction) {
            super(aTransaction);
        }

        /**
         * Create an empty model.
         */
        public SingleWritableTransactionTableModel() {
        }

        /**
         * @return Returns the transaction.
         * @see #myTransaction
         */
        public GnucashWritableTransaction getWritableTransaction() {
            return (GnucashWritableTransaction) super.getTransaction();
        }

        /**
         * @param aTransaction The transaction to set.
         * @see #myTransaction
         */
        public void setTransaction(final GnucashTransaction aTransaction) {
            if (! (aTransaction instanceof GnucashWritableTransaction)) {
                throw new IllegalArgumentException("only writable transactions allowed."
                        + " Please use ShowTransactionPanel for non-writable Transactions.");
            }
            super.setTransaction(aTransaction);
        }
        /**
         * @param aTransaction The transaction to set.
         * @see #myTransaction
         */
        public void setWritableTransaction(final GnucashWritableTransaction aTransaction) {
            super.setTransaction(aTransaction);
        }

        /** 
         * @param aRowIndex the split to return (starts with 0).
         * @return the selected split of the transaction.
         */
        public GnucashWritableTransactionSplit getWritableTransactionSplit(final int aRowIndex) {
            return getWritableTransactionSplits().get(aRowIndex);
        }

        /** 
         * @return all splits of the transaction.
         */
        public List<GnucashWritableTransactionSplit> getWritableTransactionSplits() {
            GnucashWritableTransaction transaction = getWritableTransaction();
            if (transaction == null) {
                return new LinkedList<GnucashWritableTransactionSplit>();
            }
            try {
                return new ArrayList<GnucashWritableTransactionSplit>(transaction.getWritingSplits());
            } catch (JAXBException e) {
                LOG.log(Level.SEVERE, "[JAXBException] Problem in "
                           + getClass().getName(),
                             e);
                return new LinkedList<GnucashWritableTransactionSplit>();
            }
        }

        /**
        *
        * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
        */
       public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
           if (aValue == null) {
               return;
           }
           // "date", "action", "description", "account", "+", "-"};
           try {
               if (rowIndex == 0) {
                   // show data of transaction
                   switch(columnIndex) {
                   case 0: try {
						       getWritableTransaction().setDatePosted(dateFormat.parse(aValue.toString()));
						       for (TableModelListener listener : this.myTableModelListeners) {
						           listener.tableChanged(new TableModelEvent(this));
						       }
						   } catch (ParseException e) {
						       // ignore wrong dates
						   }
						return;
					case 1: getWritableTransaction().setTransactionNumber(aValue.toString());
						return;
					case 2: getWritableTransaction().setDescription(aValue.toString());
						return;
					case 3: return;
					case 4: return;
					case 5: return;
					default:
                       throw new IllegalArgumentException("illegal columnIndex "+columnIndex);
                   }
               }

               GnucashWritableTransactionSplit split = null;
               boolean informListeners = false;
               if (rowIndex == getRowCount() - 1) {
                // add one row for adding a new split
                   if (aValue.toString().trim().length() > 0) {
                       GnucashWritableTransaction writableTransaction = getWritableTransaction();
                       split = getWritableTransaction().createWritingSplit(getBalancingAccount(writableTransaction));
                       informListeners = true;
                   } else {
                       return;
                   }
               } else {
                   split = getWritableTransactionSplit(rowIndex - 1);
               }
               if (split == null) {
                   LOG.log(Level.SEVERE, "Split is null in setValue(row, col) - this should not be possible");
                   return;
               }

               switch(columnIndex) {
               case 0: return;
				case 1: split.setSplitAction(aValue.toString());
					if (informListeners) {
					       for (TableModelListener listener : this.myTableModelListeners) {
					           listener.tableChanged(new TableModelEvent(this));
					       }
					   }
					return;
				case 2: split.setDescription(aValue.toString());
					if (informListeners) {
					       for (TableModelListener listener : this.myTableModelListeners) {
					           listener.tableChanged(new TableModelEvent(this));
					       }
					   }
					return;
				case 3: if (aValue.toString().trim().length() == 0
					       && split.getQuantity().equals(new FixedPointNumber())
					       && split.getValue().equals(new FixedPointNumber())
					       && split.getDescription().trim().length() == 0
					       && split.getSplitAction().trim().length() == 0) {
					       //remove split
					       split.remove();
					       for (TableModelListener listener : this.myTableModelListeners) {
					           listener.tableChanged(new TableModelEvent(this));
					       }
					       return;
					   }
					try {
					       GnucashAccount account = getTransaction().getFile().getAccountByIDorNameEx(aValue.toString(), aValue.toString());
					       if (account != null) {
					           split.setAccount(account);
					       }
					   } catch (Exception e) {
					       LOG.log(Level.SEVERE,"[Exception] Problem in "
					               + getClass().getName(),
					               e);
					   }
					if (informListeners) {
					       for (TableModelListener listener : this.myTableModelListeners) {
					           listener.tableChanged(new TableModelEvent(this));
					       }
					   }
					return;
				case 4: // retain the value in the "-" -field to sum both
					   try {
					       //TODO: Parse the format "<value> (<quantity>)" too
					       if(!split.getQuantity().isPositive()) {
					           FixedPointNumber add = split.getQuantity();
					           split.setQuantity(aValue.toString());
					           split.setQuantity(add.add(split.getQuantity()));
					       } else {
					           split.setQuantity(aValue.toString());
					       }
					       if (!split.getTransaction().isBalanced()) {
					           balanceTransaction();
					       }
					   } catch (Exception e) {
					       LOG.log(Level.SEVERE,"[Exception] Problem in "
					               + getClass().getName(),
					               e);
					   }
					if (informListeners) {
					       for (TableModelListener listener : this.myTableModelListeners) {
					           listener.tableChanged(new TableModelEvent(this));
					       }
					   }
					return;
				case 5: // retain the value in the "-" -field to sum both
					   try {
					       //TODO: Parse the format "<value> (<quantity>)" too
					       if(split.getQuantity().isPositive()) {
					           FixedPointNumber add = split.getQuantity();
					           split.setQuantity(aValue.toString());
					           split.setQuantity(split.getQuantity().negate());
					           split.setQuantity(add.add(split.getQuantity()));
					       } else {
					           split.setQuantity(aValue.toString());
					           split.setQuantity(split.getQuantity().negate());
					       }
					       if (!split.getTransaction().isBalanced()) {
					           balanceTransaction();
					       }
					   } catch (Exception e) {
					       LOG.log(Level.SEVERE,"[Exception] Problem in "
					               + getClass().getName(),
					               e);
					   }
					if (informListeners) {
					       for (TableModelListener listener : this.myTableModelListeners) {
					           listener.tableChanged(new TableModelEvent(this));
					       }
					   }
					return;
				default:
                   throw new IllegalArgumentException("illegal columnIndex "+columnIndex);
               }


           } catch(Exception x) {

               String message = "Internal Error in "
                   + getClass().getName() + ":setValueAt(int rowIndex="+
                   + rowIndex
                   + ", int columnIndex="
                   + columnIndex
                   + ")!\n"
                   + "Exception of Type [" + x.getClass().getName() + "]\n"
                   + "\"" + x.getMessage() + "\"";
               StringWriter trace = new StringWriter();
               PrintWriter pw = new PrintWriter(trace);
               x.printStackTrace(pw);
               pw.close();
               message += trace.getBuffer();

               System.err.println(message);
               JOptionPane.showMessageDialog(null, message);
               return;
           }
       }

       /**
        * @throws JAXBException 
        * 
        */
       protected void balanceTransaction() throws JAXBException {
           GnucashWritableTransaction transaction = getWritableTransaction();
           if (transaction.isBalanced()) {
               return;
           }
           FixedPointNumber negatedBalance = transaction.getNegatedBalance();
           GnucashAccount balancingAccount = getBalancingAccount(transaction);
           int i=1;
           for (GnucashWritableTransactionSplit split : transaction.getWritingSplits()) {
               i++;
               if (split.getAccountID().equals(balancingAccount.getId())) {
                   split.setQuantity(split.getQuantity().add(negatedBalance));

                   for (TableModelListener listener : this.myTableModelListeners) {
                       listener.tableChanged(new TableModelEvent(this, i));
                   }
                   return;
               }
           }
           transaction.createWritingSplit(balancingAccount).setQuantity(negatedBalance);
           for (TableModelListener listener : this.myTableModelListeners) {
               listener.tableChanged(new TableModelEvent(this, getRowCount() - 1));
           }
       }

       /**
        * @param transaction
        * @return
        */
       private GnucashAccount getBalancingAccount(
                                                  final GnucashWritableTransaction transaction) {
           return transaction.getFile().getAccountByIDorName("c3e524eccc4e66cde2dc8eb3666ff469", "Ausgleichskonto-EUR");
       }

       /**
        *
        * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
        */
       public void addTableModelListener(final TableModelListener l) {
           myTableModelListeners.add(l);
       }

       /**
        *
        * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
        */
       public void removeTableModelListener(final TableModelListener l) {
           myTableModelListeners.remove(l);
       }

       /**
        * @param aRowIndex the row
        * @param aColumnIndex the column
        * @return true for most columns
        */
       public boolean isCellEditable(final int aRowIndex, final int aColumnIndex) {
           if (aRowIndex == 0) {
               // show data of transaction
               switch(aColumnIndex) {
               case 0: return true;
				case 1: return true;
				case 2: return true;
				case 3: return false;
				case 4: return false;
				case 5: return false;
				default:
                   throw new IllegalArgumentException("illegal columnIndex " + aColumnIndex);
               }
           }
           return aColumnIndex > 0; // date can only be edited on the transaction. Not on the splits.
       }

    }
}


