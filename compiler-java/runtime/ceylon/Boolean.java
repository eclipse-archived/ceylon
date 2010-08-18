package ceylon;

public final class Boolean
{
    private final boolean value;

    private Boolean(boolean b) {
        value = b;
    }

    public static ceylon.Boolean instance(boolean b) {
        return new ceylon.Boolean(b);
    }

    public ceylon.String asString() {
        return ceylon.String.instance(java.lang.Boolean.toString(value));
    }
}
