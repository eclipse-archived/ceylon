package com.redhat.ceylon.tools.help;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.tautua.markdownpapers.ast.Document;
import org.tautua.markdownpapers.parser.Parser;

import com.redhat.ceylon.common.tool.WordWrap;

public class PlaintextTest {

    public void renderAndCompare(String mdFile, String txtFile) throws Exception {
        StringWriter sw = new StringWriter();
        try (InputStreamReader stream = new InputStreamReader(PlaintextTest.class.getResourceAsStream(mdFile))) {
            Parser parser = new Parser(stream);
            Document document = parser.parse();
            PlaintextMarkdownVisitor emitter = new PlaintextMarkdownVisitor(new WordWrap(sw));
            document.accept(emitter);
        }
        
        StringBuilder sb = new StringBuilder();
        
        try (BufferedReader r = new BufferedReader(new InputStreamReader(PlaintextTest.class.getResourceAsStream(txtFile)))){
            String line = r.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = r.readLine();
            }
        }
        
        Assert.assertEquals(sb.toString(), sw.toString());
    }
    
    @Test
    public void testHeadings() throws Exception {
        renderAndCompare("headings.md", "headings.txt");   
    }
    
    @Test
    public void testParagraphs() throws Exception {
        renderAndCompare("paragraphs.md", "paragraphs.txt");   
    }
    
    @Test
    public void testLists() throws Exception {
        renderAndCompare("lists.md", "lists.txt");   
    }
    
    @Test
    public void testBlockquotes() throws Exception {
        renderAndCompare("blockquotes.md", "blockquotes.txt");   
    }
    
    @Test
    public void testCodeblocks() throws Exception {
        renderAndCompare("codeblocks.md", "codeblocks.txt");   
    }
    
    @Ignore("M5")
    @Test
    public void testNesting_fail() throws Exception {
        // Unfortunately it look like markdownpapers doesn't correctly
        // handle nesting
        renderAndCompare("nesting.md", "nesting.txt");   
    }
    
    @Test
    public void testEmphasis() throws Exception {
        renderAndCompare("emphasis.md", "emphasis.txt");   
    }
    
    @Test
    public void testLinks() throws Exception {
        renderAndCompare("links.md", "links.txt");   
    }
    
}
