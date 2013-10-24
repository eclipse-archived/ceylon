package com.redhat.ceylon.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.Constants;

public class TestOptionParser {

    @Test
    public void test1() {
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-verbose", "-compact", "-nomodule", "f1.ceylon", "f2.ceylon"));
        Options o = Options.parse(args);
        Assert.assertTrue(o.isVerbose());
        Assert.assertFalse(o.isComment());
        Assert.assertFalse(o.isIndent());
        Assert.assertEquals(2, args.size());
        Assert.assertTrue(args.contains("f1.ceylon"));
        Assert.assertTrue(args.contains("f2.ceylon"));
        Assert.assertTrue(o.getRepos().isEmpty());
        Assert.assertEquals(Constants.DEFAULT_MODULE_DIR, o.getOutDir());
        Assert.assertEquals(Constants.DEFAULT_SOURCE_DIR, o.getSrcDirs().get(0));
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
        Assert.assertEquals("/tmp", o.getSrcDirs().get(0));
        Assert.assertEquals("/tmp", o.getOutDir());
    }

    @Test
    public void testRepos() {
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-rep", "r1", "-rep", "r2", "-rep", "r3", "other"));
        List<String> repos = Options.findRepos(args, true);
        Assert.assertEquals(3, repos.size());
        Assert.assertEquals(1, args.size());
        Assert.assertTrue(args.contains("other"));
        Assert.assertTrue(repos.contains("r1"));
        Assert.assertTrue(repos.contains("r2"));
        Assert.assertTrue(repos.contains("r3"));
    }

    @Test
    public void testSourceDirs() {
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-src", "/a", "-src", "/b",
                "-src", "/c" + File.pathSeparator + "/d"));
        Options o = Options.parse(args);
        Assert.assertEquals(4, o.getSrcDirs().size());
        Assert.assertEquals("/a", o.getSrcDirs().get(0));
        Assert.assertEquals("/b", o.getSrcDirs().get(1));
        Assert.assertEquals("/c", o.getSrcDirs().get(2));
        Assert.assertEquals("/d", o.getSrcDirs().get(3));
    }
}
