/**
 *  License: GPLv3 or later
 */
package biz.wolschon.finance.jgnucash.ComdirectCSVImporter;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.plugin.ImporterPlugin;

/**
 * This is a demo-plugin written for the very, very old and outdated ComdirectCSVImporter-class.
 */
public class Main extends org.java.plugin.Plugin implements ImporterPlugin {

	private JFileChooser jFileChooser;

	/* (non-Javadoc)
	 * @see biz.wolschon.finance.jgnucash.plugin.ImporterPlugin#runImport(biz.wolschon.fileformats.gnucash.GnucashWritableFile, biz.wolschon.fileformats.gnucash.GnucashWritableAccount)
	 */
	@Override
	public String runImport(final GnucashWritableFile writableModel,
			final GnucashWritableAccount currentAccount) throws Exception {

		JFileChooser fc = getJFileChooser();
		


		int state = fc.showOpenDialog(null);
		if (state == JFileChooser.APPROVE_OPTION) {
			File f = getJFileChooser().getSelectedFile();

			ComdirectCSVImporter importer =
				new ComdirectCSVImporter(currentAccount);
			importer.importCSV(f);
		}

		return null;
	}

    /**
     * This method initializes jFileChooser.
     * If is used for the open-dialog.
     * In JGnuCash it is also used for the save,
     * save as and import -dialog.
     *
     * @return javax.swing.JFileChooser
     */
    protected JFileChooser getJFileChooser() {
        if (jFileChooser == null) {
            jFileChooser = new javax.swing.JFileChooser();
        }
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileFilter(new FileFilter() {
			public boolean accept(final File f) {
				if (f.isDirectory()) {
					return true;
				}
				return f.isFile() && f.getName().endsWith(".csv");
			}

			public String getDescription() {
				return "Comdirect CSV- files";
			}
		});
        return jFileChooser;
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
