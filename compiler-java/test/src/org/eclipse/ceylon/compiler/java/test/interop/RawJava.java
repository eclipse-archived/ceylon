package org.eclipse.ceylon.compiler.java.test.interop;

import java.util.Collection;

interface RawJava1<E> {
    int getFoo(RawJava1<? extends E> list, E arg);
}

class RawJava2 implements RawJava1 {
    public int getFoo(RawJava1 list, Object arg) { return 1; }
}

class RawJavaA<T> { }

interface RawJavaB {
    void method(Collection<RawJavaA> a);
}
