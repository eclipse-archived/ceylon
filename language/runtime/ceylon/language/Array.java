package ceylon.language;

import static com.redhat.ceylon.compiler.java.Util.toInt;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import ceylon.language.impl.SequenceBuilder;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.AbstractArrayIterable;
import com.redhat.ceylon.compiler.java.language.AbstractArrayIterator;
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
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class(extendsType="ceylon.language::Object", basic = false, identifiable = false)
@TypeParameters(@TypeParameter(value = "Element"))
@SatisfiedTypes({
    "ceylon.language::List<Element>",
    "ceylon.language::Ranged<ceylon.language::Integer,Element,ceylon.language::Array<Element>>"
})
public final class Array<Element> 
        implements List<Element>, ReifiedType {
    
    @Ignore
    protected final Category$impl<java.lang.Object> $ceylon$language$Category$this;
    @Ignore
    protected final Iterable$impl<Element,java.lang.Object> $ceylon$language$Iterable$this;
    @Ignore
    protected final Collection$impl<Element> $ceylon$language$Collection$this;
    @Ignore
    protected final List$impl<Element> $ceylon$language$List$this;
    @Ignore
    protected final Correspondence$impl<Integer,Element> $ceylon$language$Correspondence$this;
    
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
            final ceylon.language.Iterable<? extends Element,?> elements) {
        this($reifiedElement, createArray($reifiedElement, elements));
    }

    private static <Element> java.lang.Object createArray(
            final TypeDescriptor $reifiedElement,
            final ceylon.language.Iterable<? extends Element,?> elements) {
    	final SequenceBuilder<Element> builder;
    	final int size;
    	if (elements instanceof Array) {
    		size = Util.toInt(elements.getSize());
    		builder = null;
    	}
    	else {
    	    builder = new SequenceBuilder<Element>($reifiedElement);
    	    builder.appendAll(elements);
    	    size = Util.toInt(builder.getSize());
    	}
        java.lang.Class<?> clazz = $reifiedElement.getArrayElementClass();
        if (!$reifiedElement.containsNull()) {
        	if (clazz==String.class) {
        		//note: we don't unbox strings in an Array<String?>
        		//      because it would break javaObjectArray()
        		java.lang.String[] array = new java.lang.String[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				String s = (String) builder.get(i);
    					array[i] = s==null ? null : s.value;
        			}
        		}
        		return array;
        	}
        	else if (clazz==Integer.class) {
        		long[] array = new long[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((Integer) builder.get(i)).value;
        			}
        		}
        		return array;
        	}
        	else if (clazz==Float.class) {
        		double[] array = new double[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((Float) builder.get(i)).value;
        			}
        		}
        		return array;
        	}
        	else if (clazz==Character.class) {
        		int[] array = new int[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((Character) builder.get(i)).codePoint;
        			}
        		}
        		return array;
        	}
        	else if (clazz==Boolean.class) {
        		boolean[] array = new boolean[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((Boolean) builder.get(i)).booleanValue();
        			}
        		}
        		return array;
        	}
        	else if (clazz==java.lang.Boolean.class) {
        		boolean[] array = new boolean[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((java.lang.Boolean) builder.get(i)).booleanValue();
        			}
        		}
        		return array;
        	}
        	else if (clazz==java.lang.Character.class) {
        		char[] array = new char[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((java.lang.Character) builder.get(i)).charValue();
        			}
        		}
        		return array;
        	}
        	else if (clazz==java.lang.Float.class) {
        		float[] array = new float[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((java.lang.Float) builder.get(i)).floatValue();
        			}
        		}
        		return array;
        	}
        	else if (clazz==java.lang.Double.class) {
        		double[] array = new double[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((java.lang.Double) builder.get(i)).doubleValue();
        			}
        		}
        		return array;
        	}
        	else if (clazz==java.lang.Byte.class) {
        		byte[] array = new byte[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((java.lang.Byte) builder.get(i)).byteValue();
        			}
        		}
        		return array;
        	}
        	else if (clazz==java.lang.Short.class) {
        		short[] array = new short[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((java.lang.Short) builder.get(i)).shortValue();
        			}
        		}
        		return array;
        	}
        	else if (clazz==java.lang.Integer.class) {
        		int[] array = new int[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((java.lang.Integer) builder.get(i)).intValue();
        			}
        		}
        		return array;
        	}
        	else if (clazz==java.lang.Long.class) {
        		long[] array = new long[size];
        		if (elements instanceof Array) {
        			arraycopy(((Array<?>)elements).array, 0, array, 0, size);
        		}
        		else {
        			for (int i=0; i<size; i++) {
        				array[i] = ((java.lang.Long) builder.get(i)).longValue();
        			}
        		}
        		return array;
        	}
        }
        
        java.lang.Object[] array = (java.lang.Object[]) java.lang.reflect.Array
        		.newInstance($reifiedElement.getArrayElementClass(), size);
        for (int i=0; i<size; i++) {
        	array[i] = builder.get(i);
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

    @Ignore
    private Array(@Ignore TypeDescriptor $reifiedElement, java.lang.Object array) {
        assert(array.getClass().isArray());
        this.$ceylon$language$Category$this = 
        		new Category$impl<java.lang.Object>(Object.$TypeDescriptor$,this);
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
        this.array = array;
        this.$reifiedElement = $reifiedElement;
    }

    @Ignore
    @Override
    public Category$impl<java.lang.Object> $ceylon$language$Category$impl(){
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
    final class ArrayIterable extends AbstractArrayIterable<Element, java.lang.Object> {

        ArrayIterable() {
            // ok to cast here, since we know the size must fit in an int
            super($reifiedElement, array, (int)Array.this.getSize());
        }
        
        protected ArrayIterable(java.lang.Object array, int start,
                int len, int step) {
            super($reifiedElement, array, start, len, step);
        }

        @Override
        protected ArrayIterable newInstance(java.lang.Object array, int start,
                int len, int step) {
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
                return new java.lang.String((int[])array, start, (int)this.getSize());
            } 
            return null;
        }
    }
    
    @Override
    public Iterator<Element> iterator() {
        // ok to cast here, since we know the size must fit in an int
        return new AbstractArrayIterator<Element>($reifiedElement, 0, (int)getSize(), 1) {
            protected Element get(int index) {
                return unsafeItem(index);
            }
        };
    }
    
    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public Element get(@Name("Index") Integer key) {
        return getFromFirst(key.longValue());
    }

    @Override
    @TypeInfo("ceylon.language::Null|Element")
    public Element getFromLast(@Name("Index") long key) {
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
        java.lang.Class<?> arrayElementClass = $reifiedElement.getArrayElementClass();
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

    public void set(@Name("index") @TypeInfo("ceylon.language::Integer") long index,
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
    @Ignore
    public List<? extends ceylon.language.Integer> getKeys() {
        return $ceylon$language$List$this.getKeys();
    }

    @Override
    @Ignore
    public boolean definesEvery(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    Iterable<? extends Integer, ?> keys) {
        return $ceylon$language$Correspondence$this.definesEvery(keys);
    }

    @Override
    @Ignore
    public boolean definesAny(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    Iterable<? extends Integer, ?> keys) {
        return $ceylon$language$Correspondence$this.definesAny(keys);
    }

    @Override
    @Ignore
    public Sequential<? extends Element> items(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    Iterable<? extends Integer, ?> keys){
        return $ceylon$language$Correspondence$this.items(keys);
    }

    @Override
    public Array<Element> $clone() {
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
    @Ignore
    public boolean containsEvery(@Name("elements")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>")
    Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
    }

    @Override
    @Ignore
    public boolean containsAny(@Name("elements")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>")
    Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
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
    
    @Override
    @Ignore
    public List<? extends Element> getRest() {
        return $ceylon$language$List$this.getRest();
    }
    
    @Override
    @Ignore
    public List<? extends Element> sublistFrom(long index) {
        return $ceylon$language$List$this.sublistFrom(index);
    }
    
    @Override
    @Ignore
    public List<? extends Element> sublistTo(long index) {
        return $ceylon$language$List$this.sublistTo(index);
    }
    
    @Override
    @Ignore
    public List<? extends Element> sublist(long from, long to) {
        return $ceylon$language$List$this.sublist(from, to);
    }
    
    @Override
    @Ignore
    public List<? extends Element> getReversed() {
        return $ceylon$language$List$this.getReversed();
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
    public <Result> Iterable<? extends Result, ?> 
    map(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.map($reifiedResult, f);
    }
    @Override
    @Ignore
    public Iterable<? extends Element, ?> 
    filter(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.filter(f);
    }
    @Override
    @Ignore
    public Iterable<? extends Integer, ?> 
    indexesWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.indexesWhere(f);
    }
    @Override
    @Ignore
    public Integer 
    firstIndexWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.firstIndexWhere(f);
    }
    @Override
    @Ignore
    public Integer 
    lastIndexWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.lastIndexWhere(f);
    }
    @Override @Ignore
    public <Result> Sequential<? extends Result> 
    collect(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.collect($reifiedResult, f);
    }
    @Override @Ignore
    public Sequential<? extends Element> select(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.select(f);
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
        return $ceylon$language$List$this.longerThan(length);
    }
    
    @Override @Ignore
    public boolean shorterThan(long length) {
        return $ceylon$language$List$this.shorterThan(length);
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
	
    @Override @Ignore
    public Iterable<? extends Element, ?> 
    getCoalesced() {
        return $ceylon$language$Iterable$this.getCoalesced();
    }
    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Element>, 
    		?> 
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
    
    @Override 
    @TypeInfo("ceylon.language::Sequential<Element>")
    public Sequential<? extends Element> sequence() {
        // ok to cast here, since we know the size must fit in an int
        int size = (int)getSize();
        java.lang.Class<?> arrayElementClass = $reifiedElement.getArrayElementClass();
        java.lang.Object[] result = (java.lang.Object[])java.lang.reflect.Array
        		.newInstance(arrayElementClass, size);
        if (array instanceof long[]) {
            long[] arr = (long[]) array;
            if (arrayElementClass==Integer.class) {
            	for (int i=0; i<size; i++) {
            		result[i] = Integer.instance(arr[i]);
            	}
            }
            else {
            	for (int i=0; i<size; i++) {
            		result[i] = java.lang.Long.valueOf(arr[i]);
            	}
            }
        }
        else if (array instanceof double[]) {
            double[] arr = (double[]) array;
            if (arrayElementClass==Float.class) {
            	for (int i=0; i<size; i++) {
            		result[i] = Float.instance(arr[i]);
            	}
            }
            else {
            	for (int i=0; i<size; i++) {
            		result[i] = java.lang.Double.valueOf(arr[i]);
            	}
            }
        }
        else if (array instanceof char[]) {
            char[] arr = (char[]) array;
            for (int i=0; i<size; i++) {
                result[i] = java.lang.Character.valueOf(arr[i]);
            }
        }
        else if (array instanceof java.lang.String[]) {
            java.lang.String[] arr = (java.lang.String[]) array;
            if (arrayElementClass==String.class) {
                for (int i=0; i<size; i++) {
                    result[i] = String.instance(arr[i]);
                }
            } else {
                for (int i=0; i<size; i++) {
                    result[i] = arr[i];
                }
            }
        }
        else if (array instanceof int[]) {
            int[] arr = (int[]) array;
            if (arrayElementClass==Character.class) {
            	for (int i=0; i<size; i++) {
            		result[i] = Character.instance(arr[i]);
            	}
            }
            else {
            	for (int i=0; i<size; i++) {
            		result[i] = java.lang.Integer.valueOf(arr[i]);
            	}
            }
        }
        else if (array instanceof short[]) {
            short[] arr = (short[]) array;
            for (int i=0; i<size; i++) {
                result[i] = java.lang.Short.valueOf(arr[i]);
            }
        }
        else if (array instanceof byte[]) {
            byte[] arr = (byte[]) array;
            for (int i=0; i<size; i++) {
                result[i] = java.lang.Byte.valueOf(arr[i]);
            }
        }
        else if (array instanceof float[]) {
            float[] arr = (float[]) array;
            for (int i=0; i<size; i++) {
                result[i] = java.lang.Float.valueOf(arr[i]);
            }
        }
        else if (array instanceof boolean[]) {
            boolean[] arr = (boolean[]) array;
            if (arrayElementClass==Boolean.class) {
            	for (int i=0; i<size; i++) {
            		result[i] = Boolean.instance(arr[i]);
            	}
            }
            else {
            	for (int i=0; i<size; i++) {
            		result[i] = java.lang.Boolean.valueOf(arr[i]);
            	}
            }
        }
        else {
            arraycopy(array, 0, result, 0, size);
        }
        return new ArraySequence<Element>($reifiedElement, 
        		new Array<Element>($reifiedElement, result));
    }

    @Override @Ignore @SuppressWarnings("rawtypes")
    public <Other> Tuple<java.lang.Object,? extends Other,? extends Sequential<? extends Element>> 
    withLeading(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withLeading($reifiedOther, e);
    }
    
    @Override @Ignore @SuppressWarnings("rawtypes")
    public <Other> Sequence 
    withTrailing(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withTrailing($reifiedOther, e);
    }

    @Override @Ignore @SuppressWarnings("rawtypes")
    public <Other> Sequential 
    append(@Ignore TypeDescriptor $reifiedOther, Iterable<? extends Other, ?> e) {
        return $ceylon$language$List$this.append($reifiedOther, e);
    }

    @Override @Ignore @SuppressWarnings("rawtypes")
    public <Other> List
    extend(@Ignore TypeDescriptor $reifiedOther, List<? extends Other> e) {
        return $ceylon$language$List$this.extend($reifiedOther, e);
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
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                    	if (val instanceof Float) {
                    		target[desti] = ((Float) val).value;
                    	}
                    	else {
                    		target[desti] = ((java.lang.Double) val).doubleValue();
                    	}
                    }
                    else {
                        throw new AssertionError("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof float[]) {
                    float[] target = (float[]) destination.array;
                    if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        target[desti] = ((java.lang.Float) val).floatValue();
                    }
                    else {
                        throw new AssertionError("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof long[]) {
                    long[] target = (long[]) destination.array;
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
                        throw new AssertionError("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof int[]) {
                	int[] target = (int[]) destination.array;
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
                        throw new AssertionError("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof short[]) {
                    short[] target = (short[]) destination.array;
                    if (array instanceof Object[]) {
                    	//TODO: think about java wrapper types here!!!
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        target[desti] = ((java.lang.Short) val).shortValue();
                    }
                    else {
                        throw new AssertionError("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof byte[]) {
                    byte[] target = (byte[]) destination.array;
                    if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        target[desti] = ((java.lang.Byte) val).byteValue();
                    }
                    else {
                        throw new AssertionError("unexpected array types in copyTo()");
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
                        throw new AssertionError("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof char[]) {
                    char[] target = (char[]) destination.array;
                    if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                        target[desti] = ((java.lang.Character) val).charValue();
                    }
                    else {
                        throw new AssertionError("unexpected array types in copyTo()");
                    }
                }
                else if (destination.array instanceof java.lang.String[]) {
                	java.lang.String[] target = (java.lang.String[]) destination.array;
                	if (array instanceof Object[]) {
                    	java.lang.Object val = ((java.lang.Object[]) array)[sourcei];
                    	if (val instanceof String) {
                    		String s = (String) val;
							target[desti] = s.value;
                    	}
                    	else {
                    		target[desti] = (java.lang.String) val;
                    	}
                    }
                    else {
                        throw new AssertionError("unexpected array types in copyTo()");
                    }
                }
                else {
                    java.lang.Object[] target = (java.lang.Object[]) destination.array;
                    if (array instanceof long[]) {
                    	if ($reifiedElement==Integer.$TypeDescriptor$) {
                    		target[desti] = Integer.instance(((long[])array)[sourcei]);
                    	}
                    	else {
                    		target[desti] = java.lang.Long.valueOf(((long[])array)[sourcei]);
                    	}
                    }
                    else if (array instanceof int[]) {
                    	if ($reifiedElement==Character.$TypeDescriptor$) {
                    		target[desti] = Character.instance(((int[])array)[sourcei]);
                    	}
                    	else {
                    		target[desti] = java.lang.Integer.valueOf(((int[])array)[sourcei]);
                    	}
                    }
                    else if (array instanceof short[]) {
                    	target[desti] = java.lang.Short.valueOf(((short[])array)[sourcei]);
                    }
                    else if (array instanceof byte[]) {
                    	target[desti] = java.lang.Byte.valueOf(((byte[])array)[sourcei]);
                    }
                    else if (array instanceof double[]) {
                    	if ($reifiedElement==Float.$TypeDescriptor$) {
                    		target[desti] = Float.instance(((double[])array)[sourcei]);
                    	}
                    	else {
                    		target[desti] = java.lang.Double.valueOf(((double[])array)[sourcei]);
                    	}
                    }
                    else if (array instanceof float[]) {
                    	target[desti] = java.lang.Float.valueOf(((float[])array)[sourcei]);
                    }
                    else if (array instanceof boolean[]) {
                    	if ($reifiedElement==Boolean.$TypeDescriptor$) {
                    		target[desti] = Boolean.instance(((boolean[])array)[sourcei]);
                    	}
                    	else {
                    		target[desti] = java.lang.Boolean.valueOf(((boolean[])array)[sourcei]);
                    	}
                    }
                    else if (array instanceof char[]) {
                    	target[desti] = java.lang.Character.valueOf(((char[])array)[sourcei]);
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
                        throw new AssertionError("unexpected array types in copyTo()");
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
    public Iterable<? extends Element, ?> 
    takeWhile(Callable<? extends Boolean> take) {
        return $ceylon$language$Iterable$this.takeWhile(take);
    }
    
    @Override @Ignore
    public Iterable<? extends Element, ?> 
    skipWhile(Callable<? extends Boolean> skip) {
        return $ceylon$language$Iterable$this.skipWhile(skip);
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
    
    @Override @Ignore
    public final <Result,Args extends Sequential<? extends java.lang.Object>> Callable<? extends Iterable<? extends Result, ?>>
    spread(@Ignore TypeDescriptor $reifiedResult,@Ignore TypeDescriptor $reifiedArgs, Callable<? extends Callable<? extends Result>> method) {
    	return $ceylon$language$Iterable$this.spread($reifiedResult, $reifiedArgs, method);
    }
    
    public void sortInPlace(
            @Name("comparing")@FunctionalParameter("(x,y)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Comparison,ceylon.language::Tuple<Element,Element,ceylon.language::Tuple<Element,Element,ceylon.language::Empty>>>") 
            final Callable<? extends Comparison> comparing) {
        java.util.List<Element> list = 
        		new java.util.AbstractList<Element>() {
            @Override
            public Element get(int index) {
                return Array.this.unsafeItem(index);
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
    
//    @TypeInfo("ceylon.language::Array<Element&Object>")
//    public Array<Element> coalescedArray() {
//    	int resultSize = 0;
//    	long size = getSize();
//		for (int i=0; i<size; i++) {
//    		if (unsafeItem(i)!=null) {
//    			resultSize++;
//    		}
//    	}
//        java.lang.Class<?> arrayElementClass = $reifiedElement.getArrayElementClass();
//        if (arrayElementClass==int.class) {
//        	
//        }
//    }
    
}
