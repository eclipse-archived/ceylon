shared extension class Flattenable<R,T,P...>(R this(T t)(P... args)) {
    R flatten(T t, P... p) {
        return this(t)(p)
    }
}