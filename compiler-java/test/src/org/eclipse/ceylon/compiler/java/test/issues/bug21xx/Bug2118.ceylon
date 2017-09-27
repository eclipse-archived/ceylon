shared interface Observer2118<T> { }

shared interface Observable2118<T> {
    
    shared default Observable2118<U> lift<U>() {
        return object satisfies Observable2118<U> { };
    }
    
    //shared class Foo<U>() satisfies Observer2118<U> { }
    //shared class Gee<T>() satisfies Observer2118<T> { }
    shared class Baz() satisfies Observer2118<T> { }
    shared class Bar<V>() satisfies Observer2118<V> { }
    
}