package ceylon;

public final class Natural
    extends Object
    implements Integral<Natural>, Invertable<Integer>, Case<Natural> {

    private final long value;
    private Natural(long l) {
        value = l;
    }

    public static ceylon.Natural instance(long l) {
        assert l >= 0;
        return new ceylon.Natural(l);
    }

    public ceylon.Natural plus(ceylon.Natural op) {
        return instance(value + op.value);
    }

    public ceylon.Natural minus(ceylon.Natural op) {
        return instance(value - op.value);
    }

    public ceylon.Natural times(ceylon.Natural op) {
        return instance(value * op.value);
    }

    public ceylon.Natural divided(ceylon.Natural op) {
        return instance(value / op.value);
    }

    public ceylon.Natural power(ceylon.Natural op) {
        return instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }

    public ceylon.Natural remainder(ceylon.Natural op) {
        return instance(value % op.value);
    }

    public ceylon.Integer inverse() {
        return ceylon.Integer.instance(-value);
    }

    public ceylon.Boolean test(ceylon.Natural op) {
        return ceylon.Boolean.instance(value == op.value);
    }

    public ceylon.Comparison compare(ceylon.Natural op) {
        long x = value;
        long y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    @Extension
    public ceylon.String string() {
        return ceylon.String.instance(java.lang.Long.toString(value));
    }
}
