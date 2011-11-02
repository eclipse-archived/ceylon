package com.redhat.ceylon.compiler.test.structure;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class StructureTest extends CompilerTest {
    
    //
    // Packages
    
    @Test
    public void testPkgPackage(){
        compareWithJavaSource("pkg/pkg");
    }

    //
    // Modules
    
    @Test
    public void testMdlModule(){
        compareWithJavaSource("module/single/module");
    }

    @Test
    public void testMdlModuleFromCompiledModule() throws IOException{
        compile("module/single/module.ceylon");
        
        String moduleName = "com.redhat.ceylon.compiler.test.structure.module.single";
        String modulePath = moduleName.replace('.', File.separatorChar)+File.separatorChar+"6.6.6"+File.separator;
        File jarFile = new File(destDir, modulePath+moduleName+"-6.6.6.car");
        assertTrue(jarFile.exists());

        JarFile jar = new JarFile(jarFile);
        // just to be sure
        ZipEntry bogusEntry = jar.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/BOGUS");
        assertNull(bogusEntry);

        ZipEntry moduleClass = jar.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/module.class");
        assertNotNull(moduleClass);
        jar.close();

        compile("module/single/subpackage/Subpackage.ceylon");

        // MUST reopen it
        jar = new JarFile(jarFile);

        ZipEntry subpackageClass = jar.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/subpackage/Subpackage.class");
        assertNotNull(subpackageClass);
    }

    //
    // Classes
    
    @Test
    public void testKlsClass(){
        compareWithJavaSource("klass/Klass");
    }
    @Test
    public void testKlsPublicClass(){
        compareWithJavaSource("klass/PublicKlass");
    }
    @Test
    public void testKlsInterface(){
        compareWithJavaSource("klass/Interface");
    }
    @Test
    public void testKlsInterfaceWithMembers(){
        compareWithJavaSource("klass/InterfaceWithMembers");
    }
    @Test
    public void testKlsInterfaceWithConcreteMembers(){
        compareWithJavaSource("klass/InterfaceWithConcreteMembers");
    }
    @Test
    public void testKlsInitializerParameter(){
        compareWithJavaSource("klass/InitializerParameter");
    }
    @Test
    public void testKlsExtends(){
        compareWithJavaSource("klass/Extends");
    }
    @Test
    public void testKlsSatisfiesErasure(){
        compareWithJavaSource("klass/SatisfiesErasure");
    }
    @Test
    public void testKlsSatisfies(){
        compareWithJavaSource("klass/Satisfies");
    }
    @Test
    public void testKlsSatisfiesWithMembers(){
        compareWithJavaSource("klass/SatisfiesWithMembers");
    }
    @Test
    public void testKlsAbstractFormal(){
        compareWithJavaSource("klass/AbstractFormal");
    }
    @Test
    public void testKlsKlassMethodTypeParams(){
        compareWithJavaSource("klass/KlassMethodTypeParams");
    }
    @Test
    public void testKlsKlassTypeParams(){
        compareWithJavaSource("klass/KlassTypeParams");
    }
    @Test
    public void testKlsKlassTypeParamsSatisfies(){
        compareWithJavaSource("klass/KlassTypeParamsSatisfies");
    }
    @Test
    public void testKlsInnerClass(){
        compareWithJavaSource("klass/InnerClass");
    }
    @Test
    public void testKlsLocalClass(){
        compareWithJavaSource("klass/LocalClass");
    }
    @Test
    public void testKlsInitializerVarargs(){
        compareWithJavaSource("klass/InitializerVarargs");
    }
    @Test
    public void testKlsKlassWithObjectMember(){
        compareWithJavaSource("klass/KlassWithObjectMember");
    }
    @Ignore("M2")
    @Test
    public void testKlsCaseTypes(){
        compareWithJavaSource("klass/CaseTypes");
    }
    
    //
    // Methods
    
    @Test
    public void testMthLocalMethod(){
        compareWithJavaSource("method/LocalMethod");
    }
    @Test
    public void testMthMethod(){
        compareWithJavaSource("method/Method");
    }
    @Test
    public void testMthMethodErasure(){
        compareWithJavaSource("method/MethodErasure");
    }
    @Test
    public void testMthMethodTypeParams(){
        compareWithJavaSource("method/MethodTypeParams");
    }
    @Test
    public void testMthMethodWithLocalObject(){
        compareWithJavaSource("method/MethodWithLocalObject");
    }
    @Test
    public void testMthMethodWithParam(){
        compareWithJavaSource("method/MethodWithParam");
    }
    @Test
    public void testMthMethodWithVarargs(){
        compareWithJavaSource("method/MethodWithVarargs");
    }
    @Test
    public void testMthPublicMethod(){
        compareWithJavaSource("method/PublicMethod");
    }

    //
    // Attributes
    
    @Test
    public void testAtrClassVariable(){
        compareWithJavaSource("attribute/ClassVariable");
    }
    @Test
    public void testAtrClassVariableWithInitializer(){
        compareWithJavaSource("attribute/ClassVariableWithInitializer");
    }
    @Test
    public void testAtrClassAttribute(){
        // FIXME: this one should fail and we should make sure it fails for the right reason
        compareWithJavaSource("attribute/ClassAttribute");
    }
    @Test
    public void testAtrClassAttributeWithInitializer(){
        compareWithJavaSource("attribute/ClassAttributeWithInitializer");
    }
    @Test
    public void testAtrClassAttributeGetter(){
        compareWithJavaSource("attribute/ClassAttributeGetter");
    }
    @Test
    public void testAtrClassAttributeGetterSetter(){
        compareWithJavaSource("attribute/ClassAttributeGetterSetter");
    }
    @Test
    public void testAtrInnerAttributeGetter(){
        compareWithJavaSource("attribute/InnerAttributeGetter");
    }
    @Test
    public void testAtrInnerAttributeGetterSetter(){
        compareWithJavaSource("attribute/InnerAttributeGetterSetter");
    }
    
    //
    // Toplevel
    
    @Test
    public void testTopToplevelAttribute(){
        compareWithJavaSource("toplevel/ToplevelAttribute");
    }
    @Test
    public void testTopToplevelAttributeShared(){
        compareWithJavaSource("toplevel/ToplevelAttributeShared");
    }
    @Test
    public void testTopToplevelVariable(){
        compareWithJavaSource("toplevel/ToplevelVariable");
    }
    @Test
    public void testTopToplevelVariableShared(){
        compareWithJavaSource("toplevel/ToplevelVariableShared");
    }
    @Test
    public void testTopToplevelObject(){
        compareWithJavaSource("toplevel/ToplevelObject");
    }
    @Test
    public void testTopToplevelObjectShared(){
        compareWithJavaSource("toplevel/ToplevelObjectShared");
    }
    @Test
    public void testTopToplevelObjectWithMembers(){
        compareWithJavaSource("toplevel/ToplevelObjectWithMembers");
    }
    @Test
    public void testTopToplevelObjectWithSupertypes(){
        compareWithJavaSource("toplevel/ToplevelObjectWithSupertypes");
    }
    @Test
    public void testTopToplevelMethods(){
        compareWithJavaSource("toplevel/ToplevelMethods");
    }
    
    //
    // Type
    
    @Test
    public void testTypBasicTypes(){
        compareWithJavaSource("type/BasicTypes");
    }
    @Test
    public void testTypConversions(){
        compareWithJavaSource("type/Conversions");
    }
    @Test
    public void testTypOptionalType(){
        compareWithJavaSource("type/OptionalType");
    }
    @Test
    public void testTypSequenceType(){
        compareWithJavaSource("type/SequenceType");
    }
    
    //
    // import
    
    private void compileImportedPackage() {
        compile("import_/pkg/C1.ceylon", "import_/pkg/C2.ceylon");
    }

    @Test
    public void testImpImportAttrSingle(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportAttrSingle");
    }

    @Test
    public void testImpImportMethodSingle(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportMethodSingle");
    }
    
    @Test
    public void testImpImportTypeSingle(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportTypeSingle");
    }
    
    @Test
    public void testImpImportTypeMultiple(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportTypeMultiple");
    }
    
    @Test
    public void testImpImportTypeAlias(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportTypeAlias");
    }
    
    @Test
    public void testImpImportWildcard(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportWildcard");
    }
    
    @Test
    public void testImpImportJavaRuntimeTypeSingle(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportJavaRuntimeTypeSingle");
    }
}
