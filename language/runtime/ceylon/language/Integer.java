package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.ValueType;

@Ceylon(major = 2)
@SatisfiedTypes({
    "ceylon.language.Scalar<ceylon.language.Integer>",
    "ceylon.language.Integral<ceylon.language.Integer>",
    "ceylon.language.Exponentiable<ceylon.language.Integer,ceylon.language.Integer>",
    "ceylon.language.Castable<ceylon.language.Integer|ceylon.language.Float>"
})
@Class(extendsType="ceylon.language.Object")
@ValueType
public final class Integer
    implements Scalar<Integer>, Integral<Integer>,
               Exponentiable<Integer,Integer>,
               Castable<Numeric> {

    final long value;

    private Integer(long l) {
        value = l;
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
        return (x < y) ? smaller.getSmaller() :
            ((x == y) ? equal.getEqual() : larger.getLarger());
    }

    @Ignore
    public static Comparison compare(long value, long otherValue) {
        long x = value;
        long y = otherValue;
        return (x < y) ? smaller.getSmaller() :
            ((x == y) ? equal.getEqual() : larger.getLarger());
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

    @TypeInfo(value="ceylon.language.Integer")
    @Override
    public long getInteger() {
        return value;
    }

    @Ignore
    public static long getInteger(long value) {
        return value;
    }

    @TypeInfo(value="ceylon.language.Float")
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

    @TypeInfo("ceylon.language.Character")
    public int getCharacter() {
        return (int) value;
    }

    @Ignore
    @TypeInfo("ceylon.language.Character")
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

    @Override
    @Annotations(@Annotation("actual"))
    @TypeInfo("ceylon.language.Integer")
    public long distanceFrom(Integer other) {
        return value-other.value;
    }

    @Ignore
    public long distanceFrom(long other) {
        return value-other;
    }
    @Ignore
    public static long distanceFrom(long a, long b) {
        return a-b;
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
    public <CastValue extends Numeric> CastValue castTo() {
        return (CastValue)this;
    }

    @Ignore
    public <CastValue extends Numeric> CastValue castTo(long value) {
        // FIXME Is this correct?
        return (CastValue)instance(value);
    }

}
