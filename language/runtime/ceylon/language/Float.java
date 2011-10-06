package ceylon.language;

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

    public Float plus(Float op) {
        return instance(value + op.value);
    }

    public Float minus(Float op) {
        return instance(value - op.value);
    }

    public Float times(Float op) {
        return instance(value * op.value);
    }

    public Float divided(Float op) {
        return instance(value / op.value);
    }

    public Float power(Float op) {
        return instance(Math.pow(value, op.value));
    }

    @Override
    public Float getNegativeValue() {
        return instance(-value);
    }

    @Override
    public Float getPositiveValue() {
        return this;
    }

    public Comparison compare(Float op) {
        double x = value;
        double y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    public java.lang.String toString() {
        return java.lang.Double.toString(value);
    }

    // Conversions between numeric types

    public Natural getNatural() {
        return Natural.instance((long) value);
    }

    public Integer getInteger() {
        return Integer.instance((long) value);
    }

    public Float getFloat() {
        return this;
    }

    public Float getPredecessor() {
        return Float.instance(value - 1);
    }

    public Float getSuccessor() {
        return Float.instance(value + 1);
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
