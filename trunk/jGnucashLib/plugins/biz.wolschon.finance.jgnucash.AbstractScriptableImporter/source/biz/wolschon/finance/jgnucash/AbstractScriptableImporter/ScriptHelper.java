/**
 * ScriptHelper.java
 * created: 09.10.2008 05:41:32
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 *
 * This file is part of jGnucashLib-GPL by Marcus Wolschon <a
 * href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or a commercial license of this file
 * (unless modified by others) by contacting him directly.
 *
 * jGnucashLib-GPL is
 * free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * jGnucashLib-GPL is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with jGnucashLib-GPL. If not, see <http://www.gnu.org/licenses/>.
 * **************************************************************************
 * Editing this file:
 * -For consistent code-quality this file should be checked
 * with the checkstyle-ruleset enclosed in this project. -After the design of
 * this file has settled it should get it's own JUnit-Test that shall be
 * executed regularly. It is best to write the test-case BEFORE writing this
 * class and to run it on every build as a regression-test.
 * **************************************************************************
 * major Changes:
 * 09.10.2008 - initial version ...
 */
package biz.wolschon.finance.jgnucash.AbstractScriptableImporter;

// other imports
import java.util.logging.Logger;

import biz.wolschon.numbers.FixedPointNumber;

/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und
 * Beratung</a>.<br/>
 * Project: jGnucashLib-GPL<br/>
 * ScriptHelper.java<br/>
 * created: 09.10.2008 05:41:32 <br/>
 *<br/>
 * <br/>
 * <b>Helper-class given to scripts</b>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class ScriptHelper {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(ScriptHelper.class
            .getName());

    /**
     * Due to a classloader-problem scripts cannot create a
     * FixedPointNumber directly. This method is a crude way
     * to work around this.
     * @param value the value to assign
     * @return a new FixedPointNumber
     */
    public FixedPointNumber createNumber(final String value) {
        return new FixedPointNumber(value);
    }

    /**
     *
     * Due to a classloader-problem scripts cannot create a
     * FixedPointNumber directly. This method is a crude way
     * to work around this.
     * @param value the value to clone
     * @return a new FixedPointNumber
     */
    public FixedPointNumber createNumber(final FixedPointNumber value) {
        return new FixedPointNumber(value);
    }

    /**
     * Just an overridden ToString to return this classe's name and hashCode.
     * @return className and hashCode
     */
    @Override
    public String toString() {
        return "ScriptHelper@" + hashCode();
    }
}
