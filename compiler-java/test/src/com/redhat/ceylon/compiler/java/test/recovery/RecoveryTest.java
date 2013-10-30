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

}