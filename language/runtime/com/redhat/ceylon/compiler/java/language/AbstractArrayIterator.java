package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Iterator;
import ceylon.language.finished_;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public abstract class AbstractArrayIterator<Element> 
        implements Iterator<Element> {
    
    private final int start;
    private final int len;
    private final int step;
    private int i = 0;

    public AbstractArrayIterator(TypeDescriptor $reified$Element, 
    		int start, int len, int step) {
        this.len = len;
        this.step = step;
        this.start = start;
    }
    
    @Override
    public Object next() {
        if (i < len) {
            Element result = get(start+i*step);
            i++;
            return result;
        } else {
            return finished_.get_();
        }
    }
    
    protected abstract Element get(int index);
}