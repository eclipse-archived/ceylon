package com.redhat.ceylon.compiler.java.test.recovery;

import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class RecoveryTest extends CompilerTest {
    
    @Test
    public void testRcvBrokenClass(){
        compile(3, "BrokenClass.ceylon");
    }

    @Test
    public void testRcvBrokenMethod(){
        compile(4, "BrokenMethod.ceylon");
    }

    @Test
    public void testRcvBrokenAttribute(){
        compile(14, "BrokenAttribute.ceylon");
    }

    @Test
    public void testRcvClassWithBrokenMembers(){
        compile(50, "ClassWithBrokenMembers.ceylon");
    }

    @Test
    public void testRcvDuplicateDeclarations(){
        // this is https://github.com/ceylon/ceylon-compiler/issues/250
        compile(2, "DuplicateDeclaration1.ceylon", "DuplicateDeclaration2.ceylon");
    }

    @Test
    public void testRcvM2Features(){
        compile(1, "M2Features.ceylon");
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

}