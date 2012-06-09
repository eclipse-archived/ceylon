package com.redhat.ceylon.compiler.java;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;

/**
 * Helper class for generated Ceylon code that needs to call implementation logic.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class Util {
    
    /**
     * Returns true if the given object satisfies ceylon.language.Identifiable
     */
    public static boolean isIdentifiable(java.lang.Object o){
        return satisfiesInterface(o, "ceylon.language.Identifiable");
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
        if ((className.equals("ceylon.language.IdentifiableObject") /*|| 
             className.equals("ceylon.language.Equality")*/)
                && klass!=java.lang.Object.class
        		&& !klass.isAnnotationPresent(Ceylon.class)) {
        	//TODO: this is broken for a Java class that
        	//      extends a Ceylon class
        	return true;
        }
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
    
    public static boolean[] toBooleanArray(ceylon.language.FixedSized<? extends ceylon.language.Boolean> sequence){
        boolean[] ret = new boolean[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst().booleanValue();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends ceylon.language.Boolean>)sequence).getRest();
            else
                break;
        }
        return ret;
    }
    
    public static byte[] toByteArray(ceylon.language.FixedSized<? extends ceylon.language.Integer> sequence){
        byte[] ret = new byte[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (byte) sequence.getFirst().longValue();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends ceylon.language.Integer>)sequence).getRest();
            else
                break;
        }
        return ret;
    }
    
    public static short[] toShortArray(ceylon.language.FixedSized<? extends ceylon.language.Integer> sequence){
        short[] ret = new short[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (short) sequence.getFirst().longValue();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends ceylon.language.Integer>)sequence).getRest();
            else
                break;
        }
        return ret;
    }
    
    public static int[] toIntArray(ceylon.language.FixedSized<? extends ceylon.language.Integer> sequence){
        int[] ret = new int[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (int) sequence.getFirst().longValue();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends ceylon.language.Integer>)sequence).getRest();
            else
                break;
        }
        return ret;
    }
    
    public static long[] toLongArray(ceylon.language.FixedSized<? extends ceylon.language.Integer> sequence){
        long[] ret = new long[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst().longValue();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends ceylon.language.Integer>)sequence).getRest();
            else
                break;
        }
        return ret;
    }

    public static float[] toFloatArray(ceylon.language.FixedSized<? extends ceylon.language.Float> sequence){
        float[] ret = new float[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (float) sequence.getFirst().doubleValue();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends ceylon.language.Float>)sequence).getRest();
            else
                break;
        }
        return ret;
    }

    public static double[] toDoubleArray(ceylon.language.FixedSized<? extends ceylon.language.Float> sequence){
        double[] ret = new double[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst().doubleValue();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends ceylon.language.Float>)sequence).getRest();
            else
                break;
        }
        return ret;
    }

    public static char[] toCharArray(ceylon.language.FixedSized<? extends ceylon.language.Character> sequence){
        char[] ret = new char[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (char) sequence.getFirst().intValue();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends ceylon.language.Character>)sequence).getRest();
            else
                break;
        }
        return ret;
    }

    public static java.lang.String[] toJavaStringArray(ceylon.language.FixedSized<? extends ceylon.language.String> sequence){
        java.lang.String[] ret = new java.lang.String[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst().toString();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends ceylon.language.String>)sequence).getRest();
            else
                break;
        }
        return ret;
    }

    public static <T> T[] toArray(ceylon.language.FixedSized<? extends T> sequence,
            T[] ret){
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst();
            if(sequence instanceof ceylon.language.Some<?>)
                sequence = ((ceylon.language.Some<? extends T>)sequence).getRest();
            else
                break;
        }
        return ret;
    }

}
