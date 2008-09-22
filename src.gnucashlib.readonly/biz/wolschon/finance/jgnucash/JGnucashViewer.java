/**
 * JGnucashViewer.java
 * Created on 15.05.2005
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 *
 * -----------------------------------------------------------
 * major Changes:
 *  15.05.2005 - initial version
 *  16.05.2005 - split into JGnucashViewer and JGnucash
 * ...
 *
 */
package biz.wolschon.finance.jgnucash;


import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashFile;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashFileImpl;
import biz.wolschon.finance.jgnucash.panels.TaxReportPanel;
import biz.wolschon.finance.jgnucash.panels.TransactionsPanel;
import biz.wolschon.finance.jgnucash.swingModels.GnucashAccountsTreeModel;

import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import biz.wolschon.finance.jgnucash.panels.DebugLogPanel;

/**
*
* created: 15.05.2005 <br/>
*
* Simple Viewer for Gnucash-Files.
* @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
*/
public class JGnucashViewer extends JFrame {

    /**
     * Our logger for debug- and error-ourput.
     */
    private static final Log LOGGER = LogFactory.getLog(JGnucashViewer.class);

    GnucashFile myModel;


    private javax.swing.JPanel jContentPane = null;

    private javax.swing.JScrollPane treeScrollPane = null;

    private javax.swing.JFileChooser jFileChooser = null; //  @jve:visual-info  decl-index=0 visual-constraint="582,36"



    private static final String title = "JGnucash";

    protected JSplitPane jSplitPane = null;
    private JTree accountsTree = null;
    /**
     * The {@link JTabbedPane} containing {@link #transactionsPanel}
     * and {@link #taxReportPanel}.
     */
    private JTabbedPane myTabbedPane = null;
    private TransactionsPanel transactionsPanel = null;
    private TaxReportPanel taxReportPanel = null;
    private JMenuBar jJMenuBar = null;
    /**
     * The File-Menu.
     */
    private JMenu myFileMenu = null;
    /**
     * The Help-Menu.
     */
    private JMenu myHelpMenu = null;
    /**
     * File->Load.
     */
    private JMenuItem myFileLoadMenuItem = null;
    /**
     * File->Exit.
     */
    private JMenuItem myFileExitMenuItem = null;

    /**
     * The MenuItem to show the current log.
     */
	private JMenuItem myDebugLogMenu;

    /**
     * This method initializes
     * the GnucashViewer.
     */
    public JGnucashViewer() {
        super();
        // initialize logger
        Logger rootLogger = Logger.getLogger("");
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.WARNING);
        Logger.getLogger("biz.wolschon").setLevel(Level.ALL);

        initialize();
        initializeErrorLogHandler();
    }

    /**
     * Add a handler to ERROR-log-messages to display
     * a Dialog.
     */
    private void initializeErrorLogHandler() {
        Logger.getLogger("").addHandler(new Handler() {

            @Override
            public void close() {
                // ignored
            }

            @Override
            public void flush() {
                // ignored
            }

            @Override
            public void publish(final LogRecord aRecord) {

                if (aRecord.getLevel().intValue() < Level.SEVERE.intValue()) {
                    return;
                }

                JOptionPane.showMessageDialog(JGnucashViewer.this, aRecord.getLevel() + " "
                        + "in " + aRecord.getSourceClassName() + ":" + aRecord.getSourceMethodName() + "(...) "
                        + aRecord.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * This method initializes jSplitPane.
     *
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            jSplitPane.setLeftComponent(getTreeScrollPane());
            jSplitPane.setRightComponent(getJTabbedPane());
        }
        return jSplitPane;
    }
    /**
     * This method initializes accountsTree.
     *
     * @return javax.swing.JTree
     */
    protected JTree getAccountsTree() {
        if (accountsTree == null) {
            accountsTree = new JTree();

            if (getModel() == null) {
                accountsTree.setModel(new DefaultTreeModel(null));
            } else {
                accountsTree.setModel(new GnucashAccountsTreeModel(getModel()));
            }

            accountsTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(final TreeSelectionEvent e) {

                    TreePath path = e.getPath();
                    if (path == null) {
                        getTransactionsPanel().setAccount(null);
                    } else {
                     GnucashAccountsTreeModel.GnucashAccountTreeEntry entry
                     = (GnucashAccountsTreeModel.GnucashAccountTreeEntry)
                       path.getLastPathComponent();
                     getTransactionsPanel().setAccount(entry.getAccount());
                     LOGGER.debug("accoun " + entry.getAccount().getId()
                             + " = " + entry.getAccount().getQualifiedName()
                             + " selected");

                    }

                }
            });

        }
        return accountsTree;
    }
    /**
     * This method initializes transactionsPanel.
     *
     * @return javax.swing.JTable
     */
    protected JTabbedPane getJTabbedPane() {
        if (myTabbedPane == null) {
            myTabbedPane = new JTabbedPane();
            myTabbedPane.addTab("transactions", getTransactionsPanel());
            myTabbedPane.addTab("taxes", getTaxReportPanel());
        }
        return myTabbedPane;
    }

    /**
     * This method initializes transactionsPanel.
     *
     * @return javax.swing.JTable
     */
    protected TransactionsPanel getTransactionsPanel() {
        if (transactionsPanel == null) {
            transactionsPanel = new TransactionsPanel();
        }
        return transactionsPanel;
    }

    /**
     * This method initializes transactionsPanel.
     *
     * @return javax.swing.JTable
     */
    protected TaxReportPanel getTaxReportPanel() {
        if (taxReportPanel == null) {
            taxReportPanel = new TaxReportPanel(getModel());
        }
        return taxReportPanel;
    }

    /**
     * This method initializes jJMenuBar.
     *
     * @return javax.swing.JMenuBar
     */
    public JMenuBar getJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes FileMenu.
     *
     * @return javax.swing.JMenu
     */
    protected JMenu getFileMenu() {
        if (myFileMenu == null) {
            myFileMenu = new JMenu();
            myFileMenu.setText("File");
            myFileMenu.setMnemonic('f');
            myFileMenu.add(getFileLoadMenuItem());
            myFileMenu.add(new JSeparator());
            myFileMenu.add(getFileExitMenuItem());
        }
        return myFileMenu;
    }

    /**
     * This method initializes HelpMenu.
     *
     * @return javax.swing.JMenu
     */
    protected JMenu getHelpMenu() {
        if (myHelpMenu == null) {
        	myHelpMenu = new JMenu();
        	myHelpMenu.setText("Help");
        	myHelpMenu.setMnemonic('h');
        	myHelpMenu.add(getDebugLogMenuItem());
        }
        return myHelpMenu;
    }
 
    /**
     * This method initializes FileLoadMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getFileLoadMenuItem() {
        if (myFileLoadMenuItem == null) {
            myFileLoadMenuItem = new JMenuItem();
            myFileLoadMenuItem.setText("Open...");
            myFileLoadMenuItem.setMnemonic('a');
            myFileLoadMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    JGnucashViewer.this.loadFile();
                }
            });
        }
        return myFileLoadMenuItem;
    }
    /**
     * This method initializes fileExitMenuItem.
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getFileExitMenuItem() {
        if (myFileExitMenuItem == null) {
            myFileExitMenuItem = new JMenuItem();
            myFileExitMenuItem.setText("Exit...");
            myFileExitMenuItem.setMnemonic('x');
            myFileExitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    doExit();
                }
            });
        }
        return myFileExitMenuItem;
    }

    /**
     * @return The MenuItem to show the current log.
     */
    private JMenuItem getDebugLogMenuItem() {
        if (myDebugLogMenu == null) {
            myDebugLogMenu = new JMenuItem();
            myDebugLogMenu.setName("Debug-Log");
            myDebugLogMenu.setText("Show log");
            myDebugLogMenu.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent arg0) {
                    JFrame f = new JFrame("debug-log");
                    f.getContentPane().add(new DebugLogPanel());
                    f.pack();
                    f.setVisible(true);
                }
            });
        }
        return myDebugLogMenu;
    }

    /**
     * @param args empty or contains a gnucash-file-name as a first param.
     */
    public static void main(final String[] args) {
        JGnucashViewer ste = new JGnucashViewer();
        ste.setVisible(true);
        if (args.length > 0) {
            ste.loadFile(new File(args[0]));
        }
    }

    /**
     * This method initializes jContentPane.
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            final int border = 5;
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(
                    border, border, border, border));
            jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes this gui.
     */
    private void initialize() {
        final int defaultWidth  = 750;
        final int defaultHeight = 600;

        this.setJMenuBar(getJMenuBar());
        this.setContentPane(getJContentPane());
        this.setSize(defaultWidth, defaultHeight);
        this.setTitle(title);
        this.setDefaultCloseOperation(
                javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(final java.awt.event.WindowEvent e) {
                doExit();
            }
        });

    }

    /**
     * This method initializes treeScrollPane.
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getTreeScrollPane() {
        if (treeScrollPane == null) {
            final int defaultWidth = 400;
            treeScrollPane = new javax.swing.JScrollPane();
            treeScrollPane.setViewportView(getAccountsTree());
            treeScrollPane.setPreferredSize(
                    new Dimension(defaultWidth, Integer.MAX_VALUE));
        }
        return treeScrollPane;
    }

    /**
     * This method initializes jFileChooser.
     * If is used for the open-dialog.
     * In JGnuCash it is also used for the save,
     * save as and import -dialog.
     *
     * @return javax.swing.JFileChooser
     */
    protected javax.swing.JFileChooser getJFileChooser() {
        if (jFileChooser == null) {
            jFileChooser = new javax.swing.JFileChooser();
        }
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileFilter(new FileFilter() {
            public boolean accept(final File f) {
                return true; // accept all files
            }

            public String getDescription() {
                return "gnucash files";
            }
        });
        return jFileChooser;
    }


    /**
     * Given a file, create a GnucashFile for it.
     * @param f the file
     * @return the GnucashFile
     * @throws IOException if the file cannot be loaded from disk
     * @throws JAXBException if the file cannot be parsed
     */
    protected GnucashFile createModelFromFile(final File f)
    throws IOException, JAXBException {
        return new GnucashFileImpl(f);
    }

    /**
     * @return true if the file was loaded successfully
     */
    protected boolean loadFile() {
        int state = getJFileChooser().showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION) {
            File f = getJFileChooser().getSelectedFile();
            return loadFile(f);
        }
        return false;
    }

    /**
     * @param f the file to load.
     * @return true if the file was loaded successfully
     */
    public boolean loadFile(final File f) {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            setModel(createModelFromFile(f));
            getAccountsTree().setModel(
                    new GnucashAccountsTreeModel(getModel()));
            getTaxReportPanel().setBooks(getModel());
            getTransactionsPanel().setAccount(null);
            setTitle(title);
            jSplitPane.setDividerLocation(0.5);
            return true;
        } catch (IOException e1) {
           LOGGER.error("cannot load file '" + f.getAbsoluteFile() + "'", e1);
        } catch (JAXBException e1) {
            LOGGER.error("cannot load file '" + f.getAbsoluteFile() + "'", e1);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
        return false;
    }


    protected void doExit() {
        System.exit(0);
    }
    protected GnucashFile getModel() {
        return myModel;
    }
    protected void setModel(final GnucashFile model) {
        if (model == null)
            throw new IllegalArgumentException(
                    "null not allowed for field this.model");
        this.myModel = model;
    }
} //  @jve:visual-info  decl-index=0 visual-constraint="20,27"
