package com.redhat.ceylon.compiler.java.test.interop;

public class JavaBean {
    public boolean getBooleanWithGet(){ return false; }
    public void setBooleanWithGet(boolean b){}

    public boolean isBooleanWithIs(){ return false; }
    public void setBooleanWithIs(boolean b){}
    
    public boolean oldStyle(){ return false; }
    public void setOldStyle(boolean b){}
}
