package com.redhat.ceylon.compiler.java.test.interop;

public enum JavaEnum {
    
    ONE, TWO;
    
    public long field;
    public void method(){}
    public long getProperty(){ return 1; }
    public void setProperty(long p){}
}
