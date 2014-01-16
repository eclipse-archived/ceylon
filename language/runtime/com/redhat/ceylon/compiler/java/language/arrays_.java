package com.redhat.ceylon.compiler.java.language;

import ceylon.language.Array;
import ceylon.language.AssertionException;
import ceylon.language.String;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * Utilities singleton for array type conversions operations
 * that transform Ceylon {@link Array}s to a native Java
 * arrays.
 * 
 * This class pretends to be part of <tt>java.lang</tt>.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
@Ceylon(major = 6)
@Object
public final class arrays_ {
        
    @Ignore
    private arrays_() {}
    @Ignore
    private static final arrays_ value = new arrays_();

    @Ignore
    public static arrays_ get_() {
        return value;
    }
    
    /**
     * Cast a Ceylon <code>Array&lt;Integer&gt;</code> to a Java 
     * <code>LongArray</code>, that is, to <code>long[]</code>, 
     * preserving the identity of the given array.
     * 
     * @see LongArray
     */
    public long[] asNativeLongArray(@Name("array") Array<ceylon.language.Integer> array){
        // simplest is same underlying type
        if(array.toArray() instanceof long[]){
            long[] src = (long[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;Float&gt;</code> to a Java 
     * <code>DoubleArray</code>, that is, to <code>double[]</code>, 
     * preserving the identity of the given array.
     * 
     * @see DoubleArray
     */
    public double[] asNativeDoubleArray(@Name("array") Array<ceylon.language.Float> array){
        // simplest is same underlying type
        if(array.toArray() instanceof double[]){
            double[] src = (double[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;Boolean&gt;</code> to a Java 
     * <code>BooleanArray</code>, that is, to <code>boolean[]</code>, 
     * preserving the identity of the given array.
     * 
     * @see BooleanArray
     */
    public boolean[] asNativeBooleanArray(@Name("array") Array<ceylon.language.Boolean> array){
        // simplest is same underlying type
        if(array.toArray() instanceof boolean[]){
            boolean[] src = (boolean[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;String&gt;</code> to a Java 
     * <code>ObjectArray&lt;java.lang.String&gt;</code>, that is, 
     * to <code>java.lang.String[]</code>, preserving the identity 
     * of the given array.
     * 
     * @see ObjectArray
     */
    public java.lang.String[] asNativeStringArray(@Name("array") 
    @TypeInfo("ceylon.language::Array<ceylon.language::String>") 
    Array<ceylon.language.String> array){
        // simplest is same underlying type
        if(array.toArray() instanceof java.lang.String[]){
        	java.lang.String[] src = (java.lang.String[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;Character&gt;</code> to a Java 
     * <code>IntArray</code>, that is, to <code>int[]</code>, 
     * preserving the identity of the given array.
     * 
     * @see IntArray
     */
    public int[] asCodePointArray(@Name("array") Array<ceylon.language.Character> array){
        // simplest is same underlying type
        if(array.toArray() instanceof int[]){
            int[] src = (int[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }


    /**
     * Cast a Ceylon <code>Array&lt;java.lang.Byte&gt;</code> to a Java 
     * <code>ByteArray</code>, that is, to <code>byte[]</code>, preserving 
     * the identity of the given array.
     * 
     * @see ByteArray
     */
    public byte[] asByteArray(@Name("array") Array<java.lang.Byte> array){
        // simplest is same underlying type
        if(array.toArray() instanceof byte[]){
            byte[] src = (byte[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;java.lang.Short&gt;</code> to a Java 
     * <code>ShortArray</code>, that is, to <code>short[]</code>, preserving 
     * the identity of the given array.
     * 
     * @see ShortArray
     */
    public short[] asShortArray(@Name("array") Array<java.lang.Short> array){
        // simplest is same underlying type
        if(array.toArray() instanceof short[]){
            short[] src = (short[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;java.lang.Integer&gt;</code> to a Java 
     * <code>IntArray</code>, that is, to <code>int[]</code>, preserving 
     * the identity of the given array.
     * 
     * @see IntArray
     */
    public int[] asIntArray(@Name("array") Array<java.lang.Integer> array){
        // simplest is same underlying type
        if(array.toArray() instanceof int[]){
            int[] src = (int[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;java.lang.Long&gt;</code> to a Java 
     * <code>LongArray</code>, that is, to <code>long[]</code>, preserving 
     * the identity of the given array.
     * 
     * @see LongArray
     */
    public long[] asLongArray(@Name("array") Array<java.lang.Long> array){
        // simplest is same underlying type
        if(array.toArray() instanceof long[]){
            long[] src = (long[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;java.lang.Float&gt;</code> to a Java 
     * <code>FloatArray</code>, that is, to <code>float[]</code>, preserving 
     * the identity of the given array. 
     * 
     * @see FloatArray
     */
    public float[] asFloatArray(@Name("array") Array<java.lang.Float> array){
        // simplest is same underlying type
        if(array.toArray() instanceof float[]){
            float[] src = (float[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;java.lang.Double&gt;</code> to a Java 
     * <code>DoubleArray</code>, that is, to <code>double[]</code>, preserving 
     * the identity of the given array. 
     * 
     * @see DoubleArray
     */
    public double[] asDoubleArray(@Name("array") Array<java.lang.Double> array){
        // simplest is same underlying type
        if(array.toArray() instanceof double[]){
            double[] src = (double[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;java.lang.Character&gt;</code> to a Java 
     * <code>CharArray</code>, that is, to <code>char[]</code>, preserving 
     * the identity of the given array. 
     * 
     * @see CharArray
     */
    public char[] asCharArray(@Name("array") Array<java.lang.Character> array){
        // simplest is same underlying type
        if(array.toArray() instanceof char[]){
            char[] src = (char[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;java.lang.Boolean&gt;</code> to a Java 
     * <code>BooleanArray</code>, that is, to <code>boolean[]</code>, preserving 
     * the identity of the given array. 
     * 
     * @see BooleanArray
     */
    public boolean[] asBooleanArray(@Name("array") Array<java.lang.Boolean> array){
        // simplest is same underlying type
        if(array.toArray() instanceof boolean[]){
            boolean[] src = (boolean[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /**
     * Cast a Ceylon <code>Array&lt;T&gt;</code> to a Java <code>ObjectArray&lt;T&gt;</code>,
     * that is, to <code>T[]</code>, preserving the identity of the given array. 
     * 
     * @see ObjectArray
     */
    public <T> T[] asObjectArray(@Ignore TypeDescriptor $reifiedT, 
            @TypeInfo("ceylon.language::Array<T|ceylon.language::Null>") 
            @Name("array") Array<T> array){
        // simplest is same underlying type
        if(array.toArray() instanceof java.lang.Object[]){
            @SuppressWarnings("unchecked")
            T[] src = (T[]) array.toArray();
            return src;
        }
        throw new AssertionException("Invalid source array type: "+array.toArray());
    }

    /*public byte[] toByteArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
                              @Name("values")
                              Iterable<? extends Integer,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends Integer> array = (Array<? extends Integer>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof byte[]){
                byte[] src = (byte[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            // implicit type conversions
            if(array.toArray() instanceof short[]){
                short[] src = (short[]) array.toArray();
                byte[] dst = new byte[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (byte) src[i];
                return dst;
            }
            if(array.toArray() instanceof int[]){
                int[] src = (int[]) array.toArray();
                byte[] dst = new byte[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (byte) src[i];
                return dst;
            }
            if(array.toArray() instanceof long[]){
                long[] src = (long[]) array.toArray();
                byte[] dst = new byte[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (byte) src[i];
                return dst;
            }
            // must be a real c.l.Integer array
            if(array.toArray() instanceof ceylon.language.Integer[]){
                ceylon.language.Integer[] src = (ceylon.language.Integer[]) array.toArray();
                byte[] dst = new byte[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (byte) src[i].longValue();
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toByteArray(values);
    }*/

    /*public short[] toShortArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
                                @Name("values")
                                Iterable<? extends Integer,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends Integer> array = (Array<? extends Integer>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof short[]){
                short[] src = (short[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            // implicit type conversions
            if(array.toArray() instanceof byte[]){
                byte[] src = (byte[]) array.toArray();
                short[] dst = new short[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (short) src[i];
                return dst;
            }
            if(array.toArray() instanceof int[]){
                int[] src = (int[]) array.toArray();
                short[] dst = new short[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (short) src[i];
                return dst;
            }
            if(array.toArray() instanceof long[]){
                long[] src = (long[]) array.toArray();
                short[] dst = new short[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (short) src[i];
                return dst;
            }
            // must be a real c.l.Integer array
            if(array.toArray() instanceof ceylon.language.Integer[]){
                ceylon.language.Integer[] src = (ceylon.language.Integer[]) array.toArray();
                short[] dst = new short[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (short) src[i].longValue();
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toShortArray(values);
    }*/

    /*public int[] toIntArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
                            @Name("values")
                            Iterable<? extends Integer,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends Integer> array = (Array<? extends Integer>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof int[]){
                int[] src = (int[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            // implicit type conversions
            if(array.toArray() instanceof byte[]){
                byte[] src = (byte[]) array.toArray();
                int[] dst = new int[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (int) src[i];
                return dst;
            }
            if(array.toArray() instanceof short[]){
                short[] src = (short[]) array.toArray();
                int[] dst = new int[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (int) src[i];
                return dst;
            }
            if(array.toArray() instanceof long[]){
                long[] src = (long[]) array.toArray();
                int[] dst = new int[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (int) src[i];
                return dst;
            }
            // must be a real c.l.Integer array
            if(array.toArray() instanceof ceylon.language.Integer[]){
                ceylon.language.Integer[] src = (ceylon.language.Integer[]) array.toArray();
                int[] dst = new int[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (int) src[i].longValue();
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toIntArray(values);
    }*/

    /*public long[] toLongArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
                              @Name("values")
                              Iterable<? extends Integer,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends Integer> array = (Array<? extends Integer>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof long[]){
                long[] src = (long[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            // implicit type conversions
            if(array.toArray() instanceof byte[]){
                byte[] src = (byte[]) array.toArray();
                long[] dst = new long[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (long) src[i];
                return dst;
            }
            if(array.toArray() instanceof short[]){
                short[] src = (short[]) array.toArray();
                long[] dst = new long[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (long) src[i];
                return dst;
            }
            if(array.toArray() instanceof int[]){
                int[] src = (int[]) array.toArray();
                long[] dst = new long[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (long) src[i];
                return dst;
            }
            // must be a real c.l.Integer array
            if(array.toArray() instanceof ceylon.language.Integer[]){
                ceylon.language.Integer[] src = (ceylon.language.Integer[]) array.toArray();
                long[] dst = new long[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (long) src[i].longValue();
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toLongArray(values);
    }*/

    /*public float[] toFloatArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::Float,ceylon.language::Null>")
                                @Name("values")
                                Iterable<? extends Float,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends Float> array = (Array<? extends Float>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof float[]){
                float[] src = (float[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            // implicit type conversions
            if(array.toArray() instanceof double[]){
                double[] src = (double[]) array.toArray();
                float[] dst = new float[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (float) src[i];
                return dst;
            }
            // must be a real c.l.Float array
            if(array.toArray() instanceof ceylon.language.Float[]){
                ceylon.language.Float[] src = (ceylon.language.Float[]) array.toArray();
                float[] dst = new float[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (float) src[i].doubleValue();
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toFloatArray(values);
    }*/
    
    /*public double[] toDoubleArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::Float,ceylon.language::Null>")
                                  @Name("values")
                                  Iterable<? extends Float,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends Float> array = (Array<? extends Float>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof double[]){
                double[] src = (double[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            // implicit type conversions
            if(array.toArray() instanceof float[]){
                float[] src = (float[]) array.toArray();
                double[] dst = new double[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (double) src[i];
                return dst;
            }
            // must be a real c.l.Float array
            if(array.toArray() instanceof ceylon.language.Float[]){
                ceylon.language.Float[] src = (ceylon.language.Float[]) array.toArray();
                double[] dst = new double[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (double) src[i].doubleValue();
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toDoubleArray(values);
    }*/

    /*public char[] toCharArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::Character,ceylon.language::Null>")
                              @Name("values")
                              Iterable<? extends Character,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends Character> array = (Array<? extends Character>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof char[]){
                char[] src = (char[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            // implicit type conversions
            if(array.toArray() instanceof int[]){
                int[] src = (int[]) array.toArray();
                char[] dst = new char[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (char) src[i];
                return dst;
            }
            // must be a real c.l.Character array
            if(array.toArray() instanceof ceylon.language.Character[]){
                ceylon.language.Character[] src = (ceylon.language.Character[]) array.toArray();
                char[] dst = new char[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (char) src[i].intValue();
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toCharArray(values);
    }*/

    /*public int[] toCodePointArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::Character,ceylon.language::Null>")
                                  @Name("values")
                                  Iterable<? extends Character,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends Character> array = (Array<? extends Character>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof int[]){
                int[] src = (int[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            // implicit type conversions
            if(array.toArray() instanceof char[]){
                char[] src = (char[]) array.toArray();
                int[] dst = new int[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (int) src[i];
                return dst;
            }
            // must be a real c.l.Character array
            if(array.toArray() instanceof ceylon.language.Character[]){
                ceylon.language.Character[] src = (ceylon.language.Character[]) array.toArray();
                int[] dst = new int[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = (int) src[i].intValue();
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toCodepointArray(values);
    }

    public boolean[] toBooleanArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::Boolean,ceylon.language::Null>")
                                    @Name("values")
                                    Iterable<? extends Boolean,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends Boolean> array = (Array<? extends Boolean>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof boolean[]){
                boolean[] src = (boolean[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            // must be a real c.l.Boolean array
            if(array.toArray() instanceof ceylon.language.Boolean[]){
                ceylon.language.Boolean[] src = (ceylon.language.Boolean[]) array.toArray();
                boolean[] dst = new boolean[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = src[i].booleanValue();
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toBooleanArray(values);
    }*/

    /*public <T> T[] toObjectArray(@Ignore TypeDescriptor $reifiedT, 
                                 @TypeInfo("ceylon.language::Iterable<T|ceylon.language::Null,ceylon.language::Null>")
                                 @Name("values")
                                 Iterable<? extends T,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends T> array = (Array<? extends T>) values;
            // simplest is same underlying type
            if(array.toArray() instanceof java.lang.Object[]){
                @SuppressWarnings("unchecked")
                T[] src = (T[]) array.toArray();
                return Arrays.copyOf(src, src.length);
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.<T>toArray(values, Util.<T>getJavaClassForDescriptor($reifiedT));
    }*/

    /*public java.lang.String[] toJavaStringArray(@TypeInfo("ceylon.language::Iterable<ceylon.language::String|ceylon.language::Null,ceylon.language::Null>")
                                                @Name("values")
                                                Iterable<? extends String,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends String> array = (Array<? extends String>) values;
            // must be a real c.l.String array
            if(array.toArray() instanceof ceylon.language.String[]){
                ceylon.language.String[] src = (ceylon.language.String[]) array.toArray();
                java.lang.String[] dst = new java.lang.String[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = src[i] != null ? src[i].toString() : null;
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        return Util.toJavaStringArray(values);
    }*/

    /*public String[] toStringArray(@TypeInfo("ceylon.language::Iterable<java.lang::String|ceylon.language::Null,ceylon.language::Null>")
                                  @Name("values")
                                  Iterable<? extends java.lang.String,? extends java.lang.Object> values){
        if(values instanceof Array){
            @SuppressWarnings("unchecked")
            Array<? extends java.lang.String> array = (Array<? extends java.lang.String>) values;
            // must be a real java.lang.String array
            if(array.toArray() instanceof java.lang.String[]){
                java.lang.String[] src = (java.lang.String[]) array.toArray();
                String[] dst = new String[src.length];
                for(int i=0;i<src.length;i++)
                    dst[i] = String.instance(src[i]);
                return dst;
            }
            throw new AssertionException("Invalid source array type: "+array.toArray());
        }
        String[] dst = new String[(int) values.getSize()];
        Iterator<? extends java.lang.String> iterator = values.iterator();
        for(int i=0;i<dst.length;i++){
            java.lang.Object o = iterator.next();
            if(o == finished_.get_())
                throw new AssertionException("Depleted values iterable before its size was reached. Size = " + dst.length + ", index = "+i);
            dst[i] = String.instance((java.lang.String)o);
        }
        return dst;
    }*/

    public String[] javaStringArrayToCeylonStringArray(@Name("array") java.lang.String[] array){
        String[] dst = new String[array.length];
        for(int i=0;i<array.length;i++)
            dst[i] = String.instance(array[i]);
        return dst;
    }

    public java.lang.String[] ceylonStringArrayToJavaStringArray(@Name("array") String[] array){
        java.lang.String[] dst = new java.lang.String[array.length];
        for(int i=0;i<array.length;i++)
            dst[i] = array[i] != null ? array[i].toString() : null;
        return dst;
    }
}
