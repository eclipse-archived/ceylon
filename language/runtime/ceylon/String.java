package ceylon;

public final class String extends Object
{
    public final java.lang.String value;

    private String(java.lang.String s) {
        value = s;
    }

    public java.lang.String toJavaString() {
        return value;
    }

    public static ceylon.String instance(java.lang.String s) {
        return new ceylon.String(s);
    }

    public ceylon.String string() {
        return this;
    }
}
