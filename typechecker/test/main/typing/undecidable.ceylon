void undecidable() {
    
    interface In<P> {}
    @error interface X satisfies In<X&In<X>> {}
    @error interface W satisfies Inv<W|Inv<W>> {}
    
    interface Co<out T> {}
    @error interface A satisfies Co<Inv<A&B&Co<Inv<A&B>>>> {}
    @error interface B satisfies Co<Co<B&A>> {}
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
    
    interface Sub satisfies In<E> {}
    interface SubList satisfies List<E> {}
    @error alias E=>D;
    @error alias D=>E;
    
}

void moreundecidable() {
    interface Co<out P> {}
    @error interface A satisfies Co<B&Co<A>> {}
    @error interface B satisfies Co<A&Co<B>> {}
    @error Co<A&B&Co<A&B>> foo(Co<A&B> co) => co;
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

void aliases() {
    @error interface L1<in T> => List<T>;
    @error alias L2<in T> => List<T>;
    @error class S1<in T>(T t) => Singleton<T>(t);
    @error alias S2<in T> => Singleton<T>;
    @error class SS<in T>(T t) extends Singleton<T>(t) {}
    @error interface LS<in T> satisfies List<T> {}
}

void broken() {
    interface Contra<in P> {}
    interface Inv<P> {}
    @error interface X satisfies Contra<Contra<X>> {}
    @error interface Y satisfies Contra<Contra<Y>> {}
    @error X&Contra<Y|Contra<X>> sub2(X&Contra<Y> sub) => sub;
    X&Contra<Y> sup2(X&Contra<Y|Contra<X>> sup) => sup;
    @error Inv<X&Contra<Y|Contra<X>>> foo2(Inv<X&Contra<Y>> foo) => foo;
}

void evenmore() {
    interface Inv<P> {}
    @error interface A satisfies Inv<B|Inv<B>> {}
    interface B satisfies A {}
    
    @error interface AA satisfies Inv<BB&CC> {}
    interface BB satisfies Inv<AA> {}
    interface CC satisfies AA {}
}