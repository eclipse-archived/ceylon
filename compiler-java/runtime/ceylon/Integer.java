package ceylon;

public final class Integer
{
    private final long value;
    private Integer(long l) {
        value = l;
    }

    public static ceylon.Integer instance(long l) {
        return new ceylon.Integer(l);
    }

    public ceylon.Integer operatorAdd(ceylon.Integer op) {
        return instance(value + op.value);
    }

    public ceylon.Integer operatorSubtract(ceylon.Integer op) {
        return instance(value - op.value);
    }

    public ceylon.Integer operatorMultiply(ceylon.Integer op) {
        return instance(value * op.value);
    }

    public ceylon.Integer operatorPower(ceylon.Integer op) {
        return instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }

    public ceylon.Integer operatorDivide(ceylon.Integer op) {
        return instance(value / op.value);
    }

    public ceylon.Integer operatorModulo(ceylon.Integer op) {
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

    public ceylon.Integer operatorCompare(ceylon.Integer op) {
        return instance(java.lang.Long.valueOf(value).compareTo(op.value));
    }

    public ceylon.String asString() {
        return ceylon.String.instance(java.lang.Long.toString(value));
    }
}
