package ceylon.language;

import java.util.Arrays;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element"))
@SatisfiedTypes({
    "ceylon.language.List<Element>",
    "ceylon.language.FixedSized<Element>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Empty|ceylon.language.Array<Element>>",
    "ceylon.language.Cloneable<ceylon.language.Array<Element>>"
})
public abstract class Array<Element> implements List<Element>, FixedSized<Element> {
    
    protected final java.lang.Object array;
    
    public Array(char... array) {
        this.array = array;
    }
    
    public Array(byte... array) {
        this.array = array;
    }
    
    public Array(short... array) {
        this.array = array;
    }
    
    public Array(int... array) {
        this.array = array;
    }
    
    public Array(long... array) {
        this.array = array;
    }
    
    public Array(float... array) {
        this.array = array;
    }
    
    public Array(double... array) {
        this.array = array;
    }
    
    public Array(boolean... array) {
        this.array = array;
    }

    public Array(java.lang.String... array) {
        this.array = array;
    }

    public Array(Element... array) {
        this.array = array;
    }
    
    @Ignore
    Array(java.util.List<Element> list) {
        this.array = list.toArray();
    }

    @Ignore
    Array(int size) {
        this.array = new Object[size];
    }
    
    @Ignore
    public static Array<Character> instance(char[] array) {
        if (array.length == 0) {
            return new EmptyArray<Character>();
        } else {
            return new NonemptyArray<Character>(array);
        }
    }
    
    @Ignore
    public static Array<Integer> instance(byte[] array) {
        if (array.length == 0) {
            return new EmptyArray<Integer>();
        } else {
            return new NonemptyArray<Integer>(array);
        }
    }
    
    @Ignore
    public static Array<Integer> instance(short[] array) {
        if (array.length == 0) {
            return new EmptyArray<Integer>();
        } else {
            return new NonemptyArray<Integer>(array);
        }
    }
    
    @Ignore
    public static Array<Integer> instance(int[] array) {
        if (array.length == 0) {
            return new EmptyArray<Integer>();
        } else {
            return new NonemptyArray<Integer>(array);
        }
    }
    
    @Ignore
    public static Array<Integer> instance(long[] array) {
        if (array.length == 0) {
            return new EmptyArray<Integer>();
        } else {
            return new NonemptyArray<Integer>(array);
        }
    }
    
    @Ignore
    public static Array<Float> instance(float[] array) {
        if (array.length == 0) {
            return new EmptyArray<Float>();
        } else {
            return new NonemptyArray<Float>(array);
        }
    }
    
    @Ignore
    public static Array<Float> instance(double[] array) {
        if (array.length == 0) {
            return new EmptyArray<Float>();
        } else {
            return new NonemptyArray<Float>(array);
        }
    }
    
    @Ignore
    public static Array<Boolean> instance(boolean[] array) {
        if (array.length == 0) {
            return new EmptyArray<Boolean>();
        } else {
            return new NonemptyArray<Boolean>(array);
        }
    }
    
    @Ignore
    public static Array<String> instance(java.lang.String[] array) {
        if (array.length == 0) {
            return new EmptyArray<String>();
        } else {
            return new NonemptyArray<String>(array);
        }
    }
    
    @Ignore
    public static <T> Array<T> instance(T[] array) {
        if (array.length == 0) {
            return new EmptyArray<T>();
        } else {
            return new NonemptyArray<T>(array);
        }
    }

    @Override
    public Element getFirst() {
        if (getEmpty()) {
            return null;
        } else {
            return unsafeItem(0);
        }
    }

    @Override
    public FixedSized<? extends Element> span(Integer from, Integer to) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long toIndex = to==null ? getSize()-1 : to.longValue();
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||toIndex<fromIndex) {
            return $empty.getEmpty();
        } else {
            if (array instanceof char[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((char[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof byte[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((byte[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof short[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((short[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof int[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((int[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof long[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((long[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof float[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((float[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof double[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((double[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof boolean[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((boolean[])array, (int)fromIndex, (int)toIndex+1));
            } else if (array instanceof java.lang.String[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((java.lang.String[])array, (int)fromIndex, (int)toIndex+1));
            } else {
                return new NonemptyArray<Element>(Arrays.copyOfRange((Element[])array, (int)fromIndex, (int)toIndex+1));
            }
        }
    }
    
    @Override
    public FixedSized<? extends Element> segment(Integer from, Integer length) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long resultLength = length.longValue();
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||resultLength==0) {
            return $empty.getEmpty();
        } else {
            if (array instanceof char[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((char[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof byte[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((byte[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof short[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((short[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof int[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((int[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof long[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((long[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof float[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((float[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof double[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((double[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof boolean[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((boolean[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else if (array instanceof java.lang.String[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((java.lang.String[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            } else {
                return new NonemptyArray<Element>(Arrays.copyOfRange((Element[])array, (int)fromIndex, (int)(fromIndex + resultLength)));
            }
        }
    }

    @Override
    @TypeInfo("ceylon.language.Integer")
    public Integer getLastIndex() {
        return Integer.instance(getSize() - 1);
    }

    @Override
    public boolean getEmpty() {
        return getSize() == 0;
    }

    @Override
    @TypeInfo("ceylon.language.Integer")
    public long getSize() {
        return java.lang.reflect.Array.getLength(array);
    }

    @Override
    public boolean defines(Integer index) {
        long ind = index.longValue();
        return ind >= 0 && ind < getSize();
    }

    @Override
    public Iterator<Element> getIterator() {
        return new ArrayIterator();
    }

    public class ArrayIterator implements Iterator<Element> {
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

    @Override
    public Element item(Integer key) {
        long index = key.longValue();
        return item((int)index);
    }

    private Element item(int index) {
        return index < 0 || index >= getSize() ? 
                null : unsafeItem(index);
    }

    private Element unsafeItem(int index) {
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

    public void setItem(@Name("index") @TypeInfo("ceylon.language.Integer") long index, @Name("element") @TypeInfo("ceylon.language.Nothing|Element") Element element) {
        int idx = (int) index;
        if (idx>=0 && idx < getSize()) {
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
    public Category getKeys() {
        return Correspondence$impl.getKeys(this);
    }

    @Override
    public boolean definesEvery(Iterable<? extends Integer> keys) {
        return Correspondence$impl.definesEvery(this, keys);
    }

    @Override
    public boolean definesAny(Iterable<? extends Integer> keys) {
        return Correspondence$impl.definesAny(this, keys);
    }

    @Override
    public ceylon.language.List<? extends Element> items(Iterable<? extends Integer> keys) {
        return Correspondence$impl.items(this, keys);
    }

    @Override
    public Array<Element> getClone() {
        return this;
    }
    
    @Override
    public java.lang.String toString() {
        return List$impl.toString(this);
    }

    public java.lang.Object toArray() {
        return array;
    }
    
    @Override
    public boolean equals(java.lang.Object that) {
        return List$impl.equals(this, that);
    }

    @Override
    public int hashCode() {
        return List$impl.hashCode(this);
    }
    
    @Override
    public boolean contains(java.lang.Object element) {
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
    public long count(java.lang.Object element) {
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
    public boolean containsEvery(Iterable<?> elements) {
        return Category$impl.containsEvery(this, elements);
    }

    @Override
    public boolean containsAny(Iterable<?> elements) {
        return Category$impl.containsAny(this, elements);
    }
}

@Ignore
class EmptyArray<Element> extends Array<Element> implements None<Element> {

    public EmptyArray() {
        super(0);
    }
    
}

@Ignore
class NonemptyArray<Element> extends Array<Element> implements Some<Element> {

    public NonemptyArray(char... array) {
        super(array);
    }
    
    public NonemptyArray(byte... array) {
        super(array);
    }
    
    public NonemptyArray(short... array) {
        super(array);
    }
    
    public NonemptyArray(int... array) {
        super(array);
    }
    
    public NonemptyArray(long... array) {
        super(array);
    }
    
    public NonemptyArray(float... array) {
        super(array);
    }
    
    public NonemptyArray(double... array) {
        super(array);
    }
    
    public NonemptyArray(boolean... array) {
        super(array);
    }

    public NonemptyArray(java.lang.String... array) {
        super(array);
    }

    public NonemptyArray(Element... array) {
        super(array);
    }
    
    @Ignore
    public NonemptyArray(java.util.List<Element> list) {
        super(list);
    }

    @Override
    public FixedSized<? extends Element> getRest() {
        if (getSize() == 1) {
            return arrayOfNone.<Element>arrayOfNone();
        } else {
            if (array instanceof char[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((char[])array, 1, (int)getSize() - 1));
            } else if (array instanceof byte[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((byte[])array, 1, (int)getSize() - 1));
            } else if (array instanceof short[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((short[])array, 1, (int)getSize() - 1));
            } else if (array instanceof int[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((int[])array, 1, (int)getSize() - 1));
            } else if (array instanceof long[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((long[])array, 1, (int)getSize() - 1));
            } else if (array instanceof float[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((float[])array, 1, (int)getSize() - 1));
            } else if (array instanceof double[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((double[])array, 1, (int)getSize() - 1));
            } else if (array instanceof boolean[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((boolean[])array, 1, (int)getSize() - 1));
            } else if (array instanceof java.lang.String[]) {
                return new NonemptyArray<Element>(Arrays.copyOfRange((java.lang.String[])array, 1, (int)getSize() - 1));
            } else {
                return new NonemptyArray<Element>(Arrays.copyOfRange((Element[])array, 1, (int)getSize() - 1));
            }
        }
    }

}
