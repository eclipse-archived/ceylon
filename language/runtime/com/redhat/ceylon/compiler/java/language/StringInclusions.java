package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Integer;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class
public class StringInclusions 
extends BaseIterable<Integer,java.lang.Object> {
    
    private final java.lang.String str;
    private final java.lang.String oc;

    public StringInclusions(java.lang.String str, 
            java.lang.String oc) {
        super(ceylon.language.String.$TypeDescriptor$, 
                Null.$TypeDescriptor$);
        this.str = str;
        this.oc = oc;
    }

    class InclusionIterator 
    extends BaseIterator<Integer> {

        public InclusionIterator() {
            super(Integer.$TypeDescriptor$);
        }

        private int pos=0;

        @Override
        public java.lang.Object next() {
            int idx = str.indexOf(oc, pos);
            if (idx >= pos) {
                pos = idx+oc.length();
                return Integer.instance(idx);
            }
            else {
                return finished_.get_();
            }
        }

        @Override
        @Ignore
        public TypeDescriptor $getType$() {
            return TypeDescriptor.klass(InclusionIterator.class);
        }

    }

    @Override
    public Iterator<? extends Integer> iterator() {
        return new InclusionIterator();
    }

    @Override
    public boolean getEmpty() {
        return iterator().next() == finished_.get_();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(StringInclusions.class);
    }
    
}