/**
 * MySQLDataSource.java
 * created: 03.08.2009
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

package biz.wolschon.finance.jgnucash.mysql;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import org.apache.axis.utils.ClassUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import biz.wolschon.fileformats.gnucash.GnucashWritableFile;
import biz.wolschon.finance.jgnucash.mysql.impl.GnucashDatabase;
import biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin;


/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * MySQLDataSource<br/>
 * created: 03.08.2009 <br/>
 *<br/><br/>
 * <b>Data-source-plugin that allows reading from and writing to a gnucash-2.3-database
 * stored on a remote computer using mysql instead of an XML-file.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class MySQLDataSource implements DataSourcePlugin {
    /**
     * The port MySQL works on.
     */
    private static final int MYSQLPORT = 0;

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(MySQLDataSource.class
            .getName());


    /**
     * {@inheritDoc}
     * @see biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin#loadFile()
     */
    @Override
    public GnucashWritableFile loadFile() throws IOException, JAXBException {
        JOptionPane.showMessageDialog(null, "WARNING! MySQL-support is still incomplete");
        try {
            com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
            //Class.forName("com.mysql.jdbc.Driver").newInstance();

//            SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver, "jdbc:mysql://localhost/gnucash", "root", "");
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            ClassUtils.setDefaultClassLoader(getClassLoader());
            try {
                dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                e.printStackTrace(); //ignored
            }
            dataSource.setUrl("jdbc:mysql://localhost/gnucash");
            dataSource.setUsername("root");
            dataSource.setPassword("");

            return new GnucashDatabase(dataSource);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot open database-connection", e);
        }
        return null;
    }

    /**
     * @return the class-loader that can load the database-driver.
     * @throws ClassNotFoundException no driver
     */
    private ClassLoader getClassLoader() throws ClassNotFoundException{
        //return PluginMain.getMyInstance().getManager().getPluginClassLoader(PluginMain.getMyInstance().getDescriptor());
        return Class.forName("com.mysql.jdbc.Driver").getClassLoader();
    }

     /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin#write(biz.wolschon.fileformats.gnucash.GnucashWritableFile)
     */
    @Override
    public void write(final GnucashWritableFile aFile) throws IOException, JAXBException {

        JOptionPane.showMessageDialog(null, "WARNING! MySQL-support is still incomplete");
//        if (myLastLoadedFile == null) {
//            return;
//        }
//        SSHDialog dialog = myLastLoadedFile;
//        saveFileViaSSH(dialog.getRemoteHostUserInfo(), dialog.getSSHTunnelUserInfo(), dialog.getRemotePath(), aFile);

    }
    /* (non-Javadoc)
     * @see biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin#writeTo(biz.wolschon.fileformats.gnucash.GnucashWritableFile)
     */
    @Override
    public void writeTo(final GnucashWritableFile aFile) throws IOException, JAXBException {

        JOptionPane.showMessageDialog(null, "WARNING! MySQL-support is still incomplete");
//        SSHDialog dialog = new SSHDialog();
//        dialog.setVisible(true);
//
//        myLastLoadedFile = dialog;
//        write(aFile);
    }

}
