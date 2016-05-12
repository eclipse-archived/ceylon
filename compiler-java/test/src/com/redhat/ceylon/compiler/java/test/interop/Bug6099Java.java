package com.redhat.ceylon.compiler.java.test.interop;

class Bug6099JavaSuperClass {
    public static final int WIDTH3 = 1;
}

public class Bug6099Java extends Bug6099JavaSuperClass {
    public static class innerClass {}
    public interface innerInterface {}
    public interface innerInterface2 {}
    public interface innerInterface3 {}
    
    public void Method(){}
    public static void StaticMethod(){}
    
    public int InstanceField = 2;
    public static final int StaticConstant = 2;
    public static final int STATIC_CONSTANT_ALT = 2;
    public static final int STATIC_CONSTANT_ALT2 = 2;
    
    public static final int DERIVED = 1;
    public static void derived(){}
    public static final int DERIVED2 = 1;
    public boolean isDerived2(){ return true; }
    public void setDerived2(boolean d){ }
    public static final int DERIVED3 = 1;
    public static final boolean derived3 = true;
    
    public static final int STRING = 1;
    
    public int WIDTH;
    public boolean width(){ return true; }

    public int getWidth2() { return 1; }
    public boolean WIDTH2(){ return true; }
    
    public boolean getWidth3(){ return true; }
}

class bug6099JavaTopLevelClass {}
interface bug6099JavaTopLevelInterface {}
