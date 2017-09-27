package org.eclipse.ceylon.tools;

import static org.eclipse.ceylon.common.ModuleSpec.DEFAULT_MODULE;
import static org.eclipse.ceylon.common.ModuleSpec.parse;

import org.eclipse.ceylon.common.ModuleSpec.Option;
import org.junit.Assert;
import org.junit.Test;

public class ModuleSpecTest {

    @Test
    public void nameOnly() {
        parse("org.example");
        parse("org.example", Option.VERSION_PROHIBITED);
        try {
            parse("org.example", Option.VERSION_REQUIRED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        parse("org.example/");
        
        try {
            parse("org.example/", Option.VERSION_PROHIBITED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            parse("org.example/", Option.VERSION_REQUIRED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void nameAndVersion() {
        parse("org.example/1.0");
        parse("org.example/1.0", Option.VERSION_REQUIRED);
        
        try {
            parse("org.example/1.0", Option.VERSION_PROHIBITED);    
            Assert.fail();
        } catch (IllegalArgumentException e) {}   
    }
    
    @Test
    public void missingName() {
        try {
            parse("/org.example");
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            parse("/org.example", Option.VERSION_PROHIBITED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            parse("/org.example", Option.VERSION_REQUIRED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void defaultModule() {
        DEFAULT_MODULE.equals(parse("DEFAULT"));
        DEFAULT_MODULE.equals(parse("DEFAULT", Option.VERSION_PROHIBITED));
        DEFAULT_MODULE.equals(parse("DEFAULT", Option.VERSION_REQUIRED));
        try {
            parse("DEFAULT", Option.DEFAULT_MODULE_PROHIBITED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
    }
    
}
