package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.finished_;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * Base class for the iterables used for iterable instantiation and the 
 * sequenced argument of are named argument invocation, where the expressions 
 * in the iterable are evaluated lazily. 
 */
public abstract class LazyIterable<Element, Absent> extends AbstractIterable<Element, Absent>{
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
        return new AbstractIterator<Element>($reifiedElement){
            
            int index = 0;
            Iterator<? extends Element> rest = null;
            
            @Override
            public Object next() {
                if (rest != null) {
                    return rest.next();
                } 
                if (index >= $numExpressions) {
                    return finished_.get_();
                }
                Object result = $evaluate$(index++);
                if ($spread && index == $numExpressions) {
                    rest = ((Iterable)result).iterator();
                    result = rest.next();
                }
                return result;
            }
        };
    }
    
    /**
     * Evaluate the expression at the given index.
     */
    @Ignore
    protected abstract Object $evaluate$(int $index$);
}
