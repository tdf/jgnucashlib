/**
 *
 */
package biz.wolschon.finance.jgnucash.AdvanziaEmailImporter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashWritableFile;

/**
 * TODO: not done yet!!!
 * (c) 2010 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * License: GPLv3 or later<br/>
 * Main<br/>
 * created: 09.08.2010 <br/>
 *<br/><br/>
 * <b>TODO: describe the purpose of this class.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class Main extends org.java.plugin.Plugin implements biz.wolschon.finance.jgnucash.mailImport.MailImportHandler {
    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(AdvanziaImporter.class.getName());

	@Override
	protected void doStart() throws Exception {
		// ignored
	}

	@Override
	protected void doStop() throws Exception {
		// ignored
	}

    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.mailImport.MailImportHandler#handleMultiPartMail(biz.wolschon.fileformats.gnucash.GnucashWritableFile, java.lang.String, javax.mail.Message, javax.mail.Multipart)
     */
    @Override
    public boolean handleMultiPartMail(final GnucashWritableFile aWritableModel,
                                       final String aSubject,
                                       final Message aMsg,
                                       final Multipart aMessage)
                                                          throws MessagingException,
                                                          JAXBException {
        return false;
    }

    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.mailImport.MailImportHandler#handleTextMail(biz.wolschon.fileformats.gnucash.GnucashWritableFile, java.lang.String, javax.mail.Message, java.lang.String)
     */
    @Override
    public boolean handleTextMail(final GnucashWritableFile aWritableModel,
                                  final String aSubject,
                                  final Message aMsg,
                                  final String aMessage) throws MessagingException,
                                                  JAXBException {
        if (!aSubject.startsWith("Gebührenfrei Mastercard Gold - Rechnung")) {
            return false;
        }
        try {
            AdvanziaImporter advanziaImporter = new AdvanziaImporter(aMessage);
            return advanziaImporter.runImport(aWritableModel, null) == null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error running import for Advanzia-email", e);
            return false;
        }
    }

}
