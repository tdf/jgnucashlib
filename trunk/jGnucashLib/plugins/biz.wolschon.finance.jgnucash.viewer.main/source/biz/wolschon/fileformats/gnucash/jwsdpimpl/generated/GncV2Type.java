//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2010.06.07 um 08:10:31 CEST 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated;


/**
 * Java content class for anonymous complex type.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 28)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}gnc_count-data"/>
 *         &lt;element name="gnc_book">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="book_id">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="book_slots" type="{}slots_type" minOccurs="0"/>
 *                   &lt;element ref="{}gnc_count-data" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;group ref="{}book_elements" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
public interface GncV2Type {


    /**
     * Gets the value of the gncCountData property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncCountData}
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncCountDataType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncCountDataType getGncCountData();

    /**
     * Sets the value of the gncCountData property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncCountData}
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncCountDataType}
     */
    void setGncCountData(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncCountDataType value);

    /**
     * Gets the value of the gncBook property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type.GncBookType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type.GncBookType getGncBook();

    /**
     * Sets the value of the gncBook property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type.GncBookType}
     */
    void setGncBook(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type.GncBookType value);


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 32)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="book_id">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="book_slots" type="{}slots_type" minOccurs="0"/>
     *         &lt;element ref="{}gnc_count-data" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;group ref="{}book_elements" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface GncBookType {


        /**
         * Gets the value of the GncCountData property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the GncCountData property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGncCountData().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncCountData}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncCountDataType}
         * 
         */
        java.util.List getGncCountData();

        /**
         * Gets the value of the BookElements property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the BookElements property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBookElements().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncInvoice}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncBudget}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncAccount}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncEmployee}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncSchedxaction}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncTransaction}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncCustomer}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncVendor}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncBillTerm}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncCommodity}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncJob}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncTaxTable}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncTemplateTransactions}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncEntry}
         * {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncPricedb}
         * 
         */
        java.util.List getBookElements();

        /**
         * Gets the value of the bookId property.
         * 
         * @return
         *     possible object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type.GncBookType.BookIdType}
         */
        biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type.GncBookType.BookIdType getBookId();

        /**
         * Sets the value of the bookId property.
         * 
         * @param value
         *     allowed object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type.GncBookType.BookIdType}
         */
        void setBookId(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncV2Type.GncBookType.BookIdType value);

        /**
         * Gets the value of the bookSlots property.
         * 
         * @return
         *     possible object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType}
         */
        biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType getBookSlots();

        /**
         * Sets the value of the bookSlots property.
         * 
         * @param value
         *     allowed object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType}
         */
        void setBookSlots(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsType value);

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
         * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 35)
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
        public interface BookIdType {


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

}
