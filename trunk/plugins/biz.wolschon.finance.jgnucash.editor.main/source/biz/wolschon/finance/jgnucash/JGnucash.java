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
 *  06.11.2005 - added file-menu-plugins
 * ...
 *
 */
package biz.wolschon.finance.jgnucash;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.IntegrityCheckReport;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.Extension.Parameter;

import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashFileWritingImpl;
import biz.wolschon.finance.jgnucash.actions.ImportPluginMenuAction;
import biz.wolschon.finance.jgnucash.actions.OpenFilePluginMenuAction;
import biz.wolschon.finance.jgnucash.actions.ToolPluginMenuAction;
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

    public JGnucash() {
    }

    public JGnucash(final PluginManager manager, final PluginDescriptor descriptor) {
        this();
        setPluginManager(manager);
        setPluginDescriptor(descriptor);
        initializeGUI();
    }


    /**
     * Our logger for debug- and error-output.
     */
    static final Log LOGGER = LogFactory.getLog(JGnucash.class);

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

//    /**
//     * File-Menu to import other gnucash-files
//     * into this one.
//     */
//    private JMenuItem fileImport = null;

    /**
     * The Import-Menu.
     */
    private JMenu importMenu = null;

    /**
     * The Import-Menu.
     */
    private JMenu toolMenu = null;

    /**
     * The main-entry-point to our plugin-api.
     */
    private PluginManager pluginManager = null;

    /**
     * The descriptor for our top-level application-plugin.
     */
    private PluginDescriptor pluginDescriptor = null;

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
     * Our menu-bar.
     */
    private JMenuBar menuBar;

    private JMenu helpMenu;

    private JMenuItem helpPluginReport;

    /**
     * The main-entry-point to our plugin-api.
     * @return the pluginManager
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * The main-entry-point to our plugin-api.
     * @param aPluginManager the pluginManager to set
     */
    public void setPluginManager(final PluginManager aPluginManager) {
        pluginManager = aPluginManager;
    }

    /**
     * The descriptor for our top-level application-plugin.
     * @return the pluginDescriptor
     */
    public PluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }

    /**
     * The descriptor for our top-level application-plugin.
     * @param aPluginDescriptor the pluginDescriptor to set
     */
    public void setPluginDescriptor(final PluginDescriptor aPluginDescriptor) {
        pluginDescriptor = aPluginDescriptor;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.JGnucashViewer#getHelpMenu()
     */
    @Override
    protected JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = super.getHelpMenu();
            if (getPluginManager() != null) {
                helpMenu.add(getHelpPluginReportMenu());
            }
        }
        return helpMenu;
    }

    /**
     * @return a menu-item to show a report on plugin-loading
     */
    private JMenuItem getHelpPluginReportMenu() {
        if (helpPluginReport == null) {
            helpPluginReport = new JMenuItem();
            helpPluginReport.setText("Plugin Report");
            helpPluginReport.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(final ActionEvent e) {
                    PluginManager manager = getPluginManager();
                    IntegrityCheckReport report = manager.getRegistry().getRegistrationReport();
                    StringBuilder message = new StringBuilder();
                    Collection<IntegrityCheckReport.ReportItem> items = report.getItems();
                    for (IntegrityCheckReport.ReportItem reportItem : items) {
                        String severity = ""; /*unknown: ";
                        if (reportItem.getSeverity() == IntegrityCheckReport.ReportItem.SEVERITY_ERROR) {
                            severity = "Error: ";
                        } else if (reportItem.getSeverity() == IntegrityCheckReport.ReportItem.SEVERITY_WARNING) {
                            severity = "Warning: ";
                        } else if (reportItem.getSeverity() == IntegrityCheckReport.ReportItem.SEVERITY_INFO) {
                            severity = "Info: ";
                        };*/
                        message.append(severity)
                               /*.append('(')
                               .append(reportItem.getCode())
                               .append(')')*/
                               .append(reportItem.getMessage())
                               .append('\n');
                    }
                    JOptionPane.showMessageDialog(JGnucash.this,
                            message.toString(),
                            "Plugins-Report",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                }
            );
        }
        return helpPluginReport;
    }


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
            writableTransactionsPanel = new WritableTransactionsPanel(getPluginManager(), getPluginDescriptor());
        }
        return writableTransactionsPanel;
    }


    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.JGnucashViewer#getJMenuBar()
     */
    @Override
    public JMenuBar getJMenuBar() {
        if (menuBar == null) {
            menuBar = super.getJMenuBar();
            // insert the "Import"-menu before the help-menu.
            menuBar.add(getImportMenu(), menuBar.getMenuCount() - 1);
            menuBar.add(getToolMenu(), menuBar.getMenuCount() - 1);
        }
        return menuBar;
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

            // allow plugins to supply file-open and file-save -actions
            PluginManager manager = getPluginManager();
            // if we are configured for the plugin-api
            if (manager != null) {
                ExtensionPoint toolExtPoint = manager.getRegistry().getExtensionPoint(
                                              getPluginDescriptor().getId(), "DataSource");
                for (Iterator<Extension> it = toolExtPoint.getConnectedExtensions().iterator(); it.hasNext();) {
                    Extension ext = it.next();
                    String pluginName = "unknown";

                    try {
                        pluginName = ext.getParameter("name").valueAsString();
                        JMenuItem newMenuItem = new JMenuItem();
                        newMenuItem.putClientProperty("extension", ext);
                        Parameter descrParam = ext.getParameter("description");
                        Parameter iconParam = ext.getParameter("icon");
                        if (iconParam != null) {
                            try {
                                URL iconUrl = getPluginManager().getPluginClassLoader(
                                        ext.getDeclaringPluginDescriptor()).getResource(iconParam.valueAsString());
                                if (iconUrl != null) {
                                    newMenuItem.setIcon(new ImageIcon(iconUrl));
                                }
                            } catch (Exception e) {
                                LOGGER.error("cannot load icon for Loader-Plugin '" + pluginName + "'", e);
                            }
                        }
                        newMenuItem.setText(pluginName);
                        if (descrParam != null) {
                            newMenuItem.setToolTipText(descrParam.valueAsString());
                        }
                        newMenuItem.addActionListener(new OpenFilePluginMenuAction(this, ext, pluginName));
                        fileMenu.add(newMenuItem);
                    } catch (Exception e) {
                        LOGGER.error("cannot load Loader-Plugin '" + pluginName + "'", e);
                        JOptionPane.showMessageDialog(this, "Error",
                                "Cannot load Loader-Plugin '" + pluginName + "'",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        return fileMenu;
    }


    /**
     * This method initializes import-menu..
     *
     * @return javax.swing.JMenu
     */
    protected JMenu getImportMenu() {
        if (importMenu == null) {
            importMenu = new JMenu();
            importMenu.setText("Import");
            importMenu.setMnemonic('i');
            //importMenu.setEnabled(false);// first we need to load a file

            PluginManager manager = getPluginManager();
            // if we are configured for the plugin-api
            if (manager != null) {
                ExtensionPoint toolExtPoint = manager.getRegistry().getExtensionPoint(
                                              getPluginDescriptor().getId(), "Importer");
                for (Iterator<Extension> it = toolExtPoint.getConnectedExtensions().iterator(); it.hasNext();) {
                    Extension ext = it.next();
                    String pluginName = "unknown";

                    try {
                        pluginName = ext.getParameter("name").valueAsString();
                        JMenuItem newMenuItem = new JMenuItem();
                        newMenuItem.putClientProperty("extension", ext);
                        Parameter descrParam = ext.getParameter("description");
                        Parameter iconParam = ext.getParameter("icon");
                        URL iconUrl = null;
                        if (iconParam != null) {
                            try {
                                iconUrl = getPluginManager().getPluginClassLoader(
                                        ext.getDeclaringPluginDescriptor()).getResource(iconParam.valueAsString());
                                if (iconUrl != null) {
                                    newMenuItem.setIcon(new ImageIcon(iconUrl));
                                }
                            } catch (Exception e) {
                                LOGGER.error("cannot load icon for Importer-Plugin '" + pluginName + "'", e);
                            }
                        }
                        newMenuItem.setText(pluginName);
                        if (descrParam != null) {
                            newMenuItem.setToolTipText(descrParam.valueAsString());
                        }
                        newMenuItem.addActionListener(new ImportPluginMenuAction(this, ext, pluginName));
                        importMenu.add(newMenuItem);
                    } catch (Exception e) {
                        LOGGER.error("cannot load Importer-Plugin '" + pluginName + "'", e);
                        JOptionPane.showMessageDialog(this, "Error",
                                "Cannot load Importer-Plugin '" + pluginName + "'",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        return importMenu;
    }

    /**
     * This method initializes import-menu..
     *
     * @return javax.swing.JMenu
     */
    protected JMenu getToolMenu() {
        if (toolMenu == null) {
            toolMenu = new JMenu();
            toolMenu.setText("Tools");
            toolMenu.setMnemonic('t');
            //importMenu.setEnabled(false);// first we need to load a file

            PluginManager manager = getPluginManager();
            // if we are configured for the plugin-api
            if (manager != null) {
                ExtensionPoint toolExtPoint = manager.getRegistry().getExtensionPoint(
                                              getPluginDescriptor().getId(), "Tool");
                for (Iterator<Extension> it = toolExtPoint.getConnectedExtensions().iterator(); it.hasNext();) {
                    Extension ext = it.next();
                    String pluginName = "unknown";

                    try {
                        pluginName = ext.getParameter("name").valueAsString();
                        JMenuItem newMenuItem = new JMenuItem();
                        newMenuItem.putClientProperty("extension", ext);
                        Parameter descrParam = ext.getParameter("description");
                        Parameter iconParam = ext.getParameter("icon");
                        URL iconUrl = null;
                        if (iconParam != null) {
                            try {
                                iconUrl = getPluginManager().getPluginClassLoader(
                                        ext.getDeclaringPluginDescriptor()).getResource(iconParam.valueAsString());
                                if (iconUrl != null) {
                                    newMenuItem.setIcon(new ImageIcon(iconUrl));
                                }
                            } catch (Exception e) {
                                LOGGER.error("cannot load icon for Tool-Plugin '" + pluginName + "'", e);
                            }
                        }
                        newMenuItem.setText(pluginName);
                        if (descrParam != null) {
                            newMenuItem.setToolTipText(descrParam.valueAsString());
                        }
                        newMenuItem.addActionListener(new ToolPluginMenuAction(this, ext, pluginName));
                        toolMenu.add(newMenuItem);
                    } catch (Exception e) {
                        LOGGER.error("cannot load Tool-Plugin '" + pluginName + "'", e);
                        JOptionPane.showMessageDialog(this, "Error",
                                "Cannot load Tool-Plugin '" + pluginName + "'",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        return toolMenu;
    }
    /**
     * This method initializes fileSaveMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getFileSaveMenuItem() {
        if (fileSaveMenuItem == null) {
            fileSaveMenuItem = new JMenuItem();
            fileSaveMenuItem.setText("Save...");
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
				LOGGER.error("cannot save file '" + f.getAbsolutePath() + "' (file not found)", e);
				JOptionPane.showMessageDialog(this, "Error",
						"cannot save file '" + f.getAbsolutePath() + "' (file not found)",
		                JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
				LOGGER.error("cannot save file '" + f.getAbsolutePath() + "' (io-problem)", e);
				JOptionPane.showMessageDialog(this, "Error",
						"cannot save file '" + f.getAbsolutePath() + "' (io-problem)",
		                JOptionPane.ERROR_MESSAGE);
            } catch (JAXBException e) {
				LOGGER.error("cannot save file '" + f.getAbsolutePath() + "' (gnucash-format-problem)", e);
				JOptionPane.showMessageDialog(this, "Error",
						"cannot save file '" + f.getAbsolutePath() + "' (gnucash-format-problem)",
		                JOptionPane.ERROR_MESSAGE);
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }

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
			File f = getWritableModel().getFile();
			if (f == null) {
				f = new File("unknown");
			}
			LOGGER.error("cannot save file '" + f.getAbsolutePath() + "' (file not found)", e);
			JOptionPane.showMessageDialog(this, "Error",
					"cannot save file '" + f.getAbsolutePath() + "' (file not found)",
	                JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        	File f = getWritableModel().getFile();
			if (f == null) {
				f = new File("unknown");
			}
			LOGGER.error("cannot save file '" + f.getAbsolutePath() + "' (io-problem)", e);
			JOptionPane.showMessageDialog(this, "Error",
					"cannot save file '" + f.getAbsolutePath() + "' (io-problem)",
	                JOptionPane.ERROR_MESSAGE);
        } catch (JAXBException e) {
        	File f = getWritableModel().getFile();
			if (f == null) {
				f = new File("unknown");
			}
			LOGGER.error("cannot save file '" + f.getAbsolutePath() + "' (gnucash-format-problem)", e);
			JOptionPane.showMessageDialog(this, "Error",
					"cannot save file '" + f.getAbsolutePath() + "' (gnucash-format-problem)",
	                JOptionPane.ERROR_MESSAGE);
        }
	}

    @Override
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
   public GnucashWritableFile getWritableModel() {
       return model;
   }

	/**
	 * ${@inheritDoc}.
	 */
    @Override
    protected GnucashFile getModel() {
        return model;
    }

    /**
     * ${@inheritDoc}.
     */
    @Override
    public void setModel(final GnucashFile newModel) {
        if (!(newModel instanceof GnucashWritableFile)) {
            throw new IllegalArgumentException("given model is not writable!");
        }
        setWritableModel((GnucashWritableFile) newModel);
    }

    /**
     *
     * @param newModel our new model
     */
    public void setWritableModel(final GnucashWritableFile newModel) {
        if (newModel == null) {
            throw new IllegalArgumentException(
                    "null not allowed for field this.model");
        }

        super.setModel(newModel);
        model = newModel;
        hasChanged = false;

        getFileSaveAsMenuItem().setEnabled(true);
        getFileSaveMenuItem().setEnabled(true);
        getImportMenu().setEnabled(true);
    }
}
