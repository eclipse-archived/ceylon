package com.redhat.ceylon.compiler.java.test.interop;

public class JavaBean {
    public boolean getBooleanWithGet(){ return false; }
    public void setBooleanWithGet(boolean b){}

    public boolean isBooleanWithIs(){ return false; }
    public void setBooleanWithIs(boolean b){}
    
    public boolean oldStyle(){ return false; }
    public void setOldStyle(boolean b){}
    
    public String getURL(){ return null; }
    public void setURL(String url){}

    public String getURLEncoderForHTML(){ return null; }
    public void setURLEncoderForHTML(String url){}
    
    public boolean getConfusedProperty(){ return false; }
    public void setConfusedProperty(String str){}
    
    public long getÉpardaud(){ throw new RuntimeException("I am not a number, I am a free man!"); }
    public void setÉpardaud(long l){}
}
