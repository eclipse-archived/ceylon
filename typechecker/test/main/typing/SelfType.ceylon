shared abstract class Comparable2<in Other>() of Other
        given Other satisfies Comparable2<Other> {
    
    shared formal Integer compare(Other that);
    
    shared Integer reverseCompare(Other that) { 
        return that.compare(this);
    }
    
}

class Foo(i)
        extends Comparable2<Foo>() {
    Integer i;
    shared actual Integer compare(Foo that) {
        return this.i - that.i;
    }
}

@error class Bar()
       extends Comparable2<Foo>() {
    shared actual Integer compare(Foo that) {
        @error return that.i;
    }
}

void test() {
    Comparable2<Foo> foo = Foo(+1);
    foo.compare(foo);
    Foo foo2 = foo;
}

class SelfTypeEquivalence1() {
    interface Self<T> of T {}
    class X() satisfies Self<X> {}
    interface Inv<T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
    Inv<Self<X>> l3 = l2;
    Inv<X> l4 = l1;
}

class SelfTypeEquivalence2() {
    interface Self<T> of T {}
    class X() {}
    interface Inv<T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
    @error Inv<Self<X>> l3 = l2;
    @error Inv<X> l4 = l1;
}

class SelfTypeEquivalence3() {
    interface Self<T> of T {}
    class X() {}
    interface Inv<out T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
    @error Inv<Self<X>> l3 = l2;
    Inv<X> l4 = l1;
}

class SelfTypeEquivalence4() {
    interface Self<T> of T {}
    class X() {}
    interface Inv<in T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
    Inv<Self<X>> l3 = l2;
    @error Inv<X> l4 = l1;
}