//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2010.05.26 um 03:04:34 CEST 
//


package biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl;

public class BookElementsGncGncCustomerImpl
    extends biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.GncGncCustomerTypeImpl
    implements biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncCustomer, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallableObject, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializable, biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncCustomer.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "gnc_GncCustomer";
    }

    public biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingEventHandler createUnmarshaller(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.UnmarshallingContext context) {
        return new biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.BookElementsGncGncCustomerImpl.Unmarshaller(context);
    }

    public void serializeBody(biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "gnc_GncCustomer");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
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
        return (biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.BookElementsGncGncCustomer.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv."
+"grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000"
+"\fcontentModelt\u0000 Lcom/sun/msv/grammar/Expression;xr\u0000\u001ecom.sun."
+"msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Lj"
+"ava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0003xppp\u0000sr\u0000\u001fcom.sun.msv.gra"
+"mmar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.BinaryExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007pp"
+"sq\u0000~\u0000\u0000pp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bp"
+"psr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun."
+"msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004sr\u0000\u0011java.lan"
+"g.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.Attr"
+"ibuteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\"ps"
+"r\u00002com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000!\u0001psr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.s"
+"un.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004"
+"q\u0000~\u0000\'psr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tl"
+"ocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000.xq\u0000~\u0000)t\u0000Tbi"
+"z.wolschon.fileformats.gnucash.jwsdpimpl.generated.GncGncCus"
+"tomerType.CustGuidTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-"
+"elementssq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000"
+"~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.su"
+"n.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datat"
+"ype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype"
+".xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSD"
+"atatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000.L\u0000\btypeNameq\u0000~\u0000.L\u0000"
+"\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;"
+"xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv"
+".datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com"
+".sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000co"
+"m.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq"
+"\u0000~\u0000.L\u0000\fnamespaceURIq\u0000~\u0000.xpq\u0000~\u0000?q\u0000~\u0000>sq\u0000~\u0000-t\u0000\u0004typet\u0000)http://w"
+"ww.w3.org/2001/XMLSchema-instanceq\u0000~\u0000,sq\u0000~\u0000-t\u0000\tcust_guidt\u0000\u0000s"
+"q\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00004ppsr\u0000\'com.sun.msv.datatype.xsd.MinLeng"
+"thFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxr\u00009com.sun.msv.datatype.xsd.D"
+"ataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.da"
+"tatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012nee"
+"dValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDat"
+"atypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Concre"
+"teType;L\u0000\tfacetNameq\u0000~\u0000.xq\u0000~\u0000;q\u0000~\u0000Lpsr\u00005com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000A\u0000\u0000sr\u0000#com."
+"sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq"
+"\u0000~\u00009q\u0000~\u0000>t\u0000\u0006stringq\u0000~\u0000W\u0001q\u0000~\u0000Yt\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000Dsq\u0000~\u0000Et\u0000\u000es"
+"tring-derivedq\u0000~\u0000Lsq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t"
+"\u0000\tcust_nameq\u0000~\u0000Lsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00004q\u0000~\u0000\"pq\u0000~\u0000Yq\u0000~\u0000Dsq\u0000~\u0000"
+"Eq\u0000~\u0000Zq\u0000~\u0000>sq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\u0007cust_"
+"idq\u0000~\u0000Lsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001cppsq\u0000~\u0000\u001eq\u0000~\u0000\"psq\u0000~\u0000#q"
+"\u0000~\u0000\"pq\u0000~\u0000&q\u0000~\u0000*q\u0000~\u0000,sq\u0000~\u0000-t\u0000<biz.wolschon.fileformats.gnucas"
+"h.jwsdpimpl.generated.Addressq\u0000~\u00001sq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q"
+"\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\tcust_addrq\u0000~\u0000Lsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq"
+"\u0000~\u0000\u001cppsq\u0000~\u0000\u001eq\u0000~\u0000\"psq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u0000&q\u0000~\u0000*q\u0000~\u0000,sq\u0000~\u0000-q\u0000~\u0000qq\u0000~\u0000"
+"1sq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\rcust_shipaddrq\u0000"
+"~\u0000Lsq\u0000~\u0000\u001cppsq\u0000~\u0000\u0000q\u0000~\u0000\"p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000dsq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~"
+"\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\ncust_notesq\u0000~\u0000Lq\u0000~\u0000,sq\u0000~\u0000\u001cppsq\u0000~\u0000\u0000q\u0000~\u0000\""
+"p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001cppsq\u0000~\u0000\u001eq\u0000~\u0000\"psq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u0000&q\u0000~\u0000"
+"*q\u0000~\u0000,sq\u0000~\u0000-t\u0000Ubiz.wolschon.fileformats.gnucash.jwsdpimpl.ge"
+"nerated.GncGncCustomerType.CustTermsTypeq\u0000~\u00001sq\u0000~\u0000\u001cppsq\u0000~\u0000#q"
+"\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\ncust_termsq\u0000~\u0000Lq\u0000~\u0000,sq\u0000~\u0000\u0000pp\u0000sq"
+"\u0000~\u0000\u0007ppq\u0000~\u0000dsq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\u0010cust_"
+"taxincludedq\u0000~\u0000Lsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00004ppsr\u0000 com.sun.msv.dat"
+"atype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.xsd.Int"
+"egerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetsq\u0000~\u0000Sxq\u0000~\u00009q\u0000~\u0000>t\u0000\u0003in"
+"tq\u0000~\u0000Bsr\u0000*com.sun.msv.datatype.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimi"
+"tValuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000Qppq\u0000~\u0000B\u0000\u0001sr\u0000*com.sun.msv.da"
+"tatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u00a3ppq\u0000~\u0000B\u0000\u0000sr\u0000!co"
+"m.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u009fq\u0000~\u0000>t\u0000\u0004long"
+"q\u0000~\u0000Bsq\u0000~\u0000\u00a2ppq\u0000~\u0000B\u0000\u0001sq\u0000~\u0000\u00a6ppq\u0000~\u0000B\u0000\u0000sr\u0000$com.sun.msv.datatype."
+"xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u009fq\u0000~\u0000>t\u0000\u0007integerq\u0000~\u0000Bsr\u0000,com."
+"sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalex"
+"r\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstraintFac"
+"etT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000Rppq\u0000~\u0000B\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.Num"
+"berType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00009q\u0000~\u0000>t\u0000\u0007decimalq\u0000~\u0000Bq\u0000~\u0000\u00b4t\u0000\u000efraction"
+"Digits\u0000\u0000\u0000\u0000q\u0000~\u0000\u00aet\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J"
+"\u0000\u0005valuexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000\u00aet\u0000\fmaxI"
+"nclusivesq\u0000~\u0000\u00b8\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000\u00a9q\u0000~\u0000\u00b7sr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u0087"
+"8\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000\u00b9\u0080\u0000\u0000\u0000q\u0000~\u0000\u00a9q\u0000~\u0000\u00bbsq\u0000~\u0000\u00bd\u007f\u00ff\u00ff\u00ffq\u0000~\u0000Dsq\u0000~\u0000Eq\u0000~\u0000\u00a1q\u0000"
+"~\u0000>sq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\u000bcust_activeq\u0000"
+"~\u0000Lsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000dsq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,"
+"sq\u0000~\u0000-t\u0000\rcust_discountq\u0000~\u0000Lsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000dsq\u0000~\u0000\u001cppsq\u0000"
+"~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\u000bcust_creditq\u0000~\u0000Lsq\u0000~\u0000\u0000pp\u0000sq"
+"\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001cppsq\u0000~\u0000\u001eq\u0000~\u0000\"psq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u0000&q\u0000~\u0000*q\u0000~"
+"\u0000,sq\u0000~\u0000-t\u0000Xbiz.wolschon.fileformats.gnucash.jwsdpimpl.genera"
+"ted.GncGncCustomerType.CustCurrencyTypeq\u0000~\u00001sq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000"
+"~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\rcust_currencyq\u0000~\u0000Lsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000"
+"\u0007ppq\u0000~\u0000\u009dsq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\u000bcust_use"
+"-ttq\u0000~\u0000Lsq\u0000~\u0000\u001cppsq\u0000~\u0000\u0000q\u0000~\u0000\"p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001cppsq\u0000~\u0000\u001e"
+"q\u0000~\u0000\"psq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u0000&q\u0000~\u0000*q\u0000~\u0000,sq\u0000~\u0000-t\u0000Xbiz.wolschon.filef"
+"ormats.gnucash.jwsdpimpl.generated.GncGncCustomerType.CustTa"
+"xtableTypeq\u0000~\u00001sq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\rc"
+"ust_taxtableq\u0000~\u0000Lq\u0000~\u0000,sq\u0000~\u0000\u001cppsq\u0000~\u0000\u0000q\u0000~\u0000\"p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000"
+"sq\u0000~\u0000\u001cppsq\u0000~\u0000\u001eq\u0000~\u0000\"psq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u0000&q\u0000~\u0000*q\u0000~\u0000,sq\u0000~\u0000-t\u0000>biz."
+"wolschon.fileformats.gnucash.jwsdpimpl.generated.SlotsTypeq\u0000"
+"~\u00001sq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\ncust_slotsq\u0000~"
+"\u0000Lq\u0000~\u0000,sq\u0000~\u0000#ppq\u0000~\u0000dsq\u0000~\u0000-t\u0000\u0007versionq\u0000~\u0000Lsq\u0000~\u0000\u001cppsq\u0000~\u0000#q\u0000~\u0000\""
+"pq\u0000~\u00007q\u0000~\u0000Gq\u0000~\u0000,sq\u0000~\u0000-t\u0000\u000fgnc_GncCustomerq\u0000~\u0000Lsr\u0000\"com.sun.msv"
+".grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv"
+"/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar"
+".ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersio"
+"nL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000A\u0001pq\u0000~"
+"\u0000\u0012q\u0000~\u0000\u00e7q\u0000~\u0000\u00d4q\u0000~\u0000\u008cq\u0000~\u0000yq\u0000~\u0000mq\u0000~\u0000\u001dq\u0000~\u0000\u00f4q\u0000~\u0000\u0081q\u0000~\u0000\fq\u0000~\u0000\u000eq\u0000~\u0000\u00e5q\u0000~"
+"\u0000\u00d2q\u0000~\u0000\u008aq\u0000~\u0000wq\u0000~\u0000kq\u0000~\u0000\u001aq\u0000~\u0000\u00f2q\u0000~\u0000\tq\u0000~\u0000\nq\u0000~\u0000\u0016q\u0000~\u0000\u0010q\u0000~\u0000\u00ecq\u0000~\u0000\u00dfq\u0000~"
+"\u0000\u00d9q\u0000~\u0000\u00cdq\u0000~\u0000\u00c7q\u0000~\u0000\u00c1q\u0000~\u0000\u0097q\u0000~\u0000\u0091q\u0000~\u0000\u0084q\u0000~\u0000}q\u0000~\u0000rq\u0000~\u0000fq\u0000~\u0000^q\u0000~\u00002q\u0000~"
+"\u0000\u00f9q\u0000~\u0001\u0000q\u0000~\u0000\u00ccq\u0000~\u0000\u00c6q\u0000~\u0000\u0096q\u0000~\u0000\u0083q\u0000~\u0000cq\u0000~\u0000\rq\u0000~\u0000\u0015q\u0000~\u0000\u000bq\u0000~\u0000\u0017q\u0000~\u0000\u0013q\u0000~"
+"\u0000\u00deq\u0000~\u0000\u009cq\u0000~\u0000\u0011q\u0000~\u0000Nq\u0000~\u0000\u000fq\u0000~\u0000\u00f5q\u0000~\u0000\u00e8q\u0000~\u0000\u00d5q\u0000~\u0000\u008dq\u0000~\u0000zq\u0000~\u0000nq\u0000~\u0000 q\u0000~"
+"\u0000\u0018q\u0000~\u0000\u00e3q\u0000~\u0000\u0088q\u0000~\u0000\u00f0q\u0000~\u0000\u0014x"));
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
            return biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.BookElementsGncGncCustomerImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        attIdx = context.getAttribute("", "version");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("gnc_GncCustomer" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
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
                    case  1 :
                        attIdx = context.getAttribute("", "version");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("gnc_GncCustomer" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
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
                    case  1 :
                        if (("version" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.GncGncCustomerTypeImpl)biz.wolschon.fileformats.gnucash.jwsdpimpl.generated.impl.BookElementsGncGncCustomerImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  3 :
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
                    case  1 :
                        attIdx = context.getAttribute("", "version");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  3 :
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
                            attIdx = context.getAttribute("", "version");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                        case  3 :
                            revertToParentFromText(value);
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
