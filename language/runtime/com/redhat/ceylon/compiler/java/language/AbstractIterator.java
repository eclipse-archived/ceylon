package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

import ceylon.language.Iterator;
import ceylon.language.Iterator$impl;

public abstract class AbstractIterator<Element> implements Iterator<Element> {
    // our subclasses are generated classes for comprehensions that never need their superinterface because
    // it's not visible to user code
    @Ignore
    @Override
    public Iterator$impl<Element> $ceylon$language$Iterator$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }
}
