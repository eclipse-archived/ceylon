package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Iterator;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;

/**
 * Base class for the iterables used for enumerated stream
 * instantiation and the sequenced argument of a named 
 * argument invocation, where the listed expressions in the 
 * stream are compile time constants (literals), therefore 
 * we know their evaluation has no side-effects:
 * 
 *     {1, 2, "three", *rest}
 * 
 * @see LazyIterable 
 */
public final class ConstantIterable<Element, Absent> 
        extends BaseIterable<Element, Absent> {
    
    private final TypeDescriptor $reified$Element;
    private final Element[] initial;
    private final ceylon.language.Iterable<Element, ? extends Object> rest;
    
    public ConstantIterable(TypeDescriptor $reified$Element, TypeDescriptor $reified$Absent, ceylon.language.Iterable<Element, ? extends Object> rest, Element... initial) {
        super($reified$Element, $reified$Absent);
        this.$reified$Element = $reified$Element;
        this.initial = initial;
        this.rest = rest;
    }
    
    @Override
    public Iterator<? extends Element> iterator() {
        return new BaseIterator<Element>($reified$Element) {
            private int index = 0;
            private ceylon.language.Iterator<? extends Element> restIter;
            @Override
            public Object next() {
                if (initial != null
                        && index < initial.length) {
                    Element result = initial[index];
                    index++;
                    return result;
                } else if (rest != null) {
                    if (restIter == null) {
                        restIter = rest.iterator();
                    }
                    return restIter.next();
                } else {
                    return finished_.get_();
                }
            }
        };
    }
    
    @Override
    public boolean getEmpty() {
        return (initial == null || initial.length == 0)
                && (rest == null || rest.getEmpty());
    }
    
    @Override
    public long getSize() {
        return (initial == null ? 0 :  initial.length) + (rest == null ? 0 : rest.getSize());
    }
    
    @Override
    public boolean longerThan(long length) {
        if (rest == null) {
            return initial != null && initial.length > length;
        } else {
            return rest.longerThan(length-(initial == null ? 0 : initial.length));
        }
    }
    
    @Override
    public boolean shorterThan(long length) {
        if (rest == null) {
            return initial != null && initial.length < length;
        } else {
            return rest.shorterThan(length-(initial == null ? 0 : initial.length));
        }
    }
    
    protected Object writeReplace() {
        return sequence();
    }

}
