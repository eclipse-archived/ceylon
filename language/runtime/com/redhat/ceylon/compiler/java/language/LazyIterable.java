package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * Base class for the iterables used for iterable 
 * instantiation and the sequenced argument of are named 
 * argument invocation, where the expressions in the 
 * iterable are evaluated lazily. 
 */
public abstract class LazyIterable<Element, Absent> 
extends BaseIterable<Element, Absent>{
	
    private final class LazyIterator 
    extends BaseIterator<Element> {
    	
	    int index = 0;
	    Iterator<? extends Element> rest = null;
	    
	    private LazyIterator() {
		    super($reifiedElement);
	    }
	    
	    @SuppressWarnings({ "unchecked" })
	    @Override
	    public Object next() {
	        if (rest != null) {
	            return rest.next();
	        } 
	        else if (index >= $numExpressions) {
	            return finished_.get_();
	        }
	        else {
	        	Object result = $evaluate$(index++);
	        	if ($spread && index == $numExpressions) {
	        		Iterable<? extends Element, ?> iterable = 
	        				(Iterable<? extends Element,?>) result;
	        		rest = iterable.iterator();
	        		result = rest.next();
	        	}
	        	return result;
	        }
	    }
    }
    
	private final TypeDescriptor $reifiedElement;
    private final int $numExpressions;
    private final boolean $spread;
    
    public LazyIterable(
            @Ignore TypeDescriptor $reifiedElement, 
            @Ignore TypeDescriptor $reifiedAbsent, 
            int $numMethods, 
            boolean $spread) {
        super($reifiedElement, $reifiedAbsent);
        this.$reifiedElement = $reifiedElement;
        this.$numExpressions = $numMethods;
        this.$spread = $spread;
    }
    
    @Override
    public Iterator<? extends Element> iterator() {
        return new LazyIterator();
    }
    
    /**
     * Evaluate the expression at the given index.
     */
    @Ignore
    protected abstract Object $evaluate$(int $index$);
    
    @Override
    public boolean getEmpty() {
        if ($numExpressions == 0) {
            return true;
        }
        // we have at least one expression, but is it spread?
        else if ($spread) {
            // do we have at least one non-spread expression?
            return $numExpressions > 1 ? 
            		false : super.getEmpty(); // with spread we just don't know
        }
        else{
            // we have at least one non-spread expression
            return false;
        }
    }
    
    @Override
    public long getSize() {
        if ($spread) {
            return super.getSize(); // too hazardous
        }
        // safe
        return $numExpressions;
    }
    
    @Override
    public boolean longerThan(long length) {
        if ($spread && length >= $numExpressions) {
            return super.longerThan(length);
        }
        else {
        	return $numExpressions > length;
        }
    }
    
    @Override
    public boolean shorterThan(long length) {
        if ($spread && length >= $numExpressions) {
            return super.shorterThan(length);
        }
        else {
        	return $numExpressions < length;
        }
    }
}
