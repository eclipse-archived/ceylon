package com.redhat.ceylon.common.test;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.CeylonConfig;
import com.redhat.ceylon.common.ConfigParser;

public class CeylonConfigTest {

    @Before
    public void setup() {
        System.setProperty(ConfigParser.PROP_CEYLON_CONFIG_FILE, "test-src/com/redhat/ceylon/common/test/test.config");
    }
    
    @Test
    public void testIsOptionDefined() {
        Assert.assertFalse(CeylonConfig.isOptionDefined("foo.bar"));
        Assert.assertTrue(CeylonConfig.isOptionDefined("test.string-hello"));
    }
    
    @Test
    public void testStrings() {
        Assert.assertEquals(null, CeylonConfig.getOption("foo.bar"));
        Assert.assertEquals("test", CeylonConfig.getOption("foo.bar", "test"));
        Assert.assertEquals("hello", CeylonConfig.getOption("test.string-hello"));
        Assert.assertEquals("world", CeylonConfig.getOption("test.string-world"));
        Assert.assertEquals(" with spaces   ", CeylonConfig.getOption("test.string-spaces"));
        Assert.assertEquals("\"\\", CeylonConfig.getOption("test.string-escapes"));
        Assert.assertEquals("aap\nnoot\nmies", CeylonConfig.getOption("test.string-multiline"));
        Assert.assertEquals("aap\nnoot\nmies", CeylonConfig.getOption("test.string-multiline-with-spaces"));
        Assert.assertEquals("aap\nnoot\nmies", CeylonConfig.getOption("test.string-quoted-multiline"));
    }
    
    @Test
    public void testBooleans() {
        Assert.assertEquals(null, CeylonConfig.getBoolOption("foo.bar"));
        Assert.assertEquals(true, CeylonConfig.getBoolOption("foo.bar", true));
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.boolean-true"));
        Assert.assertEquals(Boolean.FALSE, CeylonConfig.getBoolOption("test.boolean-false-with-spaces"));
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.boolean-on"));
        Assert.assertEquals(Boolean.FALSE, CeylonConfig.getBoolOption("test.boolean-off"));
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.boolean-yes"));
        Assert.assertEquals(Boolean.FALSE, CeylonConfig.getBoolOption("test.boolean-no"));
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.boolean-1"));
        Assert.assertEquals(Boolean.FALSE, CeylonConfig.getBoolOption("test.boolean-0"));
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.boolean-implicit-true"));
    }
    
    @Test
    public void testNumbers() {
        Assert.assertEquals(null, CeylonConfig.getNumberOption("foo.bar"));
        Assert.assertEquals(1, CeylonConfig.getNumberOption("foo.bar", 1));
        Assert.assertEquals(Long.valueOf(42), CeylonConfig.getNumberOption("test.number"));
        Assert.assertEquals(Long.valueOf(123), CeylonConfig.getNumberOption("test.number-with-spaces"));
    }
    
    @Test
    public void testCommentedStrings() {
        Assert.assertEquals("hello", CeylonConfig.getOption("test.commented.string-hello"));
        Assert.assertEquals("world", CeylonConfig.getOption("test.commented.string-world"));
        Assert.assertEquals(" with spaces   ", CeylonConfig.getOption("test.commented.string-spaces"));
        Assert.assertEquals("\"\\", CeylonConfig.getOption("test.commented.string-escapes"));
        Assert.assertEquals("aap\nnoot\nmies", CeylonConfig.getOption("test.commented.string-multiline"));
        Assert.assertEquals("aap\nnoot\nmies", CeylonConfig.getOption("test.commented.string-multiline-with-spaces"));
        Assert.assertEquals("aap\nnoot\nmies", CeylonConfig.getOption("test.commented.string-quoted-multiline"));
    }
    
    @Test
    public void testCommentedBooleans() {
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.commented.boolean-true"));
        Assert.assertEquals(Boolean.FALSE, CeylonConfig.getBoolOption("test.commented.boolean-false-with-spaces"));
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.commented.boolean-on"));
        Assert.assertEquals(Boolean.FALSE, CeylonConfig.getBoolOption("test.commented.boolean-off"));
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.commented.boolean-yes"));
        Assert.assertEquals(Boolean.FALSE, CeylonConfig.getBoolOption("test.commented.boolean-no"));
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.commented.boolean-1"));
        Assert.assertEquals(Boolean.FALSE, CeylonConfig.getBoolOption("test.commented.boolean-0"));
        Assert.assertEquals(Boolean.TRUE, CeylonConfig.getBoolOption("test.commented.boolean-implicit-true"));
    }
    
    @Test
    public void testCommentedNumbers() {
        Assert.assertEquals(Long.valueOf(42), CeylonConfig.getNumberOption("test.commented.number"));
        Assert.assertEquals(Long.valueOf(123), CeylonConfig.getNumberOption("test.commented.number-with-spaces"));
    }
    
    @Test
    public void testMultiple() {
        Assert.assertEquals(null, CeylonConfig.getOptionValues("foo.bar"));
        Assert.assertTrue(compareStringArrays(new String[]{"aap", "noot", "mies"}, CeylonConfig.getOptionValues("test.multiple.strings")));
    }
    
    private boolean compareStringArrays(String[] one, String[] two) {
        if (one.length == two.length) {
            for (int i = 0; i < one.length; i++) {
                if (!one[i].equals(two[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
