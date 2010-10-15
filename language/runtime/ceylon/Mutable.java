package ceylon;

public final class Mutable<T> {
    private T t;
    public Mutable(T t) {
        set(t);
    }
    public final void set(T t) {
        this.t = t;
    }
    @Extension
    public final T get() {
        return t;
    }
    @SuppressWarnings("unchecked")
    public static final <T> Mutable<T> of(T t) {
        return new Mutable(t);
    }
}
