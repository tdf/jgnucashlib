//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2010.05.26 um 03:04:34 CEST 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated;


/**
 * Java content class for anonymous complex type.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 659)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="entry_guid">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="entry_date">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}ts_date"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="entry_entered">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}ts_date"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="entry_description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entry_action" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Auftrag"/>
 *             &lt;enumeration value="Material"/>
 *             &lt;enumeration value="Stunden"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="entry_qty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entry_b-acct" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="entry_b-price" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entry_bill" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="entry_billable" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="entry_b-taxable" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="entry_b-taxincluded" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="entry_b-pay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entry_i-acct" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="entry_i-price" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entry_i-discount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entry_invoice">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="entry_i-disc-type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="entry_i-disc-how" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="entry_i-taxable" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="entry_i-taxincluded" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="entry_i-taxtable" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="entry_slots" type="{}slots_type" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GncGncEntryType {


    /**
     * Gets the value of the entryITaxtable property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryITaxtableType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryITaxtableType getEntryITaxtable();

    /**
     * Sets the value of the entryITaxtable property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryITaxtableType}
     */
    void setEntryITaxtable(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryITaxtableType value);

    /**
     * Gets the value of the entryBAcct property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryBAcctType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryBAcctType getEntryBAcct();

    /**
     * Sets the value of the entryBAcct property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryBAcctType}
     */
    void setEntryBAcct(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryBAcctType value);

    /**
     * Gets the value of the entryBill property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryBillType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryBillType getEntryBill();

    /**
     * Sets the value of the entryBill property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryBillType}
     */
    void setEntryBill(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryBillType value);

    /**
     * Gets the value of the entryBPrice property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryBPrice();

    /**
     * Sets the value of the entryBPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryBPrice(java.lang.String value);

    /**
     * Gets the value of the entryIDiscType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryIDiscType();

    /**
     * Sets the value of the entryIDiscType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryIDiscType(java.lang.String value);

    /**
     * Gets the value of the entryIDiscount property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryIDiscount();

    /**
     * Sets the value of the entryIDiscount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryIDiscount(java.lang.String value);

    /**
     * Gets the value of the entryBTaxable property.
     * 
     */
    int getEntryBTaxable();

    /**
     * Sets the value of the entryBTaxable property.
     * 
     */
    void setEntryBTaxable(int value);

    /**
     * Gets the value of the entrySlots property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType getEntrySlots();

    /**
     * Sets the value of the entrySlots property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType}
     */
    void setEntrySlots(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType value);

    /**
     * Gets the value of the entryDate property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryDateType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryDateType getEntryDate();

    /**
     * Sets the value of the entryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryDateType}
     */
    void setEntryDate(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryDateType value);

    /**
     * Gets the value of the entryBillable property.
     * 
     */
    int getEntryBillable();

    /**
     * Sets the value of the entryBillable property.
     * 
     */
    void setEntryBillable(int value);

    /**
     * Gets the value of the entryBPay property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryBPay();

    /**
     * Sets the value of the entryBPay property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryBPay(java.lang.String value);

    /**
     * Gets the value of the entryIDiscHow property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryIDiscHow();

    /**
     * Sets the value of the entryIDiscHow property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryIDiscHow(java.lang.String value);

    /**
     * Gets the value of the entryQty property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryQty();

    /**
     * Sets the value of the entryQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryQty(java.lang.String value);

    /**
     * Gets the value of the entryBTaxincluded property.
     * 
     */
    int getEntryBTaxincluded();

    /**
     * Sets the value of the entryBTaxincluded property.
     * 
     */
    void setEntryBTaxincluded(int value);

    /**
     * Gets the value of the entryIPrice property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryIPrice();

    /**
     * Sets the value of the entryIPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryIPrice(java.lang.String value);

    /**
     * Gets the value of the entryEntered property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryEnteredType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryEnteredType getEntryEntered();

    /**
     * Sets the value of the entryEntered property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryEnteredType}
     */
    void setEntryEntered(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryEnteredType value);

    /**
     * Gets the value of the entryGuid property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryGuidType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryGuidType getEntryGuid();

    /**
     * Sets the value of the entryGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryGuidType}
     */
    void setEntryGuid(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryGuidType value);

    /**
     * Gets the value of the entryInvoice property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryInvoiceType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryInvoiceType getEntryInvoice();

    /**
     * Sets the value of the entryInvoice property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryInvoiceType}
     */
    void setEntryInvoice(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryInvoiceType value);

    /**
     * Gets the value of the entryDescription property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryDescription();

    /**
     * Sets the value of the entryDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryDescription(java.lang.String value);

    /**
     * Gets the value of the entryAction property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEntryAction();

    /**
     * Sets the value of the entryAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEntryAction(java.lang.String value);

    /**
     * Gets the value of the entryITaxincluded property.
     * 
     */
    int getEntryITaxincluded();

    /**
     * Sets the value of the entryITaxincluded property.
     * 
     */
    void setEntryITaxincluded(int value);

    /**
     * Gets the value of the entryITaxable property.
     * 
     */
    int getEntryITaxable();

    /**
     * Sets the value of the entryITaxable property.
     * 
     */
    void setEntryITaxable(int value);

    /**
     * Gets the value of the entryIAcct property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryIAcctType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryIAcctType getEntryIAcct();

    /**
     * Sets the value of the entryIAcct property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryIAcctType}
     */
    void setEntryIAcct(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncEntryType.EntryIAcctType value);

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
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 696)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EntryBAcctType {


        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getValue();

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setValue(java.lang.String value);

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

    }


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 706)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EntryBillType {


        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getValue();

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setValue(java.lang.String value);

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

    }


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 671)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{}ts_date"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EntryDateType {


        /**
         * Gets the value of the tsDate property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getTsDate();

        /**
         * Sets the value of the tsDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setTsDate(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 678)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{}ts_date"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EntryEnteredType {


        /**
         * Gets the value of the tsDate property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getTsDate();

        /**
         * Sets the value of the tsDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setTsDate(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 662)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EntryGuidType {


        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getValue();

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setValue(java.lang.String value);

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

    }


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 719)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EntryIAcctType {


        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getValue();

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setValue(java.lang.String value);

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

    }


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 743)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EntryITaxtableType {


        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getValue();

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setValue(java.lang.String value);

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

    }


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 730)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EntryInvoiceType {


        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getValue();

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setValue(java.lang.String value);

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

    }

}
