/**
 * created: 28.09.2008 <br/>
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 *
 */
package biz.wolschon.finance.jgnucash.plugin;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import biz.wolschon.fileformats.gnucash.GnucashWritableFile;

/**
 * created: 28.09.2008 <br/>
 *
 * This is a plugin-interface that plugins that want to plug
 * into the extension-point "Importer" of the
 * "biz.wolschon.finance.jgnucash.editor.main"- plugin.<br/>
 * Extension - point declaration:<br/>
 * <pre>
    <extension-point id="DataSource"><!-- displayed in file-menu as "load <xyz>..." -->
        <parameter-def id="class" /> <!-- class must implement biz.wolschon.finance.jgnucash.plugin.DataSourcePlugin -->
        <parameter-def id="name" />
        <parameter-def id="description" multiplicity="none-or-one" />
        <parameter-def id="icon" multiplicity="none-or-one" />
        <parameter-def id="supportsWriting"/> <!-- "true" or "false" : Supports a menu-handler "write file" that writes to where it was loaded from. -->
        <parameter-def id="supportsWritingTo"/> <!-- "true" or "false" : Supports a menu-handler "write file to...". -->
    </extension-point>
  </pre>
 *
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 *
 */
public interface DataSourcePlugin {

    /**
     * Runt the actual import.
     * @return the loaded file or null
     * @throws IOException on IO-issues
     * @throws JAXBException on IO-issues
     */
    GnucashWritableFile loadFile() throws IOException, JAXBException;

    /**
     * Write to where this file was loaded from.
     * @param file the file to write
     * @throws IOException on IO-issues
     * @throws JAXBException on IO-issues
     */
    void write(GnucashWritableFile file) throws IOException, JAXBException;

    /**
     * Let the user choose a location to write to.
     * @param file the file to write
     * @throws IOException on IO-issues
     * @throws JAXBException on IO-issues
     */
    void writeTo(GnucashWritableFile file) throws IOException, JAXBException;
}
