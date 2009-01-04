/**
 * SSHDialog.java
 * created: 10.12.2008
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

package biz.wolschon.finance.jgnucash.ssh;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * SSHDialog<br/>
 * created: 10.12.2008 <br/>
 *<br/><br/>
 * <b>Dialog to ask for SSH-parameters.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class SSHDialog extends JDialog {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(SSHDialog.class
            .getName());

    /**
     * The panel containing the data of the host to conect to.
     */
    private JPanel myRemoteSystemPanel;

    /**
     * The panel containing the data for an optional SSH-tunnel.
     */
    private JPanel mySSHTunnelPanel;

    /**
     * The panel containing the panels.
     */
    private JPanel mySystemPanel;

    /**
     * The panel containing the buttons.
     */
    private JPanel myButtonsPanel;

    /**
     * The button to finish entering data.
     */
    private JButton myConnectButton;

    /**
     * The user to connect width.
     */
    private JTextField myRemoteUser;

    /**
     * The port to connect to.
     */
    private JTextField myRemotePort;

    /**
     * The host to connect to.
     */
    private JTextField myRemoteHost;

    /**
     * The path to load from/store to.
     */
    private JTextField myRemotePath;

//    /**
//     * The password to connect with.
//     */
//    private JPasswordField myRemotePassword;

    /**
     * Label for {@link SSHDialog#myRemoteUser}.
     */
    private JLabel myRemoteUserLabel;

    /**
     * Label for {@link SSHDialog#myRemotePort}.
     */
    private JLabel myRemotePortLabel;

    /**
     * Label for {@link SSHDialog#myRemoteHost}.
     */
    private JLabel myRemoteHostLabel;

//    /**
//     * Label for {@link SSHDialog#myRemotePassword}.
//     */
//    private JLabel myRemotePasswordLabel;

    /**
     * Label for {@link SSHDialog#myRemotePath}.
     */
    private JLabel myRemotePathLabel;

    private JCheckBox mySSHTunnelEnabled;

    private JLabel mySSHTunnelEnabledLabel;

    private JLabel mySSHTunnelHostLabel;

    private JTextField mySSHTunnelHost;

    private JLabel mySSHTunnelPortLabel;

    private JTextField mySSHTunnelPort;

    private JLabel mySSHTunnelUserLabel;

    private JTextField mySSHTunnelUser;

    //private JLabel mySSHTunnelPasswordLabel;

    //private JPasswordField mySSHTunnelPassword;

    /**
     * Show a new SSH-dialog.
     */
    public SSHDialog() {
        super((JFrame) null, true);
        initGUI();
        File configFile = getConfigFile();
        if (configFile.exists()) {
            try {
                Properties prop = new Properties();
                prop.loadFromXML(new FileInputStream(configFile));
                myRemoteHost.setText(prop.getProperty("remoteHost", "somehost"));
                myRemotePort.setText(prop.getProperty("remotePort", "22"));
                myRemoteUser.setText(prop.getProperty("remoteUser", System.getProperty("user.name")));
                myRemotePath.setText(prop.getProperty("remotePath", "/data"));
                mySSHTunnelEnabled.setSelected(prop.getProperty("sshTunnelEnabled", "false").equalsIgnoreCase("true"));
                mySSHTunnelHost.setText(prop.getProperty("sshTunnelHost", "somehost"));
                mySSHTunnelPort.setText(prop.getProperty("sshTunnelPort", "22"));
                mySSHTunnelUser.setText(prop.getProperty("sshTunnelUser", System.getProperty("user.name")));
                checkSSHTunnelPanelEnabled();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Cannot read properties-file with the last used SSH-settings", e);
            }
        }
    }

    /**
     * @return the configuration-file (need not exist yet)
     */
    protected File getConfigFile() {
        return new File(getConfigFileDirectory(), "ssh.xml");
    }

    /**
     * @return The directory where we store config-files.
     */
    protected File getConfigFileDirectory() {
        return new File(System.getProperty("user.home", "~"), ".jgnucash");
    }

    /**
     * Create the graphical input-components.
     */
    private void initGUI() {

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getSystemPanel(), BorderLayout.CENTER);
        this.getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
        this.pack();
    }
    /**
     * @return The panel containing the data of the host to conect to.
     */
    private JPanel getRemoteSystemPanel() {
        if (myRemoteSystemPanel == null) {
            myRemoteSystemPanel = new JPanel();
            myRemoteSystemPanel.setLayout(new GridLayout(4, 2));
            myRemoteHostLabel = new JLabel("Host:");
            myRemoteSystemPanel.add(myRemoteHostLabel);
            myRemoteHost = new JTextField("somehost");
            myRemoteSystemPanel.add(myRemoteHost);

            myRemotePortLabel = new JLabel("Port:");
            myRemoteSystemPanel.add(myRemotePortLabel);
            myRemotePort = new JTextField("22");
            myRemoteSystemPanel.add(myRemotePort);

            myRemoteUserLabel = new JLabel("User:");
            myRemoteSystemPanel.add(myRemoteUserLabel);
            myRemoteUser = new JTextField(System.getProperty("user.name"));
            myRemoteSystemPanel.add(myRemoteUser);

//            myRemotePasswordLabel = new JLabel("Password:");
//            myRemoteSystemPanel.add(myRemotePasswordLabel);
//            myRemotePassword = new JPasswordField();
//            myRemoteSystemPanel.add(myRemotePassword);

            myRemotePathLabel = new JLabel("Path:");
            myRemoteSystemPanel.add(myRemotePathLabel);
            myRemotePath = new JTextField();
            myRemoteSystemPanel.add(myRemotePath);
        }
        return myRemoteSystemPanel;
    }

    /**
     * @return The panel containing the panels.
     */

    private JPanel getSSHTunnelPanel() {
        if (mySSHTunnelPanel == null) {
            mySSHTunnelPanel = new JPanel();
            mySSHTunnelPanel.setLayout(new GridLayout(4, 1));
            mySSHTunnelPanel.setBorder(new EtchedBorder());

            mySSHTunnelEnabledLabel = new JLabel("connect through SSH-tunnel?");
            mySSHTunnelEnabled =  new JCheckBox();
            mySSHTunnelPanel.add(mySSHTunnelEnabled);
            mySSHTunnelPanel.add(mySSHTunnelEnabledLabel);

            mySSHTunnelHostLabel = new JLabel("Tunnel-Host:");
            mySSHTunnelPanel.add(mySSHTunnelHostLabel);
            mySSHTunnelHost = new JTextField("somehost");
            mySSHTunnelPanel.add(mySSHTunnelHost);

            mySSHTunnelPortLabel = new JLabel("Port:");
            mySSHTunnelPanel.add(mySSHTunnelPortLabel);
            mySSHTunnelPort = new JTextField("22");
            mySSHTunnelPanel.add(mySSHTunnelPort);

            mySSHTunnelUserLabel = new JLabel("User:");
            mySSHTunnelPanel.add(mySSHTunnelUserLabel);
            mySSHTunnelUser = new JTextField(System.getProperty("user.name"));
            mySSHTunnelPanel.add(mySSHTunnelUser);

//            mySSHTunnelPasswordLabel = new JLabel("Password:");
//            mySSHTunnelPanel.add(mySSHTunnelPasswordLabel);
//            mySSHTunnelPassword = new JPasswordField();
//            mySSHTunnelPanel.add(mySSHTunnelPassword);

            mySSHTunnelEnabled.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent aE) {
                    checkSSHTunnelPanelEnabled();
                }
            });
            mySSHTunnelEnabled.setSelected(false);
            checkSSHTunnelPanelEnabled();
        }
        return mySSHTunnelPanel;
    }

    /**
     * Check the state of {@link SSHDialog#mySSHTunnelEnabledLabel} and
     * set the enabled-status if the text-fields in the SSHTunnelPanel.
     */
    private void checkSSHTunnelPanelEnabled() {
        //mySSHTunnelPassword.setEnabled(mySSHTunnelEnabled.isSelected());
        mySSHTunnelHost.setEnabled(mySSHTunnelEnabled.isSelected());
        mySSHTunnelPort.setEnabled(mySSHTunnelEnabled.isSelected());
        mySSHTunnelUser.setEnabled(mySSHTunnelEnabled.isSelected());
    }

    /**
     * @return The panel containing the panels.
     */
    private JPanel getSystemPanel() {
        if (mySystemPanel == null) {
            mySystemPanel = new JPanel();
            mySystemPanel.setLayout(new GridLayout(3, 1));
            mySystemPanel.add(getRemoteSystemPanel());
            mySystemPanel.add(getSSHTunnelPanel());
            //TODO: HttpTunnelPanel
        }
        return mySystemPanel;
    }

    /**
     * @return The panel containing the buttons.
     */
    private JPanel getButtonsPanel() {
        if (myButtonsPanel == null) {
            myButtonsPanel = new JPanel();
            myConnectButton = new JButton("Connect");
            myConnectButton.setDefaultCapable(true);
            myButtonsPanel.add(myConnectButton);
            myConnectButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent aE) {
                    SSHDialog.this.setVisible(false);
                    try {
                        Properties prop = new Properties();
                        prop.setProperty("remoteHost", myRemoteHost.getText());
                        prop.setProperty("remotePort", myRemotePort.getText());
                        prop.setProperty("remoteUser", myRemoteUser.getText());
                        prop.setProperty("remotePath", myRemotePath.getText());
                        prop.setProperty("sshTunnelEnabled", mySSHTunnelEnabled.isSelected()?"true":"false");
                        prop.setProperty("sshTunnelHost", mySSHTunnelHost.getText());
                        prop.setProperty("sshTunnelPort", mySSHTunnelPort.getText());
                        prop.setProperty("sshTunnelUser", mySSHTunnelUser.getText());
                        getConfigFileDirectory().mkdirs();
                        prop.storeToXML(new FileOutputStream(getConfigFile()), "");
                    } catch (Exception e) {
                        LOG.log(Level.WARNING, "Cannot write properties-file with the last used SSH-settings", e);
                    }
                }
            });
        }
        return myButtonsPanel;
    }

    /**
     * @return the host, user and password to connect through or null
     * @see #getRemotePath()
     */
    public ConnectInfo getRemoteHostUserInfo() {
        if (!isSHHTunneling()) {
            return null;
        }
        return new ConnectInfo(myRemoteUser.getText(),
                null, //new String(myRemotePassword.getPassword()),
                myRemoteHost.getText(),
                Integer.parseInt(myRemotePort.getText()));
    }

    /**
     * @return the path to load from/store to
     */
    public String getRemotePath() {
        return myRemotePath.getText();
    }

    /**
     * @return true if the user wants to ssh-tunnel into a system behind  router
     */
    public boolean isSHHTunneling() {
        return mySSHTunnelEnabled.isSelected();
    }

    /**
     * @return the host, user and password an SSH-tunnel
     * @see #isSHHTunneling()
     */
    public ConnectInfo getSSHTunnelUserInfo() {
        return new ConnectInfo(mySSHTunnelUser.getText(),
                null,//new String(mySSHTunnelPassword.getPassword()),
                mySSHTunnelHost.getText(),
                Integer.parseInt(mySSHTunnelPort.getText()));
    }

}
