class List<T>() 
        extends Value()
        satisfies Sequence<T> {
    shared void add(T t) {}
    shared actual Integer lastIndex {
        return 0;
    }
    shared actual List<T> clone {
        return this;
    }
    shared actual T? item(Integer n) {
        return null;
    }
    shared actual T[] rest {
        return this;
    }
    shared actual List<T> reversed {
        return this;
    }
    shared actual T[] segment(Integer from, Integer length) {
        return this;
    }
    shared actual T first {
        throw;
    }
    shared actual T last {
        throw;
    }
    shared actual T[] span(Integer from, Integer to) {
        return this;
    }
    shared actual T[] spanFrom(Integer from) {
        return this;
    }
    shared actual T[] spanTo(Integer to) {
        return this;
    }
}