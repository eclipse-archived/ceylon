<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl="http://exslt.org/common"
                exclude-result-prefixes="exsl"
                version='1.0'>

<xsl:template match="book">
  <xsl:param name="node" select="."/>
  <xsl:param name="title" select="./bookinfo/title"/>
  <xsl:param name="subtitle" select="./bookinfo/subtitle"/>
  <xsl:param name="version" select="./bookinfo/releaseinfo"/>
  <xsl:param name="author" select="concat(./bookinfo/author/firstname, ' ', ./bookinfo/author/surname)"/>
\input texinfo
@setfilename ceylon-spec.info
@settitle <xsl:value-of select="$title"/>
@dircategory Software development
@direntry
* Ceylon: (ceylon-spec).       The Ceylon Language Specification.
@end direntry
@codequotebacktick on

@copying
This is the Ceylon Language Specification, version <xsl:value-of select="$version"/>.

Copyright @copyright{} 2008--2015 <xsl:value-of select="$author"/> and contributors

@quotation
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
@end quotation
@end copying

@titlepage
@title <xsl:value-of select="$title"/>
@subtitle <xsl:value-of select="$subtitle"/>

@page
@vskip 0pt plus 1filll
@insertcopying
@end titlepage

@contents

@ifnottex
@node Top
@top <xsl:value-of select="$title"/>

This is the Ceylon Language Specification, version <xsl:value-of select="$version"/>.
@end ifnottex

@menu
* preface:: Welcome to Ceylon.<xsl:for-each select="/book/chapter"> <!-- needs to be on one line -->
    <xsl:variable name="chaptitle"><xsl:apply-templates select="." mode="title.markup"/></xsl:variable>
    <xsl:variable name="chapid" select="@id"/>
* <xsl:value-of select="$chapid"/>:: <xsl:value-of select="$chaptitle"/>
  </xsl:for-each>
* index:: Complete index.
@end menu

<xsl:apply-templates/>

@node index
@unnumbered Index

@printindex cp

@bye
</xsl:template>

<xsl:template match="chapter">
  <xsl:variable name="id" select="@id"/>
  <xsl:variable name="title"><xsl:apply-templates select="." mode="title.markup"/></xsl:variable>
@node <xsl:value-of select="$id"/>
@chapter <xsl:value-of select="$title"/>
@cindex <xsl:value-of select="$title"/>

@menu
  <xsl:for-each select="./section">
    <xsl:variable name="sectitle"><xsl:apply-templates select="." mode="title.markup"/></xsl:variable>
    <xsl:variable name="secid" select="@id"/>
* <xsl:value-of select="$secid"/>:: <xsl:value-of select="$sectitle"/>
  </xsl:for-each>
@end menu

  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="section">
  <xsl:variable name="depth" select="count(ancestor::section)+1"/>
  <xsl:variable name="id" select="@id"/>
  <xsl:variable name="title"><xsl:apply-templates select="." mode="title.markup"/></xsl:variable>
@node <xsl:value-of select="$id"/>
  <xsl:choose>
    <xsl:when test="$depth=1">
@section <xsl:value-of select="$title"/>
    </xsl:when>
    <xsl:when test="$depth=2">
@subsection <xsl:value-of select="$title"/>
    </xsl:when>
    <xsl:when test="$depth=3">
@subsubsection <xsl:value-of select="$title"/>
    </xsl:when>
  </xsl:choose>
@cindex <xsl:value-of select="$title"/>
  <xsl:text><!-- explicit blank line needed -->

  </xsl:text>

  <xsl:choose>
    <xsl:when test="count(./section) > 0">
      <!-- we have subsections, let's make a menu -->
@menu
      <xsl:for-each select="./section">
        <xsl:variable name="sectitle"><xsl:apply-templates select="." mode="title.markup"/></xsl:variable>
        <xsl:variable name="secid" select="@id"/>
* <xsl:value-of select="$secid"/>:: <xsl:value-of select="$sectitle"/>
      </xsl:for-each>
@end menu

      <xsl:apply-templates/>
    </xsl:when>
    <xsl:otherwise>
      <!-- no subsections, no menu -->
      <xsl:apply-templates/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="preface">
  <xsl:variable name="id" select="@id"/>
  <xsl:variable name="title">
    <xsl:apply-templates select="." mode="title.markup"/>
  </xsl:variable>
@node preface
@unnumbered Preface
@cindex Preface
<xsl:text> <!-- explicit blank line -->
  
</xsl:text>
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="@colwidth">
  <xsl:value-of select="substring-before(., '*')"/>
</xsl:template>

<xsl:template match="table/tgroup">
  <!--
      Okay, so about the column widths.
      The colspec/@colwidth attributes look like: 1.5* 7* 2.3* etc.
      To turn this into fractional widths, we would need to form their sums.
      The sum() function does that… but of course not with that asterisk at the end.
      Problem is, as soon as we do anything to remove the asterisk (for-each or something),
      it’s no longer a node set, it’s just a concatenated string "1.572.3".
      I have found no way to work around this, so we’re just hardcoding the widths
      of the two tables that occur in the Ceylon spec.
      Sorry.

<xsl:variable name="colwidths-ast" select="colspec/@colwidth"/>
<xsl:variable name="colwidths"><xsl:for-each select="$colwidths-ast"><xsl:value-of select="number(substring-before(., '*'))"/></xsl:for-each></xsl:variable>
@multitable <xsl:for-each select="$colwidths"><xsl:value-of select="concat(' ', $colwidths)"/></xsl:for-each>
  -->
  <xsl:choose>
    <xsl:when test="count(colspec) = 4">
@multitable {Comparison, containment, assignability, inheritance:} {@code{=}, @code{+=}, @code{-=}, @code{*=}, @code{/=}, @code{%=}, @code{&amp;=}, @code{|=}, @code{~=}, @code{&amp;&amp;=}, @code{||=}} {Binary (and ternary)} {Associativity}
    </xsl:when>
    <xsl:when test="count(colspec) = 6">
@multitable {@code{lhs[from:length]}} {lower spanned subrange} {@code{compose((Iterable&lt;T,N&gt; ts)=>[*ts], lhs.spread(X.method))}} {any subtype of @code{Anything[]?} whose intersections with @code{[]} and @code{[Nothing+]} are not @code{Nothing}} {@code{Y given Y satisfies Identifiable} where @code{X&amp;Y} is not @code{Nothing}} {@code{[T*](*P)} or @code{[T+](*P)}}
    </xsl:when>
    <xsl:otherwise>
@multitable @columnfractions <xsl:for-each select="tgroup/colspec"><xsl:value-of select="1 div count(../colspec)"/></xsl:for-each>
    </xsl:otherwise>
  </xsl:choose>
  <xsl:for-each select="thead/row">
@headitem
    <xsl:apply-templates select="entry[position()=1]"/>
    <xsl:for-each select="entry[position()>1]">
@tab <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:for-each>
  <xsl:for-each select="tbody/row">
@item
    <xsl:apply-templates select="entry[position()=1]"/>
    <xsl:for-each select="entry[position()>1]">
@tab <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:for-each>
@end multitable
</xsl:template>

<xsl:template match="table"><xsl:apply-templates select="tgroup"/></xsl:template>
<xsl:template match="entry"><xsl:apply-templates select="child::node()"/></xsl:template>

</xsl:stylesheet>
