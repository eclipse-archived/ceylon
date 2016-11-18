package com.redhat.ceylon.compiler.java.wrapping;

import java.io.Serializable;
import java.util.AbstractList;

import com.redhat.ceylon.compiler.java.Util;

import ceylon.language.List;

/**
 * A wrapper for a Ceylon List that satisfies {@code java.util.List} 
 */
class WrappedCeylonList<In,Out> 
        extends AbstractList<Out>
        implements Serializable {

    private static final long serialVersionUID = -7464247133391425179L;
    
    private List<In> cList;
    private Wrapping<In, Out> elementWrapping;

    public WrappedCeylonList(ceylon.language.List<In> cList, Wrapping<In, Out> elementWrapping) {
        this.cList = cList;
        this.elementWrapping = elementWrapping;
    }
    
    @Override
    public Out get(int index) {
        return elementWrapping.wrap(cList.getFromFirst(index));
    }

    @Override
    public int size() {
        return Util.toInt(cList.getSize());
    }
    
    public ceylon.language.List<In> unwrap() {
        return cList;
    }
}