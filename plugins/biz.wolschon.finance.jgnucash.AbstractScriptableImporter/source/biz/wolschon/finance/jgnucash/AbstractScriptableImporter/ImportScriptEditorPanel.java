/**
 * ImportScriptEditorPanel.java
 * created: 09.10.2008 06:58:35
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jGnucashLib-GPL by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 *  jGnucashLib-GPL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jGnucashLib-GPL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jGnucashLib-GPL.  If not, see <http://www.gnu.org/licenses/>.
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
package biz.wolschon.finance.jgnucash.AbstractScriptableImporter;

//other imports
import java.io.File;
import java.io.StringReader;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit;
import biz.wolschon.finance.jgnucash.AbstractScriptablePlugin.ScriptEditorPanel;
import biz.wolschon.numbers.FixedPointNumber;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib-GPL<br/>
 * ImportScriptEditorPanel.java<br/>
 * created: 09.10.2008 06:58:35 <br/>
 *<br/><br/>
 * <b>TODO: Write a comment what this type does and how to use it!</a>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class ImportScriptEditorPanel extends ScriptEditorPanel {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger
            .getLogger(ImportScriptEditorPanel.class.getName());


    /**
     * The plugin we belong to.
     */
    private final AbstractScriptableImporter myImportPlugin;

    /**
     * The filename of the script.
     */
    private final File filename;

     /**
     * @return Returns the importPlugin.
     * @see #myImportPlugin
     */
    protected AbstractScriptableImporter getImportPlugin() {
        return myImportPlugin;
    }

    /**
      * @param myScriptNumber the number of the new script to edit
      * @param myInputText The description of the transaction. (May be null)
      * @param myInputDate The date of the transaction. (May be null)
      * @param myInputValue The value of the transaction. (May be null)
      * @param properties The complete and current settings of the HBCIImporter.
      * @param anImporter the plugin that makes use of us (no spaces, all lowercase short name).
      */
    public ImportScriptEditorPanel(final int myScriptNumber,
                                         final String myInputText,
                                         final Date myInputDate,
                                         final FixedPointNumber myInputValue,
                                         final Properties properties,
                                         final AbstractScriptableImporter anImporter) {
       super(myScriptNumber, myInputText, myInputDate, myInputValue, properties);
       myImportPlugin = anImporter;
       File dir = new File(getConfigFile().getParentFile(), "import_scripts");
       dir.mkdirs();
       filename = new File(dir, anImporter.getPluginName() + getMyScriptNumber() + ".js");
    }

    /**
     * Just an overridden ToString to return this classe's name
     * and hashCode.
     * @return className and hashCode
     */
    @Override
    public String toString() {
        return "ImportScriptEditorPanel@" + hashCode();
    }

    /**
     * ${@inheritDoc}.
     */
    @Override
    public File getConfigFile() {
        return getImportPlugin().getConfigFile();
    }

    /**
     * ${@inheritDoc}.
     */
    @Override
    public File getScriptFileName() {
        return filename;
    }

    /**
     * ${@inheritDoc}.
     */
    @Override
    public void addScriptToSettings(final int aScriptNumber, final String aRelativeFileName) {
           getSettings().setProperty(getImportPlugin().getPluginName() + AbstractScriptableImporter.SETTINGS_PREFIX_IMPORTSCRIPT + getMyScriptNumber(), aRelativeFileName);
           getSettings().setProperty(getImportPlugin().getPluginName() + AbstractScriptableImporter.SETTINGS_PREFIX_IMPORTSCRIPT_REGEXP + getMyScriptNumber(), getRegExpArea().getText());
           getSettings().setProperty(getImportPlugin().getPluginName() + AbstractScriptableImporter.SETTINGS_PREFIX_IMPORTSCRIPT_LANGUAGE + getMyScriptNumber(), "javascript");
    }

    /**
     * ${@inheritDoc}.
     */
    @Override
    public GnucashWritableAccount doTestRun(final GnucashWritableFile dummyFile) throws javax.xml.bind.JAXBException {
        GnucashWritableAccount bankAccount = dummyFile.createWritableAccount();
        bankAccount.setName("Dummy Bank-Account");
        bankAccount.setCurrencyNameSpace("ISO4217");
        bankAccount.setCurrencyID("EUR");

        // prepare dummy-transaction
        GnucashWritableTransaction transaction = dummyFile.createWritableTransaction();
        transaction.setDescription(getMyInputText().replace('\n', ' '));
        transaction.setDatePosted(getMyInputDate());
        transaction.setCurrencyNameSpace(bankAccount.getCurrencyNameSpace());
        transaction.setCurrencyID(bankAccount.getCurrencyID());

        // we always need the split on out account's side
        GnucashWritableTransactionSplit myAccountSplit = transaction.createWritingSplit(bankAccount);
        myAccountSplit.setValue(getMyInputValue());
        myAccountSplit.setQuantity(getMyInputValue());
        myAccountSplit.setUserDefinedAttribute("HBCI.orig_description", getMyInputText());
        myAccountSplit.setDescription(getMyInputText().replace('\n', ' '));


        myImportPlugin.runImportScript(getMyInputValue(),
                                     getMyInputText(),
                                     myAccountSplit,
                                     getMyScriptNumber(),
                                     "JavaScript",
                                     "(editor)",
                                     new StringReader(getEditorArea().getText()));
        return bankAccount;
    }
}


