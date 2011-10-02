package com.redhat.ceylon.compiler.test.misc;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class DefaultPackageTest extends CompilerTest {
    @Test
    public void testDefaultPackage(){
        compareWithJavaSource("defaultPackage");
    }
    
    @Override
    protected String getSourcePath() {
        return path;
    }
}
