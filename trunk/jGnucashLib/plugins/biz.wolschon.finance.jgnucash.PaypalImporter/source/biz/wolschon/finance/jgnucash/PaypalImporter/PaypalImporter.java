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
 *
 * Otherwise, this code is made available under GPLv3 or later.
 *
 * -----------------------------------------------------------
 * major Changes:
 * 06.11.2008 - initial version created using the HBCI-plugin as a template...
 */
package biz.wolschon.finance.jgnucash.PaypalImporter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.AbstractScriptableImporter.AbstractScriptableImporter;
import biz.wolschon.finance.jgnucash.plugin.PluginConfigHelper;
import biz.wolschon.numbers.FixedPointNumber;

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
     * We go back one day at a time until we reach this many
     * days where we find nothing to do.
     */
    private static final int MAXDAYSWITHNOIMPORT = 10;


    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(PaypalImporter.class.getName());


    /**
     * Keys to the settings that absolutele must be set and have another value
     * then in the default-properties.
     */
    private static final String[] REQUIREDSETTINGS = new String[] {
            PaypalImporter.SETTINGS_APIUSER,
            PaypalImporter.SETTINGS_APIPASSWD,
            PaypalImporter.SETTINGS_GNUCASHACCOUNT,
            PaypalImporter.SETTINGS_CERTFILE,
            PaypalImporter.SETTINGS_CERTPASSWD
    };

    /**
     * Names to the settings that absolutele must be set and have another value
     * then in the default-properties.
     */
    private static final String[] REQUIREDSETTINGNAMES = new String[] {
            "Your paypal api-user.",
            "Your paypal api-password.",
            "Your paypal-account name in Gnucash.",
            "Your API-certificate-file",
            "Your password for the API-certificate-file"
    };
    /**
     * The key for the setting of the paypal api-user.
     * @see #myProperties
     */
    public static final String SETTINGS_APIUSER = "paypal.apiuser";
    /**
     * The key for the setting of the paypal api-password.
     * @see #myProperties
     */
    public static final String SETTINGS_APIPASSWD = "paypal.apipasswd";
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
            profile.setAPIUsername(getMyProperties().getProperty(PaypalImporter.SETTINGS_APIUSER));
            profile.setAPIPassword(getMyProperties().getProperty(PaypalImporter.SETTINGS_APIPASSWD));
            profile.setCertificateFile(getMyProperties().getProperty(PaypalImporter.SETTINGS_CERTFILE));
            profile.setPrivateKeyPassword(getMyProperties().getProperty(PaypalImporter.SETTINGS_CERTPASSWD));
//            profile.setEnvironment("beta-sandbox");
            profile.setEnvironment("live");
            caller.setAPIProfile(profile);


            TransactionSearchRequestType request = new TransactionSearchRequestType();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            // Paypal is limited to 100 transactions per result.
            // thus we:
            // * start with the current day,
            // *then go back one day at a time
            // * until we reached 10 consecutive days
            //   with no transaction that needed importing.
            StringBuilder finalMessage = new StringBuilder();
            int daysWithNoImportCountdown = MAXDAYSWITHNOIMPORT;
            while (daysWithNoImportCountdown > 0) {
                request.setEndDate((Calendar)calendar.clone());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                request.setStartDate(calendar);

                TransactionSearchResponseType response =
                    (TransactionSearchResponseType) caller.call("TransactionSearch", request);
                if (response.getAck().getValue() != AckCodeType._Success) {
                    LOG.log(Level.SEVERE, "Paypal-search with start-date " + calendar.getTime().toString() + " failed");
                    return;
                }

                PaymentTransactionSearchResultType[] ts = response.getPaymentTransactions();
                if (ts == null) {
                    LOG.log(Level.INFO, "Paypal-search  with start-date " + calendar.getTime().toString() + " had no result");
                    daysWithNoImportCountdown--;
                    continue;
                }
                LOG.log(Level.INFO, "Found " + ts.length + " records at all for day " + calendar.getTime().toString());

                int importedCount = importDay(ts, finalMessage, calendar.getTime());
                if (importedCount == 0) {
                    daysWithNoImportCountdown--;
                }
                else {
                    daysWithNoImportCountdown = MAXDAYSWITHNOIMPORT;
                }
            }
            if (finalMessage.length() > 0) {
                JOptionPane.showMessageDialog(null,
                        "Imorted Transactions:\n"
                        + finalMessage.toString());
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error synchronizing transactions from Paypal.", e);
        }
    }



    /**
     * Import a single day worth of transactions.
     * @param ts the transactions for the day
     * @param finalMessage message to be displayed after importing all relevant days
     * @param date the day we are importing
     * @return the number of imported or moved transactions
     * @throws JAXBException on issues with the gnucash-backend
     */
    private int importDay(final PaymentTransactionSearchResultType[] ts,
                           final StringBuilder finalMessage,
                           final Date date) throws JAXBException {
        int retval = 0;

        // shorthands
        String ourCurrency = getDefaultAccount().getCurrencyID();

        for (int currentTransaction = 0; currentTransaction < ts.length; currentTransaction++) {
            PaymentTransactionSearchResultType element = ts[currentTransaction];

            // skip in-progress transactions
            if (element.getStatus().equals("Pending") ) {
                continue;
            }

            java.util.Calendar timestamp = element.getTimestamp();
            Date valutaDate = timestamp.getTime();
            StringBuilder message = new StringBuilder();
            Formatter formatter = new Formatter(message, null);
            formatter.format("%s [transaction %s] [type %s]",
                             element.getPayerDisplayName(),
                             element.getTransactionID(),
                             element.getType());
            FixedPointNumber value = new FixedPointNumber(element.getNetAmount().get_value());

            LOG.finer("--------------current transaction------------------------------");
            LOG.finer("value=" + value + "\n"
                    + "date=" + valutaDate + "\n"
                    + "currency=" + element.getNetAmount().getCurrencyID().toString() + "\n"
                    + "default currency=" + getDefaultAccount().getCurrencyID().toString() + "\n"
                    + "Message=" + message.toString());

            //////////////////////////////////////////////////////////////////////
            // we need to support different currencies
            // this only works for payments made yet but it works

/* Withdrawals in different currencies are split into 3 transactions
 * by paypal:
Payer Name: actual customer
Gross Amount: USD -20.00
Fee Amount: USD 0.00
Net Amount: USD -20.00
----------------
Transaction ID: AAAAAAAAAAAAAAAAAAAA
Payer Name: From Euro
Gross Amount: USD 20.00
Fee Amount: USD 0.00
Net Amount: USD 20.00
-----------------
Transaction ID: BBBBBBBBBBBBBBBBBBBBB
Payer Name: To U.S. Dollar
Gross Amount: EUR -15.80
Fee Amount: EUR 0.00
Net Amount: EUR -15.80

/* Payments in different currencies are split into 3 transactions
 * by paypal:
Payer Name: From U.S. Dollar
Gross Amount: EUR 20.00
Fee Amount: EUR 0.00
Net Amount: EUR 20.00
----------------
Transaction ID: AAAAAAAAAAAAAAAAAAAA
Payer Name: To Euro
Gross Amount: USD -20.00
Fee Amount: USD 0.00
Net Amount: USD -20.00
-----------------
Transaction ID: BBBBBBBBBBBBBBBBBBBBB
Payer Name: actual payer
Gross Amount: USD 30.00
Fee Amount: USD -0.50
Net Amount: USD 29.50

since it is usually not needed tu support multiple currencies in AbstractScriptableImporter,
we don't extend it to support this but combine such 3 transactions here before handing them
to our base-class for import.
 * */
          // look ahead for merging foreign-currency transactions
          if (ts.length > currentTransaction + 2) {
              // more shorthands
              String trans1stCurrency = element.getNetAmount().getCurrencyID().toString();
              String trans1stValue = element.getNetAmount().get_value();
              String trans2ndCurrency = ts[currentTransaction + 1].getNetAmount().getCurrencyID().toString();
              String trans2ndValue = ts[currentTransaction + 1].getNetAmount().get_value();
              String trans3rdCurrency = ts[currentTransaction + 2].getNetAmount().getCurrencyID().toString();
              String trans3rdValue = ts[currentTransaction + 2].getNetAmount().get_value();

              // incoming or outgoing?
              if (!trans1stCurrency.equals(ourCurrency)) {
                  if (trans1stValue.startsWith("-")
                      && trans3rdValue.startsWith("-")
                      && ts[currentTransaction + 1].getPayerDisplayName().startsWith("From ")
                      && ts[currentTransaction + 2].getPayerDisplayName().startsWith("To ")
                      && trans2ndCurrency.equals(trans1stCurrency)
                      && trans3rdCurrency.equals(ourCurrency)) {

                      LOG.fine("combining an outgoing foreign-currency transaction with currency-conversion into a single transaction");

                      value = new FixedPointNumber(trans3rdValue);
                      formatter.format(" (%s - net amount: %s %s)",
                                       ts[currentTransaction + 2].getPayerDisplayName(),
                                       trans1stCurrency, trans1stValue);
                      currentTransaction += 2;
                  } else {

                      LOG.warning("we got a foreign-currency transaction that we cannot handle. Handling it as a EUR-transaction. The account-balance will be wrong!");
                      formatter.format(" [WARN: %s %s]",
                                       trans1stCurrency, trans1stValue);

                  }
              } else if (!trans3rdCurrency.equals(ourCurrency)
                         && !trans1stValue.startsWith("-")
                         && trans2ndValue.startsWith("-")
                         && !trans3rdValue.startsWith("-")
                         && ts[currentTransaction + 0].getPayerDisplayName().startsWith("From ")
                         && ts[currentTransaction + 1].getPayerDisplayName().startsWith("To ")
                         && trans1stCurrency.equals(ourCurrency)
                         && trans2ndCurrency.equals(trans3rdCurrency)) {

                  LOG.fine("combining an incoming foreign-currency transaction with currency-conversion into a single transaction");

                  message = new StringBuilder();
                  formatter = new Formatter(message, null);
                  formatter.format("%s [transaction %s] [type %s] (%s - gross amount: %s %s)",
                                   ts[currentTransaction + 2].getPayerDisplayName(),
                                   ts[currentTransaction + 2].getTransactionID(),
                                   ts[currentTransaction + 2].getType(),
                                   element.getPayerDisplayName(),
                                   trans3rdCurrency, trans3rdValue);
                  currentTransaction += 2;
              }
          }
          //////////////////////////////////////////////////////////////////////

        if (!isTransactionPresent(valutaDate, value, message.toString())) {

            retval++;
            LOG.finer("--------------importing------------------------------");
            LOG.finer("value=" + value + "\n"
                    + "date=" + valutaDate + "\n"
                    + "Message=" + message.toString());
            // import this transaction
            importTransaction(valutaDate,
                                value,
                                message.toString());

            finalMessage.append(valutaDate).append(" ")
                             .append(value.toString())
                             .append(" - ").append(message.toString())
                             .append("\n");

                        // create a saldo-transaction for the last imported
                        // transaction of each day

           }
        }
        markNonExistingTransactions(date);
        return retval;
    }



    @Override
    public String runImport(final GnucashWritableFile aWritableModel,
                            final GnucashWritableAccount aCurrentAccount)
                                                                   throws Exception {

        // load the properties
        Properties defaultSettings = new Properties();
        defaultSettings
                .load(getClass()
                        .getClassLoader()
                        .getResourceAsStream(
                                "biz/wolschon/finance/jgnucash/PaypalImporter/default_paypal.properties"));

        // get all accounts that contain contact-information
        // for an HBCI online-banking server of the respective
        // bank
        Collection<GnucashWritableAccount> paypalAccounts = PluginConfigHelper.getAllAccountsWithKey(aWritableModel, "paypal");
        if (paypalAccounts != null && paypalAccounts.size() > 0) {
            for (GnucashWritableAccount paypalAccount : paypalAccounts) {
                LOG.info("synchronizing Paypaö-account " + paypalAccount.getId() + "=\"" + paypalAccount.getName() + "\"");
                setMyProperties(paypalAccount);
                setMyAccount(paypalAccount);
                defaultSettings.put(SETTINGS_GNUCASHACCOUNT, paypalAccount.getId());
                boolean ok = askRequiresSettings(defaultSettings,
                        paypalAccount, aWritableModel);
                if (ok) {
                    synchronizeAllTransactions();
                }
            }
            return null;
        }


        GnucashWritableAccount paypalAccount = PluginConfigHelper.getOrConfigureAccountWithKey(aWritableModel, "paypal", "yes",
                "Please select the account representing\n"
                + " your paypal-account in Gnucash.\n"
                + "You can add additional accounts later by setting the user-defined poperty paypal=yes on them.");
        if (paypalAccount == null) {
            return null;
        }


        ////////////////////////////////
        // try to import an old config-file
        // into the new format of account-properties
        Properties settings = new Properties();
        File configfile = getConfigFile();
        if (configfile.exists()) {
            settings.load(new FileReader(configfile));
            for (String key : REQUIREDSETTINGS) {
                String value = settings.getProperty(key, "");
                paypalAccount.setUserDefinedAttribute(key, value);
            }
        }

        // ask all missing settings
        boolean ok = askRequiresSettings(defaultSettings, paypalAccount, aWritableModel);
        // user-attributes

        if (ok) {
            // run the actual import.
            setMyProperties(settings);
            setMyAccount(paypalAccount);
            synchronizeAllTransactions();
        }

        return null;
    }

//    /**
//     * @param settings
//     *            settings currently in effect
//     * @param defaultSettings
//     *            default-values for settings
//     * @param configfile
//     *            the file to save a changed config to
//     * @param aCurrentAccount
//     *            the currently selected account (may be null)
//     * @return true if all is ready for action
//     * @throws IOException
//     *             if we cannot write the config-file
//     */
//    private boolean askRequiresSettings(final Properties settings,
//                                        final Properties defaultSettings,
//                                        final File configfile,
//                                        final GnucashWritableAccount aCurrentAccount)
//                                                                                     throws IOException {
//        if (settings.getProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT) == null
//                || settings.getProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT)
//                        .trim().length() == 0
//                || settings
//                        .getProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT)
//                        .equalsIgnoreCase(
//                                defaultSettings
//                                        .getProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT))) {
//            if (aCurrentAccount != null) {
//                // the user cannot be expected to write down account-ids,
//                // so for this one setting we are going to help him
//                settings.setProperty(PaypalImporter.SETTINGS_GNUCASHACCOUNT,
//                        aCurrentAccount.getId());
//                settings.store(new FileWriter(configfile),
//                        "automatically created default-values");
//            } else {
//                JOptionPane
//                        .showMessageDialog(
//                                null,
//                                "Please select the account representing\n"
//                                        + " your paypal-account in Gnucash and try again.\n"
//                                        + "A config-file will then automatically be generated for you.");
//                return false;
//            }
//        }
//
//        boolean ok = true;
//        for (int i = 0; i < REQUIREDSETTINGS.length; i++) {
//            String key = REQUIREDSETTINGS[i];
//            if (settings.getProperty(key) == null
//                    || settings.getProperty(key).trim().length() == 0
//                    || settings.getProperty(key).equalsIgnoreCase(
//                            defaultSettings.getProperty(key))) {
//                String input = JOptionPane.showInputDialog("Please enter\n"
//                        + REQUIREDSETTINGNAMES[i]
//                        + "\nYou can later edit these values in\n"
//                        + configfile.getAbsolutePath(), settings.getProperty(
//                        key, ""));
//                if (input != null && input.trim().length() > 0) {
//                    settings.setProperty(key, input);
//                    settings.store(new FileWriter(configfile),
//                            "automatically created default-values");
//                } else {
//                    ok = false;
//                }
//                // JOptionPane.showMessageDialog(null,
//                // "Please edit\n" + configfile.getAbsolutePath()
//                // + "\nand add a meaningfull value for '" + key + "'.",
//                // "Config-File missing.",
//                // JOptionPane.INFORMATION_MESSAGE);
//                // return null;
//            }
//        } // TODO: we may better save bankcode, ... in the gnucash-account as
//        return ok;
//    }


    /**
     * Clear our settings and add only the attributes
     * of the given account to the settings.
     * @param aPaypalAccount the account
     */
    private void setMyProperties(final GnucashWritableAccount aPaypalAccount) {
        Properties prop = new Properties();

        LOG.log(Level.INFO, "Loading properties from gnucash-account " + aPaypalAccount.getQualifiedName());
        Collection<String> keys = aPaypalAccount.getUserDefinedAttributeKeys();
        for (String key : keys) {
            LOG.log(Level.INFO, "Loading from gnucash-account key: "
                    + key);
            String value = aPaypalAccount.getUserDefinedAttribute(key);
            prop.setProperty(key, value);
        }

        // migration
        try {
            // set all properties from old config file
            File oldfile = getConfigFile();
            if (oldfile != null && oldfile.exists()) {
                LOG.log(Level.INFO, "Importing old config file:"
                        + oldfile.getAbsolutePath() + " to new config");
                Properties oldprop = new Properties();
                oldprop.load(new FileReader(oldfile));
                for (Object keyo : oldprop.keySet()) {
                    String key = keyo.toString();
                    if (prop.contains(key)) {
                        continue;
                    }
                    LOG.log(Level.INFO, "Importing old config key "
                            + key + " to new config in gnucash-account");
                    String value = oldprop.getProperty(key);
                    prop.setProperty(key, value);
                    aPaypalAccount.setUserDefinedAttribute(key, value);
                }
                oldfile.renameTo(new File(oldfile.getAbsolutePath() + ".migrated"));
            } else {
                LOG.log(Level.INFO, "No old config file to import into new config");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Problem in "
                    + getClass().getName() + " while loading old config file",
                    e);
        }

        super.setMyProperties(prop);
    }

    /**
     * @param settings
     *            settings currently in effect
     * @param defaultSettings
     *            default-values for settings
     * @param aCurrentAccount
     *            the currently selected account (may be null)
     * @param aWritableModel the current file we are working on
     * @return true if all is ready for action
     * @throws IOException
     *             if we cannot write the config-file
     * @throws JAXBException on issues with the backend
     */
    private boolean askRequiresSettings(final Properties defaultSettings,
                                        final GnucashWritableAccount aCurrentAccount,
                                        final GnucashWritableFile aWritableModel)
                                                                                     throws IOException, JAXBException {
        boolean ok = true;
        for (int i = 0; i < REQUIREDSETTINGS.length; i++) {
            String key = REQUIREDSETTINGS[i];
            String value = aCurrentAccount.getUserDefinedAttribute(key);
            if (key.equals(SETTINGS_GNUCASHACCOUNT)) {
                continue;
            }
            if (value == null
                    || value.trim().length() == 0
                    || value
                    .equalsIgnoreCase(
                            defaultSettings.getProperty(key))) {

                if (value == null) {
                    value = defaultSettings.getProperty(key);
                }
                String input = JOptionPane.showInputDialog("Please enter\n"
                        + REQUIREDSETTINGNAMES[i]
                        + "\nYou can later edit these values the properties of the account\n"
                        + aCurrentAccount.getName(),
                        value);
                if (input != null && input.trim().length() > 0) {
                    aCurrentAccount.setUserDefinedAttribute(key, input);
                } else {
                    ok = false;
                }
            }
        } // for

        return ok;
    }




    @Override
    public String getPluginName() {
        return "paypal";
    }

}
