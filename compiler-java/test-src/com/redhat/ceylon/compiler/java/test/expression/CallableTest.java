package com.redhat.ceylon.compiler.java.test.expression;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class CallableTest extends CompilerTest {

    @Test
    public void testCallableArgumentNullary(){
        compareWithJavaSource("invoke/CallableArgumentNullary");
    }
    
    @Test
    public void testCallableArgumentUnary(){
        compareWithJavaSource("invoke/CallableArgumentUnary");
    }
    
    @Test
    public void testCallableArgumentBinary(){
        compareWithJavaSource("invoke/CallableArgumentBinary");
    }
    
    @Test
    public void testCallableArgumentTernary(){
        compareWithJavaSource("invoke/CallableArgumentTernary");
    }
    
    @Test
    public void testCallableArgumentNary(){
        compareWithJavaSource("invoke/CallableArgumentNary");
    }
    
    @Test
    public void testCallableArgumentParameterClass(){
        compareWithJavaSource("invoke/CallableArgumentParameterClass");
    }
    
    @Test
    public void testCallableArgumentParameterQualified(){
        compareWithJavaSource("invoke/CallableArgumentParameterQualified");
    }
    
    @Test
    public void testCallableArgumentParameterTypeParam(){
        compareWithJavaSource("invoke/CallableArgumentParameterTypeParam");
    }
    
    @Test
    @Ignore("Awaiting support for parameter bounds")
    public void testCallableArgumentParameterCtor(){
        compareWithJavaSource("invoke/CallableArgumentParameterCtor");
    }
    
    @Test
    public void testCallableArgumentPassed(){
        compareWithJavaSource("invoke/CallableArgumentPassed");
    }
    
    @Test
    public void testCallableArgumentReturningInteger(){
        compareWithJavaSource("invoke/CallableArgumentReturningInteger");
    }
    
    @Test
    public void testCallableArgumentReturningClass(){
        compareWithJavaSource("invoke/CallableArgumentReturningClass");
    }
    
    @Test
    public void testCallableArgumentReturningQualifiedClass(){
        compareWithJavaSource("invoke/CallableArgumentReturningQualifiedClass");
    }
    
    @Test
    public void testCallableArgumentReturningTypeParam(){
        compareWithJavaSource("invoke/CallableArgumentReturningTypeParam");
    }
    
    @Test
    public void testCallableReturnNullary(){
        compareWithJavaSource("invoke/CallableReturnNullary");
    }
    
    @Test
    public void testCallableReturnUnary(){
        compareWithJavaSource("invoke/CallableReturnUnary");
    }
    
    @Test
    public void testCallableReturnBinary(){
        compareWithJavaSource("invoke/CallableReturnBinary");
    }
    
    @Test
    public void testCallableReturnTernary(){
        compareWithJavaSource("invoke/CallableReturnTernary");
    }
    
    @Test
    public void testCallableReturnNary(){
        compareWithJavaSource("invoke/CallableReturnNary");
    }
    
    @Test
    public void testCallableReturnCallable(){
        compareWithJavaSource("invoke/CallableReturnCallable");
    }
    
    @Test
    public void testCallableReturnMethod(){
        compareWithJavaSource("invoke/CallableReturnMethod");
    }
    
    @Test
    public void testCallableReturnReturningInteger(){
        compareWithJavaSource("invoke/CallableReturnReturningInteger");
    }
    
    @Test
    public void testCallableReturnReturningClass(){
        compareWithJavaSource("invoke/CallableReturnReturningClass");
    }
    
    @Test
    public void testCallablePositionalInvocationNullary(){
        compareWithJavaSource("invoke/CallablePositionalInvocationNullary");
    }
    
    @Test
    public void testCallablePositionalInvocationUnary(){
        compareWithJavaSource("invoke/CallablePositionalInvocationUnary");
    }
    
    @Test
    @Ignore("Awaiting fix for ceylon-spec#218")
    public void testCallablePositionalInvocationQualified(){
        compareWithJavaSource("invoke/CallablePositionalInvocationQualified");
    }
    
    @Test
    public void testCallableNamedInvocationNullary(){
        compareWithJavaSource("invoke/CallableNamedInvocationNullary");
    }
    
    @Test
    public void testCallableNamedInvocationUnary(){
        compareWithJavaSource("invoke/CallableNamedInvocationUnary");
    }
    
    @Test
    public void testCallableNamedInvocationBinary(){
        compareWithJavaSource("invoke/CallableNamedInvocationBinary");
    }
    
    @Test
    public void testCallableNamedInvocationNary(){
        compareWithJavaSource("invoke/CallableNamedInvocationNary");
    }

}