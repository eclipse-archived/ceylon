package com.redhat.ceylon.compiler.test.recovery;

import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

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
        compile(11, "BrokenAttribute.ceylon");
    }

    @Test
    public void testRcvClassWithBrokenMembers(){
        compile(35, "ClassWithBrokenMembers.ceylon");
    }

    private void compile(int expectedErrors, String... ceylon){
        DiagnosticCollector<JavaFileObject> errorCollector = new DiagnosticCollector<JavaFileObject>();
        Boolean success = getCompilerTask(defaultOptions, errorCollector , ceylon).call();
        Assert.assertEquals(expectedErrors, getErrorCount(errorCollector));
        Assert.assertFalse(success);
    }

    private int getErrorCount(DiagnosticCollector<JavaFileObject> errorCollector) {
        int errors = 0;
        for(Diagnostic<? extends JavaFileObject> diagnostic : errorCollector.getDiagnostics()){
            if(diagnostic.getKind() == Kind.ERROR)
                errors++;
            System.err.println("("+diagnostic.getKind()+") "+diagnostic.getSource().getName()
                    +"["+diagnostic.getLineNumber()+","+diagnostic.getColumnNumber()+"]: "
                    +diagnostic.getMessage(Locale.getDefault()));
        }
        return errors;
    }

}