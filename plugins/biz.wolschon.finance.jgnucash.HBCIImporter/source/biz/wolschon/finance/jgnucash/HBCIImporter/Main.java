/**
 * 
 */
package biz.wolschon.finance.jgnucash.HBCIImporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.plugin.ImporterPlugin;

/**
 * Scriptable plugin for importing transactions via HBCI/FinTS -online-banking.
 */
public class Main extends org.java.plugin.Plugin implements ImporterPlugin {

	private JFileChooser jFileChooser;

	/**
	 * Keys to the settings that absolutele must be set
	 * and have another value then in the default-properties.
	 */
	private static final String[] REQUIREDSETTINGS = new String[] {"hbci.accountid",
				"hbci.pin",
				"hbci.bankcode",
				"hbci.country",
				"hbci.account",
				"hbci.server",
				"hbci.import.defaultTargetAccountID"};

	/* (non-Javadoc)
	 * @see biz.wolschon.finance.jgnucash.plugin.ImporterPlugin#runImport(biz.wolschon.fileformats.gnucash.GnucashWritableFile, biz.wolschon.fileformats.gnucash.GnucashWritableAccount)
	 */
	@Override
	public String runImport(final GnucashWritableFile writableModel,
			final GnucashWritableAccount currentAccount) throws Exception {

		// load the properties
		Properties settings = new Properties();
		Properties default_settings = new Properties();
		default_settings.load(getClass().getClassLoader().getResourceAsStream("biz/wolschon/finance/jgnucash/HBCIImporter/default_hbci.properties"));
		
		
		File configfile = getConfigFile();
		if (configfile.exists()) {
			settings.load(new FileReader(configfile));
		} else {
			settings = default_settings;
			if(currentAccount != null) {
				settings.setProperty("hbci.accountid", currentAccount.getId());
			} else {
				JOptionPane.showMessageDialog(null,
						"Please select the gnucash-account representing\nyour bank-account and try again.",
						"Config-File missing.",
						JOptionPane.INFORMATION_MESSAGE);
				return null;
				
			}
			configfile.getParentFile().mkdirs();
			settings.store(new FileWriter(configfile), "automatically created default-values");
			JOptionPane.showMessageDialog(null,
					"An empty configfile\n" + configfile.getAbsolutePath()
					+ "\nhas been created for the currently open Account.\n"
					+ "Please edit it to reflect your credentials and try again.",
					"Config-File missing.",
					JOptionPane.INFORMATION_MESSAGE);
			return null;
		}

		if (settings.getProperty("hbci.accountid") == null
			|| settings.getProperty("hbci.accountid").equalsIgnoreCase(default_settings.getProperty("hbci.accountid"))) {
			if(currentAccount != null) {
				// the user cannot be expected to write down account-ids,
				// so for this one setting we are going to help him
				settings.setProperty("hbci.accountid", currentAccount.getId());
				settings.store(new FileWriter(configfile), "automatically created default-values");
			}
		}
		// check the config-file
		for (String key : REQUIREDSETTINGS) {
			if (settings.getProperty(key) == null
				|| settings.getProperty(key).equalsIgnoreCase(default_settings.getProperty(key))) {
				JOptionPane.showMessageDialog(null,
						"Please edit\n" + configfile.getAbsolutePath()
						+ "\nand add a meaningfull value for '" + key + "'.",
						"Config-File missing.",
						JOptionPane.INFORMATION_MESSAGE);
				return null;
			}
		}//TODO: we may better save bankcode, ... in the gnucash-account as user-attributes
		
		// run the actual import.
		HBCIImporter importer = new HBCIImporter(writableModel.getAccountByID(settings.getProperty("hbci.accountid")), settings);
		importer.synchronizeAllTransactions();

		return null;
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
