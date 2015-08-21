<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                exclude-result-prefixes="exsl"
                version='1.0'>

<xsl:template match="para">
  <xsl:apply-templates/>
  <xsl:text> <!-- explicit blank line -->

  </xsl:text>
</xsl:template>

<xsl:template match="itemizedlist">
  @itemize @bullet
  <xsl:apply-templates/>
  @end itemize
</xsl:template>

<xsl:template match="listitem">
  @item <xsl:apply-templates/>
</xsl:template>

<xsl:template match="comment">
@smallindentedblock
  <xsl:apply-templates/>
@end smallindentedblock
</xsl:template>

<xsl:template match="programlisting">
@example
<xsl:apply-templates/>
@end example
</xsl:template>

<xsl:template match="screen">
@quotation
 <xsl:apply-templates/>
@end quotation
</xsl:template>

<xsl:template match="synopsis">
@verbatim
<xsl:value-of select="."/>
@end verbatim
</xsl:template>

</xsl:stylesheet>
