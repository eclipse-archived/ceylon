package ceylon.language;

import static com.redhat.ceylon.compiler.java.Util.toInt;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import ceylon.language.impl.BaseIterator;
import ceylon.language.impl.BaseList;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.AbstractArrayIterable;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.FunctionalParameter;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class(extendsType="ceylon.language::Object", basic = false, identifiable = false)
@TypeParameters(@TypeParameter(value = "Element"))
@SatisfiedTypes({
    "ceylon.language::List<Element>",
    "ceylon.language::Ranged<ceylon.language::Integer,Element,ceylon.language::Array<Element>>"
})
public final class Array<Element>
        extends BaseList<Element>
        implements List<Element> {
    
    private final java.lang.Object array;
    
    private final TypeDescriptor $reifiedElement;
    
    @Ignore
    public Array(final TypeDescriptor $reifiedElement, 
            int size, Element element) {
        this($reifiedElement, 
                createArray($reifiedElement, size, element));
    }
    
    @Ignore
    public Array(final TypeDescriptor $reifiedElement, 
            int size, Element element, Callable<? extends Element> next) {
        this($reifiedElement, 
                createArray($reifiedElement, size, element, next));
    }
    
    @Ignore
    public Array(final TypeDescriptor $reifiedElement, 
            int size, Callable<? extends Element> element) {
        this($reifiedElement, 
                createArray($reifiedElement, size, element));
    }
    
    @Ignore
    public Array(final TypeDescriptor $reifiedElement, 
            int size, Collector<? extends Element> element) {
        this($reifiedElement, 
                createArray($reifiedElement, size, element));
    }
    
    public Array(@Ignore final TypeDescriptor $reifiedElement, 
            @Name("elements")
            @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
            final ceylon.language.Iterable<? extends Element,?> elements) {
        this($reifiedElement, 
                createArray($reifiedElement, elements));
    }

    @SuppressWarnings("unchecked")
    private static <Element> java.lang.Object createArray(
            final TypeDescriptor $reifiedElement,
            final ceylon.language.Iterable<? extends Element,?> elements) {
        
        final ArrayList<Element> list;
        final int size;
        if (elements instanceof Array) {
            size = Util.toInt(elements.getSize());
            list = null;
        }
        else if (elements instanceof Sequential<?>) {
            size = Util.toInt(elements.getSize());
            list = null;
        }
        else {
            list = new ArrayList<Element>();
            Iterator<?> iterator = elements.iterator();
            java.lang.Object elem;
            while ((elem=iterator.next())!=finished_.get_()) {
                list.add((Element) elem);
            }
            size = Util.toInt(list.size());
        }
        
        final java.lang.Class<?> clazz = 
                $reifiedElement.getArrayElementClass();
        if (!$reifiedElement.containsNull()) {
            if (clazz==String.class) {
                //note: we don't unbox strings in an Array<String?>
                //      because it would break javaObjectArray()
                java.lang.String[] array = 
                        new java.lang.String[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        String s = (String) 
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = s==null ? null : s.value;
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        String s = (String) list.get(i);
                        array[i] = s==null ? null : s.value;
                    }
                }
                return array;
            }
            else if (clazz==Integer.class) {
                long[] array = new long[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        Integer e = (Integer)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.value;
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((Integer) list.get(i)).value;
                    }
                }
                return array;
            }
            else if (clazz==Float.class) {
                double[] array = new double[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        Float e = (Float)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.value;
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((Float) list.get(i)).value;
                    }
                }
                return array;
            }
            else if (clazz==Character.class) {
                int[] array = new int[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        Character e = (Character)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.codePoint;
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((Character) list.get(i))
                                        .codePoint;
                    }
                }
                return array;
            }
            else if (clazz==Boolean.class) {
                boolean[] array = new boolean[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        Boolean e = (Boolean)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.booleanValue();
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((Boolean) list.get(i))
                                        .booleanValue();
                    }
                }
                return array;
            }
            else if (clazz==java.lang.Boolean.class) {
                boolean[] array = new boolean[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        java.lang.Boolean e = (java.lang.Boolean)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.booleanValue();
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((java.lang.Boolean) list.get(i))
                                        .booleanValue();
                    }
                }
                return array;
            }
            else if (clazz==java.lang.Character.class) {
                char[] array = new char[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        java.lang.Character e = (java.lang.Character)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.charValue();
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((java.lang.Character) list.get(i))
                                        .charValue();
                    }
                }
                return array;
            }
            else if (clazz==java.lang.Float.class) {
                float[] array = new float[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        java.lang.Float e = (java.lang.Float)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.floatValue();
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((java.lang.Float) list.get(i))
                                        .floatValue();
                    }
                }
                return array;
            }
            else if (clazz==java.lang.Double.class) {
                double[] array = new double[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        java.lang.Double e = (java.lang.Double)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.doubleValue();
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((java.lang.Double) list.get(i))
                                        .doubleValue();
                    }
                }
                return array;
            }
            else if (clazz==java.lang.Byte.class) {
                byte[] array = new byte[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        java.lang.Byte e = (java.lang.Byte)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.byteValue();
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((java.lang.Byte) list.get(i))
                                        .byteValue();
                    }
                }
                return array;
            }
            else if (clazz==java.lang.Short.class) {
                short[] array = new short[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        java.lang.Short e = (java.lang.Short)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.shortValue();
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((java.lang.Short) list.get(i))
                                        .shortValue();
                    }
                }
                return array;
            }
            else if (clazz==java.lang.Integer.class) {
                int[] array = new int[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        java.lang.Integer e = (java.lang.Integer)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.intValue();
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((java.lang.Integer) list.get(i))
                                        .intValue();
                    }
                }
                return array;
            }
            else if (clazz==java.lang.Long.class) {
                long[] array = new long[size];
                if (elements instanceof Array) {
                    arraycopy(((Array<?>)elements).array, 
                            0, array, 0, size);
                }
                else if (elements instanceof Sequential<?>) {
                    for (int i=0; i<size; i++) {
                        java.lang.Long e = (java.lang.Long)
                                ((Sequential<?>) elements).getFromFirst(i);
                        array[i] = e.longValue();
                    }
                }
                else {
                    for (int i=0; i<size; i++) {
                        array[i] = 
                                ((java.lang.Long) list.get(i))
                                        .longValue();
                    }
                }
                return array;
            }
        }
        
        java.lang.Object[] array = (java.lang.Object[]) java.lang.reflect.Array
                .newInstance($reifiedElement.getArrayElementClass(), size);
        if (elements instanceof Array) {
            java.lang.Object otherArray = ((Array<?>) elements).array;
            if (otherArray.getClass()==array.getClass()) {
                arraycopy(otherArray, 0, array, 0, size);
            }
            else if (otherArray instanceof Object[]) {
                Object[] objects = (Object[])otherArray;
                for (int i=0; i<size; i++) {
                    array[i] = objects[i];
                }
            }
            else if (otherArray instanceof int[]) {
                int[] ints = (int[])otherArray;
                for (int i=0; i<size; i++) {
                    array[i] = ints[i];
                }
            }
            else if (otherArray instanceof long[]) {
                long[] longs = (long[])otherArray;
                for (int i=0; i<size; i++) {
                    array[i] = longs[i];
                }
            }
            else if (otherArray instanceof byte[]) {
                byte[] bytes = (byte[])otherArray;
                for (int i=0; i<size; i++) {
                    array[i] = bytes[i];
                }
            }
            else if (otherArray instanceof short[]) {
                short[] shorts = (short[])otherArray;
                for (int i=0; i<size; i++) {
                    array[i] = shorts[i];
                }
            }
            else if (otherArray instanceof float[]) {
                float[] floats = (float[])otherArray;
                for (int i=0; i<size; i++) {
                    array[i] = floats[i];
                }
            }
            else if (otherArray instanceof double[]) {
                double[] doubles = (double[])otherArray;
                for (int i=0; i<size; i++) {
                    array[i] = doubles[i];
                }
            }
            else if (otherArray instanceof char[]) {
                for (int i=0; i<size; i++) {
                    array[i] = ((char[])otherArray)[i];
                }
            }
            else if (otherArray instanceof boolean[]) {
                boolean[] bools = (boolean[])otherArray;
                for (int i=0; i<size; i++) {
                    array[i] = bools[i];
                }
            }
        }
        else if (elements instanceof Sequential<?>) {
            for (int i=0; i<size; i++) {
                java.lang.Object e = 
                        ((Sequential<?>) elements).getFromFirst(i);
                array[i] = e;
            }
        }
        else {
            for (int i=0; i<size; i++) {
                array[i] = list.get(i);
            }
        }
        return array;
    }
    
    private static <Element> java.lang.Object createArray(
        final TypeDescriptor $reifiedElement,
        final int size, Element element) {
        java.lang.Class<?> clazz = $reifiedElement.getArrayElementClass();
        if (!$reifiedElement.containsNull()) {
            if (clazz==String.class) {
                //note: we don't unbox strings in an Array<String?>
                //      because it would break javaObjectArray()
                java.lang.String[] array = new java.lang.String[size];
                String s = (String) element;
                if (s!=null) Arrays.fill(array, s.value); 
                return array;
            }
            else if (clazz==Integer.class) {
                long[] array = new long[size];
                long value = ((Integer) element).value;
                if (value!=0l) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==Float.class) {
                double[] array = new double[size];
                double value = ((Float) element).value;
                if (value!=0.0d) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==Character.class) {
                int[] array = new int[size];
                int value = ((Character) element).codePoint;
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==Boolean.class) {
                boolean[] array = new boolean[size];
                boolean value = ((Boolean) element).booleanValue();
                if (value!=false) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==java.lang.Boolean.class) {
                boolean[] array = new boolean[size];
                boolean value = ((java.lang.Boolean) element).booleanValue();
                if (value!=false) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==java.lang.Character.class) {
                char[] array = new char[size];
                char value = ((java.lang.Character) element).charValue();
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==java.lang.Float.class) {
                float[] array = new float[size];
                float value = ((java.lang.Float) element).floatValue();
                if (value!=0.0f) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==java.lang.Double.class) {
                double[] array = new double[size];
                double value = ((java.lang.Double) element).doubleValue();
                if (value!=0.0d) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==java.lang.Byte.class) {
                byte[] array = new byte[size];
                byte value = ((java.lang.Byte) element).byteValue();
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==java.lang.Short.class) {
                short[] array = new short[size];
                short value = ((java.lang.Short) element).shortValue();
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==java.lang.Integer.class) {
                int[] array = new int[size];
                int value = ((java.lang.Integer) element).intValue();
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz==java.lang.Long.class) {
                long[] array = new long[size];
                long value = ((java.lang.Long) element).longValue();
                if (value!=0l) Arrays.fill(array, value); 
                return array;
            }
        }
        
        java.lang.Object[] array = 
                (java.lang.Object[]) java.lang.reflect.Array
                        .newInstance(clazz, size);
        if (element!=null) {
            Arrays.fill(array, element);
        }
        return array;
    }

    private static <Element> java.lang.Object createArray(
            final TypeDescriptor $reifiedElement,
            final int size, final Element element,
            final Callable<? extends Element> next) {
        java.lang.Class<?> clazz = $reifiedElement.getArrayElementClass();
        if (!$reifiedElement.containsNull()) {
            if (clazz==String.class) {
                //note: we don't unbox strings in an Array<String?>
                //      because it would break javaObjectArray()
                java.lang.String[] array = new java.lang.String[size];
                String s = (String) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.value;
                    s = (String) next.$call$(s);
                }
                return array;
            }
            else if (clazz==Integer.class) {
                long[] array = new long[size];
                Integer s = (Integer) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.value;
                    s = (Integer) next.$call$(s);
                }
                return array;
            }
            else if (clazz==Float.class) {
                double[] array = new double[size];
                Float s = (Float) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.value;
                    s = (Float) next.$call$(s);
                }
                return array;
            }
            else if (clazz==Character.class) {
                int[] array = new int[size];
                Character s = (Character) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.codePoint;
                    s = (Character) next.$call$(s);
                }
                return array;
            }
            else if (clazz==Boolean.class) {
                boolean[] array = new boolean[size];
                Boolean s = (Boolean) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.booleanValue();
                    s = (Boolean) next.$call$(s);
                }
                return array;
            }
            else if (clazz==java.lang.Boolean.class) {
                boolean[] array = new boolean[size];
                java.lang.Boolean s = (java.lang.Boolean) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.booleanValue();
                    s = (java.lang.Boolean) next.$call$(s);
                }
                return array;
            }
            else if (clazz==java.lang.Character.class) {
                char[] array = new char[size];
                java.lang.Character s = (java.lang.Character) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.charValue();
                    s = (java.lang.Character) next.$call$(s);
                }
                return array;
            }
            else if (clazz==java.lang.Float.class) {
                float[] array = new float[size];
                java.lang.Float s = (java.lang.Float) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.floatValue();
                    s = (java.lang.Float) next.$call$(s);
                }
                return array;
            }
            else if (clazz==java.lang.Double.class) {
                double[] array = new double[size];
                java.lang.Double s = (java.lang.Double) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.doubleValue();
                    s = (java.lang.Double) next.$call$(s);
                }
                return array;
            }
            else if (clazz==java.lang.Byte.class) {
                byte[] array = new byte[size];
                java.lang.Byte s = (java.lang.Byte) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.byteValue();
                    s = (java.lang.Byte) next.$call$(s);
                }
                return array;
            }
            else if (clazz==java.lang.Short.class) {
                short[] array = new short[size];
                java.lang.Short s = (java.lang.Short) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.shortValue();
                    s = (java.lang.Short) next.$call$(s);
                }
                return array;
            }
            else if (clazz==java.lang.Integer.class) {
                int[] array = new int[size];
                java.lang.Integer s = (java.lang.Integer) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.intValue();
                    s = (java.lang.Integer) next.$call$(s);
                }
                return array;
            }
            else if (clazz==java.lang.Long.class) {
                long[] array = new long[size];
                java.lang.Long s = (java.lang.Long) element;
                for (int i=0; i<size; i++) {
                    array[i] = s.longValue();
                    s = (java.lang.Long) next.$call$(s);
                }
                return array;
            }
        }
        
        java.lang.Object[] array = 
                (java.lang.Object[]) java.lang.reflect.Array
                        .newInstance(clazz, size);
        Element current = element;
        for (int i=0; i<size; i++) {
            array[i] = element;
            current = next.$call$(current);
        }
        return array;
    }

    private static <Element> java.lang.Object createArray(
            final TypeDescriptor $reifiedElement,
            final int size, final Callable<? extends Element> element) {
        java.lang.Class<?> clazz = $reifiedElement.getArrayElementClass();
        if (!$reifiedElement.containsNull()) {
            if (clazz==String.class) {
                //note: we don't unbox strings in an Array<String?>
                //      because it would break javaObjectArray()
                java.lang.String[] array = new java.lang.String[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((String) 
                            element.$call$(Integer.instance(i)))
                            .value;
                }
                return array;
            }
            else if (clazz==Integer.class) {
                long[] array = new long[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((Integer) 
                            element.$call$(Integer.instance(i)))
                            .value;
                }
                return array;
            }
            else if (clazz==Float.class) {
                double[] array = new double[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((Float) 
                            element.$call$(Integer.instance(i)))
                            .value;
                }
                return array;
            }
            else if (clazz==Character.class) {
                int[] array = new int[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((Character) 
                            element.$call$(Integer.instance(i)))
                            .codePoint;
                }
                return array;
            }
            else if (clazz==Boolean.class) {
                boolean[] array = new boolean[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((Boolean) 
                            element.$call$(Integer.instance(i)))
                            .booleanValue();
                }
                return array;
            }
            else if (clazz==java.lang.Boolean.class) {
                boolean[] array = new boolean[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Boolean) 
                            element.$call$(Integer.instance(i));
                }
                return array;
            }
            else if (clazz==java.lang.Character.class) {
                char[] array = new char[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Character) 
                            element.$call$(Integer.instance(i));
                }
                return array;
            }
            else if (clazz==java.lang.Float.class) {
                float[] array = new float[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Float) 
                            element.$call$(Integer.instance(i));
                }
                return array;
            }
            else if (clazz==java.lang.Double.class) {
                double[] array = new double[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Double) 
                            element.$call$(Integer.instance(i));
                }
                return array;
            }
            else if (clazz==java.lang.Byte.class) {
                byte[] array = new byte[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Byte) 
                            element.$call$(Integer.instance(i));
                }
                return array;
            }
            else if (clazz==java.lang.Short.class) {
                short[] array = new short[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Short) 
                            element.$call$(Integer.instance(i));
                }
                return array;
            }
            else if (clazz==java.lang.Integer.class) {
                int[] array = new int[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Integer) 
                            element.$call$(Integer.instance(i));
                }
                return array;
            }
            else if (clazz==java.lang.Long.class) {
                long[] array = new long[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Long) 
                            element.$call$(Integer.instance(i));
                }
                return array;
            }
        }
        
        java.lang.Object[] array = 
                (java.lang.Object[]) java.lang.reflect.Array
                        .newInstance(clazz, size);
        for (int i=0; i<size; i++) {
            array[i] = element.$call$(Integer.instance(i));
        }
        return array;
    }

    private static <Element> java.lang.Object createArray(
            final TypeDescriptor $reifiedElement,
            final int size, final Collector<? extends Element> element) {
        java.lang.Class<?> clazz = $reifiedElement.getArrayElementClass();
        if (!$reifiedElement.containsNull()) {
            if (clazz==String.class) {
                //note: we don't unbox strings in an Array<String?>
                //      because it would break javaObjectArray()
                java.lang.String[] array = new java.lang.String[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((String) 
                            element.call(i))
                            .value;
                }
                return array;
            }
            else if (clazz==Integer.class) {
                long[] array = new long[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((Integer) 
                            element.call(i))
                            .value;
                }
                return array;
            }
            else if (clazz==Float.class) {
                double[] array = new double[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((Float) 
                            element.call(i))
                            .value;
                }
                return array;
            }
            else if (clazz==Character.class) {
                int[] array = new int[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((Character) 
                            element.call(i))
                            .codePoint;
                }
                return array;
            }
            else if (clazz==Boolean.class) {
                boolean[] array = new boolean[size];
                for (int i=0; i<size; i++) {
                    array[i] = ((Boolean) 
                            element.call(i))
                            .booleanValue();
                }
                return array;
            }
            else if (clazz==java.lang.Boolean.class) {
                boolean[] array = new boolean[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Boolean) 
                            element.call(i);
                }
                return array;
            }
            else if (clazz==java.lang.Character.class) {
                char[] array = new char[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Character) 
                            element.call(i);
                }
                return array;
            }
            else if (clazz==java.lang.Float.class) {
                float[] array = new float[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Float) 
                            element.call(i);
                }
                return array;
            }
            else if (clazz==java.lang.Double.class) {
                double[] array = new double[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Double) 
                            element.call(i);
                }
                return array;
            }
            else if (clazz==java.lang.Byte.class) {
                byte[] array = new byte[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Byte) 
                            element.call(i);
                }
                return array;
            }
            else if (clazz==java.lang.Short.class) {
                short[] array = new short[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Short) 
                            element.call(i);
                }
                return array;
            }
            else if (clazz==java.lang.Integer.class) {
                int[] array = new int[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Integer) 
                            element.call(i);
                }
                return array;
            }
            else if (clazz==java.lang.Long.class) {
                long[] array = new long[size];
                for (int i=0; i<size; i++) {
                    array[i] = (java.lang.Long) 
                            element.call(i);
                }
                return array;
            }
        }
        
        java.lang.Object[] array = 
                (java.lang.Object[]) java.lang.reflect.Array
                        .newInstance(clazz, size);
        for (int i=0; i<size; i++) {
            array[i] = element.call(i);
        }
        return array;
    }

    @Ignore
    private Array(@Ignore TypeDescriptor $reifiedElement, java.lang.Object array) {
    	super($reifiedElement);
    	this.$reifiedElement = $reifiedElement;
        assert(array.getClass().isArray());
        this.array = array;
    }
    
    @Ignore
    public static <T> Array<T> instance(T[] array) {
        if (array == null) {
            return null;
        }
        java.lang.Class<?> componentType = array.getClass().getComponentType();
        return new Array<T>(TypeDescriptor.klass(componentType), array);
    }
    
    private static final TypeDescriptor CHAR_TYPE = 
            TypeDescriptor.klass(java.lang.Character.class);
    @Ignore
    public static Array<java.lang.Character> instance(char[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Character>(CHAR_TYPE, array);
    }

    private static final TypeDescriptor BYTE_TYPE = 
            TypeDescriptor.klass(java.lang.Byte.class);
    @Ignore
    public static Array<java.lang.Byte> instance(byte[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Byte>(BYTE_TYPE, array);
    }

    private static final TypeDescriptor SHORT_TYPE = 
            TypeDescriptor.klass(java.lang.Short.class);
    @Ignore
    public static Array<java.lang.Short> instance(short[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Short>(SHORT_TYPE, array);
    }
    
    private static final TypeDescriptor INT_TYPE = 
            TypeDescriptor.klass(java.lang.Integer.class);
    @Ignore
    public static Array<java.lang.Integer> instance(int[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Integer>(INT_TYPE, array);
    }
    
    private static final TypeDescriptor LONG_TYPE = 
            TypeDescriptor.klass(java.lang.Long.class);
    @Ignore
    public static Array<java.lang.Long> instance(long[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Long>(LONG_TYPE, array);
    }
    
    private static final TypeDescriptor FLOAT_TYPE = 
            TypeDescriptor.klass(java.lang.Float.class);
    @Ignore
    public static Array<java.lang.Float> instance(float[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Float>(FLOAT_TYPE, array);
    }
    
    private static final TypeDescriptor DOUBLE_TYPE = 
            TypeDescriptor.klass(java.lang.Double.class);
    @Ignore
    public static Array<java.lang.Double> instance(double[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Double>(DOUBLE_TYPE, array);
    }
    
    private static final TypeDescriptor BOOLEAN_TYPE = 
            TypeDescriptor.klass(java.lang.Boolean.class);
    @Ignore
    public static Array<java.lang.Boolean> instance(boolean[] array) {
        if (array == null) {
            return null;
        }
        return new Array<java.lang.Boolean>(BOOLEAN_TYPE, array);
    }
    
    @Ignore
    public static Array<Float> instanceForFloats(double[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Float>(Float.$TypeDescriptor$, array);
    }

    @Ignore
    public static Array<Boolean> instanceForBooleans(boolean[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Boolean>(Boolean.$TypeDescriptor$, array);
    }
    
    @Ignore
    public static Array<Character> instanceForCodePoints(int[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Character>(Character.$TypeDescriptor$, array);
    }

    @Ignore
    public static Array<Integer> instanceForIntegers(long[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(Integer.$TypeDescriptor$, array);
    }

    @Override
    public Array<Element> spanFrom(@Name("from") Integer from) {
        return span(from, Integer.instance(getSize()));
    }
    
    @Override
    public Array<Element> spanTo(@Name("to") Integer to) {
        return span(Integer.instance(0), to);
    }
    
    private static final java.lang.Object[] EMPTY_ARRAY = new java.lang.Object[0];
    
    @Override
    public Array<Element> span(@Name("from") Integer from,
            @Name("to") Integer to) {
        long fromIndex = from.longValue(); //inclusive
        long toIndex = to.longValue(); //inclusive
        boolean revert = toIndex < fromIndex;
        if (revert) {
            long swap = toIndex;
            toIndex = fromIndex;
            fromIndex = swap;
        }
        if (fromIndex<0) {
            fromIndex = 0;
        }
        long size = getSize();
        if (toIndex>=size) {
            toIndex = size-1;
        }
        if (fromIndex>=size || toIndex<0 || toIndex<fromIndex) {
            return new Array<Element>($reifiedElement, EMPTY_ARRAY);
        }
        else {
            int resultFromIndex = toInt(fromIndex); //inclusive
            int resultToIndex = toInt(toIndex+1); //exclusive
            java.lang.Object newArray;
            if (array instanceof char[]) {
                char[] copy = copyOfRange((char[])array, 
                        resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        char temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof byte[]) {
                byte[] copy = copyOfRange((byte[])array, 
                        resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        byte temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof short[]) {
                short[] copy = copyOfRange((short[])array, 
                        resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        short temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof int[]) {
                int[] copy = copyOfRange((int[])array, 
                        resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        int temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof long[]) {
                long[] copy = copyOfRange((long[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        long temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof float[]) {
                float[] copy = copyOfRange((float[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        float temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof double[]) {
                double[] copy = copyOfRange((double[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        double temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else if (array instanceof boolean[]) {
                boolean[] copy = copyOfRange((boolean[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        boolean temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            else {
                java.lang.Object[] copy = 
                        copyOfRange((java.lang.Object[])array, 
                    resultFromIndex, resultToIndex);
                if (revert) {
                    for (int i = 0; i<copy.length/2; i++) {
                        java.lang.Object temp = copy[i];
                        copy[i] = copy[copy.length-1-i];
                        copy[copy.length-1-i] = temp;
                    }
                }
                newArray = copy;
            }
            return new Array<Element>($reifiedElement, newArray);
        }
    }

    @Override
    public Array<Element> segment(@Name("from") Integer from,
            @Name("length") long length) {
        long fromIndex = from.longValue(); //inclusive
        long toIndex = fromIndex + length; //exclusive
        if (fromIndex<0) {
            fromIndex=0;
        }
        long size = getSize();
        if (toIndex > size) {
            toIndex = size;
        }
        if (fromIndex>=size || toIndex<=0) {
            return new Array<Element>($reifiedElement, EMPTY_ARRAY);
        }
        else {
            int resultToIndex = toInt(toIndex);
            int resultFromIndex = toInt(fromIndex);
            java.lang.Object newArray;
            if (array instanceof char[]) {
                newArray = copyOfRange((char[])array, 
                    resultFromIndex, resultToIndex);
            }
            else if (array instanceof byte[]) {
                newArray = copyOfRange((byte[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof short[]) {
                newArray = copyOfRange((short[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof int[]) {
                newArray = copyOfRange((int[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof long[]) {
                newArray = copyOfRange((long[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof float[]) {
                newArray = copyOfRange((float[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof double[]) {
                newArray = copyOfRange((double[])array, 
                        resultFromIndex, resultToIndex);
            }
            else if (array instanceof boolean[]) {
                newArray = copyOfRange((boolean[])array, 
                        resultFromIndex, resultToIndex);
            }
            else {
                newArray = copyOfRange((java.lang.Object[])array, 
                        resultFromIndex, resultToIndex);
            }
            return new Array<Element>($reifiedElement, newArray);
        }
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer getLastIndex() {
        return getEmpty() ? null : Integer.instance(getSize() - 1);
    }

    @Override
    public boolean getEmpty() {
        return getSize() == 0;
    }

    @Override
    public long getSize() {
        return java.lang.reflect.Array.getLength(array);
    }

    @Override
    public boolean defines(@Name("index") Integer key) {
        long ind = key.longValue();
        return ind >= 0 && ind < getSize();
    }
    
    @Ignore
    private final class ArrayIterator extends BaseIterator<Element> {
    	
	    private int index = 0;
	    // ok to cast here, since we know the size must fit in an int
	    private final int size = (int) getSize();

	    private ArrayIterator(TypeDescriptor $reified$Element) {
		    super($reified$Element);
	    }

	    @Override
	    public java.lang.Object next() {
	    	if (index<size) {
	    		return unsafeItem(index++);
	    	}
	    	else {
	    		return finished_.get_();
	    	}
	    }

	    @Override
	    public java.lang.String toString() {
	        return Array.this.toString() + ".iterator()";
	    }
    }

	@Ignore
    static final class Collector<Result> {
	    private final Callable<? extends Result> collecting;
	    private final Array<?> array;
	    private Collector(Array<?> array, 
	            Callable<? extends Result> fun) {
		    this.collecting = fun;
		    this.array = array;
	    }
	    Result call(int index) {
	    	return collecting.$call$(array.unsafeItem(index));
	    }
    }

	@Ignore
    final class ArrayIterable 
    extends AbstractArrayIterable<Element, java.lang.Object> {

        ArrayIterable() {
            // ok to cast here, since we know the size must fit in an int
            super($reifiedElement, array, (int)Array.this.getSize());
        }
        
        protected ArrayIterable(java.lang.Object array, int start,
                int len, int step) {
            super($reifiedElement, array, start, len, step);
        }

        @Override
        protected ArrayIterable newInstance(java.lang.Object array, 
                int start, int len, int step) {
            return new ArrayIterable(array, start, len, step);
        }

        @Override
        protected Element get(java.lang.Object array, int index) {
            return unsafeItem(index);
        }
        
        /** 
         * If this is an Iterable over an {@code Array<Character>} 
         * (wrapping a {@code int[]}) with unit step size, then 
         * returns a String of those codepoints. 
         * Otherwise returns null.
         */
        @Ignore
        java.lang.String stringValue() {
            if (array instanceof int[]
                    && step == 1) {
                // ok to cast here, since we know the size must fit in an int
                return new java.lang.String((int[])array, start, 
                        (int)this.getSize());
            } 
            return null;
        }
    }
    
    @Override
    public Iterator<Element> iterator() {
        return new ArrayIterator($reifiedElement);
    }
    
    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public Element get(@Name("index") Integer key) {
        return getFromFirst(key.longValue());
    }

    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public Element getFromLast(@Name("index") long key) {
        int index = toInt(key);
        int size = toInt(getSize());
        return index < 0 || index >= size ?
            null : unsafeItem(size-index-1);
    }
    
    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public Element getFromFirst(@Name("index") long key) {
        int index = toInt(key);
        return index < 0 || index >= getSize() ? 
                null : unsafeItem(index);
    }
    
    // Used by the jvm backend code to avoid boxing the index
    @SuppressWarnings("unchecked")
    @Ignore
    public Element unsafeItem(int index) {
        java.lang.Class<?> arrayElementClass = 
                $reifiedElement.getArrayElementClass();
        if (array instanceof char[]) {
            char val = ((char[])array)[index];
            return (Element) java.lang.Character.valueOf(val);
        } 
        else if (array instanceof byte[]) {
            byte val = ((byte[])array)[index];
            return (Element) java.lang.Byte.valueOf(val);
        } 
        else if (array instanceof short[]) {
            short val = ((short[])array)[index];
            return (Element) java.lang.Short.valueOf(val);
        } 
        else if (array instanceof int[]) {
            int val = ((int[])array)[index];
            if (arrayElementClass == Character.class) {
                return (Element) Character.instance(val);
            }
            else {
                return (Element) java.lang.Integer.valueOf(val);
            }
        } 
        else if (array instanceof long[]) {
            long val = ((long[])array)[index];
            if (arrayElementClass == Integer.class) {
                return (Element) Integer.instance(val);
            }
            else {
                return (Element) java.lang.Long.valueOf(val);
            }
        } 
        else if (array instanceof float[]) {
            float val = ((float[])array)[index];
            return (Element) java.lang.Float.valueOf(val);
        } 
        else if (array instanceof double[]) {
            double val = ((double[])array)[index];
            if (arrayElementClass == Float.class) {
                return (Element) Float.instance(val);
            }
            else {
                return (Element) java.lang.Double.valueOf(val);
            }
        } 
        else if (array instanceof boolean[]) {
            boolean val = ((boolean[])array)[index];
            if (arrayElementClass == Boolean.class) {
                return (Element) Boolean.instance(val);
            }
            else {
                return (Element) java.lang.Boolean.valueOf(val);
            }
        } 
        else if (array instanceof java.lang.String[]) {
            java.lang.String val = ((java.lang.String[])array)[index];
            if (arrayElementClass == String.class) {
                return (Element) String.instance(val);
            }
            else {
                return (Element) val;
            }
        } 
        else {
            return (Element) ((java.lang.Object[])array)[index];
        }
    }

    public void set(
            @Name("index") @TypeInfo("ceylon.language::Integer") long index,
            @Name("element") @TypeInfo("Element") Element element) {
        long size = getSize();
        if (index<0) {
            throw new AssertionError("array index " + index + 
                    " may not be negative");
        }
        else if (index>=size) {
            throw new AssertionError("array index " + index + 
                    " must be less than size of array " + size);
        }
        else {
            int idx = toInt(index);
            if (array instanceof char[]) {
                ((char[])array)[idx] = 
                        ((java.lang.Character)element).charValue();
            }
            else if (array instanceof byte[]) {
                ((byte[])array)[idx] = 
                        ((java.lang.Byte)element).byteValue();
            }
            else if (array instanceof short[]) {
                ((short[])array)[idx] = 
                        ((java.lang.Short)element).shortValue();
            }
            else if (array instanceof int[]) {
                if (element instanceof Character) {
                    ((int[])array)[idx] = ((Character)element).codePoint;
                }
                else {
                    ((int[])array)[idx] = 
                            ((java.lang.Integer)element).intValue();
                }
            }
            else if (array instanceof long[]) {
                if (element instanceof Integer) {
                    ((long[])array)[idx] = ((Integer)element).value;
                }
                else {
                    ((long[])array)[idx] = 
                            ((java.lang.Long) element).longValue();
                }
            }
            else if (array instanceof float[]) {
                ((float[])array)[idx] = 
                        ((java.lang.Float)element).floatValue();
            }
            else if (array instanceof double[]) {
                if (element instanceof Float) {
                    ((double[])array)[idx] = 
                            ((Float)element).value;
                }
                else {
                    ((double[])array)[idx] = 
                            ((java.lang.Double)element).doubleValue();
                }
            }
            else if (array instanceof boolean[]) {
                if (element instanceof Boolean) {
                    ((boolean[])array)[idx] = 
                            ((Boolean)element).booleanValue();
                }
                else {
                    ((boolean[])array)[idx] = 
                            ((java.lang.Boolean)element).booleanValue();
                }
            }
            else if (array instanceof java.lang.String[]) {
                if (element instanceof String) {
                    ((java.lang.String[])array)[idx] = 
                            ((String)element).value;
                }
                else {
                    ((java.lang.String[])array)[idx] = 
                            ((java.lang.String)element);
                }
            }
            else {
                ((java.lang.Object[])array)[idx] = element;
            }
        }
    }

    
    @Override
    public Array<Element> $clone() {
        return new Array<Element>($reifiedElement, copyArray());
    }
    
    private java.lang.Object copyArray() {
        if (array instanceof java.lang.Object[]) {
            return Arrays.copyOf((java.lang.Object[]) array, 
                    ((java.lang.Object[]) array).length);
        }
        else if (array instanceof long[]) {
            return Arrays.copyOf((long[]) array, 
                    ((long[]) array).length);
        }
        else if (array instanceof double[]) {
            return Arrays.copyOf((double[]) array, 
                    ((double[]) array).length);
        }
        else if (array instanceof boolean[]) {
            return Arrays.copyOf((boolean[]) array, 
                    ((boolean[]) array).length);
        }
        else if (array instanceof int[]) {
            return Arrays.copyOf((int[]) array, 
                    ((int[]) array).length);
        }
        else if (array instanceof byte[]) {
            return Arrays.copyOf((byte[]) array, 
                    ((byte[]) array).length);
        }
        else if (array instanceof short[]) {
            return Arrays.copyOf((short[]) array, 
                    ((short[]) array).length);
        }
        else if (array instanceof float[]) {
            return Arrays.copyOf((float[]) array, 
                    ((float[]) array).length);
        }
        else if (array instanceof char[]) {
            return Arrays.copyOf((char[]) array, 
                    ((char[]) array).length);
        }
        else {
            throw new AssertionError("impossible array type");
        }
    }

    @Ignore
    public java.lang.Object toArray() {
        return array;
    }

    @Override
    public boolean contains(@Name("element") 
    @TypeInfo("ceylon.language::Object")
    java.lang.Object element) {
        // FIXME Very inefficient for primitive types due to boxing
        Iterator<Element> iter = iterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (elem != null && elem.equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long count(@Name("selecting")@FunctionalParameter("(element)") 
    @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
    Callable<? extends Boolean> selecting) {
        // FIXME Very inefficient for primitive types due to boxing
        int count=0;
        Iterator<Element> iter = iterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (elem != null && selecting.$call$(elem).booleanValue()) {
                count++;
            }
        }
        return count;
    }

    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Null|Element")
    public Element getFirst() {
        if (getSize()>0) {
            return unsafeItem(0);
        }
        else {
            return null;
        }
    }
    
    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Null|Element")
    public Element getLast() {
        // ok to cast here, since we know the size must fit in an int
        final int size = (int)getSize();
        return size > 0 ? unsafeItem(size-1) : null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    @TypeInfo("ceylon.language::Sequential<Result>")
    @TypeParameters(@TypeParameter("Result"))
    public <Result> Sequential<? extends Result> 
    collect(@Ignore TypeDescriptor $reifiedResult, 
            @Name("collecting") @FunctionalParameter("(element)")
            @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            final Callable<? extends Result> collecting) {
        if (getEmpty()) {
        	return (Sequential<? extends Result>) empty_.get_();
        }
    	return new ArraySequence<Result>($reifiedResult, 
                new Array<Result>($reifiedResult, (int) getSize(), 
                		new Collector<Result>(this,collecting)));
    }
    @SuppressWarnings("unchecked")
    @Override
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> 
    sort(@Name("comparing") @FunctionalParameter("(x,y)")
    @TypeInfo("ceylon.language::Callable<ceylon.language::Comparison,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>") 
    Callable<? extends Comparison> comparing) {
        if (getEmpty()) {
        	return (Sequential<? extends Element>) empty_.get_();
        }
        Array<Element> clone = $clone();
        clone.sortInPlace(comparing);
        return new ArraySequence<Element>($reifiedElement, clone);
    }
    @SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> 
    reverse() {
        if (getEmpty()) {
        	return (Sequential<? extends Element>) empty_.get_();
        }
        Array<Element> clone = $clone();
        clone.reverseInPlace();
        return new ArraySequence<Element>($reifiedElement, clone);
    }
    
    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ?> 
    skip(@Name("skipping") long skipping) {
        int intSkip = toInt(skipping);
        // ok to cast here, since we know the size must fit in an int
        int length = (int)getSize();
        if (skipping <= 0) {
            return this;
        }
        return new ArrayIterable(this.array, intSkip, 
                Math.max(0, length-intSkip), 1);
    }

    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ?> 
    take(@Name("taking") long taking) {
        // ok to cast here, since we know the size must fit in an int
        int length = (int)getSize();
        if (taking >= length) {
            return this;
        }
        return new ArrayIterable(this.array, 0, 
                Math.max(toInt(taking), 0), 1);
    }

    @Override
    @Annotations({ @Annotation("actual") })
    @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
    public Iterable<? extends Element, ?> 
    by(@Name("step") long step) {
        if (step <= 0) {
            throw new AssertionError("step size must be greater than zero");
        } else if (step == 1) {
            return this;
        }
        return new ArrayIterable(array, 0, 
                toInt((getSize()+step-1)/step), 
                toInt(step));
    }
    
    @SuppressWarnings("unchecked")
    @Override 
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> sequence() {
    	if (getEmpty()) {
    		return (Sequential<? extends Element>) empty_.get_();
    	}
    	else {
    		return new ArraySequence<Element>($reifiedElement, 
    				new Array<Element>($reifiedElement, copyArray()));
    	}
    }
    
    @Ignore
    public int copyTo$sourcePosition(Element[] destination){
        return 0;
    }

    @Ignore
    public int copyTo$destinationPosition(Element[] destination, 
            int sourcePosition){
        return 0;
    }

    @Ignore
    public int copyTo$length(Element[] destination, 
            int sourcePosition, int destinationPosition){
        return java.lang.reflect.Array.getLength(array)-sourcePosition;
    }

    @Ignore
    public void copyTo(Array<Element> destination){
        copyTo(destination, 0, 0);
    }

    @Ignore
    public void copyTo(Array<Element> destination, int sourcePosition){
        copyTo(destination, sourcePosition, 0);
    }

    @Ignore
    public void copyTo(Array<Element> destination, 
            int sourcePosition, int destinationPosition){
        copyTo(destination, sourcePosition, destinationPosition, 
                java.lang.reflect.Array.getLength(array)-sourcePosition);
    }

    public void copyTo(@Name("destination") Array<Element> destination, 
                       @Name("sourcePosition") @Defaulted int sourcePosition, 
                       @Name("destinationPosition") @Defaulted int destinationPosition, 
                       @Name("length") @Defaulted int length){
        if (array instanceof java.lang.Object[] && 
                    destination.array instanceof java.lang.Object[] ||
            array instanceof java.lang.String[] && 
                    destination.array instanceof java.lang.String[] ||
            array instanceof boolean[] && destination.array instanceof boolean[] ||
            array instanceof int[] && destination.array instanceof int[] ||
            array instanceof long[] && destination.array instanceof long[] ||
            array instanceof float[] && destination.array instanceof float[] ||
            array instanceof double[] && destination.array instanceof double[] ||
            array instanceof char[] && destination.array instanceof char[] ||
            array instanceof byte[] && destination.array instanceof byte[] ||
            array instanceof short[] && destination.array instanceof short[]) {
                arraycopy(array, sourcePosition, destination.array, 
                        destinationPosition, length);
        }
        //TODO: can these cases even happen? If the Element 
        //      type is the same, the array type is the same!
        else {
            for (int i=0; i<length; i++) {
                int desti = i+destinationPosition;
                int sourcei = i+sourcePosition;
                if (destination.array instanceof double[]) {
                    double[] target = (double[]) destination.array;
                    if (array instanceof Object[]) {
                        java.lang.Object val = 
                                ((java.lang.Object[]) array)[sourcei];
                        if (val instanceof Float) {
                            target[desti] = 
                                    ((Float) val).value;
                        }
                        else {
                            target[desti] = 
                                    ((java.lang.Double) val).doubleValue();
                        }
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof float[]) {
                    float[] target = (float[]) destination.array;
                    if (array instanceof Object[]) {
                        java.lang.Object val = 
                                ((java.lang.Object[]) array)[sourcei];
                        target[desti] = 
                                ((java.lang.Float) val).floatValue();
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof long[]) {
                    long[] target = (long[]) destination.array;
                    if (array instanceof Object[]) {
                        java.lang.Object val = 
                                ((java.lang.Object[]) array)[sourcei];
                        if (val instanceof Integer) {
                            target[desti] = 
                                    ((Integer) val).value;
                        }
                        else {
                            target[desti] = 
                                    ((java.lang.Long) val).longValue();
                        }
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof int[]) {
                    int[] target = (int[]) destination.array;
                    if (array instanceof Object[]) {
                        java.lang.Object val = 
                                ((java.lang.Object[]) array)[sourcei];
                        if (val instanceof Character) {
                            target[desti] = 
                                    ((Character) val).codePoint;
                        }
                        else {
                            target[desti] = 
                                    ((java.lang.Integer) val).intValue();
                        }
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof short[]) {
                    short[] target = (short[]) destination.array;
                    if (array instanceof Object[]) {
                        //TODO: think about java wrapper types here!!!
                        java.lang.Object val = 
                                ((java.lang.Object[]) array)[sourcei];
                        target[desti] = 
                                ((java.lang.Short) val).shortValue();
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof byte[]) {
                    byte[] target = (byte[]) destination.array;
                    if (array instanceof Object[]) {
                        java.lang.Object val = 
                                ((java.lang.Object[]) array)[sourcei];
                        target[desti] = 
                                ((java.lang.Byte) val).byteValue();
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof boolean[]) {
                    boolean[] target = (boolean[]) destination.array;
                    if (array instanceof Object[]) {
                        java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        if (val instanceof Boolean) {
                            target[desti] = 
                                    ((Boolean) val).booleanValue();
                        }
                        else {
                            target[desti] = 
                                    ((java.lang.Boolean) val).booleanValue();
                        }
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof char[]) {
                    char[] target = (char[]) destination.array;
                    if (array instanceof Object[]) {
                        java.lang.Object val = 
                                ((java.lang.Object[]) array)[sourcei];
                        target[desti] = 
                                ((java.lang.Character) val).charValue();
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof java.lang.String[]) {
                    java.lang.String[] target = 
                            (java.lang.String[]) destination.array;
                    if (array instanceof Object[]) {
                        java.lang.Object val = 
                                ((java.lang.Object[]) array)[sourcei];
                        if (val instanceof String) {
                            String s = (String) val;
                            target[desti] = s.value;
                        }
                        else {
                            target[desti] = (java.lang.String) val;
                        }
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
                else {
                    java.lang.Object[] target = 
                            (java.lang.Object[]) destination.array;
                    if (array instanceof long[]) {
                        if ($reifiedElement==Integer.$TypeDescriptor$) {
                            target[desti] = 
                                    Integer.instance(((long[])array)[sourcei]);
                        }
                        else {
                            target[desti] = 
                                    java.lang.Long.valueOf(((long[])array)[sourcei]);
                        }
                    }
                    else if (array instanceof int[]) {
                        if ($reifiedElement==Character.$TypeDescriptor$) {
                            target[desti] = 
                                    Character.instance(((int[])array)[sourcei]);
                        }
                        else {
                            target[desti] = 
                                    java.lang.Integer.valueOf(((int[])array)[sourcei]);
                        }
                    }
                    else if (array instanceof short[]) {
                        target[desti] = 
                                java.lang.Short.valueOf(((short[])array)[sourcei]);
                    }
                    else if (array instanceof byte[]) {
                        target[desti] = 
                                java.lang.Byte.valueOf(((byte[])array)[sourcei]);
                    }
                    else if (array instanceof double[]) {
                        if ($reifiedElement==Float.$TypeDescriptor$) {
                            target[desti] = 
                                    Float.instance(((double[])array)[sourcei]);
                        }
                        else {
                            target[desti] = 
                                    java.lang.Double.valueOf(((double[])array)[sourcei]);
                        }
                    }
                    else if (array instanceof float[]) {
                        target[desti] = 
                                java.lang.Float.valueOf(((float[])array)[sourcei]);
                    }
                    else if (array instanceof boolean[]) {
                        if ($reifiedElement==Boolean.$TypeDescriptor$) {
                            target[desti] = 
                                    Boolean.instance(((boolean[])array)[sourcei]);
                        }
                        else {
                            target[desti] = 
                                    java.lang.Boolean.valueOf(((boolean[])array)[sourcei]);
                        }
                    }
                    else if (array instanceof char[]) {
                        target[desti] = 
                                java.lang.Character.valueOf(((char[])array)[sourcei]);
                    }
                    else if (array instanceof java.lang.String[]) {
                        if ($reifiedElement==String.$TypeDescriptor$) {
                            target[desti] = 
                                    String.instance(((java.lang.String[])array)[sourcei]);
                        }
                        else {
                            target[desti] = 
                                    ((java.lang.String[])array)[sourcei];
                        }
                    }
                    else {
                        throw new AssertionError(
                                "unexpected array types in copyTo()");
                    }
                }
            }
        }
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(Array.class, $reifiedElement);
    }
    
    public void reverseInPlace() {
        int size = (int) getSize();
        if (array instanceof java.lang.Object[]) {
        	for (int index=0; index<size/2; index++) {
        		java.lang.Object[] arr = (java.lang.Object[]) array;
				java.lang.Object swap = arr[index];
        		int indexFromLast = size-index-1;
        		arr[index] = arr[indexFromLast];
        		arr[indexFromLast] = swap;
        	}
        }
        else if (array instanceof long[]) {
        	for (int index=0; index<size/2; index++) {
        		long[] arr = (long[]) array;
        		long swap = arr[index];
        		int indexFromLast = size-index-1;
        		arr[index] = arr[indexFromLast];
        		arr[indexFromLast] = swap;
        	}
        }
        else if (array instanceof int[]) {
        	for (int index=0; index<size/2; index++) {
        		int[] arr = (int[]) array;
        		int swap = arr[index];
        		int indexFromLast = size-index-1;
        		arr[index] = arr[indexFromLast];
        		arr[indexFromLast] = swap;
        	}
        }
        else if (array instanceof short[]) {
        	for (int index=0; index<size/2; index++) {
        		short[] arr = (short[]) array;
        		short swap = arr[index];
        		int indexFromLast = size-index-1;
        		arr[index] = arr[indexFromLast];
        		arr[indexFromLast] = swap;
        	}
        }
        else if (array instanceof byte[]) {
        	for (int index=0; index<size/2; index++) {
        		byte[] arr = (byte[]) array;
        		byte swap = arr[index];
        		int indexFromLast = size-index-1;
        		arr[index] = arr[indexFromLast];
        		arr[indexFromLast] = swap;
        	}
        }
        else if (array instanceof double[]) {
        	for (int index=0; index<size/2; index++) {
        		double[] arr = (double[]) array;
        		double swap = arr[index];
        		int indexFromLast = size-index-1;
        		arr[index] = arr[indexFromLast];
        		arr[indexFromLast] = swap;
        	}
        }
        else if (array instanceof float[]) {
        	for (int index=0; index<size/2; index++) {
        		float[] arr = (float[]) array;
        		float swap = arr[index];
        		int indexFromLast = size-index-1;
        		arr[index] = arr[indexFromLast];
        		arr[indexFromLast] = swap;
        	}
        }
        else if (array instanceof boolean[]) {
        	for (int index=0; index<size/2; index++) {
        		boolean[] arr = (boolean[]) array;
        		boolean swap = arr[index];
        		int indexFromLast = size-index-1;
        		arr[index] = arr[indexFromLast];
        		arr[indexFromLast] = swap;
        	}
        }
        else if (array instanceof char[]) {
        	for (int index=0; index<size/2; index++) {
        		char[] arr = (char[]) array;
        		char swap = arr[index];
        		int indexFromLast = size-index-1;
        		arr[index] = arr[indexFromLast];
        		arr[indexFromLast] = swap;
        	}
        }
        else {
        	throw new AssertionError("illegal array type");
        }
    }
    
    public void sortInPlace(
            @Name("comparing") @FunctionalParameter("(x,y)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Comparison,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>") 
            final Callable<? extends Comparison> comparing) {
        java.util.List<Element> list = 
                new java.util.AbstractList<Element>() {
            @Override
            public Element get(int index) {
                return unsafeItem(index);
            }
            @Override
            public Element set(int index, Element element) {
                // Strictly this method should return the element that was at 
                // the given index, but that might require even more boxing 
                // and in practice the return value
                // doesn't seem to be used by the sorting algorithm
                Array.this.set(index, element);
                return null;
            }
            @Override
            public int size() {
                return (int)Array.this.getSize();
            }
            @Override
            public java.lang.Object[] toArray() {
                if (array instanceof java.lang.Object[] &&
                    !(array instanceof java.lang.String[])) {
                    return (java.lang.Object[]) array;
                }
                else {
                    int size = size();
                    java.lang.Object[] result = 
                            new java.lang.Object[size];
                    for (int i=0; i<size; i++) {
                        result[i] = unsafeItem(i);
                    }
                    return result;
                }
            }
        };
        Comparator<Element> comparator = 
                new Comparator<Element>() {
            public int compare(Element x, Element y) {
                Comparison result = comparing.$call$(x, y);
                if (result==larger_.get_()) return 1;
                if (result==smaller_.get_()) return -1;
                return 0;
            }
        };
        Collections.<Element>sort(list, comparator);
    }
    
}
