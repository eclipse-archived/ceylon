<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                exclude-result-prefixes="exsl"
                version='1.0'>

<xsl:template match="literal">@code{<xsl:apply-templates/>}</xsl:template>
<xsl:template match="emphasis">@emph{<xsl:value-of select="."/>}</xsl:template>
<xsl:template match="xref">@ref{<xsl:value-of select="@linkend"/>, ,<xsl:value-of select="key('id',@linkend)[1]/title"/>}</xsl:template>
<xsl:template match="filename">@file{<xsl:value-of select="."/>}</xsl:template>

</xsl:stylesheet>
