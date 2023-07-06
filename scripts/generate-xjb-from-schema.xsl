<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:jxb="https://jakarta.ee/xml/ns/jaxb"
    xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc"
    xmlns:nav="http://www.4soft.de/xjc-plugins/navigations"
    xmlns:mt="http://www.prostep.org/ecad-if/2022/model-meta"
    xmlns:annox="http://jvnet.org/basicjaxb/xjc/annox"
    exclude-result-prefixes="mt"
    version="2.0">
    
    <xsl:output method="xml" indent="yes"/>
    <xsl:strip-space elements="*"/>
    
    <xsl:param name="VEC_VERSION" required="yes"/>
    <xsl:param name="PACKAGE" required="yes"/>
    
    <xsl:key name="parents" match="xs:element" use="replace(@type,'vec:','')"/>
    
    
    
    <xsl:template match="/">       
        <jxb:bindings node="/xs:schema" schemaLocation="{concat('vec_',$VEC_VERSION,'.xsd')}" version="3.0" extensionBindingPrefixes="xjc">
            <jxb:globalBindings>
                <xjc:simple/>
                <jxb:serializable uid="1"/>
            </jxb:globalBindings>
            <jxb:schemaBindings>
                <jxb:package name="{$PACKAGE}"/>
                <jxb:nameXmlTransform>
                    <jxb:typeName prefix="Vec"/>
                </jxb:nameXmlTransform>
            </jxb:schemaBindings>
            <xsl:apply-templates select="//xs:complexType"></xsl:apply-templates>           
        </jxb:bindings>        
    </xsl:template>
    
    <xsl:template match="xs:complexType">        
        <jxb:bindings node="//xs:complexType[@name='{@name}']">
            <xsl:if test="xs:annotation/xs:appinfo/mt:deprecated">
                <annox:annotate>@java.lang.Deprecated(forRemoval=true)</annox:annotate>
            </xsl:if>
            <xsl:for-each-group select="key('parents', @name)[.//mt:relationship[@relationship-type='Composition']]" group-by="ancestor::xs:complexType[1]">
                <xsl:apply-templates select="current-group()[1]" mode="parent"/>                    
            </xsl:for-each-group>
            <xsl:apply-templates select=".//xs:element"/>            
        </jxb:bindings>
    </xsl:template>
      
    <xsl:template match="xs:element">
        <jxb:bindings node=".//xs:element[@name='{@name}']">
            <xsl:if test="xs:annotation/xs:appinfo/mt:deprecated">
                <annox:annotate target="getter">@java.lang.Deprecated(forRemoval=true)</annox:annotate>
                <annox:annotate target="setter">@java.lang.Deprecated(forRemoval=true)</annox:annotate>
            </xsl:if>
            <xsl:apply-templates select=".[@type='xs:IDREFS' or @type='xs:IDREF']" mode="idref"></xsl:apply-templates>        
        </jxb:bindings>
    </xsl:template>
  
  
    <xsl:template match="xs:element" mode="idref">
            <nav:ext-reference schema-type="{.//mt:relationship/@element-type/replace(.,'vec:','')}" inverse="ref{ancestor::xs:complexType/@name}"/>
    </xsl:template>
    
    <xsl:template match="xs:element" mode="parent">
        <xsl:variable name="owningType" select="ancestor::xs:complexType[1]"/>
        <nav:parent name="parent{$owningType/@name}" schema-type="{$owningType/@name}"></nav:parent>
    </xsl:template>
   
    <xsl:template match="*"/>
    
</xsl:stylesheet>