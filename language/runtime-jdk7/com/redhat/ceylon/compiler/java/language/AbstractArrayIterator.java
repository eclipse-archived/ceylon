package com.redhat.ceylon.compiler.java.language;

import ceylon.language.finished_;
import ceylon.language.impl.BaseIterator;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public abstract class AbstractArrayIterator<Element> 
        extends BaseIterator<Element> {
    
    private final int start;
    private final int len;
    private final int step;
    private int i = 0;

    public AbstractArrayIterator(
    		TypeDescriptor $reified$Element, 
    		int start, int len, int step) {
    	super($reified$Element);
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
        }
        else {
            return finished_.get_();
        }
    }
    
    protected abstract Element get(int index);
}