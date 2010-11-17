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
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import biz.wolschon.fileformats.gnucash.GnucashAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.finance.jgnucash.actions.AccountAction;

import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.propertysheet.PropertySheetTableModel;

/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * AccountProperties<br/>
 * created: 19.06.2009 <br/>
 *<br/><br/>
 * <b>Action to open an the properties of an account in a new tab.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class AccountProperties implements AccountAction, ClipboardOwner {

    /**
     * Our logger for debug- and error-output.
     */
    private static final Log LOGGER = LogFactory.getLog(AccountProperties.class);

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
     * Popup-menu on properties.
     */
    private JPopupMenu myPropertyMenu;

    /**
     * The menu-item in the {@link #myPropertyMenu} to remove a custom attribute.
     */
    private JMenuItem myRemoveMenuItem;

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
        if (myAccount != null) {
            LOGGER.debug("setAccount(" +  myAccount.getName() + ")");
            updateCustomAttributesPanel();
        }
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
        final JTextField disabledIDInput = new JTextField(myAccount.getId());
        final JPopupMenu accountIDPopupMenu = createAccountIDPopupMenu();
        disabledIDInput.setEditable(false);
        disabledIDInput.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(final MouseEvent arg0) {
                if (arg0.isPopupTrigger()) {
                    accountIDPopupMenu.show(disabledIDInput, arg0.getX(), arg0.getY());
                }
            }

            @Override
            public void mousePressed(final MouseEvent arg0) {
                if (arg0.isPopupTrigger()) {
                    accountIDPopupMenu.show(disabledIDInput, arg0.getX(), arg0.getY());
                }
            }

        });
        newPanel.add(disabledIDInput);

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

    private JPopupMenu createAccountIDPopupMenu() {
        final JPopupMenu accountIDPopupMenu = new JPopupMenu();
        JMenuItem copyAccountIDMenuItem = new JMenuItem("copy");
        copyAccountIDMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection( myAccount.getId() ), AccountProperties.this);
            }
        });
        accountIDPopupMenu.add(copyAccountIDMenuItem);
        return accountIDPopupMenu;
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

            myPropertySheet.addPropertySheetChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(final PropertyChangeEvent aEvt) {
                    Object property = aEvt.getSource();
                    if (property instanceof DefaultProperty) {
                        DefaultProperty prop = (DefaultProperty) property;
                        try {
                            myAccount.setUserDefinedAttribute(prop.getName(),
                                    prop.getValue().toString());
                        } catch (Exception e) {
                            LOGGER.error("error in writing userDefinedAttribute", e);
                        }
                    }
                }

            });
            myPropertySheet.getTable().addMouseListener(new MouseAdapter() {

                /** show popup if mouseReleased is a popupTrigger on this platform.
                 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
                 */
                @Override
                public void mouseReleased(final MouseEvent aE) {
                    if (aE.isPopupTrigger()) {
                        JPopupMenu menu = getPropertyPopup();
                        menu.show(myPropertySheet, aE.getX(), aE.getY());
                    }
                    super.mouseClicked(aE);
                }
                /** show popup if mousePressed is a popupTrigger on this platform.
                 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
                 */
                @Override
                public void mousePressed(final MouseEvent aE) {
                    if (aE.isPopupTrigger()) {
                        JPopupMenu menu = getPropertyPopup();
                        menu.show(myPropertySheet, aE.getX(), aE.getY());
                    }
                    super.mouseClicked(aE);
                }

            });
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
        if (myPropertySheet == null) {
            LOGGER.debug("updateCustomAttributesPanel() myPropertySheet is null");
            return;
        }
        Property[] properties = myPropertySheet.getProperties();
        if (properties != null) {
            LOGGER.debug("updateCustomAttributesPanel() "
                    + properties.length + " attributes to remove from panel");
            for (Property property : properties) {
                myPropertySheet.removeProperty(property);
            }
        } else {
            LOGGER.debug("updateCustomAttributesPanel() "
                    + "no attributes to remove from panel");
        }

        Collection<String> keys = myAccount.getUserDefinedAttributeKeys();
        LOGGER.debug("updateCustomAttributesPanel() #UserDefinedAttributeKeys=" + keys.size());
        for (String key : keys) {
            DefaultProperty property = new DefaultProperty();
            property.setName(key);
            property.setDisplayName(key);
            property.setEditable(true);
            //property.setCategory("");
            property.setType(String.class);
            property.setValue(myAccount.getUserDefinedAttribute(key));
            myPropertySheet.addProperty(property);
        }
        // remove the dummy-slots created in GnucashObjectImpl (cannot save empty xml-slots-entity)
        if (myPropertySheet.getProperties().length > 0
                && myPropertySheet.getProperties()[0].getName().equals("dummy")) {
            myPropertySheet.removeProperty(myPropertySheet.getProperties()[0]);
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
                        LOGGER.debug("adding user-defined attribute '"
                                + getCustomAttributeName().getText() + "' to '"
                                + getCustomAttributeValue().getText() + "'");
                        (myAccount).setUserDefinedAttribute(getCustomAttributeName().getText(), getCustomAttributeValue().getText());
                        updateCustomAttributesPanel();
                    } catch (Exception e) {
                        LOGGER.error("error in updateCustomAttributesPanel", e);
                    }
                }

            });
        }
        return myAddCustomAttributeButton;
    }


    /**
     * @return Popup-menu on properties.
     */
    protected JPopupMenu getPropertyPopup() {
        if (myPropertyMenu == null) {
            myPropertyMenu = new JPopupMenu();
            myPropertyMenu.add(getRemoveMenuItem());
        }
        return myPropertyMenu;
    }

    /**
     * @return The menu-item in the {@link #myPropertyMenu} to remove a custom attribute.
     */
    private JMenuItem getRemoveMenuItem() {
        if (myRemoveMenuItem == null) {
            myRemoveMenuItem = new JMenuItem("remove");
            myRemoveMenuItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent aE) {
                    int selectedRow = myPropertySheet.getTable().getSelectedRow();
                    PropertySheetTableModel model = ((PropertySheetTableModel) myPropertySheet.getTable().getModel());
                    LOGGER.debug("selected for deletion: #" + selectedRow + " of " + model.getProperties().length);
                    Property property = model.getProperties()[selectedRow - 1];
                    LOGGER.debug("selected for deletion: " + property.getName());
                    model.removeProperty(property);
                }

            });
        }
        return myRemoveMenuItem;
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
     */
    @Override
    public void lostOwnership(final Clipboard arg0, final Transferable arg1) {
        // ignored
    }
}
