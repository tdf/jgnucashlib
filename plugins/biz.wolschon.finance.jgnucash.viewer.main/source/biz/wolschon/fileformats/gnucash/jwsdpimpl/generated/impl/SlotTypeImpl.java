//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2008.09.28 um 04:25:46 CEST 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl;

public class SlotTypeImpl implements biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType, com.sun.xml.bind.JAXBObject, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallableObject, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializable, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.ValidatableObject
{

    protected biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType _SlotValue;
    protected java.lang.String _SlotKey;
    public final static java.lang.Class version = (biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.class);
    }

    public biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType getSlotValue() {
        return _SlotValue;
    }

    public void setSlotValue(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType value) {
        _SlotValue = value;
    }

    public java.lang.String getSlotKey() {
        return _SlotKey;
    }

    public void setSlotKey(java.lang.String value) {
        _SlotKey = value;
    }

    public biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingEventHandler createUnmarshaller(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingContext context) {
        return new biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.SlotTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "slot_key");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _SlotKey), "SlotKey");
        } catch (java.lang.Exception e) {
            biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "slot_value");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _SlotValue), "SlotValue");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _SlotValue), "SlotValue");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _SlotValue), "SlotValue");
        context.endElement();
    }

    public void serializeAttributes(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsr\u0000\'com.sun.msv.grammar.trex.ElementPatt"
+"ern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;"
+"xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndecl"
+"aredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.s"
+"un.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/dataty"
+"pe/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/String"
+"Pair;xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000#c"
+"om.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysVali"
+"dxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.su"
+"n.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit"
+"\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0015L\u0000\nwhiteSpacet\u0000.Lcom/sun"
+"/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org"
+"/2001/XMLSchemat\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.WhiteSp"
+"aceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd"
+".WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Ex"
+"pression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv"
+".util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0015L\u0000\fnamespaceURIq"
+"\u0000~\u0000\u0015xpq\u0000~\u0000\u0019q\u0000~\u0000\u0018sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003ex"
+"pq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u0007xq\u0000~\u0000\u0003q\u0000~\u0000\u0010psq\u0000~\u0000\u000bppsr\u0000\"com.sun.msv.d"
+"atatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0005QNamesr\u00005com.s"
+"un.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u001bq\u0000~\u0000\u001esq\u0000~\u0000\u001fq\u0000~\u0000(q\u0000~\u0000\u0018sr\u0000#com.sun.msv.grammar.SimpleName"
+"Class\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0015L\u0000\fnamespaceURIq\u0000~\u0000\u0015xr\u0000\u001dcom"
+".sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www."
+"w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expres"
+"sion$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u000f\u0001psq\u0000~\u0000,t\u0000\bslot"
+"_keyt\u0000\u0000sq\u0000~\u0000\u0006pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0006pp\u0000sq\u0000~\u0000!ppsr\u0000 com.sun.msv.gra"
+"mmar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\u0010psq\u0000~\u0000#q\u0000~\u0000\u0010psr\u00002com.sun.ms"
+"v.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~"
+"\u00003psr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000-q\u0000~\u0000"
+"2sq\u0000~\u0000,t\u0000Kbiz.wolschon.fileformats.gnucash.jwsdpimpl.generat"
+"ed.SlotType.SlotValueTypet\u0000+http://java.sun.com/jaxb/xjc/dum"
+"my-elementssq\u0000~\u0000!ppsq\u0000~\u0000#q\u0000~\u0000\u0010pq\u0000~\u0000%q\u0000~\u0000.q\u0000~\u00002sq\u0000~\u0000,t\u0000\nslot_"
+"valueq\u0000~\u00006sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L"
+"\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;"
+"xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003"
+"\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/E"
+"xpressionPool;xp\u0000\u0000\u0000\u0007\u0001pq\u0000~\u0000\u0005q\u0000~\u00008q\u0000~\u0000\nq\u0000~\u0000=q\u0000~\u0000:q\u0000~\u0000\"q\u0000~\u0000Fx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public static class SlotValueTypeImpl implements biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType, com.sun.xml.bind.JAXBObject, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallableObject, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializable, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.ValidatableObject
    {

        protected java.lang.String _Type;
        protected com.sun.xml.bind.util.ListImpl _Content;
        public final static java.lang.Class version = (biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.JAXBVersion.class);
        private static com.sun.msv.grammar.Grammar schemaFragment;

        private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
            return (biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType.class);
        }

        public java.lang.String getType() {
            return _Type;
        }

        public void setType(java.lang.String value) {
            _Type = value;
        }

        protected com.sun.xml.bind.util.ListImpl _getContent() {
            if (_Content == null) {
                _Content = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
            }
            return _Content;
        }

        public java.util.List getContent() {
            return _getContent();
        }

        public biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingEventHandler createUnmarshaller(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingContext context) {
            return new biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.SlotTypeImpl.SlotValueTypeImpl.Unmarshaller(context);
        }

        public void serializeBody(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializer context)
            throws org.xml.sax.SAXException
        {
            int idx2 = 0;
            final int len2 = ((_Content == null)? 0 :_Content.size());
            while (idx2 != len2) {
                {
                    java.lang.Object o = _Content.get(idx2);
                    if (o instanceof com.sun.xml.bind.JAXBObject) {
                        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Content.get(idx2 ++)), "Content");
                    } else {
                        if (o instanceof java.lang.String) {
                            try {
                                context.text(((java.lang.String) _Content.get(idx2 ++)), "Content");
                            } catch (java.lang.Exception e) {
                                biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.Util.handlePrintConversionException(this, e, context);
                            }
                        } else {
                            biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.Util.handleTypeMismatchError(context, this, "Content", o);
                        }
                    }
                }
            }
        }

        public void serializeAttributes(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializer context)
            throws org.xml.sax.SAXException
        {
            int idx2 = 0;
            final int len2 = ((_Content == null)? 0 :_Content.size());
            context.startAttribute("", "type");
            try {
                context.text(((java.lang.String) _Type), "Type");
            } catch (java.lang.Exception e) {
                biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
            while (idx2 != len2) {
                {
                    java.lang.Object o = _Content.get(idx2);
                    if (o instanceof com.sun.xml.bind.JAXBObject) {
                        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Content.get(idx2 ++)), "Content");
                    } else {
                        if (o instanceof java.lang.String) {
                            try {
                                idx2 += 1;
                            } catch (java.lang.Exception e) {
                                biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.Util.handlePrintConversionException(this, e, context);
                            }
                        } else {
                            biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.Util.handleTypeMismatchError(context, this, "Content", o);
                        }
                    }
                }
            }
        }

        public void serializeURIs(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializer context)
            throws org.xml.sax.SAXException
        {
            int idx2 = 0;
            final int len2 = ((_Content == null)? 0 :_Content.size());
            while (idx2 != len2) {
                {
                    java.lang.Object o = _Content.get(idx2);
                    if (o instanceof com.sun.xml.bind.JAXBObject) {
                        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Content.get(idx2 ++)), "Content");
                    } else {
                        if (o instanceof java.lang.String) {
                            try {
                                idx2 += 1;
                            } catch (java.lang.Exception e) {
                                biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.Util.handlePrintConversionException(this, e, context);
                            }
                        } else {
                            biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.Util.handleTypeMismatchError(context, this, "Content", o);
                        }
                    }
                }
            }
        }

        public java.lang.Class getPrimaryInterface() {
            return (biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotType.SlotValueType.class);
        }

        public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
            if (schemaFragment == null) {
                schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsr\u0000\u001ccom.sun.msv.grammar.MixedExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000"
+"~\u0000\u0003ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000"
+" com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0007sr\u0000\u0011java.l"
+"ang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\tq\u0000~\u0000\u000epsr\u0000\'com.sun.ms"
+"v.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/"
+"sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002"
+"xq\u0000~\u0000\u0003q\u0000~\u0000\u000ep\u0000sq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u000epsr\u0000 com.sun.msv.grammar.Att"
+"ributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u0011xq\u0000~\u0000\u0003q\u0000~\u0000\u000ep"
+"sr\u00002com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\r\u0001psr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com."
+"sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0003q\u0000~\u0000\u001apsr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\t"
+"localNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000!xq\u0000~\u0000\u001ct\u00009b"
+"iz.wolschon.fileformats.gnucash.jwsdpimpl.generated.Slott\u0000+h"
+"ttp://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0010q\u0000~\u0000\u000ep\u0000sq\u0000~\u0000"
+"\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u000epsq\u0000~\u0000\u0016q\u0000~\u0000\u000epq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000;biz.wolsc"
+"hon.fileformats.gnucash.jwsdpimpl.generated.TsDateq\u0000~\u0000$q\u0000~\u0000\u001f"
+"sq\u0000~\u0000\u0016ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLo"
+"rg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/su"
+"n/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000)com.sun.msv.datatype.xsd.E"
+"numerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com"
+".sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7"
+"\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun"
+"/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/"
+"msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000!xr\u0000\'com.sun.m"
+"sv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000"
+"!L\u0000\btypeNameq\u0000~\u0000!L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/W"
+"hiteSpaceProcessor;xpt\u0000\u0000psr\u00005com.sun.msv.datatype.xsd.WhiteS"
+"paceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xs"
+"d.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000$com.sun.msv.datatype"
+".xsd.NmtokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\"com.sun.msv.datatype.xsd.Toke"
+"nType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAt"
+"omicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00006t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0007NMTOK"
+"ENq\u0000~\u0000<\u0000q\u0000~\u0000Bt\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000x"
+"pw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0005t\u0000\u0004guidt\u0000\u0005framet\u0000\u0007integert\u0000\u0006stringt\u0000\btimespec"
+"xsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tloc"
+"alNameq\u0000~\u0000!L\u0000\fnamespaceURIq\u0000~\u0000!xpt\u0000\u000fNMTOKEN-derivedq\u0000~\u00009sq\u0000~"
+"\u0000 t\u0000\u0004typeq\u0000~\u00009sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedH"
+"ash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef"
+"\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/gramm"
+"ar/ExpressionPool;xp\u0000\u0000\u0000\t\u0001pq\u0000~\u0000\nq\u0000~\u0000\u000fq\u0000~\u0000\u0015q\u0000~\u0000\'q\u0000~\u0000\u0014q\u0000~\u0000&q\u0000~\u0000"
+"\u0005q\u0000~\u0000\fq\u0000~\u0000\bx"));
            }
            return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
        }

        public class Unmarshaller
            extends biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.AbstractUnmarshallingEventHandlerImpl
        {


            public Unmarshaller(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingContext context) {
                super(context, "----");
            }

            protected Unmarshaller(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingContext context, int startState) {
                this(context);
                state = startState;
            }

            public java.lang.Object owner() {
                return biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.SlotTypeImpl.SlotValueTypeImpl.this;
            }

            public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  3 :
                            if (("slot" == ___local)&&("" == ___uri)) {
                                _getContent().add(((biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.SlotImpl) spawnChildFromEnterElement((biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.SlotImpl.class), 3, ___uri, ___local, ___qname, __atts)));
                                return ;
                            }
                            if (("ts_date" == ___local)&&("" == ___uri)) {
                                _getContent().add(((biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.TsDateImpl) spawnChildFromEnterElement((biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.TsDateImpl.class), 3, ___uri, ___local, ___qname, __atts)));
                                return ;
                            }
                            revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "type");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                    }
                    super.enterElement(___uri, ___local, ___qname, __atts);
                    break;
                }
            }

            private void eatText1(final java.lang.String value)
                throws org.xml.sax.SAXException
            {
                try {
                    _Type = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
                } catch (java.lang.Exception e) {
                    handleParseConversionException(e);
                }
            }

            public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  3 :
                            revertToParentFromLeaveElement(___uri, ___local, ___qname);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "type");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                    }
                    super.leaveElement(___uri, ___local, ___qname);
                    break;
                }
            }

            public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  3 :
                            revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                            return ;
                        case  0 :
                            if (("type" == ___local)&&("" == ___uri)) {
                                state = 1;
                                return ;
                            }
                            break;
                    }
                    super.enterAttribute(___uri, ___local, ___qname);
                    break;
                }
            }

            public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  3 :
                            revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "type");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                        case  2 :
                            if (("type" == ___local)&&("" == ___uri)) {
                                state = 3;
                                return ;
                            }
                            break;
                    }
                    super.leaveAttribute(___uri, ___local, ___qname);
                    break;
                }
            }

            public void handleText(final java.lang.String value)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    try {
                        switch (state) {
                            case  1 :
                                eatText1(value);
                                state = 2;
                                return ;
                            case  3 :
                                eatText2(value);
                                state = 3;
                                return ;
                            case  0 :
                                attIdx = context.getAttribute("", "type");
                                if (attIdx >= 0) {
                                    final java.lang.String v = context.eatAttribute(attIdx);
                                    eatText1(v);
                                    state = 3;
                                    continue outer;
                                }
                                break;
                        }
                    } catch (java.lang.RuntimeException e) {
                        handleUnexpectedTextException(value, e);
                    }
                    break;
                }
            }

            private void eatText2(final java.lang.String value)
                throws org.xml.sax.SAXException
            {
                try {
                    _getContent().add(value);
                } catch (java.lang.Exception e) {
                    handleParseConversionException(e);
                }
            }

        }

    }

    public class Unmarshaller
        extends biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingContext context) {
            super(context, "-------");
        }

        protected Unmarshaller(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.SlotTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  4 :
                        attIdx = context.getAttribute("", "type");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("slot_key" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  6 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        if (("slot_value" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  5 :
                        if (("slot_value" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  4 :
                        attIdx = context.getAttribute("", "type");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  2 :
                        if (("slot_key" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  6 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  4 :
                        if (("type" == ___local)&&("" == ___uri)) {
                            _SlotValue = ((biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.SlotTypeImpl.SlotValueTypeImpl) spawnChildFromEnterAttribute((biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.SlotTypeImpl.SlotValueTypeImpl.class), 5, ___uri, ___local, ___qname));
                            return ;
                        }
                        break;
                    case  6 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  4 :
                        attIdx = context.getAttribute("", "type");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  6 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  4 :
                            attIdx = context.getAttribute("", "type");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                        case  6 :
                            revertToParentFromText(value);
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SlotKey = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
