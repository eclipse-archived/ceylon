package com.redhat.ceylon.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class ConstructorArgumentParser<T> implements ArgumentParser<T> {

    private Constructor<T> ctor;

    public ConstructorArgumentParser(Class<T> clazz) {
        try {
            this.ctor = clazz.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T parse(String argument) {
        try {
            return ctor.newInstance(argument);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            } else if (cause instanceof Error) {
                throw (Error)cause;
            } else {
                throw new RuntimeException(e);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
    
}
