package org.eclipse.ceylon.compiler.java.wrapping;

import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.wrapping.WrappedJavaIterator;
import org.eclipse.ceylon.compiler.java.wrapping.Wrapping;

import ceylon.language.Iterator;
import ceylon.language.impl.BaseSet;

/**
 * A wrapper for a Java List that satisfies {@code ceylon.language.List}
 */

class WrappedJavaSet<In,Out> extends BaseSet<Out> {

    private static final long serialVersionUID = 1L;
    private final java.util.Set<In> jSet;
    private final TypeDescriptor $reified$Element;
    private final Wrapping<In, Out> wrapping;
    
    public WrappedJavaSet(TypeDescriptor $reified$Element, java.util.Set<In> jSet, Wrapping<In, Out> wrapping) {
        super($reified$Element);
        this.$reified$Element = $reified$Element;
        this.jSet = jSet;
        this.wrapping = wrapping;
    }

    @Override
    public Iterator<? extends Out> iterator() {
        return new WrappedJavaIterator<Out,In>($reified$Element, jSet, wrapping);
    }
    
    java.util.Set<In> unwrap() {
        return jSet;
    }
}