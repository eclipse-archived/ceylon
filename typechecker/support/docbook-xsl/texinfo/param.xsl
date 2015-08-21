<?xml version="1.0" encoding="utf-8"?>
<!-- This file contains a bunch of params and templates that we don't care about, but the runtime needs. -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:src="http://nwalsh.com/xmlns/litprog/fragment" exclude-result-prefixes="src" version="1.0">

<xsl:template name="is.graphic.format"><xsl:param name="format"/>0</xsl:template>
<xsl:template name="is.graphic.extension"><xsl:param name="format"/>0</xsl:template>
<xsl:template name="write.chunk"/>
<xsl:template name="href.target"/>
<xsl:template name="xref.xreflabel"/>

<xsl:param name="appendix.autolabel" select="1"/>
<xsl:param name="author.othername.in.middle" select="1"/>
<xsl:param name="chapter.autolabel" select="1"/>
<xsl:param name="collect.xref.targets" select="'no'"/>
<xsl:param name="formal.procedures" select="1"/>
<xsl:param name="graphic.default.extension"/>
<xsl:param name="l10n.gentext.default.language" select="'en'"/>
<xsl:param name="l10n.gentext.language" select="''"/>
<xsl:param name="l10n.gentext.use.xref.language" select="0"/>
<xsl:param name="olink.base.uri" select="''"/>
<xsl:param name="part.autolabel" select="1"/>
<xsl:param name="preface.autolabel" select="0"/>
<xsl:param name="preferred.mediaobject.role"/>
<xsl:param name="punct.honorific" select="'.'"/>
<xsl:param name="qanda.defaultlabel">number</xsl:param>
<xsl:param name="qanda.inherit.numeration" select="1"/>
<xsl:param name="qandadiv.autolabel" select="1"/>
<xsl:param name="section.autolabel" select="0"/>
<xsl:param name="section.label.includes.component.label" select="0"/>
<xsl:param name="targets.filename" select="'target.db'"/>
<xsl:param name="tex.math.in.alt" select="''"/>
<xsl:param name="use.role.for.mediaobject" select="1"/>
<xsl:param name="use.svg" select="1"/>
<xsl:param name="xref.with.number.and.title" select="1"/>
<xsl:param name="xref.label-title.separator">: </xsl:param>
<xsl:param name="xref.label-page.separator"><xsl:text> </xsl:text></xsl:param>
<xsl:param name="xref.title-page.separator"><xsl:text> </xsl:text></xsl:param>
<xsl:param name="insert.xref.page.number">no</xsl:param>
</xsl:stylesheet>
