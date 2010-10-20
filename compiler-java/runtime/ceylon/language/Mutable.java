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
    @SuppressWarnings("unchecked")
    public static final <T> Mutable<T> of(T t) {
        return new Mutable(t);
    }
    @SuppressWarnings("unchecked")
        public static final <N,T extends Numeric<N>> T postIncrement(Mutable<T> op) {
        Numeric<N> n = op.t;
        N m = n.succ();
        op.t = (T)m;
        return (T)n;
    }
    @SuppressWarnings("unchecked")
        public static final <N,T extends Numeric<N>> T postDecrement(Mutable<T> op) {
        Numeric<N> n = op.t;
        N m = n.succ();
        op.t = (T)m;
        return (T)n;
    }
    @SuppressWarnings("unchecked")
        public static final <N,T extends Numeric<N>> T preIncrement(Mutable<T> op) {
        Numeric<N> n = op.t;
        N m = n.succ();
        op.t = (T)m;
        return (T)n;
    }
    @SuppressWarnings("unchecked")
        public static final <N,T extends Numeric<N>> T preDecrement(Mutable<T> op) {
        Numeric<N> n = op.t;
        N m = n.succ();
        op.t = (T)m;
        return (T)n;
    }
}
