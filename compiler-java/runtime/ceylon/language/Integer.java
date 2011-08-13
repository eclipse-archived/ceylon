package ceylon.language;

public final class Integer
    extends Object
    implements Integral<Integer>, Invertable<Integer> {

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

    public boolean test(ceylon.language.Integer op) {
        return value == op.value;
    }

    public ceylon.language.Comparison compare(ceylon.language.Integer op) {
        long x = value;
        long y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    @Extension
    public java.lang.String toString() {
        return java.lang.Long.toString(value);
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
    public ceylon.language.Float toFloat() {
        return ceylon.language.Float.instance(value);
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

    public ceylon.language.Integer getPredecessor() {
        return Integer.instance(value - 1);
    }

    public ceylon.language.Integer getSuccessor() {
        return Integer.instance(value + 1);
    }

    // Probably not spec-conformant
    public ceylon.language.Integer complement() {
        return instance(~value);
    }

    @Override
    public boolean equals(java.lang.Object s) {
        if (s instanceof Integer) {
            return value == ((Integer)s).value;
        } else {
            return false;
        }
    }
}
