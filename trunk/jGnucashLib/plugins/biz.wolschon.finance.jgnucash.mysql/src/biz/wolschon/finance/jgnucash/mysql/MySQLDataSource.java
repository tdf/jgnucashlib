/**
 * MySQLDataSource.java
 * created: 03.08.2009
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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashFileWritingImpl;
import biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * MySQLDataSource<br/>
 * created: 03.08.2009 <br/>
 *<br/><br/>
 * <b>Data-source-plugin that allows reading from and writing to a gnucash-2.3-database
 * stored on a remote computer using mysql instead of an XML-file.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class MySQLDataSource implements DataSourcePlugin {
    /**
     * The port MySQL works on.
     */
    private static final int MYSQLPORT = 0;

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(MySQLDataSource.class
            .getName());


    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin#loadFile()
     */
    @Override
    public GnucashWritableFile loadFile() throws IOException, JAXBException {

//        SSHDialog dialog = new SSHDialog();
//        dialog.setVisible(true);
//        //String input = JOptionPane.showInputDialog("Please enter the file to load.", "user@host:path/file");
//        File tempFile = File.createTempFile("jGnucaashEditor_SSH_", ".xml.gz");
//        if (loadFileeViaSSH(dialog.getRemoteHostUserInfo(), dialog.getSSHTunnelUserInfo(), dialog.getRemotePath(), tempFile)) {
//            myLastLoadedFile = dialog;
//            return new GnucashFileWritingImpl(tempFile);
//        }
        return null;
    }

    /**
     * @param remoteSystem
     * @param sshTunnelSystem
     * @return
     * @throws JSchException
     */
    private Session createConnection(final ConnectInfo remoteSystem,
                                     final ConnectInfo sshTunnelSystem)
                                                                       throws JSchException {
        JSch jsch = new JSch();
        Session session = null;
        if (sshTunnelSystem != null) {
            Session tunnelSession = jsch.getSession(sshTunnelSystem.getUserName(), sshTunnelSystem.getHostName(), sshTunnelSystem.getHostPost());
            SSHUserInfo ui = new SSHUserInfo();
            ui.setPassword(sshTunnelSystem.getPassword());
            tunnelSession.setUserInfo(ui);
            tunnelSession.connect();
            int assingedPort = tunnelSession.setPortForwardingL(8764, remoteSystem.getHostName(), remoteSystem.getHostPost());
            // connect through tunnel
            // TODO: try ::1 for IPv6 if there is no 127.0.0.1
            session = jsch.getSession(remoteSystem.getUserName(), "127.0.0.1", assingedPort);
        } else {
            // connect directly
            session = jsch.getSession(remoteSystem.getUserName(), remoteSystem.getHostName(), remoteSystem.getHostPost());
        }
        // username and password will be given via UserInfo interface.
        SSHUserInfo ui = new SSHUserInfo();
        ui.setPassword(remoteSystem.getPassword());
        session.setUserInfo(ui);
        session.connect();

        return session;
    }


    static int checkAck(final InputStream in) throws IOException{
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) {
            return b;
        }
        if (b == -1) {
            return b;
        }

        if (b==1 || b==2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char)c);
            }
            while (c != '\n');
            if  (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

    public static class SSHUserInfo implements UserInfo, UIKeyboardInteractive{
        public boolean promptYesNo(String str){
            Object[] options={ "yes", "no" };
            int foo=JOptionPane.showOptionDialog(null,
                    str,
                    "Warning",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return foo==0;
        }

        private String myPassword;
        JTextField passwordField=new JPasswordField(20);

        public String getPassphrase(){ return null; }
        public boolean promptPassphrase(String message){ return true; }
        public boolean promptPassword(String message){
            Object[] ob={passwordField};
            int result=
                JOptionPane.showConfirmDialog(null, ob, message,
                        JOptionPane.OK_CANCEL_OPTION);
            if(result==JOptionPane.OK_OPTION){
                myPassword=passwordField.getText();
                return true;
            } else {
                return false;
            }
        }

    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin#write(biz.wolschon.fileformats.gnucash.GnucashWritableFile)
     */
    @Override
    public void write(final GnucashWritableFile aFile) throws IOException,
//    JAXBException {
//        if (myLastLoadedFile == null) {
//            return;
//        }
//        SSHDialog dialog = myLastLoadedFile;
//        saveFileViaSSH(dialog.getRemoteHostUserInfo(), dialog.getSSHTunnelUserInfo(), dialog.getRemotePath(), aFile);

    }
    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin#writeTo(biz.wolschon.fileformats.gnucash.GnucashWritableFile)
     */
    @Override
    public void writeTo(final GnucashWritableFile aFile) throws IOException, JAXBException {
//        SSHDialog dialog = new SSHDialog();
//        dialog.setVisible(true);
//
//        myLastLoadedFile = dialog;
//        write(aFile);
    }

}
