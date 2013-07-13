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