package com.redhat.ceylon.compiler.java.test.structure.ee;

import java.util.ArrayList;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class EeTests extends CompilerTests {

    
    @Test
    public void testNoFinalMethods() {
        ArrayList<String> list = new ArrayList<String>(defaultOptions);
        list.add("-ee");
        compareWithJavaSource(list, "NoFinalMethods.src", "NoFinalMethods.ceylon");
    }
    
    @Test
    public void testPublicImplicitCtor() {
        ArrayList<String> list = new ArrayList<String>(defaultOptions);
        list.add("-ee");
        compareWithJavaSource(list, "PublicImplicitCtor.src", "PublicImplicitCtor.ceylon");
    }
    
    @Test
    public void testUncheckedLate() {
        ArrayList<String> list = new ArrayList<String>(defaultOptions);
        list.add("-ee");
        compareWithJavaSource(list, "UncheckedLate.src", "UncheckedLate.ceylon");
    }
    
    @Test
    public void testJavaBoxes() {
        ArrayList<String> list = new ArrayList<String>(defaultOptions);
        list.add("-ee");
        compareWithJavaSource(list, "JavaBoxes.src", "JavaBoxes.ceylon");
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
}
