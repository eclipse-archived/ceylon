package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Class(extendsType="ceylon.language.Object")
public final class Float
    implements Castable<Float>, Numeric<Float>, 
               Subtractable<Float,Float> {
    
    final double value;
    private Float(double d) {
        value = d;
    }
    
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
    
    public Float plus(Natural op) {
        return instance(value + op.value);
    }
    
    public Float minus(Natural op) {
        return instance(value - op.value);
    }
    
    public Float times(Natural op) {
        return instance(value * op.value);
    }
    
    public Float divided(Natural op) {
        return instance(value / op.value);
    }
    
    public Float power(Natural op) {
        return instance(Math.pow(value, op.value));
    }
    
    public Float plus(Integer op) {
        return instance(value + op.value);
    }
    
    public Float minus(Integer op) {
        return instance(value - op.value);
    }
    
    public Float times(Integer op) {
        return instance(value * op.value);
    }
    
    public Float divided(Integer op) {
        return instance(value / op.value);
    }
    
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
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }
    
    @Override
    public java.lang.String toString() {
        return java.lang.Double.toString(value);
    }
    
    // Conversions between numeric types
    
    @TypeInfo(value="ceylon.language.Natural")
    @Override
    public long getNatural() {
        if (value < 0.0)
            throw new NegativeNumberException();
        return Math.round(value);
    }
    
    @TypeInfo(value="ceylon.language.Integer")
    @Override
    public long getInteger() {
        return Math.round(value);
    }
    
    @TypeInfo(value="ceylon.language.Float")
    @Override
    public double getFloat() {
        return value;
    }
    
    @Override
    public java.lang.String getFormatted() {
        return java.lang.Double.toString(value);
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
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Equality") java.lang.Object that) {
        if (that instanceof Natural) {
            return value == ((Natural)that).value;
        } 
        else if (that instanceof Integer) {
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
}
