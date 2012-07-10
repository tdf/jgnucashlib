/**
 * PluginMain.java
 * created: 26.06.2009
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
package biz.wolschon.finance.jgnucash.mailImport;
//automatically created logger for debug and error -output
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.Extension.Parameter;



/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jGnucashLib-GPL<br/>
 * PluginMain.java<br/>
 * created: 26.06.2009 <br/>
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
     * Singleton-instance.
     */
    private static PluginMain myInstance;

    /**
     * Plugins that have registred with us.
     */
    private static Collection<MailImportHandler> myHandlers  = null;

    /**
     * @return the singleton -instance
     */
    protected static PluginMain getInstance() {
        return myInstance;
    }

    /**
     * @return Al registred mail-handlers
     */
    @SuppressWarnings("unchecked")
    public static Collection<MailImportHandler> getMailHandlers() {
        if (myHandlers != null) {
            return myHandlers;
        }
        LOG.info("loading MailImportHandlers...");
        Collection<MailImportHandler> retval = new LinkedList<MailImportHandler>();
        PluginManager manager = getInstance().getManager();
        ExtensionPoint extensionPoint = manager.getRegistry().getExtensionPoint(getInstance().getDescriptor().getId(), "mailHandler");
        LOG.info("loading MailImportHandlers... extensionPoint=("
                + getInstance().getDescriptor().getId() + "/mailHandler" + ") ="
                + extensionPoint);

        Collection<Extension> availableExtensions = extensionPoint.getConnectedExtensions();
        LOG.info("loading MailImportHandlers... #availableExtensions=" + availableExtensions.size());

        for (Extension extension : availableExtensions) {
            Parameter className = extension.getParameter("class");
            try {
                manager.activatePlugin(extension.getDeclaringPluginDescriptor().getId());
                // Get plug-in class loader.
                ClassLoader classLoader = manager.getPluginClassLoader(
                                          extension.getDeclaringPluginDescriptor());
                // Load Tool class.
                Class toolCls = classLoader.loadClass(className.valueAsString());
                // Create Tool instance.
                Object o = toolCls.newInstance();
                if (!(o instanceof MailImportHandler)) {
                    LOG.log(Level.SEVERE, "Extension '" + extension.getId() + "' does not implement MailImportHandler-interface.");
                    JOptionPane.showMessageDialog(null, "Error",
                            "The Extension '" + extension.getId() + "'"
                            + " does not implement MailImportHandler-interface.",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    MailImportHandler handler = (MailImportHandler) o;
                    retval.add(handler);
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Problem with extension '" + extension.getId() + "'.", e);
            }
        }
        myHandlers = retval;
        return retval;
    }
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
        myInstance = this;
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


