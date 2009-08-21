/**
 * GnucashDBCommodity.javaTransactionMenuAction.java
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

/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: jgnucashLib-GPL<br/>
 * GnucashDBCommodity<br/>
 * created: 11.08.2009 <br/>
 *<br/><br/>
 * <b>This class represents a currency/commodity in a Gnucash 2.3.3 -database</b>
 * @author  <a href="mailto:Marcus@Wolschon.biz">fox</a>
 */
public class GnucashDBCommodity {

    private final String myGUID;
    /**
     * @param aGuid
     * @param aNamespace
     * @param aMnemonic
     * @param aName
     * @param aCusip
     * @param aFraction
     */
    protected GnucashDBCommodity(String aGuid, String aNamespace,
            String aMnemonic, String aName, String aCusip, int aFraction) {
        super();
        myGUID = aGuid;
        myNamespace = aNamespace;
        myMnemonic = aMnemonic;
        myName = aName;
        myCUSIP = aCusip;
        myFraction = aFraction;
    }
    /**
     * @return the namespace
     */
    public String getNamespace() {
        return myNamespace;
    }
    /**
     * @param aNamespace the namespace to set
     */
    public void setNamespace(String aNamespace) {
        myNamespace = aNamespace;
    }
    /**
     * @return the mnemonic
     */
    public String getMnemonic() {
        return myMnemonic;
    }
    /**
     * @param aMnemonic the mnemonic to set
     */
    public void setMnemonic(String aMnemonic) {
        myMnemonic = aMnemonic;
    }
    /**
     * @return the name
     */
    public String getName() {
        return myName;
    }
    /**
     * @param aName the name to set
     */
    public void setName(String aName) {
        myName = aName;
    }
    /**
     * @return the cUSIP
     */
    public String getCUSIP() {
        return myCUSIP;
    }
    /**
     * @param aCusip the cUSIP to set
     */
    public void setCUSIP(String aCusip) {
        myCUSIP = aCusip;
    }
    /**
     * @return the fraction
     */
    public int getFraction() {
        return myFraction;
    }
    /**
     * @param aFraction the fraction to set
     */
    public void setFraction(int aFraction) {
        myFraction = aFraction;
    }
    /**
     * @return the gUID
     */
    public String getGUID() {
        return myGUID;
    }
    private String myNamespace;
    private String myMnemonic;
    private String myName;
    private String myCUSIP;
    private int myFraction;
}
