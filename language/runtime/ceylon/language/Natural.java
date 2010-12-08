package ceylon.language;

public final class Natural
    extends Object
    implements Integral<Natural>, Invertable<Integer>, Case<Natural> {

    private final long value;
    private Natural(long l) {
        if (l < 0)
            throw new NegativeNumberException();
        value = l;
    }

    public static ceylon.language.Natural instance(long l) {
        assert l >= 0;
        return new ceylon.language.Natural(l);
    }

    public ceylon.language.Natural plus(ceylon.language.Natural op) {
        return instance(value + op.value);
    }

    public ceylon.language.Natural minus(ceylon.language.Natural op) {
        return instance(value - op.value);
    }

    public ceylon.language.Natural times(ceylon.language.Natural op) {
        return instance(value * op.value);
    }

    public ceylon.language.Natural divided(ceylon.language.Natural op) {
        return instance(value / op.value);
    }

    public ceylon.language.Natural power(ceylon.language.Natural op) {
        return instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }

    public ceylon.language.Natural remainder(ceylon.language.Natural op) {
        return instance(value % op.value);
    }

    public ceylon.language.Integer inverse() {
        return ceylon.language.Integer.instance(-value);
    }

    public ceylon.language.Boolean test(ceylon.language.Natural op) {
        return ceylon.language.Boolean.instance(value == op.value);
    }

    public ceylon.language.Comparison compare(ceylon.language.Natural op) {
        long x = value;
        long y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    @Extension
    public ceylon.language.String string() {
        return ceylon.language.String.instance(java.lang.Long.toString(value));
    }

    // Conversions between numeric types

    public ceylon.language.Natural natural() {
        return this;
    }

    @Extension
    public ceylon.language.Integer integer() {
        return ceylon.language.Integer.instance(value);
    }

    @Extension
    public ceylon.language.Whole whole() {
        throw new RuntimeException("not implemented");
    }

    @Extension
    public ceylon.language.Float floatXXX() {
        return ceylon.language.Float.instance(value);
    }

    @Extension
    public ceylon.language.Decimal decimal() {
        throw new RuntimeException("not implemented");
    }

    // Just a kludge til we have full autoboxing
    @Extension
    public long longValue() {
        return value;
    }

    @Extension
    public int intValue() {
        return (int)value;
    }

    public ceylon.language.Natural pred() {
        return Natural.instance(value - 1);
    }

    public ceylon.language.Natural succ() {
        return Natural.instance(value + 1);
    }

    public ceylon.language.Natural and(ceylon.language.Natural op) {
        return Natural.instance(value & op.value);
    }
}
