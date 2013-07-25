//interface Aaa satisfies Comparable<Ccc|Aaa> {}
//interface Ccc satisfies Comparable<Ccc|Aaa> {}

shared abstract class Comparable2<in Other>() of Other
        given Other satisfies Comparable2<Other> {
    
    shared formal Integer compare(Other that);
    
    shared Integer reverseCompare(Other that) { 
        return that.compare(this of Other);
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
    foo.compare(foo of Foo);
    Foo foo2 = foo of Foo;
}

class SelfTypeEquivalence1() {
    interface Self<T> of T {}
    class X() satisfies Self<X> {}
    interface Inv<T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
//    Inv<Self<X>> l3 = l2;
//    Inv<X> l4 = l1;
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
//    @error Inv<Self<X>> l3 = l2;
//    Inv<X> l4 = l1;
}

class SelfTypeEquivalence4() {
    interface Self<T> of T {}
    class X() {}
    interface Inv<in T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
//    Inv<Self<X>> l3 = l2;
//    @error Inv<X> l4 = l1;
}

interface Aa {}
@error interface Bb satisfies Comparable<Bb&Aa> {}
@error interface Cc satisfies Comparable<Cc|Aa> {}
@error interface Zz satisfies Comparable<Bb|Cc|Aa> {}

interface A satisfies Comparable<C|A> {}
interface C satisfies Comparable<C|A> {}

void testOf(Comparable<C|A> comp, Anything vd) {
    A|C ac = comp of C|A;
    Object? maybe = vd of Object|Null;
}

interface Comp<in T> of T
        given T satisfies Comp<T> {
    shared formal Comparison compare(T other);
}

interface Scal<in T> of T
        satisfies Comp<T> 
        given T satisfies Scal<T> {}

class CompBar() satisfies Comp<CompBar> {
    shared actual Comparison compare(CompBar other) {
        return nothing;
    }
}

@error class CompFoo() satisfies Comp<CompBar> {
    shared actual Comparison compare(CompBar other) {
        return nothing;
    }
}

@error class Broken<T>() of T 
        satisfies Comparable<T> 
        given T satisfies Comparable<T>{
    shared actual Comparison compare(T other) {
        return nothing;
    }
}

