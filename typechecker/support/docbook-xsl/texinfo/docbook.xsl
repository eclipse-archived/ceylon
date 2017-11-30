<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                exclude-result-prefixes="exsl"
                version='1.0'>

<xsl:output method="text"
            encoding="ISO-8859-1"
            indent="no"/>

<xsl:include href="../VERSION"/>
<xsl:include href="param.xsl"/>
<xsl:include href="empty.xsl"/>
<xsl:include href="text.xsl"/>
<xsl:include href="inline.xsl"/>
<xsl:include href="block.xsl"/>
<xsl:include href="structure.xsl"/>
<xsl:include href="../lib/lib.xsl"/>
<xsl:include href="../common/l10n.xsl"/>
<xsl:include href="../common/common.xsl"/>
<xsl:include href="../common/titles.xsl"/>

<xsl:param name="stylesheet.result.type" select="'texinfo'"/>

<xsl:key name="id" match="*" use="@id"/>

<xsl:template match="/"><xsl:apply-templates/></xsl:template>

</xsl:stylesheet>
