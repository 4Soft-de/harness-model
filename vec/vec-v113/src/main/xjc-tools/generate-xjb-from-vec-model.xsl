<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:uml="http://www.omg.org/spec/UML/20131001"
        xmlns:xmi="http://www.omg.org/spec/XMI/20131001"
        xmlns:Stereotypes="http://www.magicdraw.com/schemas/Stereotypes.xmi"
        xmlns:jxb="http://java.sun.com/xml/ns/jaxb"
        xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc"
        xmlns:xs="http://www.w3.org/2001/XMLSchema"
        xmlns:nav="http://www.4soft.de/xjc-plugins/navigations"
        exclude-result-prefixes="uml xmi Stereotypes" version="2.0">

    <xsl:output method="xml" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="idlookup" match="*" use="@xmi:id"/>

    <xsl:key name="parents" match="ownedAttribute[@xmi:type='uml:Property' and exists(@association) and @aggregation='composite']" use="@type"/>
    <xsl:key name="incoming-refs" match="ownedAttribute[@xmi:type='uml:Property' and exists(@association) and not(@aggregation='composite')]" use="@type"/>

    <xsl:variable name="VEC_VERSION">1.1.3</xsl:variable>

    <!-- Header -->
    <xsl:template match="/">
        <jxb:bindings node="/xs:schema" schemaLocation="{concat('vec_',$VEC_VERSION,'.xsd')}" version="2.1" extensionBindingPrefixes="xjc">
            <jxb:globalBindings>
                <xjc:simple/>
                <jxb:serializable uid="1"/>
            </jxb:globalBindings>
            <jxb:schemaBindings>
                <jxb:package name="com.foursoft.harness.vec.v113"/>
                <jxb:nameXmlTransform>
                    <jxb:typeName prefix="Vec"/>
                </jxb:nameXmlTransform>
            </jxb:schemaBindings>
            <xsl:apply-templates select="xmi:XMI/uml:Model/packagedElement[@name='VEC']//packagedElement[@xmi:type='uml:Class']"/>
        </jxb:bindings>
    </xsl:template>

    <xsl:template match="packagedElement[@xmi:type='uml:Class']">
        <xsl:variable name="outgoing-refs"
                select=".//ownedAttribute[@xmi:type='uml:Property' and exists(@association) and not(@aggregation='composite') and not(key('idlookup',@type)/@xmi:type='uml:Enumeration')]"/>
        <xsl:variable name="parents" select="key('parents',@xmi:id)/.."/>
        <xsl:if test="$parents or $outgoing-refs">
            <jxb:bindings>
                <xsl:attribute name="node">//xs:complexType[@name='<xsl:apply-templates select="."
                        mode="create-name"/>']
                </xsl:attribute>
                <xsl:apply-templates select="$parents" mode="create-parents"/>
                <xsl:apply-templates select="$outgoing-refs" mode="create-property"/>
            </jxb:bindings>
        </xsl:if>
    </xsl:template>

    <xsl:template match="packagedElement[@xmi:type='uml:Class']" mode="create-parents">
        <nav:parent>
            <xsl:attribute name="name">parent
                <xsl:apply-templates select="." mode="create-name"/>
            </xsl:attribute>
            <xsl:attribute name="schema-type">
                <xsl:apply-templates select="." mode="create-name"/>
            </xsl:attribute>
        </nav:parent>
    </xsl:template>

    <xsl:template match="ownedAttribute" mode="create-property">
        <xsl:variable name="type" select="key('idlookup',@type)"/>
        <xsl:if test="not($type/@xmi:type='uml:Enumeration')">

            <jxb:bindings>
                <xsl:attribute name="node">.//xs:element[@name='<xsl:apply-templates select="." mode="create-name"/>']
                </xsl:attribute>
                <nav:ext-reference>
                    <xsl:attribute name="schema-type">
                        <xsl:apply-templates select="$type" mode="create-name"/>
                    </xsl:attribute>
                    <xsl:attribute name="inverse">ref
                        <xsl:apply-templates select=".." mode="create-name"/>
                    </xsl:attribute>
                </nav:ext-reference>
            </jxb:bindings>

        </xsl:if>
    </xsl:template>

    <xsl:template match="*" mode="java-type-name">
        <xsl:if test="not(starts-with(@name,'Vec'))">Vec</xsl:if>
        <xsl:apply-templates select="." mode="create-name"/>
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
        <xsl:variable name="tempName" select="normalize-space(.)"/>
        <xsl:variable name="name">
            <!-- strip leading '/' -->
            <xsl:if test="substring($tempName,1,1) != '/'">
                <xsl:value-of select="translate(substring($tempName,1,1),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
            </xsl:if>
            <xsl:value-of select="substring($tempName,2,string-length($tempName))"/>
        </xsl:variable>
        <xsl:value-of select="$name"/>
    </xsl:template>

    <!-- Overriding default templates -->
    <xsl:template match="*"/>

    <xsl:template match="@*"/>


</xsl:stylesheet>