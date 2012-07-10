/**
 * SearchTool.java
 * created: 12.10.2009
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jGnucashLib-private by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 * Otherwise, this code is made available under GPLv3 or later.
 *
 * ***********************************
 * Editing this file:
 *  -For consistent code-quality this file should be checked with the
 *   checkstyle-ruleset enclosed in this project.
 *  -After the design of this file has settled it should get it's own
 *   JUnit-Test that shall be executed regularly. It is best to write
 *   the test-case BEFORE writing this class and to run it on every build
 *   as a regression-test.
 */
package biz.wolschon.finance.jgnucash.search;


//automatically created logger for debug and error -output
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.panels.WritableTransactionsPanel;
import biz.wolschon.finance.jgnucash.plugin.ToolPlugin;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib-private<br/>
 * SearchTool.java<br/>
 * created: 12.10.2009<br/>
 *<br/><br/>
 * <b>Split a gnucash-file at a given data, arching old transactions into another file.</b>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class SearchTool implements ToolPlugin {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(SearchTool.class
            .getName());


    /**
     * Just an overridden ToString to return this classe's name
     * and hashCode.
     * @return className and hashCode
     */
    @Override
    public String toString() {
        return "SearchTool@" + hashCode();
    }

    /**
     * ${@inheritDoc}.
     */
    @Override
    public String runTool(final GnucashWritableFile aWritableModel,
                            final GnucashWritableAccount aCurrentAccount) {

        //TODO: l10n
        String term = JOptionPane.showInputDialog("search for:");
        Collection<? extends GnucashTransaction> transactions = aWritableModel.getTransactions();
        List<GnucashTransactionSplit> found = new LinkedList<GnucashTransactionSplit>();
        for (GnucashTransaction gnucashTransaction : transactions) {
            try {
                if (matches(term, gnucashTransaction.getDescription())
                        || matches(term, gnucashTransaction.getTransactionNumber())) {
                    found.add(gnucashTransaction.getFirstSplit());
                } else {
                    List<? extends GnucashTransactionSplit> splits = gnucashTransaction.getSplits();
                    for (GnucashTransactionSplit gnucashTransactionSplit : splits) {
                        try {
                            if (matches(term, gnucashTransactionSplit.getDescription())
                                    || matches(term, gnucashTransactionSplit.getSplitAction())) {
                                found.add(gnucashTransactionSplit);
                                break;
                            }
                        } catch (Exception e) {
                            LOG.log(Level.SEVERE, "Cannot search transaction-split of transaction " + gnucashTransaction.getId(), e);
                        }
                    }
                }
            } catch (JAXBException e) {
                LOG.log(Level.SEVERE, "Cannot search a transaction", e);
            }
        }

        showResult(found, term);

        return "";
    }

    /**
     * Display a frame with the found transactions.
     * @param aFound the transactions found
     * @param aTerm the search-term
     */
    private void showResult(final List<GnucashTransactionSplit> aFound, final String aTerm) {
        WritableTransactionsPanel newPanel = new WritableTransactionsPanel();
        newPanel.setDisplayedTransactions(aFound);
        JFrame newFrame = new JFrame("found for: " + aTerm); //TODO: l10n
        newFrame.getContentPane().add(newPanel);
        newFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        newFrame.pack();
        newFrame.setVisible(true);
    }

    /**
     * Does the search-string  match the given
     * text?
     * @param aTerm the search-string
     * @param aDescription the text
     * @return true or false
     */
    private boolean matches(final String aTerm, final String aDescription) {
        if (aDescription == null) {
            return aTerm.length() == 0;
        }
        return aDescription.contains(aTerm) || aDescription.matches(aTerm);
    }

    /**
     * @param aWritableModel
     * @param newFile
     * @return
     * @throws IOException
     * @throws JAXBException
     */
    private String runTool(final GnucashWritableFile aWritableModel,
                           final GnucashWritableAccount aCurrentAccount,
                           final File newFile) throws IOException, JAXBException {

        return runTool(aWritableModel, aCurrentAccount);
    }
}


