interface Functor<Fun> given Fun<Element> {
    shared formal Fun<Out> map<In,Out>
                (Out apply(In a))
                (Fun<In> inFun);
}

object listFunctor satisfies Functor<List> {
    shared actual List<Out> map<In,Out>
                (Out apply(In a))
                (List<In> inList) 
            => [ for (a in inList) apply(a) ];
}

object iterFunctor satisfies Functor<Iterable> {
    shared actual Iterable<Out> map<In,Out>
                (Out apply(In a))
                (Iterable<In> inIterable)
            => { for (a in inIterable) apply(a) };
}

Fun<String> toString<Fun>
            (Functor<Fun> functor)
            (Fun<Object> inFun) 
        given Fun<Element> 
        => functor.map(Object.string)(inFun);


void testFunctors() {
    value listToString = toString<List>(listFunctor);
    @type:"List<String>" 
    value strList = listToString([0, 0.0, 1, 1.0]);
    value iterToString = toString<Iterable>(iterFunctor);
    @type:"Iterable<String,Null>" 
    value strIter = iterToString({0, 0.0, 1, 1.0});
}

class X<T>() given T<U> {
    shared T<String> f(T<Integer> t) => nothing;
}

T<String> getF<T>(X<T> x, T<Integer> t) 
        given T<U> {
    return x.f(t);
}

void test() {
    @type:"Sequence<String>" 
    value ts = getF<Sequence>(X<Sequence>(), 
        ([1] of Sequence<Integer>));
    @type:"List<String>" 
    value ls = X<List>().f([1]);
    @type:"Sequence<String>" 
    value ss = X<Sequence>().f([1]);
    @error value es = X<Singleton>().f([1]);
    @type:"Singleton<String>" 
    value sss = X<Singleton>().f(Singleton(1));
    X<Singleton> xs1 = X<Singleton>();
    @error X<Singleton> xs2 = X<List>();
}

void fun<X>
        (X<String> strings) 
        given X<T> satisfies {T*} 
            given T satisfies Object {
    for (s in strings) {}
    @error X<Null> nulls;
}
        
class Blah<Element>() 
        satisfies Iterable<Element>
        given Element satisfies String {
    iterator() => nothing;
}

class Meh<Element>() {}

void testfun() {
    fun<List>(["", "", ""]);
    @error fun<Meh>(Meh());
    @error fun<Integer>(["", "", ""]);
    @error fun<Blah>(Blah());
}

class Dummy<X>() given X<T> {
    shared Element foo<Element>(X<Element> ds) => nothing;
}
void testDummy() {
    @type:"Integer" Dummy<List>().foo([1, 2, 3]);
}

interface High<out T> given T<X> given X satisfies String {}
interface U<X> given X satisfies Object {}
interface V<X> satisfies U<X> given X satisfies Object {}
interface W<X> satisfies U<X> given X satisfies String {}
interface T<X> given X satisfies Object {}
interface S<X> satisfies U<String> given X satisfies Object {}

void testSubtyping(High<U> h1, 
        High<V> h2, 
        High<W> h3, 
        High<U|V> h4, 
        High<T> h5, 
        High<S> h6) {
    High<U> ha = h1;
    High<U> hb = h2;
    @error High<U> hc = h3;
    High<U> hd = h4;
    @error High<U> he = h5;
    @error High<V> h = h1;
    @error High<U> hf = h6;
}
