package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.*;

@SatisfiedTypes({
    "ceylon.language.Castable<ceylon.language.Natural|ceylon.language.Integer|ceylon.language.Float>",
    "ceylon.language.Integral<ceylon.language.Natural>",
    "ceylon.language.Invertable<ceylon.language.Integer>"
})
public final class Natural
    extends Object
    implements Castable<Numeric>, Integral<Natural>, Invertable<Integer> {

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

    public boolean test(ceylon.language.Natural op) {
        return value == op.value;
    }

    public ceylon.language.Comparison compare(ceylon.language.Natural op) {
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

    public ceylon.language.Natural getPredecessor() {
        return Natural.instance(value - 1);
    }

    public ceylon.language.Natural getSuccessor() {
        return Natural.instance(value + 1);
    }

    public ceylon.language.Natural and(ceylon.language.Natural op) {
        return Natural.instance(value & op.value);
    }

    public ceylon.language.Natural or(ceylon.language.Natural op) {
        return Natural.instance(value | op.value);
    }

    public ceylon.language.Natural xor(ceylon.language.Natural op) {
        return Natural.instance(value ^ op.value);
    }

    @Override
    public boolean equals(java.lang.Object s) {
        if (s instanceof Natural) {
            return value == ((Natural)s).value;
        } else {
            return false;
        }
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "CastValue", satisfies = "ceylon.language.Natural|ceylon.language.Integer|ceylon.language.Float")
    })
    public <CastValue extends Numeric> CastValue as() {
        return (CastValue) this;
    }

    @Override
    public java.lang.String getFormatted() {
        return java.lang.Long.toString(value);
    }
}
