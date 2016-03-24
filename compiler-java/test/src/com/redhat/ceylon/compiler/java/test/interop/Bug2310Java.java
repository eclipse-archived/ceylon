package com.redhat.ceylon.compiler.java.test.interop;

import java.util.Comparator;

public class Bug2310Java<T> {
    public Bug2310Java(Comparator<? super T> comp) {}
    public Bug2310Java() {}

    public <X> Bug2310Java<X> m(Comparator<? super X> comp) { return null; }
    public <X> Bug2310Java<X> m() {return null; }
}
