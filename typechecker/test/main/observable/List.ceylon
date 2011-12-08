class List<T>() 
        extends Object()
        satisfies Sequence<T> {
    shared void add(T t) {}
    shared actual Natural lastIndex {
        return 0;
    }
    shared actual List<T> clone {
        throw;
    }
    shared actual T? item(Natural n) {
        return null;
    }
    shared actual T[] rest {
        return this;
    }
    shared actual T[] segment(Natural from, Natural length) {
        return this;
    }
    shared actual T first {
        throw;
    }
    shared actual T[] span(Natural from, Natural? to) {
        return this;
    }
}