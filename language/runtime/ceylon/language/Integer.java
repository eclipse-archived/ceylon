package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;

@SatisfiedTypes({
    "ceylon.language.Castable<ceylon.language.Integer|ceylon.language.Float>",
    "ceylon.language.Integral<ceylon.language.Integer>",
    "ceylon.language.Invertable<ceylon.language.Integer>"
})
public final class Integer
    extends Object
    implements Castable<Numeric>, Integral<Integer>, Invertable<Integer> {

    private final long value;
    private Integer(long l) {
        value = l;
    }

    public static Integer instance(long l) {
        return new Integer(l);
    }

    public Integer plus(Integer op) {
        return instance(value + op.value);
    }

    public Integer minus(Integer op) {
        return instance(value - op.value);
    }

    public Integer times(Integer op) {
        return instance(value * op.value);
    }

    public Integer divided(Integer op) {
        return instance(value / op.value);
    }

    public Integer power(Integer op) {
        return instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }

    public Integer remainder(Integer op) {
        return instance(value % op.value);
    }

    @Override
    public Integer getPositiveValue() {
        return this;
    }

    @Override
    public Integer getNegativeValue() {
        return instance(-value);
    }

    public boolean test(Integer op) {
        return value == op.value;
    }

    public Comparison compare(Integer op) {
        long x = value;
        long y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    public java.lang.String toString() {
        return java.lang.Long.toString(value);
    }

    // Conversions between numeric types

    public Natural getNatural() {
        return Natural.instance(value);
    }

    public Integer getInteger() {
        return this;
    }

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

    public Integer getPredecessor() {
        return Integer.instance(value - 1);
    }

    public Integer getSuccessor() {
        return Integer.instance(value + 1);
    }

    // Probably not spec-conformant
    public Integer complement() {
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

    @Override
    public java.lang.String getFormatted() {
        return java.lang.Long.toString(value);
    }

    @Override
    public boolean largerThan(Integer other) {
        return value > other.value;
    }

    @Override
    public boolean smallerThan(Integer other) {
        return value < other.value;
    }

    @Override
    public boolean asLargeAs(Integer other) {
        return value >= other.value;
    }

    @Override
    public boolean asSmallAs(Integer other) {
        return value <= other.value;
    }

    @Override
    public <CastValue extends Numeric> CastValue castTo() {
        return (CastValue)this;
    }
}
