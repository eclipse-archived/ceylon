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

    public ceylon.Integer operatorPlus(ceylon.Integer op) {
        return instance(value + op.value);
    }

    public ceylon.String asString() {
        return ceylon.String.instance(java.lang.Long.toString(value));
    }
}
