package com.redhat.ceylon.tools.new_;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.tools.new_.Environment;
import com.redhat.ceylon.tools.new_.Template;

public class TemplateTest {

    Environment env(String... keyVals) {
        if (keyVals.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Environment result = new Environment();
        for (int ii = 0; ii < keyVals.length; ii+=2) {
            result.put(keyVals[ii], keyVals[ii+1]);
        }
        return result;
    }
    
    @Test
    public void testSimple() {
        Template t = new Template("This is a test @[foo]\n@[bar]");
        Assert.assertEquals("This is a test FOO\nBAR", t.eval(env(
                "foo", "FOO", 
                "bar", "BAR")));
    }
    
    @Test
    public void testJustPlaceholder() {
        Template t = new Template("@[foo]");
        Assert.assertEquals("FOO", t.eval(env(
                "foo", "FOO")));
    }
    
    @Test
    public void testSimpleMissing() {
        Template t = new Template("This is a test @[foo]\n@[bar]");
        try {
            t.eval(env(
                    "foo", "FOO"));
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("No replacement for bar at line 2", e.getMessage());
        }
        t = new Template("This is a test @[foo]\n\r\n@[bar]");
        try {
            t.eval(env(
                    "foo", "FOO"));
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("No replacement for bar at line 3", e.getMessage());
        }
    }
}
