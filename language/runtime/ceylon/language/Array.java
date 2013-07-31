package ceylon.language;

import java.util.Arrays;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.AbstractIterator;
import com.redhat.ceylon.compiler.java.language.FilterIterable;
import com.redhat.ceylon.compiler.java.language.MapIterable;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@Class(extendsType="ceylon.language::Object")
@TypeParameters(@TypeParameter(value = "Element"))
@SatisfiedTypes({
    "ceylon.language::List<Element>",
    "ceylon.language::Cloneable<ceylon.language::Array<Element>>",
    "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::Array<Element>>"
})
public final class Array<Element> implements List<Element>, ReifiedType {
    @Ignore
    protected final ceylon.language.Category$impl $ceylon$language$Category$this;
    @Ignore
    protected final ceylon.language.Iterable$impl<Element,java.lang.Object> $ceylon$language$Iterable$this;
    @Ignore
    protected final ceylon.language.Container$impl<Element,java.lang.Object> $ceylon$language$Container$this;
    @Ignore
    protected final ceylon.language.Collection$impl<Element> $ceylon$language$Collection$this;
    @Ignore
    protected final ceylon.language.List$impl<Element> $ceylon$language$List$this;
    @Ignore
    protected final ceylon.language.Correspondence$impl<Integer,Element> $ceylon$language$Correspondence$this;
    @Ignore
    protected final ceylon.language.Ranged$impl<Integer,List<Element>> $ceylon$language$Ranged$this;
    @Ignore
    protected final ceylon.language.Cloneable$impl $ceylon$language$Cloneable$this;

    private final java.lang.Object array;
    
    @Ignore
    private TypeDescriptor $reifiedElement;

    private Array(@Ignore TypeDescriptor $reifiedElement, java.lang.Object array) {
        assert(array.getClass().isArray());
        this.$ceylon$language$Category$this = new ceylon.language.Category$impl(this);
        this.$ceylon$language$Container$this = new ceylon.language.Container$impl<Element,java.lang.Object>($reifiedElement, Null.$TypeDescriptor, this);
        this.$ceylon$language$Iterable$this = new ceylon.language.Iterable$impl<Element,java.lang.Object>($reifiedElement, Null.$TypeDescriptor, this);
        this.$ceylon$language$Collection$this = new ceylon.language.Collection$impl<Element>($reifiedElement, this);
        this.$ceylon$language$List$this = new ceylon.language.List$impl<Element>($reifiedElement, this);
        this.$ceylon$language$Correspondence$this = new ceylon.language.Correspondence$impl<Integer,Element>(Integer.$TypeDescriptor, $reifiedElement, this);
        this.$ceylon$language$Ranged$this = new ceylon.language.Ranged$impl<Integer,List<Element>>(Integer.$TypeDescriptor, TypeDescriptor.klass(Array.class, $reifiedElement), (Ranged)this);
        this.$ceylon$language$Cloneable$this = new ceylon.language.Cloneable$impl(TypeDescriptor.klass(Array.class, $reifiedElement), (Array)this);
        this.array = array;
        this.$reifiedElement = $reifiedElement;
    }

    @Ignore
    @Override
    public Category$impl $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

    @Ignore
    @Override
    public Container$impl<Element,java.lang.Object> $ceylon$language$Container$impl(){
        return $ceylon$language$Container$this;
    }

    @Ignore
    @Override
    public Iterable$impl<Element,java.lang.Object> $ceylon$language$Iterable$impl(){
        return $ceylon$language$Iterable$this;
    }

    @Ignore
    @Override
    public Collection$impl<Element> $ceylon$language$Collection$impl(){
        return $ceylon$language$Collection$this;
    }

    @Ignore
    @Override
    public List$impl<Element> $ceylon$language$List$impl(){
        return $ceylon$language$List$this;
    }

    @Ignore
    @Override
    public Correspondence$impl<Integer,Element> $ceylon$language$Correspondence$impl(){
        return $ceylon$language$Correspondence$this;
    }

    @Ignore
    @Override
    public Ranged$impl<Integer,List<? extends Element>> $ceylon$language$Ranged$impl(){
        return (Ranged$impl)$ceylon$language$Ranged$this;
    }

    @Ignore
    @Override
    public Cloneable$impl $ceylon$language$Cloneable$impl(){
        return $ceylon$language$Cloneable$this;
    }

    @Ignore
    public static Array<Character> instance(char[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Character>(Character.$TypeDescriptor, array);
    }

    @Ignore
    public static Array<Character> instanceForCodePoints(int[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Character>(Character.$TypeDescriptor, array);
    }

    @Ignore
    public static Array<Integer> instance(byte[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(Integer.$TypeDescriptor, array);
    }

    @Ignore
    public static Array<Integer> instance(short[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(Integer.$TypeDescriptor, array);
    }

    @Ignore
    public static Array<Integer> instance(int[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(Integer.$TypeDescriptor, array);
    }

    @Ignore
    public static Array<Integer> instance(long[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Integer>(Integer.$TypeDescriptor, array);
    }

    @Ignore
    public static Array<Float> instance(float[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Float>(Float.$TypeDescriptor, array);
    }

    @Ignore
    public static Array<Float> instance(double[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Float>(Float.$TypeDescriptor, array);
    }

    @Ignore
    public static Array<Boolean> instance(boolean[] array) {
        if (array == null) {
            return null;
        }
        return new Array<Boolean>(Boolean.$TypeDescriptor, array);
    }

    @Ignore
    public static <T> Array<T> instance(TypeDescriptor $reifiedT, T[] array) {
        if (array == null) {
            return null;
        }
        return new Array<T>($reifiedT, array);
    }

    @Ignore
    public static <T> Array<T> instance(TypeDescriptor $reifiedT, java.lang.Object array) {
        if (array == null) {
            return null;
        }
        return new Array<T>($reifiedT, array);
    }

    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Array<T> instance(TypeDescriptor $reifiedT, java.lang.Class typeClass, Iterable<? extends T, ? extends java.lang.Object> elements) {
        if (typeClass == null) {
            typeClass = java.lang.Object.class;
        }
        if (typeClass == int.class) {
            return (Array<T>) instance(Util.toIntArray((Iterable<? extends Integer, ? extends java.lang.Object>) elements));
        } else if (typeClass == long.class) {
            return (Array<T>) instance(Util.toLongArray((Iterable<? extends Integer, ? extends java.lang.Object>) elements));
        } else if (typeClass == byte.class) {
            return (Array<T>) instance(Util.toByteArray((Iterable<? extends Integer, ? extends java.lang.Object>) elements));
        } else if (typeClass == short.class) {
            return (Array<T>) instance(Util.toShortArray((Iterable<? extends Integer, ? extends java.lang.Object>) elements));
        } else if (typeClass == float.class) {
            return (Array<T>) instance(Util.toFloatArray((Iterable<? extends Float, ? extends java.lang.Object>) elements));
        } else if (typeClass == double.class) {
            return (Array<T>) instance(Util.toDoubleArray((Iterable<? extends Float, ? extends java.lang.Object>) elements));
        } else if (typeClass == boolean.class) {
            return (Array<T>) instance(Util.toBooleanArray((Iterable<? extends Boolean, ? extends java.lang.Object>) elements));
        } else if (typeClass == char.class) {
            return (Array<T>) instance(Util.toCharArray((Iterable<? extends Character, ? extends java.lang.Object>) elements));
        } else {
            return (Array<T>) instance($reifiedT, (T[]) Util.toArray(elements, typeClass));
        }
    }

    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Array<T> instance(TypeDescriptor $reifiedT, java.lang.Class typeClass, int size, T element) {
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
        } else {
            T[] arr = (T[]) java.lang.reflect.Array.newInstance(typeClass, size);
            if (element != null) {
                Arrays.fill(arr, element);
            }
            return (Array<T>) instance($reifiedT, arr);
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
        return (Array<? extends Element>) (to.value < 0 ? empty_.$get() : span(Integer.instance(0), to));
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
            return Array.instance($reifiedElement, typeClass, null);
        } else {
            boolean revert = toIndex<fromIndex;
            if (revert) {
                long _tmp = toIndex;
                toIndex = fromIndex;
                fromIndex = _tmp;
            }
            Array<Element> rval;
            if (typeClass == char.class) {
                rval = new Array<Element>($reifiedElement, Arrays.copyOfRange((char[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == byte.class) {
                rval = new Array<Element>($reifiedElement, Arrays.copyOfRange((byte[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == short.class) {
                rval = new Array<Element>($reifiedElement, Arrays.copyOfRange((short[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == int.class) {
                rval = new Array<Element>($reifiedElement, Arrays.copyOfRange((int[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == long.class) {
                rval = new Array<Element>($reifiedElement, Arrays.copyOfRange((long[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == float.class) {
                rval = new Array<Element>($reifiedElement, Arrays.copyOfRange((float[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == double.class) {
                rval = new Array<Element>($reifiedElement, Arrays.copyOfRange((double[])array, (int)fromIndex, (int)toIndex+1));
            } else if (typeClass == boolean.class) {
                rval = new Array<Element>($reifiedElement, Arrays.copyOfRange((boolean[])array, (int)fromIndex, (int)toIndex+1));
            } else {
                rval = new Array<Element>($reifiedElement, Arrays.copyOfRange((Element[])array, (int)fromIndex, (int)toIndex+1));
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
            return Array.instance($reifiedElement, typeClass, null);
        } else {
            if (typeClass == char.class) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((char[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == byte.class) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((byte[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == short.class) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((short[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == int.class) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((int[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == long.class) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((long[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == float.class) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((float[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == double.class) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((double[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (typeClass == boolean.class) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((boolean[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((Element[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
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
    public Iterator<Element> iterator() {
        class ArrayIterator extends AbstractIterator<Element> implements ReifiedType {
            
            public ArrayIterator() {
                super($reifiedElement);
            }

            private int idx = 0;

            @Override
            public java.lang.Object next() {
                if (idx < getSize()) {
                    return unsafeItem(idx++);
                }
                else {
                    return finished_.$get();
                }
            }

            @Override
            public java.lang.String toString() {
                return "ArrayIterator";
            }

            @Override
            @Ignore
            public TypeDescriptor $getType() {
                return TypeDescriptor.klass(ArrayIterator.class, $reifiedElement);
            }
        }
        return new ArrayIterator();
    }

    @TypeInfo("ceylon.language::Null|Element")
    @Override
    public Element get(@Name("key") Integer key) {
        long i = key.longValue();
        return get((int)i);
    }

    private Element get(int index) {
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
            int val = ((int[])array)[index];
            if($reifiedElement == Character.$TypeDescriptor)
                return (Element) Character.instance(val);
            else
                return (Element) Integer.instance(val);
        } else if (typeClass == long.class) {
            return (Element) Integer.instance(((long[])array)[index]);
        } else if (typeClass == float.class) {
            return (Element) Float.instance(((float[])array)[index]);
        } else if (typeClass == double.class) {
            return (Element) Float.instance(((double[])array)[index]);
        } else if (typeClass == boolean.class) {
            return (Element) Boolean.instance(((boolean[])array)[index]);
        } else {
            return ((Element[])array)[index];
        }
    }

    public void set(@Name("index") @TypeInfo("ceylon.language::Integer") long index,
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
                if(element instanceof Character)
                    ((int[])array)[idx] = (int) ((Character)element).intValue();
                else
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
    public boolean definesEvery(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    Iterable<? extends Integer, ? extends java.lang.Object> keys) {
        return $ceylon$language$Correspondence$this.definesEvery(keys);
    }
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public boolean definesEvery() {
//        return $ceylon$language$Correspondence$this.definesEvery((Sequential)empty_.getEmpty$());
//    }
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public Sequential definesEvery$keys() {
//        return empty_.getEmpty$();
//    }

    @Override
    @Ignore
    public boolean definesAny(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    Iterable<? extends Integer, ? extends java.lang.Object> keys) {
        return $ceylon$language$Correspondence$this.definesAny(keys);
    }
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public boolean definesAny() {
//        return $ceylon$language$Correspondence$this.definesAny((Sequential)empty_.getEmpty$());
//    }
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public Iterable definesAny$keys() {
//        return empty_.getEmpty$();
//    }

    @Override
    @Ignore
    public Sequential<? extends Element> items(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    Iterable<? extends Integer, ? extends java.lang.Object> keys){
        return $ceylon$language$Correspondence$this.items(keys);
    }
//    @Override
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public Sequential<? extends Element> items() {
//        return $ceylon$language$Correspondence$this.items((Sequential)empty_.getEmpty$());
//    }
//    @Override
//    @Ignore @SuppressWarnings({"unchecked", "rawtypes"})
//    public Sequential<? extends Integer> items$keys() {
//        return (Sequential)empty_.getEmpty$();
//    }

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
        Iterator<Element> iter = iterator();
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
        Iterator<Element> iter = iterator();
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
    public boolean containsEvery(@Name("elements")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>")
    Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }
//    @Override
//    @Ignore
//    public boolean containsEvery() {
//        return $ceylon$language$Category$this.containsEvery(empty_.getEmpty$());
//    }
//    @Override
//    @Ignore
//    public Sequential<?> containsEvery$elements() {
//        return empty_.getEmpty$();
//    }

    @Override
    @Ignore
    public boolean containsAny(@Name("elements")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>")
    Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }
//    @Override
//    @Ignore
//    public boolean containsAny() {
//        return $ceylon$language$Category$this.containsAny(empty_.getEmpty$());
//    }
//    @Override
//    @Ignore
//    public Sequential<?> containsAny$elements() {
//        return empty_.getEmpty$();
//    }
    
    @Override
    public Array<? extends Element> getRest() {
        if (getSize() < 2) {
            return array_.<Element>array($reifiedElement);
        } else {
            if (array instanceof char[]) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((char[])array, 1, (int)getSize()));
            } else if (array instanceof byte[]) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((byte[])array, 1, (int)getSize()));
            } else if (array instanceof short[]) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((short[])array, 1, (int)getSize()));
            } else if (array instanceof int[]) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((int[])array, 1, (int)getSize()));
            } else if (array instanceof long[]) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((long[])array, 1, (int)getSize()));
            } else if (array instanceof float[]) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((float[])array, 1, (int)getSize()));
            } else if (array instanceof double[]) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((double[])array, 1, (int)getSize()));
            } else if (array instanceof boolean[]) {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((boolean[])array, 1, (int)getSize()));
            } else {
                return new Array<Element>($reifiedElement, Arrays.copyOfRange((Element[])array, 1, (int)getSize()));
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
            return new Array<Element>($reifiedElement, rev);
        } else if (array instanceof byte[]) {
            byte[] __a = (byte[])array;
            byte[] rev = new byte[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>($reifiedElement, rev);
        } else if (array instanceof short[]) {
            short[] __a = (short[])array;
            short[] rev = new short[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>($reifiedElement, rev);
        } else if (array instanceof int[]) {
            int[] __a = (int[])array;
            int[] rev = new int[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>($reifiedElement, rev);
        } else if (array instanceof long[]) {
            long[] __a = (long[])array;
            long[] rev = new long[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>($reifiedElement, rev);
        } else if (array instanceof float[]) {
            float[] __a = (float[])array;
            float[] rev = new float[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>($reifiedElement, rev);
        } else if (array instanceof double[]) {
            double[] __a = (double[])array;
            double[] rev = new double[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>($reifiedElement, rev);
        } else if (array instanceof boolean[]) {
            boolean[] __a = (boolean[])array;
            boolean[] rev = new boolean[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new Array<Element>($reifiedElement, rev);
        }

        Element[] __a = (Element[])array;
        java.lang.Object[] rev = new java.lang.Object[__a.length];
        for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
            rev[i] = __a[j];
        }
        return new Array<Element>($reifiedElement, (Element[])rev);
    }

    @Override @Ignore
    public Element find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }
    @Override @Ignore
    public Element findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.findLast(f);
    }
    @Override
    @Ignore
    public Sequential<? extends Element> sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Iterable$this.sort(f);
    }
    @Override
    @Ignore
    public <Result> Iterable<? extends Result, ? extends java.lang.Object> map(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return new MapIterable<Element, java.lang.Object, Result>($reifiedElement, Null.$TypeDescriptor, $reifiedResult, this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor, this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Integer, ? extends java.lang.Object> indexes(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.indexes(f);
    }
    @Override @Ignore
    public <Result> Sequential<? extends Result> collect(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return new MapIterable<Element, java.lang.Object, Result>($reifiedElement, Null.$TypeDescriptor, $reifiedResult, this, f).getSequence();
    }
    @Override @Ignore
    public Sequential<? extends Element> select(Callable<? extends Boolean> f) {
        return new FilterIterable<Element,  Null>($reifiedElement, Null.$TypeDescriptor, this, f).getSequence();
    }
    @Override
    @Ignore
    public <Result> Result fold(@Ignore TypeDescriptor $reifiedResult, Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold($reifiedResult, ini, f);
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
	public Iterable<? extends Element, ? extends java.lang.Object> skipping(long skip) {
		return $ceylon$language$Iterable$this.skipping(skip);
	}

	@Override @Ignore
	public Iterable<? extends Element, ? extends java.lang.Object> taking(long take) {
		return $ceylon$language$Iterable$this.taking(take);
	}

	@Override @Ignore
	public Iterable<? extends Element, ? extends java.lang.Object> by(long step) {
		return $ceylon$language$Iterable$this.by(step);
	}
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, ? extends java.lang.Object> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other,Absent>Iterable chain(@Ignore TypeDescriptor $reifiedOther, @Ignore TypeDescriptor $reifiedOtherAbsent, Iterable<? extends Other, ? extends Absent> other) {
        return $ceylon$language$Iterable$this.chain($reifiedOther, $reifiedOtherAbsent, other);
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore 
    public <Other> Iterable following(@Ignore TypeDescriptor $reifiedOther, Other other) {
        return $ceylon$language$Iterable$this.following($reifiedOther, other);
    }
    @Override @Ignore
    public <Default>Iterable<?,?> defaultNullElements(@Ignore TypeDescriptor $reifiedDefault, Default defaultValue) {
        return $ceylon$language$Iterable$this.defaultNullElements($reifiedDefault, defaultValue);
    }
    /*@Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }*/
    
    @Override 
    @Ignore 
    public Sequential<? extends Element> getSequence() { 
        return $ceylon$language$Iterable$this.getSequence(); 
    }

    @Override @SuppressWarnings("rawtypes")
    @Annotations({ @Annotation("actual") })
    public <Other> Sequence<Other> withLeading(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withLeading($reifiedOther, e);
    }
    @Override @SuppressWarnings("rawtypes")
    @Annotations({ @Annotation("actual") })
    public <Other> Sequence<Other> withTrailing(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withTrailing($reifiedOther, e);
    }

    @Ignore
    public int copyTo$sourcePosition(Element[] destination){
        return 0;
    }

    @Ignore
    public int copyTo$destinationPosition(Element[] destination, int sourcePosition){
        return 0;
    }

    @Ignore
    public int copyTo$length(Element[] destination, int sourcePosition, int destinationPosition){
        return java.lang.reflect.Array.getLength(array);
    }

    @Ignore
    public void copyTo(Array<Element> destination){
        System.arraycopy(array, 0, destination.array, 0, (int)getSize());
    }

    @Ignore
    public void copyTo(Array<Element> destination, 
                       int sourcePosition){
        System.arraycopy(array, sourcePosition, destination.array, 0, (int)getSize());
    }

    @Ignore
    public void copyTo(Array<Element> destination, 
                       int sourcePosition, 
                       int destinationPosition){
        System.arraycopy(array, sourcePosition, destination.array, destinationPosition, (int)getSize());
    }

    public void copyTo(@Name("destination") Array<Element> destination, 
                       @Name("sourcePosition") @Defaulted int sourcePosition, 
                       @Name("destinationPosition") @Defaulted int destinationPosition, 
                       @Name("length") @Defaulted int length){
        System.arraycopy(array, sourcePosition, destination.array, destinationPosition, length);
    }
    
    @Override
    @Ignore
    public Iterable<? extends Integer,?> occurrencesIn(List<?> element) {
        return $ceylon$language$List$this.occurrencesIn(element);
    }

    @Override
    @Ignore
    public boolean occursAt(long index, List<?> element) {
        return $ceylon$language$List$this.occursAt(index, element);
    }

    @Override
    @Ignore
    public boolean occursAtStart(List<?> element) {
        return $ceylon$language$List$this.occursAtStart(element);
    }

    @Override
    @Ignore
    public boolean occursIn(List<?> element) {
        return $ceylon$language$List$this.occursIn(element);
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(Array.class, $reifiedElement);
    }
}
