/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.tools.help;

import java.io.StringWriter;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.tautua.markdownpapers.HtmlEmitter;
import org.tautua.markdownpapers.ast.Document;
import org.tautua.markdownpapers.ast.Node;

import com.redhat.ceylon.tools.help.Markdown.Section;

public class MarkdownTest {

    private String html(Node sectionBody) {
        StringWriter sw = new StringWriter();
        HtmlEmitter emitter = new HtmlEmitter(sw);
        sectionBody.accept(emitter);
        return sw.toString();
    }
    
    @Test
    public void testSplit() {
        Document doc = Markdown.markdown("Some stuff\n" +
        		"\n" +
        		"# First `H1`\n" +
        		"\n" +
        		"A sentence\n" +
        		"\n" +
        		"## `H2` under first `H1`" +
        		"\n" +
                "A sentence\n" +
                "\n" +
                "# Second `H1`\n" +
                "\n" +
                "A sentence\n" +
                "\n" +
                "## `H2` under second `H1`" +
                "\n" +
                "A sentence\n" +
                "\n");
        List<Section> sections = Markdown.extractSections(doc);
        Assert.assertEquals(3, sections.size());
        Section section = sections.get(0);
        Assert.assertNull(section.getHeading());
        Document sectionBody = section.getDoc();
        Assert.assertEquals("<p>Some stuff</p>", html(sectionBody).trim());
        
        section = sections.get(1);
        Assert.assertEquals("<h1> First <code>H1</code></h1>", 
                html(section.getHeading()).trim());
        sectionBody = section.getDoc();
        Assert.assertEquals(""+
"<p>A sentence</p>\n"+
"\n"+
"<h2> <code>H2</code> under first <code>H1</code></h2>\n"+
"\n"+
"<p>A sentence</p>", html(sectionBody).trim());
        
        section = sections.get(2);
        Assert.assertEquals("<h1> Second <code>H1</code></h1>", 
                html(section.getHeading()).trim());
        sectionBody = section.getDoc();
        Assert.assertEquals(""+
"<p>A sentence</p>\n"+
"\n"+
"<h2> <code>H2</code> under second <code>H1</code></h2>\n"+
"\n"+
"<p>A sentence</p>", html(sectionBody).trim());
    }
    
    @Test
    public void testAdjustHeadings() {
        Document doc = Markdown.markdown("Some stuff\n" +
                "\n" +
                "# First `H1`\n" +
                "\n" +
                "A sentence\n" +
                "\n" +
                "## `H2` under first `H1`" +
                "\n" +
                "A sentence");
        Markdown.adjustHeadings(doc, 1);
        Assert.assertEquals("<p>Some stuff</p>\n"+
"\n"+
"<h2> First <code>H1</code></h2>\n"+
"\n"+
"<p>A sentence</p>\n"+
"\n"+
"<h3> <code>H2</code> under first <code>H1</code></h3>\n"+
"\n"+
"<p>A sentence</p>\n", html(doc));
    }
    
}
