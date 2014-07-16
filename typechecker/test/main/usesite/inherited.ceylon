interface InvariantSupertype {
    
    interface List<T> {
        shared formal Iterator<T> iterator();
        shared formal T? get(Integer index);
        shared formal T? first;
        shared formal void add(T element);
    }
    
    interface Array<T> satisfies List<T> {}
    
    void testInheritance<X>(X x, Array<out X> outArray, Array<in X> inArray) {
        Iterator<X> outIt = outArray.iterator();
        @error Iterator<X> inIt = inArray.iterator();
        X? outFirst = outArray.first;
        @error X? inFirst = inArray.first;
        X? outGet = outArray.get(0);
        @error X? inGet = inArray.get(0);
        inArray.add(x);
        @error outArray.add(x);
    }
    
}


interface CovariantSupertype {
    
    interface List<out T> {
        shared formal Iterator<T> iterator();
        shared formal T? get(Integer index);
        shared formal T? first;
    }
    
    interface Array<T> satisfies List<T> {
        shared formal void add(T element);
    }
    
    void testInheritance<X>(X x, Array<out X> outArray, Array<in X> inArray) {
        Iterator<X> outIt = outArray.iterator();
        @error Iterator<X> inIt = inArray.iterator();
        X? outFirst = outArray.first;
        @error X? inFirst = inArray.first;
        X? outGet = outArray.get(0);
        @error X? inGet = inArray.get(0);
        inArray.add(x);
        @error outArray.add(x);
    }
    
}