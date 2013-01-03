package com.redhat.ceylon.compiler.java;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ceylon.language.Iterable;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.exhausted_;

import com.redhat.ceylon.compiler.java.language.ArraySequence;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;

/**
 * Helper class for generated Ceylon code that needs to call implementation logic.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class Util {
    
    public static String declClassName(String name) {
        return name.replace("::", ".");
    }
    
    /**
     * Returns true if the given object satisfies ceylon.language.Identifiable
     */
    public static boolean isIdentifiable(java.lang.Object o){
        return satisfiesInterface(o, "ceylon.language.Identifiable");
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
        if (klass.getName().equals(className))
            return true;
        if ((className.equals("ceylon.language.IdentifiableObject"))
                && klass!=java.lang.Object.class
                && !isSubclassOfString(klass)
                //&& klass!=java.lang.String.class
        		&& !klass.isAnnotationPresent(Class.class)
        		&& (!klass.isInterface() || !klass.isAnnotationPresent(Ceylon.class))) {
        	//TODO: this is broken for a Java class that
        	//      extends a Ceylon class
        	return true;
        }
        Class classAnnotation = klass.getAnnotation(Class.class);
        if (classAnnotation != null) {
            String superclassName = declClassName(classAnnotation.extendsType());
            int i = superclassName.indexOf('<');
            if (i>0) {
                superclassName = superclassName.substring(0, i);
            }
            if (superclassName.isEmpty()) {
                return false;
            }
            try {
                return classExtendsClass(
                        java.lang.Class.forName(superclassName, true, klass.getClassLoader()), 
                        className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
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
        if(klass == null
                || klass == ceylon.language.Void.class)
            return false;
        if ((className.equals("ceylon.language.Identifiable"))
                && klass!=java.lang.Object.class
                //&& klass!=java.lang.String.class
                && !klass.isAnnotationPresent(Ceylon.class)) {
            //TODO: this is broken for a Java class that
            //      extends a Ceylon class
            return true;
        }
        if (className.equals("ceylon.language.Identifiable")
            && isSubclassOfString(klass)) {
            return false;
        }
        // try the interfaces
        if(lookForInterface(klass, className, alreadyVisited))
            return true;
        // try its superclass
        Class classAnnotation = klass.getAnnotation(Class.class);
        String superclassName;
        if (classAnnotation!=null) {
            superclassName = declClassName(classAnnotation.extendsType());
            int i = superclassName.indexOf('<');
            if (i>0) {
                superclassName = superclassName.substring(0, i);
            }
            
        } else {
            // Maybe the class didn't have an extends, so implictly IdentifiableObject
            superclassName = "ceylon.language.IdentifiableObject";
        }
        if (!superclassName.isEmpty()) {
            try {
                return classSatisfiesInterface(
                        java.lang.Class.forName(superclassName, true, klass.getClassLoader()), 
                        className, alreadyVisited);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classSatisfiesInterface(klass.getSuperclass(), className, alreadyVisited);
    }

    private static boolean isSubclassOfString(java.lang.Class<?> klass) {
        return klass.getName().equals("com.redhat.ceylon.compiler.java.language.EmptyString") || klass.getName().equals("com.redhat.ceylon.compiler.java.language.SequenceString");
    }

    private static boolean lookForInterface(java.lang.Class<?> klass, String className, 
            Set<java.lang.Class<?>> alreadyVisited){
        if (klass.getName().equals(className))
            return true;
        // did we already visit this type?
        if(!alreadyVisited.add(klass))
            return false;
        // first see if it satisfies it directly
        SatisfiedTypes satisfiesAnnotation = klass.getAnnotation(SatisfiedTypes.class);
        if (satisfiesAnnotation!=null){
            for (String satisfiedType : satisfiesAnnotation.value()){
                satisfiedType = declClassName(satisfiedType);
                int i = satisfiedType.indexOf('<');
                if (i>0) {
                    satisfiedType = satisfiedType.substring(0, i);
                }
                try {
                    if (lookForInterface(
                            java.lang.Class.forName(satisfiedType, true, klass.getClassLoader()), 
                            className, alreadyVisited)) {
                        return true;
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // now look at this class's interfaces
        for (java.lang.Class<?> intrface : klass.getInterfaces()){
            if (lookForInterface(intrface, className, alreadyVisited))
                return true;
        }
        // no luck
        return false;
    }
    
    //
    // Java variadic conversions
    
    @SuppressWarnings("unchecked")
    public static <T> List<T> collectIterable(Iterable<? extends T> sequence) {
        List<T> list = new LinkedList<T>();
        if (sequence != null) {
            Iterator<? extends T> iterator = sequence.getIterator();
            Object o; 
            while((o = iterator.next()) != exhausted_.getExhausted$()){
                list.add((T)o);
            }
        }
        return list;
    }

    public static boolean[] toBooleanArray(ceylon.language.Iterable<? extends ceylon.language.Boolean> sequence){
        if(sequence instanceof ceylon.language.List)
            return toBooleanArray((ceylon.language.List<? extends ceylon.language.Boolean>)sequence);
        List<ceylon.language.Boolean> list = collectIterable(sequence);
        boolean[] ret = new boolean[list.size()];
        int i=0;
        for(ceylon.language.Boolean e : list){
            ret[i++] = e.booleanValue();
        }
        return ret;
    }
    
    public static boolean[] toBooleanArray(ceylon.language.List<? extends ceylon.language.Boolean> sequence){
        boolean[] ret = new boolean[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst().booleanValue();
            sequence = (ceylon.language.List<? extends ceylon.language.Boolean>)sequence.getRest();
        }
        return ret;
    }
    
    public static byte[] toByteArray(ceylon.language.Iterable<? extends ceylon.language.Integer> sequence){
        if(sequence instanceof ceylon.language.List)
            return toByteArray((ceylon.language.List<? extends ceylon.language.Integer>)sequence);
        List<ceylon.language.Integer> list = collectIterable(sequence);
        byte[] ret = new byte[list.size()];
        int i=0;
        for(ceylon.language.Integer e : list){
            ret[i++] = (byte)e.longValue();
        }
        return ret;
    }

    public static byte[] toByteArray(ceylon.language.List<? extends ceylon.language.Integer> sequence){
        byte[] ret = new byte[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (byte) sequence.getFirst().longValue();
            sequence = (ceylon.language.List<? extends ceylon.language.Integer>)sequence.getRest();
        }
        return ret;
    }
    
    public static short[] toShortArray(ceylon.language.Iterable<? extends ceylon.language.Integer> sequence){
        if(sequence instanceof ceylon.language.List)
            return toShortArray((ceylon.language.List<? extends ceylon.language.Integer>)sequence);
        List<ceylon.language.Integer> list = collectIterable(sequence);
        short[] ret = new short[list.size()];
        int i=0;
        for(ceylon.language.Integer e : list){
            ret[i++] = (short)e.longValue();
        }
        return ret;
    }

    public static short[] toShortArray(ceylon.language.List<? extends ceylon.language.Integer> sequence){
        short[] ret = new short[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (short) sequence.getFirst().longValue();
            sequence = (ceylon.language.List<? extends ceylon.language.Integer>)sequence.getRest();
        }
        return ret;
    }
    
    public static int[] toIntArray(ceylon.language.Iterable<? extends ceylon.language.Integer> sequence){
        if(sequence instanceof ceylon.language.List)
            return toIntArray((ceylon.language.List<? extends ceylon.language.Integer>)sequence);
        List<ceylon.language.Integer> list = collectIterable(sequence);
        int[] ret = new int[list.size()];
        int i=0;
        for(ceylon.language.Integer e : list){
            ret[i++] = (int)e.longValue();
        }
        return ret;
    }

    public static int[] toIntArray(ceylon.language.List<? extends ceylon.language.Integer> sequence){
        int[] ret = new int[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (int) sequence.getFirst().longValue();
            sequence = (ceylon.language.List<? extends ceylon.language.Integer>)sequence.getRest();
        }
        return ret;
    }
    
    public static long[] toLongArray(ceylon.language.Iterable<? extends ceylon.language.Integer> sequence){
        if(sequence instanceof ceylon.language.List)
            return toLongArray((ceylon.language.List<? extends ceylon.language.Integer>)sequence);
        List<ceylon.language.Integer> list = collectIterable(sequence);
        long[] ret = new long[list.size()];
        int i=0;
        for(ceylon.language.Integer e : list){
            ret[i++] = e.longValue();
        }
        return ret;
    }

    public static long[] toLongArray(ceylon.language.List<? extends ceylon.language.Integer> sequence){
        long[] ret = new long[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst().longValue();
            sequence = (ceylon.language.List<? extends ceylon.language.Integer>)sequence.getRest();
        }
        return ret;
    }

    public static float[] toFloatArray(ceylon.language.Iterable<? extends ceylon.language.Float> sequence){
        if(sequence instanceof ceylon.language.List)
            return toFloatArray((ceylon.language.List<? extends ceylon.language.Float>)sequence);
        List<ceylon.language.Float> list = collectIterable(sequence);
        float[] ret = new float[list.size()];
        int i=0;
        for(ceylon.language.Float e : list){
            ret[i++] = (float)e.doubleValue();
        }
        return ret;
    }

    public static float[] toFloatArray(ceylon.language.List<? extends ceylon.language.Float> sequence){
        float[] ret = new float[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (float) sequence.getFirst().doubleValue();
            sequence = (ceylon.language.List<? extends ceylon.language.Float>)sequence.getRest();
        }
        return ret;
    }

    public static double[] toDoubleArray(ceylon.language.Iterable<? extends ceylon.language.Float> sequence){
        if(sequence instanceof ceylon.language.List)
            return toDoubleArray((ceylon.language.List<? extends ceylon.language.Float>)sequence);
        List<ceylon.language.Float> list = collectIterable(sequence);
        double[] ret = new double[list.size()];
        int i=0;
        for(ceylon.language.Float e : list){
            ret[i++] = e.doubleValue();
        }
        return ret;
    }

    public static double[] toDoubleArray(ceylon.language.List<? extends ceylon.language.Float> sequence){
        double[] ret = new double[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst().doubleValue();
            sequence = (ceylon.language.List<? extends ceylon.language.Float>)sequence.getRest();
        }
        return ret;
    }

    public static char[] toCharArray(ceylon.language.Iterable<? extends ceylon.language.Character> sequence){
        if(sequence instanceof ceylon.language.List)
            return toCharArray((ceylon.language.List<? extends ceylon.language.Character>)sequence);
        List<ceylon.language.Character> list = collectIterable(sequence);
        char[] ret = new char[list.size()];
        int i=0;
        // FIXME: this is invalid and should yield a larger array by splitting chars > 16 bits in two
        for(ceylon.language.Character e : list){
            ret[i++] = (char)e.intValue();
        }
        return ret;
    }

    public static char[] toCharArray(ceylon.language.List<? extends ceylon.language.Character> sequence){
        char[] ret = new char[(int) sequence.getSize()];
        int i=0;
        // FIXME: this is invalid and should yield a larger array by splitting chars > 16 bits in two
        while(!sequence.getEmpty()){
            ret[i++] = (char) sequence.getFirst().intValue();
            sequence = (ceylon.language.List<? extends ceylon.language.Character>)sequence.getRest();
        }
        return ret;
    }

    public static int[] toCodepointArray(ceylon.language.Iterable<? extends ceylon.language.Character> sequence){
        if(sequence instanceof ceylon.language.List)
            return toCodepointArray((ceylon.language.List<? extends ceylon.language.Character>)sequence);
        List<ceylon.language.Character> list = collectIterable(sequence);
        int[] ret = new int[list.size()];
        int i=0;
        for(ceylon.language.Character e : list){
            ret[i++] = e.intValue();
        }
        return ret;
    }

    public static int[] toCodepointArray(ceylon.language.List<? extends ceylon.language.Character> sequence){
        int[] ret = new int[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = (char) sequence.getFirst().intValue();
            sequence = (ceylon.language.List<? extends ceylon.language.Character>)sequence.getRest();
        }
        return ret;
    }

    public static java.lang.String[] toJavaStringArray(ceylon.language.Iterable<? extends ceylon.language.String> sequence){
        if(sequence instanceof ceylon.language.List)
            return toJavaStringArray((ceylon.language.List<? extends ceylon.language.String>)sequence);
        List<ceylon.language.String> list = collectIterable(sequence);
        java.lang.String[] ret = new java.lang.String[list.size()];
        int i=0;
        for(ceylon.language.String e : list){
            ret[i++] = e.toString();
        }
        return ret;
    }

    public static java.lang.String[] toJavaStringArray(ceylon.language.List<? extends ceylon.language.String> sequence){
        java.lang.String[] ret = new java.lang.String[(int) sequence.getSize()];
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst().toString();
            sequence = (ceylon.language.List<? extends ceylon.language.String>)sequence.getRest();
        }
        return ret;
    }

    public static <T> T[] toArray(ceylon.language.List<? extends T> sequence,
            T[] ret){
        int i=0;
        while(!sequence.getEmpty()){
            ret[i++] = sequence.getFirst();
            sequence = (ceylon.language.List<? extends T>)sequence.getRest();
        }
        return ret;
    }

    public static <T> T[] toArray(ceylon.language.Iterable<? extends T> iterable,
            java.lang.Class<T> klass){
        List<T> list = collectIterable(iterable);
        @SuppressWarnings("unchecked")
        T[] ret = (T[]) java.lang.reflect.Array.newInstance(klass, list.size());
        return list.toArray(ret);
    }
    
    public static <T> T checkNull(T t){
        if(t == null)
            throw new NullPointerException();
        return t;
    }
    
    /**
     * Return {@link empty_#getEmpty$ empty} or an {@link ArraySequence}
     * wrapping the given elements, depending on whether the given array is 
     * empty
     * @param elements The elements
     * @return A Sequential
     */
    public static <T> Sequential<T> sequentialInstance(T[] elements) {
        if (elements.length == 0) {
            return (Sequential)empty_.getEmpty$();
        }
        return new ArraySequence<T>(elements);
    }
}
