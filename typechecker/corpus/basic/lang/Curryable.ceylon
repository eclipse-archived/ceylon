shared extension class Curryable<R,T,P...>(R this(T t, P... args)) {
    R partial(T t)(P... p) {
        R curried(P... p) {
            return this(t, args)
        }
        return curried
    }
}