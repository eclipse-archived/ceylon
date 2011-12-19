/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.test.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;
import com.redhat.ceylon.compiler.tools.CeyloncTaskImpl;
import com.sun.tools.javac.zip.ZipFileIndex;

public class StructureTest extends CompilerTest {
    
    //
    // Packages
    
    @Test
    public void testPkgPackage(){
        compareWithJavaSource("pkg/pkg");
    }

    @Test
    public void testPkgPackageMetadata(){
        compareWithJavaSource("pkg/package");
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
        
        ZipFileIndex.clearCache();
        Assert.assertEquals(ZipFileIndex.getZipFileIndexes().size(), 0);
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.single", "6.6.6");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);
        // just to be sure
        ZipEntry bogusEntry = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/BOGUS");
        assertNull(bogusEntry);

        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/module.class");
        assertNotNull(moduleClass);
        car.close();

        compile("module/single/subpackage/Subpackage.ceylon");

        ZipFileIndex.clearCache();
        Assert.assertEquals(ZipFileIndex.getZipFileIndexes().size(), 0);
        
        // MUST reopen it
        car = new JarFile(carFile);

        ZipEntry subpackageClass = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/subpackage/Subpackage.class");
        assertNotNull(subpackageClass);
    }

    @Test
    public void testMdlCompilerGeneratesModuleForValidUnits() throws IOException{
        CeyloncTaskImpl compilerTask = getCompilerTask("module/single/module.ceylon", "module/single/Correct.ceylon", "module/single/Invalid.ceylon");
        Boolean success = compilerTask.call();
        assertFalse(success);
        
        ZipFileIndex.clearCache();
        Assert.assertEquals(ZipFileIndex.getZipFileIndexes().size(), 0);
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.single", "6.6.6");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/module.class");
        assertNotNull(moduleClass);

        ZipEntry correctClass = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/Correct.class");
        assertNotNull(correctClass);

        ZipEntry invalidClass = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/Invalid.class");
        assertNull(invalidClass);
        
        car.close();
    }

    private File getModuleArchive(String moduleName, String version) {
        return getModuleArchive(moduleName, version, destDir);
    }

    private File getModuleArchive(String moduleName, String version, String destDir) {
        return getArchiveName(moduleName, version, destDir, "car");
    }

    private File getSourceArchive(String moduleName, String version) {
        return getArchiveName(moduleName, version, destDir, "src");
    }
    
    private File getArchiveName(String moduleName, String version, String destDir, String extension) {
        String modulePath = moduleName.replace('.', File.separatorChar)+File.separatorChar+version+File.separator;
        File archiveFile = new File(destDir, modulePath+moduleName+"-"+version+"."+extension);
        return archiveFile;
    }

    @Test
    public void testMdlInterdepModule(){
        // first compile it all from source
        compile("module/interdep/a/module.ceylon", "module/interdep/a/package.ceylon", "module/interdep/a/b.ceylon", "module/interdep/a/A.ceylon",
                "module/interdep/b/module.ceylon", "module/interdep/b/package.ceylon", "module/interdep/b/a.ceylon", "module/interdep/b/B.ceylon");
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.interdep.a", "6.6.6");
        assertTrue(carFile.exists());

        carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.interdep.b", "6.6.6");
        assertTrue(carFile.exists());
        
        // then try to compile only one module (the other being loaded from its car) 
        compile("module/interdep/a/module.ceylon", "module/interdep/a/b.ceylon", "module/interdep/a/A.ceylon");
    }

    @Test
    public void testMdlDependentModule(){
        // Compile only the first module 
        compile("module/depend/a/module.ceylon", "module/depend/a/package.ceylon", "module/depend/a/A.ceylon");
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.depend.a", "6.6.6");
        assertTrue(carFile.exists());

        // then try to compile only one module (the other being loaded from its car) 
        compile("module/depend/b/module.ceylon", "module/depend/b/a.ceylon", "module/depend/b/aWildcard.ceylon");

        carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.depend.b", "6.6.6");
        assertTrue(carFile.exists());
    }

    @Test
    public void testMdlImplicitDependentModule(){
        // Compile only the first module 
        compile("module/implicit/a/module.ceylon", "module/implicit/a/package.ceylon", "module/implicit/a/A.ceylon",
                "module/implicit/b/module.ceylon", "module/implicit/b/package.ceylon", "module/implicit/b/B.ceylon", "module/implicit/b/B2.ceylon",
                "module/implicit/c/module.ceylon", "module/implicit/c/package.ceylon", "module/implicit/c/c.ceylon");
        
        // Dependencies:
        //
        // c.ceylon--> B2.ceylon
        //         |
        //         '-> B.ceylon  --> A.ceylon

        // Successfull tests :
        
        compile("module/implicit/c/c.ceylon");
        compile("module/implicit/b/B.ceylon", "module/implicit/c/c.ceylon");
        compile("module/implicit/b/B2.ceylon", "module/implicit/c/c.ceylon");
        
        // Failing tests :
        
        Boolean success1 = getCompilerTask("module/implicit/c/c.ceylon", "module/implicit/b/B.ceylon").call();
        // => B.ceylon : package not found in dependent modules: com.redhat.ceylon.compiler.test.structure.module.implicit.a
        Boolean success2 = getCompilerTask("module/implicit/c/c.ceylon", "module/implicit/b/B2.ceylon").call();
        // => c.ceylon : TypeVisitor caused an exception visiting Import node: com.sun.tools.javac.code.Symbol$CompletionFailure: class file for com.redhat.ceylon.compiler.test.structure.module.implicit.a.A not found at unknown

        Assert.assertTrue(success1 && success2);
    }

    private void copy(File source, File dest) throws IOException {
        InputStream inputStream = new FileInputStream(source);
        OutputStream outputStream = new FileOutputStream(dest); 
        byte[] buffer = new byte[4096];
        int read;
        while((read = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, read);
        }
        inputStream.close();
        outputStream.close();
    }
    
    @Test
    public void testMdlSuppressObsoleteClasses() throws IOException{
        File sourceFile = new File(path, "module/single/SuppressClass.ceylon");

        copy(new File(path, "module/single/SuppressClass_1.ceylon"), sourceFile);
        CeyloncTaskImpl compilerTask = getCompilerTask("module/single/module.ceylon", "module/single/SuppressClass.ceylon");
        Boolean success = compilerTask.call();
        assertTrue(success);

        ZipFileIndex.clearCache();
        Assert.assertEquals(ZipFileIndex.getZipFileIndexes().size(), 0);
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.single", "6.6.6");
        assertTrue(carFile.exists());
        ZipFile car = new ZipFile(carFile);
        ZipEntry oneClass = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/One.class");
        assertNotNull(oneClass);
        ZipEntry twoClass = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/Two.class");
        assertNotNull(twoClass);
        car.close();

        copy(new File(path, "module/single/SuppressClass_2.ceylon"), sourceFile);
        compilerTask = getCompilerTask("module/single/module.ceylon", "module/single/SuppressClass.ceylon");
        success = compilerTask.call();
        assertTrue(success);
        
        ZipFileIndex.clearCache();
        Assert.assertEquals(ZipFileIndex.getZipFileIndexes().size(), 0);
        
        carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.single", "6.6.6");
        assertTrue(carFile.exists());
        car = new ZipFile(carFile);
        oneClass = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/One.class");
        assertNotNull(oneClass);
        twoClass = car.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/Two.class");
        assertNull(twoClass);
        car.close();
        
        sourceFile.delete();
    }

    
    @Test
    public void testMdlMultipleRepos(){
        cleanCars("build/ceylon-cars-a");
        cleanCars("build/ceylon-cars-b");
        cleanCars("build/ceylon-cars-c");
        
        // Compile the first module in its own repo 
        File repoA = new File("build/ceylon-cars-a");
        repoA.mkdirs();
        Boolean result = getCompilerTask(Arrays.asList("-out", repoA.getPath()),
                "module/depend/a/module.ceylon", "module/depend/a/package.ceylon", "module/depend/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.depend.a", "6.6.6", repoA.getPath());
        assertTrue(carFile.exists());

        // make another repo for the second module
        File repoB = new File("build/ceylon-cars-b");
        repoB.mkdirs();

        // then try to compile only one module (the other being loaded from its car) 
        result = getCompilerTask(Arrays.asList("-out", repoB.getPath(), "-rep", repoA.getPath()),
                "module/depend/b/module.ceylon", "module/depend/b/package.ceylon", "module/depend/b/a.ceylon", "module/depend/b/B.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.depend.b", "6.6.6", repoB.getPath());
        assertTrue(carFile.exists());

        // make another repo for the third module
        File repoC = new File("build/ceylon-cars-c");
        repoC.mkdirs();

        // then try to compile only one module (the others being loaded from their car) 
        result = getCompilerTask(Arrays.asList("-out", repoC.getPath(), 
                "-rep", repoA.getPath(), "-rep", repoB.getPath()),
                "module/depend/c/module.ceylon", "module/depend/c/a.ceylon", "module/depend/c/b.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        carFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.depend.c", "6.6.6", repoC.getPath());
        assertTrue(carFile.exists());
    }

    @Test
    public void testMdlSourceArchive() throws IOException{
        File sourceArchiveFile = getSourceArchive("com.redhat.ceylon.compiler.test.structure.module.single", "6.6.6");
        sourceArchiveFile.delete();
        assertFalse(sourceArchiveFile.exists());

        // compile one file
        compile("module/single/module.ceylon");

        // make sure it was created
        assertTrue(sourceArchiveFile.exists());

        JarFile sourceArchive = new JarFile(sourceArchiveFile);
        assertEquals(1, countEntries(sourceArchive));

        ZipEntry moduleClass = sourceArchive.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/module.ceylon");
        assertNotNull(moduleClass);
        sourceArchive.close();

        // now compile another file
        compile("module/single/subpackage/Subpackage.ceylon");

        // MUST reopen it
        sourceArchive = new JarFile(sourceArchiveFile);
        assertEquals(2, countEntries(sourceArchive));

        ZipEntry subpackageClass = sourceArchive.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/subpackage/Subpackage.ceylon");
        assertNotNull(subpackageClass);
    }

    private int countEntries(JarFile jar) {
        int count = 0;
        Enumeration<JarEntry> entries = jar.entries();
        while(entries.hasMoreElements()){
            count++;
            entries.nextElement();
        }
        return count;
    }

    @Test
    public void testMdlSha1Signatures() throws IOException{
        File sourceArchiveFile = getSourceArchive("com.redhat.ceylon.compiler.test.structure.module.single", "6.6.6");
        File sourceArchiveSignatureFile = new File(sourceArchiveFile.getPath()+".sha1");
        File moduleArchiveFile = getModuleArchive("com.redhat.ceylon.compiler.test.structure.module.single", "6.6.6");
        File moduleArchiveSignatureFile = new File(moduleArchiveFile.getPath()+".sha1");
        // cleanup
        sourceArchiveFile.delete();
        sourceArchiveSignatureFile.delete();
        moduleArchiveFile.delete();
        moduleArchiveSignatureFile.delete();
        // safety check
        assertFalse(sourceArchiveFile.exists());
        assertFalse(sourceArchiveSignatureFile.exists());
        assertFalse(moduleArchiveFile.exists());
        assertFalse(moduleArchiveSignatureFile.exists());

        // compile one file
        compile("module/single/module.ceylon");

        // make sure everything was created
        assertTrue(sourceArchiveFile.exists());
        assertTrue(sourceArchiveSignatureFile.exists());
        assertTrue(moduleArchiveFile.exists());
        assertTrue(moduleArchiveSignatureFile.exists());

        // check the signatures vaguely
        checkSha1(sourceArchiveSignatureFile);
        checkSha1(moduleArchiveSignatureFile);
    }

    private void checkSha1(File signatureFile) throws IOException {
        Assert.assertEquals(40, signatureFile.length());
        FileInputStream reader = new FileInputStream(signatureFile);
        byte[] bytes = new byte[40];
        Assert.assertEquals(40, reader.read(bytes));
        reader.close();
        char[] sha1 = new String(bytes, "ASCII").toCharArray();
        for (int i = 0; i < sha1.length; i++) {
            char c = sha1[i];
            Assert.assertTrue((c >= '0' && c <= '9')
                    || (c >= 'a' && c <= 'f')
                    || (c >= 'A' && c <= 'F'));
        }
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
    // Classes
    
    @Test
    public void testKlsClass(){
        compareWithJavaSource("klass/Klass");
    }
    @Test
    public void testKlsDefaultedInitializerParameter(){
        compareWithJavaSource("klass/DefaultedInitializerParameter");
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
    @Ignore("M2")
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
    public void testKlsExtendsGeneric(){
        compareWithJavaSource("klass/ExtendsGeneric");
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
    public void testKlsSatisfiesGeneric(){
        compareWithJavaSource("klass/SatisfiesGeneric");
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
    public void testMthMethodWithDefaultParams(){
        compareWithJavaSource("method/MethodWithDefaultParams");
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
    public void testTopToplevelMethods(){
        compareWithJavaSource("toplevel/ToplevelMethods");
    }
    @Test
    public void testTopToplevelMethodWithDefaultedParams(){
        compareWithJavaSource("toplevel/ToplevelMethodWithDefaultedParams");
    }
    @Test
    public void testTopToplevelObject(){
        compareWithJavaSource("toplevel/ToplevelObject");
    }
    @Test
    public void testTopToplevelObjectWithMembers(){
        compareWithJavaSource("toplevel/ToplevelObjectWithMembers");
    }
    @Test
    public void testTopToplevelObjectShared(){
        compareWithJavaSource("toplevel/ToplevelObjectShared");
    }
    @Test
    public void testTopToplevelObjectWithSupertypes(){
        compareWithJavaSource("toplevel/ToplevelObjectWithSupertypes");
    }
    @Test
    public void testTopToplevelVariable(){
        compareWithJavaSource("toplevel/ToplevelVariable");
    }
    @Test
    public void testTopToplevelVariableShared(){
        compareWithJavaSource("toplevel/ToplevelVariableShared");
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
    
    @Test
    public void testImpImportAliasAndWildcard(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportAliasAndWildcard");
    }
    
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

    @Test
    public void testImpImportCeylonLanguageType(){
        compareWithJavaSource("import_/ImportCeylonLanguageType");
    }
}
