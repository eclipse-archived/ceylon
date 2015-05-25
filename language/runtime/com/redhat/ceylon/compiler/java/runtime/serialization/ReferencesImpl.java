package com.redhat.ceylon.compiler.java.runtime.serialization;

import ceylon.language.Anything;
import ceylon.language.Array;
import ceylon.language.Entry;
import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;
import ceylon.language.meta.declaration.ClassDeclaration;
import ceylon.language.serialization.Element;
import ceylon.language.serialization.ReachableReference;
import ceylon.language.serialization.References;

import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public class ReferencesImpl extends BaseIterable<Entry<? extends ReachableReference, ? extends Object>, java.lang.Object> implements References {

    private final Serializable instance;

    ReferencesImpl(Serializable instance) {
        super(TypeDescriptor.klass(Entry.class, 
                    ReachableReference.$TypeDescriptor$, Anything.$TypeDescriptor$), 
                Null.$TypeDescriptor$);
        this.instance = instance;
    }
    
    @Override
    public Iterator<? extends Entry<? extends ReachableReference, ? extends Object>> iterator() {
        return new BaseIterator<Entry<? extends ReachableReference, ? extends Object>>(TypeDescriptor.klass(Entry.class, ReachableReference.$TypeDescriptor$, Anything.$TypeDescriptor$)) {
            Iterator<? extends ReachableReference> it = getReferences().iterator();
            @Override
            public Object next() {
                java.lang.Object next = it.next();
                if (next == finished_.get_()) {
                    return finished_.get_();
                } else {
                    ReachableReference ref = (ReachableReference)next;
                    return new Entry<ReachableReference, Object>(
                            ((ReifiedType)ref).$getType$(), Anything.$TypeDescriptor$, 
                            ref, ref.referred(instance));
                }
            }
            
        };
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    @Override
    public Iterable<? extends ReachableReference, ? extends Object> getReferences() {
        return new BaseIterable<ReachableReference, Object>(ReachableReference.$TypeDescriptor$, Null.$TypeDescriptor$) {

            @Override
            public Iterator<? extends ReachableReference> iterator() {
                return new BaseIterator<ReachableReference>(ReachableReference.$TypeDescriptor$) {
                    java.util.Iterator<ReachableReference> it = instance.$references$().iterator();
                    int index = 0;
                    @Override
                    public Object next() {
                        if (it.hasNext()) {
                            return it.next();
                        } else if (instance instanceof Array
                                && index < ((Array<?>)instance).getSize()) {
                            Element result = new ElementImpl(index);
                            index++;
                            return result;
                        } else {
                            return finished_.get_();
                        }
                    }
                };
            }
        };
    }
/*
    @Override
    public Object reference(ReachableReference ref) {
        return ref.reference(instance);
        /*if (ref instanceof Member) {
            return instance.$get$(((Member)ref).getAttribute().getQualifiedName());
        } else if (ref instanceof Element) {
            return instance.$get$((int)((Element)ref).getIndex());
        } else if (ref instanceof Outer) {
            return instance.$get$(null);
        } else {
            throw new AssertionError();
        }* /
    }
*/
}