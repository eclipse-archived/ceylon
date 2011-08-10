package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;

@SatisfiedTypes({
    "ceylon.language.Equality",
    "ceylon.language.Comparable<ceylon.language.String>"
})
public final class String extends Object implements Equality, Comparable<String> {
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

    public java.lang.String uppercase() {
        return value.toUpperCase();
    }

    public java.lang.String lowercase() {
        return value.toLowerCase();
    }

	@Override
    public boolean equals(Equality that) {
		if (that instanceof String) {
			String s = (String)that;
			return value.equals(s.value);
		} else {
			return false;
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

    @Override
    public Comparison compare(String other) {
        int c = value.compareTo(other.value);
        return (c < 0) ? Comparison.SMALLER :
            ((c == 0) ? Comparison.EQUAL : Comparison.LARGER);
    }

}
