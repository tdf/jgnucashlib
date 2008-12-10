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
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

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

    /**
     * The password to connect with.
     */
    private JPasswordField myRemotePassword;

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

    /**
     * Label for {@link SSHDialog#myRemotePassword}.
     */
    private JLabel myRemotePasswordLabel;

    /**
     * Label for {@link SSHDialog#myRemotePath}.
     */
    private JLabel myRemotePathLabel;

    /**
     * Show a new SSH-dialog.
     */
    public SSHDialog() {
        super((JFrame) null, true);
        initGUI();
    }
    /**
     * Create the graphical input-components.
     */
    private void initGUI() {

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(getRemoteSystemPanel(), BorderLayout.CENTER);
        //TODO: SSHTunnelPanel
        //TODO: HttpTunnelPanel
        this.getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
        this.pack();
    }

    /**
     * @return The panel containing the data of the host to conect to.
     */
    private JPanel getRemoteSystemPanel() {
        if (myRemoteSystemPanel == null) {
            myRemoteSystemPanel = new JPanel();
            myRemoteSystemPanel.setLayout(new GridLayout(5, 2));
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

            myRemotePasswordLabel = new JLabel("Password:");
            myRemoteSystemPanel.add(myRemotePasswordLabel);
            myRemotePassword = new JPasswordField();
            myRemoteSystemPanel.add(myRemotePassword);

            myRemotePathLabel = new JLabel("Password:");
            myRemoteSystemPanel.add(myRemotePathLabel);
            myRemotePath = new JTextField();
            myRemoteSystemPanel.add(myRemotePath);
        }
        return myRemoteSystemPanel;
    }

    /**
     * @return The panel containing the buttons.
     */
    private JPanel getButtonsPanel() {
        if (myButtonsPanel == null) {
            myButtonsPanel = new JPanel();
            myConnectButton = new JButton("Connect");
            myButtonsPanel.add(myConnectButton);
            myConnectButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent aE) {
                    SSHDialog.this.setVisible(false);
                }
            });
        }
        return myButtonsPanel;
    }

    /**
     * @return the host to connect to.
     * @see #getRemotePort()
     */
    public String getRemoteHost() {
        return myRemoteHost.getText();
    }

    /**
     * @return the port to connect to.
     * @see #getRemoteHost()
     */
    public String getRemotePort() {
        return myRemotePort.getText();
    }

    /**
     * @return the user to connect with.
     * @see #getRemotePassword()
     */
    public String getRemoteUser() {
        return myRemoteUser.getText();
    }

    /**
     * @return the password to connect with.
     * @see #getRemoteUser()
     */
    public char[] getRemotePassword() {
        return myRemotePassword.getPassword();
    }

    /**
     * @return the path to load from/store to
     */
    public String getRemotePath() {
        return myRemotePath.getText();
    }
}
