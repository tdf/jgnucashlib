//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2010.01.10 um 03:17:24 CET 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated;


/**
 * Java content class for gnc-v2 element declaration.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 27)
 * <p>
 * <pre>
 * &lt;element name="gnc-v2">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{}gnc_count-data"/>
 *           &lt;element name="gnc_book">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="book_id">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                             &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="book_slots" type="{}slots_type" minOccurs="0"/>
 *                     &lt;element ref="{}gnc_count-data" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;group ref="{}book_elements" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;/sequence>
 *                   &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface GncV2
    extends javax.xml.bind.Element, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type
{


}
