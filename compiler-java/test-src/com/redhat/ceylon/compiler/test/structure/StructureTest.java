package com.redhat.ceylon.compiler.test.structure;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class StructureTest extends CompilerTest {
    
    //
    // Packages
    
    @Test
    public void testPackage(){
        compareWithJavaSource("pkg/pkg");
    }
    
    //
    // Classes
    
    @Test
    public void testClass(){
        compareWithJavaSource("klass/Klass");
    }
    @Test
    public void testPublicClass(){
        compareWithJavaSource("klass/PublicKlass");
    }
    @Test
    public void testInterface(){
        compareWithJavaSource("klass/Interface");
    }
    @Test
    public void testInterfaceWithMembers(){
        compareWithJavaSource("klass/InterfaceWithMembers");
    }
    @Test
    public void testInitializerParameter(){
        compareWithJavaSource("klass/InitializerParameter");
    }
    @Test
    public void testExtends(){
        compareWithJavaSource("klass/Extends");
    }
    @Test
    public void testSatisfies(){
        compareWithJavaSource("klass/Satisfies");
    }
    @Test
    public void testSatisfiesWithMembers(){
        compareWithJavaSource("klass/SatisfiesWithMembers");
    }
    @Test
    public void testAbstractFormal(){
        compareWithJavaSource("klass/AbstractFormal");
    }
    
    //
    // Methods
    
    @Test
    public void testMethod(){
        compareWithJavaSource("method/Method");
    }
    @Test
    public void testMethodWithParam(){
        compareWithJavaSource("method/MethodWithParam");
    }
    @Test
    public void testPublicMethod(){
        compareWithJavaSource("method/PublicMethod");
    }
    @Test
    public void testInnerMethod(){
        compareWithJavaSource("method/InnerMethod");
    }

    //
    // Attributes
    
    @Test
    public void testClassVariable(){
        compareWithJavaSource("attribute/ClassVariable");
    }
    @Test
    public void testClassVariableWithInitializer(){
        compareWithJavaSource("attribute/ClassVariableWithInitializer");
    }
    @Test
    public void testClassAttribute(){
        // FIXME: this one should fail and we should make sure it fails for the right reason
        compareWithJavaSource("attribute/ClassAttribute");
    }
    @Test
    public void testClassAttributeWithInitializer(){
        compareWithJavaSource("attribute/ClassAttributeWithInitializer");
    }
    @Test
    public void testClassAttributeGetter(){
        compareWithJavaSource("attribute/ClassAttributeGetter");
    }
    @Test
    public void testClassAttributeGetterSetter(){
        compareWithJavaSource("attribute/ClassAttributeGetterSetter");
    }
    
    //
    // Toplevel
    
    @Test
    public void testToplevelAttribute(){
        compareWithJavaSource("toplevel/ToplevelAttribute");
    }
    @Test
    public void testToplevelAttributeShared(){
        compareWithJavaSource("toplevel/ToplevelAttributeShared");
    }
    @Test
    public void testToplevelVariable(){
        compareWithJavaSource("toplevel/ToplevelVariable");
    }
    @Test
    public void testToplevelVariableShared(){
        compareWithJavaSource("toplevel/ToplevelVariableShared");
    }
    @Test
    public void testToplevelObject(){
        compareWithJavaSource("toplevel/ToplevelObject");
    }
    @Test
    public void testToplevelMethods(){
        compareWithJavaSource("toplevel/ToplevelMethods");
    }
}
