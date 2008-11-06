/**
 * PaypalImporter.java
 * Created on 06.11.2008
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 * Permission is granted to use, modify,
 * publish and sub-license this code as specified in the contract. If nothing
 * else is specified these rights are given non-exclusively with no restrictions
 * solely to the contractor(s). If no specified otherwise I reserve the right to
 * use, modify, publish and sub-license this code to other parties myself.
 * -----------------------------------------------------------
 * major Changes:
 * 06.11.2008 - initial version created using the HBCI-plugin as a template...
 */
package biz.wolschon.finance.jgnucash.PaypalImporter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.AbstractScriptableImporter.AbstractScriptableImporter;

import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.CallerServices;
import com.paypal.soap.api.AckCodeType;
import com.paypal.soap.api.PaymentTransactionSearchResultType;
import com.paypal.soap.api.TransactionSearchRequestType;
import com.paypal.soap.api.TransactionSearchResponseType;

/**
 * Use the Paypal-API to import transactions into a given
 * account.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class PaypalImporter extends AbstractScriptableImporter {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(PaypalImporter.class.getName());


    /**
     * Keys to the settings that absolutele must be set and have another value
     * then in the default-properties.
     */
    private static final String[] REQUIREDSETTINGS = new String[] {
            PaypalImporter.SETTINGS_ACCOUNT,
            PaypalImporter.SETTINGS_GNUCASHACCOUNT,
            PaypalImporter.SETTINGS_CERTFILE,
            PaypalImporter.SETTINGS_CERTPASSWD
    };

    /**
     * Names to the settings that absolutele must be set and have another value
     * then in the default-properties.
     */
    private static final String[] REQUIREDSETTINGNAMES = new String[] {
            "Your paypal account-name..",
            "Your paypal-account name in Gnucash.",
            "Your API-certificate-file",
            "Your password for the API-certificate-file"
    };
    /**
     * The key for the setting of the account-name of the paypal-account. .
     * @see #myProperties
     */
    public static final String SETTINGS_ACCOUNT = "paypal.paypalaccountname";
    /**
     * The key for the setting of the account-number .
     * @see #myProperties
     */
    public static final String SETTINGS_GNUCASHACCOUNT = "paypal.gnucashaccountid";
    /**
     * The key for the setting of the certificate-file .
     * @see #myProperties
     */
    public static final String SETTINGS_CERTFILE = "paypal.cert.filename";
    /**
     * The key for the setting of the password for the certificate-file .
     * @see #myProperties
     */
    public static final String SETTINGS_CERTPASSWD = "paypal.cert.password";




    /**
     * Import all transactions that are not yet in the account.
     * @see #myAccount
     */
    public void synchronizeAllTransactions() {
//
        try {
            CallerServices caller = new CallerServices();

            /*
             WARNING: Do not embed plaintext credentials in your application code.
             Doing so is insecure and against best practices.
             Your API credentials must be handled securely. Please consider
             encrypting them for use in any production environment, and ensure
             that only authorized individuals may view or modify them.
             */
            APIProfile profile = ProfileFactory.createSSLAPIProfile();
            profile.setAPIUsername("sdk-seller_api1.sdk.com");
            profile.setAPIPassword("12345678");
            profile.setCertificateFile("../Cert/sdk-seller.p12");
            profile.setPrivateKeyPassword("password");
            profile.setEnvironment("beta-sandbox");
            caller.setAPIProfile(profile);


            TransactionSearchRequestType request = new TransactionSearchRequestType();
            TransactionSearchResponseType response =
                (TransactionSearchResponseType) caller.call("TransactionSearch", request);
            if (response.getAck().getValue() != AckCodeType._Success) {


                LOG.log(Level.SEVERE, "Paypal-search failed");
                return;
            }

            PaymentTransactionSearchResultType[] ts = response.getPaymentTransactions();
            if (ts != null) {
                    System.out.println("Found " + ts.length + " records");

                    // Display the results of the first transaction returned
                    for (PaymentTransactionSearchResultType element : ts) {
                            System.out.println("\nTransaction ID: " + element.getTransactionID());
                            System.out.println("Payer Name: " + element.getPayerDisplayName());
                            System.out.println("Gross Amount: " + element.getGrossAmount().getCurrencyID() + " " + element.getGrossAmount().get_value());
                            System.out.println("Fee Amount: " + element.getFeeAmount().getCurrencyID() + " " + element.getFeeAmount().get_value());
                            System.out.println("Net Amount: " + element.getNetAmount().getCurrencyID() + " " + element.getNetAmount().get_value());
                    }
            } else {
                    System.out.println("Found 0 transaction");
            }

////TODO
//                if (result instanceof ...) {
//                    //TODO
//                    Date lastImportedDate = null;
//                    StringBuilder finalMessage = new StringBuilder();
//                    for (int i = 0; i < flatData.length; i++) {
//
//                        StringBuilder message = new StringBuilder();
//                        for (String element : flatData[i].usage) {
//                            message.append('\n').append(element);
//                        }
//                        message.replace(0, 1, "");
//
//                        Date valutaDate = flatData[i].valuta;
//                        FixedPointNumber value = new FixedPointNumber(flatData[i].value
//                                .getLongValue())
//                                .divideBy(new BigDecimal(100));
//                        if (!isTransactionPresent(valutaDate,value, message.toString())) {
//
//                            LOG.finer("--------------importing------------------------------");
//                            LOG.finer("value=" + value);
//                            LOG.finer("date=" + flatData[i].valuta);
//                            LOG.finer("UmsLine[" + i + "] Message="
//                                    + message.toString());
//                            // import this transaction
//                            importTransaction(flatData[i].valuta,
//                                    (new FixedPointNumber(flatData[i].value
//                                            .getLongValue()))
//                                            .divideBy(new BigDecimal(100)),
//                                    message.toString());
//
//                            finalMessage.append(flatData[i].valuta).append(" ")
//                                    .append(value.toString()).append("\n");
//
//                            // create a saldo-transaction for the last imported
//                            // transaction of each day
//                            if (lastImportedDate != null
//                                    && !lastImportedDate
//                                            .equals(flatData[i].valuta)) {
//                                Saldo saldo = flatData[i - 1].saldo;
//                                createSaldoEntry((new FixedPointNumber(saldo.value
//                                                      .getLongValue())).divideBy(new BigDecimal(100)),
//                                                      saldo.timestamp);
//                                markNonExistingTransactions(lastImportedDate);
//                            }
//                            lastImportedDate = flatData[i].valuta;
//                        }
//                    }
//
//                    // create a saldo-transaction after the last imported
//                    // transaction
//                    if (lastImportedDate != null) {
//                        Saldo saldo = flatData[flatData.length - 1].saldo;
//                        createSaldoEntry((new FixedPointNumber(saldo.value
//                                              .getLongValue())).divideBy(new BigDecimal(100)),
//                                              saldo.timestamp);
//                        markNonExistingTransactions(lastImportedDate);
//                    }
//                    if (finalMessage.length() > 0) {
//                        JOptionPane.showMessageDialog(null,
//                                "Imorted Transactions:\n"
//                                        + finalMessage.toString());
//                    }
//                } else {
//                    LOG.warning("result is no on org.kapott.hbci.GV_Result.GVRKUms but a  ["
//                                    + result.getClass().getName()
//                                    + "] = "
//                                    + result);
//                }
//            } // accs != null
//
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error synchronizing transactions from Paypal.", e);
        } finally {
        }
        JOptionPane.showMessageDialog(null, "not implemented yet.");
    }



    @Override
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
                                "biz/wolschon/finance/jgnucash/HBCIImporter/default_paypal.properties"));

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
                            .getProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT)));
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
        if (settings.getProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT) == null
                || settings.getProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT)
                        .trim().length() == 0
                || settings
                        .getProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT)
                        .equalsIgnoreCase(
                                defaultSettings
                                        .getProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT))) {
            if (aCurrentAccount != null) {
                // the user cannot be expected to write down account-ids,
                // so for this one setting we are going to help him
                settings.setProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT,
                        aCurrentAccount.getId());
                settings.store(new FileWriter(configfile),
                        "automatically created default-values");
            } else {
                JOptionPane
                        .showMessageDialog(
                                null,
                                "Please select the account representing\n"
                                        + " your paypal-account in Gnucash and try again.\n"
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
        return "paypal";
    }

}
