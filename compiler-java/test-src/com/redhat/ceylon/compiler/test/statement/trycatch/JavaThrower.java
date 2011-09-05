package com.redhat.ceylon.compiler.test.statement.trycatch;

import java.io.IOException;

public class JavaThrower {

    JavaThrower() {
        
    }
    
    public boolean throwException() throws Exception {
        throw new Exception();
    }
    
    public boolean throwThrowable() throws Throwable {
        throw new Throwable();
    }
    
    public boolean throwRuntimeException() throws RuntimeException {
        throw new RuntimeException();
    }
    
    public boolean throwError() throws Error {
        throw new Error();
    }
    
    public boolean throwsMultiple() throws ClassNotFoundException, IOException {
        return false;
    }
    
}
