<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:uml="http://www.omg.org/spec/UML/20131001"
    xmlns:xmi="http://www.omg.org/spec/XMI/20131001"
    xmlns:Stereotypes="http://www.magicdraw.com/schemas/Stereotypes.xmi"
    xmlns:jxb="http://java.sun.com/xml/ns/jaxb"
    xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc"
    xmlns:inheritance="http://jaxb2-commons.dev.java.net/basic/inheritance"
    xmlns:ci="http://jaxb.dev.java.net/plugin/code-injector"
    xmlns:xsi="http://www.w3.org/2000/10/XMLSchema-instance" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema"    
    xmlns:nav="http://www.4soft.de/xjc-plugins/navigations"
    exclude-result-prefixes="uml xmi Stereotypes" version="2.0">

    <xsl:output method="xml" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="idlookup" match="*" use="@xmi:id"/>
    
    <xsl:key name="parents" match="ownedAttribute[@xmi:type='uml:Property' and exists(@association) and @aggregation='composite']" use="@type"/>
    <xsl:key name="incoming-refs" match="ownedAttribute[@xmi:type='uml:Property' and exists(@association) and not(@aggregation='composite')]" use="@type"/>


    <!-- Header -->
    <xsl:template match="/">
        <jxb:bindings node="/xs:schema" schemaLocation="KBL24_SR1.xsd" version="2.1"  extensionBindingPrefixes="xjc">
            <jxb:globalBindings>
                <xjc:simple/>
                <jxb:serializable uid="1" />
            </jxb:globalBindings>
            <jxb:schemaBindings>                   
                <jxb:package name="de.foursoft.harness.kbl.model.kbl24" />
                <jxb:nameXmlTransform> 
                    <jxb:typeName  prefix="Kbl"/>
                </jxb:nameXmlTransform>
            </jxb:schemaBindings>
            <xsl:apply-templates select="xmi:XMI/uml:Model/packagedElement[@name='KBL']//packagedElement[@xmi:type='uml:Class']" mode="selectors"/>	
            <xsl:apply-templates select="xmi:XMI/uml:Model/packagedElement[@name='KBL']//packagedElement[@xmi:type='uml:Class']"/>
        </jxb:bindings>
    </xsl:template> 
    
    <xsl:template match="packagedElement[@xmi:type='uml:Class' and @xmi:id=//Stereotypes:Selector/@base_Class]" mode="selectors">
        <nav:selector>
            <xsl:attribute name="name"><xsl:apply-templates select="." mode="create-java-name"/></xsl:attribute>
            <xsl:attribute name="implements">de.foursoft.harness.kbl.model.kbl24.visitor.Visitable</xsl:attribute>
        </nav:selector>
    </xsl:template>
       
    <xsl:template match="packagedElement[@xmi:type='uml:Class' and not(@xmi:id=//Stereotypes:Selector/@base_Class)]">
        <xsl:variable name="outgoing-refs"
            select=".//ownedAttribute[@xmi:type='uml:Property' and exists(@association) and not(@aggregation='composite') and not(key('idlookup',@type)/@xmi:type='uml:Enumeration')]"/>
        <xsl:variable name="parents" select="key('parents',@xmi:id)/.."/>
        <xsl:variable name="selectors" select="key('idlookup',generalization/@general)[@xmi:id=//Stereotypes:Selector/@base_Class]"/>
        <xsl:variable name="selector-parents" select="key('idlookup',generalization/@general)[@xmi:id=//Stereotypes:Selector/@base_Class]/key('parents',@xmi:id)/.."/>
        
        <xsl:if test="($parents or $outgoing-refs or $selectors or $selector-parents)">
            <jxb:bindings>
                <xsl:attribute name="node">//xs:complexType[@name='<xsl:apply-templates select="."
                        mode="create-name"/>']</xsl:attribute>
                
                <xsl:apply-templates select="$selectors" mode="create-selectors"/>
                <xsl:apply-templates select="$selector-parents" mode="create-parents"/>
                <xsl:apply-templates select="$parents" mode="create-parents"/>
                <xsl:apply-templates select="$outgoing-refs" mode="create-property"/>           
            </jxb:bindings>
        </xsl:if>
    </xsl:template>

    <xsl:template match="packagedElement[@xmi:type='uml:Class']" mode="create-selectors">
        <nav:selector>
            <xsl:attribute name="name"><xsl:apply-templates select="." mode="create-java-name"/></xsl:attribute>
        </nav:selector>
    </xsl:template>
        
    <xsl:template match="packagedElement[@xmi:type='uml:Class']" mode="create-parents">
        <nav:parent>
            <xsl:attribute name="name">parent<xsl:apply-templates select="." mode="create-java-name"/></xsl:attribute>
            <xsl:choose>
                <xsl:when test="@xmi:id = //Stereotypes:Selector/@base_Class">
                    <xsl:attribute name="selector">
                        <xsl:apply-templates select="." mode="create-java-name"/>
                    </xsl:attribute>                    
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="schema-type">
                        <xsl:apply-templates select="." mode="create-name"/>
                    </xsl:attribute>
                </xsl:otherwise>
            </xsl:choose>
        </nav:parent>
    </xsl:template>
    
    <xsl:template match="ownedAttribute" mode="create-property">
        <xsl:variable name="type" select="key('idlookup',@type)"/>
        <xsl:if test="not($type/@xmi:type='uml:Enumeration')">
            
        <jxb:bindings>
            <xsl:attribute name="node">.//xs:element[@name='<xsl:apply-templates select="." mode="create-name"/>']</xsl:attribute>
            <nav:ext-reference>
                <xsl:choose>
                    <xsl:when test="$type/@xmi:id=//Stereotypes:Selector/@base_Class"><xsl:attribute name="selector"><xsl:apply-templates select="$type" mode="create-java-name"/></xsl:attribute></xsl:when>
                    <xsl:otherwise><xsl:attribute name="schema-type"><xsl:apply-templates select="$type" mode="create-name"/></xsl:attribute></xsl:otherwise>
                </xsl:choose>
                <xsl:attribute name="inverse">ref<xsl:apply-templates select=".." mode="create-java-name"/> </xsl:attribute>               
            </nav:ext-reference>
        </jxb:bindings>
           
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="*" mode="java-type-name"><xsl:if test="not(starts-with(@name,'Vec'))">Vec</xsl:if><xsl:apply-templates select="." mode="create-name"/></xsl:template>
    
    <xsl:template match="*" mode="create-java-name">
        <xsl:apply-templates select="@name" mode="format-java-name"/>
    </xsl:template>
    
    <xsl:template match="@*" mode="format-java-name">
        <xsl:for-each select="tokenize(.,'_')"><xsl:value-of select="nav:first-upper(normalize-space())"/></xsl:for-each>        
    </xsl:template>
    
    <xsl:template match="ownedAttribute" mode="create-name">
        <xsl:choose>
            <xsl:when test="@name !=''">
                <xsl:apply-templates select="@name" mode="format-name"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="key('idlookup',@type)" mode="create-name"/>								
            </xsl:otherwise>
        </xsl:choose>                
    </xsl:template>
    
    <xsl:template match="*" mode="create-name">
        <xsl:apply-templates select="@name" mode="format-name"/>
    </xsl:template>
    
    <xsl:template match="@*" mode="format-name">
        <xsl:value-of select="nav:first-upper(normalize-space(.))"/>
    </xsl:template>
    
    <xsl:function name="nav:first-upper">
        <xsl:param name="value"/>
        <xsl:if test="substring($value,1,1) != '/'">
            <xsl:value-of select="translate(substring($value,1,1),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
        </xsl:if>
        <xsl:value-of select="substring($value,2,string-length($value))"/>       
    </xsl:function>
    
    <!-- Overriding default templates -->
    <xsl:template match="*"/>

    <xsl:template match="@*"/>


</xsl:stylesheet>