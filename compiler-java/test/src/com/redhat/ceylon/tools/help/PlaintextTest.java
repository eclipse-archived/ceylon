package com.redhat.ceylon.tools.help;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;

import junit.framework.Assert;

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
    public void test1() throws Exception {
        renderAndCompare("test.md", "test.txt");
    }
    
    @Test
    public void test2() throws Exception {
        renderAndCompare("test2.md", "test2.txt");
        
    }
    
}
