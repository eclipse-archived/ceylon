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
package com.redhat.ceylon.compiler.java.test.recovery;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.test.ErrorCollector;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;

public class RecoveryTest extends CompilerTest {
    
    @Test
    public void testRcvBrokenClass(){
        compile(3, "BrokenClass.ceylon");
    }

    @Test
    public void testRcvBrokenMethod(){
        compile(3, "BrokenMethod.ceylon");
    }

    @Test
    public void testRcvBrokenAttribute(){
        compile(10, "BrokenAttribute.ceylon");
    }

    @Test
    public void testRcvClassWithBrokenMembers(){
        compile(30, "ClassWithBrokenMembers.ceylon");
    }

    @Test
    public void testRcvDuplicateDeclarations(){
        // this is https://github.com/ceylon/ceylon-compiler/issues/250
        compile(1, "DuplicateDeclaration1.ceylon", "DuplicateDeclaration2.ceylon");
    }

    @Test
    public void testRcvM3Features(){
        compile(1, "M3Features.ceylon");
    }

    @Ignore("https://github.com/ceylon/ceylon-compiler/issues/1173")
    @Test
    public void testRcvIndirectUser(){
        compile(1, "BrokenIndirectUser.ceylon");
    }

    private void compile(int expectedErrors, String... ceylon){
        DiagnosticCollector<JavaFileObject> errorCollector = new DiagnosticCollector<JavaFileObject>();
        // Stef: can't seem to be able to get this cast right no matter what I try
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Boolean success = getCompilerTask(defaultOptions, (DiagnosticListener)errorCollector , ceylon).call();
        Assert.assertEquals(expectedErrors, getErrorCount(errorCollector));
        Assert.assertFalse(success);
    }

    private int getErrorCount(DiagnosticCollector<JavaFileObject> errorCollector) {
        int errors = 0;
        for(Diagnostic<? extends JavaFileObject> diagnostic : errorCollector.getDiagnostics()){
            if(diagnostic.getKind() == Kind.ERROR)
                errors++;
            if(diagnostic.getSource() != null){
                System.err.println("("+diagnostic.getKind()+") "+diagnostic.getSource().getName()
                        +"["+diagnostic.getLineNumber()+","+diagnostic.getColumnNumber()+"]: "
                        +diagnostic.getMessage(Locale.getDefault()));
            }else{
                System.err.println("("+diagnostic.getKind()+"): "
                        +diagnostic.getMessage(Locale.getDefault()));
            }
        }
        return errors;
    }

    @Test
    public void testDuplicateInDefault() throws IOException {
        String subPath = "modules/duplicateInDefault";
        String srcPath = getPackagePath()+subPath;
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(srcPath);
        options.addAll(defaultOptions);
        options.add("-continue");
        ErrorCollector c = new ErrorCollector();
        CeyloncTaskImpl task = getCompilerTask(options, 
                c,
                Arrays.asList("okmodule"),
                subPath + "/dupdeclerr1.ceylon",
                subPath + "/dupdeclerr2.ceylon",
                subPath + "/someok.ceylon");
        Boolean ret = task.call();
        assertFalse(ret);

        TreeSet<CompilerError> actualErrors = c.get(Diagnostic.Kind.ERROR);
        compareErrors(actualErrors,
//                new CompilerError(21, "cannot find module artifact notfound-1(.car|.jar)\n  \t- dependency tree: okmodule/1.0.0 -> notfound/1"),
                new CompilerError(20, "duplicate declaration name: run"));
        
        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("foobar_.class");
        assertNotNull(moduleClass);
        car.close();

        carFile = getModuleArchive("okmodule", "1.0.0");
        assertTrue(carFile.exists());

        car = new JarFile(carFile);

        moduleClass = car.getEntry("okmodule/module_.class");
        assertNotNull(moduleClass);
        car.close();
    }

    @Test
    public void testMissingImport() throws IOException {
        String subPath = "modules/missingImport";
        String srcPath = getPackagePath()+subPath;
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(srcPath);
        options.addAll(defaultOptions);
        options.add("-continue");
        ErrorCollector c = new ErrorCollector();
        CeyloncTaskImpl task = getCompilerTask(options, 
                c,
                Arrays.asList("okmodule"),
                subPath + "/someok.ceylon");
        Boolean ret = task.call();
        assertFalse(ret);

        TreeSet<CompilerError> actualErrors = c.get(Diagnostic.Kind.ERROR);
        compareErrors(actualErrors,
                new CompilerError(21, "cannot find module artifact notfound-1(.car|.jar)\n  \t- dependency tree: okmodule/1.0.0 -> notfound/1"));
        
        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("foobar_.class");
        assertNotNull(moduleClass);
        car.close();

        carFile = getModuleArchive("okmodule", "1.0.0");
        assertFalse(carFile.exists());
    }

    @Test
    public void testSyntaxErrorInModule() throws IOException {
        String subPath = "modules/syntaxErrorInModule";
        String srcPath = getPackagePath()+subPath;
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(srcPath);
        options.addAll(defaultOptions);
        options.add("-continue");
        ErrorCollector c = new ErrorCollector();
        CeyloncTaskImpl task = getCompilerTask(options, 
                c,
                Arrays.asList("okmodule"),
                subPath + "/someok.ceylon");
        Boolean ret = task.call();
        assertFalse(ret);

        TreeSet<CompilerError> actualErrors = c.get(Diagnostic.Kind.ERROR);
        compareErrors(actualErrors,
                new CompilerError(-1, "incorrect syntax: no viable alternative at token end of file"),
                new CompilerError(20, "incorrect syntax: mismatched token 'ERROR' expecting initial-lowercase identifier"),
                new CompilerError(20, "incorrect syntax: no viable alternative at token '\"1.0.0\"'"),
                new CompilerError(20, "incorrect syntax: no viable alternative at token 'okmodule'"),
                new CompilerError(21, "incorrect syntax: no viable alternative at token '}'")
        );
        
        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("foobar_.class");
        assertNotNull(moduleClass);
        car.close();

        carFile = getModuleArchive("okmodule", "1.0.0");
        assertFalse(carFile.exists());
    }

    @Test
    public void testTako() throws IOException {
        String subPath = "modules/bug1411";
        String srcPath = getPackagePath()+subPath;
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(srcPath);
        options.addAll(defaultOptions);
        options.add("-continue");
        ErrorCollector c = new ErrorCollector();
        CeyloncTaskImpl task = getCompilerTask(options, 
                c,
                subPath + "/dupdecl.ceylon",
                subPath + "/moduletest1/module.ceylon",
                subPath + "/moduletest1/package.ceylon",
                subPath + "/moduletest1/helloworld.ceylon",
                subPath + "/hello/hello.ceylon",
                subPath + "/hello/module.ceylon",
                subPath + "/hello/package.ceylon",
                subPath + "/testtest/module.ceylon",
                subPath + "/testtest/test.ceylon",
                subPath + "/sub/mod/module.ceylon",
                subPath + "/sub/mod/run.ceylon",
                subPath + "/sub/mod/package.ceylon",
                subPath + "/unknownmodule/module.ceylon",
                subPath + "/unknownmodule/run.ceylon",
                subPath + "/unknownmodule/package.ceylon",
                subPath + "/interop/module.ceylon",
                subPath + "/interop/interop.ceylon",
                subPath + "/jsonly/jsonly.ceylon",
                subPath + "/jsonly/module.ceylon",
                subPath + "/jsonly/package.ceylon",
                subPath + "/modulep/module.ceylon",
                subPath + "/modulep/main.ceylon",
                subPath + "/browser/dom/main.ceylon",
                subPath + "/browser/dom/package.ceylon",
                subPath + "/browser/module.ceylon",
                subPath + "/browser/main.ceylon",
                subPath + "/browser/package.ceylon",
                subPath + "/test/InheritanceAndBoxingTest.ceylon",
                subPath + "/test/ExceptionTest.ceylon",
                subPath + "/test/Foo.ceylon",
                subPath + "/test/GameOfLife.ceylon",
                subPath + "/test/Bar.ceylon",
                subPath + "/test/encoding.ceylon",
                subPath + "/test/helloworld.ceylon",
                subPath + "/swing/module.ceylon",
                subPath + "/swing/run.ceylon",
                subPath + "/swing/package.ceylon",
                subPath + "/interoprev/module.ceylon",
                subPath + "/interoprev/run.ceylon",
                subPath + "/interoprev/package.ceylon",
                subPath + "/java/hello.ceylon",
                subPath + "/java/module.ceylon",
                subPath + "/java/package.ceylon",
                subPath + "/helloworld.ceylon",
                subPath + "/wrongversion/module.ceylon",
                subPath + "/wrongversion/run.ceylon",
                subPath + "/wrongversion/package.ceylon",
                subPath + "/importhello/module.ceylon",
                subPath + "/importhello/helloworld.ceylon",
                subPath + "/usingimport.ceylon",
                // These depend on the error recovery we plan for 1.1
//                subPath + "/javaa/bugJvm1290.ceylon",
//                subPath + "/javaa/module.ceylon",
//                subPath + "/javaa/run.ceylon"
                subPath + "/gavinprob/module.ceylon",
                subPath + "/gavinprob/main.ceylon",
                subPath + "/web_ide_script/module.ceylon",
                subPath + "/web_ide_script/hello_world.ceylon",
                subPath + "/timetest/module.ceylon",
                subPath + "/timetest/test.ceylon"
                );
        Boolean ret = task.call();
        assertFalse(ret);

        TreeSet<CompilerError> actualErrors = c.get(Diagnostic.Kind.ERROR);
        compareErrors(actualErrors,
                new CompilerError(1, "package not found in imported modules: java.lang"),
                new CompilerError(1, "source code imports two different versions of the same module: version 0.5 and version 1.0.0 of ceylon.file"),
                new CompilerError(2, "cannot find module artifact fooxhilio.bastardo-77.9(.car|.jar)\n"
                +"  \t- dependency tree: unknownmodule/1.0.0 -> fooxhilio.bastardo/77.9"),
                new CompilerError(4, "function or value does not exist: nanoTime"),
                new CompilerError(4, "type does not exist: Test"),
                new CompilerError(5, "duplicate declaration name: run"),
                new CompilerError(7, "duplicate declaration name: run"),
                new CompilerError(10, "case must be a toplevel anonymous class: alive is not toplevel"),
                new CompilerError(10, "case must be a toplevel anonymous class: dead is not toplevel"),
                new CompilerError(10, "case must be a toplevel anonymous class: moribund is not toplevel"),
                new CompilerError(10, "case must be a toplevel anonymous class: resurrected is not toplevel"),
                new CompilerError(12, "imported declaration not found: newServer"),
                new CompilerError(30, "function or value does not exist: newServer"),
                new CompilerError(30, "named arguments not supported for indirect invocations"),
                new CompilerError(135, "function or value does not exist: array"),
                new CompilerError(135, "value type could not be inferred"),
                new CompilerError(135, "named arguments not supported for indirect invocations"),
                new CompilerError(138, "could not determine type of function or value reference: cells"),
                new CompilerError(138, "value type could not be inferred"),
                new CompilerError(139, "could not determine type of function or value reference: c"),
                new CompilerError(140, "could not determine type of function or value reference: c")
        );
        
        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        String[] modules = new String[]{
                "swing/1.0.0",
                "java/1.0.0",
                "web_ide_script/1.0.0",
                "browser/1.0.0",
                "timetest/1.0.0",
                "interop/1.0.0",
                "importhello/0.1",
                "interoprev/1.0.0",
                "modulep/0.1",
                "hello/1.0.0",
                "moduletest1/0.1",
                "jsonly/1.0.0",
                "testtest/1.1",
                "sub.mod/1.0.0"
        };

        for(String module : modules){
            int sep = module.indexOf('/');
            String name = module.substring(0, sep);
            String version = module.substring(sep+1);
            carFile = getModuleArchive(name, version);
            assertTrue(carFile.exists());
        }
    }

    @Test
    public void testErrorSameCompilationUnit() throws IOException {
        String subPath = "modules/errorInSameCompilationUnit";
        String srcPath = getPackagePath()+subPath;
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(srcPath);
        options.addAll(defaultOptions);
        options.add("-continue");
        ErrorCollector c = new ErrorCollector();
        CeyloncTaskImpl task = getCompilerTask(options, 
                c,
                subPath + "/someok.ceylon");
        Boolean ret = task.call();
        assertFalse(ret);

        TreeSet<CompilerError> actualErrors = c.get(Diagnostic.Kind.ERROR);
        compareErrors(actualErrors,
                new CompilerError(23, "type does not exist: ERROR")
        );
        
        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("a_.class");
        assertNotNull(moduleClass);
        car.close();
    }

}