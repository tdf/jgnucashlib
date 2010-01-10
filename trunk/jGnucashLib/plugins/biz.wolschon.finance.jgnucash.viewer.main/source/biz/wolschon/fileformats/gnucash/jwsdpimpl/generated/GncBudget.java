//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2010.01.10 um 03:17:24 CET 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated;


/**
 * Java content class for gnc_budget complex type.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 961)
 * <p>
 * <pre>
 * &lt;complexType name="gnc_budget">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bgt_id" type="{}gnc_idType"/>
 *         &lt;element name="bgt_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bgt_description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bgt_num-periods" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="bgt_recurrence">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="recurrence_mult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="recurrence_period_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="recurrence_start" type="{}recurrence_startType"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="bgt_slots" type="{}slots_type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GncBudget {


    /**
     * Gets the value of the bgtDescription property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getBgtDescription();

    /**
     * Sets the value of the bgtDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setBgtDescription(java.lang.String value);

    /**
     * Gets the value of the bgtSlots property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType getBgtSlots();

    /**
     * Sets the value of the bgtSlots property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType}
     */
    void setBgtSlots(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType value);

    /**
     * Gets the value of the bgtName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getBgtName();

    /**
     * Sets the value of the bgtName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setBgtName(java.lang.String value);

    /**
     * Gets the value of the bgtId property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncIdType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncIdType getBgtId();

    /**
     * Sets the value of the bgtId property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncIdType}
     */
    void setBgtId(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncIdType value);

    /**
     * Gets the value of the bgtNumPeriods property.
     * 
     */
    int getBgtNumPeriods();

    /**
     * Sets the value of the bgtNumPeriods property.
     * 
     */
    void setBgtNumPeriods(int value);

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
     * Gets the value of the bgtRecurrence property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncBudget.BgtRecurrenceType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncBudget.BgtRecurrenceType getBgtRecurrence();

    /**
     * Sets the value of the bgtRecurrence property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncBudget.BgtRecurrenceType}
     */
    void setBgtRecurrence(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncBudget.BgtRecurrenceType value);


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 978)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="recurrence_mult" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="recurrence_period_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="recurrence_start" type="{}recurrence_startType"/>
     *       &lt;/sequence>
     *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface BgtRecurrenceType {


        /**
         * Gets the value of the recurrenceMult property.
         * 
         */
        int getRecurrenceMult();

        /**
         * Sets the value of the recurrenceMult property.
         * 
         */
        void setRecurrenceMult(int value);

        /**
         * Gets the value of the recurrencePeriodType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getRecurrencePeriodType();

        /**
         * Sets the value of the recurrencePeriodType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setRecurrencePeriodType(java.lang.String value);

        /**
         * Gets the value of the recurrenceStart property.
         * 
         * @return
         *     possible object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.RecurrenceStartType}
         */
        biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.RecurrenceStartType getRecurrenceStart();

        /**
         * Sets the value of the recurrenceStart property.
         * 
         * @param value
         *     allowed object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.RecurrenceStartType}
         */
        void setRecurrenceStart(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.RecurrenceStartType value);

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

    }

}
