/**
 * SplitterTool.java
 * created: 29.11.2008
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jGnucashLib-private by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
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
package biz.wolschon.finance.jgnucash.splitter;


//automatically created logger for debug and error -output
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashFileWritingImpl;
import biz.wolschon.finance.jgnucash.plugin.ToolPlugin;
import biz.wolschon.numbers.FixedPointNumber;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib-private<br/>
 * SplitterTool.java<br/>
 * created: 29.11.2008<br/>
 *<br/><br/>
 * <b>Split a gnucash-file at a given data, arching old transactions into another file.</b>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class SplitterTool implements ToolPlugin {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(SplitterTool.class
            .getName());


    /**
     * Just an overridden ToString to return this classe's name
     * and hashCode.
     * @return className and hashCode
     */
    @Override
    public String toString() {
        return "SplitterTool@" + hashCode();
    }

    /**
     * ${@inheritDoc}.
     */
    @Override
    public String runTool(final GnucashWritableFile aWritableModel,
                            final GnucashWritableAccount aCurrentAccount) throws Exception {

        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(final File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.isFile() && !f.exists();
            }

            @Override
            public String getDescription() {
                return "OSCommerce-XML-file";
            }
        });
        int returnCode = fc.showSaveDialog(null);
        if (returnCode != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File newFile = fc.getSelectedFile();
        if (newFile.exists()) {
            JOptionPane.showMessageDialog(null, "File must not yet exist.");
            return;
        }
        DateFormat localDateFormat = DateFormat.getDateInstance();
        GregorianCalendar lastFirstJanuary = new GregorianCalendar();
        lastFirstJanuary.set(Calendar.MONTH, Calendar.JANUARY);
        lastFirstJanuary.set(Calendar.DAY_OF_YEAR, 0);
        lastFirstJanuary.add(Calendar.DAY_OF_YEAR, -1);
        Date splitDate = null;
        while (true) {
            try {
                String inputDate = JOptionPane.showInputDialog(null, "Please enter the date to split at", localDateFormat.format(lastFirstJanuary.getTime()));
                splitDate = localDateFormat.parse(inputDate);
                if (splitDate != null) {
                    break;
                }
            } catch (ParseException x) {
                JOptionPane.showMessageDialog(null, "unparsable date.");
            }
        }
        // we cannot clone in memory, thus create a 1:1-copy first
        // and then remove transactions.
        aWritableModel.writeFile(newFile);
        GnucashWritableFile newModel = new GnucashFileWritingImpl(newFile);
        for (GnucashTransaction transaction : newModel.getTransactions()) {
            if (transaction.getDatePosted().after(splitDate)) {
                newModel.removeTransaction(transaction);
            }
        }
        newModel.writeFile(newFile);
        
        // get Balance for all accounts in newModel
        // and insert a split in the current model to get these balances
        GnucashWritableTransaction balanceTransaction = aWritableModel.createWritableTransaction();
        for (GnucashAccount newAccount : newModel.getAccounts()) {
            FixedPointNumber balance = newAccount.getBalance();
            GnucashAccount account = aWritableModel.getAccountByID(account.getId());
            GnucashWritableTransactionSplit split = balanceTransaction.createWritingSplit(account);
            split.setQuantity(balance);
        }
        // balance the transaction
        FixedPointNumber balance = balanceTransaction.getNegatedBalance();
        GnucashWritableTransactionSplit split = balanceTransaction.createWritingSplit(aCurrentAccount);
        split.setValue(balance);
    
        // remove all old Transactions from the existing file
        for (GnucashTransaction transaction : newModel.getTransactions()) {
            GnucashTransaction removeMe = aWritableModel.getTransactionByID(transaction.getId());
            aWritableModel.removeTransaction(removeMe);
        }

        return "";
    }

}


