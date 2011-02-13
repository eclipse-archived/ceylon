shared interface EnumerableSequence<X>
        satisfies Callable<X[],X...> 
        given X satisfies Equality<X> {
    shared actual default X[] call(X... elements) {
        return elements;
    }
}