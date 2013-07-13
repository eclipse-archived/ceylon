void undecidable() {

interface In<P> {}
@error interface X satisfies In<X&In<X>> {}

interface Co<out T> {}
@error interface A satisfies Co<Inv<A&B&Co<Inv<A&B>>>> {}
@error interface B satisfies Co<Co<B&A>> {}
class Inv<T>() {}
@error Inv<A&B&Co<Inv<A>>> foo(Inv<A&B> bar) => bar;

}