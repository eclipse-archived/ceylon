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
}