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
package biz.wolschon.finance.jgnucash.HBCIImporter;

//other imports

//automatically created logger for debug and error -output
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

//automatically created propertyChangeListener-Support
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;

import biz.wolschon.numbers.FixedPointNumber;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib<br/>
 * ScriptEditorPanel.java<br/>
 * created: 28.09.2008 15:03:18 <br/>
 *<br/><br/>
 * This panel acts as an editor for scripts to the HBCIImporter..
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class ScriptEditorPanel extends JPanel {


	/**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(
    		ScriptEditorPanel.class.getName());

    private int myScriptNumber;
    private String myInputText;
    private Date myInputDate;
    private FixedPointNumber myInputValue;
    
    /**
     * The area with the script-text.
     */
    private JTextPane myEditorArea = new JTextPane();

    /**
     * The area with the regexp-text.
     */
    private JTextField myRegExpArea = new JTextField();

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

	private Properties mySettings;

    /**
	 * @param myScriptNumber
	 * @param myInputText
	 * @param myInputDate
	 * @param myInputValue
     * @param properties 
	 */
	public ScriptEditorPanel(final int myScriptNumber,
			                 final String myInputText,
			                 final Date myInputDate,
			                 final FixedPointNumber myInputValue,
			                 final Properties properties) {
		this.myScriptNumber = myScriptNumber;
		this.myInputText = myInputText;
		this.myInputDate = myInputDate;
		this.myInputValue = myInputValue;
		this.mySettings = properties;

		this.setLayout(new BorderLayout());
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("biz/wolschon/finance/jgnucash/HBCIImporter/defaultscript.js")));
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
        this.myEditorArea.setStyledDocument(new DefaultStyledDocument());
        this.myEditorArea.setText(defaultScript.toString());
        this.myEditorArea.setPreferredSize(new Dimension(1000, 400));
        this.add(new JScrollPane(this.myEditorArea), BorderLayout.CENTER);

        this.myRegExpArea.setText("(?s).*" + myInputText + ".*");
        this.add(new JScrollPane(this.myRegExpArea), BorderLayout.NORTH);
        this.add(getButtonsPanel(), BorderLayout.SOUTH);

        // color the textfield red, yellow or green
        if (myInputText != null && myInputText.trim().length() > 0) {
        	this.myRegExpArea.getDocument().addDocumentListener(new DocumentListener() {

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
	 * @param myInputText The myInputText to set.
	 * @see #myInputText
	 */
	public void setMyInputText(final String myInputText) {
		if (myInputText == null) {
			throw new IllegalArgumentException("null 'myInputText' given!");
		}
	
		Object old = this.myInputText;
		if (old == myInputText) {
			return; // nothing has changed
		}
		this.myInputText = myInputText;
		// <<insert code to react further to this change here
		PropertyChangeSupport propertyChangeFirer = getPropertyChangeSupport();
		if (propertyChangeFirer != null) {
			propertyChangeFirer.firePropertyChange("myInputText", old, myInputText);
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
	 * @param myInputDate The myInputDate to set.
	 * @see #myInputDate
	 */
	public void setMyInputDate(final Date myInputDate) {
		if (myInputDate == null) {
			throw new IllegalArgumentException("null 'myInputDate' given!");
		}
	
		Object old = this.myInputDate;
		if (old == myInputDate) {
			return; // nothing has changed
		}
		this.myInputDate = myInputDate;
		// <<insert code to react further to this change here
		PropertyChangeSupport propertyChangeFirer = getPropertyChangeSupport();
		if (propertyChangeFirer != null) {
			propertyChangeFirer.firePropertyChange("myInputDate", old, myInputDate);
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
		if (this.myButtonsPanel == null) {
			this.myButtonsPanel = new JPanel();
			this.myButtonsPanel.setLayout(new GridLayout(1, 2));
			this.myButtonsPanel.add(getMyCancelButton());
			this.myButtonsPanel.add(getMySaveButton());
		}
		return myButtonsPanel;
	}

    /**
	 * @return Returns the myCancelButton.
	 * @see #myCancelButton
	 */
	private JButton getMyCancelButton() {
		if (this.myCancelButton == null) {
			this.myCancelButton = new JButton("Cancel");
			this.myCancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					ScriptEditorPanel.this.wasCanceled = true;
					Container parent = ScriptEditorPanel.this.getParent();
					while (parent != null) {
						if (parent instanceof JFrame) {
							JFrame frame = (JFrame) parent;
							frame.setVisible(false);
							frame.dispose();
							return;
						}
						if (parent instanceof JDialog) {
							JDialog frame = (JDialog) parent;
							frame.setVisible(false);
							frame.dispose();
							return;
						}
						parent = parent.getParent();
					}
					
				}
				
			});
		}

		return this.myCancelButton;
	}

	/**
	 * @return Returns the mySaveButton.
	 * @see #mySaveButton
	 */
	private JButton getMySaveButton() {
		if (this.mySaveButton == null) {
			this.mySaveButton = new JButton("Save");
			this.mySaveButton.addActionListener(new ActionListener() {

				
				@Override
				public void actionPerformed(final ActionEvent e) {
					ScriptEditorPanel.this.wasCanceled = false;
					
					try {
						File dir = new File(Main.getConfigFileDirectory(), "import_scripts");
						File filename = new File(dir, getMyScriptNumber() + ".js");
						dir.mkdirs();
						FileWriter fw = new FileWriter(filename);
						fw.write(myEditorArea.getText());
						fw.close();
						ScriptEditorPanel.this.mySettings.setProperty(HBCIImporter.SETTINGS_PREFIX_IMPORTSCRIPT + getMyScriptNumber(), "import_scripts" + File.separator + getMyScriptNumber() + ".js");
						ScriptEditorPanel.this.mySettings.setProperty(HBCIImporter.SETTINGS_PREFIX_IMPORTSCRIPT_REGEXP + getMyScriptNumber(), myRegExpArea.getText());
						ScriptEditorPanel.this.mySettings.setProperty(HBCIImporter.SETTINGS_PREFIX_IMPORTSCRIPT_LANGUAGE + getMyScriptNumber(), "javascript");
						ScriptEditorPanel.this.mySettings.store(new FileWriter(Main.getConfigFile()), "saved by ScriptEditprPanel");
					} catch (IOException e1) {
						ScriptEditorPanel.this.wasCanceled = true;
						LOG.log(Level.SEVERE,"[IOException] Problem in "
						           + getClass().getName(),
						             e1);
						return;
					}
					
					Container parent = ScriptEditorPanel.this.getParent();
					while (parent != null) {
						if (parent instanceof JFrame) {
							JFrame frame = (JFrame) parent;
							frame.setVisible(false);
							frame.dispose();
							return;
						}
						if (parent instanceof JDialog) {
							JDialog frame = (JDialog) parent;
							frame.setVisible(false);
							frame.dispose();
							return;
						}
						parent = parent.getParent();
					}
				}
				
			});
		}

		return this.mySaveButton;
	}

    //------------------------ support for propertyChangeListeners ------------------

	/**
     * support for firing PropertyChangeEvents.
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
    public String toString() {
        return "DebugLogPanel@" + hashCode();
    }
}


