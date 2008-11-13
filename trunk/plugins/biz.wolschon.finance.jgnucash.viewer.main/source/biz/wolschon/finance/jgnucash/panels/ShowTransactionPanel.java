/**
 * TransactionsPanel.java
 * created: 21.10.2006 17:17:17
 */
package biz.wolschon.finance.jgnucash.panels;

//other imports
//automatically created logger for debug and error -output
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;
import javax.xml.bind.JAXBException;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.finance.jgnucash.swingModels.GnucashTransactionsSplitsTableModel;

/**
 * (c) 2006 by Wolschon Softwaredesign und Beratung.<br/>
 * Project: gnucashReader<br/>
 * TransactionsPanel.java<br/>
 * created: 21.10.2006 17:17:17 <br/>
 *<br/><br/>
 * <b>This Panel shows all splits of a single transaction.</b>
 * @author <a href="Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class ShowTransactionPanel extends JPanel {


	/**
	 * Automatically created logger for debug and error-output.
	 */
	private static final Log LOGGER = LogFactory.getLog(ShowTransactionPanel.class);

	/**
	 * for serializing.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The transaction we are showing.
	 */
	private GnucashTransaction myTransaction = null;


	/**
     * @param aTransaction The transaction we are showing.
     */
    public ShowTransactionPanel(final GnucashTransaction aTransaction) {
        super();
        this.myTransaction = aTransaction;

        initialize();
    }
    
    /**
     * initialize the Gui.
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.add(getTransactionTableScrollPane(), BorderLayout.CENTER);
        setTransaction(getTransaction());
    }

    /**
     * @param aTransaction The transaction we are showing.
     */
    public ShowTransactionPanel() {
        super();
        this.myTransaction = null;

        initialize();
    }

    /**
     * @return Returns the transaction.
     * @see #myTransaction
     */
    public GnucashTransaction getTransaction() {
        return this.myTransaction;
    }

    /**
     * @param aTransaction The transaction to set.
     * @see #myTransaction
     */
    public void setTransaction(final GnucashTransaction aTransaction) {
        
        Object old = this.myTransaction;
        if (old == aTransaction) {
            return; // nothing has changed
        }
        this.myTransaction = aTransaction;

        SingleTransactionTableModel model = null;

        if (aTransaction == null) {
            model = new SingleTransactionTableModel();
            setPreferredSize(new Dimension(0, 0));
            invalidate();
        } else {
            model = new SingleTransactionTableModel(aTransaction);
            setPreferredSize(new Dimension(200, 200));
            invalidate();
        }   
        setModel(model);
    }

//
//    /**
//	 * Give an account who's transactions to display.
//	 * @param account if null, an empty table will be shown.
//	 */
//	public void setAccount(final GnucashAccount account) {
//
//
//	    throw new IllegalStateException("not possible for ShowTransactionPanel");
//	}

	/**
     * The model of our ${@link #transactionTable}.
     */
    private GnucashTransactionsSplitsTableModel model;

    /**
     * The table showing the splits.
     */
    private JTable transactionTable;

    /**
     * My SCrollPane over {@link #transactionTable}.
     */
    private JScrollPane transactionTableScrollPane;


    /**
     * @return Returns the model.
     * @see #model
     */
    public GnucashTransactionsSplitsTableModel getModel() {
        return this.model;
    }

    /**
     * @param aModel The model to set.
     * @see #model
     */
    protected void setModel(final SingleTransactionTableModel aModel) {
        if (aModel == null) {
            throw new IllegalArgumentException("null 'aModel' given!");
        }
    
        Object old = this.model;
        if (old == aModel) {
            return; // nothing has changed
        }
        this.model = aModel;
        
        getTransactionTable().setModel(this.model);
        transactionTable.setAutoCreateRowSorter(false);
        // set column-width
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(transactionTable.getFont());
        
        
        
        transactionTable.getColumn("date").setPreferredWidth(SwingUtilities.computeStringWidth(metrics, SingleTransactionTableModel.dateFormat.format(new Date())) + 5);
        int currencyWidth = SwingUtilities.computeStringWidth(metrics, SingleTransactionTableModel.currencyFormat.format(10000));
        if (aModel.isMultiCurrency()) {
            currencyWidth = currencyWidth * 2 + 20;
        }
        transactionTable.getColumn("+").setPreferredWidth(currencyWidth);
        transactionTable.getColumn("-").setPreferredWidth(currencyWidth);
        transactionTable.getColumn("action").setPreferredWidth(SwingUtilities.computeStringWidth(metrics, "VERKAUF"));

        
        transactionTable.getColumn("date").setMaxWidth(SwingUtilities.computeStringWidth(metrics, SingleTransactionTableModel.dateFormat.format(new Date())) + 5);
        transactionTable.getColumn("+").setMaxWidth(currencyWidth);
        transactionTable.getColumn("-").setMaxWidth(currencyWidth);
        transactionTable.getColumn("action").setMaxWidth(SwingUtilities.computeStringWidth(metrics, "VERKAUF          "));
    }
    
    /**
     * This method initializes transactionTableScrollPane   
     *  
     * @return javax.swing.JScrollPane  
     */
    private JScrollPane getTransactionTableScrollPane() {
        if (transactionTableScrollPane == null) {
            transactionTableScrollPane = new JScrollPane();
            transactionTableScrollPane.setViewportView(getTransactionTable());
        }
        return transactionTableScrollPane;
    }

    /**
     * This method initializes transactionTable.
     *
     * @return javax.swing.JTable
     */
    protected JTable getTransactionTable() {
        if (transactionTable == null) {
            transactionTable = new JTable();
            setModel(new SingleTransactionTableModel());

        }
        return transactionTable;
    }
	

    protected static class SingleTransactionTableModel  implements GnucashTransactionsSplitsTableModel {


        /**
         * The transaction we are showing.
         */
        private GnucashTransaction myTransaction;

        /**
         * The columns we display.
         */
        private String[] defaultColumnNames = new String[] {"date", "action", "description", "account", "+", "-"};

        /**
         * How to format dates.
         */
        public static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        /**
         * How to format currencies.
         */
        public static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        /**
         * @param aAccount
         * @param aTransaction
         */
        public SingleTransactionTableModel(final GnucashTransaction aTransaction) {
            super();
            this.myTransaction = aTransaction;
        }
        public boolean isMultiCurrency() {
            if (getTransaction() == null) {
                return false;
            }
            try {
                for (GnucashTransactionSplit split : getTransaction().getSplits()) {
                    if (!split.getAccount().getCurrencyNameSpace().equals(getTransaction().getCurrencyNameSpace())
                          || !split.getAccount().getCurrencyID().equals(getTransaction().getCurrencyID())) {
                        return true;
                    }
                }
            } catch (JAXBException e) {
                LOGGER.error("[JAXBException] Problem in "
                           + getClass().getName(),
                             e);
                return true; // stay on the safe side
            }
            return false;
        }
        /**
         * @param aAccount
         * @param aTransaction
         */
        public SingleTransactionTableModel() {
            super();
            this.myTransaction = null;
        }

        /**
         * @return Returns the transaction.
         * @see #myTransaction
         */
        public GnucashTransaction getTransaction() {
            return this.myTransaction;
        }

        /**
         * @param aTransaction The transaction to set.
         * @see #myTransaction
         */
        public void setTransaction(final GnucashTransaction aTransaction) {
            if (aTransaction == null) {
                throw new IllegalArgumentException("null 'aTransaction' given!");
            }

            Object old = this.myTransaction;
            if (old == aTransaction) {
                return; // nothing has changed
            }
            this.myTransaction = aTransaction;
        }

        /** 
         * ${@inheritDoc}.
         */
        public GnucashTransactionSplit getTransactionSplit(int aRowIndex) {
            return getTransactionSplits().get(aRowIndex);
        }

        /** 
         * ${@inheritDoc}.
         */
        public List<GnucashTransactionSplit> getTransactionSplits() {
            GnucashTransaction transaction = getTransaction();
            if (transaction == null) {
                return new LinkedList<GnucashTransactionSplit>();
            }
            try {
                return new ArrayList<GnucashTransactionSplit>(transaction.getSplits());
            } catch (JAXBException e) {
                LOGGER.error("[JAXBException] Problem in "
                           + getClass().getName(),
                             e);
                return new LinkedList<GnucashTransactionSplit>();
            }
        }


        /**
         *
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return defaultColumnNames.length;
        }

        /**
         *
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {

            GnucashTransaction transaction = getTransaction();
            if(transaction == null) {
                return 0;
            }
            return 1 + getTransactionSplits().size();
        }

        /**
         *
         * @see javax.swing.table.TableModel#getColumnClass(int)
         */
        @SuppressWarnings("unchecked")
        public Class getColumnClass(int columnIndex) {
            return String.class;
        }

        /**
         *
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            // "date", "action", "description", "account", "+", "-"};
            try {
                if (rowIndex == 0) {
                    // show data of transaction
                    switch(columnIndex) {
                    case 0: { //DATE
                        return dateFormat.format(getTransaction().getDatePosted());
                    }
                    case 1: { //action == transaction-Number
                        String number = getTransaction().getTransactionNumber();
                        if(number== null || number.trim().length()==0)
                            return "";
                        return number;
                    }
                    case 2: { //description
                        String desc = getTransaction().getDescription();
                        if(desc== null || desc.trim().length()==0)
                            return "";
                        return desc;
                    }
                    case 3: { // account
                        return "";
                    }
                    case 4: { // +
                        return "";
                    }
                    case 5: { // -
                        return "";
                    }

                    default:
                        throw new IllegalArgumentException("illegal columnIndex "+columnIndex);
                    }
                }

                GnucashTransactionSplit split = getTransactionSplit(rowIndex - 1);

                switch(columnIndex) {
                case 0: { //DATE
                    return dateFormat.format(split.getTransaction().getDatePosted());
                }
                case 1: { //action
                    String action = split.getSplitAction();
                    if(action== null || action.trim().length()==0)
                        return "";
                    return action;
                }
                case 2: { //description
                    String desc = split.getDescription();
                    if(desc== null || desc.trim().length()==0)
                        return "";
                    return desc;
                }
                case 3: { // account
                    return split.getAccount().getQualifiedName();
                }
                case 4: { // +
                    if(split.getQuantity().isPositive()) {
                        if (split.getAccount().getCurrencyNameSpace().equals(getTransaction().getCurrencyNameSpace())
                           && split.getAccount().getCurrencyID().equals(getTransaction().getCurrencyID())) {
                            return split.getValueFormatet();
                        }
                        return split.getValueFormatet() + " (" + split.getQuantityFormatet() + ")";                        
                    } else return "";
                }
                case 5: { // -
                    if(!split.getQuantity().isPositive()) {
                        if (split.getAccount().getCurrencyNameSpace().equals(getTransaction().getCurrencyNameSpace())
                                && split.getAccount().getCurrencyID().equals(getTransaction().getCurrencyID())) {
                                 return split.getValueFormatet();
                             }
                             return split.getValueFormatet() + " (" + split.getQuantityFormatet() + ")";                        
                         } else return "";
                }

                default:
                    throw new IllegalArgumentException("illegal columnIndex "+columnIndex);
                }


            } catch(Exception x) {

                String message = "Internal Error in "
                    + getClass().getName() + ":getValueAt(int rowIndex="+
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
                return "ERROR";
            }
        }

        /**
         *
         * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
         */
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            // ignored, this model is read-only
        }

        /**
         *
         * @see javax.swing.table.TableModel#getColumnName(int)
         */
        public String getColumnName(final int columnIndex) {
            return defaultColumnNames[columnIndex]; //TODO: l10n
        }

        /**
         *
         * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
         */
        public void addTableModelListener(final TableModelListener l) {
         // ignored, this model is read-only
        }

        /**
         *
         * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
         */
        public void removeTableModelListener(final TableModelListener l) {
         // ignored, this model is read-only
        }

        /**
         * 
         * @param aRowIndex the row
         * @param aColumnIndex the column
         * @return false
         */
        public boolean isCellEditable(final int aRowIndex, final int aColumnIndex) {
            return false;
        }
    }
}
