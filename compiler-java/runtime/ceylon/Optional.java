package ceylon;

public final class Optional<T> extends ceylon.Nothing {
    public Optional(T t) {
        this.t = t;
    }
    public T t;
    public T $internalErasedExists() {
        return t;
    }
}
