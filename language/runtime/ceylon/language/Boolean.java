package ceylon.language;

public final class Boolean extends Object {
    private final boolean value;

    private Boolean(boolean b) {
        value = b;
    }

    public static ceylon.language.Boolean instance(boolean b) {
        return new ceylon.language.Boolean(b);
    }

    @Extension
    public ceylon.language.String string() {
        return ceylon.language.String.instance(java.lang.Boolean.toString(value));
    }

    @Extension
    public boolean booleanValue() {
        return value;
    }
}
