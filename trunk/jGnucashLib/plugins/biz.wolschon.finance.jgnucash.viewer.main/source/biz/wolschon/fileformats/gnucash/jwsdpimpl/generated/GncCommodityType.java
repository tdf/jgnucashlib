//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2010.05.27 um 06:59:09 CEST 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated;


/**
 * Java content class for anonymous complex type.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 63)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}cmdty_space"/>
 *         &lt;element ref="{}cmdty_id"/>
 *         &lt;element name="cmdty_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cmdty_xcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cmdty_fraction" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="cmdty_get_quotes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cmdty_quote_source" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cmdty_quote_tz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GncCommodityType {


    /**
     * Gets the value of the cmdtyGetQuotes property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCmdtyGetQuotes();

    /**
     * Sets the value of the cmdtyGetQuotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCmdtyGetQuotes(java.lang.String value);

    /**
     * Gets the value of the cmdtyId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCmdtyId();

    /**
     * Sets the value of the cmdtyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCmdtyId(java.lang.String value);

    /**
     * Gets the value of the cmdtyName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCmdtyName();

    /**
     * Sets the value of the cmdtyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCmdtyName(java.lang.String value);

    /**
     * Gets the value of the cmdtyQuoteTz property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCmdtyQuoteTz();

    /**
     * Sets the value of the cmdtyQuoteTz property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCmdtyQuoteTz(java.lang.String value);

    /**
     * Gets the value of the cmdtyXcode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCmdtyXcode();

    /**
     * Sets the value of the cmdtyXcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCmdtyXcode(java.lang.String value);

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getVersion();

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setVersion(java.lang.String value);

    /**
     * Gets the value of the cmdtyQuoteSource property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCmdtyQuoteSource();

    /**
     * Sets the value of the cmdtyQuoteSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCmdtyQuoteSource(java.lang.String value);

    /**
     * Gets the value of the cmdtySpace property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCmdtySpace();

    /**
     * Sets the value of the cmdtySpace property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCmdtySpace(java.lang.String value);

    /**
     * Gets the value of the cmdtyFraction property.
     * 
     */
    int getCmdtyFraction();

    /**
     * Sets the value of the cmdtyFraction property.
     * 
     */
    void setCmdtyFraction(int value);

}
