package ceylon.language;

public final class Float
    extends Object
    implements Numeric<Float>, Invertable<Float> {

    private final double value;
    private Float(double d) {
        value = d;
    }

    public static ceylon.language.Float instance(double d) {
        return new ceylon.language.Float(d);
    }

    public ceylon.language.Float plus(ceylon.language.Float op) {
        return instance(value + op.value);
    }

    public ceylon.language.Float minus(ceylon.language.Float op) {
        return instance(value - op.value);
    }

    public ceylon.language.Float times(ceylon.language.Float op) {
        return instance(value * op.value);
    }

    public ceylon.language.Float divided(ceylon.language.Float op) {
        return instance(value / op.value);
    }

    public ceylon.language.Float power(ceylon.language.Float op) {
        return instance(Math.pow(value, op.value));
    }

    public ceylon.language.Float inverse() {
        return instance(-value);
    }

    public ceylon.language.Comparison compare(ceylon.language.Float op) {
        double x = value;
        double y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    @Extension
    public java.lang.String toString() {
        return java.lang.Double.toString(value);
    }

    // Conversions between numeric types

    public ceylon.language.Natural natural() {
        return ceylon.language.Natural.instance((long) value);
    }

    public ceylon.language.Integer integer() {
        return ceylon.language.Integer.instance((long) value);
    }

    public ceylon.language.Float toFloat() {
        return this;
    }

    public ceylon.language.Float pred() {
        return Float.instance(value - 1);
    }

    public ceylon.language.Float succ() {
        return Float.instance(value + 1);
    }

    @Override
    public java.lang.String getFormatted() {
        return java.lang.Double.toString(value);
    }
}
