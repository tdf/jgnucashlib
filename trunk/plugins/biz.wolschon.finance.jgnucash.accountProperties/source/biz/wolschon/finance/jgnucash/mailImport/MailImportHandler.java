/**
 * MailImportHandler.javaTransactionMenuAction.java
 * created: 28.06.2009
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

package biz.wolschon.finance.jgnucash.mailImport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashWritableFile;

/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * MailImportHandler<br/>
 * created: 28.06.2009 <br/>
 *<br/><br/>
 * <b>Handler that can be regitred with {@link MailImport} to do an actual import.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public interface MailImportHandler {

    /**
     *
     * @param aSubject the subject of the message
     * @param aMessage the message-text
     * @param aMsg the full message
     * @return true if this message is handled and shall not be handled by anything else anymore
     */
    boolean handleTextMail(final GnucashWritableFile aWritableModel,
                           final String aSubject, final Message aMsg, final String aMessage) throws MessagingException, JAXBException;
    /**
     *
     * @param aSubject of the message
     * @param aMessage the extracted message-parts
     * @param aMsg the full message
     * @return true if this message is handled and shall not be handled by anything else anymore
     */
    boolean handleMultiPartMail(final GnucashWritableFile aWritableModel,
                                final String aSubject, final Message aMsg, final Multipart aMessage) throws MessagingException, JAXBException;
}
