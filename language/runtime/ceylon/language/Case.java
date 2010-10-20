package ceylon.language;

public interface Case<X> {
    /** Determine if the given value matches
        this case, returning {true} if the
        value matches. */
    public ceylon.language.Boolean test(X value);
}
