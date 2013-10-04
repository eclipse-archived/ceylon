package ceylon.language;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@SatisfiedTypes({
    "ceylon.language::Scalar<ceylon.language::Integer>",
    "ceylon.language::Integral<ceylon.language::Integer>",
    "ceylon.language::Binary<ceylon.language::Integer>",
    "ceylon.language::Exponentiable<ceylon.language::Integer,ceylon.language::Integer>"
})
@Class(extendsType="ceylon.language::Object")
@ValueType
public final class Integer
    implements Scalar<Integer>, Integral<Integer>,
               Binary<Integer>,
               Exponentiable<Integer,Integer>, ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(Integer.class);

    @Ignore
    final long value;

    public Integer(@Name("integer") long integer) {
        value = integer;
    }

    @Ignore
    @Override
    public Scalar$impl<Integer> $ceylon$language$Scalar$impl(){
        // drags Numeric<Integer> Comparable<Integer> Number
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Numeric$impl<Integer> $ceylon$language$Numeric$impl(){
        // drags Summable<Integer> Invertable<Integer>
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Summable$impl<Integer> $ceylon$language$Summable$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Invertable$impl<Integer> $ceylon$language$Invertable$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Comparable$impl<Integer> $ceylon$language$Comparable$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Number$impl $ceylon$language$Number$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Integral$impl<Integer> $ceylon$language$Integral$impl(){
        // drags Numeric<Integer> Enumerable<Integer>
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Ordinal$impl<Integer> $ceylon$language$Ordinal$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Enumerable$impl<Integer> $ceylon$language$Enumerable$impl(){
        // drags Ordinal<Integer>
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Binary$impl<Integer> $ceylon$language$Binary$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    @Override
    public Exponentiable$impl<Integer,Integer> $ceylon$language$Exponentiable$impl(){
        throw Util.makeUnimplementedMixinAccessException();
    }

    @Ignore
    public static Integer instance(long l) {
        return new Integer(l);
    }

    @Ignore
    public long longValue() {
        return value;
    }

    @Override
    public Integer plus(@Name("other") Integer other) {
        return instance(value + other.value);
    }

    @Ignore
    public static long plus(long value, long otherValue) {
        return value + otherValue;
    }

    @Override
    public Integer minus(@Name("other") Integer other) {
        return instance(value - other.value);
    }

    @Ignore
    public static long minus(long value, long otherValue) {
        return value - otherValue;
    }

    @Override
    public Integer times(@Name("other") Integer other) {
        return instance(value * other.value);
    }

    @Ignore
    public static long times(long value, long otherValue) {
        return value * otherValue;
    }

    @Override
    public Integer divided(@Name("other") Integer other) {
        return instance(value / other.value);
    }

    @Ignore
    public static long divided(long value, long otherValue) {
        return value / otherValue;
    }

    private static final long POWER_BY_SQUARING_BREAKEVEN = 6;

    private static long powerBySquaring(long base, long power) {
        long result = 1;
        long x = base;
        while (power != 0) {
            if ((power & 1L) == 1L) {
                result *= x;
                power -= 1;
            }
            x *= x;
            power /= 2;
        }
        return result;
    }

    private static long powerByMultiplying(long base, long power) {
        long result = 1;
        while (power > 0) {
            result *= base;
            power--;
        }
        return result;
    }

    @Override
    public Integer power(@Name("other") Integer other) {
        return instance(power(value, other.value));
    }

    @Ignore
    public static long power(long value, long otherValue) {
        long power = otherValue;
        if (value == -1) {
            return power % 2 == 0 ? 1 : -1;
        } else if (value == 1) {
            return 1L;
        }
        if (power < 0) {
            throw new RuntimeException(value + "**" + power + " cannot be represented as an Integer");
        } else if (power == 0L) {
            return 1L;
        }
        if (power >= POWER_BY_SQUARING_BREAKEVEN) {
            return powerBySquaring(value, power);
        } else {
            return powerByMultiplying(value, power);
        }
    }

    @Ignore
    public Float plus(Float other) {
        return Float.instance(value + other.value);
    }

    @Ignore
    public static double plus(long value, double otherValue) {
        return value + otherValue;
    }

    @Ignore
    public Float minus(Float other) {
        return Float.instance(value - other.value);
    }

    @Ignore
    public static double minus(long value, double otherValue) {
        return value - otherValue;
    }

    @Ignore
    public Float times(Float other) {
        return Float.instance(value * other.value);
    }

    @Ignore
    public static double times(long value, double otherValue) {
        return value * otherValue;
    }

    @Ignore
    public Float divided(Float other) {
        return Float.instance(value / other.value);
    }

    @Ignore
    public static double divided(long value, double otherValue) {
        return value / otherValue;
    }

    @Ignore
    public Float power(Float other) {
        return Float.instance(Math.pow(value, other.value)); // FIXME: ugly
    }

    @Ignore
    public static double power(long value, double otherValue) {
        return Math.pow(value, otherValue); // FIXME: ugly
    }

    @Override
    public Integer getMagnitude() {
        return instance(Math.abs(value));
    }

    @Ignore
    public static long getMagnitude(long value) {
        return Math.abs(value);
    }

    @Override
    public Integer getFractionalPart() {
        return instance(0);
    }

    @Ignore
    public static long getFractionalPart(long value) {
        return 0;
    }

    @Override
    public Integer getWholePart() {
        return this;
    }

    @Ignore
    public static long getWholePart(long value) {
        return value;
    }

    @Override
    public boolean getPositive() {
        return value > 0;
    }

    @Ignore
    public static boolean getPositive(long value) {
        return value > 0;
    }

    @Override
    public boolean getNegative() {
        return value < 0;
    }

    @Ignore
    public static boolean getNegative(long value) {
        return value < 0;
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
    public static long getSign(long value) {
        if (value > 0)
            return 1;
        if (value < 0)
            return -1;
        return 0;
    }

    @Override
    public Integer remainder(@Name("other") Integer other) {
        return instance(value % other.value);
    }

    @Ignore
    public static long remainder(long value, long otherValue) {
        return value % otherValue;
    }

    @Override
    public boolean divides(@Name("other") Integer other) {
        return other.value % value == 0;
    }

    @Ignore
    public static boolean divides(long value, long otherValue) {
        return otherValue % value == 0;
    }

    @Override
    public Integer getPositiveValue() {
        return this;
    }

    @Ignore
    public static long getPositiveValue(long value) {
        return value;
    }

    @Override
    public Integer getNegativeValue() {
        return instance(-value);
    }

    @Ignore
    public static long getNegativeValue(long value) {
        return -value;
    }

    public boolean test(Integer op) {
        return value == op.value;
    }

    @Ignore
    public static boolean test(long value, long otherValue) {
        return value == otherValue;
    }

    @Override
    public Comparison compare(@Name("other") Integer other) {
        long x = value;
        long y = other.value;
        return (x < y) ? smaller_.get_() :
            ((x == y) ? equal_.get_() : larger_.get_());
    }

    @Ignore
    public static Comparison compare(long value, long otherValue) {
        long x = value;
        long y = otherValue;
        return (x < y) ? smaller_.get_() :
            ((x == y) ? equal_.get_() : larger_.get_());
    }

    @Override
    public java.lang.String toString() {
        return java.lang.Long.toString(value);
    }

    @Ignore
    public static java.lang.String toString(long value) {
        return java.lang.Long.toString(value);
    }

    // Conversions between numeric types

    @TypeInfo("ceylon.language::Integer")
    @Override
    public long getInteger() {
        return value;
    }

    @TypeInfo("ceylon.language::Integer")
    @Override
    public long getIntegerValue() {
        return value;
    }

    @Ignore
    public static long getInteger(long value) {
        return value;
    }

    @Ignore
    public static long getIntegerValue(long value) {
        return value;
    }

    @TypeInfo("ceylon.language::Float")
    @Override
    public double getFloat() {
        if (value >= 9007199254740992L
                || value <= -9007199254740992L) {
            throw new OverflowException();
        }
        return (double) value;
    }

    @Ignore
    public static double getFloat(long value) {
        if (value >= 9007199254740992L
                || value <= -9007199254740992L) {
            throw new OverflowException();
        }
        return (double) value;
    }

    @TypeInfo("ceylon.language::Character")
    public int getCharacter() {
        return (int) value;
    }

    @Ignore
    @TypeInfo("ceylon.language::Character")
    public static int getCharacter(long value) {
        return (int) value;
    }

    @Override
    public boolean getUnit() {
        return value==1;
    }

    @Ignore
    public static boolean getUnit(long value) {
        return value==1;
    }

    @Override
    public boolean getZero() {
        return value==0;
    }

    @Ignore
    public static boolean getZero(long value) {
        return value==0;
    }

    @Override
    public Integer getPredecessor() {
        return Integer.instance(value - 1);
    }

    @Ignore
    public static long getPredecessor(long value) {
        return value - 1;
    }

    @Override
    public Integer getSuccessor() {
        return Integer.instance(value + 1);
    }

    @Ignore
    public static long getSuccessor(long value) {
        return value + 1;
    }

    // Probably not spec-conformant
    public Integer complement() {
        return instance(~value);
    }

    @Ignore
    public static long complement(long value) {
        return ~value;
    }

    @Override
    public boolean equals(@Name("that") java.lang.Object that) {
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

    @Ignore
    public static boolean equals(long value, java.lang.Object that) {
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
    	return (int)(value ^ (value >>> 32));
    }

    @Ignore
    public static int hashCode(long value) {
        return (int)(value ^ (value >>> 32));
    }

    /*@Override
    public boolean largerThan(@Name("other") Integer other) {
        return value > other.value;
    }

    @Override
    public boolean smallerThan(@Name("other") Integer other) {
        return value < other.value;
    }

    @Override
    public boolean asLargeAs(@Name("other") Integer other) {
        return value >= other.value;
    }

    @Override
    public boolean asSmallAs(@Name("other") Integer other) {
        return value <= other.value;
    }*/

    @Override
    public Integer getNot() {
        return instance(~value);
    }
    
    @Ignore
    public static long getNot(long value){
        return ~value;
    }

    @Override
    public long getSize() {
        return 64;
    }
    
    @Ignore
    public static long getSize(long value){
        return 64;
    }

    @Override
    public Integer leftLogicalShift(@Name("shift") long shift) {
        return instance(value << shift);
    }
    
    @Ignore
    public static long leftLogicalShift(long value, long shift){
        return value << shift;
    }

    @Override
    public Integer rightLogicalShift(@Name("shift") long shift) {
        return instance(value >>> shift);
    }
    
    @Ignore
    public static long rightLogicalShift(long value, long shift){
        return value >>> shift;
    }

    @Override
    public Integer rightArithmeticShift(@Name("shift") long shift) {
        return instance(value >> shift);
    }
    
    @Ignore
    public static long rightArithmeticShift(long value, long shift){
        return value >> shift;
    }

    @Override
    public Integer and(@Name("other") Integer other) {
        return instance(value & other.value);
    }
    
    @Ignore
    public static long and(long value, long other){
        return value & other;
    }

    @Override
    public Integer or(@Name("other") Integer other) {
        return instance(value | other.value);
    }
    
    @Ignore
    public static long or(long value, long other){
        return value | other;
    }

    @Override
    public Integer xor(@Name("other") Integer other) {
        return instance(value ^ other.value);
    }
    
    @Ignore
    public static long xor(long value, long other){
        return value ^ other;
    }

    @Override
    public boolean get(@Name("index") long index) {
        long mask = 1 << index;
        return (value & mask) != 0;
    }

    @Ignore
    public static boolean get(long value, long index){
        long mask = 1 << index;
        return (value & mask) != 0;
    }
    
    @Override
    @Ignore
    public Integer set(long index) {
        long mask = 1 << index;
        return instance(value | mask);
    }

    @Override
    public Integer set(@Name("index") long index, @Name("bit") @Defaulted boolean bit) {
        long mask = 1 << index;
        return bit ? instance(value | mask) : instance(value & ~mask);
    }

    @Override
    @Ignore
    public boolean set$bit(long index) {
        return true;
    }

    @Ignore
    public static long set(long value, long index) {
        long mask = 1 << index;
        return value | mask;
    }

    @Ignore
    public static long set(long value, long index, boolean bit) {
        long mask = 1 << index;
        return bit ? value | mask : value & ~mask;
    }

    @Override
    public Integer clear(@Name("index") long index) {
        long mask = 1 << index;
        return instance(value & ~mask);
    }

    @Ignore
    public static long clear(long value, long index) {
        long mask = 1 << index;
        return value & ~mask;
    }

    @Override
    public Integer flip(@Name("index") long index) {
        long mask = 1 << index;
        return instance(value ^ mask);
    }

    @Ignore
    public static long flip(long value, long index) {
        long mask = 1 << index;
        return value ^ mask;
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
