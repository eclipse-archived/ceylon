package ceylon.language;

public final class Optional<T> extends ceylon.language.Nothing {
    public Optional(T t) {
        this.t = t;
    }
    public T t;
    public T $internalErasedExists() {
        return t;
    }
}
