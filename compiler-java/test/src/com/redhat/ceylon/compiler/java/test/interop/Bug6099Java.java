package com.redhat.ceylon.compiler.java.test.interop;

public class Bug6099Java {
    public class innerClass {}
    public interface innerInterface {}
    
    public void Method(){}
    public static void StaticMethod(){}
    
    public Integer InstanceField = 2;
    public static final Integer StaticConstant = 2;
    public static final Integer STATIC_CONSTANT_ALT = 2;
}

class bug6099JavaTopLevelClass {}
interface bug6099JavaTopLevelInterface {}
