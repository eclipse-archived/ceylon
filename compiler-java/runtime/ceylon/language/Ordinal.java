package ceylon.language;

// FIXME: not parameterized in the spec
public interface Ordinal<O> {
    public O successor();
    public O predecessor();
}
