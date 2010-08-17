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

    public ceylon.Integer remainder(ceylon.Integer op) {
        return instance(value % op.value);
    }

    public ceylon.Integer power(ceylon.Integer op) {
        return instance((long) Math.pow(value, op.value)); // FIXME: ugly
    }

    public ceylon.Integer and(ceylon.Integer op) {
        return instance(value & op.value);
    }

    public ceylon.Integer or(ceylon.Integer op) {
        return instance(value | op.value);
    }

    public ceylon.Integer xor(ceylon.Integer op) {
        return instance(value ^ op.value);
    }

    public ceylon.String asString() {
        return ceylon.String.instance(java.lang.Long.toString(value));
    }
}
