package com.redhat.ceylon.ceylondoc.test;

import java.lang.reflect.Method;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.ceylondoc.Util;

public class UtilTest {

    private static Locale defaultLocale;
    
    // The BreakIterator stuff is locale-dependent. To make the tests work
    // in any locale we need to set it to something specific
    @BeforeClass
    public static void setLocale() {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }
    
    @AfterClass
    public static void restoreLocale() {
        Locale.setDefault(defaultLocale);
    }
    
    private void assertFirstLine(String expect, String text) throws Exception {
        Method m = Util.class.getDeclaredMethod("getFirstLine", String.class);
        m.setAccessible(true);
        Assert.assertEquals(expect, m.invoke(null, text));
    }
    
    @Test
    public void testFirstLine() throws Exception {
        assertFirstLine("Blah blah blah", "Blah blah blah");
        assertFirstLine("Blah blah blah.", "Blah blah blah. Foo bar baz.");
        assertFirstLine("Blah blah blah!", "Blah blah blah! Foo bar baz.");
        assertFirstLine("Blah blah e.g. blah!", "Blah blah e.g. blah! Foo bar baz.");
        assertFirstLine("Blah blah i.e. blah!", "Blah blah i.e. blah! Foo bar baz.");
        assertFirstLine("Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah", 
                        "Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah");
        assertFirstLine("Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blahh", 
                        "Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blahh");
        assertFirstLine("Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah…", 
                        "Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blahhh");
        assertFirstLine("Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah…", 
                        "Blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah");        
    }
    
}
