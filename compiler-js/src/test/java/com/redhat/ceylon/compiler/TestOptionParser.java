package com.redhat.ceylon.compiler;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class TestOptionParser {

    @Test
    public void test1() {
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-verbose", "-compact", "-module", "f1.ceylon", "f2.ceylon"));
        Options o = Options.parse(args);
        Assert.assertTrue(o.isVerbose());
        Assert.assertFalse(o.isComment());
        Assert.assertFalse(o.isIndent());
        Assert.assertEquals(2, args.size());
        Assert.assertTrue(args.contains("f1.ceylon"));
        Assert.assertTrue(args.contains("f2.ceylon"));
        Assert.assertTrue(o.getRepos().contains("modules"));
        Assert.assertEquals("modules", o.getOutDir());
        Assert.assertEquals("source", o.getSrcDir());
    }

    @Test
    public void test2() {
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-rep", "r1", "-user", "username", "-pass", "passwd",
                "-rep", "http://r2/", "f3.ceylon", "-src", "/tmp", "-out", "/tmp"));
        Options o = Options.parse(args);
        Assert.assertTrue(o.isComment());
        Assert.assertTrue(o.isIndent());
        Assert.assertFalse(o.isVerbose());
        Assert.assertTrue(o.getRepos().contains("r1"));
        Assert.assertTrue(o.getRepos().contains("http://r2/"));
        Assert.assertFalse(o.getRepos().contains("modules"));
        Assert.assertEquals("username", o.getUser());
        Assert.assertEquals("passwd", o.getPass());
        Assert.assertEquals("/tmp", o.getSrcDir());
        Assert.assertEquals("/tmp", o.getOutDir());
    }

}
