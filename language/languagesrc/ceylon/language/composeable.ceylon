doc "The following extension provides function composition,
     combining a |Callable<Y,X>| with a |Callable<X,P...>| to produce a |Callable<Y,P...>|."
shared extension Y composeable<X,Y,P...>(Y this(X x))(X g(P... args))(P... args) {
    Y compose(X g(P... args))(P... args) {
        Y composition(P... args) {
            return this(g(args));
        }
        return composition;
    }
    return compose;
}