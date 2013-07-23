void undecidable() {
    
    interface In<P> {}
    @error interface X satisfies In<X&In<X>> {}
    
    interface Co<out T> {}
    @error interface A satisfies Co<Inv<A&B&Co<Inv<A&B>>>> {}
    interface B satisfies Co<Co<B&A>> {}
    class Inv<T>() {}
    @error Inv<A&B&Co<Inv<A>>> foo(Inv<A&B> bar) => bar;
    
    interface Int<P> {}
    @error interface Y satisfies Int<Y&IntY> {}
    alias IntY => Int<Y>;
    @error interface Z satisfies Int<ZIntZ> {}
    alias ZIntZ => Z&Int<Z>;
    
    interface Invar<P> {}
    interface G satisfies Invar<H> {}
    @error interface H satisfies Invar<G&H> {}
    
    @error interface Sub satisfies In<E> {}
    @error interface SubList satisfies List<E> {}
    @error alias E=>D;
    alias D=>E;
    
}

void decidable() {
    interface Co<out P> {}
    interface Contra<in P> {}    
    interface Inv<P> {}
    
    interface A satisfies Co<Co<A>> {}
    interface B satisfies Co<Co<B>> {}
    A&Co<B&Co<A>> sub1(A&Co<B> sub) => sub;
    A&Co<B> sup1(A&Co<B&Co<A>> sup) => sup;
    Inv<A&Co<B&Co<A>>> foo1(Inv<A&Co<B>> foo) => foo;
    
    interface X satisfies Contra<X> {}
    interface Y satisfies Contra<Y> {}
    X&Contra<Y|X> sub2(X&Contra<Y> sub) => sub;
    X&Contra<Y> sup2(X&Contra<Y|X> sup) => sup;
    Inv<X&Contra<Y|X>> foo2(Inv<X&Contra<Y>> foo) => foo;
}