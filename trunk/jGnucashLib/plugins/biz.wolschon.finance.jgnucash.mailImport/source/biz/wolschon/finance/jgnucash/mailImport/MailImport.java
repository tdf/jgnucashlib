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
 *
 * Otherwise, this code is made available under GPLv3 or later.
 *
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
import javax.mail.Flags;
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
     * All folders of the mail-store.
     */
    private List<String> myFolders = null;

    /**
     * @return all folders, cached
     * @param aStore our mail-store
     * @throws MessagingException if we cannot fetch the folders
     */
    public List<String> getFolders(final Store aStore) throws MessagingException {
        if (myFolders == null) {
            LOG.info("listing folders...");
            JOptionPane.showMessageDialog(null, "There is no mail-folder configured or it does not exist.\n"
                    + "Listing folders after pressing OK...\n"
                    + "THIS MAY TAKE A WHILE!");
            myFolders = listFolders(aStore.getDefaultFolder());
        }

        return myFolders;
    }

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
                server = JOptionPane.showInputDialog("imap-server: (can be changed later in the root-account properties.)");
                aWritableModel.setUserDefinedAttribute("imap.server", server);
            }
            String user = aWritableModel.getUserDefinedAttribute("imap.user");
            if (user == null) {
                user = JOptionPane.showInputDialog("imap-user: (can be changed later in the root-account´s properties.)");
                aWritableModel.setUserDefinedAttribute("imap.user", user);
            }
            String passwd = JOptionPane.showInputDialog("imap-password:");
            store.connect(server, user, passwd);

            Folder folder = getInputFolder(aWritableModel, store);
            Folder doneFolder = getDoneFolder(aWritableModel, store);
            folder.open(Folder.READ_WRITE);
            doneFolder.open(Folder.READ_WRITE);
            LOG.info("mail folder opened");

            Message[] messages = folder.getMessages();
            for (Message message : messages) {
                try {
                    if (importMessage(message, aCurrentAccount, aWritableModel)) {
                        message.setFlag(Flag.SEEN, true);
                        folder.copyMessages(new Message[]{message}, doneFolder);
                        message.setFlag(Flags.Flag.DELETED, true);
                    }
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "Cannot import email" + message.getSubject(), e);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot scan mail-folder", e);
        }
        return null;
    }

    /**
     * @param aWritableModel
     * @param store
     * @return
     * @throws MessagingException
     * @throws JAXBException
     */
    private Folder getInputFolder(final GnucashWritableFile aWritableModel,
                                  Store store) throws MessagingException,
                                              JAXBException {
        String folderName = aWritableModel.getUserDefinedAttribute("imap.folder");
        Folder folder = null;
        if (folderName != null) {
            folder = store.getFolder(folderName);
        }
        while (folder == null || !folder.exists()) {
            final JDialog selectFolderDialog = new JDialog((JFrame) null, "Select mail folder");
            selectFolderDialog.getContentPane().setLayout(new BorderLayout());
            final JList folderListBox = new JList(new Vector<String>(getFolders(store)));
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
        return folder;
    }

    /**
     * @param aWritableModel stores our config
     * @param aStore our mail-store
     * @throws MessagingException issued with email
     * @throws JAXBException issued with the model
     */
    private Folder getDoneFolder(final GnucashWritableFile aWritableModel,
                               final Store aStore) throws MessagingException,
                                                 JAXBException {
        String doneFolderName = aWritableModel.getUserDefinedAttribute("imap.donefolder");
        Folder doneFolder = null;
        if (doneFolderName != null) {
            doneFolder = aStore.getFolder(doneFolderName);
        }
        while (doneFolder == null || !doneFolder.exists()) {
            final JDialog selectFolderDialog = new JDialog((JFrame) null, "Select mail folder to move processed mails to");
            selectFolderDialog.getContentPane().setLayout(new BorderLayout());
            final JList folderListBox = new JList(new Vector<String>(getFolders(aStore)));
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
            doneFolderName = folderListBox.getSelectedValue().toString();
            aWritableModel.setUserDefinedAttribute("imap.donefolder", doneFolderName);
            doneFolder = aStore.getFolder(doneFolderName);
        }
        return doneFolder;
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
     * @return true if the message has been handled successfully
     * @throws MessagingException in case of mail- issues
     * @throws IOException in case of io-issues
     * @throws JAXBException in case of XML-issues with the plugins
     */
    private boolean importMessage(final Message message,
                               final GnucashWritableAccount aCurrentAccount,
                               final GnucashWritableFile aWritableModel) throws MessagingException,
                                               IOException, JAXBException {
        LOG.fine("=========================================");
        if (message.isSet(Flag.DELETED)) {
            return false;
        }
        LOG.info("Message: " + message.getSubject());
        LOG.info("disposition: " + message.getDisposition());
        LOG.info("contentType: " + message.getContentType());
        if (message.isSet(Flag.SEEN)) {
            LOG.info("Flagged: SEEN");
        }
        if (message.isSet(Flag.DRAFT)) {
            LOG.info("Flagged: DRAFT");
        }
        if (message.isSet(Flag.ANSWERED)) {
            LOG.info("Flagged: ANSWERED");
        }
        if (message.isSet(Flag.USER)) {
            LOG.info("Flagged: USER");
        }
        if (message.isSet(Flag.FLAGGED)) {
            LOG.info("Flagged: FLAGGED");
        }
        if (message.isSet(Flag.RECENT)) {
            LOG.info("Flagged: RECENT");
        }
        if (message.isSet(Flag.DELETED)) {
            LOG.info("Flagged: DELETED");
        }
        Flag[] systemFlags = message.getFlags().getSystemFlags();
        for (Flag flag : systemFlags) {
            LOG.info("system-flag: " + flag.toString());
        }
        String[] userFlags = message.getFlags().getUserFlags();
        for (String flag : userFlags) {
            LOG.info("user-flag: " + flag);
        }
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
            LOG.fine("handling as text/* via our #" + mailHandlers.size() + " plugins...");
            for (MailImportHandler mailImportHandler : mailHandlers) {
                LOG.fine("handling as text/* via our #" + mailHandlers.size() + " plugins...trying " + mailImportHandler.getClass().getName());
                if (mailImportHandler.handleTextMail(aWritableModel, message.getSubject(), message, readTextContent(message))) {
                    return true;
                }
            }
        }
        if (content instanceof Multipart) {
            Multipart mp = (Multipart) content;
            // call plugins
            Collection<MailImportHandler> mailHandlers = PluginMain.getMailHandlers();
            LOG.fine("handling as multipart via our #" + mailHandlers.size() + " plugins...");
            for (MailImportHandler mailImportHandler : mailHandlers) {
                LOG.fine("handling as text/* via our #" + mailHandlers.size() + " plugins...trying " + mailImportHandler.getClass().getName());
                if (mailImportHandler.handleMultiPartMail(aWritableModel, message.getSubject(), message, mp)) {
                    return true;
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
        return false;
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
