/**
 * AdvanziaImporter.javaTransactionMenuAction.java
 * created: 09.08.2010
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

package biz.wolschon.finance.jgnucash.AdvanziaEmailImporter;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.AbstractScriptableImporter.AbstractScriptableImporter;
import biz.wolschon.numbers.FixedPointNumber;

/**
 *
 * TODO: not done yet!!!
 *
 * (c) 2010 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * AdvanziaImporter<br/>
 * created: 09.08.2010 <br/>
 *<br/><br/>
 * <b>TODO: describe the purpose of this class.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class AdvanziaImporter extends AbstractScriptableImporter {
    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(AdvanziaImporter.class.getName());

    private class Line {
        /**
         * @param aDate
         * @param aDescription
         * @param aValue
         */
        public Line(Date aDate, String aDescription, FixedPointNumber aValue) {
            super();
            date = aDate;
            description = aDescription;
            value = aValue;
        }
        private final Date date;
        /**
         * @return the date
         */
        public Date getDate() {
            return date;
        }
        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }
        /**
         * @return the value
         */
        public FixedPointNumber getValue() {
            return value;
        }
        private final String description;
        private final FixedPointNumber value;

    }
    private final List<Line> lines = new LinkedList<Line>();

    /**
     * @param aMessage
     * @throws ParseException
     */
    public AdvanziaImporter(final String aMessage) throws ParseException {
        // TODO Auto-generated constructor stub
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Pattern linePattern = Pattern.compile(

                  "<TR>"
                + "[\\s]*<TD [^>]*><font [^>]*>([0-9][0-9].[0-9][0-9].[0-9][0-9][0-9][0-9])</font></TD>"
                + "[\\s]*<TD [^>]*><font [^>]*> </font></TD>"
                + "[\\s]*<TD [^>]*><font [^>]*>([^<]*)</font></TD>"
                + "[\\s]*<TD [^>]*><font [^>]*>([0-9]*,[0-9][0-9])</font></TD>"
                + "[\\s]*</TR>");
        Matcher matcher = linePattern.matcher(aMessage.replace('\n', ' '));
        while (matcher.find()) {
            String date = matcher.group(1);
            String description = matcher.group(2);
            String value = matcher.group(3);
            LOG.fine("Advanzia date="+date +" description="+description+ " value=" + value);
            Date d = sdf.parse(date);
            FixedPointNumber v = new FixedPointNumber(value).negate();
            lines.add(new Line(d, description, v));

        }
        LOG.fine("Advanzia matching done");
        //TODO: ask for the account if not given, so we don't import everything to "Ausgleichskonto-EUR"

        File configfile = getConfigFile();
        Properties settings = new Properties();
        if (configfile.exists()) {
            try {
                settings.load(new FileReader(configfile));
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "cannot log config-file", e);
            }
        }
        setMyProperties(settings);
    }

    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.AbstractScriptableImporter.AbstractScriptableImporter#getPluginName()
     */
    @Override
    public String getPluginName() {
        return "Advanzia";
    }

    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.plugin.ImporterPlugin#runImport(biz.wolschon.fileformats.gnucash.GnucashWritableFile, biz.wolschon.fileformats.gnucash.GnucashWritableAccount)
     */
    @Override
    public String runImport(GnucashWritableFile aWritableModel,
                            GnucashWritableAccount aCurrentAccount)
                                                                   throws Exception {

        setMyAccount(aWritableModel.getAccountByID("9771d8a5b5c56b556db4b7e036cea44b"));//TODO
        for (Line line : lines) {
            importTransaction(line.getDate(), line.getValue(), line.getDescription());
        }
        return null;
    }

}
