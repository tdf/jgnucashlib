//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2008.11.14 um 02:26:13 MEZ 
//

package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.helpers.ValidationEventImpl;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.xml.bind.validator.Locator;

/**
 * Receives errors through {@link ErrorHandler} and reports to the
 * {@link SAXUnmarshallerHandler}.
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class ErrorHandlerAdaptor implements ErrorHandler {
    
    /** the client event handler that will receive the validation events */
    private final SAXUnmarshallerHandler host;
    
    /** the locator object responsible for filling in the validation event
     *  location info **/
    private final Locator locator;
   
    public ErrorHandlerAdaptor(
        SAXUnmarshallerHandler _host, Locator locator ) {
        this.host = _host;
        this.locator = locator;
    }
    
    public void error(SAXParseException exception) 
        throws SAXException {
            
        propagateEvent( ValidationEvent.ERROR, exception );
    }
    
    public void warning(SAXParseException exception) 
        throws SAXException {
            
        propagateEvent( ValidationEvent.WARNING, exception );
    }
    
    public void fatalError(SAXParseException exception) 
        throws SAXException {
            
        propagateEvent( ValidationEvent.FATAL_ERROR, exception );
    }
    
    private void propagateEvent( int severity, SAXParseException saxException ) 
        throws SAXException {
            
        // get location info:
        //     sax locators simply use the location info embedded in the 
        //     sax exception, dom locators keep a reference to their DOMScanner
        //     and call back to figure out where the error occurred.
        ValidationEventLocator vel = 
            locator.getLocation( saxException );

        ValidationEventImpl ve = 
            new ValidationEventImpl( severity, saxException.getMessage(), vel  );

        Exception e = saxException.getException();
        if( e != null ) {
            ve.setLinkedException( e );
        } else {
            ve.setLinkedException( saxException );
        }
        
        // call the client's event handler.
        host.handleEvent( ve, severity!=ValidationEvent.FATAL_ERROR );
    }
}
