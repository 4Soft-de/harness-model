<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:uml="http://www.omg.org/spec/UML/20131001"
    xmlns:xmi="http://www.omg.org/spec/XMI/20131001"
    xmlns:Stereotypes="http://www.magicdraw.com/schemas/Stereotypes.xmi"
    xmlns:MagicDraw_Profile="http://www.omg.org/spec/UML/20131001/MagicDrawProfile"
    xmlns:jxb="https://jakarta.ee/xml/ns/jaxb"
    xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:nav="http://www.4soft.de/xjc-plugins/navigations"
    xmlns:inheritance="urn:jvnet.org:basicjaxb:xjc:inheritance"
    exclude-result-prefixes="uml xmi Stereotypes MagicDraw_Profile"
    version="3.0">
    
    <xsl:key name="idlookup" match="*" use="@xmi:id"/>
    
    <xsl:output method="xml" indent="yes"/>
    <xsl:strip-space elements="*"/>
    
    <xsl:param name="VEC_VERSION" required="yes"/>
    
    <xsl:template match="/">
        <jxb:bindings node="/xs:schema" schemaLocation="{concat('vec_',$VEC_VERSION,'.xsd')}" version="3.0" extensionBindingPrefixes="xjc">
            <xsl:apply-templates select="key('idlookup',//Stereotypes:StructuredPrimitive/@base_Class)"></xsl:apply-templates>        
        </jxb:bindings>
    </xsl:template>
    
    <xsl:template match="packagedElement">
        <jxb:bindings multiple="true" node="//xs:complexType[@name='{@name}']">
            <inheritance:implements>com.foursoft.harness.vec.common.StructuredPrimitive</inheritance:implements>
        </jxb:bindings>        
    </xsl:template>
    
</xsl:stylesheet>