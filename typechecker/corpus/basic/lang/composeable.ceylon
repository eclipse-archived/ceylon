shared extension Y composeable<X,Y,P...>(Y this(X x))(X g(P... args))(P... args) {
    Y compose(X g(P... args))(P... args) {
        Y composition(P... args) {
            return this(g(args));
        }
        return composition;
    }
    return compose;
}