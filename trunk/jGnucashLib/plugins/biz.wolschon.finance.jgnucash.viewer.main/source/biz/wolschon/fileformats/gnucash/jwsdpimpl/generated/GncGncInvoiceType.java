//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2010.05.26 um 03:04:34 CEST 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated;


/**
 * Java content class for anonymous complex type.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 557)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="invoice_guid">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="invoice_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="invoice_owner">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}owner_type"/>
 *                   &lt;element ref="{}owner_id"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="invoice_opened">
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
 *         &lt;element name="invoice_posted" minOccurs="0">
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
 *         &lt;element name="invoice_notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invoice_billing_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invoice_active" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="invoice_posttxn" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="invoice_postlot" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="invoice_postacc" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="invoice_currency">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}cmdty_space"/>
 *                   &lt;element ref="{}cmdty_id"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="invoice_billto" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="owner_type">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="gncCustomer"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="owner_id">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GncGncInvoiceType {


    /**
     * Gets the value of the invoiceBillingId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getInvoiceBillingId();

    /**
     * Sets the value of the invoiceBillingId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setInvoiceBillingId(java.lang.String value);

    /**
     * Gets the value of the invoicePosted property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostedType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostedType getInvoicePosted();

    /**
     * Sets the value of the invoicePosted property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostedType}
     */
    void setInvoicePosted(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostedType value);

    /**
     * Gets the value of the invoiceOpened property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceOpenedType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceOpenedType getInvoiceOpened();

    /**
     * Sets the value of the invoiceOpened property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceOpenedType}
     */
    void setInvoiceOpened(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceOpenedType value);

    /**
     * Gets the value of the invoiceNotes property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getInvoiceNotes();

    /**
     * Sets the value of the invoiceNotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setInvoiceNotes(java.lang.String value);

    /**
     * Gets the value of the invoiceOwner property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceOwnerType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceOwnerType getInvoiceOwner();

    /**
     * Sets the value of the invoiceOwner property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceOwnerType}
     */
    void setInvoiceOwner(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceOwnerType value);

    /**
     * Gets the value of the invoiceActive property.
     * 
     */
    int getInvoiceActive();

    /**
     * Sets the value of the invoiceActive property.
     * 
     */
    void setInvoiceActive(int value);

    /**
     * Gets the value of the invoiceGuid property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceGuidType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceGuidType getInvoiceGuid();

    /**
     * Sets the value of the invoiceGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceGuidType}
     */
    void setInvoiceGuid(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceGuidType value);

    /**
     * Gets the value of the invoicePosttxn property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePosttxnType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePosttxnType getInvoicePosttxn();

    /**
     * Sets the value of the invoicePosttxn property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePosttxnType}
     */
    void setInvoicePosttxn(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePosttxnType value);

    /**
     * Gets the value of the invoicePostlot property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostlotType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostlotType getInvoicePostlot();

    /**
     * Sets the value of the invoicePostlot property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostlotType}
     */
    void setInvoicePostlot(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostlotType value);

    /**
     * Gets the value of the invoiceId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getInvoiceId();

    /**
     * Sets the value of the invoiceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setInvoiceId(java.lang.String value);

    /**
     * Gets the value of the invoiceBillto property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceBilltoType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceBilltoType getInvoiceBillto();

    /**
     * Sets the value of the invoiceBillto property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceBilltoType}
     */
    void setInvoiceBillto(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceBilltoType value);

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
     * Gets the value of the invoicePostacc property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostaccType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostaccType getInvoicePostacc();

    /**
     * Sets the value of the invoicePostacc property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostaccType}
     */
    void setInvoicePostacc(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoicePostaccType value);

    /**
     * Gets the value of the invoiceCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceCurrencyType}
     */
    biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceCurrencyType getInvoiceCurrency();

    /**
     * Sets the value of the invoiceCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceCurrencyType}
     */
    void setInvoiceCurrency(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceCurrencyType value);


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 633)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="owner_type">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="gncCustomer"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="owner_id">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface InvoiceBilltoType {


        /**
         * Gets the value of the ownerId property.
         * 
         * @return
         *     possible object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceBilltoType.OwnerIdType}
         */
        biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceBilltoType.OwnerIdType getOwnerId();

        /**
         * Sets the value of the ownerId property.
         * 
         * @param value
         *     allowed object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceBilltoType.OwnerIdType}
         */
        void setOwnerId(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncInvoiceType.InvoiceBilltoType.OwnerIdType value);

        /**
         * Gets the value of the ownerType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getOwnerType();

        /**
         * Sets the value of the ownerType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setOwnerType(java.lang.String value);


        /**
         * Java content class for anonymous complex type.
         * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 643)
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
        public interface OwnerIdType {


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


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 623)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{}cmdty_space"/>
     *         &lt;element ref="{}cmdty_id"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface InvoiceCurrencyType {


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

    }


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 560)
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
    public interface InvoiceGuidType {


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
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 579)
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
    public interface InvoiceOpenedType {


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
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 570)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{}owner_type"/>
     *         &lt;element ref="{}owner_id"/>
     *       &lt;/sequence>
     *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface InvoiceOwnerType {


        /**
         * Gets the value of the ownerId property.
         * 
         * @return
         *     possible object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.OwnerId}
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.OwnerIdType}
         */
        biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.OwnerIdType getOwnerId();

        /**
         * Sets the value of the ownerId property.
         * 
         * @param value
         *     allowed object is
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.OwnerId}
         *     {@link biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.OwnerIdType}
         */
        void setOwnerId(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.OwnerIdType value);

        /**
         * Gets the value of the ownerType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getOwnerType();

        /**
         * Sets the value of the ownerType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setOwnerType(java.lang.String value);

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


    /**
     * Java content class for anonymous complex type.
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 614)
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
    public interface InvoicePostaccType {


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
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 586)
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
    public interface InvoicePostedType {


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
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 605)
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
    public interface InvoicePostlotType {


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
     * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/home/fox/workspace/jGnucashLib-GPL/plugins/biz.wolschon.finance.jgnucash.viewer.main/source/gnucash.xsd line 596)
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
    public interface InvoicePosttxnType {


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
