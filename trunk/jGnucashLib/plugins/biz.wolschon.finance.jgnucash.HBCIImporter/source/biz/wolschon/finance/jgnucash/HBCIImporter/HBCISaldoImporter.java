/**
 * HBCIImporter.java
 * Created on 28.04.2009
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 * Permission is granted to use, modify,
 * publish and sub-license this code as specified in the contract. If nothing
 * else is specified these rights are given non-exclusively with no restrictions
 * solely to the contractor(s). If no specified otherwise I reserve the right to
 * use, modify, publish and sub-license this code to other parties myself.
 *
 * Otherwise, this code is made available under GPLv3 or later.
 *
 * -----------------------------------------------------------
 * major Changes:
 * 28.04.2009 - initial version ...
 */
package biz.wolschon.finance.jgnucash.HBCIImporter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.kapott.hbci.GV_Result.GVRKUms.BTag;
import org.kapott.hbci.callback.HBCICallback;
import org.kapott.hbci.structures.Saldo;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.AbstractScriptableImporter.AbstractScriptableImporter;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * Use HBCI4Java to import transactions via HBCI from a bank into a given
 * account.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class HBCISaldoImporter extends AbstractScriptableImporter {

    /**
     * Internally HBCI4Java represents monetary values as cent,
     * so we need to divity by this constant on 100.
     */
    private static final BigDecimal CENTPEREURO = new BigDecimal(100);


    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(HBCISaldoImporter.class.getName());


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


    /**
     * The key for the setting of the pin-code.
     * @see #myProperties
     */
    public static final String SETTINGS_PIN = "hbci.pin";

    /**
     * The key for the setting of the bank-number (German: BLZ) .
     * @see #myProperties
     */
    public static final String SETTINGS_BANKCODE = "hbci.bankcode";

    /**
     * The key for the setting of the country (defaults to "DE") .
     * @see #myProperties
     */
    public static final String SETTINGS_COUNTRY = "hbci.country";

    /**
     * The key for the setting of the account-number .
     * @see #myProperties
     */
    public static final String SETTINGS_GNUCASHACCOUNT = "hbci.gnucashaccountid";

    /**
     * The key for the setting of the account-number .
     * @see #myProperties
     */
    public static final String SETTINGS_ACCOUNT = "hbci.account";

    /**
     * The key for the setting of the hbci-url (without leading "https://") .
     * @see #myProperties
     */
    public static final String SETTINGS_SERVER = "hbci.server";





    /**
     * Import all transactions that are not yet in the account.
     * @see #myAccount
     */
    public void synchronizeAllTransactions() {

        File pintanfile;
        try {
            pintanfile = File.createTempFile("pintan", "hbci");
            pintanfile.deleteOnExit();
            pintanfile.delete(); // the file MUST not exist
        } catch (IOException e1) {
            LOG.log(Level.SEVERE, "[IOException] Problem in "
                       + getClass().getName(),
                         e1);
            // fall back to a default-filename
            pintanfile = new File("/tmp/pintan." + Math.random());
        }

        try {
            HBCICallback callback = new PropertiesHBCICallback(this
                    .getMyProperties());

            org.kapott.hbci.manager.HBCIUtils.init(null, null, callback);

            org.kapott.hbci.manager.HBCIUtils.setParam("log.loglevel.default",
                    "" + org.kapott.hbci.manager.HBCIUtils.LOG_DEBUG2);

            org.kapott.hbci.manager.HBCIUtils.setParam("client.passport.default", "PinTan");

            org.kapott.hbci.manager.HBCIUtils.setParam(
                    "client.passport.PinTan.filename", pintanfile.getAbsolutePath());
            org.kapott.hbci.manager.HBCIUtils.setParam(
                    "client.passport.PinTan.init", "1");

            org.kapott.hbci.passport.HBCIPassport passport = org.kapott.hbci.passport.AbstractHBCIPassport
                    .getInstance();

            String pversion = passport.getHBCIVersion();
            if (pversion == null || pversion.length() < 1) {
                pversion = "plus";
            }

            LOG.fine("hbci-version=" + pversion);

            org.kapott.hbci.manager.HBCIHandler handler = new org.kapott.hbci.manager.HBCIHandler(
                    pversion, passport);

            org.kapott.hbci.structures.Konto[] accs = passport.getAccounts();
            if (accs == null) {
                LOG.fine("passport.getAccounts()=null");
            } else {
                LOG.fine("passport.getAccounts()="
                        + java.util.Arrays.toString(accs));
            }
            org.kapott.hbci.GV.HBCIJob job = handler.newJob("KUmsAll");

            /*
             * job.setParam("src",src); job.setParam("dst",dst);
             * job.setParam("name",applet.dstname.getText());
             * job.setParam("btg",btg)/;
             */

            job.addToQueue();
            // handler.addJob(job);

            org.kapott.hbci.status.HBCIExecStatus status = handler.execute();
            if (!status.isOK()) {
                LOG.severe("Error synchronizing transactions from HBCI."
                        + " Status=" + status.getErrorString());
                return;
            }
            org.kapott.hbci.GV_Result.HBCIJobResult result = job.getJobResult();

            if (accs == null) {
                LOG.info("no result on fetching HBCI-transactions");
            } else {

                if (result instanceof org.kapott.hbci.GV_Result.GVRKUms) {
                    //org.kapott.hbci.GV_Result.GVRKUms r = (org.kapott.hbci.GV_Result.GVRKUms) result;
                    BTag[] dataPerDay = ((org.kapott.hbci.GV_Result.GVRKUms) result).getDataPerDay();
//                    org.kapott.hbci.GV_Result.GVRKUms.UmsLine[] flatData = r
//                            .getFlatData();

                    for (BTag tag : dataPerDay) {
                        Saldo saldo = tag.end;
                        createSaldoEntry((new FixedPointNumber(saldo.value
                                              .getLongValue())).divideBy(CENTPEREURO),
                                              saldo.timestamp);

                    }
                } else {
                    LOG.warning("result is no on org.kapott.hbci.GV_Result.GVRKUms but a  ["
                                    + result.getClass().getName()
                                    + "] = "
                                    + result);
                }
            } // accs != null

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error synchronizing saldi from HBCI.", e);
        } finally {
            pintanfile.delete();
        }
    }



    public String runImport(final GnucashWritableFile aWritableModel,
                            final GnucashWritableAccount aCurrentAccount)
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
                aCurrentAccount);
        // user-attributes

        if (ok) {
            // run the actual import.
            setMyProperties(settings);
            setMyAccount(aWritableModel.getAccountByID(settings
                            .getProperty(HBCIImporter.SETTINGS_GNUCASHACCOUNT)));
            synchronizeAllTransactions();
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



    @Override
    public String getPluginName() {
        return "hbci-saldi";
    }

}
