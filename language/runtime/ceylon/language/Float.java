/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;
import org.eclipse.ceylon.compiler.java.metadata.Defaulted;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.SatisfiedTypes;
import org.eclipse.ceylon.compiler.java.metadata.Transient;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.ValueType;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Class(extendsType="ceylon.language::Object", basic = false, identifiable = false, constructors = true)
@SatisfiedTypes({
    "ceylon.language::Number<ceylon.language::Float>",
    "ceylon.language::Exponentiable<ceylon.language::Float,ceylon.language::Float>"
})
@ValueType
@SharedAnnotation$annotation$
@FinalAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public final class Float
    implements Number<Float>, Exponentiable<Float,Float>, 
               ReifiedType, 
               java.io.Serializable, 
               java.lang.Comparable<Float> {

    private static final long serialVersionUID = 8699090544995758140L;

    private static final double TWO_FIFTY_TWO = (double) (1L << 52);

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = 
            TypeDescriptor.klass(Float.class);

    public static double sum(
            @TypeInfo("{ceylon.language::Float*}")
            @Name("floats")
            Iterable<? extends Float, ? extends java.lang.Object> floats) {
        
        if (floats instanceof Array) {
            java.lang.Object array = 
                    ((Array<?>) floats).toArray();
            if (array instanceof float[]) {
                float sum = 0.0f;
                float[] floatArray = (float[]) array;
                for (int i=0, s=floatArray.length; i<s; i++) {
                    sum += floatArray[i];
                }
                return sum;
            }
            if (array instanceof double[]) {
                double sum = 0.0d;
                double[] doubleArray = (double[]) array;
                for (int i=0, s=doubleArray.length; i<s; i++) {
                    sum += doubleArray[i];
                }
                return sum;
            }
        }
        
        if (floats instanceof List) {
            @SuppressWarnings("unchecked")
            List<? extends Float> list = 
                    (List<? extends Float>) floats;
            long size = list.getSize();
            double sum = 0.0d;
            for (int i=0; i<size; i++) {
                sum += list.getFromFirst(i).value;
            }
            return sum;
        }
        
        double sum = 0.0d;
        Iterator<? extends Float> it = floats.iterator();
        java.lang.Object o;
        while ((o=it.next())!=finished_.get_()) {
            sum += ((Float) o).value;
        }
        return sum;
    }
    
    public static double product(
            @TypeInfo("{ceylon.language::Float*}")
            @Name("floats")
            Iterable<? extends Float, ? extends java.lang.Object> floats) {
        
        if (floats instanceof Array) {
            java.lang.Object array = 
                    ((Array<?>) floats).toArray();
            if (array instanceof float[]) {
                float product = 1.0f;
                float[] floatArray = (float[]) array;
                for (int i=0, s=floatArray.length; i<s; i++) {
                    product *= floatArray[i];
                }
                return product;
            }
            if (array instanceof double[]) {
                double product = 1.0d;
                double[] doubleArray = (double[]) array;
                for (int i=0, s=doubleArray.length; i<s; i++) {
                    product *= doubleArray[i];
                }
                return product;
            }
        }
        
        if (floats instanceof List) {
            @SuppressWarnings("unchecked")
            List<? extends Float> list = 
                    (List<? extends Float>) floats;
            long size = list.getSize();
            double product = 1.0d;
            for (int i=0; i<size; i++) {
                product *= list.getFromFirst(i).value;
            }
            return product;
        }
        
        double product = 1.0d;
        Iterator<? extends Float> it = floats.iterator();
        java.lang.Object o;
        while ((o=it.next())!=finished_.get_()) {
            product *= ((Float) o).value;
        }
        return product;
    }
    
    @TypeParameters(@TypeParameter(value="Absent", satisfies="ceylon.language::Null"))
    @TypeInfo("ceylon.language::Float|Absent")
    public static <Absent> java.lang.Object min(
            @Ignore TypeDescriptor $reifiedAbsent,
            @TypeInfo("ceylon.language::Iterable<ceylon.language::Float,Absent>")
            @Name("floats")
            Iterable<? extends Float, ? extends java.lang.Object> floats) {
        
        if (floats instanceof Array) {
            java.lang.Object array = 
                    ((Array<?>) floats).toArray();
            if (array instanceof float[]) {
                float[] floatArray = (float[]) array;
                int s = floatArray.length;
                if (s==0) {
                    return null;
                }
                float min = floatArray[0];
                for (int i=1; i<s; i++) {
                    min = Math.min(min, floatArray[i]);
                }
                return instance(min);
            }
            if (array instanceof double[]) {
                double[] doubleArray = (double[]) array;
                int s = doubleArray.length;
                if (s==0) {
                    return null;
                }
                double min = doubleArray[0];
                for (int i=1; i<s; i++) {
                    min = Math.min(min, doubleArray[i]);
                }
                return instance(min);
            }
        }
        
        if (floats instanceof List) {
            @SuppressWarnings("unchecked")
            List<? extends Float> list = 
                    (List<? extends Float>) 
                        floats;
            long size = list.getSize();
            if (size==0) {
                return null;
            }
            double min = floats.getFromFirst(0).value;
            for (int i=1; i<size; i++) {
                min = Math.min(min,
                        list.getFromFirst(i).value);
            }
            return instance(min);
        }
        
        double min = Double.NaN;
        boolean first = true;
        Iterator<? extends Float> it = 
                floats.iterator();
        java.lang.Object o;
        while ((o=it.next())!=finished_.get_()) {
            double x = ((Float) o).value;
            if (first) {
                min = x;
                first = false;
            }
            else {
                min = Math.max(min, x);
            }
        }
        return first ? null : instance(min);
    }
    
    @TypeParameters(@TypeParameter(value="Absent", satisfies="ceylon.language::Null"))
    @TypeInfo("ceylon.language::Float|Absent")
    public static <Absent> java.lang.Object max(
            @Ignore TypeDescriptor $reifiedAbsent,
            @TypeInfo("ceylon.language::Iterable<ceylon.language::Float,Absent>")
            @Name("floats")
            Iterable<? extends Float, ? extends java.lang.Object> floats) {
        
        if (floats instanceof Array) {
            java.lang.Object array = 
                    ((Array<?>) floats).toArray();
            if (array instanceof float[]) {
                float[] floatArray = (float[]) array;
                int s = floatArray.length;
                if (s==0) {
                    return null;
                }
                float max = floatArray[0];
                for (int i=1; i<s; i++) {
                    max = Math.max(max, floatArray[i]);
                }
                return instance(max);
            }
            if (array instanceof double[]) {
                double[] doubleArray = (double[]) array;
                int s = doubleArray.length;
                if (s==0) {
                    return null;
                }
                double max = doubleArray[0];
                for (int i=1; i<s; i++) {
                    max = Math.max(max, doubleArray[i]);
                }
                return instance(max);
            }
        }
        
        if (floats instanceof List) {
            @SuppressWarnings("unchecked")
            List<? extends Float> list = 
                    (List<? extends Float>) 
                        floats;
            long size = list.getSize();
            if (size==0) {
                return null;
            }
            double max = floats.getFromFirst(0).value;
            for (int i=1; i<size; i++) {
                max = Math.max(max,
                        list.getFromFirst(i).value);
            }
            return instance(max);
        }
        
        double max = Double.NaN;
        boolean first = true;
        Iterator<? extends Float> it = 
                floats.iterator();
        java.lang.Object o;
        while ((o=it.next())!=finished_.get_()) {
            double x = ((Float) o).value;
            if (first) {
                max = x;
                first = false;
            }
            else {
                max = Math.max(max, x);
            }
        }
        return first ? null : instance(max);
    }
    
    public static double smallest(@Name("x") double x, @Name("y") double y) {
        return Math.min(x, y);
    }
    
    public static double largest(@Name("x") double x, @Name("y") double y) {
        return Math.max(x, y);
    }
    
    @Ignore
    final double value;
    
    @SharedAnnotation$annotation$
    public Float(@Name("float") double f) {
        value = f;
    }
    
    @Ignore
    @Override
    public Number$impl<Float> $ceylon$language$Number$impl(){
        // drags Numeric<Float> Comparable<Float>
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Invertible$impl<Float> $ceylon$language$Invertible$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Comparable$impl<Float> $ceylon$language$Comparable$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }
    
    @Ignore
    public static Float instance(double d) {
        return new Float(d);
    }
    
    @Ignore
    public double doubleValue() {
        return value;
    }
    
    @SharedAnnotation$annotation$
    @StaticAnnotation$annotation$
    @TypeInfo("ceylon.language::Float|ceylon.language::ParseException")
    public static java.lang.Object parse(
            @Name("string") java.lang.String string) {
        return parseFloatInternal_.parseFloatInternal(string);
    }
    
    @Ignore
    public static java.lang.String format(double f) {
        return formatFloat_.formatFloat(f);
    }
    
    @Ignore
    public static final long format$minDecimalPlaces(double $float) {
        return 1L;
    }
    
    @Ignore
    public static final java.lang.String format(double f, final long minDecimalPlaces) {
        return formatFloat_.formatFloat(f, minDecimalPlaces);
    }
    
    @Ignore
    public static final long format$maxDecimalPlaces(double f, final long minDecimalPlaces) {
        return 9L;
    }
    
    @Ignore
    public static final java.lang.String format(double f, final long minDecimalPlaces, final long maxDecimalPlaces) {
        return formatFloat_.formatFloat(f, minDecimalPlaces, maxDecimalPlaces);
    }
    
    @Ignore
    public static final int format$decimalSeparator(double f, final long minDecimalPlaces, final long maxDecimalPlaces) {
        return 46;
    }
    
    @Ignore
    public static java.lang.String format(double f, final long minDecimalPlaces, final long maxDecimalPlaces, final int decimalSeparator) {
        return formatFloat_.formatFloat(f, minDecimalPlaces, maxDecimalPlaces, decimalSeparator);
    }
    
    @Ignore
    public static final ceylon.language.Character format$thousandsSeparator(double f, final long minDecimalPlaces, final long maxDecimalPlaces, final int decimalSeparator) {
        return null;
    }
        
    @SharedAnnotation$annotation$
    @StaticAnnotation$annotation$
    public static java.lang.String format(
            @Name("float") double f, 
            @Name("minDecimalPlaces") @Defaulted
            long minDecimalPlaces, 
            @Name("maxDecimalPlaces") @Defaulted
            long maxDecimalPlaces, 
            @Name("decimalSeparator") @Defaulted
            @TypeInfo("ceylon.language::Character")
            int decimalSeparator, 
            @Name("thousandsSeparator") @Defaulted
            @TypeInfo("ceylon.language::Character?")
            Character thousandsSeparator) {
        return formatFloat_.formatFloat(f, minDecimalPlaces, maxDecimalPlaces, decimalSeparator, thousandsSeparator);
    }
    

    
    @Override
    public Float plus(@Name("other") Float other) {
        return instance(value + other.value);
    }
    
    @Ignore
    public static double plus(double value, double otherValue) {
        return value + otherValue;
    }
    
    @Override
    public Float minus(@Name("other") Float other) {
        return instance(value - other.value);
    }
    
    @Ignore
    public static double minus(double value, double otherValue) {
        return value - otherValue;
    }
    
    @Override
    public Float times(@Name("other") Float other) {
        return instance(value * other.value);
    }
    
    @Ignore
    public static double times(double value, double otherValue) {
        return value * otherValue;
    }
    
    @Override
    public Float divided(@Name("other") Float other) {
        return instance(value / other.value);
    }
    
    @Ignore
    public static double divided(double value, double otherValue) {
        return value / otherValue;
    }
    
    @Override
    public Float power(@Name("other") Float other) {
        return instance(power(value, other.value));
    }
    
    @Ignore
    public static double $power$(double value, double otherValue) {
        return power(value, otherValue);
    }
    
    @Ignore
    public static float $power$(float value, float otherValue) {
        if (otherValue==0.0F && 
                !java.lang.Float.isNaN(value)) {
            return 1.0F;
        }
        else if (otherValue==1.0F) {
            return value;
        }
        else if (otherValue==2.0F) {
            return value*value;
        }
        else if (otherValue==3.0F) {
            return value*value*value;
        }
        else if (otherValue==4.0F) {
            float sqr = value*value;
            return sqr*sqr;
        }
        else if (otherValue==5.0F) {
            float sqr = value*value;
            return sqr*sqr*value;
        }
        else if (otherValue==6.0F) {
            float sqr = value*value;
            return sqr*sqr*sqr;
        }
        //TODO: other positive integer powers for which
        //      multiplying is faster than pow()
        else if (otherValue==0.5F) {
            return (float)Math.sqrt(value);
        }
        else if (otherValue==0.25F) {
            return (float)Math.sqrt(Math.sqrt(value));
        }
        else if (otherValue==-1.0F) {
            return 1.0F/value;
        }
        else if (otherValue==-2.0F) {
            return 1.0F/value/value;
        }
        else if (otherValue==-3.0F) {
            return 1.0F/value/value/value;
        }
        else if (otherValue==-4.0F) {
            float sqr = value*value;
            return 1/sqr/sqr;
        }
        else if (otherValue==-5.0F) {
            float sqr = value*value;
            return 1/sqr/sqr/value;
        }
        else if (otherValue==-6.0F) {
            float sqr = value*value;
            return 1/sqr/sqr/sqr;
        }
        else if (otherValue==-0.5F) {
            return (float)(1.0/Math.sqrt(value));
        }
        else if (otherValue==-0.25F) {
            return (float)(1.0/Math.sqrt(Math.sqrt(value)));
        }
        else if (value==1.0F) {
            return 1.0F;
        }
        else if (value==-1.0F && 
                (otherValue == java.lang.Float.POSITIVE_INFINITY || 
                 otherValue == java.lang.Float.NEGATIVE_INFINITY)) {
            return 1.0F;
        }
        else {
            //NOTE: this function is _really_ slow!
            return (float)Math.pow(value, otherValue);
        }
    }
    
    @Ignore
    public static double power(double value, double otherValue) {
        if (otherValue==0.0 && 
                !Double.isNaN(value)) {
            return 1.0;
        }
        else if (otherValue==1.0) {
            return value;
        }
        else if (otherValue==2.0) {
            return value*value;
        }
        else if (otherValue==3.0) {
            return value*value*value;
        }
        else if (otherValue==4.0) {
            double sqr = value*value;
            return sqr*sqr;
        }
        else if (otherValue==5.0) {
            double sqr = value*value;
            return sqr*sqr*value;
        }
        else if (otherValue==6.0) {
            double sqr = value*value;
            return sqr*sqr*sqr;
        }
        //TODO: other positive integer powers for which
        //      multiplying is faster than pow()
        else if (otherValue==0.5) {
            return Math.sqrt(value);
        }
        else if (otherValue==0.25) {
            return Math.sqrt(Math.sqrt(value));
        }
        else if (otherValue==-1.0) {
            return 1.0/value;
        }
        else if (otherValue==-2.0) {
            return 1.0/value/value;
        }
        else if (otherValue==-3.0) {
            return 1.0/value/value/value;
        }
        else if (otherValue==-4.0) {
            double sqr = value*value;
            return 1/sqr/sqr;
        }
        else if (otherValue==-5.0) {
            double sqr = value*value;
            return 1/sqr/sqr/value;
        }
        else if (otherValue==-6.0) {
            double sqr = value*value;
            return 1/sqr/sqr/sqr;
        }
        else if (otherValue==-0.5) {
            return 1.0/Math.sqrt(value);
        }
        else if (otherValue==-0.25) {
            return 1.0/Math.sqrt(Math.sqrt(value));
        }
        else if (value==1.0) {
            return 1.0;
        }
        else if (value==-1.0 && 
                (otherValue == Double.POSITIVE_INFINITY || 
                 otherValue == Double.NEGATIVE_INFINITY)) {
            return 1.0;
        }
        else {
            //NOTE: this function is _really_ slow!
            return Math.pow(value, otherValue);
        }
    }
    
    @Ignore
    public Float plus(Integer other) {
        return instance(value + other.value);
    }
    
    @Ignore
    public static double plus(double value, long otherValue) {
        return value + otherValue;
    }
    
    @Ignore
    public Float minus(Integer other) {
        return instance(value - other.value);
    }
    
    @Ignore
    public static double minus(double value, long otherValue) {
        return value - otherValue;
    }
    
    @Ignore
    public Float times(Integer other) {
        return instance(value * other.value);
    }
    
    @Ignore
    public static double times(double value, long otherValue) {
        return value * otherValue;
    }
    
    @Ignore
    public Float divided(Integer other) {
        return instance(value / other.value);
    }
    
    @Ignore
    public static double divided(double value, long otherValue) {
        return value / otherValue;
    }
    
    @Ignore
    public Float power(Integer other) {
        return instance(powerOfInteger(value, other.value));
    }
    
    @Ignore
    public static double power(double value, long otherValue) {
        return powerOfInteger(value, otherValue);
    }
    
    @Ignore
    public static double $power$(double value, long otherValue) {
        return powerOfInteger(value, otherValue);
    }

    @AliasesAnnotation$annotation$(aliases = "absolute")
    @Transient
    @Override
    public Float getMagnitude() {
        return instance(Math.abs(value));
    }
    
    @Ignore
    public static double getMagnitude(double value) {
        return Math.abs(value);
    }
    
    @Override
    public Float getFractionalPart() {
        double fractionalPart = getFractionalPart(value);
        if (fractionalPart != 0 && fractionalPart == value) {
            return this;
        }
        return instance(fractionalPart);
    }

    @Ignore
    public static double getFractionalPart(double value) {
        if (value <= -TWO_FIFTY_TWO) {
            return -0d;
        }
        else if (value >= TWO_FIFTY_TWO) {
            return 0d;
        }
        else if (Double.isNaN(value)) {
            return Double.NaN;
        }
        else {
            double result = value - (long) value;
            if (result == 0 && (1/value) < 0) {
                return -0d;
            }
            else {
                return result;
            }
        }
    }

    @Override
    public Float getWholePart() {
        double wholePart = getWholePart(value);
        if (wholePart != 0 && wholePart == value) {
            return this;
        }
        return instance(wholePart);
    }

    @Ignore
    public static double getWholePart(double value) {
        if (value <= -TWO_FIFTY_TWO || 
            value >= TWO_FIFTY_TWO) {
            return value;
        }
        else if (Double.isNaN(value)) {
            return Double.NaN;
        }
        else {
            long result = (long) value;
            if (result == 0 && (1/value) < 0) {
                return -0.0d;
            }
            else {
                return result;
            }
        }
    }

    @Override
    @Transient
    public boolean getPositive() {
        return value > 0;
    }
    
    @Ignore
    public static boolean getPositive(double value) {
        return value > 0;
    }
    
    @Override
    @Transient
    public boolean getNegative() {
        return value < 0;
    }
    
    @Ignore
    public static boolean getNegative(double value) {
        return value < 0;
    }
    
    @Transient
    public boolean getStrictlyPositive() {
        return (Double.doubleToRawLongBits(value) >> 63)==0
                && !Double.isNaN(value);
    }
    
    @Ignore
    public static boolean getStrictlyPositive(double value) {
        return (Double.doubleToRawLongBits(value) >> 63)==0
                && !Double.isNaN(value);
    }
    
    @Transient
    public boolean getStrictlyNegative() {
        return (Double.doubleToRawLongBits(value) >> 63)!=0
                && !Double.isNaN(value);
    }
    
    @Ignore
    public static boolean getStrictlyNegative(double value) {
        return (Double.doubleToRawLongBits(value) >> 63)!=0
                && !Double.isNaN(value);
    }
    
    @Override
    @Transient
    public long getSign() {
        if (value > 0)
            return 1;
        if (value < 0)
            return -1;
        return 0;
    }   
    
    @Ignore
    public static long getSign(double value) {
        if (value > 0)
            return 1;
        if (value < 0)
            return -1;
        return 0;
    }   
    
    @Override
    public Float getNegated() {
        return instance(-value);
    }
    
    @Ignore
    public static double getNegated(double value) {
        return -value;
    }
    
    @Override
    public Comparison compare(@Name("other") Float other) {
        double x = value;
        double y = other.value;
        if (Double.isNaN(x) || Double.isNaN(y)) {
            throw new Exception(new String("NaN is not comparable"));
        }
        return (x < y) ? smaller_.get_() :
            ((x == y) ? equal_.get_() : larger_.get_());
    }
    
    @Override @Ignore
    public int compareTo(Float other) {
        return Double.compare(value, other.value);
    }

    @Ignore
    public static Comparison compare(double value, double otherValue) {
        double x = value;
        double y = otherValue;
        if (Double.isNaN(x) || Double.isNaN(y)) {
            throw new Exception(new String("NaN is not comparable"));
        }
        return (x < y) ? smaller_.get_() :
            ((x == y) ? equal_.get_() : larger_.get_());
    }
    
    @Override
    public java.lang.String toString() {
        return java.lang.Double.toString(value);
    }
    
    @Ignore
    public static java.lang.String toString(double value) {
        return java.lang.Double.toString(value);
    }
    
    // Conversions between numeric types
    
    public long getInteger() {
        return getInteger(value);
    }
    
    @Ignore
    public static long getInteger(double value) {
        if (value >= Long.MIN_VALUE && 
            value <= Long.MAX_VALUE) {
            return (long) value;
        }
        else {
            throw new OverflowException(value + 
                    " cannot be coerced to a 64 bit integer");
        }
    }
    
    @AliasesAnnotation$annotation$(aliases = "notANumber")
    @Transient
    public boolean getUndefined() {
        return Double.isNaN(this.value);
    }
    
    @Ignore
    public static boolean getUndefined(double value) {
        return Double.isNaN(value);
    }
    
    @Transient
    public boolean getFinite() {
        return !Double.isInfinite(this.value) 
                && !getUndefined();
    }
    
    @Ignore
    public static boolean getFinite(double value) {
        return !Double.isInfinite(value) 
                && !getUndefined(value);
    }
    
    @Transient
    public boolean getInfinite() {
        return Double.isInfinite(value);
    }
    
    @Ignore
    public static boolean getInfinite(double value) {
        return Double.isInfinite(value);
    }
    
    @Override
    public boolean equals(@Name("that") java.lang.Object that) {
        return equals(value, that);
    }
    
    @Ignore
    public static boolean equals(double value, java.lang.Object that) {
        if (that instanceof Integer) {
            long intValue = ((Integer) that).value;
            return value == intValue 
                    && intValue > -Integer.TWO_FIFTY_THREE 
                    && intValue < Integer.TWO_FIFTY_THREE;
        } 
        else if (that instanceof Float) {
            return value == ((Float)that).value;
        } 
        else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return hashCode(value);
    }
    
    @Ignore
    public static int hashCode(double value) {
        long wholePart = (long) value;
        if (value == wholePart) {// make integers and floats have consistent hashes
            return Integer.hashCode(wholePart);
        } else {
            final long bits = Double.doubleToLongBits(value);
            return (int)(bits ^ (bits >>> 32));
        }
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

    @Ignore
    public static boolean largerThan(double value, Float other) {
        return value>other.value;
    }

    @Ignore
    public static boolean largerThan(double value, double other) {
        return value>other;
    }
    
    @Override
    public boolean largerThan(@Name("other")Float other) {
        return value>other.value;
    }

    @Ignore
    public static boolean notSmallerThan(double value, Float other) {
        return value>=other.value;
    }

    @Ignore
    public static boolean notSmallerThan(double value, double other) {
        return value>=other;
    }

    @Override
    public boolean notSmallerThan(@Name("other") Float other) {
        return value>=other.value;
    }

    @Ignore
    public static boolean smallerThan(double value, Float other) {
        return value<other.value;
    }

    @Ignore
    public static boolean smallerThan(double value, double other) {
        return value<other;
    }

    @Override
    public boolean smallerThan(@Name("other") Float other) {
        return value<other.value;
    }

    @Ignore
    public static boolean notLargerThan(double value, Float other) {
        return value<=other.value;
    }

    @Ignore
    public static boolean notLargerThan(double value, double other) {
        return value<=other;
    }

    @Override
    public boolean notLargerThan(@Name("other") Float other) {
        return value<=other.value;
    }
    
    @Override
    public Float timesInteger(@Name("integer") long integer) {
        return instance(value*integer);
    }
    
    @Ignore
    public static double timesInteger(double value, long integer) {
        return value*integer;
    }
    
    @Override
    public Float plusInteger(@Name("integer") long integer) {
        return instance(value+integer);
    }
    
    @Ignore
    public static double plusInteger(double value, long integer) {
        return value+integer;
    }
    
    @Override
    public Float powerOfInteger(@Name("integer") long integer) {
        return instance(powerOfInteger(value,integer));
    }
    
    @Ignore
    public static double powerOfInteger(double value, long integer) {
        if (integer == 0 && 
                !Double.isNaN(value)) {
            return 1.0;
        }
        else if (integer == 1) {
            return value;
        }
        else if (integer == 2) {
            return value*value;
        }
        else if (integer == 3) {
            return value*value*value;
        }
        else if (integer == 4) {
            double sqr = value*value;
            return sqr*sqr;
        }
        else if (integer == 5) {
            double sqr = value*value;
            return sqr*sqr*value;
        }
        else if (integer == 6) {
            double sqr = value*value;
            return sqr*sqr*sqr;
        }
        //TODO: other positive integer powers for which
        //      multiplication is more efficient than pow()
        else if (integer == -1) {
            return 1/value;
        }
        else if (integer == -2) {
            return 1/value/value;
        }
        else if (integer == -3) {
            return 1/value/value/value;
        }
        else if (integer == -4) {
            double sqr = value*value;
            return 1/sqr/sqr;
        }
        else if (integer == -5) {
            double sqr = value*value;
            return 1/sqr/sqr/value;
        }
        else if (integer == -6) {
            double sqr = value*value;
            return 1/sqr/sqr/sqr;
        }
        else {
            //NOTE: this function is _really_ slow!
            return Math.pow(value,integer);
        }
    }
    
    @Ignore
    public static Float valueOf(java.lang.String string) {
        return instance(java.lang.Double.parseDouble(string));
    }
    
}

