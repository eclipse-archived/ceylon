package ceylon.language;

public final class String implements Equality {
    public final java.lang.String value;

    private String(java.lang.String s) {
        value = s;
    }

    public java.lang.String toJavaString() {
        return value;
    }

    public static ceylon.language.String instance(java.lang.String s) {
        return new ceylon.language.String(s);
    }

    public ceylon.language.String uppercase() {
        return instance(value.toUpperCase());
    }

    public ceylon.language.String lowercase() {
        return instance(value.toLowerCase());
    }

	@Override
    public ceylon.language.Boolean equals(Equality that) {
		if (that instanceof String) {
			String s = (String)that;
			return Boolean.instance(value.equals(s.value));
		} else {
			return Boolean.instance(false);
		}
    }

    public static ceylon.language.String instance(java.lang.String... strings) {
        StringBuffer buf = new StringBuffer();
        for (java.lang.String s: strings)
            buf.append(s);
        return new ceylon.language.String(buf.toString());
    }

    public static ceylon.language.String instance(String... strings) {
        StringBuffer buf = new StringBuffer();
        for (String s: strings)
            buf.append(s.value);
        return new ceylon.language.String(buf.toString());
    }

}
