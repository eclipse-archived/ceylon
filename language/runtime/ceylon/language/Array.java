package ceylon.language;

import java.util.Arrays;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Class(extendsType="ceylon.language.Object")
@TypeParameters(@TypeParameter(value = "Element"))
@SatisfiedTypes({
    "ceylon.language.List<Element>",
    "ceylon.language.FixedSized<Element>",
    "ceylon.language.Cloneable<ceylon.language.Array<Element>>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Array<Element>>"
})
public abstract class Array<Element> implements List<Element>, FixedSized<Element> {
    
    protected final java.lang.Object array;
    
    protected Array(char... array) {
        this.array = array;
    }
    
    protected Array(byte... array) {
        this.array = array;
    }
    
    protected Array(short... array) {
        this.array = array;
    }
    
    protected Array(int... array) {
        this.array = array;
    }
    
    protected Array(long... array) {
        this.array = array;
    }
    
    protected Array(float... array) {
        this.array = array;
    }
    
    protected Array(double... array) {
        this.array = array;
    }
    
    protected Array(boolean... array) {
        this.array = array;
    }

    protected Array(java.lang.String... array) {
        this.array = array;
    }

    protected Array(Element... array) {
        this.array = array;
    }

    @Ignore
    public static Array<Character> instance(char[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<Character>(array);
        } else {
            return new ArrayOfSome<Character>(array);
        }
    }
    
    @Ignore
    public static Array<Integer> instance(byte[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<Integer>(array);
        } else {
            return new ArrayOfSome<Integer>(array);
        }
    }
    
    @Ignore
    public static Array<Integer> instance(short[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<Integer>(array);
        } else {
            return new ArrayOfSome<Integer>(array);
        }
    }
    
    @Ignore
    public static Array<Integer> instance(int[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<Integer>(array);
        } else {
            return new ArrayOfSome<Integer>(array);
        }
    }
    
    @Ignore
    public static Array<Integer> instance(long[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<Integer>(array);
        } else {
            return new ArrayOfSome<Integer>(array);
        }
    }
    
    @Ignore
    public static Array<Float> instance(float[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<Float>(array);
        } else {
            return new ArrayOfSome<Float>(array);
        }
    }
    
    @Ignore
    public static Array<Float> instance(double[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<Float>(array);
        } else {
            return new ArrayOfSome<Float>(array);
        }
    }
    
    @Ignore
    public static Array<Boolean> instance(boolean[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<Boolean>(array);
        } else {
            return new ArrayOfSome<Boolean>(array);
        }
    }
    
    @Ignore
    public static Array<String> instance(java.lang.String[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<String>(array);
        } else {
            return new ArrayOfSome<String>(array);
        }
    }
    
    @Ignore
    public static <T> Array<T> instance(T[] array) {
        if (array.length == 0) {
            return new ArrayOfNone<T>(array);
        } else {
            return new ArrayOfSome<T>(array);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Ignore
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
    
    @SuppressWarnings("unchecked")
    @Ignore
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
    @TypeInfo("ceylon.language.Nothing|Element")
    public Element getFirst() {
        if (getEmpty()) {
            return null;
        } else {
            return unsafeItem(0);
        }
    }*/

    @Override
    public Array<? extends Element> span(@Name("from") Integer from, 
            @Name("to") @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer") Integer to) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long toIndex = to==null ? getSize()-1 : to.longValue();
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||toIndex<fromIndex) {
            return ArrayOfNone.instance(array.getClass().getComponentType(), null);
        } else {
            if (array instanceof char[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((char[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof byte[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((byte[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof short[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((short[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof int[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((int[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof long[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((long[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof float[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((float[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof double[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((double[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof boolean[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((boolean[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof java.lang.String[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((java.lang.String[])array, (int)fromIndex, (int)toIndex+1));
            } else {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((Element[])array, (int)fromIndex, (int)toIndex+1));
            }
        }
    }
    
    @Override
    public Array<? extends Element> segment(@Name("from") Integer from, 
            @Name("length") long length) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long resultLength = length;
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||resultLength<=0) {
            return ArrayOfNone.instance(array.getClass().getComponentType(), null);
        } else {
            if (array instanceof char[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((char[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof byte[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((byte[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof short[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((short[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof int[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((int[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof long[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((long[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof float[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((float[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof double[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((double[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof boolean[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((boolean[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof java.lang.String[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((java.lang.String[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((Element[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            }
        }
    }

    @Override
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Integer")
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
                    return exhausted.getExhausted();
                }
            }

            @Override
            public java.lang.String toString() {
                return "ArrayIterator";
            }

        }
        return new ArrayIterator();
    }

    @TypeInfo("ceylon.language.Nothing|Element")
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
    protected Element unsafeItem(int index) {
        if (array instanceof char[]) {
            return (Element) Character.instance(((char[])array)[index]);
        } else if (array instanceof byte[]) {
            return (Element) Integer.instance(((byte[])array)[index]);
        } else if (array instanceof short[]) {
            return (Element) Integer.instance(((short[])array)[index]);
        } else if (array instanceof int[]) {
            return (Element) Integer.instance(((int[])array)[index]);
        } else if (array instanceof long[]) {
            return (Element) Integer.instance(((long[])array)[index]);
        } else if (array instanceof float[]) {
            return (Element) Float.instance(((float[])array)[index]);
        } else if (array instanceof double[]) {
            return (Element) Float.instance(((float[])array)[index]);
        } else if (array instanceof boolean[]) {
            return (Element) Boolean.instance(((boolean[])array)[index]);
        } else if (array instanceof java.lang.String[]) {
            return (Element) String.instance(((java.lang.String[])array)[index]);
        } else {
            return ((Element[])array)[index];
        }
    }

    public void setItem(@Name("index") @TypeInfo("ceylon.language.Integer") long index, 
            @Name("element") @TypeInfo("Element") Element element) {
        int idx = (int) index;
        if (idx >= 0 && idx < getSize()) {
            if (array instanceof char[]) {
                // FIXME This is really unsafe! Should we try to do something more intelligent here??
                ((char[])array)[idx] = (char) ((Character)element).codePoint;
            } else if (array instanceof byte[]) {
                // FIXME Another unsafe conversion
                ((byte[])array)[idx] = (byte) ((Integer)element).longValue();
            } else if (array instanceof short[]) {
                // FIXME Another unsafe conversion
                ((short[])array)[idx] = (short) ((Integer)element).longValue();
            } else if (array instanceof int[]) {
                // FIXME Another unsafe conversion
                ((int[])array)[idx] = (int) ((Integer)element).longValue();
            } else if (array instanceof long[]) {
                ((long[])array)[idx] = ((Integer)element).longValue();
            } else if (array instanceof float[]) {
                // FIXME Another unsafe conversion
                ((float[])array)[idx] = (float) ((Float)element).doubleValue();
            } else if (array instanceof double[]) {
                ((double[])array)[idx] = ((Float)element).doubleValue();
            } else if (array instanceof boolean[]) {
                ((boolean[])array)[idx] = ((Boolean)element).booleanValue();
            } else if (array instanceof java.lang.String[]) {
                ((java.lang.String[])array)[idx] = ((String)element).toString();
            } else {
                ((Element[])array)[idx] = element;
            }
        }
    }

    @Override
    @Ignore
    public Category getKeys() {
        return Correspondence$impl._getKeys(this);
    }

    @Override
    @Ignore
    public boolean definesEvery(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl._definesEvery(this, keys);
    }
    @Ignore
    public boolean definesEvery() {
        return Correspondence$impl._definesEvery(this, (Iterable)$empty.getEmpty());
    }
    @Ignore
    public Iterable definesEvery$keys() {
        return $empty.getEmpty();
    }

    @Override
    @Ignore
    public boolean definesAny(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Integer>")
    Iterable<? extends Integer> keys) {
        return Correspondence$impl._definesAny(this, keys);
    }
    @Ignore
    public boolean definesAny() {
        return Correspondence$impl._definesAny(this, (Iterable)$empty.getEmpty());
    }
    @Ignore
    public Iterable definesAny$keys() {
        return $empty.getEmpty();
    }

    @Override
    @Ignore
    public Iterable<? extends Element> items(@Sequenced @Name("keys") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Integer>")
    Iterable<? extends Integer> keys){
        return Correspondence$impl._items(this, keys);
    }
    @Override
    @Ignore
    public Iterable<? extends Element> items() {
        return Correspondence$impl._items(this, (Iterable)$empty.getEmpty());
    }
    @Override
    @Ignore
    public Iterable<? extends Integer> items$keys() {
        return (Iterable)$empty.getEmpty();
    }

    @Override
    public Array<Element> getClone() {
        return this;
    }
    
    @Override
    @Ignore
    public java.lang.String toString() {
        return List$impl._toString(this);
    }

    @Ignore
    public java.lang.Object toArray() {
        return array;
    }
    
    @Override
    @Ignore
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Object") 
    java.lang.Object that) {
        return List$impl._equals(this, that);
    }

    @Override
    @Ignore
    public int hashCode() {
        return List$impl._hashCode(this);
    }
    
    @Override
    public boolean contains(@Name("element") @TypeInfo("ceylon.language.Object") 
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
    public long count(@Name("element") @TypeInfo("ceylon.language.Object") 
    java.lang.Object element) {
        // FIXME Very inefficient for primitive types due to boxing
        int count=0;
        Iterator<Element> iter = getIterator();
        java.lang.Object elem;
        while (!((elem = iter.next()) instanceof Finished)) {
            if (elem != null && element.equals(element)) {
                count++;
            }
        }
        return count;
    }

    @Override
    @Ignore
    public boolean containsEvery(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Object>")
    Iterable<?> elements) {
        return Category$impl._containsEvery(this, elements);
    }
    @Override
    @Ignore
    public boolean containsEvery() {
        return Category$impl._containsEvery(this, $empty.getEmpty());
    }
    @Override
    @Ignore
    public Iterable<?> containsEvery$elements() {
        return $empty.getEmpty();
    }

    @Override
    @Ignore
    public boolean containsAny(@Sequenced @Name("elements") 
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Object>")
    Iterable<?> elements) {
        return Category$impl._containsAny(this, elements);
    }
    @Override
    @Ignore
    public boolean containsAny() {
        return Category$impl._containsAny(this, $empty.getEmpty());
    }
    @Override
    @Ignore
    public Iterable<?> containsAny$elements() {
        return $empty.getEmpty();
    }
}

@Ignore
@Ceylon(major = 1)
class ArrayOfNone<Element> extends Array<Element> implements None<Element> {

    @Ignore
    protected ArrayOfNone(char... array) {
        super(array);
    }
    
    @Ignore
    protected ArrayOfNone(byte... array) {
        super(array);
    }
    
    @Ignore
    protected ArrayOfNone(short... array) {
        super(array);
    }
    
    @Ignore
    protected ArrayOfNone(int... array) {
        super(array);
    }
    
    @Ignore
    protected ArrayOfNone(long... array) {
        super(array);
    }
    
    @Ignore
    protected ArrayOfNone(float... array) {
        super(array);
    }
    
    @Ignore
    protected ArrayOfNone(double... array) {
        super(array);
    }
    
    @Ignore
    protected ArrayOfNone(boolean... array) {
        super(array);
    }

    @Ignore
    protected ArrayOfNone(java.lang.String... array) {
        super(array);
    }

    @Ignore
    protected ArrayOfNone(Element... array) {
        super(array);
    }
    
    @Ignore
    public static <T> Array<T> instance(java.lang.Class typeClass) {
        return instance(typeClass, null);
    }
    
    @Override
    public long getSize() {
        return 0;
    }
    
    @Override
    public boolean getEmpty() {
        return true;
    }
    
    @Override
    public Element getFirst() {
        return null;
    }

    @Override @Ignore public Iterable<? extends Element> getSequence() { return (Iterable)$empty.getEmpty(); }
    @Override @Ignore public Element find(Callable<? extends Boolean> f) { return null; }
    @Override @Ignore public Iterable<? extends Element> sorted(Callable<? extends Comparison> f) { return this; }
    @Override @Ignore public <Result> Iterable<Result> map(Callable<? extends Result> f) { return (Iterable)$empty.getEmpty(); }
    @Override @Ignore public Iterable<? extends Element> filter(Callable<? extends Boolean> f) { return this; }
    @Override @Ignore public <Result> Result fold(Result ini, Callable<? extends Result> f) { return ini; }
    @Override @Ignore public boolean any(Callable<? extends Boolean> f) { return false; }
    @Override @Ignore public boolean every(Callable<? extends Boolean> f) { return false; }
}

@Ignore
@Ceylon(major = 1)
class ArrayOfSome<Element> extends Array<Element> implements Some<Element> {

    public ArrayOfSome(char... array) {
        super(array);
    }
    
    public ArrayOfSome(byte... array) {
        super(array);
    }
    
    public ArrayOfSome(short... array) {
        super(array);
    }
    
    public ArrayOfSome(int... array) {
        super(array);
    }
    
    public ArrayOfSome(long... array) {
        super(array);
    }
    
    public ArrayOfSome(float... array) {
        super(array);
    }
    
    public ArrayOfSome(double... array) {
        super(array);
    }
    
    public ArrayOfSome(boolean... array) {
        super(array);
    }

    public ArrayOfSome(java.lang.String... array) {
        super(array);
    }

    public ArrayOfSome(Element... array) {
        super(array);
    }
    
    @Override
    public FixedSized<? extends Element> getRest() {
        if (getSize() == 1) {
            return $arrayOfNone.<Element>arrayOfNone();
        } else {
            if (array instanceof char[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((char[])array, 1, (int)getSize()));
            } else if (array instanceof byte[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((byte[])array, 1, (int)getSize()));
            } else if (array instanceof short[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((short[])array, 1, (int)getSize()));
            } else if (array instanceof int[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((int[])array, 1, (int)getSize()));
            } else if (array instanceof long[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((long[])array, 1, (int)getSize()));
            } else if (array instanceof float[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((float[])array, 1, (int)getSize()));
            } else if (array instanceof double[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((double[])array, 1, (int)getSize()));
            } else if (array instanceof boolean[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((boolean[])array, 1, (int)getSize()));
            } else if (array instanceof java.lang.String[]) {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((java.lang.String[])array, 1, (int)getSize()));
            } else {
                return new ArrayOfSome<Element>(Arrays.copyOfRange((Element[])array, 1, (int)getSize()));
            }
        }
    }

    @Override
    public boolean getEmpty() {
        return false;
    }

    /*@Override
    public FixedSized<? extends Element> getRest() {
        return span(Integer.instance(1), null);
    }*/
    
    @Override
    public Element getFirst() {
        return unsafeItem(0); //FixedSized$impl._getFirst(this);
    }
    
    @Override 
    @Ignore 
    public Iterable<? extends Element> getSequence() { 
        return Iterable$impl._getSequence(this); 
    }
    @Override 
    @Ignore 
    public Element find(Callable<? extends Boolean> f) { 
        return Iterable$impl._find(this, f); 
    }
    @Override 
    @Ignore
    public Iterable<? extends Element> sorted(Callable<? extends Comparison> f) { 
        return Iterable$impl._sorted(this, f); 
    }
    @Override 
    @Ignore 
    public <Result> Iterable<Result> map(Callable<? extends Result> f) { 
        return new MapIterable<Element, Result>(this, f); 
    }
    @Override 
    @Ignore 
    public Iterable<? extends Element> filter(Callable<? extends Boolean> f) { 
        return new FilterIterable<Element>(this, f); 
    }
    @Override 
    @Ignore 
    public <Result> Result fold(Result ini, Callable<? extends Result> f) { 
        return Iterable$impl._fold(this, ini, f); 
    }
    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return Iterable$impl._any(this, f);
    }
    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return Iterable$impl._every(this, f);
    }
}
