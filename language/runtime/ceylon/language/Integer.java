package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 2)
@SatisfiedTypes({
    "ceylon.language.Scalar<ceylon.language.Integer>",
    "ceylon.language.Integral<ceylon.language.Integer>",
    "ceylon.language.Exponentiable<ceylon.language.Integer,ceylon.language.Integer>",
    "ceylon.language.Castable<ceylon.language.Integer|ceylon.language.Float>"
})
@Class(extendsType="ceylon.language.Object")
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
    
    @Override
    public Integer plus(@Name("other") Integer op) {
        return instance(value + op.value);
    }
    
    @Override
    public Integer minus(@Name("other") Integer op) {
        return instance(value - op.value);
    }
    
    @Override
    public Integer times(@Name("other") Integer op) {
        return instance(value * op.value);
    }
    
    @Override
    public Integer divided(@Name("other") Integer op) {
        return instance(value / op.value);
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
    public Integer power(@Name("other") Integer op) {
        long power = op.value;
        if (this.value == -1) {
            return instance(power % 2 == 0 ? 1 : -1);
        } else if (this.value == 1) {
            return instance(1L);
        }
        if (power < 0) {
            throw new RuntimeException(this.value + "**" + power + " cannot be represented as an Integer");
        } else if (power == 0L) {
            return instance(1L);
        }
        if (power >= POWER_BY_SQUARING_BREAKEVEN) {
            return instance(powerBySquaring(this.value, power));
        } else {
            return instance(powerByMultiplying(this.value, power));
        }   
    }
    
    @Ignore
    public Float plus(Float op) {
        return Float.instance(value + op.value);
    }
    
    @Ignore
    public Float minus(Float op) {
        return Float.instance(value - op.value);
    }
    
    @Ignore
    public Float times(Float op) {
        return Float.instance(value * op.value);
    }
    
    @Ignore
    public Float divided(Float op) {
        return Float.instance(value / op.value);
    }
    
    @Ignore
    public Float power(Float op) {
        return Float.instance(Math.pow(value, op.value)); // FIXME: ugly
    }
    
    @Override
    public Integer getMagnitude() {		
        return instance(Math.abs(value));
    }
    
    @Override
    public Integer getFractionalPart() {		
        return instance(0);
    }
    
    @Override
    public Integer getWholePart() {		
        return this;
    }
    
    @Override
    public boolean getPositive() {
        return value > 0;
    }
    
    @Override
    public boolean getNegative() {
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
    
    @Override
    public Integer remainder(@Name("other") Integer op) {
        return instance(value % op.value);
    }
    
    @Override
    public Integer getPositiveValue() {
        return this;
    }
    
    @Override
    public Integer getNegativeValue() {
        return instance(-value);
    }
    
    public boolean test(Integer op) {
        return value == op.value;
    }
    
    @Override
    public Comparison compare(@Name("other") Integer op) {
        long x = value;
        long y = op.value;
        return (x < y) ? smaller.getSmaller() :
            ((x == y) ? equal.getEqual() : larger.getLarger());
    }
    
    @Override
    public java.lang.String toString() {
        return java.lang.Long.toString(value);
    }
    
    // Conversions between numeric types
    
    @TypeInfo(value="ceylon.language.Integer")
    @Override
    public long getInteger() {
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
    
    @Override
    public boolean getUnit() {
        return value==1;
    }
    
    @Override
    public boolean getZero() {
        return value==0;
    }
    
    // Just a kludge til we have full autoboxing
    public long longValue() {
        return value;
    }
    
    @Override
    public Integer getPredecessor() {
        return Integer.instance(value - 1);
    }
    
    @Override
    public Integer getSuccessor() {
        return Integer.instance(value + 1);
    }
    
    // Probably not spec-conformant
    public Integer complement() {
        return instance(~value);
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
    
    @Override
    public int hashCode() {
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
    
    @TypeInfo("ceylon.language.Character")
    public int getCharacter() {
        return (int) value;
    }
    
}
