package com.redhat.ceylon.compiler.java.test.interop;

public interface Bug2053Varargs<X extends Number> {
    public <T> void unbounded(T... ts);
    public <T extends Number> void bound(T... ts);
    public <T extends Top> T bound2(T... ts);
    public <T extends X> void indirectBound(T... ts);
    
}

interface Top {}
interface Left extends Top{
    public void left();
}
interface Right extends Top{
    public void right();
}