package com.redhat.ceylon.compiler.metadata.java;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for generated Ceylon code that needs to call implementation logic.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class Util {
    
    /**
     * Returns true if the given object satisfies ceylon.language.Equality
     */
    public static boolean isEquality(java.lang.Object o){
        return satisfiesInterface(o, "ceylon.language.Equality");
    }
    
    /**
     * Returns true if the given object extends ceylon.language.IdentifiableObject
     */
    public static boolean isIdentifiableObject(java.lang.Object o){
        return extendsClass(o, "ceylon.language.IdentifiableObject");
    }
    
    /**
     * Returns true if the given object extends the given class
     */
    public static boolean extendsClass(java.lang.Object o, String className) {
        if(o == null)
            return false;
        if(className == null)
            throw new IllegalArgumentException("Type name cannot be null");
        return classExtendsClass(o.getClass(), className);
    }
    
    private static boolean classExtendsClass(java.lang.Class<?> klass, String className) {
        if(klass == null)
            return false;
        Class classAnnotation = klass.getAnnotation(Class.class);
        if(classAnnotation != null
                && classAnnotation.extendsType().equals(className))
            return true;
        return classExtendsClass(klass.getSuperclass(), className);
    }
    
    /**
     * Returns true if the given object satisfies the given interface
     */
    public static boolean satisfiesInterface(java.lang.Object o, String className){
        if(o == null)
            return false;
        if(className == null)
            throw new IllegalArgumentException("Type name cannot be null");
        // we use a hash set to speed things up for interfaces, to avoid looking at them twice
        Set<java.lang.Class<?>> alreadyVisited = new HashSet<java.lang.Class<?>>();
        return classSatisfiesInterface(o.getClass(), className, alreadyVisited);
    }

    private static boolean classSatisfiesInterface(java.lang.Class<?> klass, String className, 
            Set<java.lang.Class<?>> alreadyVisited) {
        if(klass == null)
            return false;
        // try the interfaces
        if(lookForInterface(klass, className, alreadyVisited))
            return true;
        // try its superclass
        return classSatisfiesInterface(klass.getSuperclass(), className, alreadyVisited);
    }

    private static boolean lookForInterface(java.lang.Class<?> klass, String className, 
            Set<java.lang.Class<?>> alreadyVisited){
        // did we already visit this type?
        if(!alreadyVisited.add(klass))
            return false;
        // first see if it satisfies it directly
        SatisfiedTypes classAnnotation = klass.getAnnotation(SatisfiedTypes.class);
        if(classAnnotation != null){
            for(String satisfiedType : classAnnotation.value()){
                if(satisfiedType.equals(className))
                    return true;
            }
        }
        // now look at this class's interfaces
        for(java.lang.Class<?> intrface : klass.getInterfaces()){
            if(lookForInterface(intrface, className, alreadyVisited))
                return true;
        }
        // no luck
        return false;
    }
}
