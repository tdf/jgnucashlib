//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2008.09.28 um 04:25:46 CEST 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated;


/**
 * Java content class for anonymous complex type.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 811)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="slot_key" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="slot_value">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element ref="{}slot"/>
 *                   &lt;element ref="{}ts_date"/>
 *                 &lt;/choice>
 *                 &lt;attribute name="type" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                       &lt;enumeration value="frame"/>
 *                       &lt;enumeration value="guid"/>
 *                       &lt;enumeration value="integer"/>
 *                       &lt;enumeration value="string"/>
 *                       &lt;enumeration value="timespec"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface SlotType {


    /**
     * Gets the value of the slotValue property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType getSlotValue();

    /**
     * Sets the value of the slotValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType}
     */
    void setSlotValue(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType value);

    /**
     * Gets the value of the slotKey property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSlotKey();

    /**
     * Sets the value of the slotKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSlotKey(java.lang.String value);


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 815)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice maxOccurs="unbounded" minOccurs="0">
     *         &lt;element ref="{}slot"/>
     *         &lt;element ref="{}ts_date"/>
     *       &lt;/choice>
     *       &lt;attribute name="type" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
     *             &lt;enumeration value="frame"/>
     *             &lt;enumeration value="guid"/>
     *             &lt;enumeration value="integer"/>
     *             &lt;enumeration value="string"/>
     *             &lt;enumeration value="timespec"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface SlotValueType {


        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getType();

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setType(java.lang.String value);

        /**
         * Gets the value of the Content property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the Content property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContent().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link java.lang.String}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.TsDate}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.Slot}
         * 
         */
        java.util.List getContent();

    }

}
