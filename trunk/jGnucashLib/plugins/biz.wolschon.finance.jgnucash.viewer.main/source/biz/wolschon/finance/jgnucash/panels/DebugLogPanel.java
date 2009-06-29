/**
 * DebugLogPanel.java
 * created: 19.01.2008 09:09:18
 * (c) 2008 by <a href="http://Wolschon.biz">Wolschon Softwaredesign und Beratung</a>
 * This file is part of jGnucash by Marcus Wolschon <a href="mailto:Marcus@Wolscon.biz">Marcus@Wolscon.biz</a>.
 * You can purchase support for a sensible hourly rate or
 * a commercial license of this file (unless modified by others) by contacting him directly.
 *
 *  jGnucash-RO is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  jGnucash-RO is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with traveling_salesman.  If not, see <http://www.gnu.org/licenses/>.
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
package biz.wolschon.finance.jgnucash.panels;

//other imports

//automatically created logger for debug and error -output
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

//automatically created propertyChangeListener-Support
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;


/**
 * (c) 2008 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: traveling_salesman<br/>
 * DebugLogPanel.java<br/>
 * created: 19.01.2008 09:09:18 <br/>
 *<br/><br/>
 * This panel is to show the logging-output for debugging.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public class DebugLogPanel extends JPanel {

    /**
     * Automatically created logger for debug and error-output.
     */
    private static final Logger LOG = Logger.getLogger(
                                            DebugLogPanel.class.getName());

    /**
     * The area we are logging to.
     */
    private JTextPane myLogArea = new JTextPane();

    /**
     * Initialize the panel.
     */
    public DebugLogPanel() {
        this.setLayout(new BorderLayout());
        this.myLogArea.setText("");
        this.myLogArea.setStyledDocument(new DefaultStyledDocument());
        this.myLogArea.setPreferredSize(new Dimension(1000, 400));
        this.add(new JScrollPane(this.myLogArea), BorderLayout.CENTER);

        initializeLog4JLogging();

        // Display log-messages of Java-Logging
        initializeForJavaLogging();
    }

	/**
	 * 
	 */
	private void initializeLog4JLogging() {
		org.apache.log4j.Logger.getRootLogger().addAppender(new Appender() {

        	private Layout layout = null;
        	private ErrorHandler errorHandler = null;
        	private String name = "DebugLogPanel";
        	private Filter filter = null;

			@Override
			public void addFilter(Filter arg0) {
				this.filter = arg0;
			}

			@Override
			public void clearFilters() {
				this.filter = null;
			}

			@Override
			public void close() {
				
			}

			@Override
			public void doAppend(final LoggingEvent aRecord) {
				
				//getLayout().format(aRecord);
				try {
                    // decide color or message
                    SimpleAttributeSet attrs = new SimpleAttributeSet();
                    if (aRecord.getLevel() == org.apache.log4j.Level.ALL)
                        StyleConstants.setForeground(attrs, Color.LIGHT_GRAY);
                    if (aRecord.getLevel() == org.apache.log4j.Level.DEBUG)
                        StyleConstants.setForeground(attrs, Color.LIGHT_GRAY);
                    if (aRecord.getLevel() == org.apache.log4j.Level.INFO)
                        StyleConstants.setForeground(attrs, Color.GRAY);
                    if (aRecord.getLevel() == org.apache.log4j.Level.TRACE)
                        StyleConstants.setForeground(attrs, Color.GRAY);
                    if (aRecord.getLevel() == org.apache.log4j.Level.WARN)
                        StyleConstants.setForeground(attrs, Color.ORANGE);
                    if (aRecord.getLevel() == org.apache.log4j.Level.ERROR)
                        StyleConstants.setForeground(attrs, Color.RED);
                    if (aRecord.getLevel() == org.apache.log4j.Level.FATAL)
                          StyleConstants.setForeground(attrs, Color.RED);

                    // print message
                    myLogArea.getDocument().insertString(
                            myLogArea.getDocument().getLength(),
                            "\n" + aRecord.getLevel().toString() + " "
                            + "in " + aRecord.getLocationInformation().getClassName() + ":" + aRecord.getLocationInformation().getMethodName() + "(...) :" + aRecord.getLocationInformation().getLineNumber()
                            + aRecord.getMessage(), attrs);

                    // print stack-trace
                    if (aRecord.getThrowableInformation() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        aRecord.getThrowableInformation().getThrowable().printStackTrace(pw);
                        myLogArea.getDocument().insertString(
                                myLogArea.getDocument().getLength(),
                                "\n" + sw.getBuffer(), attrs);
                    }
                } catch (BadLocationException e) {
                    LOG.log(Level.SEVERE, "[BadLocationException] Problem in "
                               + getClass().getName(),
                                 e);
                }
			}

			@Override
			public ErrorHandler getErrorHandler() {
				return errorHandler;
			}

			@Override
			public Filter getFilter() {
				return filter;
			}

			@Override
			public Layout getLayout() {
				return layout;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public boolean requiresLayout() {
				return false;
			}

			@Override
			public void setErrorHandler(ErrorHandler arg0) {
				this.errorHandler = arg0;
			}

			@Override
			public void setLayout(final Layout arg0) {
				this.layout = arg0;
			}

			@Override
			public void setName(String arg0) {
				this.name = arg0;
			}
        	
        });
	}

	/**
	 * 
	 */
	private void initializeForJavaLogging() {
		Logger.getLogger("").addHandler(new Handler() {

            @Override
            public void close() {
                try {
                    myLogArea.getDocument().insertString(myLogArea.getDocument().getLength(), "\n***LOGGING STOPPED***", null);
                } catch (BadLocationException e) {
                    LOG.log(Level.SEVERE, "[BadLocationException] Problem in "
                               + getClass().getName(),
                                 e);
                }
            }

            @Override
            public void flush() {
            }

            @Override
            public void publish(final LogRecord aRecord) {
                try {
                    // decide color or message
                    SimpleAttributeSet attrs = new SimpleAttributeSet();
                    if (aRecord.getLevel() == Level.FINEST)
                        StyleConstants.setForeground(attrs, Color.LIGHT_GRAY);
                    if (aRecord.getLevel() == Level.FINER)
                        StyleConstants.setForeground(attrs, Color.GRAY);
                    if (aRecord.getLevel() == Level.FINE)
                        StyleConstants.setForeground(attrs, Color.GRAY);
                    if (aRecord.getLevel() == Level.WARNING)
                        StyleConstants.setForeground(attrs, Color.ORANGE);
                    if (aRecord.getLevel() == Level.SEVERE)
                          StyleConstants.setForeground(attrs, Color.RED);

                    // print message
                    myLogArea.getDocument().insertString(
                            myLogArea.getDocument().getLength(),
                            "\n" + aRecord.getLevel() + " "
                            + "in " + aRecord.getSourceClassName() + ":" + aRecord.getSourceMethodName() + "(...) "
                            + aRecord.getMessage(), attrs);

                    // print stack-trace
                    if (aRecord.getThrown() != null) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        aRecord.getThrown().printStackTrace(pw);
                        myLogArea.getDocument().insertString(
                                myLogArea.getDocument().getLength(),
                                "\n" + sw.getBuffer(), attrs);
                    }
                } catch (BadLocationException e) {
                    LOG.log(Level.SEVERE, "[BadLocationException] Problem in "
                               + getClass().getName(),
                                 e);
                }
            }
            });
	}

    //------------------------ support for propertyChangeListeners ------------------

    /**
     * support for firing PropertyChangeEvents.
     * (gets initialized only if we really have listeners)
     */
    private volatile PropertyChangeSupport myPropertyChange = null;

    /**
     * Returned value may be null if we never had listeners.
     * @return Our support for firing PropertyChangeEvents
     */
    protected PropertyChangeSupport getPropertyChangeSupport() {
        return myPropertyChange;
    }

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     *
     * @param listener  The PropertyChangeListener to be added
     */
    public final void addPropertyChangeListener(
            final PropertyChangeListener listener) {
        if (myPropertyChange == null) {
            myPropertyChange = new PropertyChangeSupport(this);
        }
        myPropertyChange.addPropertyChangeListener(listener);
    }

    /**
     * Add a PropertyChangeListener for a specific property.  The listener
     * will be invoked only when a call on firePropertyChange names that
     * specific property.
     *
     * @param propertyName  The name of the property to listen on.
     * @param listener  The PropertyChangeListener to be added
     */
    public final void addPropertyChangeListener(final String propertyName,
            final PropertyChangeListener listener) {
        if (myPropertyChange == null) {
            myPropertyChange = new PropertyChangeSupport(this);
        }
        myPropertyChange.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Remove a PropertyChangeListener for a specific property.
     *
     * @param propertyName  The name of the property that was listened on.
     * @param listener  The PropertyChangeListener to be removed
     */
    public final void removePropertyChangeListener(final String propertyName,
            final PropertyChangeListener listener) {
        if (myPropertyChange != null) {
            myPropertyChange.removePropertyChangeListener(propertyName,
                    listener);
        }
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     *
     * @param listener  The PropertyChangeListener to be removed
     */
    public synchronized void removePropertyChangeListener(
            final PropertyChangeListener listener) {
        if (myPropertyChange != null) {
            myPropertyChange.removePropertyChangeListener(listener);
        }
    }

    //-------------------------------------------------------

    /**
     * Just an overridden ToString to return this classe's name
     * and hashCode.
     * @return className and hashCode
     */
    public String toString() {
        return "DebugLogPanel@" + hashCode();
    }
}


