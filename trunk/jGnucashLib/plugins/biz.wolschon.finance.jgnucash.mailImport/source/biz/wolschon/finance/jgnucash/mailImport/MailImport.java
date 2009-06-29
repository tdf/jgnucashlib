/**
 * PaypalImporter.java
 * Created on 26.06.2009
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 * Permission is granted to use, modify,
 * publish and sub-license this code as specified in the contract. If nothing
 * else is specified these rights are given non-exclusively with no restrictions
 * solely to the contractor(s). If no specified otherwise I reserve the right to
 * use, modify, publish and sub-license this code to other parties myself.
 * -----------------------------------------------------------
 * major Changes:
 * 06.11.2008 - initial version created using the HBCI-plugin as a template...
 */
package biz.wolschon.finance.jgnucash.mailImport;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataSource;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeMultipart;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.plugin.ImporterPlugin;

/**
 * Use the JavaMail-API to import transactions from emails
 * using other plugins to handle the emails.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class MailImport implements ImporterPlugin {
    /**
     * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
     * Project: jgnucashLib-GPL<br/>
     * InputStreamDataSource<br/>
     * created: 29.06.2009 <br/>
     *<br/><br/>
     * <b>Read a Mime Multipart-message from a stream.</b>
     * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
     */
    private final class InputStreamDataSource implements DataSource {
        /**
         * the message to decode.
         */
        private final Message myMessage;

        /**
         * @param aMessage the message to decode
         */
        private InputStreamDataSource(final Message aMessage) {
            myMessage = aMessage;
        }

        /**
         * {@inheritDoc}.
         */
        public String getContentType() {
            try {
                return myMessage.getContentType();
            } catch (MessagingException e) {
                LOG.log(Level.SEVERE, "Exception while getting content-type", e);
                return "text/plain";
            }
        }

        /**
         * {@inheritDoc}.
         */
        public InputStream getInputStream() throws IOException {
            try {
                return (InputStream) myMessage.getContent();
            } catch (MessagingException e) {
                LOG.log(Level.SEVERE, "Exception while getting content", e);
                return null;
            }
        }

        /**
         * {@inheritDoc}.
         */
        public String getName() {
            return "";
        }

        /**
         * {@inheritDoc}.
         */
        public OutputStream getOutputStream() throws IOException {
            return null;
        }
    }

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(MailImport.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public String runImport(final GnucashWritableFile aWritableModel,
                            final GnucashWritableAccount aCurrentAccount)
                                                                   throws Exception {
        try {
            Properties props = System.getProperties();
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imap");
            String server = aWritableModel.getUserDefinedAttribute("imap.server");
            if (server == null) {
                server = JOptionPane.showInputDialog("imap-server: (can be changed later in the root-account´s properties.)");
                aWritableModel.setUserDefinedAttribute("imap.server", server);
            }
            String user = aWritableModel.getUserDefinedAttribute("imap.user");
            if (user == null) {
                user = JOptionPane.showInputDialog("imap-user: (can be changed later in the root-account´s properties.)");
                aWritableModel.setUserDefinedAttribute("imap.user", user);
            }
            String passwd = JOptionPane.showInputDialog("imap-password:");
            store.connect(server, user, passwd);

            String folderName = aWritableModel.getUserDefinedAttribute("imap.folder");
            Folder folder = null;
            if (folderName != null) {
                folder = store.getFolder(folderName);
            }
//            listFolders(store.getDefaultFolder());

            List<String> folders = null;
            while (folder == null || !folder.exists()) {
                if (folders == null) {
                    LOG.info("listing folders...");
                    JOptionPane.showMessageDialog(null, "There is no mail-folder configured it it does not exist.\n"
                            + "Listing folders after pressing OK...\n"
                            + "THIS MAY TAKE A WHILE!");
                    folders = listFolders(store.getDefaultFolder());
                }
                final JDialog selectFolderDialog = new JDialog((JFrame) null, "Select mail folder");
                selectFolderDialog.getContentPane().setLayout(new BorderLayout());
                final JList folderListBox = new JList(new Vector<String>(folders));
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent aE) {
                        if (folderListBox.getSelectedIndices() != null) {
                            if (folderListBox.getSelectedIndices().length == 1) {
                                selectFolderDialog.setVisible(false);
                            }
                        }
                    }

                });
                selectFolderDialog.getContentPane().add(new JScrollPane(folderListBox), BorderLayout.CENTER);
                selectFolderDialog.getContentPane().add(okButton, BorderLayout.SOUTH);
                selectFolderDialog.setModal(true);
                selectFolderDialog.pack();
                selectFolderDialog.setVisible(true);
                folderName = folderListBox.getSelectedValue().toString();
                aWritableModel.setUserDefinedAttribute("imap.folder", folderName);
                folder = store.getFolder(folderName);
            }
            folder.open(Folder.READ_WRITE);
            //Folder outfolder = store.getFolder("Inbox/0-Wiederansicht/abzurechnen/erledigt");
            LOG.info("mail folder opened");

            Message[] messages = folder.getMessages();
            for (Message message : messages) {
                importMessage(message, aCurrentAccount, aWritableModel);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot scan mail-folder", e);
        }
        return null;
    }

    /**
     * Log the name of a folder and it´s subfolders.
     * @param aFolder the folder to show
     * @throws MessagingException if we cannot list the subfolders
     * @return a newline separated list of all folders
     */
    private List<String> listFolders(final Folder aFolder) throws MessagingException {
        List<String> retval = new LinkedList<String>();
        retval.add(aFolder.getFullName());
        LOG.info("folder: " + aFolder.getFullName());
        Folder[] subfolders = aFolder.list();
        for (Folder folder : subfolders) {
            retval.addAll(listFolders(folder));
        }
        return retval;
    }

    /**
     * Check if the given message is claimed by any of the registred importers
     * and handle it.
     * @param message the message to import or to ignore
     * @param aCurrentAccount the currently selected account in the JGnucashEditor
     * @param aWritableModel the gnucash-file
     * @throws MessagingException in case of mail- issues
     * @throws IOException in case of io-issues
     * @throws JAXBException in case of XML-issues with the plugins
     */
    private void importMessage(final Message message,
                               final GnucashWritableAccount aCurrentAccount,
                               final GnucashWritableFile aWritableModel) throws MessagingException,
                                               IOException, JAXBException {
        if (message.isSet(Flag.DELETED)) {
            return;
        }
        LOG.info("Message: " + message.getSubject());
        LOG.info("disposition: " + message.getDisposition());
        LOG.info("contentType: " + message.getContentType());
        Object readTextContent = message.getContent();
        Object content = readTextContent;
        LOG.info("content: " + content.getClass().getName());
//        if (message instanceof MimeMessage) {
//            MimeMessage msg = (MimeMessage) message;
//            msg.getco
//        }
        if (content instanceof InputStream && message.getContentType().toLowerCase().contains("multipart")) {
            content = new MimeMultipart(new InputStreamDataSource(message));
        }
        if (content instanceof InputStream && message.getContentType().toLowerCase().startsWith("text")) {
          // call plugins for text/plain -messages too
            Collection<MailImportHandler> mailHandlers = PluginMain.getMailHandlers();
            for (MailImportHandler mailImportHandler : mailHandlers) {
                if (mailImportHandler.handleTextMail(aWritableModel, message.getSubject(), message, readTextContent(message))) {
                    message.setFlag(Flag.SEEN, true);
                  //folder.copyMessages(new Message[]{message}, outfolder);
                    //message.setFlag(Flags.Flag.DELETED, true);
                    return;
                }
            }
        }
        if (content instanceof Multipart) {
            Multipart mp = (Multipart) content;
            // call plugins
            Collection<MailImportHandler> mailHandlers = PluginMain.getMailHandlers();
            for (MailImportHandler mailImportHandler : mailHandlers) {
                if (mailImportHandler.handleMultiPartMail(aWritableModel, message.getSubject(), message, mp)) {
                    message.setFlag(Flag.SEEN, true);
                  //folder.copyMessages(new Message[]{message}, outfolder);
                    //message.setFlag(Flags.Flag.DELETED, true);
                    return;
                }
            }
//            for (int i = 0; i < mp.getCount(); i++) {
//                Part part = mp.getBodyPart(i);
//                String disposition = part.getDisposition();
//                if (disposition == null) {
//                    continue;
//                }
//                if (disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE)) {
//                    LOG.info("\tAttachment: " + part.getFileName() + " of type " + part.getContentType());
//                    //part.getInputStream()
//                }
//            }
        }
    }

    /**
     * @param aMessage the message to read
     * @return the content if it is of content-type "text/*"
     * @throws IOException may happen in reading
     * @throws MessagingException if we cannot fetch the content of the messsag
     */
    private String readTextContent(final Message aMessage) throws IOException, MessagingException {
        Object content = aMessage.getContent();
        if (content instanceof InputStream) {
            //TODO: charset
            InputStreamReader reader = new InputStreamReader((InputStream) content);
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[Byte.MAX_VALUE];
            int reat = 0;
            while ((reat = reader.read(buffer)) > 0) {
                sb.append(buffer, 0, reat);
            }
            return sb.toString();
        }
        return content.toString();
    }


}
