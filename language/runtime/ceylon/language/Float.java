package ceylon.language;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@Class(extendsType="ceylon.language::Object")
@SatisfiedTypes({
    "ceylon.language::Scalar<ceylon.language::Float>",
    "ceylon.language::Exponentiable<ceylon.language::Float,ceylon.language::Float>"
})
@ValueType
public final class Float
    implements Scalar<Float>, Exponentiable<Float,Float>, ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(Float.class);

    @Ignore
    final double value;
    
    public Float(@Name("float") double f) {
        value = f;
    }
    
    @Ignore
    @Override
    public Scalar$impl<Float> $ceylon$language$Scalar$impl(){
        // drags Numeric<Float> Comparable<Float> Number
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Numeric$impl<Float> $ceylon$language$Numeric$impl(){
        // drags Summable<Float> Invertable<Float>
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Summable$impl<Float> $ceylon$language$Summable$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Invertable$impl<Float> $ceylon$language$Invertable$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Comparable$impl<Float> $ceylon$language$Comparable$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Number$impl $ceylon$language$Number$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Exponentiable$impl<Float,Float> $ceylon$language$Exponentiable$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    public static Float instance(double d) {
        return new Float(d);
    }
    
    public double doubleValue() {
        return value;
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
        return instance(Math.pow(value, other.value));
    }
    
    @Ignore
    public static double power(double value, double otherValue) {
        return Math.pow(value, otherValue);
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
        return instance(Math.pow(value, other.value));
    }
    
    @Ignore
    public static double power(double value, long otherValue) {
        return Math.pow(value, otherValue);
    }
    
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
        return instance(value > 0.0D ? value - (long)value : (long)value - value);
    }
    
    @Ignore
    public static double getFractionalPart(double value) {
        return value > 0.0D ? value - (long)value : (long)value - value;
    }
    
    @Override
    public Float getWholePart() {		
        return instance((long)value);
    }
    
    @Ignore
    public static double getWholePart(double value) {
        return (long)value;
    }
    
    @Override
    public boolean getPositive() {
        return value > 0;
    }
    
    @Ignore
    public static boolean getPositive(double value) {
        return value > 0;
    }
    
    @Override
    public boolean getNegative() {
        return value < 0;
    }
    
    @Ignore
    public static boolean getNegative(double value) {
        return value < 0;
    }
    
    public boolean getStrictlyPositive() {
        return (Double.doubleToRawLongBits(value) >> 63)==0
        		& !Double.isNaN(value);
    }
    
    @Ignore
    public static boolean getStrictlyPositive(double value) {
        return (Double.doubleToRawLongBits(value) >> 63)==0
                & !Double.isNaN(value);
    }
    
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
    public Float getPositiveValue() {
        return this;
    }
    
    @Ignore
    public static double getPositiveValue(double value) {
        return value;
    }
    
    @Override
    public Float getNegativeValue() {
        return instance(-value);
    }
    
    @Ignore
    public static double getNegativeValue(double value) {
        return -value;
    }
    
    @Override
    public Comparison compare(@Name("other") Float other) {
        double x = value;
        double y = other.value;
        return (x < y) ? smaller_.get_() :
            ((x == y) ? equal_.get_() : larger_.get_());
    }
    
    @Ignore
    public static Comparison compare(double value, double otherValue) {
        double x = value;
        double y = otherValue;
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
    
    @TypeInfo("ceylon.language::Integer")
    @Override
    public long getInteger() {
        if (value >= Long.MIN_VALUE
                && value <= Long.MAX_VALUE) {
            return (long)value;
        } 
        throw new OverflowException();
    }
    
    @Ignore
    public static long getInteger(double value) {
        if (value >= Long.MIN_VALUE
                && value <= Long.MAX_VALUE) {
            return (long)value;
        } 
        throw new OverflowException();
    }
    
    @TypeInfo("ceylon.language::Float")
    @Override
    public double getFloat() {
        return value;
    }
    
    @Ignore
    public static double getFloat(double value) {
        return value;
    }
    
    public boolean getUndefined() {
        return Double.isNaN(this.value);
    }
    
    @Ignore
    public static boolean getUndefined(double value) {
        return Double.isNaN(value);
    }
    
    public boolean getFinite() {
        return !Double.isInfinite(this.value) && !getUndefined();
    }
    
    @Ignore
    public static boolean getFinite(double value) {
        return !Double.isInfinite(value) && !getUndefined(value);
    }
    
    public boolean getInfinite() {
        return Double.isInfinite(value);
    }
    
    @Ignore
    public static boolean getInfinite(double value) {
        return Double.isInfinite(value);
    }
    
    /*@Override
    public boolean largerThan(@Name("other") Float other) {
        return value > other.value;
    }
    
    @Override
    public boolean smallerThan(@Name("other") Float other) {
        return value < other.value;
    }
    
    @Override
    public boolean asLargeAs(@Name("other") Float other) {
        return value >= other.value;
    }
    
    @Override
    public boolean asSmallAs(@Name("other") Float other) {
        return value <= other.value;
    }*/
    
    @Override
    public boolean equals(@Name("that") java.lang.Object that) {
        return equals(value, that);
    }
    
    @Ignore
    public static boolean equals(double value, java.lang.Object that) {
        if (that instanceof Integer) {
            return value == ((Integer)that).value;
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
        long bits = Double.doubleToLongBits(value);
        if (value == -0.0) {// make 0.0 and -0.0 have the same hash
            bits &= 0x7fffffffffffffffL; 
        }
        return (int)(bits ^ (bits >>> 32));
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
