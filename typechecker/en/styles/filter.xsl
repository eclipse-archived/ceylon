<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl = 
    "http://www.w3.org/1999/XSL/Transform" version="1.0">
    
    <!-- By default copy the whole document -->
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- Each filename is placed into index -->
    <xsl:template match="filename">
        <!-- Copy original element -->
        <xsl:copy-of select="."/>
        <!-- Create new index entry -->
        <indexterm>
            <primary>Files</primary>
            <secondary><xsl:value-of select="."/></secondary>
        </indexterm>
    </xsl:template>
    
    <!-- Each filename is placed into index -->
    <xsl:template match="screenshot">
        <!-- Copy original element -->
        <xsl:copy-of select="."/>
        <!-- Create new index entry -->
        <indexterm>
            <primary>Screenshot</primary>
            <secondary><xsl:value-of select="./screeninfo"/></secondary>
        </indexterm>
    </xsl:template>
    

</xsl:stylesheet>