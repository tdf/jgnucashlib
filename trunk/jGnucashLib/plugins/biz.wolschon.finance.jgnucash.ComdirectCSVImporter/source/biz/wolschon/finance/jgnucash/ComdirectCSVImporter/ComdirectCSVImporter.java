/**
 * ComdirectCSVImporter.java
 * Created on 16.05.2005
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
 *  16.05.2005 - initial version
 * ...
 *
 */
package biz.wolschon.finance.jgnucash.ComdirectCSVImporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

import biz.wolschon.fileformats.gnucash.GnucashWritableAccount;
import biz.wolschon.numbers.FixedPointNumber;

/**
 * created: 16.05.2005 <br/>
 * THIS IS VERY OLD CODE AND HAS BEEN REPLACED BY THE SCRIPTABLE HBCI-Importer.<br/>
 *
 *
 * Implementation of CVSImporter that knows the CSV-format of the Commdirect Bank AG
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class ComdirectCSVImporter extends CSVImporter {




    private static final String COMDIRECT_LOGIN_URL = "https://brokerage.comdirect.de/servlet/de.comdirect.usecase.login.LoginInputServlet";
    private static final String COMDIRECT_CSV_EXPORT_URL = "https://brokerage.comdirect.de/brokerage/konto/umsaetze_export.jsp";

    /**
     * @param account
     */
    public ComdirectCSVImporter(final GnucashWritableAccount account) {
        super(account);
    }

    /**
     *
     * TODO: import directly from web-page
     *
     * <html>
<head>
<title>Umsätze exportieren</title>

</head>
<body class="popupBody">

<form action='https://brokerage.comdirect.de/brokerage/konto/umsaetze_export.jsp' name="export" id="export" method="post" onsubmit="alert('hihu');">
<b>Wählen Sie das Format</b><br/>
<input type="radio" name="format" value="html" checked class="radioWhite"/><br/>
<input type="radio" name="format" value="csv" class="radioWhite"/><br/>
<a href="javascript:document.forms[0].submit();" title="Export starten"><b>Export starten</b></a>
</form>

</body>

</html>

     */

//    private void requestCSVPage(Client http, HttpConfiguration httpConfig, boolean opened)  throws IOException{
//
//        System.out.print("requestCSVPage");
//
//        httpConfig.setRequest("POST", COMDIRECT_CSV_EXPORT_URL);
//
//        String bodyString = "format=csv";
//        byte body[] = bodyString.getBytes();// we use the System-encoding here
//
//        httpConfig.setRequestHeader( "User-Agent", "GnucashImporter" );
//        httpConfig.setRequestHeader( "Content-Type", "application/x-www-form-urlencoded" );
//        httpConfig.setRequestHeader( "Content-Length", ""+body.length );
//        httpConfig.setRequestBody(body);
//
//        if(!opened) {
//            System.out.println("opening connection...");
//            http.open();
//        }
//
//        System.out.println("sending csv-request");
//        sendHttpRequest(http, httpConfig);
//    }
//
//    private void requestLoginPage(Client http, HttpConfiguration httpConfig, boolean opened)  throws IOException{
//
//        System.out.print("requestLoginPage");
//
//        httpConfig.setRequest("GET", "https://brokerage.comdirect.de/brokerage/login/login.jsp?ziel=login&errorCode=0&permissionId=memberdatamaintenance");
//
//
//        httpConfig.setRequestHeader( "User-Agent", "GnucashImporter" );
//
//        if(!opened) {
//            System.out.println("opening connection...");
//            http.open(httpConfig);
//        }
//
//        System.out.println("sending loginpage-request");
//        sendHttpRequest(http, httpConfig);
//    }
//
//

    public void importCSV() throws IOException, ParseException {

        URL requestLoginPageURL2 = new URL("https://brokerage.comdirect.de/servlet/de.comdirect.usecase.login.LoginInputServlet");
        Map cookies = new HashMap();
//        String pageStr = getURLContent(requestLoginPageURL2, cookies, "param1=XXXXXXXX&param2=XXXXXX&direktzu=KontoUebersicht");
//
//        System.err.println("====== login-page directly shown:\n"+pageStr);
//
//        if(pageStr.indexOf("<b>PIN oder Passwort</b>")==-1) {
//
//         System.out.println("==== It WORKED!!! ====");
//         return;
//        }


        URL requestLoginPageURL = new URL("https://brokerage.comdirect.de/brokerage/login/login.jsp?ziel=login&errorCode=0&permissionId=memberdatamaintenance");
        String pageStr = getURLContent(requestLoginPageURL, cookies, null);







//        HttpConfiguration httpConfig = getHttpConfig();
//        Client http = new Client(httpConfig);
//
//
//        requestLoginPage(http, httpConfig, false);
//
//        byte page[] = http.getResponseText();
//        String pageContentType = http.getResponseHeader("Content-Type");
//        String pageStr = new String(page, "ISO-8859-1");
//
//
//
////        // if we get nothing, request the login-page
////        if(pageStr.length()==0) {
////            String loginBodyString = "param1=XXXXXXXX&param2=XXXXXX&direktzu=";
////            byte loginBody[] = loginBodyString.getBytes();
////
////            httpConfig.setRequest("POST", COMDIRECT_CSV_EXPORT_URL);
////            httpConfig.setRequestHeader( "User-Agent", "GnucashImporter" );
////            httpConfig.setRequestHeader( "Content-Type", "application/x-www-form-urlencoded" );
////            httpConfig.setRequestHeader( "Content-Length", ""+loginBody.length );
////            httpConfig.setRequestBody(loginBody);
////            httpConfig.setRequest("POST", COMDIRECT_LOGIN_URL);
////            sendHttpRequest(http, httpConfig);
////
////
////            page = http.getResponseText();
////            pageContentType = http.getResponseHeader("Content-Type");
////            pageStr = new String(page, "ISO-8859-1");
////
////        }
//
//
////        while(pageContentType.indexOf("csv") == -1) {
//
//
//        System.out.print("parsing login-page");
//
            int loginFormStart = pageStr.indexOf("<form name=\"login\" id=\"login\"");

            String action = getAction(pageStr, loginFormStart);

            if (loginFormStart == -1) {
                System.err.println("\n\n===================================\npage:\n"+pageStr);
//                http.close();
                throw new IllegalStateException("No login-form detected!!");
            }

            int loginFormEnd = pageStr.indexOf("</form", loginFormStart);
            if (loginFormEnd==-1) {
                loginFormEnd = pageStr.length();
            }

            System.out.println("\n\n===================================\nlogin-part of page:\n"+pageStr.substring(loginFormStart, loginFormEnd));

            String loginBodyString = "param1=XXXXXXXX&param2=XXXXXX&direktzu=KontoUebersicht";


			loginBodyString = getTransactionID(pageStr, loginFormStart, loginBodyString);



            pageStr = getURLContent(requestLoginPageURL, cookies, loginBodyString);
            System.err.println("\n\n===================================\npage after login:\n"+pageStr);
//
//            byte loginBody[] = loginBodyString.getBytes();
//
//            System.out.print("logging in");
//
//            httpConfig.setRequest("POST", COMDIRECT_CSV_EXPORT_URL);
//            httpConfig.setRequestHeader( "User-Agent", "GnucashImporter" );
//            httpConfig.setRequestHeader( "Content-Type", "application/x-www-form-urlencoded" );
//            httpConfig.setRequestHeader( "Content-Length", ""+loginBody.length );
//            httpConfig.setRequestBody(loginBody);
//            httpConfig.setRequest("POST", COMDIRECT_LOGIN_URL);
//            sendHttpRequest(http, httpConfig);
//            page = http.getResponseText();
//            pageContentType = http.getResponseHeader("Content-Type");
//            pageStr = new String(page, "ISO-8859-1");
//
//            if(pageStr.indexOf("<form name=\"login\" id=\"login\"") != -1) {
//             System.err.print("login failed!! page=\n"+pageStr);
//             http.close();
//             return;
//            } else {
//                System.out.print("login OK!!");
//            }
//
//
//            requestCSVPage(http, httpConfig, true);
////        }
//
//        http.close();
//
//        System.out.println("CSV: (length="+pageStr.length()+")\n"+pageStr);
//
//        importCSV(pageStr);

    }

    /**
     * get the action-part of the &lt;form-tag
     *
	 * @param pageStr
	 * @param loginFormStart start of the form-tag
	 * @return
	 */
	private String getAction(String pageStr, int loginFormStart) {

        int actionIndex = pageStr.indexOf("action=\"", loginFormStart);

        if (actionIndex == -1) {
         System.err.println("canot find 'action=' for form-tag! '"
                 + pageStr.substring(loginFormStart, Math.min(pageStr.length(), loginFormStart + 300))
                 + "'");
         return null;
        }

        actionIndex += "action=\"".length();
        // just defensive prgramming
        if (pageStr.charAt(actionIndex) == '\'') {
            actionIndex++;
        }


        int actionEndIndex = pageStr.indexOf("\"", actionIndex);

        if (actionEndIndex == -1) {
         System.err.println("canot find closing of 'action=' for form-tag! '" + pageStr.substring(loginFormStart, Math.min(pageStr.length(), loginFormStart + 300)) + "'");
         return null;
        }


        String action = pageStr.substring(actionIndex, actionEndIndex);

        System.out.println("getAction()='" + action + "'");


        return action;
    }

    /**
     * @param requestLoginPageURL
     * @param loginBodyString may ne null. if not null a POST-request is done with this content
     * @throws IOException
     */
    private String getURLContent(final URL requestLoginPageURL, final Map cookies, final String loginBodyString) throws IOException {

        System.err.println("Requesting URL: " + requestLoginPageURL);

        HttpsURLConnection connection = (HttpsURLConnection) requestLoginPageURL.openConnection();

        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; de; rv:1.6) Gecko/20040113");

        if (lastURL != null) {
            connection.addRequestProperty("Referer", lastURL.toString());
            System.out.println("sending Referer: " + lastURL.toString());
        }

        lastURL = requestLoginPageURL;

        // send cookies

        String cookiestring = "";
        for (Iterator iter = cookies.entrySet().iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            System.err.println("sending cookie: " + element.getKey().toString() + "=>" + element.getValue().toString());

            if (cookiestring.length() > 0) {
                cookiestring += ";";
            }
            cookiestring += element.getKey().toString() + "=" + element.getValue().toString();
        }
        if (cookiestring.length() > 0) {
            connection.addRequestProperty("Cookie", cookiestring);
            System.err.println("sending cookie-header: " + cookiestring);
        }


        // send POST-content

        if (loginBodyString != null) {
            connection.setRequestMethod("POST");
            byte[] body = loginBodyString.getBytes();
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.addRequestProperty("Content-Length", "" + body.length);
            System.err.println("sending post-body: '" + loginBodyString + "'");
            connection.setDoOutput(true);
            connection.connect();
            OutputStream out = connection.getOutputStream();
            out.write(body);
            out.flush();
            out.close();
        } else {
            connection.connect();
        }




        System.err.println("ContentType: "   + connection.getContentType());
        System.err.println("ContentLength: " + connection.getContentLength());
        System.err.println("ResponseCode: "  + connection.getResponseCode());

        String encoding = connection.getContentEncoding();
        System.err.println("encoding: " + encoding);
        if (encoding == null || encoding.trim().length() == 0) {
            encoding = "ISO-8859-15";
        }

        for (Object element : connection.getHeaderFields().entrySet()) {
            Map.Entry entry = (Map.Entry) element;
            System.err.println("header field: '"+entry.getKey()+"'=>'"+entry.getValue()+"'");

            if(entry.getKey() != null && entry.getKey().equals("Set-Cookie")) {
                List cookie = (List)entry.getValue();
                String name2value = (String)cookie.get(0);

                int index = name2value.indexOf(';');
                if(index>-1) {
                    name2value = name2value.substring(0, index);
                }

                index = name2value.indexOf('=');

                String name = name2value.substring(0, index).trim();
                String value = name2value.substring(index+1).trim();

                System.err.println("cookie: "+name+"=>"+value);
                cookies.put(name, value);
            }


        }



//        for (Iterator iter = connection.getRequestProperties().entrySet().iterator(); iter.hasNext();) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            System.err.println("request properties: '"+entry.getKey()+"'=>'"+entry.getValue()+"'");
//		}


		InputStream in = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(in, encoding);
        StringWriter sw = new StringWriter();
        char buf[] = new char[255];
        int len = -1;
        while((len = reader.read(buf)) != -1) {
            sw.write(buf, 0, len);
        }
        reader.close();
        connection.disconnect();

        String retval = sw.toString();

        int index =  -1;
        String JSREDIRECT = "<body onLoad=\"top.location.href='";
		if((index = retval.indexOf(JSREDIRECT)) != -1) {
            index += JSREDIRECT.length();

            // just defensive prgramming
            if(retval.charAt(index)=='\'') {
                index++;
            }

            String newurl = retval.substring(index, retval.indexOf('\'', index));

            System.err.println("redirectig to '"+newurl+"'");

            URL url2 = new URL(requestLoginPageURL.getProtocol(), requestLoginPageURL.getHost(), requestLoginPageURL.getPort(), newurl);
            return getURLContent(url2, cookies, null);
        }

        return retval;
	}


    protected URL lastURL = null;

	/**
	 * @param http
	 */
//	private void sendHttpRequest(Client http, HttpConfiguration config) {
//
//        System.out.println("sending to: "+config.getURL()); //we have to do this because Client does not allow us to get it's config
//
//
//		http.send();
//
//		if(http.getErrorMessage() != null && http.getErrorMessage().indexOf("Connection closed by remote host") != -1) {
//		 System.out.println("reconnecting closing...");
//		 http.close();
//         System.out.println("reconnecting opening...");
//		 http.open();
//         System.out.println("reconnecting sending...");
//		 http.send();
//		}
//
//
//
//        System.out.println("content-type="+http.getResponseHeader("Content-Type")+" error-code="+http.getStatusCode()+" error-msg='"+http.getErrorMessage()+"' content-length="+(http.getResponseText()==null?"null":""+http.getResponseText().length)+" headers="+http.getAllResponseHeaders());
//
//	}

	/**
	 * @param pageStr
	 * @param loginFormStart
	 * @param loginBodyString
	 * @return
	 */
	private String getTransactionID(String pageStr, int loginFormStart, String loginBodyString) {
		String SEARCHSTRING = "<input type=\"hidden\" name=\"transactionID\" id=\"transactionID\" value=\"";
		int transactionIdIndex = pageStr.indexOf(SEARCHSTRING, loginFormStart);
		if(transactionIdIndex==-1) {
		 System.err.println("count not find transactionID-parameter");
		}
		else {

            transactionIdIndex += SEARCHSTRING.length();
//          just defensive prgramming
            if(pageStr.charAt(transactionIdIndex)=='\"') {
                transactionIdIndex++;
            }


		    int transactionIdIndexEnd = pageStr.indexOf("\"", transactionIdIndex);
		    if(transactionIdIndexEnd==-1) {
		        System.err.println("count not find end of transactionID-parameter");
		    } else {
		        String transactionID = pageStr.substring(transactionIdIndex+1, transactionIdIndexEnd);
		        System.out.println("transactionID="+transactionID);

		        loginBodyString += "&transactionID="+transactionID;
		    }
		}
		return loginBodyString;
	}

	public void importCSV(File csvfile) throws IOException, ParseException {

        FileReader filereader = new FileReader(csvfile);
        BufferedReader linesReader = new BufferedReader(filereader);


        importCSV(linesReader);


    }

    /**
     * @param linesReader
     * @throws IOException
     */
    private void importCSV(String csv) throws IOException {
        importCSV(new BufferedReader(new StringReader(csv)));

    }

    /**
	 * @param linesReader
	 * @throws IOException
	 */
	private void importCSV(BufferedReader linesReader) throws IOException {
		NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        DateFormat   buchungstagFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat   csvDayFormat = new SimpleDateFormat("\"E, dd.MM.yyyy, HH:mm 'Uhr'\"", Locale.GERMANY);
        DateFormat   valutaFormat = new SimpleDateFormat("dd.MM.yyyy");
        NumberFormat umsatzFormat = NumberFormat.getInstance(Locale.ENGLISH);




        String line = linesReader.readLine();
        String token = null;




        double balance = -1;
        Date   date = null;

        while((line = linesReader.readLine()) != null) {
            try {
                if(line.trim().length()==0) {
                    continue;
                }

                if(line.startsWith("\"Kontostand:\"\t\"") || line.startsWith("\"Neuer Kontosaldo\"\t\"")) {
                    StringTokenizer tokenizer = new StringTokenizer(line, "\t", false);

                    token = tokenizer.nextToken().trim();
                    token = extractToken(tokenizer);
                    token = token.trim();
                    if(token.startsWith("+")) {
                        token = token.substring(1);
                    }
                    balance = currencyFormat.parse(token.replaceFirst(" EUR", "")).doubleValue();

                    System.err.println("if all is well we should end up with a balance of: "+balance);

                    continue;
                }

                if(line.equalsIgnoreCase("\"comdirect bank AG\"")) {
                    continue;
                }

                if(line.startsWith("\"UMSÄTZE:")) {
                    continue;
                }

                if(line.equals("\"Umsätze Girokonto\"\t\" - Zeitraum: Neue Umsätze\"")) {
                    continue;
                }

                if(line.startsWith("\"Kunden-Nummer:")) {
                    continue;
                }

                if(line.startsWith("\"BLZ:")) {
                    continue;
                }

                if(line.startsWith("\"Verf")) {
                    continue;
                }

                if(line.startsWith("\"Buchungstag")) {
                    continue;
                }


                if(line.indexOf('\"', 1)==line.length()-1) {
                    try {
                        date = csvDayFormat.parse(line);
                        System.err.println("csv-export is from: "+DateFormat.getDateTimeInstance().format(date));
                        continue;
                    } catch(ParseException x) {

                    }

                }



                // we reached the end?
                if(line.startsWith("\"Alter Kontosaldo\"")) {
                    break;
                }

				Object tokens[] = csvLineFormat.parse(line);
				Date buchungstag = buchungstagFormat.parse((String)tokens[0]);
				Date valuta      = buchungstagFormat.parse((String)tokens[1]);
				String vorgang   = (String)tokens[2];
				String buchungstext   = (String)tokens[3];
				FixedPointNumber umsatzInEur = new FixedPointNumber((String)tokens[4]);

                System.err.println("["+buchungstagFormat.format(buchungstag)+"-"+valutaFormat.format(valuta)+"]<"+vorgang+"> "+umsatzInEur.doubleValue()+"  \""+buchungstext+"\"");
				importTransaction(buchungstag, valuta, vorgang, buchungstext, umsatzInEur);
			} catch (ParseException e) {
				e.printStackTrace();
				System.err.println(line);
			}
        }



        // date may be null, in that case we use the curret date and time
        double realBalance = getAccount().getBalance(date).doubleValue();
        if(balance != realBalance) {
            System.err.println("Problem!! balance should now be "+currencyFormat.format(balance)+" but is "+currencyFormat.format(realBalance));
        } else {
            System.out.println("Balance is OK should be "+currencyFormat.format(balance)+", is "+currencyFormat.format(realBalance));
        }
	}

	/**
     * sample-line:
     * "12.05.2005"    "12.05.2005"    "Lastschrift Einzug"    "OSPA                              GEBÜHREN FÜR SALDENBESTÄTIG       . REKL-NR.2643                    EZV-REF 111055016753 BLZ 13050000 "      "-6.00"
     */
    MessageFormat csvLineFormat = new MessageFormat(
            "\"{0}\"\t"+ // Buchungstag
            "\"{1}\"\t"+ // Valuta-Datum
            "\"{2}\"\t"+ // Vorgang
            "\"{3}\"\t"+ // Buchungstext
            "\"{4}\""  // Umsatz in EUR
            );
	//protected HttpConfiguration httpConfig = null;


	/**
     * gets the next token and removes the parenthesis
	 * @param tokenizer
	 */
	private String extractToken(StringTokenizer tokenizer) throws IOException{
        String token = tokenizer.nextToken().trim();
		if(token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length()-1).trim();
        }
        return token;
	}


    /**
     * We cache the Http-config because it caches cookies
     * and thus allows us to stay logged it for multiple
     * requests.
     * @return
     */
//	protected HttpConfiguration getHttpConfig() {
//        if(httpConfig==null) {
//            httpConfig = new HttpConfiguration();
//            httpConfig.enableCookies(true);
//        }
//
//		return httpConfig;
//	}
}
