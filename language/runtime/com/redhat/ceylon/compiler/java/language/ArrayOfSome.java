package com.redhat.ceylon.compiler.java.language;

import java.util.Arrays;

import ceylon.language.Array;
import ceylon.language.Iterable$impl;
import ceylon.language.List;
import ceylon.language.List$impl;
import ceylon.language.Sequence;
import ceylon.language.Some;
import ceylon.language.arrayOfNone_;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ignore
@Ceylon(major = 3)
public class ArrayOfSome<Element> extends Array<Element> implements Some<Element> {

    public ArrayOfSome(java.lang.Object array) {
        super(array);
    }
    
    @Override
    public List<? extends Element> getRest() {
        if (getSize() == 1) {
            return arrayOfNone_.<Element>arrayOfNone();
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
    
    @Override
    @Annotations({ @Annotation("actual") })
    public Array<? extends Element> getReversed() {
        if (getSize() == 1) {
            return this;
        } else if (array instanceof char[]) {
            char[] __a = (char[])array;
            char[] rev = new char[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new ArrayOfSome<Element>(rev);
        } else if (array instanceof byte[]) {
            byte[] __a = (byte[])array;
            byte[] rev = new byte[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new ArrayOfSome<Element>(rev);
        } else if (array instanceof short[]) {
            short[] __a = (short[])array;
            short[] rev = new short[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new ArrayOfSome<Element>(rev);
        } else if (array instanceof int[]) {
            int[] __a = (int[])array;
            int[] rev = new int[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new ArrayOfSome<Element>(rev);
        } else if (array instanceof long[]) {
            long[] __a = (long[])array;
            long[] rev = new long[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new ArrayOfSome<Element>(rev);
        } else if (array instanceof float[]) {
            float[] __a = (float[])array;
            float[] rev = new float[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new ArrayOfSome<Element>(rev);
        } else if (array instanceof double[]) {
            double[] __a = (double[])array;
            double[] rev = new double[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new ArrayOfSome<Element>(rev);
        } else if (array instanceof boolean[]) {
            boolean[] __a = (boolean[])array;
            boolean[] rev = new boolean[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new ArrayOfSome<Element>(rev);
        } else if (array instanceof java.lang.String[]) {
            java.lang.String[] __a = (java.lang.String[])array;
            java.lang.String[] rev = new java.lang.String[__a.length];
            for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
                rev[i] = __a[j];
            }
            return new ArrayOfSome<Element>(rev);
        }

        Element[] __a = (Element[])array;
        java.lang.Object[] rev = new java.lang.Object[__a.length];
        for (int i = 0, j=__a.length-1; i < __a.length; i++, j--) {
            rev[i] = __a[j];
        }
        return new ArrayOfSome<Element>((Element[])rev);
    }

    /*@Override
    public FixedSized<? extends Element> getRest() {
        return span(Integer.instance(1), null);
    }*/
    
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
    @Ignore 
    public List<? extends Element> getSequence() { 
        return Iterable$impl._getSequence(this); 
    }

    @Override @SuppressWarnings("rawtypes")
    @Annotations({ @Annotation("actual") })
    public <Other> Sequence withLeading(Other e) {
        return List$impl._withLeading(this, e);
    }
    @Override @SuppressWarnings("rawtypes")
    @Annotations({ @Annotation("actual") })
    public <Other> Sequence withTrailing(Other e) {
        return List$impl._withTrailing(this, e);
    }
}
