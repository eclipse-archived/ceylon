<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                exclude-result-prefixes="exsl"
                version='1.0'>

<xsl:template name="escape">
  <xsl:param name="text" select="."/>
  <xsl:variable name="text-escaped-at">
    <xsl:call-template name="string-replace-all">
      <xsl:with-param name="text" select="$text"/>
      <xsl:with-param name="replace" select="'@'"/>
      <xsl:with-param name="by" select="'@@'"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:variable name="text-escaped-lbrace">
    <xsl:call-template name="string-replace-all">
      <xsl:with-param name="text" select="$text-escaped-at"/>
      <xsl:with-param name="replace" select="'{'"/>
      <xsl:with-param name="by" select="'@{'"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:variable name="text-escaped-rbrace">
    <xsl:call-template name="string-replace-all">
      <xsl:with-param name="text" select="$text-escaped-lbrace"/>
      <xsl:with-param name="replace" select="'}'"/>
      <xsl:with-param name="by" select="'@}'"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:value-of select="$text-escaped-rbrace"/>
</xsl:template>

<xsl:template name="fix-entities">
  <xsl:param name="text" select="."/>
  <xsl:variable name="text-fixed-mdash">
    <xsl:call-template name="string-replace-all">
      <xsl:with-param name="text" select="$text"/>
      <xsl:with-param name="replace" select="'&#8212;'"/>
      <xsl:with-param name="by" select="'---'"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:variable name="text-fixed-lquot">
    <xsl:call-template name="string-replace-all">
      <xsl:with-param name="text" select="$text-fixed-mdash"/>
      <xsl:with-param name="replace" select="'&#8220;'"/>
      <xsl:with-param name="by" select="'@quotedblleft{}'"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:variable name="text-fixed-rquot">
    <xsl:call-template name="string-replace-all">
      <xsl:with-param name="text" select="$text-fixed-lquot"/>
      <xsl:with-param name="replace" select="'&#8221;'"/>
      <xsl:with-param name="by" select="'@quotedblright{}'"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:value-of select="$text-fixed-rquot"/>
</xsl:template>

<xsl:template match="text()">
  <xsl:variable name="text" select="."/>
  <xsl:variable name="text-escaped">
    <xsl:call-template name="escape">
      <xsl:with-param name="text" select="$text"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:variable name="text-fixed">
    <xsl:call-template name="fix-entities">
      <xsl:with-param name="text" select="$text-escaped"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:value-of select="$text-fixed"/>
</xsl:template>

<!-- source: https://stackoverflow.com/a/3067130/1420237 -->
<xsl:template name="string-replace-all">
  <xsl:param name="text" />
  <xsl:param name="replace" />
  <xsl:param name="by" />
  <xsl:choose>
    <xsl:when test="contains($text, $replace)">
      <xsl:value-of select="substring-before($text,$replace)" />
      <xsl:value-of select="$by" />
      <xsl:call-template name="string-replace-all">
        <xsl:with-param name="text"
                        select="substring-after($text,$replace)" />
        <xsl:with-param name="replace" select="$replace" />
        <xsl:with-param name="by" select="$by" />
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$text" />
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>
