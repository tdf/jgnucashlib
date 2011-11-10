/**
 * ScriptEditorPanel.java
 * created: 28.09.2008 15:03:18
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jGnucash by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase sport for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 *  jGnucash-RO is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jGnucash-RO is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with traveling_salesman.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************
 * Editing this file:
 *  -For consistent code-quality this file should be checked with the
 *   checkstyle-ruleset enclosed in this project.
 *  -After the design of this file has settled it should get it's own
 *   JUnit-Test that shall be executed regularly. It is best to write
 *   the test-case BEFORE writing this class and to run it on every build
 *   as a regression-test.
 */
package biz.wolschon.finance.jgnucash.AbstractScriptablePlugin;

//other imports

//automatically created logger for debug and error -output
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.JavaScriptTokenMarker;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.AbstractScriptablePlugin.ScriptEditor.DummyGnucashFile;
import biz.wolschon.finance.jgnucash.panels.WritableTransactionsPanel;
import biz.wolschon.numbers.FixedPointNumber;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib<br/>
 * ScriptEditorPanel.java<br/>
 * created: 09.10.2008 06:03:18 <br/>
 *<br/><br/>
 * This panel acts as an editor for scripts .
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public abstract class ScriptEditorPanel extends JPanel {


    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(ScriptEditorPanel.class.getName());

    /**
     * Every script in the HBCIImporter has a number to identify it.<br/>
     * When we edit a new script that has not yet been saved, it has already
     * been given a number.
     */
    private int myScriptNumber;

    /**
     * The description of the transaction.<br/>
     * May be null when editing existing scripts while
     * they are not running.
     */
    private String myInputText;

    /**
     * The date+time of the transaction.<br/>
     * May be null when editing existing scripts while
     * they are not running.
     */
    private Date myInputDate;

    /**
     * The value of the transaction.<br/>
     * May be null when editing existing scripts while
     * they are not running.
     */
    private FixedPointNumber myInputValue;

    //------------------ GUI-components

    /**
     * The area with the script-text.
     */
    private JEditTextArea myEditorArea;

    /**
     * The area with the regexp-text.<br/>
     * (The regular expression that a transaction's description
     * must match for the script to be invoked at all.)
     */
    private JTextField myRegExpArea;

    /**
     * Panel with the SAVE-button.
     */
    private JPanel myButtonsPanel;

    /**
     *  CANCEL-button.
     */
    private JButton myCancelButton;

    /**
     * Was this panel canceled?
     */
    private boolean wasCanceled = false;


    /**
     *  SAVE-button.
     */
    private JButton mySaveButton;

    /**
     * The button to test-run the script on a
     * dummy-database..
     */
    private JButton myTestrunButton;

    /**
     * The settings of the HBCIImporter.<br/>
     * They contain all installed scripts and
     * can be written to ~/.jgnucash/.hbci.properties .
     */
    private Properties mySettings;
    protected Properties getSettings() {
       return mySettings;
    }

    public abstract File getScriptFileName();
    public abstract File getConfigFile();
    /**
     *  @return the account with the created transactions
     *  @param dummyFile the gnucash-file to use.
     */
    public abstract GnucashWritableAccount doTestRun(final GnucashWritableFile dummyFile) throws javax.xml.bind.JAXBException;
    public abstract void addScriptToSettings(int aScriptNumber, String aRelativeFileName);

    /**
     * @param myScriptNumber the number of the new script to edit
     * @param myInputText The description of the transaction. (May be null)
     * @param myInputDate The date of the transaction. (May be null)
     * @param myInputValue The value of the transaction. (May be null)
     * @param properties The complete and current settings of the HBCIImporter.
     */
    public ScriptEditorPanel(final int myScriptNumber,
			                 final String myInputText,
			                 final Date myInputDate,
			                 final FixedPointNumber myInputValue,
			                 final Properties properties) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("biz/wolschon/finance/jgnucash/AbstractScriptablePlugin/defaultscript.js")));
		String line = null;
		StringBuilder defaultScript = new StringBuilder();
		try {
			while ((line = reader.readLine()) != null) {
				defaultScript.append(line).append('\n');
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE,"[IOException] Problem in "
			           + getClass().getName(),
			             e);
		}
		init(myScriptNumber,
				          defaultScript.toString(),
				          myInputText,
				          myInputDate,
				          myInputValue,
				          properties);
	}
    /**
	 * @param myScriptNumber the number of the new script to edit
	 * @param myScriptSourcecode the sourcecode of the script to edit
	 * @param myInputText The description of the transaction. (May be null)
	 * @param myInputDate The date of the transaction. (May be null)
	 * @param myInputValue The value of the transaction. (May be null)
     * @param properties The complete and current settings of the HBCIImporter.
	 */
	public ScriptEditorPanel(final int myScriptNumber,
                             final String myScriptSourcecode,
			                 final String myInputText,
			                 final Date myInputDate,
			                 final FixedPointNumber myInputValue,
			                 final Properties properties) {
		init(myScriptNumber,
				  myScriptSourcecode,
		          myInputText,
		          myInputDate,
		          myInputValue,
		          properties);
	}

    /**
	 * @param myScriptNumber the number of the new script to edit
	 * @param myScriptSourcecode the sourcecode of the script to edit
	 * @param myInputText The description of the transaction. (May be null)
	 * @param myInputDate The date of the transaction. (May be null)
	 * @param myInputValue The value of the transaction. (May be null)
     * @param properties The complete and current settings of the HBCIImporter.
	 */
	private void init(final int myScriptNumber,
                             final String myScriptSourcecode,
			                 final String myInputText,
			                 final Date myInputDate,
			                 final FixedPointNumber myInputValue,
			                 final Properties properties) {

		this.myScriptNumber = myScriptNumber;
		this.myInputText = myInputText;
		this.myInputDate = myInputDate;
		this.myInputValue = myInputValue;
		mySettings = properties;

		this.setLayout(new BorderLayout());

        this.add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(getRegExpArea()), new JScrollPane(getEditorArea(myScriptSourcecode))), BorderLayout.CENTER);
        //this.add(new JScrollPane(getRegExpArea()), BorderLayout.NORTH);
        this.add(getButtonsPanel(), BorderLayout.SOUTH);
    }

	/**
	 * The area with the script-text.
	 * @param aScriptSourcecode
	 * @return Returns the editorArea.
	 * @see #myEditorArea
	 */
	private JEditTextArea getEditorArea(final String aScriptSourcecode) {
		if (myEditorArea == null) {
			myEditorArea  = new JEditTextArea();
			//myEditorArea.setStyledDocument(new DefaultStyledDocument());
	        myEditorArea.setText(aScriptSourcecode);
	        myEditorArea.setPreferredSize(new Dimension(1000, 400));

	        //if (scriptLanguage.equals("JavaScript")) {
	        myEditorArea.setTokenMarker(new JavaScriptTokenMarker());
	       //     }
		}

		return myEditorArea;
	}
	/**
     * The area with the script-text.
     * @param aScriptSourcecode
     * @return Returns the editorArea.
     * @see #myEditorArea
     */
    protected JEditTextArea getEditorArea() {
        if (myEditorArea == null) {
            return getEditorArea("");
        }
        return myEditorArea;
    }
	/**
	 * The area with the regexp-text.<br/>
     * (The regular expression that a transaction's description
     * must match for the script to be invoked at all.)
	 * @return Returns the myRegExpArea.
	 * @see #myRegExpArea
	 */
	protected JTextField getRegExpArea() {
		if (myRegExpArea == null) {
			myRegExpArea = new JTextField();
			myRegExpArea.setText("(?s).*" + myInputText + ".*");

	        // color the textfield red, yellow or green
	        if (myInputText != null && myInputText.trim().length() > 0) {
	        	myRegExpArea.getDocument().addDocumentListener(new DocumentListener() {

					public void changed() {

						try {
							if (myInputText.matches(myRegExpArea.getText())) {
								myRegExpArea.setForeground(Color.green);
								myRegExpArea.setToolTipText("OK");
							} else {
								myRegExpArea.setForeground(Color.yellow);

								myRegExpArea.setToolTipText("does not mach:\n" + myInputText);
							}

						} catch (Exception e1) {
							myRegExpArea.setForeground(Color.red);
							myRegExpArea.setToolTipText(e1.getMessage());
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						changed();
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						changed();
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						changed();
					}

	        	});
	        }
		}

		return myRegExpArea;
	}

	/**
	 * @return Returns the wasCanceled.
	 * @see #wasCanceled
	 */
	public boolean wasCanceled() {
		return wasCanceled;
	}

	/**
	 * @return Returns the myScriptNumber.
	 * @see #myScriptNumber
	 */
	public int getMyScriptNumber() {
		return myScriptNumber;
	}

	/**
	 * @param myScriptNumber The myScriptNumber to set.
	 * @see #myScriptNumber
	 */
	public void setMyScriptNumber(final int myScriptNumber) {

		int old = this.myScriptNumber;
		if (old == myScriptNumber) {
			return; // nothing has changed
		}
		this.myScriptNumber = myScriptNumber;
		// <<insert code to react further to this change here
		PropertyChangeSupport propertyChangeFirer = getPropertyChangeSupport();
		if (propertyChangeFirer != null) {
			propertyChangeFirer.firePropertyChange("myScriptNumber", old,
					myScriptNumber);
		}
	}

    /**
     * @return Returns the myInputText.
     * @see #myInputText
     */
    public String getMyInputText() {
        return myInputText;
    }

    /**
     * @param newInputText The inputText to set.
     * @see #myInputText
     */
    public void setMyInputText(final String newInputText) {
        if (newInputText == null) {
            throw new IllegalArgumentException("null 'newInputText' given!");
        }

        Object old = myInputText;
        if (old == newInputText) {
            return; // nothing has changed
        }
        myInputText = newInputText;
        // <<insert code to react further to this change here
         getTestrunButton(); // undate the enabled-state of the button

        PropertyChangeSupport propertyChangeFirer = getPropertyChangeSupport();
        if (propertyChangeFirer != null) {
            propertyChangeFirer.firePropertyChange("myInputText", old, newInputText);
        }
    }

    /**
     * @return Returns the myInputDate.
     * @see #myInputDate
     */
    public Date getMyInputDate() {
        return myInputDate;
    }

    /**
     * @param newInputDate The inputDate to set.
     * @see #myInputDate
     */
    public void setMyInputDate(final Date newInputDate) {
        if (newInputDate == null) {
            throw new IllegalArgumentException("null 'myInputDate' given!");
        }

        Object old = myInputDate;
        if (old == newInputDate) {
            return; // nothing has changed
        }
        myInputDate = newInputDate;
        // <<insert code to react further to this change here
         getTestrunButton(); // undate the enabled-state of the button

        PropertyChangeSupport propertyChangeFirer = getPropertyChangeSupport();
        if (propertyChangeFirer != null) {
            propertyChangeFirer.firePropertyChange("myInputDate", old, newInputDate);
        }
    }

    /**
     * @return Returns the myInputValue.
     * @see #myInputValue
     */
    public FixedPointNumber getMyInputValue() {
        return myInputValue;
    }

    /**
     * @param myInputValue The myInputValue to set.
     * @see #myInputValue
     */
    public void setMyInputValue(final FixedPointNumber myInputValue) {
        if (myInputValue == null) {
            throw new IllegalArgumentException("null 'myInputValue' given!");
        }

        Object old = this.myInputValue;
        if (old == myInputValue) {
            return; // nothing has changed
        }
        this.myInputValue = myInputValue;
        // <<insert code to react further to this change here
         getTestrunButton(); // undate the enabled-state of the button

        PropertyChangeSupport propertyChangeFirer = getPropertyChangeSupport();
        if (propertyChangeFirer != null) {
            propertyChangeFirer.firePropertyChange("myInputValue", old,
                    myInputValue);
        }
    }


    /**
     * @return Returns the myButtonsPanel.
     * @see #myButtonsPanel
     */
    private JPanel getButtonsPanel() {
        if (myButtonsPanel == null) {
            myButtonsPanel = new JPanel();
            myButtonsPanel.setLayout(new GridLayout(1, 3));
            myButtonsPanel.add(getTestrunButton());
            myButtonsPanel.add(getCancelButton());
            myButtonsPanel.add(getSaveButton());
        }
        return myButtonsPanel;
    }

    /**
     * CANCEL-button.
     * @return Returns the myCancelButton.
     * @see #myCancelButton
     */
    private JButton getCancelButton() {
        if (myCancelButton == null) {
            myCancelButton = new JButton("Cancel");
            myCancelButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    wasCanceled = true;
                    getFrame().dispose();

                }

            });
        }

        return myCancelButton;
    }

    /**
     * SAVE-button.
     * @return Returns the mySaveButton.
     * @see #mySaveButton
     */
    private JButton getSaveButton() {
        if (mySaveButton == null) {
            mySaveButton = new JButton("Save");
            mySaveButton.addActionListener(new ActionListener() {


                @Override
                public void actionPerformed(final ActionEvent e) {
                    wasCanceled = false;

                    try {
                        File filename = getScriptFileName();
                        FileWriter fw = new FileWriter(filename);
                        fw.write(myEditorArea.getText());
                        fw.close();
                        addScriptToSettings(getMyScriptNumber(),
                                            getScriptFileName().getPath());
                        mySettings.store(new FileWriter(getConfigFile()), "saved by ScriptEditprPanel");
                    } catch (IOException e1) {
                        wasCanceled = true;
                        LOG.log(Level.SEVERE,"[IOException] Problem in "
                                   + getClass().getName(),
                                     e1);
                        return;
                    }

                    getFrame().dispose();
                }

            });
        }

        return mySaveButton;
    }

    /**
     * @return the frame that contains this panel.
     */
    private Window getFrame() {
        Container parent = ScriptEditorPanel.this.getParent();
        while (parent != null) {
            if (parent instanceof Window) {
                return (Window) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /**
     * This number is incremented with every testrun made.
     */
    private int testRunNumber = 0;

    /**
     * The button to test-run the script on a
     * dummy-database..
	 * @return Returns the myCancelButton.
	 * @see #myCancelButton
	 */
	private JButton getTestrunButton() {
		if (myTestrunButton == null) {
			myTestrunButton = new JButton("Testrun");
			myTestrunButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					//TODO: test this

					try {
						GnucashWritableFile dummyFile = new DummyGnucashFile(this.getClass().getClassLoader());
						testRunNumber++;

						GnucashWritableAccount bankAccount = doTestRun(dummyFile);
						// show the result in a new window

						final JDialog resultFrame = new JDialog(getFrame(), ModalityType.APPLICATION_MODAL);
						resultFrame.setTitle("Result of testrun # " + testRunNumber + " on " + DateFormat.getDateTimeInstance().format(new Date()));
						resultFrame.getContentPane().setLayout(new BorderLayout());

						resultFrame.add(new WritableTransactionsPanel(bankAccount), BorderLayout.CENTER);
						JButton closeButton = new JButton("Close");
						closeButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(final ActionEvent aE) {
								resultFrame.setVisible(false);
								resultFrame.dispose();
							}
						});
						resultFrame.add(closeButton, BorderLayout.SOUTH);

						resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						resultFrame.pack();
						resultFrame.setVisible(true);
					} catch (Exception e1) {
						LOG.log(Level.SEVERE,"[Exception] Problem in "
						           + getClass().getName(),
						             e1);
						JOptionPane.showMessageDialog(null, "Error",
								"Testrun failed.\n"
								+ "[" + e1.getClass().getName() + "]: " + e1.getMessage(),
								JOptionPane.ERROR_MESSAGE);
					}

				}

			});
		}

		myTestrunButton.setEnabled(getMyInputText() != null
				                    && getMyInputDate() != null
				                    && getMyInputValue() != null);
		return myTestrunButton;
	}

    //------------------------ support for propertyChangeListeners ------------------

	/**
     * support for firing PropertyChangeEvents..
     * (gets initialized only if we really have listeners)
     */
    private volatile PropertyChangeSupport myPropertyChange = null;

    /**
     * Returned value may be null if we never had listeners.
     * @return Our support for firing PropertyChangeEvents
     */
    protected PropertyChangeSupport getPropertyChangeSupport() {
        return myPropertyChange;
    }

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     *
     * @param listener  The PropertyChangeListener to be added
     */
    @Override
    public final void addPropertyChangeListener(
            final PropertyChangeListener listener) {
        if (myPropertyChange == null) {
            myPropertyChange = new PropertyChangeSupport(this);
        }
        myPropertyChange.addPropertyChangeListener(listener);
    }

    /**
     * Add a PropertyChangeListener for a specific property.  The listener
     * will be invoked only when a call on firePropertyChange names that
     * specific property.
     *
     * @param propertyName  The name of the property to listen on.
     * @param listener  The PropertyChangeListener to be added
     */
    @Override
    public final void addPropertyChangeListener(final String propertyName,
            final PropertyChangeListener listener) {
        if (myPropertyChange == null) {
            myPropertyChange = new PropertyChangeSupport(this);
        }
        myPropertyChange.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     *
     * @param propertyName  The name of the property that was listened on.
     * @param listener  The PropertyChangeListener to be removed
     */
    @Override
    public final void removePropertyChangeListener(final String propertyName,
            final PropertyChangeListener listener) {
        if (myPropertyChange != null) {
            myPropertyChange.removePropertyChangeListener(propertyName,
                    listener);
        }
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     *
     * @param listener  The PropertyChangeListener to be removed
     */
    @Override
    public synchronized void removePropertyChangeListener(
            final PropertyChangeListener listener) {
        if (myPropertyChange != null) {
            myPropertyChange.removePropertyChangeListener(listener);
        }
    }

    //-------------------------------------------------------

    /**
     * Just an overridden ToString to return this classe's name
     * and hashCode.
     * @return className and hashCode
     */
    @Override
    public String toString() {
        return "DebugLogPanel@" + hashCode();
    }
}


