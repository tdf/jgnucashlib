/**
 * PluginMain.java
 * created: 29.11.2008
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jGnucashLib-private by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 * Otherwise, this code is made available under GPLv3 or later.
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
package biz.wolschon.finance.jgnucash.splitter;

//automatically created logger for debug and error -output
import java.util.logging.Logger;



/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib-private<br/>
 * PluginMain.java<br/>
 * created: 29.11.2008 <br/>
 *<br/><br/>
 * <b>Entry-point for the plugin!</b>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class PluginMain extends org.java.plugin.Plugin {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(PluginMain.class
            .getName());


    /**
     * Just an overridden ToString to return this classe's name
     * and hashCode.
     * @return className and hashCode
     */
    @Override
    public String toString() {
        return "PluginMain@" + hashCode();
    }

    /**
     * ignored.
     * ${@inheritDoc}.
     */
    @Override
    protected void doStart() throws Exception {
        // ignored
    }

    /**
     * ignored.
     * ${@inheritDoc}.
     */
    @Override
    protected void doStop() throws Exception {
        // ignored
    }
}


