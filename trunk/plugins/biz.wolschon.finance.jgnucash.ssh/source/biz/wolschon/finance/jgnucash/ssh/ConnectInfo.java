/**
 * ConnectInfo.java
 * created: 12.12.2008
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

package biz.wolschon.finance.jgnucash.ssh;

/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * ConnectInfo<br/>
 * created: 12.12.2008 <br/>
 *<br/><br/>
 * <b>TODO: describe the purpose of this class.</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class ConnectInfo {

    private String myUserName;
    /**
     * @param aUserName
     * @param aPassword
     * @param aHostName
     * @param aHosstPost
     */
    public ConnectInfo(String aUserName, String aPassword, String aHostName,
            int aHosstPost) {
        super();
        myUserName = aUserName;
        myPassword = aPassword;
        myHostName = aHostName;
        myHostPost = aHosstPost;
    }
    /**
     * @param aUserName
     * @param aPassword
     * @param aHostName
     */
    public ConnectInfo(String aUserName, String aPassword, String aHostName) {
        super();
        myUserName = aUserName;
        myPassword = aPassword;
        myHostName = aHostName;
    }
    private String myPassword;
    private String myHostName;
    private int myHostPost = 22;
    /**
     * @return the userName
     */
    public String getUserName() {
        return myUserName;
    }
    /**
     * @param aUserName the userName to set
     */
    public void setUserName(String aUserName) {
        myUserName = aUserName;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return myPassword;
    }
    /**
     * @param aPassword the password to set
     */
    public void setPassword(String aPassword) {
        myPassword = aPassword;
    }
    /**
     * @return the hostName
     */
    public String getHostName() {
        return myHostName;
    }
    /**
     * @param aHostName the hostName to set
     */
    public void setHostName(final String aHostName) {
        myHostName = aHostName;
    }
    /**
     * @return the hosstPost
     */
    public int getHostPost() {
        return myHostPost;
    }
    /**
     * @param aHosstPost the hosstPost to set
     */
    public void setHostPost(final int aHosstPost) {
        myHostPost = aHosstPost;
    }
}
