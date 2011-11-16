package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@SatisfiedTypes({
    "ceylon.language.Castable<ceylon.language.Integer|ceylon.language.Float>",
    "ceylon.language.Integral<ceylon.language.Integer>",
    "ceylon.language.Invertable<ceylon.language.Integer>"
})
public final class Integer
    extends Object
    implements Castable<Numeric>, Integral<Integer>, Invertable<Integer> {
    
    final long value;
    private Integer(long l) {
        value = l;
    }
    
    public static Integer instance(long l) {
        return new Integer(l);
    }
    
    @Override
    public Integer plus(Integer op) {
        return instance(value + op.value);
    }
    
    @Override
    public Integer minus(Integer op) {
        return instance(value - op.value);
    }
    
    @Override
    public Integer times(Integer op) {
        return instance(value * op.value);
    }
    
    @Override
    public Integer divided(Integer op) {
        return instance(value / op.value);
    }
    
    @Override
    public Integer power(Integer op) {
        return instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }
    
    public Integer plus(Natural op) {
        return instance(value + op.value);
    }
    
    public Integer minus(Natural op) {
        return instance(value - op.value);
    }
    
    public Integer times(Natural op) {
        return instance(value * op.value);
    }
    
    public Integer divided(Natural op) {
        return instance(value / op.value);
    }
    
    public Integer power(Natural op) {
        return instance((long) Math.pow(value, op.value));
    }
    
    public Float plus(Float op) {
        return Float.instance(value + op.value);
    }
    
    public Float minus(Float op) {
        return Float.instance(value - op.value);
    }
    
    public Float times(Float op) {
        return Float.instance(value * op.value);
    }
    
    public Float divided(Float op) {
        return Float.instance(value / op.value);
    }
    
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
    public Integer getSign() {
        if (value > 0)
            return Integer.instance(1);
        if (value < 0)
            return Integer.instance(-1);
        return Integer.instance(0);
    }
    
    @Override
    public Integer remainder(Integer op) {
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
    public Comparison compare(Integer op) {
        long x = value;
        long y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }
    
    @Override
    public java.lang.String toString() {
        return java.lang.Long.toString(value);
    }
    
    // Conversions between numeric types
    
    @TypeInfo(value="ceylon.language.Natural")
    @Override
    public long getNatural() {
        if (value < 0.0)
            throw new NegativeNumberException();
        return (long) value;
    }
    
    @TypeInfo(value="ceylon.language.Integer")
    @Override
    public int getInteger() {
        return (int) value;
    }
    
    @TypeInfo(value="ceylon.language.Float")
    @Override
    public double getFloat() {
        return (double) value;
    }
    
    @Override
    public boolean getIntegral() {
        return true;
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
    
    public int intValue() {
        return (int)value;
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
    public boolean equals(java.lang.Object s) {
        if (s instanceof Integer) {
            return value == ((Integer)s).value;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
    	return (int)(value ^ (value >>> 32));
    }
    
    @Override
    public java.lang.String getFormatted() {
        return java.lang.Long.toString(value);
    }
    
    @Override
    public boolean largerThan(Integer other) {
        return value > other.value;
    }
    
    @Override
    public boolean smallerThan(Integer other) {
        return value < other.value;
    }
    
    @Override
    public boolean asLargeAs(Integer other) {
        return value >= other.value;
    }
    
    @Override
    public boolean asSmallAs(Integer other) {
        return value <= other.value;
    }
    
    @Override
    public <CastValue extends Numeric> CastValue castTo() {
        return (CastValue)this;
    }
    
}
