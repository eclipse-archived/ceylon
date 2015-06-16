package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/* Implement Iterable */
public final class ObjectArrayIterable<T> 
extends AbstractArrayIterable<T, Object[]> {

    public ObjectArrayIterable(Object[] array, int start, int len, int step) {
        super(TypeDescriptor.klass(array.getClass().getComponentType()), 
                array, start, len, step);
    }

    public ObjectArrayIterable(Object[] array, int length) {
        super(TypeDescriptor.klass(array.getClass().getComponentType()),
                array, length);
    }
    
    public ObjectArrayIterable(TypeDescriptor reifiedElement, Object[] array) {
        super(reifiedElement, array, array.length);
    }

    @Override
    protected ObjectArrayIterable<T> newInstance(Object[] array, int start,
            int len, int step) {
        return new ObjectArrayIterable<T>(array, start, len, step);
    }

    @Override
    protected T get(Object[] array, int index) {
        return (T)array[index];
    }
    
    @Override
    public ObjectArrayIterable<T> take(long take) {
        return (ObjectArrayIterable<T>)super.take(take);
    }
    
    @Override
    public ObjectArrayIterable<T> skip(long take) {
        return (ObjectArrayIterable<T>)super.skip(take);
    }
    
    @Override
    public ObjectArrayIterable<T> by(long take) {
        return (ObjectArrayIterable<T>)super.by(take);
    }
}