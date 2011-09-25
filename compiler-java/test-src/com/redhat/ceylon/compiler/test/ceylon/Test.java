package com.redhat.ceylon.compiler.test.ceylon;

import org.junit.Assert;

import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Test {
    public void fail(){
        Assert.fail();
    }
    public void assertEquals(@TypeInfo("ceylon.language.Object") Object reference, 
            @TypeInfo("ceylon.language.Object") Object value){
        if(reference == null){
            if(value == null)
                return;
            Assert.fail("Expected "+reference+" but got null");
        }
        if(value == null)
            Assert.fail("Expected null, got "+value);
        if(!reference.equals(value))
            Assert.fail("Expected "+reference+", got "+value);
    }
    public void assertTrue(boolean condition){
        Assert.assertTrue(condition);
    }
    public void assertFalse(boolean condition){
        Assert.assertFalse(condition);
    }
}
