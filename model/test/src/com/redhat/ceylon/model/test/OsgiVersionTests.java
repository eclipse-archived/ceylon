package com.redhat.ceylon.model.test;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.model.loader.OsgiVersion;

public class OsgiVersionTests {

    @Test
    public void testVersionConversions() {
        Assert.assertEquals("0.0.0.5", OsgiVersion.fromCeylonVersion("", false));
        Assert.assertEquals("1.0.0.3-n002-5", OsgiVersion.fromCeylonVersion("1-rc2", false));
        Assert.assertEquals("1.0.0.5", OsgiVersion.fromCeylonVersion("1", false));
        Assert.assertEquals("1.2.0.2", OsgiVersion.fromCeylonVersion("1.2.m", false));
        Assert.assertEquals("1.2.0.5", OsgiVersion.fromCeylonVersion("1.2", false));
        Assert.assertEquals("1.2.0.6-abc", OsgiVersion.fromCeylonVersion("1.2.abc", false));
        Assert.assertEquals("1.2.0.6-abc-n003-n004-5", OsgiVersion.fromCeylonVersion("1.2.abc.3.4", false));
        Assert.assertEquals("1.2.0.5-n003-n004-5", OsgiVersion.fromCeylonVersion("1.2.ga.3.4", false));
        Assert.assertEquals("1.2.3.n004-5", OsgiVersion.fromCeylonVersion("1.2.3.4", false));
        Assert.assertEquals("2.0.0.5", OsgiVersion.fromCeylonVersion("2", false));
        Assert.assertEquals("2.0.0.5", OsgiVersion.fromCeylonVersion("2-ga", false));
        Assert.assertEquals("2.0.0.5", OsgiVersion.fromCeylonVersion("2-final", false));
        Assert.assertEquals("3.0.0.3-n002-5", OsgiVersion.fromCeylonVersion("3cr2", false));
        Assert.assertEquals("3.0.0.3-n002-5", OsgiVersion.fromCeylonVersion("3.rc-2", false));
    }

    @Test
    public void testVersionOrdering() {
        Assert.assertTrue(OsgiVersion.fromCeylonVersion("", false).compareTo(OsgiVersion.fromCeylonVersion("1-rc2", false)) < 0);
        Assert.assertTrue(OsgiVersion.fromCeylonVersion("1-rc2", false).compareTo(OsgiVersion.fromCeylonVersion("1", false)) < 0);
        Assert.assertTrue(OsgiVersion.fromCeylonVersion("1", false).compareTo(OsgiVersion.fromCeylonVersion("1.2.m", false)) < 0);
        Assert.assertTrue(OsgiVersion.fromCeylonVersion("1.2.m", false).compareTo(OsgiVersion.fromCeylonVersion("1.2", false)) < 0);
        Assert.assertTrue(OsgiVersion.fromCeylonVersion("1.2", false).compareTo(OsgiVersion.fromCeylonVersion("1.2.abc", false)) < 0);
        Assert.assertTrue(OsgiVersion.fromCeylonVersion("1.2.abc", false).compareTo(OsgiVersion.fromCeylonVersion("1.2.abc.3.4", false)) < 0);
        Assert.assertTrue(OsgiVersion.fromCeylonVersion("1.2.ga.3.4", false).compareTo(OsgiVersion.fromCeylonVersion("1.2.abc.3.4", false)) < 0);
        Assert.assertTrue(OsgiVersion.fromCeylonVersion("1.2.abc.3.4", false).compareTo(OsgiVersion.fromCeylonVersion("1.2.3.4", false)) < 0);
    }
}
