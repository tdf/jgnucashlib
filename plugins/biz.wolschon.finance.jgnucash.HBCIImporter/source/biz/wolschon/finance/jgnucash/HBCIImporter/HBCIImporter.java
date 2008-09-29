/**
 * HBCIImporter.java
 * Created on 26.07.2008
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 *  Permission is granted to use, modify, publish and sub-license this code
 *  as specified in the contract. If nothing else is specified these rights
 *  are given non-exclusively with no restrictions solely to the contractor(s).
 *  If no specified otherwise I reserve the right to use, modify, publish and
 *  sub-license this code to other parties myself.
 *
 * -----------------------------------------------------------
 * major Changes:
 *  26.07.2008 - initial version
 * ...
 *
 */
package biz.wolschon.finance.jgnucash.HBCIImporter;

import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarOutputStream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kapott.hbci.callback.HBCICallback;
import org.kapott.hbci.structures.Saldo;

import com.sun.codemodel.JOp;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashTransactionSplit;
import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransaction;
import biz.wolschon.fileformats.gnucash.GnucashWritableTransactionSplit;
import biz.wolschon.finance.jgnucash.panels.DebugLogPanel;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * Use HBCI4Java to import transactions via HBCI from
 * a bank into a given account.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 *
 */
public class HBCIImporter {


	/**
	 * Automatically created logger for debug and error-output.
	 */
	private static final Log LOGGER = LogFactory.getLog(HBCIImporter.class);

	/**
	 * The account we are importing to.
	 */
	private GnucashWritableAccount myAccount;

	/**
	 * @param myAccount The account we are importing to.
	 * @param myProperties The settings like account-number, pin-code, bank.url, ....
	 * @see #SETTINGS_ACCOUNT
	 * @see #SETTINGS_BANKCODE
	 * @see #SETTINGS_COUNTRY
	 * @see #SETTINGS_PIN
	 * @see #SETTINGS_SERVER
	 */
	public HBCIImporter(final GnucashWritableAccount myAccount,
			final Properties myProperties) {
		super();
		this.myAccount = myAccount;
		this.myProperties = myProperties;
	}

	/**
	 * The settings like account-number, pin-code, bank.url, ....
	 * @see #SETTINGS_ACCOUNT
	 * @see #SETTINGS_BANKCODE
	 * @see #SETTINGS_COUNTRY
	 * @see #SETTINGS_PIN
	 * @see #SETTINGS_SERVER
	 */
	private Properties myProperties;



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
	public static final String SETTINGS_ACCOUNT = "hbci.account";

	/**
	 * The key for the setting of the hbci-url (without leading "https://") .
	 * @see #myProperties
	 */
	public static final String SETTINGS_SERVER = "hbci.server";


	/**
	 * The key for the setting of the id of the account that
	 * imported transactions will be booked to/from by default. .
	 * @see #myProperties
	 */
	public static final String SETTINGS_DEFAULTTARGETACCOUNT = "hbci.import.defaultTargetAccountID";

	/**
	 * The key-prefix for the setting of a script-file-name.<br/>
	 * To add 3 scripts to the config add 3 settings:<br/>
	 * SETTINGS_PREFIX_IMPORTSCRIPT + "0",<br/>
	 * SETTINGS_PREFIX_IMPORTSCRIPT + "1" ans <br/>
	 * SETTINGS_PREFIX_IMPORTSCRIPT + "2",<br/>.
	 * The value of this property is either the file-name or
	 * a name to be loaded via {@link ClassLoader#getResourceAsStream(String)}.
	 * @see #myProperties
	 * @see #SETTINGS_PREFIX_IMPORTSCRIPT_REGEXP
	 * @see #SETTINGS_PREFIX_IMPORTSCRIPT_ACCOUNT
	 * @see #SETTINGS_PREFIX_IMPORTSCRIPT_LANGUAGE
	 */
	public static final String SETTINGS_PREFIX_IMPORTSCRIPT = "hbci.import.import.scriptfile";
	/**
	 * The key-prefix for the setting of a regexp.<br/>
	 * Each added script must have a regexp. If the text of the transaction matches
	 * the regexp this and no other script will be executed.
	 * @see #myProperties
	 * @see #SETTINGS_PREFIX_IMPORTSCRIPT
	 * @see #SETTINGS_PREFIX_IMPORTSCRIPT_ACCOUNT
	 */
	public static final String SETTINGS_PREFIX_IMPORTSCRIPT_REGEXP = "hbci.import.import.regexp";
	/**
	 * The JSR223 language-name of the script. (defaults to "Javascript".
	 * @see #myProperties
	 * @see #SETTINGS_PREFIX_IMPORTSCRIPT
	 * @see #SETTINGS_PREFIX_IMPORTSCRIPT_ACCOUNT
	 */
	public static final String SETTINGS_PREFIX_IMPORTSCRIPT_LANGUAGE = "hbci.import.import.scriptlanguage";
	/**
	 * Instead of a full script, you can overside the accountid of {@link #SETTINGS_DEFAULTTARGETACCOUNT}.
	 * @see #myProperties
	 * @see #SETTINGS_PREFIX_IMPORTSCRIPT_REGEXP
	 * @see #SETTINGS_PREFIX_IMPORTSCRIPT
	 */
	public static final String SETTINGS_PREFIX_IMPORTSCRIPT_ACCOUNT = "hbci.import.import.accountidoverride";

	/**
	 * Setting of "true" or "false" if we are to ask the user if he wants to create a new script
	 * when no script is matching.
	 */
	private static final String SETTINGS_ASKTOCREATEMISSINGSCRIPTS = "hbci.ask_to_create_missing_scripts";

	/**
	 * @return The account we are importing to.
	 */
	public GnucashWritableAccount getMyAccount() {
		return myAccount;
	}

	/**
	 * @param myAccount The account we are importing to.
	 */
	public void setMyAccount(final GnucashWritableAccount myAccount) {
		this.myAccount = myAccount;
	}

	/**
	 * @return The settings like account-number, pin-code, bank.url, ....
	 * @see #SETTINGS_ACCOUNT
	 * @see #SETTINGS_BANKCODE
	 * @see #SETTINGS_COUNTRY
	 * @see #SETTINGS_PIN
	 * @see #SETTINGS_SERVER
	 */
	public Properties getMyProperties() {
		return myProperties;
	}

	/**
	 * @param myProperties The settings like account-number, pin-code, bank.url, ....
	 * @see #SETTINGS_ACCOUNT
	 * @see #SETTINGS_BANKCODE
	 * @see #SETTINGS_COUNTRY
	 * @see #SETTINGS_PIN
	 * @see #SETTINGS_SERVER
	 */
	public void setMyProperties(final Properties myProperties) {
		this.myProperties = myProperties;
	}


	/**
	 * Import all transactions that are not yet in the account.
	 * @see #myAccount
	 */
	public void synchronizeAllTransactions() {
		//TODO

		try {
			HBCICallback callback = new PropertiesHBCICallback(this.getMyProperties());

			org.kapott.hbci.manager.HBCIUtils.init(null,null, callback);

			org.kapott.hbci.manager.HBCIUtils.setParam("log.loglevel.default", ""+ org.kapott.hbci.manager.HBCIUtils.LOG_WARN);
			final String cpd="client.passport.default";
			org.kapott.hbci.manager.HBCIUtils.setParam(cpd,"PinTan");
			org.kapott.hbci.manager.HBCIUtils.setParam("client.passport.PinTan.filename","/tmp/pintanfile");
			//org.kapott.hbci.manager.HBCIUtils.setParam("client.passport.PinTan.certfile","/tmp/pintancert");
			org.kapott.hbci.manager.HBCIUtils.setParam("client.passport.PinTan.init","1");


			org.kapott.hbci.passport.HBCIPassport passport = org.kapott.hbci.passport.AbstractHBCIPassport.getInstance();
			System.out.println("host="+passport.getHost());
			passport.setHost("hbci.comdirect.de/pintan/HbciPinTanHttpGate");
			System.out.println("host="+passport.getHost());

			String pversion=passport.getHBCIVersion();
			if (pversion== null || pversion.length()<1)
				pversion ="plus";

			System.out.println("hbci-version=" + pversion);

			org.kapott.hbci.manager.HBCIHandler handler = new org.kapott.hbci.manager.HBCIHandler(pversion,passport);

			org.kapott.hbci.structures.Konto[]   accs=passport.getAccounts();
			if(accs == null) {
				System.err.println("passport.getAccounts()=null");
			} else {
				System.err.println("passport.getAccounts()=" + java.util.Arrays.toString(accs));
			}
			org.kapott.hbci.GV.HBCIJob job = handler.newJob("KUmsAll");

			/*job.setParam("src",src);
			        job.setParam("dst",dst);
			        job.setParam("name",applet.dstname.getText());
			        job.setParam("btg",btg)/;*/


			job.addToQueue();
			//handler.addJob(job);

			org.kapott.hbci.status.HBCIExecStatus status   = handler.execute();
			if (!status.isOK()) {
				LOGGER.error("Error synchronizing transactions from HBCI."
						+ " Status=" + status.getErrorString());
				return;
			}
			org.kapott.hbci.GV_Result.HBCIJobResult result = job.getJobResult();

			if(accs == null) {
				LOGGER.info("no result on fetching HBCI-transactions");
			} else {

				if (result instanceof org.kapott.hbci.GV_Result.GVRKUms) {
					org.kapott.hbci.GV_Result.GVRKUms r = (org.kapott.hbci.GV_Result.GVRKUms) result;
					org.kapott.hbci.GV_Result.GVRKUms.UmsLine[] flatData = r.getFlatData();
					
					Date lastImportedDate = null;
					StringBuilder finalMessage = new StringBuilder();
					for (int i = 0; i < flatData.length; i++) {

					    StringBuilder message = new StringBuilder();
                        for (int j=0; j<flatData[i].usage.length; j++) {
                            message.append('\n').append(flatData[i].usage[j]);
                        }
                        message.replace(0, 1, "");

						if (!isTransactionPresent(flatData[i].valuta,
						                          (new FixedPointNumber(flatData[i].value.getLongValue())).divideBy(new BigDecimal(100)),
						                          message.toString())) {

							LOGGER.debug("--------------importing------------------------------");
							//System.out.println("UmsLine["+i+"]=" + flatData[i].toString());
							String valueString = org.kapott.hbci.manager.HBCIUtils.value2String(-flatData[i].value.getLongValue()/-100.0d) + flatData[i].value.getCurr();
                            LOGGER.debug("value=" + valueString);
							LOGGER.debug("date=" + flatData[i].valuta);
							LOGGER.debug("UmsLine["+i+"] Message=" + message.toString());
							// import this transaction
							importTransaction(flatData[i].valuta, (new FixedPointNumber(flatData[i].value.getLongValue())).divideBy(new BigDecimal(100)), message.toString());

							finalMessage.append(flatData[i].valuta).append(" ").append(valueString).append("\n");
							
							// create a saldo-transaction for the last imported transaction of each day
							if (lastImportedDate != null
							    && !lastImportedDate.equals(flatData[i].valuta)) {
							    createSaldoEntry(flatData[i - 1].saldo);
							    markNonExistingTransactions(lastImportedDate);
							}
							lastImportedDate = flatData[i].valuta;
						};
					}


                    // create a saldo-transaction after the last imported transaction
					if (lastImportedDate != null) {
                            createSaldoEntry(flatData[flatData.length - 1].saldo);
						    markNonExistingTransactions(lastImportedDate);
                        }
					if (finalMessage.length() > 0) {
					    JOptionPane.showMessageDialog(null, "Imorted Transactions:\n" + finalMessage.toString());
					}
				}
				else
					LOGGER.warn("result is no on org.kapott.hbci.GV_Result.GVRKUms but a  [" + result.getClass().getName() + "] = " + result);
			} // accs != null

		} catch (Exception e) {
			LOGGER.error("Error synchronizing transactions from HBCI.", e);
		}
	}

	/**
	 * Create a transaction that simply acts as a comment and
	 * shows the current Saldo. This helps in finding missing
	 * or doubled transactions manually.
	 * @param aValuta the date
	 * @param aSaldo the saldo on that date
	 */
	private void createSaldoEntry(final Saldo aSaldo) {

	    try {
	        FixedPointNumber saldo = (new FixedPointNumber(aSaldo.value.getLongValue())).divideBy(new BigDecimal(100));

	        String saldoOKStr = "OK";
	        if (!getMyAccount().getBalance(aSaldo.timestamp).equals(saldo)) {
	            saldoOKStr = "NAK (" + getMyAccount().getBalance(aSaldo.timestamp).subtract(saldo) + ")";
	        }
	        
            GnucashWritableTransaction transaction = getMyAccount().getWritableFile().createWritableTransaction();
            transaction.setDescription("Saldo: " + aSaldo.value + " " + saldoOKStr);
            transaction.setDatePosted(aSaldo.timestamp);
            transaction.setCurrencyNameSpace(getMyAccount().getCurrencyNameSpace());
            transaction.setCurrencyID(getMyAccount().getCurrencyID());

            // we always need the split on out account's side
            GnucashWritableTransactionSplit myAccountSplit = transaction.createWritingSplit(getMyAccount());
            myAccountSplit.setValue(new FixedPointNumber()); //0
            myAccountSplit.setQuantity(new FixedPointNumber()); //0

            // dummy-split on the other side
            GnucashWritableAccount otherAccount = getDefaultAccount();
            GnucashWritableTransactionSplit otherAccountSplit = transaction.createWritingSplit(otherAccount);
            otherAccountSplit.setValue(new FixedPointNumber()); //0
            otherAccountSplit.setQuantity(new FixedPointNumber()); //0
	    } catch (Exception e) {
	        LOGGER.error("Exception while creating Saldo-transaction.", e);
	    }

    }
	/**
	 * Mark all transactions of the given date that are not matched
	 * by an HBCI-transaction and have a split-value != 0,
	 * so they can be quickly identified by the user.
	 * @param date
	 */
	private void markNonExistingTransactions(final Date date) {
		GregorianCalendar input = new GregorianCalendar();
		input.setTimeInMillis(date.getTime());
		GregorianCalendar day = new GregorianCalendar(input.get(GregorianCalendar.YEAR),
				                                        input.get(GregorianCalendar.MONTH),
				                                        input.get(GregorianCalendar.DAY_OF_MONTH));
		long from = day.getTimeInMillis();
		input.add(GregorianCalendar.DAY_OF_MONTH, 1);
		long to   = day.getTimeInMillis();

		FixedPointNumber zero = new FixedPointNumber();
		final String WARNING = "[did not happen in HBCI-account";
		
		//TODO: ein getTransactionSplits(fromDate, toDate) wäre praktisch
		List<GnucashTransactionSplit> splits = getMyAccount().getTransactionSplits();
		for (GnucashTransactionSplit split : splits) {
			if (split.getTransaction().getDatePosted().getTime() < from) {
				continue;
			}
			if (split.getTransaction().getDatePosted().getTime() >= to) {
				continue;
			}
			if (!split.getQuantity().equals(zero) && split.getUserDefinedAttribute("HBCI.orig_description") == null) {
			    String description = split.getTransaction().getDescription();
			    if (description.indexOf(WARNING) < 0) {
			    	((GnucashWritableTransaction) split.getTransaction()).setDescription(
			    			description
			    			+ WARNING
			    			+ " on " + DateFormat.getDateInstance().format(new Date()) + "]");
			    }
			};
		}
	}

    /**
	 * Import the given transaction into the account.
	 * @param text the description-text ("usage"-field on a paper-form) of the transaction. 
	 * @param date effective date of the transaction
	 * @param value Wert in der Währung des Kontos
	 * @see #myAccount
	 */
	private void importTransaction(final Date date,
			final FixedPointNumber value,
			final String text) {

		try {
			GnucashWritableTransaction transaction = getMyAccount().getWritableFile().createWritableTransaction();
			transaction.setDescription(text.replace('\n', ' '));
			transaction.setDatePosted(date);
			transaction.setCurrencyNameSpace(getMyAccount().getCurrencyNameSpace());
			transaction.setCurrencyID(getMyAccount().getCurrencyID());

			// we always need the split on out account's side
			GnucashWritableTransactionSplit myAccountSplit = transaction.createWritingSplit(getMyAccount());
			myAccountSplit.setValue(value);
			myAccountSplit.setQuantity(value);
			myAccountSplit.setUserDefinedAttribute("HBCI.orig_description", text);
			myAccountSplit.setDescription(text.replace('\n', ' '));

			GnucashWritableAccount otherAccount = getDefaultAccount();
			String otherAccountText = "automatically added";

			boolean scriptMatched = false;
			try {
				// look if there is a script matching,
				int scriptnum = 0;
				while (getMyProperties().containsKey(SETTINGS_PREFIX_IMPORTSCRIPT_REGEXP + scriptnum)) {

					String regexp = getMyProperties().getProperty(SETTINGS_PREFIX_IMPORTSCRIPT_REGEXP + scriptnum);
					if (text.matches(regexp)) {
						scriptMatched =  true;

						// does a full script exist or only an alternate accountid?
						if (getMyProperties().containsKey(SETTINGS_PREFIX_IMPORTSCRIPT + scriptnum)) {
							importViaScript(value,
									        text,
									        transaction,
									        myAccountSplit,
									        scriptnum);
						} else {

							String alternateAccountID = getMyProperties().getProperty(SETTINGS_PREFIX_IMPORTSCRIPT_ACCOUNT + scriptnum);
							otherAccount = getMyAccount().getWritableFile().getAccountByID(alternateAccountID);

							if (otherAccount == null) {
								LOGGER.error("Error  Account " + otherAccount + " given in user-HBCI-rule #" + scriptnum + " not found");
								JOptionPane.showMessageDialog(null, "Error  Account " + otherAccount + " given in user-HBCI-rule #" + scriptnum + " not found",
										"User-supplied target-account not found.",
										JOptionPane.ERROR_MESSAGE);
							} else {
							
								LOGGER.info("importing transaction using alternate target-account: " + alternateAccountID
										+ "=" + otherAccount.getQualifiedName());
								otherAccountText = "";
							}
							
						}


						break;
					}

					scriptnum++;
				}
			} catch (Exception e) {
				LOGGER.error("Exception while importing a transaction via a script-handler.", e);
			} catch (java.lang.NoClassDefFoundError e) {
				LOGGER.error("NoClassDefFoundError while importing a transaction. Maybe you are using JDK1.5 instead of 1.6+?", e);
			}

			if (!scriptMatched) {
				Integer newScriptnum = createMissingScript(date, value, text);
				if (newScriptnum != null) {
					importViaScript(value,
							text,
							transaction,
							myAccountSplit,
							newScriptnum.intValue());
				}
			}

			// default-case and make sure that all transactions are balanced
			if (!transaction.isBalanced()) {

				if (transaction.getSplitsCount() > 1) {
					JOptionPane.showMessageDialog(null, "Imported transaction is not balanced."
							+ "\ntransaction (value is " + value + " but " + transaction.getBalance() +" is missing ) \n" + text,
							"not balanced",
							JOptionPane.WARNING_MESSAGE);
				}
				
				GnucashWritableTransactionSplit otherSplit = transaction.createWritingSplit(otherAccount);
				FixedPointNumber negatedValue = transaction.getNegatedBalance();
				otherSplit.setValue(negatedValue);
				otherSplit.setQuantity(negatedValue);	
				otherSplit.setDescription(otherAccountText);
			}
		} catch (JAXBException e) {
			LOGGER.error("Exception while importing a transaction", e);
		}
	}

	/**
	 * We do not have a script for this transaction.<br/>
	 * Ask the user to create one.<br/>
	 * If no, return null. If yes create the script and,
	 * add it to the config and save the config.
	 * @param text the description-text ("usage"-field on a paper-form) of the transaction. 
	 * @param date effective date of the transaction
	 * @param value Wert in der Währung des Kontos
	 * @return The scriptnum of a newly created script or null.
	 */
	private Integer createMissingScript(final Date date, final FixedPointNumber value, final String text) {
		if (!myProperties.getProperty(SETTINGS_ASKTOCREATEMISSINGSCRIPTS, "true").equalsIgnoreCase("true")) {
			return null;
		}

		int answer = JOptionPane.showConfirmDialog(null, "No script exists for the following transaction. Do you want to create one?\n"
				+ "Value: " + value.toPlainString() + "\n"
				+ text + "\n"
				+ "(Never ask again by setting " + SETTINGS_ASKTOCREATEMISSINGSCRIPTS + "=false)",
				"Create script?", JOptionPane.YES_NO_OPTION);
		if (answer != JOptionPane.YES_OPTION) {
			return null;
		}

		int maxScriptnum = 0;
		while (getMyProperties().containsKey(SETTINGS_PREFIX_IMPORTSCRIPT_REGEXP + maxScriptnum)) {
			maxScriptnum++;
		}


		JDialog f = new JDialog((JFrame) null, true);
		f.setTitle("new Script");
        ScriptEditorPanel editor = new ScriptEditorPanel(maxScriptnum, text, date, value, getMyProperties());
		f.getContentPane().add(editor);
        f.pack();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
        
        // if the script was saved, return it to be run
        if (!editor.wasCanceled() && getMyProperties().containsKey(SETTINGS_PREFIX_IMPORTSCRIPT_REGEXP + maxScriptnum)) {
        	return maxScriptnum;
        }
        return null;
	}

	/**
	 * @param value
	 * @param text
	 * @param transaction
	 * @param myAccountSplit
	 * @param scriptnum
	 */
	private void importViaScript(final FixedPointNumber value,
			final String text, GnucashWritableTransaction transaction,
			GnucashWritableTransactionSplit myAccountSplit, int scriptnum) {
		String language = getMyProperties().getProperty(SETTINGS_PREFIX_IMPORTSCRIPT_LANGUAGE + scriptnum, "JavaScript");
		String scriptPath = getMyProperties().getProperty(SETTINGS_PREFIX_IMPORTSCRIPT + scriptnum);

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine jsEngine = mgr.getEngineByName(language);
		jsEngine.put("text", text);
		jsEngine.put("value", value);
		jsEngine.put("transaction", transaction);
		jsEngine.put("myAccountSplit", myAccountSplit);
		jsEngine.put("file",        getMyAccount().getWritableFile());
		FixedPointNumber ustValue = ((FixedPointNumber) value.clone()).divideBy(new FixedPointNumber("-1,19")).multiply(new FixedPointNumber("0,19")); //TODO: get tax-% from tax-config
		FixedPointNumber nettoValue = ((FixedPointNumber) value.clone()).negate().subtract(ustValue);
		jsEngine.put("USt",         ustValue);
		jsEngine.put("Netto",       nettoValue);
		InputStream is = null;
		try {
			if (new File(scriptPath).exists()) {
				is = new FileInputStream(scriptPath);
			} else if (new File(Main.getConfigFileDirectory(), scriptPath).exists()) {
				is = new FileInputStream(new File(Main.getConfigFileDirectory(), scriptPath));
			} else{
				is = this.getClass().getClassLoader().getResourceAsStream(scriptPath);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Error loading script number " + scriptnum, e);
			JOptionPane.showMessageDialog(null, "Error, user- HBCI-Import-Script #" + scriptnum
					+ " at '" + scriptPath + "' cannot be loaded:\n" + e.getMessage()
					+ "\ntransaction (value is " + value + ") \n" + text,
					scriptPath,
					JOptionPane.ERROR_MESSAGE);
		}
		if (is != null) {
			try {
				LOGGER.info("importing transaction using script: " + scriptPath);
				Reader reader = new InputStreamReader(is);
				jsEngine.eval(reader);
			} catch (Exception ex) {
				LOGGER.error("Error executing script number " + scriptnum + " from " + scriptPath, ex);
				JOptionPane.showMessageDialog(null, "Error executing user HBCI-Import-Script #" + scriptnum
						+ " '" + scriptPath + "':\n "
						+ ex.getMessage()
						+ "\ntransaction (value is " + value + ") \n" + text,
						scriptPath,
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			LOGGER.error("Error  script number " + scriptnum + " at " + scriptPath + " not found");
			JOptionPane.showMessageDialog(null, "Error, user- HBCI-Import-Script #" + scriptnum
					+ " at '" + scriptPath + "' not found"
					+ "\ntransaction (value is " + value + ") \n" + text,
					scriptPath,
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Get the default-account (something like "expenses-other").<br/>
	 * If it does not exist, create one.
	 * @return the default-accout
	 * @throws JAXBException  if account-creation fails-
	 */
	private GnucashWritableAccount getDefaultAccount() throws JAXBException {
		GnucashWritableFile writableFile = getMyAccount().getWritableFile();
		String accountID = getMyProperties().getProperty(SETTINGS_DEFAULTTARGETACCOUNT);
		GnucashWritableAccount account = accountID == null ? null : (GnucashWritableAccount) writableFile.getAccountByIDorName(accountID, accountID);
		if (account == null) {
			account = writableFile.getAccountByName("Ausgleichskonto-EUR");
		}
		if (account == null) {
			account = writableFile.createWritableAccount();
			account.setName("Ausgleichskonto-EUR");
		}
		return account;
	}

	/**
	 * Determine if a transaction of this value has been made on or around the given date.
	 * (Not very precise.) 
	 * @param date effective date of the transaction
	 * @param value Wert in der Währung des Kontos
	 * @see #myAccount
	 */
	protected boolean isTransactionPresent(final Date date, final FixedPointNumber value, final String description) {
		final long dateDeltaMillis = 4 * 24 * 60 * 60 * 1000;
		long from = date.getTime() - dateDeltaMillis;
		long to   = date.getTime() + 2 * dateDeltaMillis;

		//TODO: ein getTransactionSplits(fromDate, toDate) wäre praktisch
		List<GnucashTransactionSplit> splits = getMyAccount().getTransactionSplits();
		for (GnucashTransactionSplit split : splits) {
			if (split.getTransaction().getDatePosted().getTime() < from) {
				continue;
			}
			if (split.getTransaction().getDatePosted().getTime() > to) {
				continue;
			}
			if (split.getQuantity().equals(value)) {
			    String origDescr = split.getUserDefinedAttribute("HBCI.orig_description");
			    if (origDescr != null && !origDescr.equalsIgnoreCase(description)) {
			    	// this transactions belongs to another HBCI-transaction
			        continue;
			    }
			    if (origDescr == null) {
			        try {
                        ((GnucashWritableTransactionSplit) split).setUserDefinedAttribute("HBCI.orig_description", description);
                        ((GnucashWritableTransaction) split.getTransaction()).setDatePosted(date);
                    } catch (JAXBException e) {
                        LOGGER.warn("[JAXBException] Problem in "
                                   + getClass().getName(),
                                     e);
                    }
			    }
				return true;
			};
		}
		
		
		
		return false;
	}
}
