/**
 * 
 */
package biz.wolschon.finance.jgnucash.HBCIImporter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.plugin.ImporterPlugin;

/**
 * Scriptable plugin for importing transactions via HBCI/FinTS -online-banking.
 */
public class Main extends org.java.plugin.Plugin implements ImporterPlugin {

    private JFileChooser jFileChooser;

    /**
     * Keys to the settings that absolutele must be set and have another value
     * then in the default-properties.
     */
    private static final String[] REQUIREDSETTINGS = new String[] {
            HBCIImporter.SETTINGS_ACCOUNT,
            HBCIImporter.SETTINGS_GNUCASHACCOUNT,
            // not required HBCIImporter.SETTINGS_PIN ,
            HBCIImporter.SETTINGS_BANKCODE, HBCIImporter.SETTINGS_COUNTRY,
            HBCIImporter.SETTINGS_ACCOUNT, HBCIImporter.SETTINGS_SERVER // ,
    // not required HBCIImporter.SETTINGS_DEFAULTTARGETACCOUNT
    };

    /**
     * Names to the settings that absolutele must be set and have another value
     * then in the default-properties.
     */
    private static final String[] REQUIREDSETTINGNAMES = new String[] {
            "Your account-number with your bank.",
            "Your bank-account name in Gnucash.",
            // not required HBCIImporter.SETTINGS_PIN ,
            "Your bank-code/BLZ", "Your country, e.g. 'DE'",
            "Your bank-account-number/KNR",
            "Your HBCI-URL (without leading https://)" // ,
    // not required HBCIImporter.SETTINGS_DEFAULTTARGETACCOUNT
    };

    /*
     * (non-Javadoc)
     * @see
     * biz.wolschon.finance.jgnucash.plugin.ImporterPlugin#runImport(biz.wolschon.fileformats.gnucash.GnucashWritableFile,biz.wolschon.fileformats.gnucash.GnucashWritableAccount)
     */
    @Override
    public String runImport(final GnucashWritableFile writableModel,
                            final GnucashWritableAccount currentAccount)
                                                                        throws Exception {

        // load the properties
        Properties settings = new Properties();
        Properties defaultSettings = new Properties();
        defaultSettings
                .load(getClass()
                        .getClassLoader()
                        .getResourceAsStream(
                                "biz/wolschon/finance/jgnucash/HBCIImporter/default_hbci.properties"));

        File configfile = getConfigFile();
        if (configfile.exists()) {
            settings.load(new FileReader(configfile));
        }

        // check the config-file
        boolean ok = askRequiresSettings(settings, defaultSettings, configfile,
                currentAccount);
        // user-attributes

        if (ok) {
            // run the actual import.
            HBCIImporter importer = new HBCIImporter(
                    writableModel.getAccountByID(settings
                            .getProperty(HBCIImporter.SETTINGS_GNUCASHACCOUNT)),
                    settings);
            importer.synchronizeAllTransactions();
        }

        return null;
    }

    /**
     * @param settings
     *            settings currently in effect
     * @param defaultSettings
     *            default-values for settings
     * @param configfile
     *            the file to save a changed config to
     * @param aCurrentAccount
     *            the currently selected account (may be null)
     * @return true if all is ready for action
     * @throws IOException
     *             if we cannot write the config-file
     */
    private boolean askRequiresSettings(final Properties settings,
                                        final Properties defaultSettings,
                                        final File configfile,
                                        final GnucashWritableAccount aCurrentAccount)
                                                                                     throws IOException {
        if (settings.getProperty(HBCIImporter.SETTINGS_GNUCASHACCOUNT) == null
                || settings.getProperty(HBCIImporter.SETTINGS_GNUCASHACCOUNT)
                        .trim().length() == 0
                || settings
                        .getProperty(HBCIImporter.SETTINGS_GNUCASHACCOUNT)
                        .equalsIgnoreCase(
                                defaultSettings
                                        .getProperty(HBCIImporter.SETTINGS_GNUCASHACCOUNT))) {
            if (aCurrentAccount != null) {
                // the user cannot be expected to write down account-ids,
                // so for this one setting we are going to help him
                settings.setProperty(HBCIImporter.SETTINGS_GNUCASHACCOUNT,
                        aCurrentAccount.getId());
                settings.store(new FileWriter(configfile),
                        "automatically created default-values");
            } else {
                JOptionPane
                        .showMessageDialog(
                                null,
                                "Please select the account representing\n"
                                        + " your bank-account in Gnucash and try again.\n"
                                        + "A config-file will then automatically be generated for you.");
                return false;
            }
        }

        boolean ok = true;
        for (int i = 0; i < REQUIREDSETTINGS.length; i++) {
            String key = REQUIREDSETTINGS[i];
            if (settings.getProperty(key) == null
                    || settings.getProperty(key).trim().length() == 0
                    || settings.getProperty(key).equalsIgnoreCase(
                            defaultSettings.getProperty(key))) {
                String input = JOptionPane.showInputDialog("Please enter\n"
                        + REQUIREDSETTINGNAMES[i]
                        + "\nYou can later edit these values in\n"
                        + configfile.getAbsolutePath(), settings.getProperty(
                        key, ""));
                if (input != null && input.trim().length() > 0) {
                    settings.setProperty(key, input);
                    settings.store(new FileWriter(configfile),
                            "automatically created default-values");
                } else {
                    ok = false;
                }
                // JOptionPane.showMessageDialog(null,
                // "Please edit\n" + configfile.getAbsolutePath()
                // + "\nand add a meaningfull value for '" + key + "'.",
                // "Config-File missing.",
                // JOptionPane.INFORMATION_MESSAGE);
                // return null;
            }
        } // TODO: we may better save bankcode, ... in the gnucash-account as
        return ok;
    }

    /**
     * @return the configuration-file (need not exist yet)
     */
    protected static File getConfigFile() {
        return new File(getConfigFileDirectory(), ".hbci.properties");
    }

    /**
     * @return The directory where we store config-files.
     */
    protected static File getConfigFileDirectory() {
        return new File(System.getProperty("user.home", "~"), ".jgnucash");
    }

    @Override
    protected void doStart() throws Exception {
        // ignored
    }

    @Override
    protected void doStop() throws Exception {
        // ignored
    }

}
