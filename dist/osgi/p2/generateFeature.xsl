<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="featureId"/>
	<xsl:param name="featureVersion"/>
	<xsl:template match="/">
<feature
      id="{$featureId}"
      label="Ceylon Distribution Feature"
      version="{$featureVersion}">
      <xsl:for-each select="/*[local-name()='repository']/*[local-name()='resource']">
		<xsl:variable name="bundleName" select="./*[(local-name()='capability') and (@namespace='osgi.identity')]/*[(local-name()='attribute') and (@name='osgi.identity')]/@value"/>      
			<xsl:if test="$bundleName != 'org.slf4j.simple'">
   <plugin
         id="{$bundleName}"
         download-size="{./*[(local-name()='capability') and (@namespace='osgi.content')]/*[(local-name()='attribute') and (@name='size')]/@value}"
         install-size="{./*[(local-name()='capability') and (@namespace='osgi.content')]/*[(local-name()='attribute') and (@name='size')]/@value}"
         version="{./*[(local-name()='capability') and (@namespace='osgi.identity')]/*[(local-name()='attribute') and (@name='version')]/@value}"/>
			</xsl:if>
      </xsl:for-each>
</feature>
		<!-- TODO: Auto-generated template -->
	</xsl:template>
</xsl:stylesheet>