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
import java.util.ArrayList;
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

import org.junit.Assert;
import org.junit.Test;

import ceylon.language.Anything;
import ceylon.language.Callable;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.language.UnresolvedCompilationError;
import com.redhat.ceylon.compiler.java.metadata.CompileTimeError;
import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.test.ErrorCollector;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;

public class RecoveryTest extends CompilerTest {
    
    protected void compileAndRunWithUnresolvedCompilationError(
            String ceylon,
            String main, 
            String expectedError,
            int... sequence) {
        compileIgnoringCeylonErrors(ceylon);
        runWithUnresolvedCompilationError(main, expectedError, sequence);
    }

    protected void runWithUnresolvedCompilationError(String main,
            String expectedError,
            int... sequence) {
        final ArrayList<ceylon.language.Integer> sb= new ArrayList<ceylon.language.Integer>();
        Callable<Anything> c = new AbstractCallable<Anything>(Anything.$TypeDescriptor$, null, null, (short)-1) {
            public Anything $call$(java.lang.Object arg0) {
                sb.add((ceylon.language.Integer)arg0);
                return null;
            }
        };
        try {
            run(main, new Class[]{Callable.class}, new Object[]{c}, getDestModuleWithArtifact());
            Assert.fail("Expected execution to throw " + UnresolvedCompilationError.class.getName());
        } catch (RuntimeException e) {
            Throwable e2 = e;
            while (e2.getCause() != null) {
                e2 = e2.getCause();
            }
            if (e2 instanceof UnresolvedCompilationError) {
                Assert.assertEquals(expectedError, e2.getMessage());
            } else {
                throw e;
            }
            
        }
        ArrayList<ceylon.language.Integer> expect = new ArrayList<ceylon.language.Integer>();
        for (int ii : sequence) {
            expect.add(ceylon.language.Integer.instance(ii));
        }
        Assert.assertEquals("Sequences differ", expect, sb);
    }
    
    protected void run(String main,
            int... sequence) {
        final ArrayList<ceylon.language.Integer> sb= new ArrayList<ceylon.language.Integer>();
        Callable<Anything> c = new AbstractCallable<Anything>(Anything.$TypeDescriptor$, null, null, (short)-1) {
            public Anything $call$(java.lang.Object arg0) {
                sb.add((ceylon.language.Integer)arg0);
                return null;
            }
        };
        run(main, new Class[]{Callable.class}, new Object[]{c}, getDestModuleWithArtifact());
        ArrayList<ceylon.language.Integer> expect = new ArrayList<ceylon.language.Integer>();
        for (int ii : sequence) {
            expect.add(ceylon.language.Integer.instance(ii));
        }
        Assert.assertEquals("Sequences differ", expect, sb);
    }

    private ErrorCollector compileIgnoringCeylonErrors(String... ceylon) {
        ErrorCollector c = new ErrorCollector();
        getCompilerTask(defaultOptions, c, ceylon).call2();
        Assert.assertTrue("Expected only ceylon errors: " + c.getAssertionFailureMessage(), 0 == c.getNumBackendErrors());
        return c;
    }
    
    private void checkClassHasCompileTimeErrorAnnotation(String brokenClass) {
        synchronized(RUN_LOCK){
            try {
                NonCachingURLClassLoader classLoader = getClassLoader(brokenClass, getDestModuleWithArtifact());
                try {
                    Class<?> c = Class.forName(brokenClass, false, classLoader);
                    Assert.assertTrue("class lacks @CompileTimeError", c.isAnnotationPresent((CompileTimeError.class)));
                } finally {
                    classLoader.clearCache();
                    classLoader.close();
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
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

    @Test
    public void testRcvIndirectUser(){
        compile(1, "BrokenIndirectUser.ceylon");
    }
    
        
    @Test
    public void testRcvDeclarationRecoveryAnnotationConstructor(){
        compile(1,
                "declaration/DeclarationRecoveryAnnotationConstructor.ceylon");
        // TODO assert we didn't generate anything
    }
    
    @Test
    public void testRcvDeclarationRecoveryAnnotationConstructorArgument(){
        compile(1,
                "declaration/DeclarationRecoveryAnnotationConstructorArgument.ceylon");
        // TODO assert we didn't generate anything
    }
    
    @Test
    public void testRcvDeclarationRecoveryClassExtends(){
        // c.f. ExpressionRecoveryClassExtends: Error with the extended type is
        // a declaration error, error with the arguments is an expression error
        compile(1,
                "declaration/DeclarationRecoveryClassExtends.ceylon");
        // TODO assert we didn't generate anything
    }
    
    @Test
    public void testRcvExpressionRecoveryClassDp(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassDp.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassDp",
                "function or value does not exist: asdfClassDp");
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassDp");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassExtends(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassExtends.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassExtends",
                "no matching parameter declared by Basic: Basic has 0 parameters");
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassExtends");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassExtendsMissingParens(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassExtendsMissingParens.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassExtendsMissingParens",
                "missing invocation expression");
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassExtendsMissingParens");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassInitValueInit() {
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassInitValueInit.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitValueInit",
                "function or value does not exist: asdfClassInitValueInit", 
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitValueInit");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassInitVariableValueInit(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassInitVariableValueInit.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitVariableValueInit",
                "function or value does not exist: asdfClassInitVariableValueInit", 
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitVariableValueInit");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassInitValueSpecifier(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassInitValueSpecifier.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitValueSpecifier",
                "function or value does not exist: asdfClassInitValueSpecifier", 
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitValueSpecifier");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassInitValueSpecifierDeferred(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassInitValueSpecifierDeferred.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitValueSpecifierDeferred",
                "function or value does not exist: asdfClassInitValueSpecifierDeferred", 
                1, 2);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitValueSpecifierDeferred");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassInitIf(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassInitIf.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitIf",
                "function or value does not exist: asdfClassInitIf", 
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassInitIf");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassMethodDp(){
        compileIgnoringCeylonErrors(
                "expression/ExpressionRecoveryClassMethodDp.ceylon");
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryClassMethodDp_throw",
                "function or value does not exist: asdfClassMethodDp", 
                1, 2);
        run("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryClassMethodDp_nothrow",
                1, 2, 5, 10);
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryClassMethodDp_throw2",
                "function or value does not exist: asdfClassMethodDp",
                1, 2, 3);
        run("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryClassMethodDp_nothrow2",
                1, 2, 3, 5, 10);
        
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassMethodDp");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassMethodBody(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassMethodBody.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryClassMethodBody_main",
                "function or value does not exist: asdfClassMethodBody", 
                1, 2, 3);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassMethodBody");
    }

    @Test
    public void testRcvExpressionRecoveryClassMethodSpecifier(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassMethodSpecifier.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryClassMethodSpecifier_main",
                "function or value does not exist: asdfClassMethodSpecifier", 
                1, 2);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassMethodSpecifier");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassMethodSpecifierDeferred(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassMethodSpecifierDeferred.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryClassMethodSpecifierDeferred_main",
                "function or value does not exist: asdfClassMethodSpecifierDeferred",
                // in the deferred case we throw when an instance is initialized
                // rather than when the method is called.
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassMethodSpecifierDeferred");
    }
    @Test
    public void testRcvExpressionRecoveryClassAttributeBody(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassAttributeBody.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryClassAttributeBody_main",
                "function or value does not exist: asdfClassAttributeBody", 
                1, 2, 3);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassAttributeBody");
    }
    
    @Test
    public void testRcvExpressionRecoveryClassSetterBody(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryClassSetterBody.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryClassSetterBody_main",
                "value type could not be inferred", 
                1, 2, 3, 4, 5);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryClassSetterBody");
    }
    
    @Test
    public void testRcvExpressionRecoveryInterfaceMethodDp(){
        compileIgnoringCeylonErrors(
                "expression/ExpressionRecoveryInterfaceMethodDp.ceylon");
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryInterfaceMethodDp_throw",
                "function or value does not exist: asdfInterfaceMethodDp", 
                1, 2);
        run("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryInterfaceMethodDp_nothrow",
                1, 2, 5, 10);
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryInterfaceMethodDp_throw2",
                "function or value does not exist: asdfInterfaceMethodDp",
                1, 2, 3);
        run("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryInterfaceMethodDp_nothrow2",
                1, 2, 3, 5, 10);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryInterfaceMethodDp");
    }
    
    @Test
    public void testRcvExpressionRecoveryInterfaceMethodBody(){
        compileIgnoringCeylonErrors(
                "expression/ExpressionRecoveryInterfaceMethodBody.ceylon");
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryInterfaceMethodBody_main",
                "function or value does not exist: asdfInterfaceMethodBody", 
                1, 2, 3);
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryInterfaceMethodBody_main2",
                "function or value does not exist: asdfInterfaceMethodBody", 
                1, 2, 3);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryInterfaceMethodBody");
    }
    
    @Test
    public void testRcvExpressionRecoveryInterfaceAttributeBody(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryInterfaceAttributeBody.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryInterfaceAttributeBody_main",
                "function or value does not exist: asdfInterfaceAttributeBody", 
                1, 2, 3);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryInterfaceAttributeBody");
    }
    
    @Test
    public void testRcvExpressionRecoveryInterfaceSetterBody(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryInterfaceSetterBody.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryInterfaceSetterBody_main",
                "value type could not be inferred", 
                1, 2, 3, 4, 5);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.ExpressionRecoveryInterfaceSetterBody");
    }
    
    @Test
    public void testRcvExpressionRecoveryFunctionDp(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryFunctionDp.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionDp",
                "function or value does not exist: asdfFunctionDp");
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionDp_");
    }
    
    @Test
    public void testRcvExpressionRecoveryFunctionBody(){
        compileIgnoringCeylonErrors(
                "expression/ExpressionRecoveryFunctionBody.ceylon");
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionBody",
                "function or value does not exist: asdfFunctionBody",
                1);
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionBodyMpl_main",
                "function or value does not exist: asdfFunctionBodyMpl",
                1,2);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionBody_");
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionBodyMpl_");
    }
    
    @Test
    public void testRcvExpressionRecoveryFunctionSpecifier(){
        compileIgnoringCeylonErrors(
                "expression/ExpressionRecoveryFunctionSpecifier.ceylon");
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionSpecifier",
                "function or value does not exist: asdfFunctionSpecifier");
        runWithUnresolvedCompilationError(
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionSpecifierMpl_main",
                "function or value does not exist: asdfFunctionSpecifierMpl");
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionSpecifier_");
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryFunctionSpecifierMpl_");
    }
    
    @Test
    public void testRcvExpressionRecoveryValueBody(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryValueBody.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryValueBody_main",
                "function or value does not exist: asdfValueBody",
                1,2);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryValueBody_");
    }
    
    @Test
    public void testRcvExpressionRecoveryValueSpecifier(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryValueSpecifier.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryValueSpecifier_main",
                "function or value does not exist: asdfValueSpecifier",
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryValueSpecifier_");
    }
    
    @Test
    public void testRcvExpressionRecoveryValueInit(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryValueInit.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryValueInit_main",
                "function or value does not exist: asdfValueInit",
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryValueInit_");
    }
    
    @Test
    public void testRcvExpressionRecoveryVariableValueInit(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryVariableValueInit.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryVariableValueInit_main",
                "function or value does not exist: asdfVariableValueInit", 
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryVariableValueInit_");
    }
    
    @Test
    public void testRcvExpressionRecoverySetterBody(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoverySetterBody.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoverySetterBody_main",
                "function or value does not exist: asdfSetterBody",
                1, 2);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoverySetterBody_");
    }
    
    @Test
    public void testRcvExpressionRecoverySetterSpecifier(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoverySetterSpecifier.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoverySetterSpecifier_main",
                "function or value does not exist: asdfSetterSpecifier",
                1,2);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoverySetterSpecifier_");
    }
    
    @Test
    public void testRcvExpressionRecoveryIf(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryIf.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryIf",
                "function or value does not exist: asdfIf",
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryIf_");
    }
    
    @Test
    public void testRcvExpressionRecoveryElseIf(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryElseIf.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryElseIf",
                "function or value does not exist: asdfElseIf",
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryElseIf_");
    }
    
    @Test
    public void testRcvExpressionRecoveryIfBlock(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryIfBlock.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryIfBlock",
                "function or value does not exist: asdfIfBlock",
                1, 2);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryIfBlock_");
    }
    
    @Test
    public void testRcvExpressionRecoveryForClause(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryForClause.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryForClause",
                "function or value does not exist: asdfForClause",
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryForClause_");
    }
    
    @Test
    public void testRcvExpressionRecoveryAssertVariable(){
        compileAndRunWithUnresolvedCompilationError(
                "expression/ExpressionRecoveryAssertVariable.ceylon",
                "com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryAssertVariable",
                "function or value does not exist: baz", 
                1);
        checkClassHasCompileTimeErrorAnnotation("com.redhat.ceylon.compiler.java.test.recovery.expression.expressionRecoveryAssertVariable_");
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
                new CompilerError(1, "package not found in imported modules: java.lang (define a module and add module import to its module descriptor)"),
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
                new CompilerError(12, "imported declaration not found: newServer_bogus"),
                new CompilerError(30, "function or value does not exist: newServer_bogus"),
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
            assertTrue("Module created car: "+module, carFile.exists());
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