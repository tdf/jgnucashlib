/**
 * AccountProperties.java
 * created: 19.06.2009
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jgnucashLib-GPL by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 *  jgnucashLib-GPL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jgnucashLib-GPL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jgnucashLib-V1.  If not, see <http://www.gnu.org/licenses/>.
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

package biz.wolschon.finance.jgnucash.accountProperties;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.finance.jgnucash.actions.AccountAction;

import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * AccountProperties<br/>
 * created: 19.06.2009 <br/>
 *<br/><br/>
 * <b>Action to open an the properties of an account in a new tab.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class AccountProperties implements AccountAction {

    /**
     * The account we open.
     */
    private GnucashWritableAccount myAccount;

    /**
     * @see #getValue(String)
     */
    private final Map<String, Object> myAddedTags = new HashMap<String, Object>();

    /**
     * @see #addPropertyChangeListener(PropertyChangeListener)
     */
    private final PropertyChangeSupport myPropertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * The JPanel with the slot-values.
     */
    private JPanel mySettingsPanel;

    /**
     * The Panel with the controls to add a custom attribute.
     */
    private JPanel myAddCustomAttrPanel;

    /**
     * The text-field to enter the name of a new custom attribute.
     * @see #myAddCustomAttrPanel
     */
    private JTextField myCustomAttributeName;

    /**
     * The text-field to enter the value of a new custom attribute.
     * @see #myAddCustomAttrPanel
     */
    private JTextField myCustomAttributeValue;

    /**
     * The button to add a new custom attribute.
     */
    private JButton myAddCustomAttributeButton;

    /**
     * The PropertySheet with the custom attributes.
     */
    private PropertySheetPanel myPropertySheet;

    /**
     * The panel with the close-button.
     */
    private JPanel myButtonsPanel;

    /**
     * The frame we show everything in.
     */
    private JFrame myFrame;

    /**
     * The button to close the frame.
     */
    private JButton myCloseButton;

    /**
     * Initialize.
     */
    public AccountProperties() {
        this.putValue(Action.NAME, "Account Properties...");
        this.putValue(Action.LONG_DESCRIPTION, "Show the properties of an account.");
        this.putValue(Action.SHORT_DESCRIPTION, "Show the properties of an account.");
    }

    /**
     * @param anAccount the account to show.
     */
    public AccountProperties(final GnucashAccount anAccount) {
        this();
        setAccount(anAccount);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccount(final GnucashAccount anAccount) {
        myAccount = (GnucashWritableAccount) anAccount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener aListener) {
        myPropertyChangeSupport.addPropertyChangeListener(aListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue(final String aKey) {
        return myAddedTags.get(aKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return myAccount != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putValue(final String aKey, final Object aValue) {
        myAddedTags.put(aKey, aValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener aListener) {
        myPropertyChangeSupport.removePropertyChangeListener(aListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(final boolean aB) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(final ActionEvent aE) {
        JPanel newPanel = new JPanel(new GridLayout(2, 2));


        newPanel.add(new JLabel("GUID:"));
        newPanel.add(new JLabel(myAccount.getId()));

        newPanel.add(new JLabel("name:"));
        final JTextField nameInput = new JTextField(myAccount.getName());
        nameInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent aE) {
                myAccount.setName(nameInput.getText());
            }

        });
        newPanel.add(nameInput);

        myFrame = new JFrame(myAccount.getName());
        myFrame.getContentPane().setLayout(new BorderLayout());
        myFrame.getContentPane().add(newPanel, BorderLayout.NORTH);
        myFrame.getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);

        myFrame.getContentPane().add(getMySettingsPanel(), BorderLayout.CENTER);
        myFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        myFrame.pack();
        myFrame.setVisible(true);
    }

    /**
     * @return The panel with the close-button.
     */
    private JPanel getButtonsPanel() {
        if (myButtonsPanel == null) {
            myButtonsPanel = new JPanel(new BorderLayout());
            myButtonsPanel.add(getCloseButton(), BorderLayout.CENTER);
        }
        return myButtonsPanel;
    }

    /**
     * @return the close-button
     */
    private JButton getCloseButton() {
        if (myCloseButton == null) {
            myCloseButton = new JButton("close");
            myCloseButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent aE) {
                    myFrame.setVisible(false);
                }

            });
        }
        return myCloseButton;
    }

    /**
     * @return a panel to edit the settings of this section
     */
    private JPanel getMySettingsPanel() {
        if (mySettingsPanel == null) {
            mySettingsPanel = new JPanel();

            mySettingsPanel.setLayout(new BorderLayout());
            myPropertySheet = new PropertySheetPanel();
            myPropertySheet.setToolBarVisible(true);
            myPropertySheet.setSorting(false);
            myPropertySheet.setMode(PropertySheetPanel.VIEW_AS_CATEGORIES);
            myPropertySheet.setDescriptionVisible(true);

            //        SavingPropertyChangeListener savingPropertyChangeListener = new SavingPropertyChangeListener();
            //
            updateCustomAttributesPanel();
            //        for (ConfigurationSetting setting : getConfigSection().getSettings()) {
            //            MyProperty myProperty = new MyProperty(setting);
            //            myProperty.addPropertyChangeListener(savingPropertyChangeListener);
            //            propertySheet.addProperty(myProperty);
            //        }
            mySettingsPanel.add(new JLabel("custom attributes:"), BorderLayout.NORTH);
            mySettingsPanel.add(myPropertySheet, BorderLayout.CENTER);
            mySettingsPanel.add(getAddCustomAttrPanel(), BorderLayout.SOUTH);

            //        MyPropertyEditorFactory propertyEditorFactory = new MyPropertyEditorFactory();
            //        propertySheet.setEditorFactory(propertyEditorFactory);
            //        propertySheet.setRendererFactory(propertyEditorFactory);

        }
        return mySettingsPanel;
    }

    /**
     * Update the PropertySheet.
     */
    private void updateCustomAttributesPanel() {
        Property[] properties = myPropertySheet.getProperties();
        for (Property property : properties) {
            myPropertySheet.removeProperty(property);
        }

        Collection<String> keys = myAccount.getUserDefinedAttributeKeys();
        for (String key : keys) {
            DefaultProperty property = new DefaultProperty();
            property.setDisplayName(key);
            property.setValue(myAccount.getUserDefinedAttribute(key));
            myPropertySheet.addProperty(property);
        }
    }

    /**
     * @return The Panel with the controls to add a custom attribute.
     */
    private JPanel getAddCustomAttrPanel() {
        if (myAddCustomAttrPanel == null) {
            final int rowCount = 3;
            myAddCustomAttrPanel = new JPanel(new GridLayout(rowCount, 2));
            myAddCustomAttrPanel.add(new JLabel("Name:"));
            myAddCustomAttrPanel.add(getCustomAttributeName());
            myAddCustomAttrPanel.add(new JLabel("Value:"));
            myAddCustomAttrPanel.add(getCustomAttributeValue());
            myAddCustomAttrPanel.add(new JLabel(""));
            myAddCustomAttrPanel.add(getAddCustomAttributeButton());
        }
        return myAddCustomAttrPanel;
    }
    /**
     * @return The text-field to enter the name of a new custom attribute.
     */
    private JTextField getCustomAttributeName() {
        if (myCustomAttributeName == null) {
            myCustomAttributeName = new JTextField();
        }
        return myCustomAttributeName;
    }

    /**
     * @return The text-field to enter the value of a new custom attribute.
     */
    private JTextField getCustomAttributeValue() {
        if (myCustomAttributeValue == null) {
            myCustomAttributeValue = new JTextField();
        }
        return myCustomAttributeValue;
    }

    /**
     * @return the button to add a new custom attribute.
     */
    private JButton getAddCustomAttributeButton() {
        if (myAddCustomAttributeButton == null) {
            myAddCustomAttributeButton = new JButton("add");
            myAddCustomAttributeButton.setEnabled(myAccount instanceof GnucashWritableAccount);
            myAddCustomAttributeButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent aE) {
                    try {
                        (myAccount).setUserDefinedAttribute(getCustomAttributeName().getText(), getCustomAttributeValue().getText());
                        updateCustomAttributesPanel();
                    } catch (JAXBException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            });
        }
        return myAddCustomAttributeButton;
    }


}
