/**
 * SshDataSource.java
 * created: 05.12.2008
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
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * SshDataSource<br/>
 * created: 05.12.2008 <br/>
 *<br/><br/>
 * <b>Data-source-plugin that allows reading from and writing to a gnucash-file
 * on a remote computer using sftp/scp.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class SshDataSource implements DataSourcePlugin {
    /**
     * The port SSH works on.
     */
    private static final int SSHPORT = 22;

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(SshDataSource.class
            .getName());

    /**
     * The last file we loaded.
     * exmple: "user@host:path/file"
     */
    private SSHDialog myLastLoadedFile = null;

    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin#loadFile()
     */
    @Override
    public GnucashWritableFile loadFile() throws IOException, JAXBException {

        SSHDialog dialog = new SSHDialog();
        dialog.setVisible(true);
        //String input = JOptionPane.showInputDialog("Please enter the file to load.", "user@host:path/file");
        File tempFile = File.createTempFile("jGnucaashEditor_SSH_", ".xml.gz");
        if (loadFileeViaSSH(dialog.getRemoteHostUserInfo(), dialog.getSSHTunnelUserInfo(), dialog.getRemotePath(), tempFile)) {
            myLastLoadedFile = dialog;
            return new GnucashFileWritingImpl(tempFile);
        }
        return null;
    }

    /**
     * @param aTempFile a gnucash-file stored on disk.
     * @param aPassword
     * @return true if it worked
     */
    private boolean loadFileeViaSSH(final ConnectInfo remoteSystem, final ConnectInfo sshTunnelSystem, final String rfile, final File aTempFile) {

        FileOutputStream fos = null;
        try {

            Session session = createConnection(remoteSystem, sshTunnelSystem);



            // exec 'scp -f rfile' remotely
            String command = "scp -f " + rfile;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream  in  = channel.getInputStream();

            channel.connect();

            byte[] buf = new byte[1024];

            // send '\0'
            buf[0] = 0; out.write(buf, 0, 1); out.flush();

            while (true) {
                int c = checkAck(in);
                if (c != 'C') {
                    break;
                }

                // read '0644 '
                in.read(buf, 0, 5);

                long filesize = 0;
                while (true) {
                    if (in.read(buf, 0, 1) < 0) {
                        // error
                        break;
                    }
                    if (buf[0] == ' ') {
                        break;
                    }
                    filesize = filesize * 10L + (buf[0] - '0');
                }

                String file = null;
                for (int i = 0;; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        file = new String(buf, 0, i);
                        break;
                    }
                }

                //System.out.println("filesize="+filesize+", file="+file);

                // send '\0'
                buf[0] = 0; out.write(buf, 0, 1); out.flush();

                // read a content of lfile
                fos = new FileOutputStream(aTempFile);
                int foo;
                while (true) {
                    if (buf.length<filesize) {
                        foo = buf.length;
                    } else {
                        foo = (int) filesize;
                    }
                    foo = in.read(buf, 0, foo);
                    if (foo < 0) {
                        // error
                        break;
                    }
                    fos.write(buf, 0, foo);
                    filesize -= foo;
                    if (filesize == 0L) {
                        break;
                    }
                }
                fos.close();
                fos = null;

                if (checkAck(in) != 0) {
                    System.exit(0);
                }

                // send '\0'
                buf[0] = 0; out.write(buf, 0, 1); out.flush();
            }

            session.disconnect();

            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot load file via SSH", e);
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ee) {

            }
            return false;
        }

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
        public void showMessage(String message){
            JOptionPane.showMessageDialog(null, message);
        }
        final GridBagConstraints gbc =
            new GridBagConstraints(0,0,1,1,1,1,
                    GridBagConstraints.NORTHWEST,
                    GridBagConstraints.NONE,
                    new Insets(0,0,0,0),0,0);
        private Container panel;
        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo){
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            gbc.weightx = 1.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridx = 0;
            panel.add(new JLabel(instruction), gbc);
            gbc.gridy++;

            gbc.gridwidth = GridBagConstraints.RELATIVE;

            JTextField[] texts = new JTextField[prompt.length];
            for (int i = 0; i < prompt.length; i++){
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = 0;
                gbc.weightx = 1;
                panel.add(new JLabel(prompt[i]), gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weighty = 1;
                if (echo[i]) {
                    texts[i] = new JTextField(20);
                } else {
                    texts[i] = new JPasswordField(20);
                }
                panel.add(texts[i], gbc);
                gbc.gridy++;
            }

            if (JOptionPane.showConfirmDialog(null, panel,
                    destination + ": " + name,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE)
                    == JOptionPane.OK_OPTION) {
                String[] response = new String[prompt.length];
                for (int i = 0; i < prompt.length; i++) {
                    response[i] = texts[i].getText();
                }
                return response;
            } else {
                return null;  // cancel
            }
        }

        public String getPassword() {
            return myPassword;
        }

        /**
         * @param aPassword the password to set
         */
        private void setPassword(final String aPassword) {
            myPassword = aPassword;
        }
        /**
         * @param aPassword the password to set
         */
        private void setPassword(final char[] aPassword) {
            myPassword = new String(aPassword);
        }
    }


    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin#write(biz.wolschon.fileformats.gnucash.GnucashWritableFile)
     */
    @Override
    public void write(final GnucashWritableFile aFile) throws IOException,
    JAXBException {
        if (myLastLoadedFile == null) {
            return;
        }
        SSHDialog dialog = myLastLoadedFile;
        saveFileViaSSH(dialog.getRemoteHostUserInfo(), dialog.getSSHTunnelUserInfo(), dialog.getRemotePath(), aFile);

    }

    /**
     * @param aFile
     *
     */
    private void saveFileViaSSH(final ConnectInfo remoteSystem, final ConnectInfo sshTunnelSystem, final String rfile, final GnucashWritableFile aFile) {
        FileInputStream fis = null;
        try {
            File tempFile = File.createTempFile("jGnucasEditor_sshto_", ".xml.gz");
            aFile.writeFile(tempFile);


            Session session = createConnection(remoteSystem, sshTunnelSystem);


            // exec 'scp -t rfile' remotely
            String command  = "scp -p -t " + rfile;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in   = channel.getInputStream();

            channel.connect();

            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = (tempFile).length();
            command = "C0644 " + filesize + " ";
            String tempFilePath = tempFile.getAbsolutePath();
            if (tempFilePath.lastIndexOf('/')>0) {
                command += tempFilePath.substring(tempFilePath.lastIndexOf('/')+1);
            } else {
                command += tempFilePath;
            }
            command += "\n";
            out.write(command.getBytes()); out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send a content of lfile
            fis = new FileInputStream(tempFile);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0) {
                    break;
                }
                out.write(buf, 0, len); //out.flush();
            }
            fis.close();
            fis = null;
            // send '\0'
            buf[0] = 0; out.write(buf, 0, 1); out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
            out.close();

            channel.disconnect();
            session.disconnect();

            System.exit(0);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot store file via SSH", e);
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception ee) {
                LOG.log(Level.SEVERE, "Cannot close SSH-connection", ee);
            }
        }
    }

    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin#writeTo(biz.wolschon.fileformats.gnucash.GnucashWritableFile)
     */
    @Override
    public void writeTo(final GnucashWritableFile aFile) throws IOException, JAXBException {
        SSHDialog dialog = new SSHDialog();
        dialog.setVisible(true);

        myLastLoadedFile = dialog;
        write(aFile);
    }

}
