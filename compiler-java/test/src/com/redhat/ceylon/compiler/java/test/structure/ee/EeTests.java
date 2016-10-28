package com.redhat.ceylon.compiler.java.test.structure.ee;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class EeTests extends CompilerTests {

    
    @Test
    public void testNoFinalMethods() {
        compareWithJavaSource("NoFinalMethods");
    }
    
    @Test
    public void testPublicImplicitCtor() {
        compareWithJavaSource("PublicImplicitCtor");
    }
    
    @Test
    public void testUncheckedLate() {
        compareWithJavaSource("UncheckedLate");
    }
    
    @Test
    public void testJavaBoxes() {
        compareWithJavaSource("JavaBoxes");
    }
    
    @Test
    public void testEeModeEnabling() {
        compareWithJavaSource("enabledModule/Class");
        compareWithJavaSource("enabledModuleMaven/Class");
        compareWithJavaSource("enabledPackage/Class.src",
                "enabledPackage/Class.ceylon",
                "enabledPackage/package.ceylon");
        compareWithJavaSource("enabledClass/enabledClass");
    }
    
    @Test
    public void testEntityExample() {
        // don't want no stinkin' warnings about late and things not being properly initialized
        compilesWithoutWarnings("EntityExample.ceylon");
        compareWithJavaSource("EntityExample");
    }
}
