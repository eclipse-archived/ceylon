package com.redhat.ceylon.compiler.test.statement.trycatch;

import java.io.IOException;

public class JavaThrower {

    JavaThrower() {
        
    }
    
    public long throwException() throws Exception {
        throw new Exception();
    }
    
    public long throwThrowable() throws Throwable {
        throw new Throwable();
    }
    
    public long throwRuntimeException() throws RuntimeException {
        throw new RuntimeException();
    }
    
    public long throwError() throws Error {
        throw new Error();
    }
    
    public long throwsMultiple() throws ClassNotFoundException, IOException {
        return 0;
    }
    
}
