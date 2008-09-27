/**
 * JGnucash.java
 * Created on 16.05.2005
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
 *  16.05.2005 - initial version
 * ...
 *
 */
package biz.wolschon.finance.jgnucash;

import java.awt.Cursor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashAccountWritingImpl;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashFileWritingImpl;
//import biz.wolschon.finance.jgnucash.comdirect.ComdirectCSVImporter;
import biz.wolschon.finance.jgnucash.panels.TransactionsPanel;
import biz.wolschon.finance.jgnucash.panels.WritableTransactionsPanel;

/**
 * created: 16.05.2005 <br/>
 *
 * (Shall become a simple java-reimplementation of gnucash
 * that can read and write gnucash-files.)<br/>
 *
 * Extended version of JGnucashViewer that allows for
 * changing and writing the gnucash-file.
 * <br/>
 * TODO: use Jakarta commons logging
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class JGnucash extends JGnucashViewer {

    /**
     * The data-model.
     */
    private GnucashWritableFile model;

    /**
     * Overridden to create  {@link GnucashWritableFile}.
     * ${@inheritDoc}.
     */
    @Override
    protected final GnucashFile createModelFromFile(final File f)
    throws IOException, JAXBException {
        return new GnucashFileWritingImpl(f);
    }

    /**
     * @param args ignored.
     */
    public static void main(final String[] args) {
        JGnucash ste = new JGnucash();
        ste.setVisible(true);
    }

    /**
     * The file-menu.
     */
    private JMenu fileMenu;

    /**
     * File->Save.
     */
    private JMenuItem fileSaveMenuItem = null;

    /**
     * File->Save as...
     */
    private JMenuItem fileSaveAsMenuItem = null;

    /**
     * File-Menu to import other gnucash-files
     * into this one.
     */
    private JMenuItem fileImport = null;

    /**
     * The panel to show the current transactions.
     */
    private WritableTransactionsPanel writableTransactionsPanel;
    
    /**
     * True if we did make changes to our model.
     * Used to enable/disable the file->save -item.
     */
    private boolean hasChanged = false;

    /**
     * This method initializes transactionsPanel.
     *
     * @return javax.swing.JTable
     */
    @Override
    protected TransactionsPanel getTransactionsPanel() {
        return getWritableTransactionsPanel();
    }

    /**
     * This method initializes transactionsPanel.
     *
     * @return javax.swing.JTable
     */
    protected WritableTransactionsPanel getWritableTransactionsPanel() {
        if (writableTransactionsPanel == null) {
            writableTransactionsPanel = new WritableTransactionsPanel();
        }
        return writableTransactionsPanel;
    }

    /**
     * This method initializes FileMenu.
     *
     * @return javax.swing.JMenu
     */
    @Override
    protected JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = super.getFileMenu();
            fileMenu.setText("File");
            fileMenu.setMnemonic('f');
            int i = 1;
            fileMenu.add(getFileSaveMenuItem(), i++);
            fileMenu.add(getFileSaveAsMenuItem(), i++);
         //TODO: make this a plugin   fileMenu.add(getFileImportMenuItem(), i++);
        }
        return fileMenu;
    }
//
//    /**
//     * This method initializes fileImportComdirectCSV.
//     *
//     * @return javax.swing.JMenuItem
//     */
//    private JMenuItem getFileImportMenuItem() {
//        if (fileImport == null) {
//            fileImport = new JMenuItem();
//            fileImport.setText("import CSV...");
////          DEBUG fileSaveMenuItem.setEnabled(false);
//            fileImport.setMnemonic('i');
//            fileImport.addActionListener(new java.awt.event.ActionListener() {
//                public void actionPerformed(
//                        final java.awt.event.ActionEvent e) {
//                    showImportDialog();
//                }
//            });
//        }
//        return fileImport;
//    }

    /**
     * This method initializes fileSaveMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getFileSaveMenuItem() {
        if (fileSaveMenuItem == null) {
            fileSaveMenuItem = new JMenuItem();
            fileSaveMenuItem.setText("Save...");
//          DEBUG fileSaveMenuItem.setEnabled(false);
            fileSaveMenuItem.setMnemonic('s');
            fileSaveMenuItem.addActionListener(
               new java.awt.event.ActionListener() {
                public void actionPerformed(
                        final java.awt.event.ActionEvent e) {
                    saveFile();
                }
               });
        }
        return fileSaveMenuItem;
    }

    /**
     * This method initializes fileSaveAsMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getFileSaveAsMenuItem() {
        if (fileSaveAsMenuItem == null) {
            fileSaveAsMenuItem = new JMenuItem();
            fileSaveAsMenuItem.setText("Save as...");
            fileSaveAsMenuItem.setMnemonic('a');
            //DEBUG fileSaveAsMenuItem.setEnabled(false);
            fileSaveAsMenuItem.addActionListener(
               new java.awt.event.ActionListener() {
                public void actionPerformed(
                        final java.awt.event.ActionEvent e) {
                    saveFileAs();
                }
               });
        }
        return fileSaveAsMenuItem;
    }

    /**
     * Show the file->save as... -dialog.
     */
    private void saveFileAs() {
        int state = getJFileChooser().showSaveDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            File f = getJFileChooser().getSelectedFile();
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                getWritableModel().writeFile(f);
                saveFile();
                setTitle(f.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JAXBException e) {
                e.printStackTrace();
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }

//    /**
//     * Show a file-open -dialog and invoke the
//     * ComdirectCSVImporter with the file
//     * selected.
//     */
//    private void showImportDialog() {
//        JFileChooser fc = getJFileChooser();
//        fc.setFileFilter(new FileFilter() {
//            public boolean accept(final File f) {
//                if (f.isDirectory()) {
//                    return true;
//                }
//                return f.isFile() && f.getName().endsWith(".csv");
//            }
//
//            public String getDescription() {
//                return "Comdirect CSV- files";
//            }
//        });
//
//
//        int state = fc.showOpenDialog(this);
//        if (state == JFileChooser.APPROVE_OPTION) {
//            try {
//                File f = getJFileChooser().getSelectedFile();
//
//                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//                
//                //TODO: show selection of an account,
//                // the current account is a default
//                // another default can be saved in the options
//                GnucashAccountWritingImpl account =
//                    (GnucashAccountWritingImpl) getWritableModel()
//                                    .getAccountByName("Konto-comdirect");
//                ComdirectCSVImporter importer =
//                    new ComdirectCSVImporter(account);
//                importer.importCSV(f);
//            } catch (Exception x) {
//                showErrorMessagePopup(x);
//            } finally {
//                setCursor(Cursor.getDefaultCursor());
//            }
//        }
//    }

    /**
     * @param x the exception to show
     */
    protected final void showErrorMessagePopup(final Exception x) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        x.printStackTrace(pw);
        pw.close();
        String file = "unknown";
        try {
            getJFileChooser().getSelectedFile().getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, sw.toString(),
                "ERROR importing '" + file + "'",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * ${@inheritDoc}.
     */
	protected boolean loadFile() {
        boolean retval = super.loadFile();
        if (retval) {
            hasChanged = false;

            getFileSaveAsMenuItem().setEnabled(true);
            getFileSaveAsMenuItem().invalidate();
            getFileSaveMenuItem().setEnabled(true);
            getFileSaveMenuItem().invalidate();
//            getFileImportMenuItem().setEnabled(true);
//            getFileImportMenuItem().invalidate();
        }

        return retval;
    }

	/**
	 * @param f
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws JAXBException
	 */
	private void saveFile() {
		try {
            File oldfile = new File(getWritableModel().getFile().getAbsolutePath());
            oldfile.renameTo(new File(oldfile.getName()+(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SS").format(new Date()))+".backup"));

			getWritableModel().writeFile(getWritableModel().getFile());
			hasChanged = false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// logger.logError("Exception of type [FileNotFoundException] caught", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// logger.logError("Exception of type [IOException] caught", e);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// logger.logError("Exception of type [JAXBException] caught", e);
		}
	}

    protected void doExit() {
        if (hasChanged) {
            int state = JOptionPane.showConfirmDialog(this,
                    "File has been changed. Save before exit?");
            if (state == JOptionPane.YES_OPTION) {
                saveFile();
            } else if (state == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        System.exit(0);
    }

    /**
     * 
     * @return our model
     */
	protected GnucashWritableFile getWritableModel() {
		return model;
	}

	/**
	 * ${@inheritDoc}.
	 */
    protected GnucashFile getModel() {
        return model;
    }

    /**
     * ${@inheritDoc}.
     */
    protected void setModel(final GnucashFile model) {
        if(!(model instanceof GnucashWritableFile))
            throw new IllegalArgumentException("given model is not writable!");
        setWritableModel((GnucashWritableFile)model);
    }

    /**
     * 
     * @param model our new model
     */
	protected void setWritableModel(final GnucashWritableFile model) {
		if (model == null)
			throw new IllegalArgumentException(
					"null not allowed for field this.model");

        getFileSaveAsMenuItem().setEnabled(true);
        getFileSaveMenuItem().setEnabled(true);
        //getFileImportMenuItem().setEnabled(true);

		this.model = model;
	}
}
