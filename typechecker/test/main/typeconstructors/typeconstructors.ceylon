interface Functor<@Fun> given Fun<Element> {
    shared formal Fun<Out> map<In,Out>(Out apply(In a))(Fun<In> inFun);
}

object listFunctor satisfies Functor<@List> {
    shared actual List<Out> map<In,Out>(Out apply(In a))(List<In> inList) =>
            [ for (a in inList) apply(a) ];
}

object iterFunctor satisfies Functor<@Iterable> {
    shared actual Iterable<Out> map<In,Out>(Out apply(In a))(Iterable<In> inIterable)
            => { for (a in inIterable) apply(a) };
}

Fun<String> toString<@Fun>(Functor<@Fun> functor)(Fun<Object> inFun) 
        given Fun<Element> 
    => functor.map<Object,String>(Object.string)(inFun);


void testFunctors() {
    value listToString = toString<@List>(listFunctor);
    @type:"List<String>" value strList = listToString([0, 0.0, 1, 1.0]);
    value iterToString = toString<@Iterable>(iterFunctor);
    @type:"Iterable<String,Null>" value strIter = iterToString({0, 0.0, 1, 1.0});
}

class X<@T>() given T<U> {
    shared T<String> f(T<Integer> t) => nothing;
}

T<String> getF<@T>(X<@T> x, T<Integer> t) given T<U> {
    return x.f(t);
}

void test() {
    @type:"Sequence<String>" value ts = getF<@Sequence>(X<@Sequence>(), ([1] of Sequence<Integer>));
    @type:"List<String>" value ls = X<@List>().f([1]);
    @type:"Sequence<String>" value ss = X<@Sequence>().f([1]);
    @error value es = X<@Singleton>().f([1]);
    @type:"Singleton<String>" value sss = X<@Singleton>().f(Singleton(1));
    X<@Singleton> xs1 = X<@Singleton>();
    @error X<Singleton> xs2 = X<@List>();
}