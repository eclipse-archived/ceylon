package com.redhat.ceylon.common.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

public class WordWrapTest {

    private class Helper {
        private StringWriter sw;
        private WordWrap wrap;
        private final int width;
        public Helper(int width) {
            this.width = width;
            clear();
        }
        
        public WordWrap getWrap() {
            return wrap;
        }
        
        public void clear() {
            sw = new StringWriter();
            wrap = new WordWrap(sw, width);
        }
        
        public String getLine(int lineNumber) {
            try {
                wrap.flush();
                BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
                try {
                    String result = reader.readLine();
                    for (int ii = 0; ii < lineNumber; ii++) {
                        result = reader.readLine();
                    }
                    return result;
                } finally {
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public int getNumLines() {
            try {
                wrap.flush();
                BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
                try {
                    int num = 0;
                    String result = reader.readLine();
                    while (result != null) {
                        num++;
                        result = reader.readLine();
                    }
                    return num;
                } finally {
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public void assertLines(String... expectedLines) {
            try {
                wrap.flush();
                BufferedReader reader = new BufferedReader(new StringReader(sw.toString()));
                try {
                    int num = 0;
                    String result = reader.readLine();
                    while (result != null) {
                        Assert.assertEquals("On line " + num +": ", expectedLines[num], result);
                        num++;
                        result = reader.readLine();
                    }
                    Assert.assertEquals("Unspected number of lines", expectedLines.length, num);
                } finally {
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private Helper helper = new Helper(10);
    
    @Test
    public void testLessThanWidth() {
        helper.clear();
        helper.getWrap().append("123 567");
        helper.assertLines("123 567");
    }
    
    @Test
    public void testLessThanWidth2() {
        helper.clear();
        helper.getWrap().append("123 567 9");
        helper.assertLines("123 567 9");
    }
    
    @Test
    public void testLessThanWidth3() {
        helper.clear();
        helper.getWrap().append("123 567 90");
        helper.assertLines("123 567", "90");    
    }
    
    @Test
    public void testMoreThanWidth() {
        helper.clear();
        helper.getWrap().append("123 567 9012");
        helper.assertLines("123 567", "9012");
    }
    
    @Test
    public void testMoreThanWidth2() {    
        helper.clear();
        helper.getWrap().append("123 567").append(" 9012");
        helper.assertLines("123 567",  "9012");
    }
    
    @Test
    public void testMoreThanWidth3() {
        helper.clear();
        helper.getWrap().append("123 567 ").append("9012");
        helper.assertLines("123 567",  "9012");
        
    }
    
    @Test
    public void testMoreThanWidth4() {
        helper.clear();
        helper.getWrap().append("123 567 91011 12");
        helper.assertLines("123 567", "91011 12");
    }
    
    @Test
    public void testIndent() {
        helper.clear();
        helper.getWrap().setIndent(2);
        helper.getWrap().append("123 567 91011 12");
        helper.assertLines("  123 567", "  91011 12");
    }
    
    @Test
    public void testFirstLineIndent() {
        helper.clear();
        helper.getWrap().setIndentFirstLine(1);
        helper.getWrap().setIndentRestLines(2);
        helper.getWrap().append("123 567 91011 12");
        helper.assertLines(" 123 567", "  91011 12");
    }
    
    @Test
    public void testCollapsingWhitespace() {
        helper.clear();
        helper.getWrap().append("123  67 91011 12");
        helper.assertLines("123 67", "91011 12");
    }
    
    @Test
    public void testTab() {
        helper.clear();
        helper.getWrap().addTabStop(4);
        helper.getWrap().addTabStop(8);
        helper.getWrap().append("1").tab().append("23").tab().append("4");
        helper.assertLines("1   23  4");
    }
    
}
