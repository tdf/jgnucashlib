/**
 * MailImport.java
 * created: 28.06.2009 19:33:57
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jGnucashLib-private by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 * Otherwise, this code is made available under GPLv3 or later.
 *
 * ***********************************
 * Editing this file:
 *  -For consistent code-quality this file should be checked with the
 *   checkstyle-ruleset enclosed in this project.
 *  -After the design of this file has settled it should get it's own
 *   JUnit-Test that shall be executed regularly. It is best to write
 *   the test-case BEFORE writing this class and to run it on every build
 *   as a regression-test.
 */
package biz.wolschon.finance.jgnucash.wirecard;


//automatically created logger for debug and error -output
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.swing.JOptionPane;

import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.mailImport.MailImportHandler;
import biz.wolschon.finance.jgnucash.wirecard.importer.WirecardImporter;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib-private<br/>
 * MailImport.java<br/>
 * created: 28.06.2009 <br/>
 *<br/><br/>
 * <b>Import pdf-files with clearing-statements from Wirecard.</b>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class MailImport implements MailImportHandler {

    /**
     * Size of the buffer while reading the pdf-attachment.
     */
    private static final int BUFFERSIZE = 1024;
    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(MailImport.class
            .getName());


    /**
     * Just an overridden ToString to return this classe's name
     * and hashCode.
     * @return className and hashCode
     */
    @Override
    public String toString() {
        return "MailImport@" + hashCode();
    }


    /**
     * {@inheritDoc}.
     */
    public boolean handleMultiPartMail(final GnucashWritableFile aWritableModel,
                                       final String aSubject,
                                       final Message aMsg,
                                       final Multipart aMessage) throws MessagingException {
        LOG.fine("checking if this is a wirecard-mail...");
        Address[] from = aMsg.getFrom();
        if (from.length != 1 || from[0].toString().equals("treasury@wireard.com")) {
            LOG.fine("not a wirecard-mail, wrong sender");
            return false;
        }

        if (!aSubject.startsWith("Settlement Note for ")
            &&
            !aSubject.contains("Reserve Balance Note for")
            &&
            !aSubject.contains("Ihre Wirecard Technologies AG Rechnung:")) {
            LOG.fine("not a wirecard-mail, wrong subject");
            return false;
        }
        if (aMessage.getCount() > 1) {
            int count = aMessage.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = aMessage.getBodyPart(i);
                String fileName = bodyPart.getFileName();
                if (fileName != null && fileName.endsWith(".pdf")) {
                    File tempFile = null;
                    try {
                        tempFile = File.createTempFile(fileName, ".pdf");
                        FileOutputStream out = new FileOutputStream(tempFile);
                        InputStream in = (InputStream) bodyPart.getContent();
                        byte[] buffer = new byte[BUFFERSIZE];
                        int len = 0;
                        while ((len = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                        in.close();
                        out.close();
                        WirecardImporter.importFile(aWritableModel, tempFile);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.log(Level.SEVERE, "Error importing " + tempFile.getAbsolutePath(), e);
                        JOptionPane.showMessageDialog(null, "Error in " + tempFile.getName() + "\n"
                                + "[" + e.getClass().getName() + "]\n"
                                + e.getMessage());
                        return false;
                    }
                }
            }
        }
        return false;
    }


    /**
     * {@inheritDoc}.
     */
    public boolean handleTextMail(final GnucashWritableFile aWritableModel,
                                  final String aSubject,
                                  final Message aMsg,
                                  final String aMessage) {
        return false; // not handled
    }


}


