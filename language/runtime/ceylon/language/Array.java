package ceylon.language;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;

import java.util.Arrays;

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

@Ceylon(major = 6)
@Class(extendsType="ceylon.language::Object")
@TypeParameters(@TypeParameter(value = "Element"))
@SatisfiedTypes({
    "ceylon.language::List<Element>",
    "ceylon.language::Cloneable<ceylon.language::Array<Element>>",
    "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::Array<Element>>"
})
public final class Array<Element> 
        implements List<Element>, ReifiedType {
    
    @Ignore
    protected final Category$impl $ceylon$language$Category$this;
    @Ignore
    protected final Iterable$impl<Element,java.lang.Object> $ceylon$language$Iterable$this;
    @Ignore
    protected final Collection$impl<Element> $ceylon$language$Collection$this;
    @Ignore
    protected final List$impl<Element> $ceylon$language$List$this;
    @Ignore
    protected final Correspondence$impl<Integer,Element> $ceylon$language$Correspondence$this;
    @Ignore
    protected final Ranged$impl<Integer,List<Element>> $ceylon$language$Ranged$this;
    @Ignore
    protected final Cloneable$impl<? extends Array<Element>> $ceylon$language$Cloneable$this;

    private final java.lang.Object array;
    
    @Ignore
    private TypeDescriptor $reifiedElement;

    @Ignore
    public Array(final TypeDescriptor $reifiedElement, int size, Element element) {
        this($reifiedElement, createArray($reifiedElement, size, element));
    }
    
    public Array(@Ignore final TypeDescriptor $reifiedElement, 
            @Name("elements")
            @TypeInfo("ceylon.language::Iterable<Element,ceylon.language::Null>")
            final ceylon.language.Iterable<? extends Element,? extends java.lang.Object> elements) {
        this($reifiedElement, createArray($reifiedElement, elements));
    }

    private static <Element> java.lang.Object createArray(
            final TypeDescriptor $reifiedElement,
            final ceylon.language.Iterable<? extends Element, 
            		? extends java.lang.Object> elements) {
        int size = (int) elements.getSize();
        Iterator<?> iterator = elements.iterator();
        if ($reifiedElement instanceof TypeDescriptor.Class) {
            TypeDescriptor.Class clazz = (TypeDescriptor.Class) $reifiedElement;
            if (clazz.getKlass()==Integer.class) {
                long[] array = new long[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((Integer) iterator.next()).value;
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==Float.class) {
                double[] array = new double[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((Float) iterator.next()).value;
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==Character.class) {
                int[] array = new int[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((Character) iterator.next()).codePoint;
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==String.class) {
            	java.lang.String[] array = new java.lang.String[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((String) iterator.next()).value;
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==Boolean.class) {
                boolean[] array = new boolean[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((Boolean) iterator.next()).booleanValue();
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==java.lang.Boolean.class) {
                boolean[] array = new boolean[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((java.lang.Boolean) iterator.next()).booleanValue();
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==java.lang.Character.class) {
                char[] array = new char[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((java.lang.Character) iterator.next()).charValue();
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==java.lang.Float.class) {
                float[] array = new float[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((java.lang.Float) iterator.next()).floatValue();
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==java.lang.Double.class) {
                double[] array = new double[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((java.lang.Double) iterator.next()).doubleValue();
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==java.lang.Byte.class) {
                byte[] array = new byte[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((java.lang.Byte) iterator.next()).byteValue();
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==java.lang.Short.class) {
                short[] array = new short[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((java.lang.Short) iterator.next()).shortValue();
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==java.lang.Integer.class) {
                int[] array = new int[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((java.lang.Integer) iterator.next()).intValue();
                	}
                }
                return array;
            }
            else if (clazz.getKlass()==java.lang.Long.class) {
                long[] array = new long[size];
                if (elements instanceof Array) {
                	arraycopy(((Array)elements).array, 0, array, 0, size);
                }
                else {
                	for (int i=0; i<array.length; i++) {
                		array[i] = ((java.lang.Long) iterator.next()).longValue();
                	}
                }
                return array;
            }
            else {
            	Element[] array = (Element[])java.lang.reflect.Array
            			.newInstance(clazz.getKlass(), size);
            	for (int i=0; i<array.length; i++) {
            		array[i] = (Element) iterator.next();
            	}
                return array;
            }
        }
        else {
        	java.lang.Object[] array = new java.lang.Object[size];
        	for (int i=0; i<array.length; i++) {
                array[i] = iterator.next();
            }
        	return array;
        }
    }
    
    private static <Element> java.lang.Object createArray(
            final TypeDescriptor $reifiedElement,
            final int size, Element element) {
        if ($reifiedElement instanceof TypeDescriptor.Class) {
            TypeDescriptor.Class clazz = (TypeDescriptor.Class) $reifiedElement;
            if (clazz.getKlass()==Integer.class) {
                long[] array = new long[size];
                long value = ((Integer) element).value;
				if (value!=0l) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==Float.class) {
                double[] array = new double[size];
                double value = ((Float) element).value;
                if (value!=0.0d) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==Character.class) {
                int[] array = new int[size];
                int value = ((Character) element).codePoint;
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==String.class) {
            	java.lang.String[] array = new java.lang.String[size];
                java.lang.String value = ((String) element).value;
				Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==Boolean.class) {
                boolean[] array = new boolean[size];
                boolean value = ((Boolean) element).booleanValue();
                if (value!=false) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==java.lang.Boolean.class) {
                boolean[] array = new boolean[size];
                boolean value = ((java.lang.Boolean) element).booleanValue();
                if (value!=false) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==java.lang.Character.class) {
                char[] array = new char[size];
                char value = ((java.lang.Character) element).charValue();
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==java.lang.Float.class) {
                float[] array = new float[size];
                float value = ((java.lang.Float) element).floatValue();
                if (value!=0.0f) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==java.lang.Double.class) {
                double[] array = new double[size];
                double value = ((java.lang.Double) element).doubleValue();
                if (value!=0.0d) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==java.lang.Byte.class) {
                byte[] array = new byte[size];
                byte value = ((java.lang.Byte) element).byteValue();
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==java.lang.Short.class) {
                short[] array = new short[size];
                short value = ((java.lang.Short) element).shortValue();
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==java.lang.Integer.class) {
                int[] array = new int[size];
                int value = ((java.lang.Integer) element).intValue();
                if (value!=0) Arrays.fill(array, value); 
                return array;
            }
            else if (clazz.getKlass()==java.lang.Long.class) {
                long[] array = new long[size];
                long value = ((java.lang.Long) element).longValue();
                if (value!=0l) Arrays.fill(array, value); 
                return array;
            }
            else {
            	Element[] array = (Element[])java.lang.reflect.Array
            			.newInstance(clazz.getKlass(), size);
            	if (element!=null) Arrays.fill(array, element);
            	return array;
            }
        }
        else {
        	java.lang.Object[] array = new java.lang.Object[size];
        	if (element!=null) Arrays.fill(array, element);
        	return array;
        }
    }
    
    private Array(@Ignore TypeDescriptor $reifiedElement, java.lang.Object array) {
        assert(array.getClass().isArray());
        this.$ceylon$language$Category$this = new Category$impl(this);
        this.$ceylon$language$Iterable$this = 
        		new Iterable$impl<Element,java.lang.Object>($reifiedElement, 
        				Null.$TypeDescriptor$, this);
        this.$ceylon$language$Collection$this = 
        		new Collection$impl<Element>($reifiedElement, this);
        this.$ceylon$language$List$this = 
        		new List$impl<Element>($reifiedElement, this);
        this.$ceylon$language$Correspondence$this = 
        		new Correspondence$impl<Integer,Element>(Integer.$TypeDescriptor$, 
        				$reifiedElement, this);
        this.$ceylon$language$Ranged$this = 
        		new Ranged$impl<Integer,List<Element>>(Integer.$TypeDescriptor$, 
                        TypeDescriptor.klass(Array.class, $reifiedElement), 
                        (Ranged<Integer, ? extends List<Element>>)this);
        this.$ceylon$language$Cloneable$this = 
        		new Cloneable$impl(TypeDescriptor.klass(Array.class, 
        				$reifiedElement), this);
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
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Ranged$impl $ceylon$language$Ranged$impl(){
        return $ceylon$language$Ranged$this;
    }

    @Ignore
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Cloneable$impl $ceylon$language$Cloneable$impl(){
        return $ceylon$language$Cloneable$this;
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
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long toIndex = to.longValue();
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex) {
            return new Array<Element>($reifiedElement, EMPTY_ARRAY);
        }
        else {
            boolean revert = toIndex<fromIndex;
            if (revert) {
                long _tmp = toIndex;
                toIndex = fromIndex;
                fromIndex = _tmp;
            }
            Array<Element> rval;
            if (array instanceof char[]) {
                rval = new Array<Element>($reifiedElement, 
                        copyOfRange((char[])array, 
                        		(int)fromIndex, (int)toIndex+1));
            } else if (array instanceof byte[]) {
                rval = new Array<Element>($reifiedElement, 
                        copyOfRange((byte[])array, 
                        		(int)fromIndex, (int)toIndex+1));
            } else if (array instanceof short[]) {
                rval = new Array<Element>($reifiedElement, 
                        copyOfRange((short[])array, 
                        		(int)fromIndex, (int)toIndex+1));
            } else if (array instanceof int[]) {
                rval = new Array<Element>($reifiedElement, 
                        copyOfRange((int[])array, 
                        		(int)fromIndex, (int)toIndex+1));
            } else if (array instanceof long[]) {
                rval = new Array<Element>($reifiedElement, 
                        copyOfRange((long[])array, 
                        		(int)fromIndex, (int)toIndex+1));
            } else if (array instanceof float[]) {
                rval = new Array<Element>($reifiedElement, 
                        copyOfRange((float[])array, 
                        		(int)fromIndex, (int)toIndex+1));
            } else if (array instanceof double[]) {
                rval = new Array<Element>($reifiedElement, 
                        copyOfRange((double[])array, 
                        		(int)fromIndex, (int)toIndex+1));
            } else if (array instanceof boolean[]) {
                rval = new Array<Element>($reifiedElement, 
                        copyOfRange((boolean[])array, 
                        		(int)fromIndex, (int)toIndex+1));
            } else {
                rval = new Array<Element>($reifiedElement, 
                        copyOfRange((java.lang.Object[])array, 
                        		(int)fromIndex, (int)toIndex+1));
            }
            return revert ? rval.getReversed() : rval;
        }
    }

    @Override
    public Array<Element> segment(@Name("from") Integer from,
            @Name("length") long length) {
        long fromIndex = from.longValue();
        if (fromIndex<0) fromIndex=0;
        long resultLength = length;
        long lastIndex = getLastIndex().longValue();
        if (fromIndex>lastIndex||resultLength<=0) {
            return new Array<Element>($reifiedElement, EMPTY_ARRAY);
        } else {
            long resultFromIndex = fromIndex + resultLength;
            if (array instanceof char[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((char[])array, 
                        		(int)fromIndex, (int)resultFromIndex));
            } else if (array instanceof byte[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((byte[])array, 
                        		(int)fromIndex, (int)resultFromIndex));
            } else if (array instanceof short[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((short[])array, 
                        		(int)fromIndex, (int)resultFromIndex));
            } else if (array instanceof int[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((int[])array, 
                        		(int)fromIndex, (int)resultFromIndex));
            } else if (array instanceof long[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((long[])array, 
                        		(int)fromIndex, (int)resultFromIndex));
            } else if (array instanceof float[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((float[])array, 
                        		(int)fromIndex, (int)resultFromIndex));
            } else if (array instanceof double[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((double[])array, 
                        		(int)fromIndex, (int)resultFromIndex));
            } else if (array instanceof boolean[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((boolean[])array, 
                        		(int)fromIndex, (int)resultFromIndex));
            } else {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((java.lang.Object[])array, 
                        		(int)fromIndex, (int)resultFromIndex));
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
        class ArrayIterator extends AbstractIterator<Element> 
        implements ReifiedType {
            
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
                    return finished_.get_();
                }
            }

            @Override
            public java.lang.String toString() {
                return "ArrayIterator";
            }

            @Override
            @Ignore
            public TypeDescriptor $getType$() {
                return TypeDescriptor.klass(ArrayIterator.class, 
                		$reifiedElement);
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
        if (array instanceof char[]) {
            char val = ((char[])array)[index];
//            if ($reifiedElement == Character.$TypeDescriptor$) {
//            	//this case is for CharArray.array
//                return (Element) Character.instance(val);
//            }
//            else {
            	return (Element) new java.lang.Character(val);
//            }
        } else if (array instanceof byte[]) {
        	byte val = ((byte[])array)[index];
//            if ($reifiedElement == Integer.$TypeDescriptor$) {
//            	//this case is for ByteArray.array
//                return (Element) Integer.instance(val);
//            }
//            else {
            	return (Element) new java.lang.Byte(val);
//            }
        } else if (array instanceof short[]) {
        	short val = ((short[])array)[index];
//            if ($reifiedElement == Integer.$TypeDescriptor$) {
//            	//this case is for ShortArray.array
//                return (Element) Integer.instance(val);
//            }
//            else {
            	return (Element) new java.lang.Short(val);
//            }
        } else if (array instanceof int[]) {
            int val = ((int[])array)[index];
//            if ($reifiedElement == Integer.$TypeDescriptor$) {
//            	//this case is for IntArray.array
//                return (Element) Integer.instance(val);
//            }
//            else 
            if ($reifiedElement == Character.$TypeDescriptor$) {
                return (Element) Character.instance(val);
            }
            else {
                return (Element) new java.lang.Integer(val);
            }
        } else if (array instanceof long[]) {
            long val = ((long[])array)[index];
            if ($reifiedElement == Integer.$TypeDescriptor$) {
            	return (Element) Integer.instance(val);
            }
            else {
            	return (Element) new java.lang.Long(val);
            }
        } else if (array instanceof float[]) {
        	float val = ((float[])array)[index];
//            if ($reifiedElement == Float.$TypeDescriptor$) {
//            	//this case is for FloatArray.array
//                return (Element) Float.instance(val);
//            }
//            else {
            	return (Element) new java.lang.Float(((float[])array)[index]);
//            }
        } else if (array instanceof double[]) {
        	double val = ((double[])array)[index];
            if ($reifiedElement == Float.$TypeDescriptor$) {
            	return (Element) Float.instance(val);
            }
            else {
            	return (Element) new java.lang.Double(val);
            }
        } else if (array instanceof boolean[]) {
        	boolean val = ((boolean[])array)[index];
            if ($reifiedElement == Boolean.$TypeDescriptor$) {
            	return (Element) Boolean.instance(val);
            }
            else {
            	return (Element) new java.lang.Boolean(val);
            }
        } else if (array instanceof java.lang.String[]) {
        	java.lang.String val = ((java.lang.String[])array)[index];
			if ($reifiedElement == String.$TypeDescriptor$) {
        		return (Element) String.instance(val);
        	}
        	else {
        		return (Element) val;
        	}
        } else {
            return (Element) ((java.lang.Object[])array)[index];
        }
    }

    public void set(@Name("index") @TypeInfo("ceylon.language::Integer") long index,
            @Name("element") @TypeInfo("Element") Element element) {
    	long size = getSize();
		if (index<0) {
    		throw new AssertionException("array index " + index + 
    				" may not be negative");
    	}
    	else if (index>=size) {
    		throw new AssertionException("array index " + index + 
    				" must be less than size of array " + size);
    	}
    	else {
    		int idx = (int) index;
            if (array instanceof char[]) {
//            	if (element instanceof Character) {
//            		//this case is for CharArray.array
//            		((char[])array)[idx] = (char) ((Character) element).codePoint; //TODO: unsafe!
//            	}
//            	else {
            		((char[])array)[idx] = 
            				((java.lang.Character)element).charValue();
//            	}
            } else if (array instanceof byte[]) {
//            	if (element instanceof Integer) {
//                	//this case is for ByteArray.array
//                	((byte[])array)[idx] = (byte) ((Integer)element).value; //TODO: unsafe
//                }
//            	else {
            		((byte[])array)[idx] = 
            				((java.lang.Byte)element).byteValue();
//            	}
            } else if (array instanceof short[]) {
//            	if (element instanceof Integer) {
//                	//this case is for ShortArray.array
//                	((short[])array)[idx] = (short) ((Integer)element).value; //TODO: unsafe
//                }
//            	else {
            		((short[])array)[idx] = 
            				((java.lang.Short)element).shortValue();
//            	}
            } else if (array instanceof int[]) {
//            	if (element instanceof Integer) {
//            		//this case is for IntArray.array
//                	((int[])array)[idx] = (int) ((Integer)element).value; //TODO: unsafe
//                }
//            	else 
            	if (element instanceof Character) {
//                    ((int[])array)[idx] = ((Character)element).codePoint;
                }
                else {
                    ((int[])array)[idx] = 
                    		((java.lang.Integer)element).intValue();
                }
            } else if (array instanceof long[]) {
            	if (element instanceof Integer) {
                	((long[])array)[idx] = ((Integer)element).value;
                }
            	else {
            		((long[])array)[idx] = 
            				((java.lang.Long) element).longValue();
            	}
            } else if (array instanceof float[]) {
//            	if (element instanceof Float) {
//            		//this case is for FloatArray.array
//                	((float[])array)[idx] = (float) ((Float)element).value; //TODO: unsafe
//                }
//            	else {
            		((float[])array)[idx] = 
            				((java.lang.Float)element).floatValue();
//            	}
            } else if (array instanceof double[]) {
            	if (element instanceof Float) {
            		((double[])array)[idx] = 
            				((Float)element).value;
            	}
            	else {
            		((double[])array)[idx] = 
            				((java.lang.Double)element).doubleValue();
            	}
            } else if (array instanceof boolean[]) {
            	if (element instanceof Boolean) {
            		((boolean[])array)[idx] = 
            				((Boolean)element).booleanValue();
            	}
            	else {
            		((boolean[])array)[idx] = 
            				((java.lang.Boolean)element).booleanValue();
            	}
            } else if (array instanceof java.lang.String[]) {
            	if (element instanceof String) {
            		((java.lang.String[])array)[idx] = 
            				((String)element).value;
            	}
            	else {
            		((java.lang.String[])array)[idx] = 
            				((java.lang.String)element);
            	}
            } else {
                ((java.lang.Object[])array)[idx] = element;
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
        return new Array<Element>($reifiedElement, this);
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
    public boolean equals(@Name("that") 
    @TypeInfo("ceylon.language::Object")
    java.lang.Object that) {
        return $ceylon$language$List$this.equals(that);
    }

    @Override
    @Ignore
    public int hashCode() {
        return $ceylon$language$List$this.hashCode();
    }

    @Override
    public boolean contains(@Name("element") 
    @TypeInfo("ceylon.language::Object")
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
    public long count(@Name("selecting") 
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
    public Array<Element> getRest() {
        long size = getSize();
        if (size < 2) {
            return new Array<Element>($reifiedElement, EMPTY_ARRAY);
        } else {
            if (array instanceof char[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((char[])array, 1, (int)size));
            } else if (array instanceof byte[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((byte[])array, 1, (int)size));
            } else if (array instanceof short[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((short[])array, 1, (int)size));
            } else if (array instanceof int[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((int[])array, 1, (int)size));
            } else if (array instanceof long[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((long[])array, 1, (int)size));
            } else if (array instanceof float[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((float[])array, 1, (int)size));
            } else if (array instanceof double[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((double[])array, 1, (int)size));
            } else if (array instanceof boolean[]) {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((boolean[])array, 1, (int)size));
            } else {
                return new Array<Element>($reifiedElement, 
                        copyOfRange((java.lang.Object[])array, 1, (int)size));
            }
        }
    }
    
    @Override
    @Annotations({ @Annotation("actual") })
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
    public Element getLast() {
        final long size = getSize();
        return size > 0 ? unsafeItem((int)size-1) : null;
    }

    @Override
    @Annotations({@Annotation("actual"), @Annotation("formal")})
    public Array<Element> getReversed() {
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
        Element[] rev = (Element[])java.lang.reflect.Array
        		.newInstance(__a.getClass().getComponentType(),__a.length);
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
    public Sequential<? extends Element> 
    sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Iterable$this.sort(f);
    }
    @Override
    @Ignore
    public <Result> Iterable<? extends Result, ? extends java.lang.Object> 
    map(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return new MapIterable<Element, java.lang.Object, Result>($reifiedElement, 
        		Null.$TypeDescriptor$, $reifiedResult, this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> 
    filter(Callable<? extends Boolean> f) {
        return new FilterIterable<Element,  Null>($reifiedElement, 
        		Null.$TypeDescriptor$, this, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Integer, ? extends java.lang.Object> 
    indexes(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.indexes(f);
    }
    @Override @Ignore
    public <Result> Sequential<? extends Result> 
    collect(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return new MapIterable<Element, java.lang.Object, Result>($reifiedElement, 
        		Null.$TypeDescriptor$, $reifiedResult, this, f).getSequence();
    }
    @Override @Ignore
    public Sequential<? extends Element> select(Callable<? extends Boolean> f) {
        return new FilterIterable<Element,  Null>($reifiedElement, 
        		Null.$TypeDescriptor$, this, f).getSequence();
    }
    @Override
    @Ignore
    public <Result> Result 
    fold(@Ignore TypeDescriptor $reifiedResult, Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold($reifiedResult, ini, f);
    }
    @Override
    @Ignore
    public <Result> java.lang.Object 
    reduce(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.reduce($reifiedResult, f);
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
    public boolean longerThan(long length) {
        return getSize()>length;
    }
    
    @Override @Ignore
    public boolean shorterThan(long length) {
        return getSize()<length;
    }
    
    @Override @Ignore
	public Iterable<? extends Element, ? extends java.lang.Object> 
    skipping(long skip) {
		return $ceylon$language$Iterable$this.skipping(skip);
	}

	@Override @Ignore
	public Iterable<? extends Element, ? extends java.lang.Object> 
	taking(long take) {
		return $ceylon$language$Iterable$this.taking(take);
	}

	@Override @Ignore
	public Iterable<? extends Element, ? extends java.lang.Object> 
	by(long step) {
		return $ceylon$language$Iterable$this.by(step);
	}
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> 
    getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, 
    		? extends java.lang.Object> 
    getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    @SuppressWarnings("rawtypes")
    @Override @Ignore public <Other,Absent>Iterable 
    chain(@Ignore TypeDescriptor $reifiedOther, @Ignore TypeDescriptor $reifiedOtherAbsent, 
    		Iterable<? extends Other, ? extends Absent> other) {
        return $ceylon$language$Iterable$this.chain($reifiedOther, $reifiedOtherAbsent, other);
    }
    @Override @Ignore 
    public <Other> Iterable<?,?>
    following(@Ignore TypeDescriptor $reifiedOther, Other other) {
        return $ceylon$language$Iterable$this.following($reifiedOther, other);
    }
    @Override @Ignore
    public <Default>Iterable<?,?> 
    defaultNullElements(@Ignore TypeDescriptor $reifiedDefault, Default defaultValue) {
        return $ceylon$language$Iterable$this.defaultNullElements($reifiedDefault, defaultValue);
    }
    /*@Override @Ignore
    public <Key> Map<? extends Key, ? extends Sequence<? extends Element>> group(Callable<? extends Key> grouping) {
        return $ceylon$language$Iterable$this.group(grouping);
    }*/
    
    @Override 
    @Ignore 
    public Sequential<? extends Element> getSequence() {
        long size = getSize();
        java.lang.Object[] result;
        if ($reifiedElement instanceof TypeDescriptor.Class) {
            TypeDescriptor.Class clazz = (TypeDescriptor.Class) $reifiedElement;
            result = (Element[])java.lang.reflect.Array.newInstance(clazz.getKlass(), (int)size);
        }
        else {
            result = new java.lang.Object[(int)size];
        }
        if (array instanceof long[]) {
            long[] arr = (long[]) array;
            if ($reifiedElement==Integer.$TypeDescriptor$) {
            	for (int i=0; i<size; i++) {
            		result[i] = Integer.instance(arr[i]);
            	}
            }
            else {
            	for (int i=0; i<size; i++) {
            		result[i] = new java.lang.Long(arr[i]);
            	}
            }
        }
        else if (array instanceof double[]) {
            double[] arr = (double[]) array;
            if ($reifiedElement==Float.$TypeDescriptor$) {
            	for (int i=0; i<size; i++) {
            		result[i] = Float.instance(arr[i]);
            	}
            }
            else {
            	for (int i=0; i<size; i++) {
            		result[i] = new java.lang.Double(arr[i]);
            	}
            }
        }
        else if (array instanceof char[]) {
            char[] arr = (char[]) array;
            for (int i=0; i<size; i++) {
                result[i] = new java.lang.Character(arr[i]);
            }
        }
        else if (array instanceof int[]) {
            int[] arr = (int[]) array;
            if ($reifiedElement==Character.$TypeDescriptor$) {
            	for (int i=0; i<size; i++) {
            		result[i] = Character.instance(arr[i]);
            	}
            }
            else {
            	for (int i=0; i<size; i++) {
            		result[i] = new java.lang.Integer(arr[i]);
            	}
            }
        }
        else if (array instanceof short[]) {
            short[] arr = (short[]) array;
            for (int i=0; i<size; i++) {
                result[i] = new java.lang.Short(arr[i]);
            }
        }
        else if (array instanceof byte[]) {
            byte[] arr = (byte[]) array;
            for (int i=0; i<size; i++) {
                result[i] = new java.lang.Byte(arr[i]);
            }
        }
        else if (array instanceof float[]) {
            float[] arr = (float[]) array;
            for (int i=0; i<size; i++) {
                result[i] = new java.lang.Float(arr[i]);
            }
        }
        else if (array instanceof boolean[]) {
            boolean[] arr = (boolean[]) array;
            if ($reifiedElement==Boolean.$TypeDescriptor$) {
            	for (int i=0; i<size; i++) {
            		result[i] = Boolean.instance(arr[i]);
            	}
            }
            else {
            	for (int i=0; i<size; i++) {
            		result[i] = new java.lang.Boolean(arr[i]);
            	}
            }
        }
        else {
            arraycopy(array, 0, result, 0, (int)size);
        }
        return new ArraySequence<Element>($reifiedElement, 
        		result, 0, (int)size, false);
    }

    @Override @SuppressWarnings("rawtypes")
    @Annotations({ @Annotation("actual") })
    public <Other> Sequence 
    withLeading(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withLeading($reifiedOther, e);
    }
    @Override @SuppressWarnings("rawtypes")
    @Annotations({ @Annotation("actual") })
    public <Other> Sequence 
    withTrailing(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withTrailing($reifiedOther, e);
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
        else {
            for (int i=0; i<length; i++) {
                int desti = i+destinationPosition;
                int sourcei = i+sourcePosition;
				if (destination.array instanceof double[]) {
                    double[] target = (double[]) destination.array;
//                    if (array instanceof float[]) {
//                    	//this case is for FloatArray.array
//                        target[desti] = ((float[]) array)[sourcei];
//                    }
//                    else 
                    if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                    	if (val instanceof Float) {
                    		target[desti] = ((Float) val).value;
                    	}
                    	else {
                    		target[desti] = ((java.lang.Double) val).doubleValue();
                    	}
                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof float[]) {
                    float[] target = (float[]) destination.array;
//                    if (array instanceof double[]) {
//                    	//this case is for FloatArray.array
//                        target[desti] = (float) ((double[]) array)[sourcei];
//                    }
//                    else
                    if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        target[desti] = ((java.lang.Float) val).floatValue();
                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof long[]) {
                    long[] target = (long[]) destination.array;
//                    if (array instanceof int[]) {
//                    	//this case is for IntArray.array
//                        target[desti] = ((int[]) array)[sourcei];
//                    }
//                    else if (array instanceof short[]) {
//                    	//this case is for ShortArray.array
//                        target[desti] = ((short[]) array)[sourcei];
//                    }
//                    else if (array instanceof byte[]) {
//                    	//this case is for ByteArray.array
//                        target[desti] = ((byte[]) array)[sourcei];
//                    }
//                    else 
                    if (array instanceof Object[]) {
                        java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                    	if (val instanceof Integer) {
                    		target[desti] = ((Integer) val).value;
                    	}
                    	else {
                    		target[desti] = ((java.lang.Long) val).longValue();
                    	}
                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof int[]) {
                	int[] target = (int[]) destination.array;
//                    if (array instanceof long[]) {
//                        target[desti] = (int) ((long[]) array)[sourcei];
//                    }
//                    else if (array instanceof short[]) {
//                    	//this case is for ShortArray.array
//                        target[desti] = ((short[]) array)[sourcei];
//                    }
//                    else if (array instanceof byte[]) {
//                    	//this case is for ByteArray.array
//                        target[desti] = ((byte[]) array)[sourcei];
//                    }
//                    else if (array instanceof char[]) {
//                    	//this case is for CharArray.array
//                        target[desti] = ((char[]) array)[sourcei];
//                    }
//                    else 
                    if (array instanceof Object[]) {
                        java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        if (val instanceof Character) {
                        	target[desti] = ((Character) val).codePoint;
                        }
                        else {
                        	target[desti] = ((java.lang.Integer) val).intValue();
                        }
                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof short[]) {
                    short[] target = (short[]) destination.array;
//                    if (array instanceof long[]) {
//                        target[desti] = (short) ((long[]) array)[sourcei];
//                    }
//                    else if (array instanceof int[]) {
//                    	//this case is for IntArray.array
//                        target[desti] = (short) ((int[]) array)[sourcei];
//                    }
//                    else if (array instanceof byte[]) {
//                    	//this case is for ByteArray.array
//                        target[desti] = ((byte[]) array)[sourcei];
//                    }
//                    else
                    if (array instanceof Object[]) {
                    	//TODO: think about java wrapper types here!!!
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        target[desti] = ((java.lang.Short) val).shortValue();
                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof byte[]) {
                    byte[] target = (byte[]) destination.array;
//                    if (array instanceof long[]) {
//                        target[desti] = (byte) ((long[]) array)[sourcei];
//                    }
//                    else if (array instanceof int[]) {
//                    	//this case is for IntArray.array
//                        target[desti] = (byte) ((int[]) array)[sourcei];
//                    }
//                    else if (array instanceof short[]) {
//                    	//this case is for ShortArray.array
//                        target[desti] = (byte) ((short[]) array)[sourcei];
//                    }
//                    else
                    if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        target[desti] = ((java.lang.Byte) val).byteValue();
                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof boolean[]) {
                    boolean[] target = (boolean[]) destination.array;
                    if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                    	if (val instanceof Boolean) {
                    		target[desti] = ((Boolean) val).booleanValue();
                    	}
                    	else {
                    		target[desti] = ((java.lang.Boolean) val).booleanValue();
                    	}
                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof char[]) {
                    char[] target = (char[]) destination.array;
                    if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        target[desti] = ((java.lang.Character) val).charValue();
                    }
//                    else if (array instanceof int[]) {
//                        target[desti] = (char) ((int[]) array)[sourcei];
//                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof java.lang.String[]) {
                	java.lang.String[] target = (java.lang.String[]) destination.array;
                	if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                    	if (val instanceof String) {
                    		target[desti] = ((String) val).value;
                    	}
                    	else {
                    		target[desti] = (java.lang.String) val;
                    	}
                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
                else {
                    java.lang.Object[] target = (java.lang.Object[]) destination.array;
                    if (array instanceof long[]) {
                    	if ($reifiedElement==Integer.$TypeDescriptor$) {
                    		target[desti] = Integer.instance(((long[])array)[sourcei]);
                    	}
                    	else {
                    		target[desti] = new java.lang.Long(((long[])array)[sourcei]);
                    	}
                    }
                    else if (array instanceof int[]) {
//                    	if ($reifiedElement==Integer.$TypeDescriptor$) {
//                    		target[desti] = Integer.instance(((int[])array)[sourcei]);
//                    	}
//                    	else
                    	if ($reifiedElement==Character.$TypeDescriptor$) {
                    		target[desti] = Character.instance(((int[])array)[sourcei]);
                    	}
                    	else {
                    		target[desti] = new java.lang.Integer(((int[])array)[sourcei]);
                    	}
                    }
                    else if (array instanceof short[]) {
//                    	if ($reifiedElement==Integer.$TypeDescriptor$) {
//                    		target[desti] = Integer.instance(((short[])array)[sourcei]);
//                    	}
//                    	else {
                    		target[desti] = new java.lang.Short(((short[])array)[sourcei]);
//                    	}
                    }
                    else if (array instanceof byte[]) {
//                    	if ($reifiedElement==Integer.$TypeDescriptor$) {
//                    		target[desti] = Integer.instance(((byte[])array)[sourcei]);
//                    	}
//                    	else {
                    		target[desti] = new java.lang.Byte(((byte[])array)[sourcei]);
//                    	}
                    }
                    else if (array instanceof double[]) {
                    	if ($reifiedElement==Float.$TypeDescriptor$) {
                    		target[desti] = Float.instance(((double[])array)[sourcei]);
                    	}
                    	else {
                    		target[desti] = new java.lang.Double(((double[])array)[sourcei]);
                    	}
                    }
                    else if (array instanceof float[]) {
//                    	if ($reifiedElement==Float.$TypeDescriptor$) {
//                    		target[desti] = Float.instance(((float[])array)[sourcei]);
//                    	}
//                    	else {
                    		target[desti] = new java.lang.Float(((float[])array)[sourcei]);
//                    	}
                    }
                    else if (array instanceof boolean[]) {
                    	if ($reifiedElement==Boolean.$TypeDescriptor$) {
                    		target[desti] = Boolean.instance(((boolean[])array)[sourcei]);
                    	}
                    	else {
                    		target[desti] = new java.lang.Boolean(((boolean[])array)[sourcei]);
                    	}
                    }
                    else if (array instanceof char[]) {
//                    	if ($reifiedElement==Character.$TypeDescriptor$) {
//                    		target[desti] = Character.instance(((char[])array)[sourcei]);
//                    	}
//                    	else {
                    		target[desti] = new java.lang.Character(((char[])array)[sourcei]);
//                    	}
                    	
                    }
                    else if (array instanceof java.lang.String[]) {
                    	if ($reifiedElement==String.$TypeDescriptor$) {
                    		target[desti] = String.instance(((java.lang.String[])array)[sourcei]);
                    	}
                    	else {
                    		target[desti] = ((java.lang.String[])array)[sourcei];
                    	}
                    }
                    else {
                        throw new AssertionException("unexpected array types in copyTo()");
                    }
                }
            }
        }
    }
    
    @Override
    @Ignore
    public Iterable<? extends Integer,?> inclusions(List<?> element) {
        return $ceylon$language$List$this.inclusions(element);
    }

    @Override
    @Ignore
    public Integer firstInclusion(List<?> element) {
        return $ceylon$language$List$this.firstInclusion(element);
    }

    @Override
    @Ignore
    public Integer lastInclusion(List<?> element) {
        return $ceylon$language$List$this.lastInclusion(element);
    }

    @Override
    @Ignore
    public Iterable<? extends Integer,?> occurrences(java.lang.Object element) {
        return $ceylon$language$List$this.occurrences(element);
    }

    @Override
    @Ignore
    public Integer firstOccurrence(java.lang.Object element) {
        return $ceylon$language$List$this.firstOccurrence(element);
    }

    @Override
    @Ignore
    public Integer lastOccurrence(java.lang.Object element) {
        return $ceylon$language$List$this.lastOccurrence(element);
    }

    @Override
    @Ignore
    public boolean occurs(java.lang.Object element) {
        return $ceylon$language$List$this.occurs(element);
    }
    
    @Override
    @Ignore
    public boolean occursAt(long index, java.lang.Object element) {
        return $ceylon$language$List$this.occursAt(index, element);
    }

    @Override
    @Ignore
    public boolean includesAt(long index, List<?> element) {
        return $ceylon$language$List$this.includesAt(index, element);
    }
        
    @Override
    @Ignore
    public boolean includes(List<?> element) {
        return $ceylon$language$List$this.includes(element);
    }
        
    @Override
    @Ignore
    public boolean startsWith(List<?> element) {
        return $ceylon$language$List$this.startsWith(element);
    }
        
    @Override
    @Ignore
    public boolean endsWith(List<?> element) {
        return $ceylon$language$List$this.endsWith(element);
    }
    
    @Override @Ignore
    public List<? extends Element> trim(Callable<? extends Boolean> characters) {
        return $ceylon$language$List$this.trim(characters);
    }

    @Override @Ignore
    public List<? extends Element> 
    trimLeading(Callable<? extends Boolean> characters) {
        return $ceylon$language$List$this.trimLeading(characters);
    }

    @Override @Ignore
    public List<? extends Element> 
    trimTrailing(Callable<? extends Boolean> characters) {
        return $ceylon$language$List$this.trimTrailing(characters);
    }
    
    @Override @Ignore
    public List<? extends Element> initial(long length) {
        return $ceylon$language$List$this.initial(length);
    }
    
    @Override @Ignore
    public List<? extends Element> terminal(long length) {
        return $ceylon$language$List$this.terminal(length);
    }
    
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> 
    takingWhile(Callable<? extends Boolean> take) {
        return $ceylon$language$Iterable$this.takingWhile(take);
    }
    
    @Override @Ignore
    public Iterable<? extends Element, ? extends java.lang.Object> 
    skippingWhile(Callable<? extends Boolean> skip) {
        return $ceylon$language$Iterable$this.skippingWhile(skip);
    }
    
    @Override
    @Ignore
    public Iterable<? extends Element,?> getCycled() {
        return $ceylon$language$Iterable$this.getCycled();
    }
    @Override
    @Ignore
    public Iterable<? extends Element,?> cycle(long times) {
        return $ceylon$language$Iterable$this.cycle(times);
    }
    @Override
    @Ignore
    public List<? extends Element> repeat(long times) {
        return $ceylon$language$Iterable$this.repeat(times);
    }
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(Array.class, $reifiedElement);
    }
}
