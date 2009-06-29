/**
 * DummyGnucashFile.java
 * created: 30.09.2008 18:55:23
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jGnucashLib-GPL by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 *  jGnucashLib-GPL is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jGnucashLib-GPL is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with jGnucashLib-GPL.  If not, see <http://www.gnu.org/licenses/>.
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
package biz.wolschon.finance.jgnucash.AbstractScriptablePlugin.ScriptEditor;


//automatically created logger for debug and error -output
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.fileformats.gnucash.jwsdpimpl.GnucashFileWritingImpl;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib-GPL<br/>
 * DummyGnucashFile.java<br/>
 * created: 30.09.2008 18:55:23 <br/>
 *<br/><br/>
 * <b>Dummy gnucash-file that creates any account that is looked up.</b>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class DummyGnucashFile extends GnucashFileWritingImpl implements GnucashWritableFile {

	/**
	 * Automatically created logger for debug and error-output.
	 */
	private static final Logger LOG = Logger.getLogger(DummyGnucashFile.class
			.getName());

	/**
	 * ${@inheritDoc}.
	 * @throws JAXBException
	 */
	@Override
	public GnucashWritableAccount getAccountByName(final String aName) {
		GnucashWritableAccount account = super.getAccountByName(aName);
		if (account == null) {
			try {
				account = createWritableAccount();
				account.setName(aName);
			} catch (JAXBException e) {
				LOG.log(Level.SEVERE,"[JAXBException] Problem in "
				           + getClass().getName(),
				             e);
			}
		}
		return account;
	}


	/**
	 * ${@inheritDoc}.
	 * @throws JAXBException
	 */
	@Override
	public GnucashWritableAccount getAccountByID(final String aID) {
		GnucashWritableAccount account = super.getAccountByID(aID);
		if (account == null) {
			try {
				account = createWritableAccount(aID);
				account.setName("Dummy-Account " + aID);
			} catch (JAXBException e) {
				LOG.log(Level.SEVERE,"[JAXBException] Problem in "
				           + getClass().getName(),
				             e);
			}
		}
		return account;
	}

	/**
	 * ${@inheritDoc}.
	 * @throws JAXBException
	 */
	@Override
	public GnucashWritableAccount getAccountByIDorName(final String aID, final String aName) {
		GnucashWritableAccount account = (GnucashWritableAccount) super.getAccountByIDorName(aID, aName);
		if (account == null) {
			try {
				account = createWritableAccount(aID);
				account.setName(aName);
			} catch (JAXBException e) {
				LOG.log(Level.SEVERE,"[JAXBException] Problem in "
				           + getClass().getName(),
				             e);
			}
		}
		return account;
	}



	/**
	 * @throws IOException on problems
	 * @throws JAXBException on problems parsing our dummy-file
	 */
	public DummyGnucashFile(final ClassLoader classLoader) throws IOException, JAXBException  {
		super(createDummyFile(classLoader));
	}


	/**
	 * Create an empty gnucash-file as a temp-file
	 * that will be deleted at JVM-exit.
	 * @return
	 * @throws IOException on problems
	 */
	private static File createDummyFile(final ClassLoader classLoader) throws IOException {
		File file = File.createTempFile("dummyGnucashFile", ".xml");
		file.deleteOnExit();
		InputStream in = classLoader.getResourceAsStream("biz/wolschon/finance/jgnucash/AbstractScriptablePlugin/ScriptEditor/dummy.xml");
		FileOutputStream out = new FileOutputStream(file);
		byte[] buffer = new byte[255];
		int length = -1;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		out.close();
		in.close();
		return file;
	}
	/**
	 * Just an overridden ToString to return this classe's name
	 * and hashCode.
	 * @return className and hashCode
	 */
	@Override
    public String toString() {
		return "DummyGnucashFile@" + hashCode();
	}
}


