package ceylon;

public final class Integer
    extends Object
    implements Integral<Integer>, Invertable<Integer>, Case<Integer> {

    private final long value;
    private Integer(long l) {
        value = l;
    }

    public static ceylon.Integer instance(long l) {
        return new ceylon.Integer(l);
    }

    public ceylon.Integer plus(ceylon.Integer op) {
        return instance(value + op.value);
    }

    public ceylon.Integer minus(ceylon.Integer op) {
        return instance(value - op.value);
    }

    public ceylon.Integer times(ceylon.Integer op) {
        return instance(value * op.value);
    }

    public ceylon.Integer divided(ceylon.Integer op) {
        return instance(value / op.value);
    }

    public ceylon.Integer power(ceylon.Integer op) {
        return instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }

    public ceylon.Integer remainder(ceylon.Integer op) {
        return instance(value % op.value);
    }

    public ceylon.Integer operatorBitwiseAnd(ceylon.Integer op) {
        return instance(value & op.value);
    }

    public ceylon.Integer operatorBitwiseOr(ceylon.Integer op) {
        return instance(value | op.value);
    }

    public ceylon.Integer operatorBitwiseXor(ceylon.Integer op) {
        return instance(value ^ op.value);
    }

    public ceylon.Boolean operatorEqual(ceylon.Integer op) {
        return ceylon.Boolean.instance(value == op.value);
    }

    public ceylon.Boolean operatorIdentical(ceylon.Integer op) {
        return ceylon.Boolean.instance(value == op.value); // FIXME: correct?
    }

    public ceylon.Boolean operatorNotEqual(ceylon.Integer op) {
        return ceylon.Boolean.instance(value != op.value);
    }

    public ceylon.Boolean operatorLessThan(ceylon.Integer op) {
        return ceylon.Boolean.instance(value < op.value);
    }

    public ceylon.Boolean operatorGreaterThan(ceylon.Integer op) {
        return ceylon.Boolean.instance(value > op.value);
    }

    public ceylon.Boolean operatorLessEqual(ceylon.Integer op) {
        return ceylon.Boolean.instance(value <= op.value);
    }

    public ceylon.Boolean operatorGreaterEqual(ceylon.Integer op) {
        return ceylon.Boolean.instance(value >= op.value);
    }

    public ceylon.Integer inverse() {
        return instance(-value);
    }

    public ceylon.Boolean test(ceylon.Integer op) {
        return ceylon.Boolean.instance(value == op.value);
    }

    public ceylon.Comparison compare(ceylon.Integer op) {
        long x = value;
        long y = op.value;
        return (x < y) ? Comparison.SMALLER :
            ((x == y) ? Comparison.EQUAL : Comparison.LARGER);
    }

    public ceylon.String string() {
        return ceylon.String.instance(java.lang.Long.toString(value));
    }
}
