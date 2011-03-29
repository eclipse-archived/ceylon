doc "allows the first parameter of any function with multiple parameters to be curried,
     transforming a |Callable<R,T,P...>| into a |Callable<Callable<R,P...>,T>|."
shared extension class Curryable<R,T,P...>(R this(T t, P... args)) {
    R partial(T t)(P... p) {
        R curried(P... p) {
            return this(t, args);
        }
        return curried;
    }
}