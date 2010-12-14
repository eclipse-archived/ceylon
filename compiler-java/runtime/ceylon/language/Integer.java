package ceylon.language;

public final class Integer
    extends Object
    implements Integral<Integer>, Invertable<Integer>, Case<Integer> {

    private final long value;
    private Integer(long l) {
        value = l;
    }

    public static ceylon.language.Integer instance(long l) {
        return new ceylon.language.Integer(l);
    }

    public ceylon.language.Integer plus(ceylon.language.Integer op) {
        return instance(value + op.value);
    }

    public ceylon.language.Integer minus(ceylon.language.Integer op) {
        return instance(value - op.value);
    }

    public ceylon.language.Integer times(ceylon.language.Integer op) {
        return instance(value * op.value);
    }

    public ceylon.language.Integer divided(ceylon.language.Integer op) {
        return instance(value / op.value);
    }

    public ceylon.language.Integer power(ceylon.language.Integer op) {
        return instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }

    public ceylon.language.Integer remainder(ceylon.language.Integer op) {
        return instance(value % op.value);
    }

    public ceylon.language.Integer inverse() {
        return instance(-value);
    }

    public ceylon.language.Boolean test(ceylon.language.Integer op) {
        return ceylon.language.Boolean.instance(value == op.value);
    }

    public ceylon.language.Comparison compare(ceylon.language.Integer op) {
        long x = value;
        long y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    public ceylon.language.Boolean operatorEqual(ceylon.language.Integer s) {
        return Boolean.instance(value == s.value);
    }

    @Extension
    public ceylon.language.String string() {
        return ceylon.language.String.instance(java.lang.Long.toString(value));
    }

    // Conversions between numeric types

    public ceylon.language.Natural natural() {
        return ceylon.language.Natural.instance(value);
    }

    public ceylon.language.Integer integer() {
        return this;
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

    public ceylon.language.Integer pred() {
        return Integer.instance(value - 1);
    }

    public ceylon.language.Integer succ() {
        return Integer.instance(value + 1);
    }

    // Probably not spec-conformant
    public ceylon.language.Integer complement() {
        return instance(~value);
    }
}
