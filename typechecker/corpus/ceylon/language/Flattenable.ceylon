doc "Reverse of a currying operation. Transform a |Callable<Callable<R,P...>,T>| into a |Callable<R,T,P...>|."
shared extension class Flattenable<R,T,P...>(R this(T t)(P... args)) {
    R flatten(T t, P... p) {
        return this(t)(p);
    }
}