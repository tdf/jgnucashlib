/**
 * SplitRowMapper.javaTransactionMenuAction.java
 * created: 11.08.2009
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

package biz.wolschon.finance.jgnucash.mysql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;



/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * TransactionRowMapper<br/>
 * created: 11.08.2009 <br/>
 *<br/><br/>
 * <b>Map the result of a database-query to {@link GnucashDBTransaction}-instances.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class TransactionRowMapper implements ParameterizedRowMapper<GnucashDBTransaction> {


    /**
     * The {@link GnucashDatabase} we belong to.
     */
    private final GnucashDatabase myGnucashFile;

    /**
     * @param aGnucashFile The {@link GnucashDatabase} we belong to.
     */
    protected TransactionRowMapper(final GnucashDatabase aGnucashFile) {
        super();
        myGnucashFile = aGnucashFile;
    }

    /**
     * Example: "20090730220000".
     */
    private static final DateFormat MYDATEFORMAT = new SimpleDateFormat("yyyyMMdddHHmmss");

    /**
     * @param aResultSet the result-set who´s current result to map
     * @param aRowNumber the current row-number in the result-set
     * @return the result mapped to a bean
     * @throws SQLException on problems with the database
     * @see org.springframework.jdbc.core.simple.ParameterizedRowMapper#mapRow(java.sql.ResultSet, int)
     */
    @Override
    public GnucashDBTransaction mapRow(final ResultSet aResultSet, final int aRowNumber)
                                                              throws SQLException {
        GnucashDBTransaction retval;
        try {
            retval = new GnucashDBTransaction(myGnucashFile,
                    aResultSet.getString("guid"),
                    aResultSet.getString("currency_guid"),
                    aResultSet.getString("num"),
                    aResultSet.getString("description"),
                    MYDATEFORMAT.parse(aResultSet.getString("post_date")),
                    MYDATEFORMAT.parse(aResultSet.getString("enter_date")));
        } catch (ParseException e) {
            SQLException ex = new SQLException("invalid date");
            ex.initCause(e);
            throw ex;
        }
        return retval;
    }
}
