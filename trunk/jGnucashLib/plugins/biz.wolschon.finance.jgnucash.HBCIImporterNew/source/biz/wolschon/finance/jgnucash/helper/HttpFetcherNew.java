/**
 * OsCommerceOrderImporter.java
 * Created on 27.07.2008
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 *
 *  Permission is granted to use, modify, publish and sub-license this code
 *  as specified in the contract. If nothing else is specified these rights
 *  are given non-exclusively with no restrictions solely to the contractor(s).
 *  If no specified otherwise I reserve the right to use, modify, publish and
 *  sub-license this code to other parties myself.
 *
 * Otherwise, this code is made available under GPLv3 or later.
 *
 * -----------------------------------------------------------
 * major Changes:
 *  27.07.2008 - initial version
 * ...
 *
 */
package biz.wolschon.finance.jgnucash.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * created: 27.07.2008 <br/>
 *
 * Helper-Class to getch a URL with HTTP-Basic-Auth.
 *
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */

public final class HttpFetcherNew {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Log LOGGER = LogFactory.getLog(HttpFetcherNew.class);

    /**
     * Utility-class with no instances.
     */
    private HttpFetcherNew() {

    }

    /**
     * Fetch the given URL with http-basic-auth.
     * @param url the URL
     * @param username username
     * @param password password
     * @return the page-content or null
     */
    public static String fetchURL(final URL url, final String username, final String password) {
        StringWriter sw = new StringWriter();

        try {
            PrintWriter  pw = new PrintWriter(sw);
            InputStream content = fetchURLStream(url, username, password);
            BufferedReader in   =
                new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                pw.println(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LOGGER.error("cannot fetch malformed URL " + url, e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("cannot fetch URL " + url, e);
            return null;
        }
        return sw.toString();
    }

    /**
     * Fetch the given URL with http-basic-auth.
     * @param url the URL
     * @param username username
     * @param password password
     * @return the page-content or null
     */
    public static InputStream fetchURLStream(final URL url, final String username, final String password) {

        try {
            String userPassword = username + ":" + password;

            // Encode String
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());

            URLConnection uc = url.openConnection();
            uc.setRequestProperty("Authorization", "Basic " + encoding);
            return uc.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LOGGER.error("cannot fetch malformed URL " + url, e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("cannot fetch URL " + url, e);
            return null;
        }
    }


}
