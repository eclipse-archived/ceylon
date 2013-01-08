package ceylon.language;

import java.util.Arrays;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.FilterIterable;
import com.redhat.ceylon.compiler.java.language.MapIterable;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Class(extendsType="ceylon.language::Object")
@TypeParameters(@TypeParameter(value = "Element"))
@SatisfiedTypes({
    "ceylon.language::List<Element>",
    "ceylon.language::Cloneable<ceylon.language::Array<Element>>",
    "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::Array<Element>>"
})
public final class Array<Element> implements List<Element> {
    private final ceylon.language.Category$impl $ceylon$language$Category$this;
    private final ceylon.language.Collection$impl $ceylon$language$Collection$this;
    private final ceylon.language.Correspondence$impl $ceylon$language$Correspondence$this;
    private final ceylon.language.Iterable$impl<Element> $ceylon$language$Iterable$this;
    private final ceylon.language.List$impl<Element> $ceylon$language$List$this;

    private final java.lang.Object array;

    private Array(java.lang.Object array) {
        assert(array.getClass().isArray());
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl(this);
        this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl(this);
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element>(this);
        this.$ceylon$language$List$this = new ceylon.language.List$impl<Element>(this);
        this.array = array;
    }

    private Correspondence$impl<Integer,Element> correspondence$impl = new Correspondence$impl<Integer,Element>(this);
    
    @Ignore
    @Override
    public Correspondence$impl<? super Integer,? extends Element> $ceylon$language$Correspondence$impl(){
        return correspondence$impl;
    }

    @Override
    @Ignore
    public Correspondence$impl<? super Integer, ? extends Element>.Items Items$new(Sequence<? extends Integer> keys) {
        return correspondence$impl.Items$new(keys);
    }

    @Ignore
    public static Array<Character> instance(char[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Character>(array);
    }

    @Ignore
    public static Array<Integer> instance(byte[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(array);
    }

    @Ignore
    public static Array<Integer> instance(short[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(array);
    }

    @Ignore
    public static Array<Integer> instance(int[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(array);
    }

    @Ignore
    public static Array<Integer> instance(long[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(array);
    }

    @Ignore
    public static Array<Float> instance(float[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Float>(array);
    }

    @Ignore
    public static Array<Float> instance(double[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Float>(array);
    }

    @Ignore
    public static Array<Boolean> instance(boolean[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Boolean>(array);
    }

    @Ignore
    public static Array<String> instance(java.lang.String[] array) {
        if (array == null) {
            return null;
        }
        return new Array<String>(array);
    }

    @Ignore
    public static <T> Array<T> instance(T[] array) {
        if (array == null) {
            return null;
        }
        return new Array<T>(array);
    }

    @Ignore
    public static <T> Array<T> instance(java.lang.Object array) {
        if (array == null) {
            return null;
        }
        return new Array<T>(array);
    }

    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Array<T> instance(java.lang.Class typeClass, Iterable<? extends T> elements) {
        if (typeClass == null) {
            typeClass = java.lang.Object.class;
        }
        if (typeClass == int.class) {
            return (Array<T>) instance(Util.toIntArray((Iterable<? extends Integer>) elements));
        } else if (typeClass == long.class) {
            return (Array<T>) instance(Util.toLongArray((Iterable<? extends Integer>) elements));
        } else if (typeClass == byte.class) {
            return (Array<T>) instance(Util.toByteArray((Iterable<? extends Integer>) elements));
        } else if (typeClass == short.class) {
            return (Array<T>) instance(Util.toShortArray((Iterable<? extends Integer>) elements));
        } else if (typeClass == float.class) {
            return (Array<T>) instance(Util.toFloatArray((Iterable<? extends Float>) elements));
        } else if (typeClass == double.class) {
            return (Array<T>) instance(Util.toDoubleArray((Iterable<? extends Float>) elements));
        } else if (typeClass == boolean.class) {
            return (Array<T>) instance(Util.toBooleanArray((Iterable<? extends Boolean>) elements));
        } else if (typeClass == char.class) {
            return (Array<T>) instance(Util.toCharArray((Iterable<? extends Character>) elements));
        } else if (typeClass == java.lang.String.class) {
            return (Array<T>) instance(Util.toJavaStringArray((Iterable<? extends String>) elements));
        } else {
            return (Array<T>) instance((T[]) Util.toArray(elements, typeClass));
        }
    }

    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Array<T> instance(java.lang.Class typeClass, int size, T element) {
        if (typeClass == null) {
            typeClass = java.lang.Object.class;
        }
        if (typeClass == int.class) {
            int[] arr = new int[size];
            if (element != null) {
                Arrays.fill(arr, (int)((Integer)element).longValue());
            }
            return (Array<T>) instance(arr);
        } else if (typeClass == long.class) {
            long[] arr = new long[size];
            if (element != null) {
                Arrays.fill(arr, ((Integer)element).longValue());
            }
            return (Array<T>) instance(arr);
        } else if (typeClass == byte.class) {
            byte[] arr = new byte[size];
            if (element != null) {
                Arrays.fill(arr, (byte)((Integer)element).longValue());
            }
            return (Array<T>) instance(arr);
        } else if (typeClass == short.class) {
            short[] arr = new short[size];
            if (element != null) {
                Arrays.fill(arr, (short)((Integer)element).longValue());
            }
            return (Array<T>) instance(arr);
        } else if (typeClass == float.class) {
            float[] arr = new float[size];
            if (element != null) {
                Arrays.fill(arr, (float)((Float)element).doubleValue());
            }
            return (Array<T>) instance(arr);
        } else if (typeClass == double.class) {
            double[] arr = new double[size];
            if (element != null) {
                Arrays.fill(arr, ((Float)element).doubleValue());
            }
            return (Array<T>) instance(arr);
        } else if (typeClass == boolean.class) {
            boolean[] arr = new boolean[size];
            if (element != null) {
                Arrays.fill(arr, ((Boolean)element).booleanValue());
            }
            return (Array<T>) instance(arr);
        } else if (typeClass == char.class) {
            char[] arr = new char[size];
            if (element != null) {
             // FIXME: this is invalid
                Arrays.fill(arr, (char)((Character)element).intValue());
            }
            return (Array<T>) instance(arr);
        } else if (typeClass == java.lang.String.class) {
            java.lang.String[] arr = new java.lang.String[size];
            if (element != null) {
                Arrays.fill(arr, ((String)element).toString());
            }
            return (Array<T>) instance(arr);
        } else {
            T[] arr = (T[]) java.lang.reflect.Array.newInstance(typeClass, size);
            if (element != null) {
                Arrays.fill(arr, element);
            }
            return (Array<T>) instance(arr);
        }
    }

    /*@Override
    @TypeInfo("ceylon.language::Null|Element")
    public Element getFirst() {
        if (getEmpty()) {
            return null;
        } else {
            return unsafeItem(0);
        }
    }*/

    @Override
    public Array<? extends Element> spanFrom(@Name("from") Integer from) {
        return span(from, Integer.instance(getSize()));
    }
    @Override
    public Array<? extends Element> spanTo(@Name("to") Integer to) {
        return (Array<? extends Element>) (to.value < 0 ? empty_.getEmpty$() : span(Integer.instance(0), to));
    }

    @Override
    public Array<? extends Element> span(@Name("from") Integer from,
            @Name("to") Integer to) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long toIndex = to.longValue();
        long lastIndex = getLastIndex().longValue();
        java.lang.Class<?> typeClass = array.getClass().getComponentType();
        if (fromIndex>lastIndex) {
            return Array.instance(typeClass, null);
        } else {
            boolean revert = toIndex<fromIndex;
            if (revert) {
                long _tmp = toIndex;
                toIndex = fromIndex;
                fromIndex = _tmp;
            }
            Array<Element> rval;
            if (typeClass == char.class) {
                rval = new Array<Element>(Arrays.copyOfRange((char[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == byte.class) {
                rval = new Array<Element>(Arrays.copyOfRange((byte[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == short.class) {
                rval = new Array<Element>(Arrays.copyOfRange((short[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == int.class) {
                rval = new Array<Element>(Arrays.copyOfRange((int[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == long.class) {
                rval = new Array<Element>(Arrays.copyOfRange((long[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == float.class) {
                rval = new Array<Element>(Arrays.copyOfRange((float[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == double.class) {
                rval = new Array<Element>(Arrays.copyOfRange((double[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == boolean.class) {
                rval = new Array<Element>(Arrays.copyOfRange((boolean[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == java.lang.String.class) {
                rval = new Array<Element>(Arrays.copyOfRange((java.lang.String[])array, (int)fromIndex, (int)toIndex+1));
            } else {
                rval = new Array<Element>(Arrays.copyOfRange((Element[])array, (int)fromIndex, (int)toIndex+1));
            }
            return revert ? rval.getReversed() : rval;
        }
    }

    @Override
    public Array<? extends Element> segment(@Name("from") Integer from,
            @Name("length") long length) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long resultLength = length;
        long lastIndex = getLastIndex().longValue();
        java.lang.Class<?> typeClass = array.getClass().getComponentType();
        if (fromIndex>lastIndex||resultLength<=0) {
            return Array.instance(typeClass, null);
        } else {
            if (typeClass == char.class) {
                return new Array<Element>(Arrays.copyOfRange((char[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == byte.class) {
                return new Array<Element>(Arrays.copyOfRange((byte[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == short.class) {
                return new Array<Element>(Arrays.copyOfRange((short[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == int.class) {
                return new Array<Element>(Arrays.copyOfRange((int[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == long.class) {
                return new Array<Element>(Arrays.copyOfRange((long[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == float.class) {
                return new Array<Element>(Arrays.copyOfRange((float[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == double.class) {
                return new Array<Element>(Arrays.copyOfRange((double[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == boolean.class) {
                return new Array<Element>(Arrays.copyOfRange((boolean[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == java.lang.String.class) {
                return new Array<Element>(Arrays.copyOfRange((java.lang.String[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else {
                return new Array<Element>(Arrays.copyOfRange((Element[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            }
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
    public boolean defines(@Name("key") Integer key) {
        long ind = key.longValue();
        return ind >= 0 && ind < getSize();
    }

    @Override
    public Iterator<Element> getIterator() {
        class ArrayIterator implements Iterator<Element> {
            private int idx = 0;

            @Override
            public java.lang.Object next() {
                if (idx < getSize()) {
                    return unsafeItem(idx++);
                }
                else {
                    return finished_.getFinished$();
                }
            }

            @Override
            public java.lang.String toString() {
                return "ArrayIterator";
            }

        }
        return new ArrayIterator();
    }

    @TypeInfo("ceylon.language::Null|Element")
    @Override
    public Element item(@Name("key") Integer key) {
        long i = key.longValue();
        return item((int)i);
    }

    private Element item(int index) {
        return index < 0 || index >= getSize() ?
                null : unsafeItem(index);
    }

    @SuppressWarnings("unchecked")
    private Element unsafeItem(int index) {
        java.lang.Class<?> typeClass = array.getClass().getComponentType();
        if (typeClass == char.class) {
            return (Element) Character.instance(((char[])array)[index]);
        } else if (typeClass == byte.class) {
            return (Element) Integer.instance(((byte[])array)[index]);
        } else if (typeClass == short.class) {
            return (Element) Integer.instance(((short[])array)[index]);
        } else if (typeClass == int.class) {
            return (Element) Integer.instance(((int[])array)[index]);
        } else if (typeClass == long.class) {
            return (Element) Integer.instance(((long[])array)[index]);
        } else if (typeClass == float.class) {
            return (Element) Float.instance(((float[])array)[index]);
        } else if (typeClass == double.class) {
            return (Element) Float.instance(((double[])array)[index]);
        } else if (typeClass == boolean.class) {
            return (Element) Boolean.instance(((boolean[])array)[index]);
        } else if (typeClass == java.lang.String.class) {
            return (Element) String.instance(((java.lang.String[])array)[index]);
        } else {
            return ((Element[])array)[index];
        }
    }

    public void setItem(@Name("index") @TypeInfo("ceylon.language::Integer") long index,
            @Name("element") @TypeInfo("Element") Element element) {
        int idx = (int) index;
        if (idx >= 0 && idx < getSize()) {
            java.lang.Class<?> typeClass = array.getClass().getComponentType();
            if (typeClass == char.class) {
                // FIXME This is really unsafe! Should we try to do something more intelligent here??
                ((char[])array)[idx] = (char) ((Character)element).codePoint;
            } else if (typeClass == byte.class) {
                // FIXME Another unsafe conversion
                ((byte[])array)[idx] = (byte) ((Integer)element).longValue();
            } else if (typeClass == short.class) {
                // FIXME Another unsafe conversion
                ((short[])array)[idx] = (short) ((Integer)element).longValue();
            } else if (typeClass == int.class) {
                // FIXME Another unsafe conversion
                ((int[])array)[idx] = (int) ((Integer)element).longValue();
            } else if (typeClass == long.class) {
                ((long[])array)[idx] = ((Integer)element).longValue();
            } else if (typeClass == float.class) {
                // FIXME Another unsafe conversion
                ((float[])array)[idx] = (float) ((Float)element).doubleValue();
            } else if (typeClass == double.class) {
                ((double[])array)[idx] = ((Float)element).doubleValue();
            } else if (typeClass == boolean.class) {
                ((boolean[])array)[idx] = ((Boolean)element).booleanValue();
            } else if (typeClass == java.lang.String.class) {
                ((java.lang.String[])array)[idx] = ((String)element).toString();
            } else {
                ((Element[])array)[idx] = element;
            }
        }
    }

    @Override
    @Ignore
    public Category getKeys() {
        return $ceylon$language$Correspondence$this.getKeys();
    }

    @Override
    @Ignore
    public boolean definesEvery(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
    Sequential<? extends Integer> keys) {
        return $ceylon$language$Correspondence$this.definesEvery(keys);
    }
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean definesEvery() {
        return $ceylon$language$Correspondence$this.definesEvery((Sequential)empty_.getEmpty$());
    }
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public Sequential definesEvery$keys() {
        return empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean definesAny(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
    Sequential<? extends Integer> keys) {
        return $ceylon$language$Correspondence$this.definesAny(keys);
    }
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean definesAny() {
        return $ceylon$language$Correspondence$this.definesAny((Sequential)empty_.getEmpty$());
    }
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public Sequential definesAny$keys() {
        return empty_.getEmpty$();
    }

    @Override
    @Ignore
    public Sequential<? extends Element> items(@Sequenced @Name("keys")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Integer>")
    Sequential<? extends Integer> keys){
        return $ceylon$language$Correspondence$this.items(keys);
    }
    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public Sequential<? extends Element> items() {
        return $ceylon$language$Correspondence$this.items((Sequential)empty_.getEmpty$());
    }
    @Override
    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public Sequential<? extends Integer> items$keys() {
        return (Sequential)empty_.getEmpty$();
    }

    @Override
    public Array<Element> getClone() {
        return this;
    }

    @Override
    @Ignore
    public java.lang.String toString() {
        return $ceylon$language$Collection$this.toString();
    }

    @Ignore
    public java.lang.Object toArray() {
        return array;
    }

    @Override
    @Ignore
    public boolean equals(@Name("that") @TypeInfo("ceylon.language::Object")
    java.lang.Object that) {
        return $ceylon$language$List$this.equals(that);
    }

    @Override
    @Ignore
    public int hashCode() {
        return $ceylon$language$List$this.hashCode();
    }

    @Override
    public boolean contains(@Name("element") @TypeInfo("ceylon.language::Object")
    java.lang.Object element) {
        // FIXME Very inefficient for primitive types due to boxing
        Iterator<Element> iter = getIterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (elem != null && element.equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long count(@Name("selecting") @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>")
            Callable<? extends Boolean> selecting) {
        // FIXME Very inefficient for primitive types due to boxing
        int count=0;
        Iterator<Element> iter = getIterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (elem != null && selecting.$call(elem).booleanValue()) {
                count++;
            }
        }
        return count;
    }

    @Override
    @Ignore
    public boolean containsEvery(@Sequenced @Name("elements")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
    Sequential<?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }
    @Override
    @Ignore
    public boolean containsEvery() {
        return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
    }
    @Override
    @Ignore
    public Sequential<?> containsEvery$elements() {
        return empty_.getEmpty$();
    }

    @Override
    @Ignore
    public boolean containsAny(@Sequenced @Name("elements")
    @TypeInfo("ceylon.language::Sequential<ceylon.language::Object>")
    Sequential<?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }
    @Override
    @Ignore
    public boolean containsAny() {
        return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
    }
    @Override
    @Ignore
    public Sequential<?> containsAny$elements() {
        return empty_.getEmpty$();
    }
    
    @Override
    public Iterable<? extends Element> getRest() {
        if (getSize() < 2) {
            return array_.<Element>array();
        } else {
            if (array instanceof char[]) {
                return new Array<Element>(Arrays.copyOfRange((char[])array, 1, (int)getSize()));
            } else if (array instanceof byte[]) {
                return new Array<Element>(Arrays.copyOfRange((byte[])array, 1, (int)getSize()));
            } else if (array instanceof short[]) {
                return new Array<Element>(Arrays.copyOfRange((short[])array, 1, (int)getSize()));
            } else if (array instanceof int[]) {
                return new Array<Element>(Arrays.copyOfRange((int[])array, 1, (int)getSize()));
            } else if (array instanceof long[]) {
                return new Array<Element>(Arrays.copyOfRange((long[])array, 1, (int)getSize()));
            } else if (array instanceof float[]) {
                return new Array<Element>(Arrays.copyOfRange((float[])array, 1, (int)getSize()));
            } else if (array instanceof double[]) {
                return new Array<Element>(Arrays.copyOfRange((double[])array, 1, (int)getSize()));
            } else if (array instanceof boolean[]) {
                return new Array<Element>(Arrays.copyOfRange((boolean[])array, 1, (int)getSize()));
            } else if (array instanceof java.lang.String[]) {
                return new Array<Element>(Arrays.copyOfRange((java.lang.String[])array, 1, (int)getSize()));
            } else {
                return new Array<Element>(Arrays.copyOfRange((Element[])array, 1, (int)getSize()));
            }
        }
    }
    
    @Override
    @Annotations({ @Annotation("actual") })
    public Element getFirst() {
        return unsafeItem(0);
    }
    @Override
    @Annotations({ @Annotation("actual") })
    public Element getLast() {
        final long s = getSize();
        return s > 0 ? unsafeItem((int)s-1) : null;
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("formal")})
    public Array<? extends Element> getReversed() {
        if (getSize() < 2) {
            return this;
        } else if (array instanceof char[]) {
            char[] __a = (char[])array;
            char[] rev = new char[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>(rev);
        } else if (array instanceof byte[]) {
            byte[] __a = (byte[])array;
            byte[] rev = new byte[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>(rev);
        } else if (array instanceof short[]) {
            short[] __a = (short[])array;
            short[] rev = new short[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>(rev);
        } else if (array instanceof int[]) {
            int[] __a = (int[])array;
            int[] rev = new int[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>(rev);
        } else if (array instanceof long[]) {
            long[] __a = (long[])array;
            long[] rev = new long[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>(rev);
        } else if (array instanceof float[]) {
            float[] __a = (float[])array;
            float[] rev = new float[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>(rev);
        } else if (array instanceof double[]) {
            double[] __a = (double[])array;
            double[] rev = new double[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>(rev);
        } else if (array instanceof boolean[]) {
            boolean[] __a = (boolean[])array;
            boolean[] rev = new boolean[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>(rev);
        } else if (array instanceof java.lang.String[]) {
            java.lang.String[] __a = (java.lang.String[])array;
            java.lang.String[] rev = new java.lang.String[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>(rev);
        }

        Element[] __a = (Element[])array;
        java.lang.Object[] rev = new java.lang.Object[__a.length];
        for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
            rev[i] = __a[j];
        }
        return new Array<Element>((Element[])rev);
    }

    @Override @Ignore
    public Element find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.findLast(f);
    }
    @Override
    @Ignore
    public Sequential<? extends Element> sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Iterable$this.sort(f);
    }
    @Override
    @Ignore
    public <Result> Iterable<? extends Result> map(Callable<? extends Result> f) {
        return new MapIterable<Element, Result>(this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element>(this, f);
    }
    @Override @Ignore
    public <Result> Sequential<? extends Result> collect(Callable<? extends Result> f) {
        return new MapIterable<Element, Result>(this, f).getSequence();
    }
    @Override @Ignore
    public Sequential<? extends Element> select(Callable<? extends Boolean> f) {
        return new FilterIterable<Element>(this, f).getSequence();
    }
    @Override
    @Ignore
    public <Result> Result fold(Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold(ini, f);
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.any(f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.every(f);
    }
	@Override @Ignore
	public Iterable<? extends Element> skipping(long skip) {
		return $ceylon$language$Iterable$this.skipping(skip);
	}

	@Override @Ignore
	public Iterable<? extends Element> taking(long take) {
		return $ceylon$language$Iterable$this.taking(take);
	}

	@Override @Ignore
	public Iterable<? extends Element> by(long step) {
		return $ceylon$language$Iterable$this.by(step);
	}
    @Override @Ignore
    public Iterable<? extends Element> getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other>Iterable chain(Iterable<? extends Other> other) {
        return $ceylon$language$Iterable$this.chain(other);
    }
    @Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }
    
    @Override 
    @Ignore 
    public Sequential<? extends Element> getSequence() { 
        return $ceylon$language$Iterable$this.getSequence(); 
    }

    @Override @SuppressWarnings("rawtypes")
    @Annotations({ @Annotation("actual") })
    public <Other> Sequence withLeading(Other e) {
        return $ceylon$language$List$this.withLeading(e);
    }
    @Override @SuppressWarnings("rawtypes")
    @Annotations({ @Annotation("actual") })
    public <Other> Sequence withTrailing(Other e) {
        return $ceylon$language$List$this.withTrailing(e);
    }
}
