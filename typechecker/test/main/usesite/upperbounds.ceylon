T funWithWildcardUpperBound<T>(T t) 
        given T satisfies Boxx<in Integer> {
    t.set(1);
    $type:"Anything" value tt = t.get();
    return t;
}

interface Boxx<T> {
    shared formal T? get();
    shared formal void set(T t);
}

class Mut1() satisfies Boxx<Integer> {
    variable Integer i = 0;
    get() => i;
    set(Integer t) => i = t;
}

class Mut2() satisfies Boxx<Object> {
    variable Object? o = null;
    get() => o;
    set(Object t) => o = t;
}

void wildcardUpperBounds() {
    $type:"Mut1" funWithWildcardUpperBound(Mut1());    
    $type:"Mut2" funWithWildcardUpperBound(Mut2());
}
