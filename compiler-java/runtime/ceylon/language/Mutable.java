package ceylon.language;

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
    public static final <T> Mutable<T> of(T t) {
        return new Mutable<T>(t);
    }
    @SuppressWarnings("unchecked")
    public static final <O,T extends Ordinal<O>> T postIncrement(Mutable<T> op) {
        Ordinal<O> n = op.t;
        O m = n.successor();
        op.t = (T)m;
        return (T)n;
    }
    @SuppressWarnings("unchecked")
    public static final <O,T extends Ordinal<O>> T postDecrement(Mutable<T> op) {
        Ordinal<O> n = op.t;
        O m = n.predecessor();
        op.t = (T)m;
        return (T)n;
    }
    @SuppressWarnings("unchecked")
    public static final <O,T extends Ordinal<O>> T preIncrement(Mutable<T> op) {
        Ordinal<O> n = op.t;
        O m = n.successor();
        op.t = (T)m;
        return (T)m;
    }
    @SuppressWarnings("unchecked")
    public static final <O,T extends Ordinal<O>> T preDecrement(Mutable<T> op) {
        Ordinal<O> n = op.t;
        O m = n.predecessor();
        op.t = (T)m;
        return (T)m;
    }
}
