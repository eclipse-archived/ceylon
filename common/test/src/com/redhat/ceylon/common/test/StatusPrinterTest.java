package com.redhat.ceylon.common.test;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.StatusPrinter;

public class StatusPrinterTest {

    @Test
    public void testLimit(){
        StatusPrinter p = new StatusPrinter();
        Assert.assertEquals("", p.limit("", 0));
        Assert.assertEquals("", p.limit("", 4));

        Assert.assertEquals("abs", p.limit("abs", 4));
        Assert.assertEquals("abs", p.limit("abs", 3));
        Assert.assertEquals("a…", p.limit("abs", 2));
        Assert.assertEquals("…", p.limit("abs", 1));
        Assert.assertEquals("", p.limit("abs", 0));

        Assert.assertEquals("abbs", p.limit("abbs", 4));
        Assert.assertEquals("a…s", p.limit("abbs", 3));
        Assert.assertEquals("a…", p.limit("abbs", 2));
        Assert.assertEquals("…", p.limit("abbs", 1));
        Assert.assertEquals("", p.limit("abbs", 0));

        Assert.assertEquals("ab…bs", p.limit("abccbs", 5));
    }

    @Test
    public void testParts(){
        StatusPrinter p = new StatusPrinter(20);

        Assert.assertEquals(5, p.remainingForPercentage(0.25));
        Assert.assertEquals("co…ar", p.part("com.bar", 5));

        Assert.assertEquals("             com.bar", p.partRight("com.bar"));
        Assert.assertEquals("                 c…r", p.partRight("com.bar", 3));
    }

}
