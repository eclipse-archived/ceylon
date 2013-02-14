package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.ReifiedType;
import com.redhat.ceylon.compiler.java.TypeDescriptor;
import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

import ceylon.language.Iterator;
import ceylon.language.Iterator$impl;

public abstract class AbstractIterator<Element> implements Iterator<Element>, ReifiedType {
    
    @Ignore
    private TypeDescriptor $reifiedElement;

    public AbstractIterator(TypeDescriptor $reifiedElement) {
        this.$reifiedElement = $reifiedElement;
    }
    
    // our subclasses are generated classes for comprehensions that never need their superinterface because
    // it's not visible to user code
    @Ignore
    @Override
    public Iterator$impl<Element> $ceylon$language$Iterator$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Override
    public boolean $is(TypeDescriptor type) {
        // FIXME: implement me
        throw new RuntimeException("Not implemented");
    }
}
