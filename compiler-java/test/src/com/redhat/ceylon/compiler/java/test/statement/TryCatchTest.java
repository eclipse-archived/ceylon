package com.redhat.ceylon.compiler.java.test.statement;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class TryCatchTest extends CompilerTest {

    @Override
    protected String transformDestDir(String name) {
        return name + "-trycatch";
    }
    
    protected ModuleWithArtifact getDestModuleWithArtifact() {
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.statement.trycatch", "1");
    }

    @Test
    public void testTryExceptionTypes(){
        compareWithJavaSource("trycatch/ExceptionTypes");
    }
    
    @Test
    public void testTryExceptionAttr(){
        compareWithJavaSource("trycatch/ExceptionAttr");
    }
    
    @Test
    public void testTryExceptionAttributes(){
        compareWithJavaSource("trycatch/ExceptionAttributes");
    }
    
    @Test
    public void testTryBareThrow(){
        compareWithJavaSource("trycatch/Throw");
    }
    
    @Test
    public void testTryThrowException(){
        compareWithJavaSource("trycatch/ThrowException");
    }
    
    @Test
    public void testTryThrowExceptionNamedArgs(){
        compareWithJavaSource("trycatch/ThrowExceptionNamedArgs");
    }
    
    @Test
    public void testTryThrowExceptionSubclass(){
        compareWithJavaSource("trycatch/ThrowExceptionSubclass");
    }
    
    @Test
    public void testTryThrowMethodResult(){
        compareWithJavaSource("trycatch/ThrowMethodResult");
    }
    
    @Test
    public void testTryThrowThrowable(){
        compareWithJavaSource("trycatch/ThrowThrowable");
    }
    
    @Test
    public void testTryThrowNpe(){
        compareWithJavaSource("trycatch/ThrowNpe");
    }
    
    @Test
    public void testTryTryFinally(){
        compareWithJavaSource("trycatch/TryFinally");
    }
    
    @Test
    public void testTryTryCatch(){
        compareWithJavaSource("trycatch/TryCatch");
    }
    
    @Test
    public void testTryTryCatchFinally(){
        compareWithJavaSource("trycatch/TryCatchFinally");
    }
    
    @Test
    public void testTryTryCatchSubclass(){
        compareWithJavaSource("trycatch/TryCatchSubclass");
    }
    
    @Test
    public void testTryTryCatchUnion(){
        compareWithJavaSource("trycatch/TryCatchUnion");
    }
    
    @Test
    public void testTryTryCatchGenericIntersection(){
        compareWithJavaSource("trycatch/TryCatchGenericIntersection");
        //compile("trycatch/TryCatchGenericIntersection.ceylon");
        run("com.redhat.ceylon.compiler.java.test.statement.trycatch.tryCatchGenericIntersection");
    }
    
    @Test
    public void testTryTryWithResource(){
        compareWithJavaSource("trycatch/TryWithResource");
    }
    
    @Test
    public void testTryReplaceExceptionAtJavaCallSite(){
        compareWithJavaSource("trycatch/WrapExceptionAtJavaCallSite");
    }

}
