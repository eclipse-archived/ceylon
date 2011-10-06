package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

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

    public static Natural instance(long l) {
        assert l >= 0;
        return new Natural(l);
    }

    @Override
    public Natural plus(Natural op) {
        return instance(value + op.value);
    }

    @Override
    public Natural minus(Natural op) {
        return instance(value - op.value);
    }

    @Override
    public Natural times(Natural op) {
        return instance(value * op.value);
    }

    @Override
    public Natural divided(Natural op) {
        return instance(value / op.value);
    }

    @Override
    public Natural power(Natural op) {
        return instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }

    @Override
    public Natural remainder(Natural op) {
        return instance(value % op.value);
    }

    @Override
    public Integer getPositiveValue() {
        return Integer.instance(value);
    }

    @Override
    public Integer getNegativeValue() {
        return Integer.instance(-value);
    }

    public boolean test(Natural op) {
        return value == op.value;
    }

    @Override
    public Comparison compare(Natural op) {
        long x = value;
        long y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    @Override
    public java.lang.String toString() {
        return java.lang.Long.toString(value);
    }

    // Conversions between numeric types

    @Override
    public Natural getNatural() {
        return this;
    }

    @Override
    public Integer getInteger() {
        return Integer.instance(value);
    }

    @Override
    public Float getFloat() {
        return Float.instance(value);
    }

    // Just a kludge til we have full autoboxing
    public long longValue() {
        return value;
    }

    public int intValue() {
        return (int)value;
    }

    @Override
    public Natural getPredecessor() {
        return Natural.instance(value - 1);
    }

    @Override
    public Natural getSuccessor() {
        return Natural.instance(value + 1);
    }

    public Natural and(Natural op) {
        return Natural.instance(value & op.value);
    }

    public Natural or(Natural op) {
        return Natural.instance(value | op.value);
    }

    public Natural xor(Natural op) {
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
    public <CastValue extends Numeric> CastValue castTo() {
        return (CastValue) this;
    }

    @Override
    public java.lang.String getFormatted() {
        return java.lang.Long.toString(value);
    }

    @Override
    public boolean largerThan(Natural other) {
        return value > other.value;
    }

    @Override
    public boolean smallerThan(Natural other) {
        return value < other.value;
    }

    @Override
    public boolean asLargeAs(Natural other) {
        return value >= other.value;
    }

    @Override
    public boolean asSmallAs(Natural other) {
        return value <= other.value;
    }
}
