/**
 * FileImport.java
 * created: 07.10.2008 19:33:57
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
package biz.wolschon.finance.jgnucash.pago;


//automatically created logger for debug and error -output
import java.awt.Cursor;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.pago.importer.PagoImporter;
import biz.wolschon.finance.jgnucash.plugin.ImporterPlugin;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib-private<br/>
 * FileImport.java<br/>
 * created: 07.10.2008 19:33:57 <br/>
 *<br/><br/>
 * <b>TODO: Write a comment what this type does and how to use it!</a>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class FileImport implements ImporterPlugin {

	/**
	 * Automatically created logger for debug and error-output.
	 */
	private static final Logger LOG = Logger.getLogger(FileImport.class
			.getName());


	/**
	 * Just an overridden ToString to return this classe's name
	 * and hashCode.
	 * @return className and hashCode
	 */
	public String toString() {
		return "FileImport@" + hashCode();
	}

	/** 
	 * ${@inheritDoc}.
	 */
	@Override
	public String runImport(final GnucashWritableFile aWritableModel,
			                final GnucashWritableAccount aCurrentAccount) throws Exception {
		
		JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileFilter() {
            public boolean accept(final File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.isFile() && (f.getName().endsWith(".pdf") || f.getName().endsWith(".zip"));
            }

            public String getDescription() {
                return "Pago PDF/ZIP- files";
            }
        });


        int state = fc.showOpenDialog(null);
        if (state == JFileChooser.APPROVE_OPTION) {
            File[] f = fc.getSelectedFiles();
            /*if (f.length == 0) {
                File f2 = getJFileChooser().getSelectedFile();
                if (f2 != null) {
                    f = new File[] {f2};
                }
            }*/

            
            for (int i = 0; i < f.length; i++) {
                PagoImporter.importFile(aWritableModel, f[i]);
            }
        
        }
        return "";
	}

	/**
	 * @return the configuration-file (need not exist yet)
	 */
	protected static File getConfigFile() {
		return new File(getConfigFileDirectory(), ".pago.properties");
	}

	/**
	 * @return The directory where we store config-files.
	 */
	protected static File getConfigFileDirectory() {
		return new File(System.getProperty("user.home", "~"), ".jgnucash");
	}

}


