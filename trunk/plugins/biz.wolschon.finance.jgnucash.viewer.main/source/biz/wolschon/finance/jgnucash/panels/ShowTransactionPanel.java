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
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import biz.wolschon.fileformats.gnucash.GnucashTransaction;
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
    static final Log LOGGER = LogFactory.getLog(ShowTransactionPanel.class);

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
        myTransaction = aTransaction;

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
     * make us visible.
     */
    public ShowTransactionPanel() {
        super();
        myTransaction = null;

        initialize();
    }

    /**
     * @return Returns the transaction.
     * @see #myTransaction
     */
    public GnucashTransaction getTransaction() {
        return myTransaction;
    }

    /**
     * @param aTransaction The transaction to set.
     * @see #myTransaction
     */
    public void setTransaction(final GnucashTransaction aTransaction) {

        Object old = myTransaction;
        if (old == aTransaction) {
            return; // nothing has changed
        }
        myTransaction = aTransaction;

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
        return model;
    }

    /**
     * @param aModel The model to set.
     * @see #model
     */
    protected void setModel(final SingleTransactionTableModel aModel) {
        if (aModel == null) {
            throw new IllegalArgumentException("null 'aModel' given!");
        }

        Object old = model;
        if (old == aModel) {
            return; // nothing has changed
        }
        model = aModel;

        getTransactionTable().setModel(model);
        transactionTable.setAutoCreateRowSorter(false);
        // set column-width
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(transactionTable.getFont());



        final int extraDateWidth = 5;
        transactionTable.getColumn("date").setPreferredWidth(SwingUtilities.computeStringWidth(metrics,
                                                             SingleTransactionTableModel.DATEFORMAT.format(new Date())) + extraDateWidth);

        final int maxAnticipatedValue = 10000;
        int currencyWidth = SwingUtilities.computeStringWidth(metrics, SingleTransactionTableModel.DEFAULTCURRENCYFORMAT.format(maxAnticipatedValue));

        if (aModel.isMultiCurrency()) {
            final int extraWidth = 20;
            currencyWidth = currencyWidth * 2 + extraWidth;
        }
        transactionTable.getColumn("+").setPreferredWidth(currencyWidth);
        transactionTable.getColumn("-").setPreferredWidth(currencyWidth);
        transactionTable.getColumn("action").setPreferredWidth(SwingUtilities.computeStringWidth(metrics, "VERKAUF"));


        transactionTable.getColumn("date").setMaxWidth(SwingUtilities.computeStringWidth(metrics, SingleTransactionTableModel.DATEFORMAT.format(new Date())) + 5);
        transactionTable.getColumn("+").setMaxWidth(currencyWidth);
        transactionTable.getColumn("-").setMaxWidth(currencyWidth);
        transactionTable.getColumn("action").setMaxWidth(SwingUtilities.computeStringWidth(metrics, "VERKAUF          "));
    }

    /**
     * This method initializes transactionTableScrollPane.
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
}
