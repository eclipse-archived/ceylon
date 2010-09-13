package ceylon;

public final class Float
    extends Object
    implements Numeric<Float>, Invertable<Float> {

    private final double value;
    private Float(double d) {
        value = d;
    }

    public static ceylon.Float instance(double d) {
        return new ceylon.Float(d);
    }

    public ceylon.Float plus(ceylon.Float op) {
        return instance(value + op.value);
    }

    public ceylon.Float minus(ceylon.Float op) {
        return instance(value - op.value);
    }

    public ceylon.Float times(ceylon.Float op) {
        return instance(value * op.value);
    }

    public ceylon.Float divided(ceylon.Float op) {
        return instance(value / op.value);
    }

    public ceylon.Float power(ceylon.Float op) {
        return instance(Math.pow(value, op.value));
    }

    public ceylon.Float inverse() {
        return instance(-value);
    }

    public ceylon.Comparison compare(ceylon.Float op) {
        double x = value;
        double y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    @Extension
    public ceylon.String string() {
        return ceylon.String.instance(java.lang.Double.toString(value));
    }

    // Conversions between numeric types

    public ceylon.Natural natural() {
        return ceylon.Natural.instance((long) value);
    }

    public ceylon.Integer integer() {
        return ceylon.Integer.instance((long) value);
    }

    public ceylon.Whole whole() {
        throw new RuntimeException("not implemented");
    }

    public ceylon.Float floatXXX() {
        return this;
    }

    @Extension
    public ceylon.Decimal decimal() {
        throw new RuntimeException("not implemented");
    }
}
