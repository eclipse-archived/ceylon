package com.redhat.ceylon.compiler.java.language;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.finished_;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public abstract class LazyIterable<Element, Absent> extends AbstractIterable<Element, Absent>{
    private static final MethodType methodType = MethodType.methodType(Object.class);
    private final TypeDescriptor $reifiedElement;
    private final int $numMethods;
    private final boolean $spread;

    public LazyIterable(
            @Ignore TypeDescriptor $reifiedElement, 
            @Ignore TypeDescriptor $reifiedAbsent, 
            int $numMethods, 
            boolean $spread) {
        super($reifiedElement, $reifiedAbsent);
        this.$reifiedElement = $reifiedElement;
        this.$numMethods = $numMethods;
        this.$spread = $spread;
    }
    // We need the (anonymous) subclass to obtain the lookup for us
    protected abstract MethodHandles.Lookup lookup(); 
    
    @Override
    public Iterator<? extends Element> iterator() {
        return new AbstractIterator($reifiedElement){
            
            int index = 0;
            Iterator<? extends Element> rest = null;
            final MethodHandles.Lookup lookup = lookup();
            Class<? extends LazyIterable> subclass = LazyIterable.this.getClass();
            
            @Override
            public Object next() {
                if (rest != null) {
                    return rest.next();
                } 
                if (index >= $numMethods) {
                    return finished_.get_();
                }
                java.lang.String methodName = "$"+index;
                index++;
                Object result;
                try {
                    MethodHandle handle = lookup.findSpecial(subclass, methodName, methodType, subclass);
                    // by delegating to the (anonymous) subclass `this` will 
                    // have the subclasses type and it can use invokeExact(), where
                    // this class could not (because we can't cast or even name the type of this)
                    result = invoke(handle);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
                if ($spread && index == $numMethods) {
                    rest = ((Iterable)result).iterator();
                    result = rest.next();
                }
                return result;
            }
        };
    }
    
    protected abstract Object invoke(MethodHandle handle) throws Throwable;
}
