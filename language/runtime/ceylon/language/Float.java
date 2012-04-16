package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes({
    "ceylon.language.Castable<ceylon.language.Float>",
    "ceylon.language.Numeric<ceylon.language.Float>"
})
public final class Float
    implements Castable<Float>, Numeric<Float> {
    
    final double value;
    private Float(double d) {
        value = d;
    }
    
    @Ignore
    public static Float instance(double d) {
        return new Float(d);
    }
    
    public double doubleValue() {
        return value;
    }
    
    @Override
    public Float plus(@Name("other") Float op) {
        return instance(value + op.value);
    }
    
    @Override
    public Float minus(@Name("other") Float op) {
        return instance(value - op.value);
    }
    
    @Override
    public Float times(@Name("other") Float op) {
        return instance(value * op.value);
    }
    
    @Override
    public Float divided(@Name("other") Float op) {
        return instance(value / op.value);
    }
    
    @Override
    public Float power(@Name("other") Float op) {
        return instance(Math.pow(value, op.value));
    }
    
    @Ignore
    public Float plus(Integer op) {
        return instance(value + op.value);
    }
    
    @Ignore
    public Float minus(Integer op) {
        return instance(value - op.value);
    }
    
    @Ignore
    public Float times(Integer op) {
        return instance(value * op.value);
    }
    
    @Ignore
    public Float divided(Integer op) {
        return instance(value / op.value);
    }
    
    @Ignore
    public Float power(Integer op) {
        return instance(Math.pow(value, op.value));
    }
    
    @Override
    public Float getMagnitude() {
        return instance(Math.abs(value));
    }
    
    @Override
    public Float getFractionalPart() {		
        return instance(value - (long)value);
    }
    
    @Override
    public Float getWholePart() {		
        return instance((long)value);
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
    public Float getNegativeValue() {
        return instance(-value);
    }
    
    @Override
    public Float getPositiveValue() {
        return this;
    }
    
    @Override
    public Comparison compare(@Name("other") Float op) {
        double x = value;
        double y = op.value;
        return (x < y) ? smaller.getSmaller() :
            ((x == y) ? equal.getEqual() : larger.getLarger());
    }
    
    @Override
    public java.lang.String toString() {
        return java.lang.Double.toString(value);
    }
    
    // Conversions between numeric types
    
    @TypeInfo(value="ceylon.language.Integer")
    @Override
    public long getInteger() {
        if (value >= Long.MIN_VALUE
                && value <= Long.MAX_VALUE) {
            return (long)value;
        } 
        throw new OverflowException();
    }
    
    @TypeInfo(value="ceylon.language.Float")
    @Override
    public double getFloat() {
        return value;
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
		long bits = Double.doubleToLongBits(value);
		return (int)(bits ^ (bits >>> 32));
    }
    
    @Override
    public <CastValue extends Float> CastValue castTo() {
        return (CastValue)this;
    }
    
    public boolean getUndefined() {
        return Double.isNaN(this.value);
    }
    
    public boolean getInfinite() {
        return Double.isInfinite(this.value);
    }
    
    public boolean getFinite() {
        return !Double.isInfinite(this.value) && !getUndefined();
    }
}
