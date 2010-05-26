//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2010.05.26 um 03:04:34 CEST 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated;


/**
 * Java content class for gnc_account element declaration.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 761)
 * <p>
 * <pre>
 * &lt;element name="gnc_account">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="act_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="act_id">
 *             &lt;complexType>
 *               &lt;simpleContent>
 *                 &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                   &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;/extension>
 *               &lt;/simpleContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="act_type">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ASSET"/>
 *               &lt;enumeration value="BANK"/>
 *               &lt;enumeration value="CASH"/>
 *               &lt;enumeration value="CREDIT"/>
 *               &lt;enumeration value="EQUITY"/>
 *               &lt;enumeration value="EXPENSE"/>
 *               &lt;enumeration value="INCOME"/>
 *               &lt;enumeration value="LIABILITY"/>
 *               &lt;enumeration value="MUTUAL"/>
 *               &lt;enumeration value="RECEIVABLE"/>
 *               &lt;enumeration value="STOCK"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="act_commodity" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element ref="{}cmdty_space"/>
 *                     &lt;element ref="{}cmdty_id"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="act_commodity-scu" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="100"/>
 *               &lt;enumeration value="1000"/>
 *               &lt;enumeration value="10000"/>
 *               &lt;enumeration value="100000"/>
 *               &lt;enumeration value="1000000"/>
 *               &lt;enumeration value="10000000"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="act_non-standard-scu" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="act_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="act_description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="act_slots" type="{}slots_type" minOccurs="0"/>
 *           &lt;element name="act_parent" minOccurs="0">
 *             &lt;complexType>
 *               &lt;simpleContent>
 *                 &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                   &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;/extension>
 *               &lt;/simpleContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="act_lots" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="gnc_lot" maxOccurs="unbounded" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="lot_id">
 *                                 &lt;complexType>
 *                                   &lt;simpleContent>
 *                                     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                                       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;/extension>
 *                                   &lt;/simpleContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="lot_slots" type="{}slots_type"/>
 *                             &lt;/sequence>
 *                             &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *         &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface GncAccount
    extends javax.xml.bind.Element, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncAccountType
{


}
