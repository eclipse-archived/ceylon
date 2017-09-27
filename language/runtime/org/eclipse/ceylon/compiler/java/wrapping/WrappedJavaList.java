package org.eclipse.ceylon.compiler.java.wrapping;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.wrapping.Wrapping;

import ceylon.language.Integer;
import ceylon.language.impl.BaseList;

/**
 * A wrapper for a Java List that satisfies {@code ceylon.language.List}
 */
class WrappedJavaList<In,Out> extends BaseList<Out> {

    private static final long serialVersionUID = 1L;
    private final java.util.List<In> jList;
    private final Wrapping<In,Out> elementWrapping;

    public WrappedJavaList(TypeDescriptor $reified$Element, java.util.List<In> jList, Wrapping<In,Out> elementWrapping) {
        super($reified$Element);
        this.jList = jList;
        this.elementWrapping = elementWrapping;
    }

    @Override
    public Out getFromFirst(long arg0) {
        return elementWrapping.wrap(jList.get(Util.toInt(arg0)));
    }

    @Override
    public Integer getLastIndex() {
        return Integer.instance(jList.size()-1);
    }
    
    public java.util.List<In> unwrap() {
        return jList;
    }
}