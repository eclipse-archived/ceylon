package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public final class Float
    extends Object
    implements Castable<Float>, Numeric<Float>, Invertable<Float> {

    private final double value;
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
    public Float plus(Float op) {
        return instance(value + op.value);
    }

    @Override
    public Float minus(Float op) {
        return instance(value - op.value);
    }

    @Override
    public Float times(Float op) {
        return instance(value * op.value);
    }

    @Override
    public Float divided(Float op) {
        return instance(value / op.value);
    }

    @Override
    public Float power(Float op) {
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
	public Integer getSign() {
		if (value > 0)
			return Integer.instance(1);
		if (value < 0)
			return Integer.instance(-1);
		return Integer.instance(0);
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
    public Comparison compare(Float op) {
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
        return value;
    }
    
    @Override
    public boolean getIntegral() {
    	return value == (double) Math.round(value);
    }
    
    @Override
    public boolean getUnit() {
    	return value==1.0;
    }
    
    @Override
    public boolean getZero() {
    	return value==0.0;
    }

    @Override
    public java.lang.String getFormatted() {
        return java.lang.Double.toString(value);
    }

    @Override
    public boolean largerThan(Float other) {
        return value > other.value;
    }

    @Override
    public boolean smallerThan(Float other) {
        return value < other.value;
    }

    @Override
    public boolean asLargeAs(Float other) {
        return value >= other.value;
    }

    @Override
    public boolean asSmallAs(Float other) {
        return value <= other.value;
    }

    @Override
    public <CastValue extends Float> CastValue castTo() {
        return (CastValue)this;
    }
}
