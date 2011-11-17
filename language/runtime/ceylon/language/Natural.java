package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@SatisfiedTypes({
    "ceylon.language.Castable<ceylon.language.Natural|ceylon.language.Integer|ceylon.language.Float>",
    "ceylon.language.Integral<ceylon.language.Natural>",
    "ceylon.language.Invertable<ceylon.language.Integer>"
})
public final class Natural
    extends Object
    implements Castable<Numeric>, Integral<Natural>, Invertable<Integer> {
    
    final long value;
    private Natural(long l) {
        if (l < 0)
            throw new NegativeNumberException();
        value = l;
    }
    
    public static Natural instance(long l) {
        assert l >= 0;
        return new Natural(l);
    }
    
    @Override
    public Natural plus(Natural op) {
        long result = value + op.value;
        if (result<0) 
        	throw new OverflowException();
		return instance(result);
    }
    
    @Override
    public Natural minus(Natural op) {
        return instance(value - op.value);
    }
    
    @Override
    public Natural times(Natural op) {
        long result = value * op.value;
        if (result<0) 
        	throw new OverflowException();
        return instance(result);
    }
    
    @Override
    public Natural divided(Natural op) {
        return instance(value / op.value);
    }
    
    @Override
    public Natural power(Natural op) {
        long result = (long) Math.pow(value, op.value); // FIXME: ugly
        if (result==Long.MAX_VALUE) 
        	throw new OverflowException();
        return instance(result);
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
    
    public Integer plus(Integer op) {
        return Integer.instance(value + op.value);
    }
    
    public Integer minus(Integer op) {
        return Integer.instance(value - op.value);
    }
    
    public Integer times(Integer op) {
        return Integer.instance(value * op.value);
    }
    
    public Integer divided(Integer op) {
        return Integer.instance(value / op.value);
    }
    
    public Integer power(Integer op) {
        return Integer.instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }
    
    @Override
    public Natural remainder(Natural op) {
        return instance(value % op.value);
    }
    
    @Override
    public Natural getMagnitude() {
        return this;
    }
    
    @Override
    public Natural getFractionalPart() {		
        return instance(0);
    }
    
    @Override
    public Natural getWholePart() {		
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
    public Integer getPositiveValue() {
        return Integer.instance(value);
    }
    
    @Override
    public Integer getNegativeValue() {
        return Integer.instance(-value);
    }
    
    public boolean test(Natural op) {
        return value == op.value;
    }
    
    @Override
    public Comparison compare(Natural op) {
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
        return value;
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
        return (int) value;
    }
    
    @Override
    public Natural getPredecessor() {
        return Natural.instance(value - 1);
    }
    
    @Override
    public Natural getSuccessor() {
        return Natural.instance(value + 1);
    }
    
    public Natural and(Natural op) {
        return Natural.instance(value & op.value);
    }
    
    public Natural or(Natural op) {
        return Natural.instance(value | op.value);
    }
    
    public Natural xor(Natural op) {
        return Natural.instance(value ^ op.value);
    }
    
    @Override
    public boolean equals(java.lang.Object s) {
        if (s instanceof Natural) {
            return value == ((Natural)s).value;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
    	return (int)(value ^ (value >>> 32));
    }
    
    @Override
    @TypeParameters({
        @TypeParameter(value = "CastValue", satisfies = "ceylon.language.Natural|ceylon.language.Integer|ceylon.language.Float")
    })
    public <CastValue extends Numeric> CastValue castTo() {
        return (CastValue) this;
    }
    
    @Override
    public java.lang.String getFormatted() {
        return java.lang.Long.toString(value);
    }
    
    @Override
    public boolean largerThan(Natural other) {
        return value > other.value;
    }
    
    @Override
    public boolean smallerThan(Natural other) {
        return value < other.value;
    }
    
    @Override
    public boolean asLargeAs(Natural other) {
        return value >= other.value;
    }
    
    @Override
    public boolean asSmallAs(Natural other) {
        return value <= other.value;
    }
}
