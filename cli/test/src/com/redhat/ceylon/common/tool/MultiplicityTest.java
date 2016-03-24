package com.redhat.ceylon.common.tool;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tool.Multiplicity;

public class MultiplicityTest {

    @Test
    public void ctor() {
        try {
            new Multiplicity(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            
        }
        try {
            new Multiplicity(2, 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            
        }
    }
    
    @Test
    public void parse() {
        Assert.assertEquals(Multiplicity._0_OR_MORE, Multiplicity.fromString("*"));
        Assert.assertEquals(Multiplicity._1_OR_MORE, Multiplicity.fromString("+"));
        Assert.assertEquals(Multiplicity._0_OR_1, Multiplicity.fromString("?"));
        Assert.assertEquals(Multiplicity._1, Multiplicity.fromString("1"));
        
        Assert.assertEquals(new Multiplicity(1), Multiplicity.fromString("1"));
        Assert.assertEquals(new Multiplicity(3), Multiplicity.fromString("3"));
        
        Assert.assertEquals(new Multiplicity(1,3), Multiplicity.fromString("[1,3]"));
        Assert.assertEquals(new Multiplicity(2,3), Multiplicity.fromString("(1,3]"));
        Assert.assertEquals(new Multiplicity(2,2), Multiplicity.fromString("(1,3)"));
        Assert.assertEquals(new Multiplicity(1,2), Multiplicity.fromString("[1,3)"));
        
        Assert.assertEquals(new Multiplicity(1,Integer.MAX_VALUE), Multiplicity.fromString("[1,)"));
        Assert.assertEquals(new Multiplicity(1,Integer.MAX_VALUE), Multiplicity.fromString("[1,]"));
        
    }
    
}
